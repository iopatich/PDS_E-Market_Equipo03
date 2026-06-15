import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useState,
  type ReactNode,
} from 'react';
import { authStorage } from './authStorage';
import { authService } from '../../services/auth.service';
import { Permiso, TipoUsuario } from '../enums';
import type { AuthSession, LoginRequestDto } from '../types';

interface AuthContextValue {
  session: AuthSession | null;
  isAuthenticated: boolean;
  isAdmin: boolean;
  isCliente: boolean;
  loading: boolean;
  login: (dto: LoginRequestDto) => Promise<AuthSession>;
  logout: () => Promise<void>;
  hasPermiso: (permiso: Permiso) => boolean;
}

/**
 * Contexto global de autenticación. Comparte la sesión del usuario en toda la app.
 */
const AuthContext = createContext<AuthContextValue | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [session, setSession] = useState<AuthSession | null>(authStorage.get());
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const stored = authStorage.get();
    if (stored) {
      authService
        .getPerfil()
        .then(() => setSession(stored))
        .catch(() => {
          authStorage.clear();
          setSession(null);
        })
        .finally(() => setLoading(false));
    } else {
      setLoading(false);
    }
  }, []);

  const login = useCallback(async (dto: LoginRequestDto) => {
    const response = await authService.login(dto);
    const newSession: AuthSession = {
      token: response.token,
      tipoUsuario: response.tipoUsuario,
      id: response.id,
      username: response.username,
      permisos: response.permisos,
    };
    authStorage.set(newSession);
    setSession(newSession);
    return newSession;
  }, []);

  const logout = useCallback(async () => {
    try {
      await authService.logout();
    } finally {
      authStorage.clear();
      setSession(null);
    }
  }, []);

  const hasPermiso = useCallback(
    (permiso: Permiso) => session?.permisos.includes(permiso) ?? false,
    [session],
  );

  const value = useMemo<AuthContextValue>(
    () => ({
      session,
      isAuthenticated: !!session,
      isAdmin: session?.tipoUsuario === TipoUsuario.ADMINISTRADOR,
      isCliente: session?.tipoUsuario === TipoUsuario.CLIENTE,
      loading,
      login,
      logout,
      hasPermiso,
    }),
    [session, loading, login, logout, hasPermiso],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth(): AuthContextValue {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth debe usarse dentro de AuthProvider');
  return ctx;
}
