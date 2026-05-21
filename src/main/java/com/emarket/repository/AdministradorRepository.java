package com.emarket.repository;

import com.emarket.entity.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdministradorRepository extends JpaRepository<Administrador, Long> {
    List<Administrador> findAllByActivoTrue();
}
