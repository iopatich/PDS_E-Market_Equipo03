package com.emarket.service.interfaz;

import com.emarket.dto.varianteProducto.VarianteProductoRequestDto;
import com.emarket.dto.varianteProducto.VarianteProductoResponseDto;
import com.emarket.entity.VarianteProducto;

import java.util.List;

public interface VarianteProductoService {
    VarianteProductoResponseDto crear(VarianteProductoRequestDto dto, String token);
    List<VarianteProductoResponseDto> listar();
    VarianteProductoResponseDto eliminar(Long id, String token);
    Double calcularPrecioFinal(VarianteProducto variante);
    VarianteProductoResponseDto reducirStock(Long id, Integer cantidad, String token);
}