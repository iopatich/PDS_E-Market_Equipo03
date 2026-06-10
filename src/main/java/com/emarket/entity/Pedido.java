package com.emarket.entity;

import com.emarket.pedido.* ;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Getter
@Setter
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPedido estadoActual;

    @Column(nullable = false)
    private Double total;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> items = new ArrayList<>();

    // --- 1. AGREGADO PARA EL PATRÓN STATE CLÁSICO ---
    // @Transient evita que JPA intente crear una columna en la tabla 'pedidos' para este objeto
    @Transient
    private EstadoPedidoState estadoState;

    public void agregarItem(ItemPedido item) {
        items.add(item);
        item.setPedido(this);
    }

    // --- 2. MÉTODOS DEL PATRÓN STATE ---

    // Este es el método que actúa como "Contexto" delegando la acción
    public void avanzarEstado() {
        if (this.estadoState == null) {
            inicializarEstado();
        }
        // Delegación Polimórfica (El Bad Smell del switch desaparece aquí)
        this.estadoState.siguienteEstado(this);
    }

    // Permite que el Estado Concreto actualice el estado del Contexto
    public void setEstadoState(EstadoPedidoState estadoState) {
        this.estadoState = estadoState;
    }

    // Rehidrata el objeto State basándose en lo que trajimos de la Base de Datos
    public void inicializarEstado() {
        if (this.estadoActual == null) return;

        switch (this.estadoActual) {
            case PENDIENTE: this.estadoState = new EstadoPedidoPendienteState(); break;
            case PAGADO: this.estadoState = new EstadoPedidoPagadoState(); break;
            case ENVIADO: this.estadoState = new EstadoPedidoEnviadoState(); break;
            case ENTREGADO: this.estadoState = new EstadoPedidoEntregadoState(); break;
        }
    }
}