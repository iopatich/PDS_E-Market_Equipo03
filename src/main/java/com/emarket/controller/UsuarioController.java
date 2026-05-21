package com.emarket.controller;

import com.emarket.dto.usuario.UsuarioLoginRequestDto;
import com.emarket.dto.usuario.UsuarioLoginResponseDto;
import com.emarket.dto.usuario.UsuarioRegistroRequestDto;
import com.emarket.dto.usuario.UsuarioResponseDto;
import com.emarket.service.interfaz.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    @PostMapping("/registro")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponseDto registrar(@Valid @RequestBody UsuarioRegistroRequestDto dto) {
        return usuarioService.registrar(dto);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public UsuarioLoginResponseDto login(@Valid @RequestBody UsuarioLoginRequestDto dto) {
        return usuarioService.login(dto);
    }
}
