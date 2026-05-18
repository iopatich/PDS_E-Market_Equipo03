package com.emarket.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "clientes")
@Getter
@Setter
public class Cliente extends Usuario {

    @Column(nullable = false, length = 150)
    private String email;

    @Override
    public String getTipoUsuario() {
        return "CLIENTE";
    }
}
