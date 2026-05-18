package com.emarket.controller;

import com.emarket.dto.administrador.AdministradorRequestDto;
import com.emarket.dto.administrador.AdministradorResponseDto;
import com.emarket.dto.api.ApiResponseDto;
import com.emarket.dto.cliente.ClienteResponseDto;
import com.emarket.service.interfaz.AdministradorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/administradores")
@RequiredArgsConstructor
public class AdministradorController {

    private final AdministradorService administradorService;

    @PostMapping("/registro")
    @ResponseStatus(HttpStatus.CREATED)
    public AdministradorResponseDto registrar(@Valid @RequestBody AdministradorRequestDto dto) {
        return administradorService.registrar(dto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AdministradorResponseDto> listar(@RequestHeader("Authorization") String authorization) {
        return administradorService.listar(authorization);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<ApiResponseDto<AdministradorResponseDto>> eliminar(@PathVariable Long id) {
        AdministradorResponseDto administradorEliminado = administradorService.eliminar(id);
        return ResponseEntity.ok(new ApiResponseDto<>("Se ha eliminado el administrador ", administradorEliminado));
    }

    @DeleteMapping("/clientes/eliminar/{id}")
    public ResponseEntity<ApiResponseDto<ClienteResponseDto>> darDeBajaCliente(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorization
    ) {
        ClienteResponseDto clienteEliminado = administradorService.darDeBajaCliente(id, authorization);
        return ResponseEntity.ok(new ApiResponseDto<>("Se ha dado de baja al cliente ", clienteEliminado));
    }
}
