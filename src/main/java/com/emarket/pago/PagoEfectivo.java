package com.emarket.pago;

import com.emarket.entity.Pedido;
import org.springframework.stereotype.Component;

@Component
public class PagoEfectivo implements MetodoPago {

    @Override
    public void procesarPago(Pedido pedido) {
    }

    @Override
    public TipoPago getTipo() {
        return TipoPago.EFECTIVO;
    }
}
