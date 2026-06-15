package com.emarket.pedido;

import com.emarket.entity.Pedido;

public interface EstadoPedidoState {
    void siguienteEstado(Pedido pedido);
}