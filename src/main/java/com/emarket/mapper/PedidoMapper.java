package com.emarket.mapper;

import com.emarket.dto.pedido.ItemPedidoResponseDto;
import com.emarket.dto.pedido.PedidoResponseDto;
import com.emarket.entity.ItemPedido;
import com.emarket.entity.Pedido;

import java.util.List;

public class PedidoMapper {

    private PedidoMapper() {
    }

    public static PedidoResponseDto toResponseDto(Pedido pedido) {
        List<ItemPedidoResponseDto> items = pedido.getItems().stream()
                .map(PedidoMapper::toItemResponseDto)
                .toList();

        return new PedidoResponseDto(
                pedido.getId(),
                pedido.getCliente().getId(),
                pedido.getCliente().getUsername(),
                pedido.getFechaCreacion(),
                pedido.getEstadoActual(),
                pedido.getTotal(),
                items
        );
    }

    public static ItemPedidoResponseDto toItemResponseDto(ItemPedido item) {
        return new ItemPedidoResponseDto(
                item.getVarianteProducto().getId(),
                item.getNombreProducto(),
                item.getColorVariante(),
                item.getCantidad(),
                item.getPrecioUnitario(),
                item.getSubtotal()
        );
    }
}
