package com.emarket.notificacion;

import com.emarket.entity.EstadoPedido;
import com.emarket.entity.Pedido;

public record PedidoEstadoActualizadoEvent(
        Pedido pedido,
        EstadoPedido estadoAnterior,
        EstadoPedido estadoNuevo,
        String mensaje
) {
}
