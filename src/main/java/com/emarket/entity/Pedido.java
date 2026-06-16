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

    @Transient
    private EstadoPedidoState estadoState;

    public void agregarItem(ItemPedido item) {
        items.add(item);
        item.setPedido(this);
    }

    public void avanzarEstado() {
        if (this.estadoState == null) {
            inicializarEstado();
        }
        this.estadoState.siguienteEstado(this);
    }

    public void setEstadoState(EstadoPedidoState estadoState) {
        this.estadoState = estadoState;
    }

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