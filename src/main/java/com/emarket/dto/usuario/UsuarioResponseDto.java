package com.emarket.dto.usuario;

import com.emarket.entity.RolUsuario;

public record UsuarioResponseDto(
        Long id,
        String username,
        String email,
        RolUsuario rol
) {
}
