package com.emarket.pago;

import com.emarket.entity.Pedido;

public class PagoEfectivo implements MetodoPago {
    @Override
    public void procesarPago(Pedido pedido) {
        System.out.println("Procesando pago en efectivo para el pedido " + pedido.getId());
    }
}