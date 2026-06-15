package com.emarket.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "items_carrito")
@Getter
@Setter
public class ItemCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "carrito_id", nullable = false)
    private Carrito carrito;

    @ManyToOne(optional = false)
    @JoinColumn(name = "variante_producto_id", nullable = false)
    private VarianteProducto varianteProducto;

    @Column(nullable = false)
    private Integer cantidad;
}
