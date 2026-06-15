import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './core/auth/AuthContext';
import { CartProvider } from './core/cart/CartContext';
import { ToastProvider } from './core/toast/ToastContext';
import { MainLayout } from './components/layout/MainLayout';
import { AdminLayout } from './components/layout/AdminLayout';
import { ProtectedRoute } from './components/layout/ProtectedRoute';
import { AdminRoute } from './components/layout/AdminRoute';
import { Permiso } from './core/enums';
import { HomePage } from './pages/HomePage';
import { ProductDetailPage } from './pages/ProductDetailPage';
import { LoginPage } from './pages/LoginPage';
import { RegisterPage } from './pages/RegisterPage';
import { CartPage } from './pages/CartPage';
import { CheckoutPage } from './pages/CheckoutPage';
import { MyOrdersPage } from './pages/MyOrdersPage';
import { ProfilePage } from './pages/ProfilePage';
import { DashboardPage } from './pages/admin/DashboardPage';
import { AdminProductsPage } from './pages/admin/AdminProductsPage';
import { AdminCategoriesPage } from './pages/admin/AdminCategoriesPage';
import { AdminOrdersPage } from './pages/admin/AdminOrdersPage';
import { AdminClientsPage } from './pages/admin/AdminClientsPage';
import { AdminAdminsPage } from './pages/admin/AdminAdminsPage';
import { AdminStockPage } from './pages/admin/AdminStockPage';

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <ToastProvider>
          <CartProvider>
            <Routes>
              <Route element={<MainLayout />}>
                <Route index element={<HomePage />} />
                <Route path="productos/:id" element={<ProductDetailPage />} />
                <Route path="ingresar" element={<LoginPage />} />
                <Route path="registro" element={<RegisterPage />} />
                <Route
                  path="carrito"
                  element={
                    <ProtectedRoute permiso={Permiso.GESTIONAR_CARRITO}>
                      <CartPage />
                    </ProtectedRoute>
                  }
                />
                <Route
                  path="finalizar-compra"
                  element={
                    <ProtectedRoute permiso={Permiso.REALIZAR_COMPRA} clienteOnly>
                      <CheckoutPage />
                    </ProtectedRoute>
                  }
                />
                <Route
                  path="mis-pedidos"
                  element={
                    <ProtectedRoute permiso={Permiso.REALIZAR_COMPRA} clienteOnly>
                      <MyOrdersPage />
                    </ProtectedRoute>
                  }
                />
                <Route
                  path="perfil"
                  element={
                    <ProtectedRoute>
                      <ProfilePage />
                    </ProtectedRoute>
                  }
                />
              </Route>

              <Route
                path="admin"
                element={
                  <AdminRoute>
                    <AdminLayout />
                  </AdminRoute>
                }
              >
                <Route index element={<DashboardPage />} />
                <Route path="productos" element={<AdminProductsPage />} />
                <Route path="categorias" element={<AdminCategoriesPage />} />
                <Route path="pedidos" element={<AdminOrdersPage />} />
                <Route path="stock" element={<AdminStockPage />} />
                <Route path="clientes" element={<AdminClientsPage />} />
                <Route path="administradores" element={<AdminAdminsPage />} />
              </Route>

              <Route path="*" element={<Navigate to="/" replace />} />
            </Routes>
          </CartProvider>
        </ToastProvider>
      </AuthProvider>
    </BrowserRouter>
  );
}
