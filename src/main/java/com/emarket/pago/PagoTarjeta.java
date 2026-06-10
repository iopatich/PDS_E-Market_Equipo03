package com.emarket.pago;

import com.emarket.entity.Pedido;
import org.springframework.stereotype.Component;

@Component
public class PagoTarjeta implements MetodoPago {

    @Override
    public void procesarPago(Pedido pedido) {
        System.out.println("Pago con tarjeta procesado para el pedido " + pedido.getId());
    }

    @Override
    public TipoPago getTipo() {
        return TipoPago.TARJETA;
    }
}
