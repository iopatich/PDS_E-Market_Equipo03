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

        // Magia polimórfica: recorre la lista sin preguntar qué son
        for (ComponenteCatalogo elemento : elementos) {
            // Si el elemento es un Producto, devuelve su precio.
            // Si el elemento es OTRA Categoría, volverá a entrar a este mismo ciclo for por dentro.
            precioTotal += elemento.calcularPrecio();
        }

        return precioTotal;
    }
}