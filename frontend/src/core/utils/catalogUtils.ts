import type { ProductoResponseDto, VarianteProductoResponseDto } from '../types';

/**
 * Relaciona variantes con productos cuando el DTO no trae idProducto.
 * Usa la fórmula: precioFinal = precioBase + precioAdicional.
 */
export function filterVariantesByProducto(
  producto: ProductoResponseDto,
  variantes: VarianteProductoResponseDto[],
): VarianteProductoResponseDto[] {
  return variantes.filter(
    (v) => Math.abs(v.precioFinal - (producto.precioBase + v.precioAdicional)) < 0.01,
  );
}

export function formatCurrency(value: number): string {
  return new Intl.NumberFormat('es-AR', {
    style: 'currency',
    currency: 'ARS',
    minimumFractionDigits: 0,
  }).format(value);
}

export function getEstadoPedidoLabel(estado: string): string {
  const labels: Record<string, string> = {
    PENDIENTE: 'Pendiente',
    PAGADO: 'Pagado',
    ENVIADO: 'Enviado',
    ENTREGADO: 'Entregado',
  };
  return labels[estado] ?? estado;
}

export function getEstadoPedidoColor(estado: string): string {
  const colors: Record<string, string> = {
    PENDIENTE: 'bg-yellow-100 text-yellow-800',
    PAGADO: 'bg-blue-100 text-blue-800',
    ENVIADO: 'bg-purple-100 text-purple-800',
    ENTREGADO: 'bg-green-100 text-green-800',
  };
  return colors[estado] ?? 'bg-gray-100 text-gray-800';
}
