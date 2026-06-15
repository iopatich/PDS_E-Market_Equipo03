import { useEffect, useState } from 'react';
import { clienteService } from '../../services/cliente.service';
import { administradorService } from '../../services/administrador.service';
import { useToast } from '../../core/toast/ToastContext';
import { getErrorMessage } from '../../core/api/errorAdapter';
import { obtenerEtiquetaPermiso } from '../../core/utils/permisosUtils';
import { Button } from '../../components/ui/Button';
import { Badge } from '../../components/ui/Badge';
import { Spinner } from '../../components/ui/Spinner';
import type { ClienteResponseDto } from '../../core/types';

export function AdminClientsPage() {
  const { showToast } = useToast();
  const [clientes, setClientes] = useState<ClienteResponseDto[]>([]);
  const [loading, setLoading] = useState(true);

  const loadData = async () => {
    const data = await clienteService.listar();
    setClientes(data);
  };

  useEffect(() => {
    loadData()
      .catch((err) => showToast(getErrorMessage(err), 'error'))
      .finally(() => setLoading(false));
  }, [showToast]);

  const handleDeactivate = async (id: number) => {
    if (!confirm('¿Dar de baja a este cliente?')) return;
    try {
      const response = await administradorService.darDeBajaCliente(id);
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
      <h2 className="font-serif text-2xl font-bold text-wood-900">Clientes</h2>
      <p className="text-sm text-wood-500">Listado y baja de clientes</p>

      <div className="mt-6 overflow-x-auto rounded-xl border border-gray-200 bg-white">
        <table className="w-full text-left text-sm">
          <thead className="border-b bg-gray-50 text-wood-600">
            <tr>
              <th className="px-4 py-3">ID</th>
              <th className="px-4 py-3">Usuario</th>
              <th className="px-4 py-3">Correo</th>
              <th className="px-4 py-3">Permisos</th>
              <th className="px-4 py-3">Acciones</th>
            </tr>
          </thead>
          <tbody>
            {clientes.map((c) => (
              <tr key={c.id} className="border-b hover:bg-gray-50">
                <td className="px-4 py-3">{c.id}</td>
                <td className="px-4 py-3 font-medium">{c.username}</td>
                <td className="px-4 py-3">{c.email}</td>
                <td className="px-4 py-3">
                  <div className="flex flex-wrap gap-1">
                    {c.permisos.map((p) => (
                      <Badge key={p} className="bg-wood-100 text-wood-600 text-xs">
                        {obtenerEtiquetaPermiso(p)}
                      </Badge>
                    ))}
                  </div>
                </td>
                <td className="px-4 py-3">
                  <Button size="sm" variant="danger" onClick={() => handleDeactivate(c.id)}>
                    Dar de baja
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
