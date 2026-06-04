package com.emarket.controller;

import com.emarket.dto.api.ApiResponseDto;
import com.emarket.dto.notificacion.NotificacionResponseDto;
import com.emarket.dto.pedido.PedidoResponseDto;
import com.emarket.service.interfaz.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @GetMapping("/mios")
    @ResponseStatus(HttpStatus.OK)
    public List<PedidoResponseDto> listarMisPedidos(@RequestHeader("Authorization") String authorization) {
        return pedidoService.listarMisPedidos(authorization);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PedidoResponseDto> listarPedidos(@RequestHeader("Authorization") String authorization) {
        return pedidoService.listarPedidos(authorization);
    }

    @PutMapping("/{id}/estado/siguiente")
    public ResponseEntity<ApiResponseDto<PedidoResponseDto>> avanzarEstado(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorization
    ) {
        PedidoResponseDto pedido = pedidoService.avanzarEstado(id, authorization);
        return ResponseEntity.ok(new ApiResponseDto<>("Estado del pedido actualizado", pedido));
    }

    @GetMapping("/{id}/notificaciones")
    @ResponseStatus(HttpStatus.OK)
    public List<NotificacionResponseDto> listarNotificaciones(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorization
    ) {
        return pedidoService.listarNotificaciones(id, authorization);
    }
}
