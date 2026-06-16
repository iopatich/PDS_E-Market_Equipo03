import { useEffect, useState } from 'react';
import { authService } from '../services/auth.service';
import { useAuth } from '../core/auth/AuthContext';
import { Card } from '../components/ui/Card';
import { Spinner } from '../components/ui/Spinner';
import { Badge } from '../components/ui/Badge';
import type { PerfilResponse } from '../core/types';
import { getErrorMessage } from '../core/api/errorAdapter';
import { obtenerEtiquetaPermiso } from '../core/utils/permisosUtils';

function isCliente(perfil: PerfilResponse): perfil is import('../core/types').ClienteResponseDto {
  return 'email' in perfil;
}

export function ProfilePage() {
  const { session } = useAuth();
  const [perfil, setPerfil] = useState<PerfilResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    authService
      .getPerfil()
      .then(setPerfil)
      .catch((err) => setError(getErrorMessage(err)))
      .finally(() => setLoading(false));
  }, []);

  if (loading) {
    return (
      <div className="flex min-h-[60vh] items-center justify-center">
        <Spinner size="lg" />
      </div>
    );
  }

  return (
    <div className="mx-auto max-w-2xl animate-fade-in px-4 py-8 sm:px-6">
      <h1 className="font-serif text-3xl font-bold text-wood-900">Mi perfil</h1>

      {error && <div className="mt-4 rounded-lg bg-red-50 p-4 text-red-700">{error}</div>}

      {perfil && (
        <Card className="mt-8">
          <dl className="space-y-4">
            <div>
              <dt className="text-sm text-wood-500">Usuario</dt>
              <dd className="font-medium text-wood-900">{perfil.username}</dd>
            </div>
            {isCliente(perfil) && (
              <div>
                <dt className="text-sm text-wood-500">Correo electrónico</dt>
                <dd className="font-medium text-wood-900">{perfil.email}</dd>
              </div>
            )}
            <div>
              <dt className="text-sm text-wood-500">Tipo</dt>
              <dd className="font-medium text-wood-900">{session?.tipoUsuario}</dd>
            </div>
            <div>
              <dt className="text-sm text-wood-500">Permisos</dt>
              <dd className="mt-2 flex flex-wrap gap-2">
                {perfil.permisos.map((p) => (
                  <Badge key={p} className="bg-wood-100 text-wood-700">
                    {obtenerEtiquetaPermiso(p)}
                  </Badge>
                ))}
              </dd>
            </div>
          </dl>
        </Card>
      )}
    </div>
  );
}
