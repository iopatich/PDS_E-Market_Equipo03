import { useEffect, useState } from 'react';
import { pedidoService } from '../../services/pedido.service';
import { useToast } from '../../core/toast/ToastContext';
import {
  formatCurrency,
  getEstadoPedidoLabel,
  getEstadoPedidoColor,
} from '../../core/utils/catalogUtils';
import { getErrorMessage } from '../../core/api/errorAdapter';
import { EstadoPedido } from '../../core/enums';
import { obtenerEtiquetaCanal } from '../../core/utils/permisosUtils';
import { Button } from '../../components/ui/Button';
import { Badge } from '../../components/ui/Badge';
import { Modal } from '../../components/ui/Modal';
import { Spinner } from '../../components/ui/Spinner';
import { Card } from '../../components/ui/Card';
import type { PedidoResponseDto, NotificacionResponseDto } from '../../core/types';

export function AdminOrdersPage() {
  const { showToast } = useToast();
  const [pedidos, setPedidos] = useState<PedidoResponseDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [selectedPedido, setSelectedPedido] = useState<PedidoResponseDto | null>(null);
  const [notificaciones, setNotificaciones] = useState<NotificacionResponseDto[]>([]);
  const [showNotifModal, setShowNotifModal] = useState(false);
  const [advancing, setAdvancing] = useState<number | null>(null);

  const loadPedidos = async () => {
    const data = await pedidoService.listarTodos();
    setPedidos(data);
  };

  useEffect(() => {
    loadPedidos()
      .catch((err) => showToast(getErrorMessage(err), 'error'))
      .finally(() => setLoading(false));
  }, [showToast]);

  const handleAdvance = async (id: number) => {
    setAdvancing(id);
    try {
      const response = await pedidoService.avanzarEstado(id);
      showToast(response.mensaje, 'success');
      await loadPedidos();
    } catch (err) {
      showToast(getErrorMessage(err), 'error');
    } finally {
      setAdvancing(null);
    }
  };

  const handleViewNotifications = async (pedido: PedidoResponseDto) => {
    setSelectedPedido(pedido);
    try {
      const notifs = await pedidoService.listarNotificaciones(pedido.id);
      setNotificaciones(notifs);
      setShowNotifModal(true);
    } catch (err) {
      showToast(getErrorMessage(err), 'error');
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center py-20">
        <Spinner size="lg" />
      </div>
    );
  }

  return (
    <div>
      <div>
        <h2 className="font-serif text-2xl font-bold text-wood-900">Pedidos</h2>
        <p className="text-sm text-wood-500">Seguimiento y actualización de pedidos</p>
      </div>

      <div className="mt-6 space-y-4">
        {pedidos.length === 0 ? (
          <p className="text-wood-500">No hay pedidos.</p>
        ) : (
          pedidos.map((pedido) => (
            <Card key={pedido.id}>
              <div className="flex flex-wrap items-center justify-between gap-4">
                <div>
                  <p className="font-medium text-wood-900">
                    Pedido #{pedido.id} — {pedido.usernameCliente}
                  </p>
                  <p className="text-sm text-wood-500">
                    {new Date(pedido.fechaCreacion).toLocaleString('es-AR')}
                  </p>
                  <p className="mt-1 font-semibold">{formatCurrency(pedido.total)}</p>
                </div>
                <div className="flex items-center gap-3">
                  <Badge className={getEstadoPedidoColor(pedido.estadoActual)}>
                    {getEstadoPedidoLabel(pedido.estadoActual)}
                  </Badge>
                  {pedido.estadoActual !== EstadoPedido.ENTREGADO && (
                    <Button
                      size="sm"
                      loading={advancing === pedido.id}
                      onClick={() => handleAdvance(pedido.id)}
                    >
                      Avanzar estado
                    </Button>
                  )}
                  <Button
                    size="sm"
                    variant="outline"
                    onClick={() => handleViewNotifications(pedido)}
                  >
                    Notificaciones
                  </Button>
                </div>
              </div>
              <ul className="mt-4 space-y-1 border-t border-wood-100 pt-4 text-sm">
                {pedido.items.map((item, idx) => (
                  <li key={idx} className="flex justify-between">
                    <span>
                      {item.nombreProducto} ({item.color}) × {item.cantidad}
                    </span>
                    <span>{formatCurrency(item.subtotal)}</span>
                  </li>
                ))}
              </ul>
            </Card>
          ))
        )}
      </div>

      <Modal
        open={showNotifModal}
        onClose={() => setShowNotifModal(false)}
        title={`Notificaciones — Pedido #${selectedPedido?.id}`}
        size="lg"
      >
        {notificaciones.length === 0 ? (
          <p className="text-wood-500">Sin notificaciones.</p>
        ) : (
          <div className="space-y-3">
            {notificaciones.map((n) => (
              <div key={n.id} className="rounded-lg border border-wood-100 p-3">
                <div className="flex items-center justify-between">
                  <Badge className="bg-wood-100 text-wood-700">{obtenerEtiquetaCanal(n.canal)}</Badge>
                  <span className="text-xs text-wood-400">
                    {new Date(n.fechaEnvio).toLocaleString('es-AR')}
                  </span>
                </div>
                <p className="mt-2 text-sm text-wood-700">{n.mensaje}</p>
              </div>
            ))}
          </div>
        )}
      </Modal>
    </div>
  );
}
