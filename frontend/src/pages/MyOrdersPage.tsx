import { useEffect, useState } from 'react';
import { pedidoService } from '../services/pedido.service';
import { formatCurrency, getEstadoPedidoLabel, getEstadoPedidoColor } from '../core/utils/catalogUtils';
import { getErrorMessage } from '../core/api/errorAdapter';
import { Badge } from '../components/ui/Badge';
import { Card } from '../components/ui/Card';
import { Spinner } from '../components/ui/Spinner';
import type { PedidoResponseDto } from '../core/types';

export function MyOrdersPage() {
  const [pedidos, setPedidos] = useState<PedidoResponseDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    pedidoService
      .listarMios()
      .then(setPedidos)
      .catch((err) => setError(getErrorMessage(err)))
      .finally(() => setLoading(false));
  }, []);

  if (loading) {
    return (
      <div className="flex min-h-[60vh] items-center justify-center">
        <Spinner size="lg" />
      </div>
    );
  }

  return (
    <div className="mx-auto max-w-7xl animate-fade-in px-4 py-8 sm:px-6 lg:px-8">
      <h1 className="font-serif text-3xl font-bold text-wood-900">Mis pedidos</h1>

      {error && <div className="mt-4 rounded-lg bg-red-50 p-4 text-red-700">{error}</div>}

      {pedidos.length === 0 ? (
        <p className="mt-8 text-center text-wood-500">No tenés pedidos aún.</p>
      ) : (
        <div className="mt-8 space-y-4">
          {pedidos.map((pedido) => (
            <Card key={pedido.id}>
              <div className="flex flex-wrap items-center justify-between gap-4">
                <div>
                  <p className="text-sm text-wood-500">
                    Pedido #{pedido.id} ·{' '}
                    {new Date(pedido.fechaCreacion).toLocaleDateString('es-AR', {
                      day: '2-digit',
                      month: 'long',
                      year: 'numeric',
                      hour: '2-digit',
                      minute: '2-digit',
                    })}
                  </p>
                  <p className="mt-1 text-lg font-semibold text-wood-900">
                    {formatCurrency(pedido.total)}
                  </p>
                </div>
                <Badge className={getEstadoPedidoColor(pedido.estadoActual)}>
                  {getEstadoPedidoLabel(pedido.estadoActual)}
                </Badge>
              </div>
              <ul className="mt-4 space-y-2 border-t border-wood-100 pt-4">
                {pedido.items.map((item, idx) => (
                  <li key={idx} className="flex justify-between text-sm">
                    <span>
                      {item.nombreProducto} ({item.color}) × {item.cantidad}
                    </span>
                    <span>{formatCurrency(item.subtotal)}</span>
                  </li>
                ))}
              </ul>
            </Card>
          ))}
        </div>
      )}
    </div>
  );
}
