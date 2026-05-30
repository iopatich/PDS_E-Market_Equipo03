package com.emarket.mapper;

import com.emarket.dto.carrito.CarritoResponseDto;
import com.emarket.dto.carrito.ItemCarritoResponseDto;
import com.emarket.entity.Carrito;
import com.emarket.entity.ItemCarrito;
import com.emarket.entity.VarianteProducto;

import java.util.List;

public class CarritoMapper {

    private CarritoMapper() {
    }

    public static ItemCarritoResponseDto toItemResponseDto(ItemCarrito item) {
        VarianteProducto variante = item.getVarianteProducto();
        double precioUnitario = variante.getProducto().getPrecioBase() + variante.getPrecioAdicional();
        return new ItemCarritoResponseDto(
                item.getId(),
                variante.getId(),
                variante.getProducto().getNombre(),
                variante.getColor(),
                item.getCantidad(),
                precioUnitario,
                precioUnitario * item.getCantidad()
        );
    }

    public static CarritoResponseDto toResponseDto(Carrito carrito) {
        List<ItemCarritoResponseDto> items = carrito.getItems().stream()
                .map(CarritoMapper::toItemResponseDto)
                .toList();

        double total = items.stream()
                .mapToDouble(ItemCarritoResponseDto::subtotal)
                .sum();

        return new CarritoResponseDto(
                carrito.getId(),
                carrito.getCliente().getId(),
                items,
                total
        );
    }
}
