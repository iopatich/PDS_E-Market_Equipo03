import { useEffect, useState } from 'react';
import { productoService } from '../../services/producto.service';
import { categoriaService } from '../../services/categoria.service';
import { varianteService } from '../../services/variante.service';
import { useToast } from '../../core/toast/ToastContext';
import { getErrorMessage } from '../../core/api/errorAdapter';
import { formatCurrency } from '../../core/utils/catalogUtils';
import { archivoImagenADataUrl, validarArchivoImagen } from '../../core/utils/imagenUtils';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';
import { Select } from '../../components/ui/Select';
import { Modal } from '../../components/ui/Modal';
import { Spinner } from '../../components/ui/Spinner';
import { ProductImage } from '../../components/ui/ProductImage';
import type { ProductoResponseDto, CategoriaResponseDto } from '../../core/types';

export function AdminProductsPage() {
  const { showToast } = useToast();
  const [productos, setProductos] = useState<ProductoResponseDto[]>([]);
  const [categorias, setCategorias] = useState<CategoriaResponseDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [showProductModal, setShowProductModal] = useState(false);
  const [showVarianteModal, setShowVarianteModal] = useState(false);
  const [selectedProducto, setSelectedProducto] = useState<ProductoResponseDto | null>(null);
  const [saving, setSaving] = useState(false);

  const [productForm, setProductForm] = useState({
    nombre: '',
    descripcion: '',
    precioBase: '',
    idCategoriaPadre: '',
    urlImagen: '' as string,
  });
  const [imagenPreview, setImagenPreview] = useState<string | null>(null);
  const [imagenError, setImagenError] = useState('');

  const [varianteForm, setVarianteForm] = useState({
    color: '',
    stock: '',
    precio: '',
  });

  const loadData = async () => {
    const [prods, cats] = await Promise.all([
      productoService.listar(),
      categoriaService.listar(),
    ]);
    setProductos(prods);
    setCategorias(cats);
  };

  useEffect(() => {
    loadData()
      .catch((err) => showToast(getErrorMessage(err), 'error'))
      .finally(() => setLoading(false));
  }, [showToast]);

  const resetProductForm = () => {
    setProductForm({ nombre: '', descripcion: '', precioBase: '', idCategoriaPadre: '', urlImagen: '' });
    setImagenPreview(null);
    setImagenError('');
  };

  const handleImagenChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const archivo = e.target.files?.[0];
    if (!archivo) return;

    const error = validarArchivoImagen(archivo);
    if (error) {
      setImagenError(error);
      setImagenPreview(null);
      setProductForm((prev) => ({ ...prev, urlImagen: '' }));
      return;
    }

    setImagenError('');
    try {
      const dataUrl = await archivoImagenADataUrl(archivo);
      setImagenPreview(dataUrl);
      setProductForm((prev) => ({ ...prev, urlImagen: dataUrl }));
    } catch {
      setImagenError('No se pudo cargar la imagen');
    }
  };

  const handleCreateProduct = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!productForm.urlImagen) {
      setImagenError('La imagen del producto es obligatoria');
      return;
    }
    setSaving(true);
    try {
      await productoService.crear({
        nombre: productForm.nombre,
        descripcion: productForm.descripcion,
        precioBase: Number(productForm.precioBase),
        idCategoriaPadre: Number(productForm.idCategoriaPadre),
        urlImagen: productForm.urlImagen,
      });
      showToast('Producto creado', 'success');
      setShowProductModal(false);
      resetProductForm();
      await loadData();
    } catch (err) {
      showToast(getErrorMessage(err), 'error');
    } finally {
      setSaving(false);
    }
  };

  const handleCreateVariante = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedProducto) return;
    setSaving(true);
    try {
      await varianteService.crear({
        color: varianteForm.color,
        stock: Number(varianteForm.stock),
        precio: Number(varianteForm.precio),
        idProducto: selectedProducto.id,
      });
      showToast('Variante creada. El producto ya se puede comprar.', 'success');
      setShowVarianteModal(false);
      setVarianteForm({ color: '', stock: '', precio: '' });
      await loadData();
    } catch (err) {
      showToast(getErrorMessage(err), 'error');
    } finally {
      setSaving(false);
    }
  };

  const handleDeleteProduct = async (id: number) => {
    if (!confirm('¿Eliminar este producto?')) return;
    try {
      await productoService.eliminar(id);
      showToast('Producto eliminado', 'success');
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
          <h2 className="font-serif text-2xl font-bold text-wood-900">Productos</h2>
          <p className="text-sm text-wood-500">Gestión de productos y variantes</p>
        </div>
        <Button onClick={() => { resetProductForm(); setShowProductModal(true); }}>Nuevo producto</Button>
      </div>

      <div className="mt-6 overflow-x-auto rounded-xl border border-gray-200 bg-white">
        <table className="w-full text-left text-sm">
          <thead className="border-b bg-gray-50 text-wood-600">
            <tr>
              <th className="px-4 py-3">Foto</th>
              <th className="px-4 py-3">ID</th>
              <th className="px-4 py-3">Nombre</th>
              <th className="px-4 py-3">Categoría</th>
              <th className="px-4 py-3">Precio base</th>
              <th className="px-4 py-3">Acciones</th>
            </tr>
          </thead>
          <tbody>
            {productos.map((p) => (
              <tr key={p.id} className="border-b hover:bg-gray-50">
                <td className="px-4 py-3">
                  <div className="h-12 w-12 overflow-hidden rounded-lg">
                    <ProductImage urlImagen={p.urlImagen} nombre={p.nombre} className="h-full w-full object-cover" />
                  </div>
                </td>
                <td className="px-4 py-3">{p.id}</td>
                <td className="px-4 py-3 font-medium">{p.nombre}</td>
                <td className="px-4 py-3">{p.nombreCategoriaPadre}</td>
                <td className="px-4 py-3">{formatCurrency(p.precioBase)}</td>
                <td className="px-4 py-3">
                  <div className="flex gap-2">
                    <Button
                      size="sm"
                      variant="outline"
                      onClick={() => {
                        setSelectedProducto(p);
                        setShowVarianteModal(true);
                      }}
                    >
                      + Agregar variante
                    </Button>
                    <Button size="sm" variant="danger" onClick={() => handleDeleteProduct(p.id)}>
                      Eliminar
                    </Button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <Modal open={showProductModal} onClose={() => { setShowProductModal(false); resetProductForm(); }} title="Nuevo producto">
        <form onSubmit={handleCreateProduct} className="space-y-4">
          <div>
            <label className="text-sm font-medium text-wood-700">Foto del producto</label>
            <input
              type="file"
              accept="image/jpeg,image/png,image/webp,image/gif"
              onChange={handleImagenChange}
              className="mt-1 block w-full text-sm text-wood-600 file:mr-3 file:rounded-lg file:border-0 file:bg-wood-100 file:px-3 file:py-2 file:text-sm file:font-medium file:text-wood-700 hover:file:bg-wood-200"
              required
            />
            <p className="mt-1 text-xs text-wood-500">JPG, PNG, WEBP o GIF. Máximo 2 MB.</p>
            {imagenError && <p className="mt-1 text-xs text-red-600">{imagenError}</p>}
            {imagenPreview && (
              <div className="mt-3 h-40 w-full overflow-hidden rounded-lg border border-wood-200">
                <img src={imagenPreview} alt="Vista previa" className="h-full w-full object-cover" />
              </div>
            )}
          </div>
          <Input
            label="Nombre"
            value={productForm.nombre}
            onChange={(e) => setProductForm({ ...productForm, nombre: e.target.value })}
            required
          />
          <Input
            label="Descripción"
            value={productForm.descripcion}
            onChange={(e) => setProductForm({ ...productForm, descripcion: e.target.value })}
            required
          />
          <Input
            label="Precio base"
            type="number"
            min="0.01"
            step="0.01"
            value={productForm.precioBase}
            onChange={(e) => setProductForm({ ...productForm, precioBase: e.target.value })}
            required
          />
          <Select
            label="Categoría padre"
            value={productForm.idCategoriaPadre}
            onChange={(e) => setProductForm({ ...productForm, idCategoriaPadre: e.target.value })}
            options={categorias.map((c) => ({ value: c.id, label: c.nombre }))}
            placeholder="Seleccionar categoría"
            required
          />
          <Button type="submit" loading={saving} className="w-full">
            Crear producto
          </Button>
        </form>
      </Modal>

      <Modal
        open={showVarianteModal}
        onClose={() => setShowVarianteModal(false)}
        title={`Nueva variante — ${selectedProducto?.nombre ?? ''}`}
      >
        <form onSubmit={handleCreateVariante} className="space-y-4">
          <Input
            label="Color"
            value={varianteForm.color}
            onChange={(e) => setVarianteForm({ ...varianteForm, color: e.target.value })}
            required
          />
          <Input
            label="Unidades en stock"
            type="number"
            min="0"
            value={varianteForm.stock}
            onChange={(e) => setVarianteForm({ ...varianteForm, stock: e.target.value })}
            required
          />
          <Input
            label="Precio adicional"
            type="number"
            min="0.01"
            step="0.01"
            value={varianteForm.precio}
            onChange={(e) => setVarianteForm({ ...varianteForm, precio: e.target.value })}
            required
          />
          <Button type="submit" loading={saving} className="w-full">
            Crear variante
          </Button>
        </form>
      </Modal>
    </div>
  );
}
