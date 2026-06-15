package com.emarket.service.interfaz;

import com.emarket.dto.categoria.CategoriaRequestDto;
import com.emarket.dto.categoria.CategoriaResponseDto;

import java.util.List;

public interface CategoriaService {
    CategoriaResponseDto crear(CategoriaRequestDto dto, String token);
    List<CategoriaResponseDto> listar();
    Double obtenerPrecioTotal(Long id);
    CategoriaResponseDto eliminar(Long id, String token);
}