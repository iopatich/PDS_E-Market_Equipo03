package com.emarket.dto.pedido;

public record ItemPedidoResponseDto(
        Long idVarianteProducto,
        String nombreProducto,
        String color,
        Integer cantidad,
        Double precioUnitario,
        Double subtotal
) {
}
