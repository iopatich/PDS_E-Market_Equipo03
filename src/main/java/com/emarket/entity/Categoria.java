package com.emarket.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "categorias")
@Getter @Setter
public class Categoria extends ComponenteCatalogo {
    @OneToMany(mappedBy = "categoriaPadre", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<ComponenteCatalogo> elementos;

    @Override
    public Double calcularPrecio() {
        Double precioTotal = 0.0;
        if (elementos == null) {
            return precioTotal;
        }

        for (ComponenteCatalogo elemento : elementos) {
            if (Boolean.TRUE.equals(elemento.getActivo())) {
                precioTotal += elemento.calcularPrecio();
            }
        }

        return precioTotal;
    }
}