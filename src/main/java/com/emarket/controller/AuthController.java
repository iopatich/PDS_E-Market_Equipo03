package com.emarket.controller;

import com.emarket.dto.auth.LoginRequestDto;
import com.emarket.dto.auth.LoginResponseDto;
import com.emarket.entity.Administrador;
import com.emarket.entity.Cliente;
import com.emarket.entity.Usuario;
import com.emarket.mapper.AdministradorMapper;
import com.emarket.mapper.ClienteMapper;
import com.emarket.service.interfaz.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponseDto login(@Valid @RequestBody LoginRequestDto dto) {
        return authService.login(dto);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestHeader("Authorization") String authorization) {
        authService.logout(authorization);
    }

    @GetMapping("/perfil")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> perfil(@RequestHeader("Authorization") String authorization) {
        Usuario usuario = authService.obtenerUsuarioAutenticado(authorization);
        if (usuario instanceof Cliente cliente) {
            return ResponseEntity.ok(ClienteMapper.toResponseDto(cliente));
        }
        if (usuario instanceof Administrador administrador) {
            return ResponseEntity.ok(AdministradorMapper.toResponseDto(administrador));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
