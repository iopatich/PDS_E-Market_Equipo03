package com.emarket.pago;

import com.emarket.entity.Pedido;

public class PagoTransferencia implements MetodoPago {
    @Override
    public void procesarPago(Pedido pedido) {
        System.out.println("Procesando pago por transferencia para el pedido " + pedido.getId());
    }
}