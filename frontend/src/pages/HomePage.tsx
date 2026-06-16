import { useEffect, useMemo, useState } from 'react';
import { Link } from 'react-router-dom';
import { productoService } from '../services/producto.service';
import { categoriaService } from '../services/categoria.service';
import { varianteService } from '../services/variante.service';
import { Card } from '../components/ui/Card';
import { Badge } from '../components/ui/Badge';
import { ProductImage } from '../components/ui/ProductImage';
import { Spinner } from '../components/ui/Spinner';
import { formatCurrency, filterVariantesByProducto } from '../core/utils/catalogUtils';
import { getErrorMessage } from '../core/api/errorAdapter';
import type { ProductoResponseDto, CategoriaResponseDto, VarianteProductoResponseDto } from '../core/types';

export function HomePage() {
  const [productos, setProductos] = useState<ProductoResponseDto[]>([]);
  const [categorias, setCategorias] = useState<CategoriaResponseDto[]>([]);
  const [variantes, setVariantes] = useState<VarianteProductoResponseDto[]>([]);
  const [categoriaFilter, setCategoriaFilter] = useState<string>('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    Promise.all([productoService.listar(), categoriaService.listar(), varianteService.listar()])
      .then(([prods, cats, vars]) => {
        setProductos(prods);
        setCategorias(cats);
        setVariantes(vars);
      })
      .catch((err) => setError(getErrorMessage(err)))
      .finally(() => setLoading(false));
  }, []);

  const filtered = useMemo(() => {
    if (!categoriaFilter) return productos;
    return productos.filter((p) => p.nombreCategoriaPadre === categoriaFilter);
  }, [productos, categoriaFilter]);

  const categoriaNames = useMemo(
    () => [...new Set(categorias.map((c) => c.nombre))],
    [categorias],
  );

  if (loading) {
    return (
      <div className="flex min-h-[60vh] items-center justify-center">
        <Spinner size="lg" />
      </div>
    );
  }

  return (
    <div className="animate-fade-in">
      <section className="relative overflow-hidden bg-wood-900 px-4 py-20 text-white sm:px-6 lg:px-8">
        <div className="absolute inset-0 bg-[url('data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNjAiIGhlaWdodD0iNjAiIHZpZXdCb3g9IjAgMCA2MCA2MCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48ZyBmaWxsPSJub25lIiBmaWxsLXJ1bGU9ImV2ZW5vZGQiPjxnIGZpbGw9IiNmZmYiIGZpbGwtb3BhY2l0eT0iMC4wMyI+PHBhdGggZD0iTTM2IDM0djItSDI0di0yaDEyek0zNiAyNHYySDI0di0yaDEyeiIvPjwvZz48L2c+PC9zdmc+')] opacity-50" />
        <div className="relative mx-auto max-w-7xl">
          <h1 className="font-serif text-4xl font-bold sm:text-5xl lg:text-6xl">
            Muebles que inspiran
          </h1>
          <p className="mt-4 max-w-xl text-lg text-wood-200">
            Descubrí nuestra colección de muebles premium. Diseño, confort y calidad en cada pieza.
          </p>
        </div>
      </section>

      <section className="mx-auto max-w-7xl px-4 py-12 sm:px-6 lg:px-8">
        {error && (
          <div className="mb-6 rounded-lg bg-red-50 p-4 text-red-700">{error}</div>
        )}

        <div className="mb-8 flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
          <h2 className="font-serif text-2xl font-semibold text-wood-900">Catálogo</h2>
          <select
            value={categoriaFilter}
            onChange={(e) => setCategoriaFilter(e.target.value)}
            className="rounded-lg border border-wood-200 bg-white px-4 py-2 text-sm"
          >
            <option value="">Todas las categorías</option>
            {categoriaNames.map((name) => (
              <option key={name} value={name}>
                {name}
              </option>
            ))}
          </select>
        </div>

        {filtered.length === 0 ? (
          <p className="text-center text-wood-500">No hay productos disponibles.</p>
        ) : (
          <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
            {filtered.map((producto) => (
              <Link key={producto.id} to={`/productos/${producto.id}`}>
                <Card hover className="h-full">
                  <div className="mb-4 h-48 overflow-hidden rounded-lg">
                    <ProductImage
                      urlImagen={producto.urlImagen}
                      nombre={producto.nombre}
                    />
                  </div>
                  <span className="text-xs font-medium uppercase tracking-wider text-accent">
                    {producto.nombreCategoriaPadre}
                  </span>
                  <h3 className="mt-1 font-serif text-xl font-semibold text-wood-900">
                    {producto.nombre}
                  </h3>
                  <p className="mt-2 line-clamp-2 text-sm text-wood-500">{producto.descripcion}</p>
                  <p className="mt-4 text-lg font-semibold text-wood-800">
                    Desde {formatCurrency(producto.precioBase)}
                  </p>
                  {(() => {
                    const varsProducto = filterVariantesByProducto(producto, variantes);
                    const stockTotal = varsProducto.reduce((sum, v) => sum + v.stock, 0);
                    if (varsProducto.length === 0) {
                      return (
                        <Badge className="mt-2 bg-yellow-100 text-yellow-800">
                          Sin variantes — no se puede comprar aún
                        </Badge>
                      );
                    }
                    return (
                      <p className="mt-2 text-xs text-wood-500">
                        {varsProducto.length} color{varsProducto.length > 1 ? 'es' : ''} ·{' '}
                        {stockTotal > 0 ? `${stockTotal} unidades disponibles` : 'Sin stock'}
                      </p>
                    );
                  })()}
                </Card>
              </Link>
            ))}
          </div>
        )}
      </section>
    </div>
  );
}
