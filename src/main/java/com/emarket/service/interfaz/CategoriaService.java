package com.emarket.service.interfaz;

import com.emarket.dto.categoria.CategoriaRequestDto;
import com.emarket.dto.categoria.CategoriaResponseDto;

import java.util.List;

public interface CategoriaService {
    CategoriaResponseDto crear(CategoriaRequestDto dto);
    List<CategoriaResponseDto> listar();
    CategoriaResponseDto eliminar(Long id);
}
