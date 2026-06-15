package com.emarket.controller;

import com.emarket.dto.api.ApiResponseDto;
import com.emarket.dto.cliente.ClienteRequestDto;
import com.emarket.dto.cliente.ClienteResponseDto;
import com.emarket.service.interfaz.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping("/registro")
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponseDto registrar(@Valid @RequestBody ClienteRequestDto dto) {
        return clienteService.registrar(dto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ClienteResponseDto> listar(@RequestHeader("Authorization") String authorization) {
        return clienteService.listar(authorization);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<ApiResponseDto<ClienteResponseDto>> eliminar(@PathVariable Long id) {
        ClienteResponseDto clienteEliminado = clienteService.eliminar(id);
        return ResponseEntity.ok(new ApiResponseDto<>("Se ha eliminado el cliente ", clienteEliminado));
    }
}