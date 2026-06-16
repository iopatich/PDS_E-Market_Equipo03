package com.emarket.mapper;

import com.emarket.dto.producto.ProductoRequestDto;
import com.emarket.dto.producto.ProductoResponseDto;
import com.emarket.entity.Categoria;
import com.emarket.entity.Producto;

public class ProductoMapper {
        private ProductoMapper() {

    }

    public static Producto toEntity(ProductoRequestDto dto, Categoria categoriaPadre) {
        Producto producto = new Producto();
        producto.setNombre(dto.nombre());
        producto.setDescripcion(dto.descripcion());
        producto.setPrecioBase(dto.precioBase());
        producto.setUrlImagen(dto.urlImagen());
        producto.setCategoriaPadre(categoriaPadre);

        return producto;
    }

    public static ProductoResponseDto toResponseDto(Producto producto) {
            return new ProductoResponseDto(
                    producto.getId(),
                    producto.getNombre(),
                    producto.getDescripcion(),
                    producto.getPrecioBase(),
                    producto.getCategoriaPadre() != null ? producto.getCategoriaPadre().getNombre() : null,
                    producto.getUrlImagen()
            );
    }

}
