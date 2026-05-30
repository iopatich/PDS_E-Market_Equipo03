package com.emarket.pedido;

import com.emarket.entity.EstadoPedido;
import com.emarket.entity.Pedido;
import com.emarket.exception.OperacionInvalidaException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EstadoPedidoResolverTest {

    private final EstadoPedidoResolver resolver = new EstadoPedidoResolver(List.of(
            new EstadoPedidoPendienteState(),
            new EstadoPedidoPagadoState(),
            new EstadoPedidoEnviadoState(),
            new EstadoPedidoEntregadoState()
    ));

    @Test
    void deberiaAvanzarSecuencialmenteLosEstadosDelPedido() {
        Pedido pedido = new Pedido();

        assertEquals(EstadoPedido.PAGADO, resolver.resolve(EstadoPedido.PENDIENTE).siguienteEstado(pedido));
        assertEquals(EstadoPedido.ENVIADO, resolver.resolve(EstadoPedido.PAGADO).siguienteEstado(pedido));
        assertEquals(EstadoPedido.ENTREGADO, resolver.resolve(EstadoPedido.ENVIADO).siguienteEstado(pedido));
    }

    @Test
    void noDeberiaPermitirAvanzarUnPedidoEntregado() {
        Pedido pedido = new Pedido();
        assertThrows(OperacionInvalidaException.class,
                () -> resolver.resolve(EstadoPedido.ENTREGADO).siguienteEstado(pedido));
    }
}
