package com.emarket.pedido;

import com.emarket.entity.EstadoPedido;
import com.emarket.entity.Pedido;
import org.springframework.stereotype.Component;

@Component
public class EstadoPedidoPendienteState implements EstadoPedidoState {
    @Override
    public EstadoPedido getEstado() {
        return EstadoPedido.PENDIENTE;
    }

    @Override
    public EstadoPedido siguienteEstado(Pedido pedido) {
        return EstadoPedido.PAGADO;
    }
}
