package com.emarket.service.interfaz;

import com.emarket.dto.producto.ProductoDetalleResponseDto;
import com.emarket.dto.producto.ProductoRequestDto;
import com.emarket.dto.producto.ProductoResponseDto;
import com.emarket.dto.producto.ProductoUpdateDto;

import java.util.List;

public interface ProductoService {
    ProductoResponseDto crear(ProductoRequestDto dto, String token);
    List<ProductoResponseDto> listar();
    ProductoDetalleResponseDto obtenerDetalle(Long id);
    ProductoResponseDto actualizar(Long id, ProductoUpdateDto dto, String token);
    ProductoResponseDto eliminar(Long id, String token);
}