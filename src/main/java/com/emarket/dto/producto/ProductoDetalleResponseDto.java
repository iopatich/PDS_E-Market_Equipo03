package com.emarket.dto.producto;

import com.emarket.dto.varianteProducto.VarianteProductoResponseDto;

import java.util.List;

public record ProductoDetalleResponseDto(
        Long id,
        String nombre,
        String descripcion,
        Double precioBase,
        String nombreCategoriaPadre,
        List<VarianteProductoResponseDto> variantes
) {
}
