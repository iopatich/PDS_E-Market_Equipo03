package com.emarket.pedido;

import com.emarket.entity.EstadoPedido;
import com.emarket.entity.Pedido;

public class EstadoPedidoEnviadoState implements EstadoPedidoState {
    @Override
    public void siguienteEstado(Pedido pedido) {
        pedido.setEstadoActual(EstadoPedido.ENTREGADO);
        pedido.setEstadoState(new EstadoPedidoEntregadoState());
    }
}