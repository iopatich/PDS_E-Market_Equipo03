package com.emarket.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "sesiones")
@Getter
@Setter
@NoArgsConstructor
public class Sesion {

    @Id
    @Column(length = 36)
    private String token;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDateTime expiracion;

    @Column(nullable = false)
    private Boolean activa = true;

    public Sesion(String token, Usuario usuario, LocalDateTime expiracion) {
        this.token = token;
        this.usuario = usuario;
        this.expiracion = expiracion;
    }

    public boolean estaVigente() {
        return Boolean.TRUE.equals(activa) && LocalDateTime.now().isBefore(expiracion);
    }
}
