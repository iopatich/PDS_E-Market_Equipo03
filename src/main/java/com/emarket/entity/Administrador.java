package com.emarket.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "administradores")
@Getter
@Setter
public class Administrador extends Usuario {

    @Override
    public String getTipoUsuario() {
        return "ADMINISTRADOR";
    }

    public void gestionarProducto(Producto producto) {
        if (!tienePermiso(Permiso.GESTIONAR_PRODUCTOS)) {
            throw new IllegalStateException("No tiene permiso para gestionar productos");
        }
    }

    public void darDeBajaCliente(Cliente cliente) {
        if (!tienePermiso(Permiso.DAR_BAJA_CLIENTE)) {
            throw new IllegalStateException("No tiene permiso para dar de baja clientes");
        }
        cliente.setActivo(false);
    }
}
