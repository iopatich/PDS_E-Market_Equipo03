import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useState,
  type ReactNode,
} from 'react';
import { carritoService } from '../../services/carrito.service';
import { cartSubject } from '../patterns/cartObserver';
import { useAuth } from '../auth/AuthContext';
import { Permiso } from '../enums';
import type { CarritoResponseDto } from '../types';

interface CartContextValue {
  cart: CarritoResponseDto | null;
  itemCount: number;
  loading: boolean;
  refreshCart: () => Promise<void>;
  clearLocalCart: () => void;
}

const CartContext = createContext<CartContextValue | null>(null);

export function CartProvider({ children }: { children: ReactNode }) {
  const { isAuthenticated, hasPermiso } = useAuth();
  const [cart, setCart] = useState<CarritoResponseDto | null>(null);
  const [loading, setLoading] = useState(false);

  const refreshCart = useCallback(async () => {
    if (!isAuthenticated || !hasPermiso(Permiso.GESTIONAR_CARRITO)) {
      setCart(null);
      cartSubject.notify(null);
      return;
    }
    setLoading(true);
    try {
      const data = await carritoService.ver();
      setCart(data);
      cartSubject.notify(data);
    } catch {
      setCart(null);
      cartSubject.notify(null);
    } finally {
      setLoading(false);
    }
  }, [isAuthenticated, hasPermiso]);

  const clearLocalCart = useCallback(() => {
    setCart(null);
    cartSubject.notify(null);
  }, []);

  useEffect(() => {
    refreshCart();
  }, [refreshCart]);

  useEffect(() => {
    return cartSubject.subscribe(setCart);
  }, []);

  const itemCount = useMemo(
    () => cart?.items.reduce((sum, item) => sum + item.cantidad, 0) ?? 0,
    [cart],
  );

  const value = useMemo(
    () => ({ cart, itemCount, loading, refreshCart, clearLocalCart }),
    [cart, itemCount, loading, refreshCart, clearLocalCart],
  );

  return <CartContext.Provider value={value}>{children}</CartContext.Provider>;
}

export function useCart(): CartContextValue {
  const ctx = useContext(CartContext);
  if (!ctx) throw new Error('useCart debe usarse dentro de CartProvider');
  return ctx;
}
