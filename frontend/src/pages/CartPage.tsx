import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { carritoService } from '../services/carrito.service';
import { useCart } from '../core/cart/CartContext';
import { useToast } from '../core/toast/ToastContext';
import { getErrorMessage } from '../core/api/errorAdapter';
import { formatCurrency } from '../core/utils/catalogUtils';
import { Button } from '../components/ui/Button';
import { Spinner } from '../components/ui/Spinner';
import { Card } from '../components/ui/Card';

export function CartPage() {
  const { cart, loading, refreshCart } = useCart();
  const { showToast } = useToast();
  const navigate = useNavigate();
  const [updatingId, setUpdatingId] = useState<number | null>(null);

  const handleUpdateQty = async (itemId: number, cantidad: number) => {
    if (cantidad < 1) return;
    setUpdatingId(itemId);
    try {
      await carritoService.actualizarCantidad(itemId, { cantidad });
      await refreshCart();
    } catch (err) {
      showToast(getErrorMessage(err), 'error');
    } finally {
      setUpdatingId(null);
    }
  };

  const handleRemove = async (itemId: number) => {
    setUpdatingId(itemId);
    try {
      await carritoService.eliminarItem(itemId);
      await refreshCart();
      showToast('Ítem eliminado', 'info');
    } catch (err) {
      showToast(getErrorMessage(err), 'error');
    } finally {
      setUpdatingId(null);
    }
  };

  if (loading) {
    return (
      <div className="flex min-h-[60vh] items-center justify-center">
        <Spinner size="lg" />
      </div>
    );
  }

  if (!cart || cart.items.length === 0) {
    return (
      <div className="mx-auto max-w-7xl px-4 py-16 text-center sm:px-6">
        <h1 className="font-serif text-2xl font-bold text-wood-900">Tu carrito está vacío</h1>
        <p className="mt-2 text-wood-500">Explorá nuestro catálogo y agregá productos.</p>
        <Button className="mt-6" onClick={() => navigate('/')}>
          Ir al catálogo
        </Button>
      </div>
    );
  }

  return (
    <div className="mx-auto max-w-7xl animate-fade-in px-4 py-8 sm:px-6 lg:px-8">
      <h1 className="font-serif text-3xl font-bold text-wood-900">Carrito</h1>

      <div className="mt-8 grid gap-8 lg:grid-cols-3">
        <div className="space-y-4 lg:col-span-2">
          {cart.items.map((item) => (
            <Card key={item.id} className="flex flex-col gap-4 sm:flex-row sm:items-center">
              <div className="flex h-20 w-20 shrink-0 items-center justify-center rounded-lg bg-wood-100 font-serif text-2xl text-wood-400">
                {item.nombreProducto.charAt(0)}
              </div>
              <div className="flex-1">
                <h3 className="font-medium text-wood-900">{item.nombreProducto}</h3>
                <p className="text-sm text-wood-500">Color: {item.color}</p>
                <p className="text-sm font-medium text-wood-700">
                  {formatCurrency(item.precioUnitario)} c/u
                </p>
              </div>
              <div className="flex items-center gap-3">
                <div className="flex items-center rounded-lg border border-wood-200">
                  <button
                    type="button"
                    disabled={updatingId === item.id}
                    onClick={() => handleUpdateQty(item.id, item.cantidad - 1)}
                    className="px-2 py-1 hover:bg-wood-50 disabled:opacity-50"
                  >
                    −
                  </button>
                  <span className="w-8 text-center text-sm">{item.cantidad}</span>
                  <button
                    type="button"
                    disabled={updatingId === item.id}
                    onClick={() => handleUpdateQty(item.id, item.cantidad + 1)}
                    className="px-2 py-1 hover:bg-wood-50 disabled:opacity-50"
                  >
                    +
                  </button>
                </div>
                <p className="w-24 text-right font-semibold">{formatCurrency(item.subtotal)}</p>
                <Button
                  variant="ghost"
                  size="sm"
                  disabled={updatingId === item.id}
                  onClick={() => handleRemove(item.id)}
                >
                  Eliminar
                </Button>
              </div>
            </Card>
          ))}
        </div>

        <Card className="h-fit">
          <h2 className="font-serif text-xl font-semibold">Resumen</h2>
          <div className="mt-4 flex justify-between border-t border-wood-100 pt-4">
            <span className="font-medium">Total</span>
            <span className="text-xl font-bold text-wood-900">{formatCurrency(cart.total)}</span>
          </div>
          <Link to="/finalizar-compra" className="mt-6 block">
            <Button className="w-full" size="lg">
              Finalizar compra
            </Button>
          </Link>
        </Card>
      </div>
    </div>
  );
}
