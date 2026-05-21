package com.emarket.dto.usuario;

import jakarta.validation.constraints.NotBlank;

public record UsuarioLoginRequestDto(
        @NotBlank(message = "El username es obligatorio")
        String username,

        @NotBlank(message = "La password es obligatoria")
        String password
) {
}
