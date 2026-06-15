package com.emarket.pedido;

import com.emarket.entity.Pedido;
import com.emarket.exception.OperacionInvalidaException;

public class EstadoPedidoEntregadoState implements EstadoPedidoState {
    @Override
    public void siguienteEstado(Pedido pedido) {
        throw new OperacionInvalidaException("El pedido ya se encuentra entregado");
    }
}