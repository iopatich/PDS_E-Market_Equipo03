import { useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../core/auth/AuthContext';
import { useToast } from '../core/toast/ToastContext';
import { TipoUsuario } from '../core/enums';
import { getErrorMessage, adaptApiError } from '../core/api/errorAdapter';
import { Button } from '../components/ui/Button';
import { Input } from '../components/ui/Input';
import { Card } from '../components/ui/Card';

export function LoginPage() {
  const { login } = useAuth();
  const { showToast } = useToast();
  const navigate = useNavigate();
  const location = useLocation();
  const from = (location.state as { from?: string })?.from ?? '/';

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [errors, setErrors] = useState<Record<string, string>>({});

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setErrors({});
    setLoading(true);
    try {
      const session = await login({ username, password });
      showToast(`Bienvenido, ${session.username}`, 'success');
      if (session.tipoUsuario === TipoUsuario.ADMINISTRADOR) {
        navigate('/admin');
      } else {
        navigate(from);
      }
    } catch (err) {
      const appError = adaptApiError(err);
      if (appError.fieldErrors) {
        setErrors(appError.fieldErrors);
      }
      showToast(getErrorMessage(err), 'error');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex min-h-[80vh] items-center justify-center px-4 py-12">
      <Card className="w-full max-w-md animate-slide-up">
        <h1 className="font-serif text-2xl font-bold text-wood-900">Ingresar</h1>
        <p className="mt-2 text-sm text-wood-500">Accedé con tu usuario y contraseña</p>

        <form onSubmit={handleSubmit} className="mt-6 space-y-4">
          <Input
            label="Usuario"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            error={errors.username}
            required
            autoComplete="username"
          />
          <Input
            label="Contraseña"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            error={errors.password}
            required
            autoComplete="current-password"
          />
          <Button type="submit" className="w-full" loading={loading}>
            Ingresar
          </Button>
        </form>

        <p className="mt-6 text-center text-sm text-wood-500">
          ¿No tenés cuenta de cliente?{' '}
          <Link to="/registro" className="font-medium text-accent hover:underline">
            Registrate
          </Link>
        </p>
      </Card>
    </div>
  );
}
