package com.emarket.dto.auth;

import com.emarket.entity.Permiso;

import java.util.List;

public record LoginResponseDto(
        String token,
        String tipoUsuario,
        Long id,
        String username,
        List<Permiso> permisos
) {
}
