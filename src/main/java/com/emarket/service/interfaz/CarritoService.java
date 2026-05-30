package com.emarket.service.interfaz;

import com.emarket.dto.carrito.ActualizarCantidadCarritoRequestDto;
import com.emarket.dto.carrito.AgregarItemCarritoRequestDto;
import com.emarket.dto.carrito.CarritoResponseDto;
import com.emarket.dto.pedido.PedidoResponseDto;

public interface CarritoService {
    CarritoResponseDto verCarrito(String token);
    CarritoResponseDto agregarItem(String token, AgregarItemCarritoRequestDto dto);
    CarritoResponseDto actualizarCantidad(String token, Long itemId, ActualizarCantidadCarritoRequestDto dto);
    CarritoResponseDto eliminarItem(String token, Long itemId);
    PedidoResponseDto confirmarCompra(String token);
}
