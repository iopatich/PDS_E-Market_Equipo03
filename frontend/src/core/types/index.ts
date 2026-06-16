import type { CanalNotificacion, EstadoPedido, Permiso, TipoPago } from '../enums';

export interface LoginRequestDto {
  username: string;
  password: string;
}

export interface LoginResponseDto {
  token: string;
  tipoUsuario: string;
  id: number;
  username: string;
  permisos: Permiso[];
}

export interface ClienteRequestDto {
  username: string;
  password: string;
  email: string;
}

export interface ClienteResponseDto {
  id: number;
  username: string;
  email: string;
  permisos: Permiso[];
}

export interface AdministradorRequestDto {
  username: string;
  password: string;
}

export interface AdministradorResponseDto {
  id: number;
  username: string;
  permisos: Permiso[];
}

export interface CategoriaRequestDto {
  nombre: string;
  idCategoriaPadre?: number;
}

export interface CategoriaResponseDto {
  id: number;
  nombre: string;
  CategoriaPadre: string | null;
}

export interface ProductoRequestDto {
  nombre: string;
  descripcion: string;
  precioBase: number;
  idCategoriaPadre: number;
  urlImagen?: string | null;
}

export interface ProductoResponseDto {
  id: number;
  nombre: string;
  descripcion: string;
  precioBase: number;
  nombreCategoriaPadre: string;
  urlImagen?: string | null;
}

export interface VarianteProductoRequestDto {
  color: string;
  stock: number;
  precio: number;
  idProducto: number;
}

export interface VarianteProductoResponseDto {
  id: number;
  color: string;
  stock: number;
  precioAdicional: number;
  precioFinal: number;
}

export interface AgregarItemCarritoRequestDto {
  idVarianteProducto: number;
  cantidad: number;
}

export interface ActualizarCantidadCarritoRequestDto {
  cantidad: number;
}

export interface ConfirmarCompraRequestDto {
  tipoPago: TipoPago;
}

export interface ItemCarritoResponseDto {
  id: number;
  idVarianteProducto: number;
  nombreProducto: string;
  color: string;
  cantidad: number;
  precioUnitario: number;
  subtotal: number;
}

export interface CarritoResponseDto {
  id: number;
  idCliente: number;
  items: ItemCarritoResponseDto[];
  total: number;
}

export interface ItemPedidoResponseDto {
  idVarianteProducto: number;
  nombreProducto: string;
  color: string;
  cantidad: number;
  precioUnitario: number;
  subtotal: number;
}

export interface PedidoResponseDto {
  id: number;
  idCliente: number;
  usernameCliente: string;
  fechaCreacion: string;
  estadoActual: EstadoPedido;
  total: number;
  items: ItemPedidoResponseDto[];
}

export interface NotificacionResponseDto {
  id: number;
  canal: CanalNotificacion;
  mensaje: string;
  fechaEnvio: string;
}

export interface ApiResponseDto<T> {
  mensaje: string;
  data: T;
}

export interface ErrorResponse {
  fecha: string;
  status: number;
  mensaje: string;
  errores?: Record<string, string>;
}

export interface AuthSession {
  token: string;
  tipoUsuario: string;
  id: number;
  username: string;
  permisos: Permiso[];
}

export type PerfilResponse = ClienteResponseDto | AdministradorResponseDto;

export interface AppError {
  status: number;
  message: string;
  fieldErrors?: Record<string, string>;
}
