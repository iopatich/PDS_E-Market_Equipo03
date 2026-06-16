import { NavLink, Outlet, useNavigate } from 'react-router-dom';
import { useAuth } from '../../core/auth/AuthContext';

const navItems = [
  { to: '/admin', label: 'Panel', end: true },
  { to: '/admin/productos', label: 'Productos' },
  { to: '/admin/categorias', label: 'Categorías' },
  { to: '/admin/pedidos', label: 'Pedidos' },
  { to: '/admin/stock', label: 'Inventario' },
  { to: '/admin/clientes', label: 'Clientes' },
  { to: '/admin/administradores', label: 'Administradores' },
];

export function AdminLayout() {
  const { logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = async () => {
    await logout();
    navigate('/ingresar');
  };

  return (
    <div className="flex min-h-screen bg-gray-100">
      <aside className="fixed inset-y-0 left-0 z-30 w-64 bg-wood-900 text-white">
        <div className="flex h-16 items-center border-b border-wood-700 px-6">
          <span className="font-serif text-xl font-bold">E-Market · Administración</span>
        </div>
        <nav className="space-y-1 p-4">
          {navItems.map((item) => (
            <NavLink
              key={item.to}
              to={item.to}
              end={item.end}
              className={({ isActive }) =>
                `block rounded-lg px-4 py-2.5 text-sm font-medium transition-colors ${
                  isActive ? 'bg-accent text-white' : 'text-wood-300 hover:bg-wood-800 hover:text-white'
                }`
              }
            >
              {item.label}
            </NavLink>
          ))}
        </nav>
        <div className="absolute bottom-0 w-full border-t border-wood-700 p-4">
          <NavLink
            to="/"
            className="mb-2 block rounded-lg px-4 py-2 text-sm text-wood-300 hover:bg-wood-800"
          >
            Ver tienda
          </NavLink>
          <button
            type="button"
            onClick={handleLogout}
            className="w-full rounded-lg px-4 py-2 text-left text-sm text-wood-300 hover:bg-wood-800"
          >
            Cerrar sesión
          </button>
        </div>
      </aside>
      <div className="ml-64 flex-1">
        <header className="sticky top-0 z-20 border-b border-gray-200 bg-white px-8 py-4">
          <h1 className="text-lg font-semibold text-wood-900">Panel de administración</h1>
        </header>
        <div className="p-8">
          <Outlet />
        </div>
      </div>
    </div>
  );
}
