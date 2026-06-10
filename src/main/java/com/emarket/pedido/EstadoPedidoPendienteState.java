package com.emarket.pedido;

import com.emarket.entity.EstadoPedido;
import com.emarket.entity.Pedido;

public class EstadoPedidoPendienteState implements EstadoPedidoState {
    @Override
    public void siguienteEstado(Pedido pedido) {
        pedido.setEstadoActual(EstadoPedido.PAGADO); // Persistencia BD
        pedido.setEstadoState(new EstadoPedidoPagadoState()); // Transición de estado GoF
    }
}