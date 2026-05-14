package com.emarket.service.implementation;

import com.emarket.dto.varianteProducto.VarianteProductoRequestDto;
import com.emarket.dto.varianteProducto.VarianteProductoResponseDto;
import com.emarket.entity.Producto;
import com.emarket.entity.VarianteProducto;
import com.emarket.mapper.VarianteProductoMapper;
import com.emarket.repository.ProductoRepository;
import com.emarket.repository.VarianteProductoRepository;
import com.emarket.service.interfaz.VarianteProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VarianteProductoServiceImpl implements VarianteProductoService {
    private final VarianteProductoRepository varianteProductoRepository;
    private final ProductoRepository productoRepository;

    @Override
    public VarianteProductoResponseDto crear(VarianteProductoRequestDto dto) {
        Producto producto = productoRepository.findById(dto.idProducto()).orElseThrow();
        VarianteProducto guardada = varianteProductoRepository.save(VarianteProductoMapper.toEntity(dto, producto));
        return VarianteProductoMapper.toResponseDto(guardada, calcularPrecioFinal(guardada));
    }

    @Override
    public List<VarianteProductoResponseDto> listar() {
        return varianteProductoRepository.findAllByActivoTrue()
                .stream()
                .map(varianteProducto -> VarianteProductoMapper
                        .toResponseDto(
                                varianteProducto, calcularPrecioFinal(varianteProducto)))
                .toList();
    }

    @Override
    public VarianteProductoResponseDto eliminar(Long id) {
        VarianteProducto varianteProducto = varianteProductoRepository.findById(id).orElseThrow();
        varianteProducto.setActivo(false);
        return VarianteProductoMapper.toResponseDto(varianteProductoRepository.save(varianteProducto), calcularPrecioFinal(varianteProducto));
    }

    @Override
    public Double calcularPrecioFinal(VarianteProducto variante) {
        return variante.getProducto().getPrecioBase() + variante.getPrecioAdicional();
    }

    @Override
    public VarianteProductoResponseDto reducirStock(Long id, Integer cantidad) {
        VarianteProducto varianteProducto = varianteProductoRepository.findById(id).orElseThrow();
        if (varianteProducto.getStock() < cantidad) throw new RuntimeException("Stock insuficiente");
        varianteProducto.setStock(varianteProducto.getStock() - cantidad);
        VarianteProducto actualizada = varianteProductoRepository.save(varianteProducto);
        return VarianteProductoMapper.toResponseDto(actualizada, calcularPrecioFinal(actualizada));
    }
}
