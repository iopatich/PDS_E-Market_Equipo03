package com.emarket.dto.usuario;

import com.emarket.entity.RolUsuario;

public record UsuarioLoginResponseDto(
        Long id,
        String username,
        RolUsuario rol,
        String mensaje
) {
}
