package com.emarket.repository;

import com.emarket.entity.Cliente;
import com.emarket.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByClienteOrderByFechaCreacionDesc(Cliente cliente);
    List<Pedido> findAllByOrderByFechaCreacionDesc();
}
