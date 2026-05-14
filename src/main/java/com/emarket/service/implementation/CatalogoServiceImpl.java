package com.emarket.service.implementation;

import com.emarket.entity.ComponenteCatalogo;
import com.emarket.entity.Producto;
import com.emarket.service.interfaz.CatalogoService;
import org.springframework.stereotype.Service;

@Service
public class CatalogoServiceImpl implements CatalogoService {
    @Override
    public Double obtenerPrecioBase(ComponenteCatalogo componente) {
        if (componente instanceof Producto producto) {
            return producto.getPrecioBase();
        }
        return 0.0;
    }
}
