import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { clienteService } from '../services/cliente.service';
import { useToast } from '../core/toast/ToastContext';
import { adaptApiError, getErrorMessage } from '../core/api/errorAdapter';
import { Button } from '../components/ui/Button';
import { Input } from '../components/ui/Input';
import { Card } from '../components/ui/Card';

export function RegisterPage() {
  const { showToast } = useToast();
  const navigate = useNavigate();

  const [form, setForm] = useState({ username: '', password: '', email: '' });
  const [loading, setLoading] = useState(false);
  const [errors, setErrors] = useState<Record<string, string>>({});

  const handleChange = (field: string, value: string) => {
    setForm((prev) => ({ ...prev, [field]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setErrors({});
    setLoading(true);
    try {
      await clienteService.registrar(form);
      showToast('Registro exitoso. Ya podés iniciar sesión.', 'success');
      navigate('/ingresar');
    } catch (err) {
      const appError = adaptApiError(err);
      if (appError.fieldErrors) setErrors(appError.fieldErrors);
      showToast(getErrorMessage(err), 'error');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex min-h-[80vh] items-center justify-center px-4 py-12">
      <Card className="w-full max-w-md animate-slide-up">
        <h1 className="font-serif text-2xl font-bold text-wood-900">Crear cuenta</h1>
        <p className="mt-2 text-sm text-wood-500">Registrate como cliente de E-Market</p>

        <form onSubmit={handleSubmit} className="mt-6 space-y-4">
          <Input
            label="Usuario"
            value={form.username}
            onChange={(e) => handleChange('username', e.target.value)}
            error={errors.username}
            required
            minLength={3}
            maxLength={50}
          />
          <Input
            label="Correo electrónico"
            type="email"
            value={form.email}
            onChange={(e) => handleChange('email', e.target.value)}
            error={errors.email}
            required
          />
          <Input
            label="Contraseña"
            type="password"
            value={form.password}
            onChange={(e) => handleChange('password', e.target.value)}
            error={errors.password}
            required
            minLength={6}
          />
          <Button type="submit" className="w-full" loading={loading}>
            Registrarse
          </Button>
        </form>

        <p className="mt-6 text-center text-sm text-wood-500">
          ¿Ya tenés cuenta?{' '}
          <Link to="/ingresar" className="font-medium text-accent hover:underline">
            Ingresar
          </Link>
        </p>
      </Card>
    </div>
  );
}
