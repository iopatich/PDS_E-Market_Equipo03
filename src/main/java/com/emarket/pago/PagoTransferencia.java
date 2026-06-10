package com.emarket.pago;

import com.emarket.entity.Pedido;
import org.springframework.stereotype.Component;

@Component
public class PagoTransferencia implements MetodoPago {

    @Override
    public void procesarPago(Pedido pedido) {
        System.out.println("Pago por transferencia procesado para el pedido " + pedido.getId());
    }

    @Override
    public TipoPago getTipo() {
        return TipoPago.TRANSFERENCIA;
    }
}
