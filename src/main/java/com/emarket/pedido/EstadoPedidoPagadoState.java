package com.emarket.pedido;

import com.emarket.entity.EstadoPedido;
import com.emarket.entity.Pedido;

public class EstadoPedidoPagadoState implements EstadoPedidoState {
    @Override
    public void siguienteEstado(Pedido pedido) {
        pedido.setEstadoActual(EstadoPedido.ENVIADO);
        pedido.setEstadoState(new EstadoPedidoEnviadoState());
    }
}