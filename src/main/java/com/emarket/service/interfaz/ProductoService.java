package com.emarket.service.interfaz;

import com.emarket.dto.producto.ProductoRequestDto;
import com.emarket.dto.producto.ProductoResponseDto;
import com.emarket.dto.producto.ProductoUpdateDto;

import java.util.List;

public interface ProductoService {
    ProductoResponseDto crear(ProductoRequestDto dto, String token);
    List<ProductoResponseDto> listar();
    ProductoResponseDto eliminar(Long id, String token);
}