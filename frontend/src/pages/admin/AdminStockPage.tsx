import { useEffect, useState } from 'react';
import { varianteService } from '../../services/variante.service';
import { useToast } from '../../core/toast/ToastContext';
import { getErrorMessage } from '../../core/api/errorAdapter';
import { formatCurrency } from '../../core/utils/catalogUtils';
import { Button } from '../../components/ui/Button';
import { Badge } from '../../components/ui/Badge';
import { Spinner } from '../../components/ui/Spinner';
import type { VarianteProductoResponseDto } from '../../core/types';

export function AdminStockPage() {
  const { showToast } = useToast();
  const [variantes, setVariantes] = useState<VarianteProductoResponseDto[]>([]);
  const [loading, setLoading] = useState(true);

  const loadData = async () => {
    const data = await varianteService.listar();
    setVariantes(data);
  };

  useEffect(() => {
    loadData()
      .catch((err) => showToast(getErrorMessage(err), 'error'))
      .finally(() => setLoading(false));
  }, [showToast]);

  const handleDelete = async (id: number) => {
    if (!confirm('¿Eliminar esta variante?')) return;
    try {
      await varianteService.eliminar(id);
      showToast('Variante eliminada', 'success');
      await loadData();
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
      <h2 className="font-serif text-2xl font-bold text-wood-900">Inventario</h2>
      <p className="text-sm text-wood-500">Variantes y unidades disponibles</p>

      <div className="mt-6 overflow-x-auto rounded-xl border border-gray-200 bg-white">
        <table className="w-full text-left text-sm">
          <thead className="border-b bg-gray-50 text-wood-600">
            <tr>
              <th className="px-4 py-3">ID</th>
              <th className="px-4 py-3">Color</th>
              <th className="px-4 py-3">Disponibles</th>
              <th className="px-4 py-3">Precio final</th>
              <th className="px-4 py-3">Acciones</th>
            </tr>
          </thead>
          <tbody>
            {variantes.map((v) => (
              <tr key={v.id} className="border-b hover:bg-gray-50">
                <td className="px-4 py-3">{v.id}</td>
                <td className="px-4 py-3 font-medium">{v.color}</td>
                <td className="px-4 py-3">
                  <Badge
                    className={
                      v.stock > 5
                        ? 'bg-green-100 text-green-800'
                        : v.stock > 0
                          ? 'bg-yellow-100 text-yellow-800'
                          : 'bg-red-100 text-red-800'
                    }
                  >
                    {v.stock} unid.
                  </Badge>
                </td>
                <td className="px-4 py-3">{formatCurrency(v.precioFinal)}</td>
                <td className="px-4 py-3">
                  <Button size="sm" variant="danger" onClick={() => handleDelete(v.id)}>
                    Eliminar
                  </Button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
