package com.emarket.service.implementation;

import com.emarket.dto.varianteProducto.VarianteProductoRequestDto;
import com.emarket.dto.varianteProducto.VarianteProductoResponseDto;
import com.emarket.entity.Producto;
import com.emarket.entity.VarianteProducto;
import com.emarket.exception.RecursoNoEncontradoException;
import com.emarket.exception.StockInsuficienteException;
import com.emarket.mapper.VarianteProductoMapper;
import com.emarket.repository.ProductoRepository;
import com.emarket.entity.Permiso;
import com.emarket.repository.VarianteProductoRepository;
import com.emarket.service.interfaz.AuthService;
import com.emarket.service.interfaz.VarianteProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VarianteProductoServiceImpl implements VarianteProductoService {
    private final VarianteProductoRepository varianteProductoRepository;
    private final ProductoRepository productoRepository;
    private final AuthService authService;

    @Override
    @Transactional
    public VarianteProductoResponseDto crear(VarianteProductoRequestDto dto, String token) {
        authService.validarPermiso(token, Permiso.CARGAR_PRODUCTO);
        Producto producto = buscarProductoActivo(dto.idProducto());
        VarianteProducto guardada = varianteProductoRepository.save(VarianteProductoMapper.toEntity(dto, producto));
        return VarianteProductoMapper.toResponseDto(guardada, calcularPrecioFinal(guardada));
    }

    @Override
    @Transactional(readOnly = true)
    public List<VarianteProductoResponseDto> listar() {
        return varianteProductoRepository.findAllByActivoTrue()
                .stream()
                .map(varianteProducto -> VarianteProductoMapper
                        .toResponseDto(
                                varianteProducto, calcularPrecioFinal(varianteProducto)))
                .toList();
    }

    @Override
    @Transactional
    public VarianteProductoResponseDto actualizar(Long id, VarianteProductoRequestDto dto, String token) {
        authService.validarPermiso(token, Permiso.GESTIONAR_PRODUCTOS);
        VarianteProducto varianteProducto = buscarVarianteActiva(id);
        Producto producto = buscarProductoActivo(dto.idProducto());

        varianteProducto.setColor(dto.color());
        varianteProducto.setStock(dto.stock());
        varianteProducto.setPrecioAdicional(dto.precio());
        varianteProducto.setProducto(producto);

        VarianteProducto actualizada = varianteProductoRepository.save(varianteProducto);
        return VarianteProductoMapper.toResponseDto(actualizada, calcularPrecioFinal(actualizada));
    }

    @Override
    @Transactional
    public VarianteProductoResponseDto eliminar(Long id, String token) {
        authService.validarPermiso(token, Permiso.GESTIONAR_PRODUCTOS);
        VarianteProducto varianteProducto = buscarVarianteActiva(id);
        varianteProducto.setActivo(false);
        return VarianteProductoMapper.toResponseDto(varianteProductoRepository.save(varianteProducto), calcularPrecioFinal(varianteProducto));
    }

    @Override
    public Double calcularPrecioFinal(VarianteProducto variante) {
        return variante.getProducto().getPrecioBase() + variante.getPrecioAdicional();
    }

    @Override
    @Transactional
    public VarianteProductoResponseDto reducirStock(Long id, Integer cantidad, String token) {
        authService.validarPermiso(token, Permiso.REALIZAR_COMPRA);
        VarianteProducto varianteProducto = buscarVarianteActiva(id);

        ajustarStock(varianteProducto, cantidad);

        VarianteProducto actualizada = varianteProductoRepository.save(varianteProducto);
        return VarianteProductoMapper.toResponseDto(actualizada, calcularPrecioFinal(actualizada));
    }

    private void ajustarStock(VarianteProducto varianteProducto, Integer cantidad) {
        if (varianteProducto.getStock() < cantidad) {
            throw new StockInsuficienteException(
                    "No hay stock suficiente"
            );
        }
        varianteProducto.setStock(varianteProducto.getStock() - cantidad);
    }

    private VarianteProducto buscarVarianteActiva(Long id) {
        VarianteProducto varianteProducto = varianteProductoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se ha encontrado la variante de producto con el id " + id
                ));
        if (!Boolean.TRUE.equals(varianteProducto.getActivo())) {
            throw new RecursoNoEncontradoException("La variante seleccionada no se encuentra disponible");
        }
        return varianteProducto;
    }

    private Producto buscarProductoActivo(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se ha encontrado el producto con el id " + id
                ));
        if (!Boolean.TRUE.equals(producto.getActivo())) {
            throw new RecursoNoEncontradoException("El producto seleccionado no se encuentra disponible");
        }
        return producto;
    }
}