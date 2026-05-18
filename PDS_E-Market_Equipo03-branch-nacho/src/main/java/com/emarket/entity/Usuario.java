package com.emarket.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public abstract class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false)
    private Boolean activo = true;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "usuario_permisos", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "permiso")
    private List<Permiso> permisos = new ArrayList<>();

    public boolean tienePermiso(Permiso permiso) {
        return permisos != null && permisos.contains(permiso);
    }

    public void asignarPermisoCliente() {
        this.permisos = new ArrayList<>(List.of(
                Permiso.VER_CATALOGO,
                Permiso.GESTIONAR_CARRITO,
                Permiso.REALIZAR_COMPRA
        ));
    }

    public void asignarPermisoAdministrador() {
        this.permisos = new ArrayList<>(Arrays.asList(Permiso.values()));
    }

    public abstract String getTipoUsuario();
}
