package com.emarket.service.interfaz;

import com.emarket.dto.varianteProducto.VarianteProductoRequestDto;
import com.emarket.dto.varianteProducto.VarianteProductoResponseDto;
import com.emarket.entity.VarianteProducto;

import java.util.List;

public interface VarianteProductoService {
    VarianteProductoResponseDto crear(VarianteProductoRequestDto dto);
    List<VarianteProductoResponseDto> listar();
    VarianteProductoResponseDto eliminar(Long id);
    Double calcularPrecioFinal(VarianteProducto variante);
    VarianteProductoResponseDto reducirStock(Long id, Integer cantidad);
}
