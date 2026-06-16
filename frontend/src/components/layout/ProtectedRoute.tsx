import { Navigate, useLocation } from 'react-router-dom';
import { useAuth } from '../../core/auth/AuthContext';
import { Permiso } from '../../core/enums';
import { Spinner } from '../ui/Spinner';

interface ProtectedRouteProps {
  children: React.ReactNode;
  permiso?: Permiso;
  clienteOnly?: boolean;
}

export function ProtectedRoute({ children, permiso, clienteOnly }: ProtectedRouteProps) {
  const { isAuthenticated, loading, hasPermiso, isCliente } = useAuth();
  const location = useLocation();

  if (loading) {
    return (
      <div className="flex min-h-[50vh] items-center justify-center">
        <Spinner size="lg" />
      </div>
    );
  }

  if (!isAuthenticated) {
    return <Navigate to="/ingresar" state={{ from: location }} replace />;
  }

  if (permiso && !hasPermiso(permiso)) {
    return <Navigate to="/" replace />;
  }

  if (clienteOnly && !isCliente) {
    return <Navigate to="/admin" replace />;
  }

  return <>{children}</>;
}
