package com.emarket.pedido;

import com.emarket.entity.EstadoPedido;
import com.emarket.entity.Pedido;
import com.emarket.exception.OperacionInvalidaException;
import org.springframework.stereotype.Component;

@Component
public class EstadoPedidoEntregadoState implements EstadoPedidoState {
    @Override
    public EstadoPedido getEstado() {
        return EstadoPedido.ENTREGADO;
    }

    @Override
    public EstadoPedido siguienteEstado(Pedido pedido) {
        throw new OperacionInvalidaException("El pedido ya se encuentra entregado");
    }
}
