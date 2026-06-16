import { useEffect, useMemo, useState } from 'react';
import { Link } from 'react-router-dom';
import { productoService } from '../services/producto.service';
import { categoriaService } from '../services/categoria.service';
import { varianteService } from '../services/variante.service';
import { Card } from '../components/ui/Card';
import { Badge } from '../components/ui/Badge';
import { Button } from '../components/ui/Button'; // Importamos tu Botón
import { Modal } from '../components/ui/Modal';   // Importamos tu Modal
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
  
  // NUEVO ESTADO: Controla si el modal de filtros está abierto
  const [showFilters, setShowFilters] = useState(false);
  
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

  const getSubcategoriasNombres = (nombreCat: string, allCats: CategoriaResponseDto[]): string[] => {
    const hijas = allCats.filter(c => c.CategoriaPadre === nombreCat).map(c => c.nombre);
    let resultado = [nombreCat, ...hijas];
    hijas.forEach(hija => {
      resultado = [...resultado, ...getSubcategoriasNombres(hija, allCats)];
    });
    return resultado;
  };

  const filtered = useMemo(() => {
    if (!categoriaFilter) return productos;
    const categoriasValidas = getSubcategoriasNombres(categoriaFilter, categorias);
    return productos.filter((p) => categoriasValidas.includes(p.nombreCategoriaPadre));
  }, [productos, categoriaFilter, categorias]);

  const categoriasRaiz = useMemo(() => categorias.filter((c) => !c.CategoriaPadre), [categorias]);

  const renderJerarquiaCategoria = (categoria: CategoriaResponseDto, nivel: number = 0) => {
    const subcategorias = categorias.filter(c => c.CategoriaPadre === categoria.nombre);

    return (
      <div key={categoria.id} className="flex flex-col w-full">
        <button
          onClick={() => {
            setCategoriaFilter(categoria.nombre);
            setShowFilters(false); // Cierra el modal automáticamente al elegir
          }}
          className={`text-left px-2 py-1.5 text-sm transition-colors rounded ${
            categoriaFilter === categoria.nombre
              ? 'bg-wood-200 text-wood-900 font-semibold'
              : 'text-wood-600 hover:bg-wood-100 hover:text-wood-900'
          }`}
        >
          <span style={{ marginLeft: `${nivel * 1.5}rem` }}>
            {nivel > 0 && "↳ "} {categoria.nombre}
          </span>
        </button>

        {subcategorias.length > 0 && (
          <div className="flex flex-col mt-1 mb-1 border-l-2 border-wood-100 ml-2">
            {subcategorias.map(sub => renderJerarquiaCategoria(sub, nivel + 1))}
          </div>
        )}
      </div>
    );
  };

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

        <div className="flex flex-col">
          {/* HEADER DEL CATÁLOGO + BOTÓN DE FILTROS */}
          <div className="mb-6 flex flex-col sm:flex-row sm:items-center justify-between border-b border-wood-100 pb-4 gap-4">
            <div>
              <h2 className="font-serif text-2xl font-semibold text-wood-900">
                {categoriaFilter ? `Catálogo: ${categoriaFilter}` : 'Catálogo Completo'}
              </h2>
              <span className="text-sm text-wood-500 font-medium">
                {filtered.length} producto{filtered.length !== 1 ? 's' : ''} encontrados
              </span>
            </div>
            
            <Button variant="secondary" onClick={() => setShowFilters(true)}>
              <span className="flex items-center gap-2">
                <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 4a1 1 0 011-1h16a1 1 0 011 1v2.586a1 1 0 01-.293.707l-6.414 6.414a1 1 0 00-.293.707V17l-4 4v-6.586a1 1 0 00-.293-.707L3.293 7.293A1 1 0 013 6.586V4z" />
                </svg>
                Filtrar Catálogo
              </span>
            </Button>
          </div>

          {/* GRILLA DE PRODUCTOS (Ahora ocupa todo el ancho: nota el xl:grid-cols-4) */}
          {filtered.length === 0 ? (
            <div className="text-center bg-wood-50 py-12 rounded-xl border border-wood-100">
              <p className="text-wood-500 text-lg">No hay productos en esta categoría.</p>
            </div>
          ) : (
            <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
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
        </div>
      </section>

      {/* COMPONENTE MODAL DE FILTROS */}
      <Modal 
        open={showFilters} 
        onClose={() => setShowFilters(false)} 
        title="Filtrar por Categoría"
        size="sm"
      >
        <div className="p-2">
          <div className="flex flex-col gap-2">
            <button
              onClick={() => {
                setCategoriaFilter('');
                setShowFilters(false);
              }}
              className={`text-left px-4 py-2 rounded-lg text-sm font-medium transition-colors border ${
                categoriaFilter === ''
                  ? 'bg-wood-800 text-white border-wood-800'
                  : 'text-wood-700 bg-white hover:bg-wood-50 border-wood-200'
              }`}
            >
              Ver todo el catálogo
            </button>
            <div className="mt-2 flex flex-col gap-1 p-2 bg-wood-50 rounded-lg border border-wood-100">
              {categoriasRaiz.map(cat => renderJerarquiaCategoria(cat))}
            </div>
          </div>
        </div>
      </Modal>
    </div>
  );
}