package com.emarket.repository;

import com.emarket.entity.Carrito;
import com.emarket.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    Optional<Carrito> findByCliente(Cliente cliente);
}
