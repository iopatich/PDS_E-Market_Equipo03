package com.emarket.service.implementation;

import com.emarket.dto.carrito.ActualizarCantidadCarritoRequestDto;
import com.emarket.dto.carrito.AgregarItemCarritoRequestDto;
import com.emarket.dto.carrito.CarritoResponseDto;
import com.emarket.dto.pedido.PedidoResponseDto;
import com.emarket.entity.*;
import com.emarket.exception.OperacionInvalidaException;
import com.emarket.exception.RecursoNoEncontradoException;
import com.emarket.exception.StockInsuficienteException;
import com.emarket.mapper.CarritoMapper;
import com.emarket.mapper.PedidoMapper;
import com.emarket.repository.CarritoRepository;
import com.emarket.repository.ItemCarritoRepository;
import com.emarket.repository.PedidoRepository;
import com.emarket.repository.VarianteProductoRepository;
import com.emarket.service.interfaz.AuthService;
import com.emarket.service.interfaz.CarritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CarritoServiceImpl implements CarritoService {

    private final CarritoRepository carritoRepository;
    private final ItemCarritoRepository itemCarritoRepository;
    private final VarianteProductoRepository varianteProductoRepository;
    private final PedidoRepository pedidoRepository;
    private final AuthService authService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional(readOnly = true)
    public CarritoResponseDto verCarrito(String token) {
        return CarritoMapper.toResponseDto(obtenerCarritoDelCliente(token));
    }

    @Override
    @Transactional
    public CarritoResponseDto agregarItem(String token, AgregarItemCarritoRequestDto dto) {
        Carrito carrito = obtenerCarritoDelCliente(token);
        VarianteProducto variante = buscarVariante(dto.idVarianteProducto());

        validarVarianteDisponible(variante);
        validarStock(variante, dto.cantidad());

        ItemCarrito item = itemCarritoRepository.findByCarritoIdAndVarianteProductoId(carrito.getId(), variante.getId())
                .orElseGet(() -> {
                    ItemCarrito nuevo = new ItemCarrito();
                    nuevo.setVarianteProducto(variante);
                    nuevo.setCantidad(0);
                    carrito.agregarItem(nuevo);
                    return nuevo;
                });

        int nuevaCantidad = item.getCantidad() + dto.cantidad();
        validarStock(variante, nuevaCantidad);
        item.setCantidad(nuevaCantidad);

        carritoRepository.save(carrito);
        return CarritoMapper.toResponseDto(carrito);
    }

    @Override
    @Transactional
    public CarritoResponseDto actualizarCantidad(String token, Long itemId, ActualizarCantidadCarritoRequestDto dto) {
        Carrito carrito = obtenerCarritoDelCliente(token);
        ItemCarrito item = itemCarritoRepository.findByIdAndCarritoId(itemId, carrito.getId())
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontro el item del carrito con el id " + itemId));

        validarStock(item.getVarianteProducto(), dto.cantidad());
        item.setCantidad(dto.cantidad());
        itemCarritoRepository.save(item);
        return CarritoMapper.toResponseDto(carrito);
    }

    @Override
    @Transactional
    public CarritoResponseDto eliminarItem(String token, Long itemId) {
        Carrito carrito = obtenerCarritoDelCliente(token);
        ItemCarrito item = itemCarritoRepository.findByIdAndCarritoId(itemId, carrito.getId())
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontro el item del carrito con el id " + itemId));

        carrito.eliminarItem(item);
        carritoRepository.save(carrito);
        return CarritoMapper.toResponseDto(carrito);
    }

    @Override
    @Transactional
    public PedidoResponseDto confirmarCompra(String token) {
        authService.validarPermiso(token, Permiso.REALIZAR_COMPRA);
        Carrito carrito = obtenerCarritoDelCliente(token);

        if (carrito.getItems().isEmpty()) {
            throw new OperacionInvalidaException("No se puede confirmar una compra con el carrito vacio");
        }

        Pedido pedido = new Pedido();
        pedido.setCliente(carrito.getCliente());
        pedido.setFechaCreacion(LocalDateTime.now());
        pedido.setEstadoActual(EstadoPedido.PENDIENTE);

        double total = 0d;
        for (ItemCarrito itemCarrito : carrito.getItems()) {
            VarianteProducto variante = itemCarrito.getVarianteProducto();
            validarVarianteDisponible(variante);
            validarStock(variante, itemCarrito.getCantidad());
            variante.setStock(variante.getStock() - itemCarrito.getCantidad());

            double precioUnitario = variante.getProducto().getPrecioBase() + variante.getPrecioAdicional();
            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setVarianteProducto(variante);
            itemPedido.setNombreProducto(variante.getProducto().getNombre());
            itemPedido.setColorVariante(variante.getColor());
            itemPedido.setCantidad(itemCarrito.getCantidad());
            itemPedido.setPrecioUnitario(precioUnitario);
            itemPedido.setSubtotal(precioUnitario * itemCarrito.getCantidad());
            pedido.agregarItem(itemPedido);
            total += itemPedido.getSubtotal();
        }

        pedido.setTotal(total);
        Pedido guardado = pedidoRepository.save(pedido);

        carrito.getItems().clear();
        carritoRepository.save(carrito);

        publicarCambioEstado(guardado, null, EstadoPedido.PENDIENTE);
        return PedidoMapper.toResponseDto(guardado);
    }

    private Carrito obtenerCarritoDelCliente(String token) {
        authService.validarPermiso(token, Permiso.GESTIONAR_CARRITO);
        Usuario usuario = authService.obtenerUsuarioAutenticado(token);
        if (!(usuario instanceof Cliente cliente)) {
            throw new OperacionInvalidaException("El carrito solo puede ser gestionado por clientes");
        }

        return carritoRepository.findByCliente(cliente)
                .orElseGet(() -> {
                    Carrito carrito = new Carrito();
                    carrito.setCliente(cliente);
                    return carritoRepository.save(carrito);
                });
    }

    private VarianteProducto buscarVariante(Long idVarianteProducto) {
        return varianteProductoRepository.findById(idVarianteProducto)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro la variante de producto con el id " + idVarianteProducto
                ));
    }

    private void validarVarianteDisponible(VarianteProducto variante) {
        if (!Boolean.TRUE.equals(variante.getActivo())) {
            throw new OperacionInvalidaException("La variante seleccionada no se encuentra disponible");
        }
    }

    private void validarStock(VarianteProducto variante, Integer cantidad) {
        if (variante.getStock() < cantidad) {
            throw new StockInsuficienteException("No hay stock suficiente para la cantidad solicitada");
        }
    }

    private void publicarCambioEstado(Pedido pedido, EstadoPedido anterior, EstadoPedido nuevo) {
        applicationEventPublisher.publishEvent(new PedidoEstadoActualizadoEvent(
                pedido,
                anterior,
                nuevo,
                "El pedido " + pedido.getId() + " ahora se encuentra en estado " + nuevo
        ));
    }
}
