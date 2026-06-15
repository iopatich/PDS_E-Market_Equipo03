package com.emarket.controller;

import com.emarket.dto.api.ApiResponseDto;
import com.emarket.dto.carrito.ActualizarCantidadCarritoRequestDto;
import com.emarket.dto.carrito.AgregarItemCarritoRequestDto;
import com.emarket.dto.carrito.CarritoResponseDto;
import com.emarket.dto.carrito.ConfirmarCompraRequestDto;
import com.emarket.dto.pedido.PedidoResponseDto;
import com.emarket.service.interfaz.CarritoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CarritoResponseDto verCarrito(@RequestHeader("Authorization") String authorization) {
        return carritoService.verCarrito(authorization);
    }

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    public CarritoResponseDto agregarItem(
            @RequestHeader("Authorization") String authorization,
            @Valid @RequestBody AgregarItemCarritoRequestDto dto
    ) {
        return carritoService.agregarItem(authorization, dto);
    }

    @PutMapping("/items/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public CarritoResponseDto actualizarCantidad(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long itemId,
            @Valid @RequestBody ActualizarCantidadCarritoRequestDto dto
    ) {
        return carritoService.actualizarCantidad(authorization, itemId, dto);
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<ApiResponseDto<CarritoResponseDto>> eliminarItem(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long itemId
    ) {
        CarritoResponseDto carrito = carritoService.eliminarItem(authorization, itemId);
        return ResponseEntity.ok(new ApiResponseDto<>("Se elimino el item del carrito", carrito));
    }

    @PostMapping("/confirmar")
    public ResponseEntity<ApiResponseDto<PedidoResponseDto>> confirmarCompra(
            @RequestHeader("Authorization") String authorization,
            @Valid @RequestBody ConfirmarCompraRequestDto dto
    ) {
        PedidoResponseDto pedido = carritoService.confirmarCompra(authorization, dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDto<>("Compra confirmada correctamente. Metodo de pago: " + dto.tipoPago(), pedido));
    }
}
