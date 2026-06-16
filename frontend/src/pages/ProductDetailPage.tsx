import { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { productoService } from '../services/producto.service';
import { varianteService } from '../services/variante.service';
import { carritoService } from '../services/carrito.service';
import { useAuth } from '../core/auth/AuthContext';
import { useCart } from '../core/cart/CartContext';
import { useToast } from '../core/toast/ToastContext';
import { Permiso } from '../core/enums';
import { filterVariantesByProducto, formatCurrency } from '../core/utils/catalogUtils';
import { getErrorMessage } from '../core/api/errorAdapter';
import { Button } from '../components/ui/Button';
import { Spinner } from '../components/ui/Spinner';
import { Badge } from '../components/ui/Badge';
import { Card } from '../components/ui/Card';
import { ProductImage } from '../components/ui/ProductImage';
import type { ProductoResponseDto, VarianteProductoResponseDto } from '../core/types';

export function ProductDetailPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { isAuthenticated, isAdmin, isCliente, hasPermiso } = useAuth();
  const { refreshCart } = useCart();
  const { showToast } = useToast();

  const [producto, setProducto] = useState<ProductoResponseDto | null>(null);
  const [variantes, setVariantes] = useState<VarianteProductoResponseDto[]>([]);
  const [selectedVariante, setSelectedVariante] = useState<VarianteProductoResponseDto | null>(
    null,
  );
  const [cantidad, setCantidad] = useState(1);
  const [loading, setLoading] = useState(true);
  const [adding, setAdding] = useState(false);

  useEffect(() => {
    const productId = Number(id);
    Promise.all([productoService.listar(), varianteService.listar()])
      .then(([productos, vars]) => {
        const found = productos.find((p) => p.id === productId);
        if (!found) throw new Error('Producto no encontrado');
        setProducto(found);
        const productVariantes = filterVariantesByProducto(found, vars);
        setVariantes(productVariantes);
        if (productVariantes.length > 0) setSelectedVariante(productVariantes[0]);
      })
      .catch(() => showToast('Producto no encontrado', 'error'))
      .finally(() => setLoading(false));
  }, [id, showToast]);

  const handleAddToCart = async () => {
    if (!isAuthenticated) {
      showToast('Tenés que ingresar como cliente para comprar', 'info');
      navigate('/ingresar', { state: { from: `/productos/${id}` } });
      return;
    }
    if (isAdmin) {
      showToast('Los administradores no pueden comprar. Usá una cuenta de cliente.', 'error');
      return;
    }
    if (!hasPermiso(Permiso.GESTIONAR_CARRITO)) {
      showToast('Tu cuenta no tiene permiso para usar el carrito', 'error');
      return;
    }
    if (!selectedVariante) {
      showToast('Seleccioná una variante', 'error');
      return;
    }
    if (cantidad > selectedVariante.stock) {
      showToast(`Stock insuficiente. Disponible: ${selectedVariante.stock}`, 'error');
      return;
    }

    setAdding(true);
    try {
      await carritoService.agregarItem({
        idVarianteProducto: selectedVariante.id,
        cantidad,
      });
      await refreshCart();
      showToast('Producto agregado al carrito', 'success');
    } catch (err) {
      showToast(getErrorMessage(err), 'error');
    } finally {
      setAdding(false);
    }
  };

  if (loading) {
    return (
      <div className="flex min-h-[60vh] items-center justify-center">
        <Spinner size="lg" />
      </div>
    );
  }

  if (!producto) {
    return (
      <div className="mx-auto max-w-7xl px-4 py-12 text-center">
        <p className="text-wood-500">Producto no encontrado</p>
        <Button className="mt-4" onClick={() => navigate('/')}>
          Volver al catálogo
        </Button>
      </div>
    );
  }

  const puedeComprar = isCliente && hasPermiso(Permiso.GESTIONAR_CARRITO);

  return (
    <div className="mx-auto max-w-7xl animate-fade-in px-4 py-8 sm:px-6 lg:px-8">
      <div className="grid gap-8 lg:grid-cols-2">
        <div className="aspect-square overflow-hidden rounded-2xl">
          <ProductImage
            urlImagen={producto.urlImagen}
            nombre={producto.nombre}
            fallbackClassName="flex aspect-square items-center justify-center rounded-2xl bg-wood-100 font-serif text-8xl text-wood-300"
          />
        </div>

        <div>
          <Badge className="bg-wood-100 text-wood-700">{producto.nombreCategoriaPadre}</Badge>
          <h1 className="mt-3 font-serif text-3xl font-bold text-wood-900 sm:text-4xl">
            {producto.nombre}
          </h1>
          <p className="mt-4 text-wood-600">{producto.descripcion}</p>
          <p className="mt-6 text-2xl font-semibold text-wood-800">
            Desde {formatCurrency(producto.precioBase)}
          </p>

          {!isAuthenticated && (
            <Card className="mt-6 border-accent/30 bg-accent/5">
              <p className="text-sm text-wood-700">
                Para comprar necesitás una cuenta de <strong>cliente</strong>.
              </p>
              <div className="mt-3 flex flex-wrap gap-2">
                <Link to="/ingresar" state={{ from: `/productos/${id}` }}>
                  <Button size="sm">Ingresar</Button>
                </Link>
                <Link to="/registro">
                  <Button size="sm" variant="outline">
                    Registrarme
                  </Button>
                </Link>
              </div>
            </Card>
          )}

          {isAdmin && (
            <Card className="mt-6 border-yellow-200 bg-yellow-50">
              <p className="text-sm text-yellow-900">
                Estás logueado como <strong>administrador</strong>. Solo las cuentas de cliente
                pueden agregar productos al carrito y realizar compras.
              </p>
              <p className="mt-2 text-sm text-yellow-800">
                Registrate como cliente con otro usuario o cerrá sesión e ingresá con una cuenta de
                cliente.
              </p>
            </Card>
          )}

          {variantes.length > 0 ? (
            <div className="mt-8 space-y-4">
              <div>
                <label className="text-sm font-medium text-wood-700">Elegí el color</label>
                <div className="mt-2 flex flex-wrap gap-2">
                  {variantes.map((v) => (
                    <button
                      key={v.id}
                      type="button"
                      onClick={() => {
                        setSelectedVariante(v);
                        setCantidad(1);
                      }}
                      className={`rounded-lg border-2 px-4 py-2 text-sm transition-all ${
                        selectedVariante?.id === v.id
                          ? 'border-accent bg-accent/10 text-accent-dark'
                          : 'border-wood-200 hover:border-wood-400'
                      }`}
                    >
                      {v.color}
                    </button>
                  ))}
                </div>
              </div>

              {selectedVariante && (
                <>
                  <div className="flex items-center gap-4 text-sm">
                    <span className="font-medium text-wood-700">
                      Precio: {formatCurrency(selectedVariante.precioFinal)}
                    </span>
                    <Badge
                      className={
                        selectedVariante.stock > 0
                          ? 'bg-green-100 text-green-800'
                          : 'bg-red-100 text-red-800'
                      }
                    >
                      Disponibles: {selectedVariante.stock}
                    </Badge>
                  </div>

                  {puedeComprar && selectedVariante.stock > 0 && (
                    <div className="flex items-center gap-4">
                      <label className="text-sm font-medium text-wood-700">Cantidad</label>
                      <div className="flex items-center rounded-lg border border-wood-200">
                        <button
                          type="button"
                          onClick={() => setCantidad((c) => Math.max(1, c - 1))}
                          className="px-3 py-2 hover:bg-wood-50"
                        >
                          −
                        </button>
                        <span className="w-10 text-center">{cantidad}</span>
                        <button
                          type="button"
                          onClick={() =>
                            setCantidad((c) => Math.min(selectedVariante.stock, c + 1))
                          }
                          disabled={cantidad >= selectedVariante.stock}
                          className="px-3 py-2 hover:bg-wood-50 disabled:opacity-50"
                        >
                          +
                        </button>
                      </div>
                    </div>
                  )}

                  <Button
                    size="lg"
                    className="w-full sm:w-auto"
                    onClick={handleAddToCart}
                    loading={adding}
                    disabled={selectedVariante.stock === 0}
                  >
                    {selectedVariante.stock === 0
                      ? 'Sin stock'
                      : !isAuthenticated
                        ? 'Ingresá para comprar'
                        : isAdmin
                          ? 'Solo clientes pueden comprar'
                          : 'Agregar al carrito'}
                  </Button>

                  {puedeComprar && (
                    <p className="text-xs text-wood-500">
                      Después podés ir al carrito y finalizar la compra.
                    </p>
                  )}
                </>
              )}
            </div>
          ) : (
            <Card className="mt-6 border-yellow-200 bg-yellow-50">
              <p className="font-medium text-yellow-900">Este producto no tiene variantes cargadas</p>
              <p className="mt-2 text-sm text-yellow-800">
                En el backend, un producto necesita al menos una variante (color + stock + precio)
                para poder venderse.
              </p>
              <p className="mt-2 text-sm text-yellow-800">
                Si sos administrador, andá a{' '}
                <Link to="/admin/productos" className="font-medium text-accent underline">
                  Administración → Productos
                </Link>{' '}
                y usá el botón <strong>+ Agregar variante</strong> en este producto.
              </p>
            </Card>
          )}
        </div>
      </div>
    </div>
  );
}
