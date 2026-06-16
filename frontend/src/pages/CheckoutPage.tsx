import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { carritoService } from '../services/carrito.service';
import { useCart } from '../core/cart/CartContext';
import { useToast } from '../core/toast/ToastContext';
import { getPaymentStrategies } from '../core/patterns/paymentStrategy';
import { TipoPago } from '../core/enums';
import { getErrorMessage } from '../core/api/errorAdapter';
import { formatCurrency } from '../core/utils/catalogUtils';
import { Button } from '../components/ui/Button';
import { Card } from '../components/ui/Card';
import { Spinner } from '../components/ui/Spinner';

export function CheckoutPage() {
  const { cart, loading, refreshCart, clearLocalCart } = useCart();
  const { showToast } = useToast();
  const navigate = useNavigate();

  const [tipoPago, setTipoPago] = useState<TipoPago>(TipoPago.TARJETA);
  const [confirming, setConfirming] = useState(false);
  const strategies = getPaymentStrategies();

  const handleConfirm = async () => {
    const strategy = strategies.find((s) => s.tipo === tipoPago);
    if (!strategy) return;

    setConfirming(true);
    try {
      const response = await carritoService.confirmarCompra(strategy.buildRequest());
      clearLocalCart();
      showToast(response.mensaje, 'success');
      navigate('/mis-pedidos');
    } catch (err) {
      showToast(getErrorMessage(err), 'error');
      await refreshCart();
    } finally {
      setConfirming(false);
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
    navigate('/carrito');
    return null;
  }

  return (
    <div className="mx-auto max-w-3xl animate-fade-in px-4 py-8 sm:px-6">
      <h1 className="font-serif text-3xl font-bold text-wood-900">Finalizar compra</h1>

      <Card className="mt-8">
        <h2 className="font-medium text-wood-900">Resumen del pedido</h2>
        <ul className="mt-4 space-y-2">
          {cart.items.map((item) => (
            <li key={item.id} className="flex justify-between text-sm">
              <span>
                {item.nombreProducto} ({item.color}) × {item.cantidad}
              </span>
              <span>{formatCurrency(item.subtotal)}</span>
            </li>
          ))}
        </ul>
        <div className="mt-4 flex justify-between border-t border-wood-100 pt-4 font-semibold">
          <span>Total</span>
          <span>{formatCurrency(cart.total)}</span>
        </div>
      </Card>

      <Card className="mt-6">
        <h2 className="font-medium text-wood-900">Forma de pago</h2>
        <p className="mt-1 text-sm text-wood-500">Elegí cómo querés abonar tu pedido</p>
        <div className="mt-4 space-y-3">
          {strategies.map((strategy) => (
            <label
              key={strategy.tipo}
              className={`flex cursor-pointer items-start gap-3 rounded-lg border-2 p-4 transition-all ${
                tipoPago === strategy.tipo
                  ? 'border-accent bg-accent/5'
                  : 'border-wood-200 hover:border-wood-300'
              }`}
            >
              <input
                type="radio"
                name="tipoPago"
                value={strategy.tipo}
                checked={tipoPago === strategy.tipo}
                onChange={() => setTipoPago(strategy.tipo)}
                className="mt-1"
              />
              <div>
                <p className="font-medium text-wood-900">{strategy.label}</p>
                <p className="text-sm text-wood-500">{strategy.description}</p>
              </div>
            </label>
          ))}
        </div>
      </Card>

      <div className="mt-8 flex gap-4">
        <Button variant="outline" onClick={() => navigate('/carrito')}>
          Volver al carrito
        </Button>
        <Button loading={confirming} onClick={handleConfirm}>
          Confirmar compra
        </Button>
      </div>
    </div>
  );
}
