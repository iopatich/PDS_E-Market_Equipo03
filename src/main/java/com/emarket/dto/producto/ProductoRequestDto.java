package com.emarket.dto.producto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;


public record ProductoRequestDto (
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @NotBlank(message = "El nombre es obligatorio")
        String descripcion,

        @NotNull(message = "El precio es obligatorio")
        @Positive(message = "El precio debe ser mayor a cero")
        Double precioBase,

        @NotNull(message = "La categoria es obligatoria")
        @Min(value = 1, message = "El id de categoria debe ser mayor o igual a 1")
        Long idCategoriaPadre,

        String urlImagen
) {}
