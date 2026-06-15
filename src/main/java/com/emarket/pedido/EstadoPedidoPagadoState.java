package com.emarket.pedido;

import com.emarket.entity.EstadoPedido;
import com.emarket.entity.Pedido;
import org.springframework.stereotype.Component;

@Component
public class EstadoPedidoPagadoState implements EstadoPedidoState {
    @Override
    public EstadoPedido getEstado() {
        return EstadoPedido.PAGADO;
    }

    @Override
    public EstadoPedido siguienteEstado(Pedido pedido) {
        return EstadoPedido.ENVIADO;
    }
}
