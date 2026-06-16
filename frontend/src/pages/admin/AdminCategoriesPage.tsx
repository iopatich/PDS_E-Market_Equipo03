import { useEffect, useState } from 'react';
import { categoriaService } from '../../services/categoria.service';
import { useToast } from '../../core/toast/ToastContext';
import { getErrorMessage } from '../../core/api/errorAdapter';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';
import { Select } from '../../components/ui/Select';
import { Modal } from '../../components/ui/Modal';
import { Spinner } from '../../components/ui/Spinner';
import type { CategoriaResponseDto } from '../../core/types';

export function AdminCategoriesPage() {
  const { showToast } = useToast();
  const [categorias, setCategorias] = useState<CategoriaResponseDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [saving, setSaving] = useState(false);
  const [form, setForm] = useState({ nombre: '', idCategoriaPadre: '' });

  const loadData = async () => {
    const data = await categoriaService.listar();
    setCategorias(data);
  };

  useEffect(() => {
    loadData()
      .catch((err) => showToast(getErrorMessage(err), 'error'))
      .finally(() => setLoading(false));
  }, [showToast]);

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault();
    setSaving(true);
    try {
      await categoriaService.crear({
        nombre: form.nombre,
        idCategoriaPadre: form.idCategoriaPadre ? Number(form.idCategoriaPadre) : undefined,
      });
      showToast('Categoría creada', 'success');
      setShowModal(false);
      setForm({ nombre: '', idCategoriaPadre: '' });
      await loadData();
    } catch (err) {
      showToast(getErrorMessage(err), 'error');
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async (id: number) => {
    if (!confirm('¿Eliminar esta categoría?')) return;
    try {
      await categoriaService.eliminar(id);
      showToast('Categoría eliminada', 'success');
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
          <h2 className="font-serif text-2xl font-bold text-wood-900">Categorías</h2>
          <p className="text-sm text-wood-500">Organización jerárquica de categorías</p>
        </div>
        <Button onClick={() => setShowModal(true)}>Nueva categoría</Button>
      </div>

      <div className="mt-6 overflow-x-auto rounded-xl border border-gray-200 bg-white">
        <table className="w-full text-left text-sm">
          <thead className="border-b bg-gray-50 text-wood-600">
            <tr>
              <th className="px-4 py-3">ID</th>
              <th className="px-4 py-3">Nombre</th>
              <th className="px-4 py-3">Categoría padre</th>
              <th className="px-4 py-3">Acciones</th>
            </tr>
          </thead>
          <tbody>
            {categorias.map((c) => (
              <tr key={c.id} className="border-b hover:bg-gray-50">
                <td className="px-4 py-3">{c.id}</td>
                <td className="px-4 py-3 font-medium">{c.nombre}</td>
                <td className="px-4 py-3">{c.CategoriaPadre ?? '—'}</td>
                <td className="px-4 py-3">
                  <Button size="sm" variant="danger" onClick={() => handleDelete(c.id)}>
                    Eliminar
                  </Button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <Modal open={showModal} onClose={() => setShowModal(false)} title="Nueva categoría">
        <form onSubmit={handleCreate} className="space-y-4">
          <Input
            label="Nombre"
            value={form.nombre}
            onChange={(e) => setForm({ ...form, nombre: e.target.value })}
            required
          />
          <Select
            label="Categoría padre (opcional)"
            value={form.idCategoriaPadre}
            onChange={(e) => setForm({ ...form, idCategoriaPadre: e.target.value })}
            options={categorias.map((c) => ({ value: c.id, label: c.nombre }))}
            placeholder="Sin categoría padre"
          />
          <Button type="submit" loading={saving} className="w-full">
            Crear categoría
          </Button>
        </form>
      </Modal>
    </div>
  );
}
