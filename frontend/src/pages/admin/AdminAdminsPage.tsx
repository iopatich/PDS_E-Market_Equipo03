import { useEffect, useState } from 'react';
import { administradorService } from '../../services/administrador.service';
import { useToast } from '../../core/toast/ToastContext';
import { getErrorMessage } from '../../core/api/errorAdapter';
import { obtenerEtiquetaPermiso } from '../../core/utils/permisosUtils';
import { Button } from '../../components/ui/Button';
import { Badge } from '../../components/ui/Badge';
import { Spinner } from '../../components/ui/Spinner';
import type { AdministradorResponseDto } from '../../core/types';

export function AdminAdminsPage() {
  const { showToast } = useToast();
  const [admins, setAdmins] = useState<AdministradorResponseDto[]>([]);
  const [loading, setLoading] = useState(true);

  const loadData = async () => {
    const data = await administradorService.listar();
    setAdmins(data);
  };

  useEffect(() => {
    loadData()
      .catch((err) => showToast(getErrorMessage(err), 'error'))
      .finally(() => setLoading(false));
  }, [showToast]);

  const handleDelete = async (id: number) => {
    if (!confirm('¿Eliminar este administrador?')) return;
    try {
      const response = await administradorService.eliminar(id);
      showToast(response.mensaje, 'success');
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
      <h2 className="font-serif text-2xl font-bold text-wood-900">Administradores</h2>
      <p className="text-sm text-wood-500">Listado de administradores del sistema</p>

      <div className="mt-6 overflow-x-auto rounded-xl border border-gray-200 bg-white">
        <table className="w-full text-left text-sm">
          <thead className="border-b bg-gray-50 text-wood-600">
            <tr>
              <th className="px-4 py-3">ID</th>
              <th className="px-4 py-3">Usuario</th>
              <th className="px-4 py-3">Permisos</th>
              <th className="px-4 py-3">Acciones</th>
            </tr>
          </thead>
          <tbody>
            {admins.map((a) => (
              <tr key={a.id} className="border-b hover:bg-gray-50">
                <td className="px-4 py-3">{a.id}</td>
                <td className="px-4 py-3 font-medium">{a.username}</td>
                <td className="px-4 py-3">
                  <div className="flex flex-wrap gap-1">
                    {a.permisos.map((p) => (
                      <Badge key={p} className="bg-wood-100 text-wood-600 text-xs">
                        {obtenerEtiquetaPermiso(p)}
                      </Badge>
                    ))}
                  </div>
                </td>
                <td className="px-4 py-3">
                  <Button size="sm" variant="danger" onClick={() => handleDelete(a.id)}>
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
