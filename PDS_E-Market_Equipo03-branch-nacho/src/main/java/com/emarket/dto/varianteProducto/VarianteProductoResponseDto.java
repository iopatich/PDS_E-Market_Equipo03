package com.emarket.dto.varianteProducto;

public record VarianteProductoResponseDto (
        Long id,
        String color,
        Integer stock,
        Double precioAdicional,
        Double precioFinal
){
}
