package com.emarket.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "productos")
@Getter
@Setter
public class Producto extends ComponenteCatalogo {

    @Column(nullable = false, length = 255)
    private String descripcion;

    @Column(name = "precio_base", nullable = false)
    private Double precioBase;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    private List<VarianteProducto> variantes;

    @Override
    public Double calcularPrecio() {
        return this.getPrecioBase();
    }
}