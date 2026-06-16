import { TipoPago } from '../enums';
import type { ConfirmarCompraRequestDto } from '../types';

/**
 * Estrategias de pago disponibles al finalizar la compra.
 * Replica la lógica de MetodoPago del backend (Tarjeta / Efectivo / Transferencia).
 */
export interface PaymentStrategy {
  readonly tipo: TipoPago;
  readonly label: string;
  readonly description: string;
  buildRequest(): ConfirmarCompraRequestDto;
}

class TarjetaStrategy implements PaymentStrategy {
  readonly tipo = TipoPago.TARJETA;
  readonly label = 'Tarjeta de crédito/débito';
  readonly description = 'Se debita al confirmar el pedido';

  buildRequest(): ConfirmarCompraRequestDto {
    return { tipoPago: TipoPago.TARJETA };
  }
}

class EfectivoStrategy implements PaymentStrategy {
  readonly tipo = TipoPago.EFECTIVO;
  readonly label = 'Efectivo';
  readonly description = 'Abonás al momento de recibir el pedido';

  buildRequest(): ConfirmarCompraRequestDto {
    return { tipoPago: TipoPago.EFECTIVO };
  }
}

class TransferenciaStrategy implements PaymentStrategy {
  readonly tipo = TipoPago.TRANSFERENCIA;
  readonly label = 'Transferencia';
  readonly description = 'Se abona por transferencia para confirmar el pedido';

  buildRequest(): ConfirmarCompraRequestDto {
    return { tipoPago: TipoPago.TRANSFERENCIA };
  }
}

const strategies: PaymentStrategy[] = [new TarjetaStrategy(), new EfectivoStrategy(), new TransferenciaStrategy()];

export function getPaymentStrategies(): PaymentStrategy[] {
  return strategies;
}

export function getPaymentStrategy(tipo: TipoPago): PaymentStrategy {
  const strategy = strategies.find((s) => s.tipo === tipo);
  if (!strategy) throw new Error(`Método de pago no soportado: ${tipo}`);
  return strategy;
}
