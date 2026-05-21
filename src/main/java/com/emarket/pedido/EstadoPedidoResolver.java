package com.emarket.pedido;

import com.emarket.entity.EstadoPedido;
import com.emarket.exception.OperacionInvalidaException;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class EstadoPedidoResolver {

    private final Map<EstadoPedido, EstadoPedidoState> states = new EnumMap<>(EstadoPedido.class);

    public EstadoPedidoResolver(List<EstadoPedidoState> states) {
        states.forEach(state -> this.states.put(state.getEstado(), state));
    }

    public EstadoPedidoState resolve(EstadoPedido estado) {
        EstadoPedidoState state = states.get(estado);
        if (state == null) {
            throw new OperacionInvalidaException("No existe una estrategia de estado para " + estado);
        }
        return state;
    }
}
