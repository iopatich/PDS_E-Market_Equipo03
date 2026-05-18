package com.emarket.repository;

import com.emarket.entity.ComponenteCatalogo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComponenteCatalogoRepository extends JpaRepository<ComponenteCatalogo, Long> {
    List<ComponenteCatalogo> findAllByActivoTrue();
}
