package com.emarket.dto.carrito;

import java.util.List;

public record CarritoResponseDto(
        Long id,
        Long idCliente,
        List<ItemCarritoResponseDto> items,
        Double total
) {
}
