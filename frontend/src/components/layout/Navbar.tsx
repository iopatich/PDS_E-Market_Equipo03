import { Link, NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../../core/auth/AuthContext';
import { useCart } from '../../core/cart/CartContext';
import { Permiso } from '../../core/enums';

export function Navbar() {
  const { isAuthenticated, isAdmin, session, logout, hasPermiso } = useAuth();
  const { itemCount } = useCart();
  const navigate = useNavigate();

  const handleLogout = async () => {
    await logout();
    navigate('/');
  };

  const linkClass = ({ isActive }: { isActive: boolean }) =>
    `text-sm font-medium transition-colors ${isActive ? 'text-accent' : 'text-wood-600 hover:text-wood-900'}`;

  return (
    <header className="sticky top-0 z-40 border-b border-wood-100 bg-white/95 backdrop-blur-md">
      <div className="mx-auto flex h-16 max-w-7xl items-center justify-between px-4 sm:px-6 lg:px-8">
        <Link to="/" className="flex items-center gap-2">
          <span className="font-serif text-2xl font-bold text-wood-900">E-Market</span>
          <span className="hidden text-xs uppercase tracking-widest text-accent sm:inline">
            Muebles
          </span>
        </Link>

        <nav className="hidden items-center gap-6 md:flex">
          <NavLink to="/" className={linkClass} end>
            Catálogo
          </NavLink>
          {isAuthenticated && hasPermiso(Permiso.GESTIONAR_CARRITO) && (
            <NavLink to="/carrito" className={linkClass}>
              Carrito
              {itemCount > 0 && (
                <span className="ml-1 inline-flex h-5 min-w-5 items-center justify-center rounded-full bg-accent px-1 text-xs text-white">
                  {itemCount}
                </span>
              )}
            </NavLink>
          )}
          {isAuthenticated && hasPermiso(Permiso.REALIZAR_COMPRA) && (
            <NavLink to="/mis-pedidos" className={linkClass}>
              Mis pedidos
            </NavLink>
          )}
          {isAdmin && (
            <NavLink to="/admin" className={linkClass}>
              Administración
            </NavLink>
          )}
        </nav>

        <div className="flex items-center gap-3">
          {isAuthenticated ? (
            <>
              <Link
                to="/perfil"
                className="hidden text-sm text-wood-600 hover:text-wood-900 sm:inline"
              >
                {session?.username}
              </Link>
              <button
                type="button"
                onClick={handleLogout}
                className="rounded-lg px-3 py-1.5 text-sm font-medium text-wood-600 hover:bg-wood-50"
              >
                Salir
              </button>
            </>
          ) : (
            <>
              <Link
                to="/ingresar"
                className="rounded-lg px-3 py-1.5 text-sm font-medium text-wood-600 hover:bg-wood-50"
              >
                Ingresar
              </Link>
              <Link
                to="/registro"
                className="rounded-lg bg-wood-800 px-4 py-1.5 text-sm font-medium text-white hover:bg-wood-700"
              >
                Registrarse
              </Link>
            </>
          )}
        </div>
      </div>
    </header>
  );
}
