package com.emarket.pedido;

import com.emarket.entity.EstadoPedido;
import com.emarket.entity.Pedido;

public interface EstadoPedidoState {
    EstadoPedido getEstado();
    EstadoPedido siguienteEstado(Pedido pedido);
}
