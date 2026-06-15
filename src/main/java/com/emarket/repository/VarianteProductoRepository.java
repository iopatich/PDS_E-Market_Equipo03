package com.emarket.repository;

import com.emarket.entity.VarianteProducto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VarianteProductoRepository extends JpaRepository<VarianteProducto, Long> {
    List<VarianteProducto> findAllByActivoTrue();
    List<VarianteProducto> findAllByProductoIdAndActivoTrue(Long productoId);
}
