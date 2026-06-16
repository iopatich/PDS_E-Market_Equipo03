package com.emarket.pago;

import com.emarket.entity.Pedido;

public class PagoTarjeta implements MetodoPago {
    @Override
    public void procesarPago(Pedido pedido) {
        System.out.println("Procesando pago con tarjeta para el pedido " + pedido.getId());
    }
}