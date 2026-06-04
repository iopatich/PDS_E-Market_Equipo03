package com.emarket.repository;

import com.emarket.entity.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SesionRepository extends JpaRepository<Sesion, String> {
    Optional<Sesion> findByTokenAndActivaTrue(String token);
}
