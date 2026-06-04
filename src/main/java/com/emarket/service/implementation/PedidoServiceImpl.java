package com.emarket.service.implementation;

import com.emarket.dto.notificacion.NotificacionResponseDto;
import com.emarket.dto.pedido.PedidoResponseDto;
import com.emarket.entity.*;
import com.emarket.exception.AccesoDenegadoException;
import com.emarket.exception.RecursoNoEncontradoException;
import com.emarket.mapper.NotificacionMapper;
import com.emarket.mapper.PedidoMapper;
import com.emarket.notificacion.PedidoEstadoActualizadoEvent;
import com.emarket.pedido.EstadoPedidoResolver;
import com.emarket.repository.NotificacionRepository;
import com.emarket.repository.PedidoRepository;
import com.emarket.service.interfaz.AuthService;
import com.emarket.service.interfaz.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final NotificacionRepository notificacionRepository;
    private final AuthService authService;
    private final EstadoPedidoResolver estadoPedidoResolver;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponseDto> listarMisPedidos(String token) {
        Usuario usuario = authService.validarPermiso(token, Permiso.REALIZAR_COMPRA);
        if (!(usuario instanceof Cliente cliente)) {
            throw new AccesoDenegadoException("Solo un cliente puede consultar sus propios pedidos");
        }
        return pedidoRepository.findByClienteOrderByFechaCreacionDesc(cliente).stream()
                .map(PedidoMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponseDto> listarPedidos(String token) {
        authService.validarPermiso(token, Permiso.ACTUALIZAR_ESTADO_PEDIDO);
        return pedidoRepository.findAllByOrderByFechaCreacionDesc().stream()
                .map(PedidoMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public PedidoResponseDto avanzarEstado(Long pedidoId, String token) {
        authService.validarPermiso(token, Permiso.ACTUALIZAR_ESTADO_PEDIDO);
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontro el pedido con el id " + pedidoId));

        EstadoPedido anterior = pedido.getEstadoActual();
        EstadoPedido nuevo = estadoPedidoResolver.resolve(anterior).siguienteEstado(pedido);
        pedido.setEstadoActual(nuevo);

        Pedido actualizado = pedidoRepository.save(pedido);
        applicationEventPublisher.publishEvent(new PedidoEstadoActualizadoEvent(
                actualizado,
                anterior,
                nuevo,
                "El pedido " + actualizado.getId() + " cambio de estado: " + anterior + " -> " + nuevo
        ));
        return PedidoMapper.toResponseDto(actualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificacionResponseDto> listarNotificaciones(Long pedidoId, String token) {
        authService.validarPermiso(token, Permiso.ACTUALIZAR_ESTADO_PEDIDO);
        pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontro el pedido con el id " + pedidoId));

        return notificacionRepository.findByPedidoIdOrderByFechaEnvioAsc(pedidoId).stream()
                .map(NotificacionMapper::toResponseDto)
                .toList();
    }
}
