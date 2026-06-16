import type { AuthSession } from '../types';

const SESSION_KEY = 'emarket_session';

export const authStorage = {
  get(): AuthSession | null {
    const raw = localStorage.getItem(SESSION_KEY);
    if (!raw) return null;
    try {
      return JSON.parse(raw) as AuthSession;
    } catch {
      return null;
    }
  },

  set(session: AuthSession): void {
    localStorage.setItem(SESSION_KEY, JSON.stringify(session));
  },

  clear(): void {
    localStorage.removeItem(SESSION_KEY);
  },

  getToken(): string | null {
    return this.get()?.token ?? null;
  },
};
