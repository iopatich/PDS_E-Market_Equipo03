package com.emarket.mapper;

import com.emarket.dto.varianteProducto.VarianteProductoRequestDto;
import com.emarket.dto.varianteProducto.VarianteProductoResponseDto;
import com.emarket.entity.Producto;
import com.emarket.entity.VarianteProducto;

public class VarianteProductoMapper {
    private VarianteProductoMapper() {}

    public static VarianteProducto toEntity(VarianteProductoRequestDto dto, Producto producto) {
        VarianteProducto varianteProducto = new VarianteProducto();
        varianteProducto.setColor(dto.color());
        varianteProducto.setStock(dto.stock());
        varianteProducto.setPrecioAdicional(dto.precio());
        varianteProducto.setProducto(producto);
        return varianteProducto;
    }

    public static VarianteProductoResponseDto toResponseDto(VarianteProducto varianteProducto, Double precioFinal) {
        return new VarianteProductoResponseDto(
                varianteProducto.getId(),
                varianteProducto.getColor(),
                varianteProducto.getStock(),
                varianteProducto.getPrecioAdicional(),
                precioFinal
        );
    }
}