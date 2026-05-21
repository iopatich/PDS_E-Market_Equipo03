package com.emarket.dto.carrito;

public record ItemCarritoResponseDto(
        Long id,
        Long idVarianteProducto,
        String nombreProducto,
        String color,
        Integer cantidad,
        Double precioUnitario,
        Double subtotal
) {
}
