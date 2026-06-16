package com.emarket.dto.producto;

public record ProductoResponseDto(
        Long id,
        String nombre,
        String descripcion,
        Double precioBase,
        String nombreCategoriaPadre,
        String urlImagen

) {
}
