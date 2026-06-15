import type { CarritoResponseDto } from '../types';

/**
 * Notifica a los componentes suscriptos cuando cambia el estado del carrito.
 */
export type CartObserver = (cart: CarritoResponseDto | null) => void;

class CartSubject {
  private observers: Set<CartObserver> = new Set();
  private cart: CarritoResponseDto | null = null;

  subscribe(observer: CartObserver): () => void {
    this.observers.add(observer);
    observer(this.cart);
    return () => this.observers.delete(observer);
  }

  notify(cart: CarritoResponseDto | null): void {
    this.cart = cart;
    this.observers.forEach((obs) => obs(cart));
  }

  getCart(): CarritoResponseDto | null {
    return this.cart;
  }
}

export const cartSubject = new CartSubject();
