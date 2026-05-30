package com.emarket.dto.pedido;

import com.emarket.entity.EstadoPedido;

import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponseDto(
        Long id,
        Long idCliente,
        String usernameCliente,
        LocalDateTime fechaCreacion,
        EstadoPedido estadoActual,
        Double total,
        List<ItemPedidoResponseDto> items
) {
}
