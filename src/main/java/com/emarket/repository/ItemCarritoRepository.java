package com.emarket.repository;

import com.emarket.entity.ItemCarrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long> {
    Optional<ItemCarrito> findByIdAndCarritoId(Long id, Long carritoId);
    Optional<ItemCarrito> findByCarritoIdAndVarianteProductoId(Long carritoId, Long varianteProductoId);
}
