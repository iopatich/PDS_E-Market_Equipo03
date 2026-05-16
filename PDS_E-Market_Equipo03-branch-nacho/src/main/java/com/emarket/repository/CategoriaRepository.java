package com.emarket.repository;

import com.emarket.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    List<Categoria> findAllByActivoTrue();
    List<Categoria> findByCategoriaPadreIsNullAndActivoTrue();
}
