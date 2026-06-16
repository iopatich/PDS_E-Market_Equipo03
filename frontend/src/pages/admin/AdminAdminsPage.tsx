import { useEffect, useState } from 'react';
import { administradorService } from '../../services/administrador.service';
import { useAuth } from '../../core/auth/AuthContext';
import { useToast } from '../../core/toast/ToastContext';
import { adaptApiError, getErrorMessage } from '../../core/api/errorAdapter';
import { obtenerEtiquetaPermiso } from '../../core/utils/permisosUtils';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';
import { Modal } from '../../components/ui/Modal';
import { Badge } from '../../components/ui/Badge';
import { Spinner } from '../../components/ui/Spinner';
import type { AdministradorResponseDto } from '../../core/types';

export function AdminAdminsPage() {
  const { session } = useAuth();
  const { showToast } = useToast();
  const [admins, setAdmins] = useState<AdministradorResponseDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [saving, setSaving] = useState(false);
  const [form, setForm] = useState({ username: '', password: '', confirmPassword: '' });
  const [errors, setErrors] = useState<Record<string, string>>({});

  const loadData = async () => {
    const data = await administradorService.listar();
    setAdmins(data);
  };

  useEffect(() => {
    loadData()
      .catch((err) => showToast(getErrorMessage(err), 'error'))
      .finally(() => setLoading(false));
  }, [showToast]);

  const resetForm = () => {
    setForm({ username: '', password: '', confirmPassword: '' });
    setErrors({});
  };

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault();
    setErrors({});

    if (form.password !== form.confirmPassword) {
      setErrors({ confirmPassword: 'Las contraseñas no coinciden' });
      return;
    }

    setSaving(true);
    try {
      await administradorService.registrar({
        username: form.username,
        password: form.password,
      });
      showToast('Administrador creado', 'success');
      setShowModal(false);
      resetForm();
      await loadData();
    } catch (err) {
      const appError = adaptApiError(err);
      if (appError.fieldErrors) setErrors(appError.fieldErrors);
      showToast(getErrorMessage(err), 'error');
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async (id: number) => {
    if (id === session?.id) {
      showToast('No podés eliminar tu propia cuenta', 'error');
      return;
    }
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
      <div className="flex items-center justify-between">
        <div>
          <h2 className="font-serif text-2xl font-bold text-wood-900">Administradores</h2>
          <p className="text-sm text-wood-500">Alta, listado y baja de administradores</p>
        </div>
        <Button onClick={() => { resetForm(); setShowModal(true); }}>
          Nuevo administrador
        </Button>
      </div>

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
            {admins.length === 0 ? (
              <tr>
                <td colSpan={4} className="px-4 py-8 text-center text-wood-500">
                  No hay administradores registrados
                </td>
              </tr>
            ) : (
              admins.map((a) => (
                <tr key={a.id} className="border-b hover:bg-gray-50">
                  <td className="px-4 py-3">{a.id}</td>
                  <td className="px-4 py-3 font-medium">
                    {a.username}
                    {a.id === session?.id && (
                      <Badge className="ml-2 bg-accent/10 text-accent text-xs">Vos</Badge>
                    )}
                  </td>
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
                    <Button
                      size="sm"
                      variant="danger"
                      disabled={a.id === session?.id}
                      onClick={() => handleDelete(a.id)}
                    >
                      Eliminar
                    </Button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      <Modal
        open={showModal}
        onClose={() => { setShowModal(false); resetForm(); }}
        title="Nuevo administrador"
        size="sm"
      >
        <form onSubmit={handleCreate} className="space-y-4">
          <Input
            label="Usuario"
            value={form.username}
            onChange={(e) => setForm((prev) => ({ ...prev, username: e.target.value }))}
            error={errors.username}
            required
            minLength={3}
            maxLength={50}
          />
          <Input
            label="Contraseña"
            type="password"
            value={form.password}
            onChange={(e) => setForm((prev) => ({ ...prev, password: e.target.value }))}
            error={errors.password}
            required
            minLength={6}
          />
          <Input
            label="Confirmar contraseña"
            type="password"
            value={form.confirmPassword}
            onChange={(e) => setForm((prev) => ({ ...prev, confirmPassword: e.target.value }))}
            error={errors.confirmPassword}
            required
            minLength={6}
          />
          <div className="flex justify-end gap-2 pt-2">
            <Button type="button" variant="secondary" onClick={() => { setShowModal(false); resetForm(); }}>
              Cancelar
            </Button>
            <Button type="submit" loading={saving}>
              Crear
            </Button>
          </div>
        </form>
      </Modal>
    </div>
  );
}
