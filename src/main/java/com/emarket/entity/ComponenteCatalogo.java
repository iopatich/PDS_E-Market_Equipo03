package com.emarket.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "componentesCatalogo")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter @Setter
public abstract class ComponenteCatalogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false)
    private Boolean activo = true;

    @ManyToOne
    @JoinColumn(name = "idCategoriaPadre")
    private Categoria categoriaPadre;
}