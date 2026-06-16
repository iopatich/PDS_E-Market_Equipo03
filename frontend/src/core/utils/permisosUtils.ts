import { CanalNotificacion, Permiso } from '../enums';

/** Etiquetas en español para los permisos del backend */
export const ETIQUETAS_PERMISO: Record<Permiso, string> = {
  [Permiso.VER_CATALOGO]: 'Ver catálogo',
  [Permiso.GESTIONAR_CARRITO]: 'Gestionar carrito',
  [Permiso.REALIZAR_COMPRA]: 'Realizar compra',
  [Permiso.CARGAR_PRODUCTO]: 'Cargar producto',
  [Permiso.GESTIONAR_PRODUCTOS]: 'Gestionar productos',
  [Permiso.ACTUALIZAR_ESTADO_PEDIDO]: 'Actualizar estado de pedido',
  [Permiso.DAR_ALTA_CLIENTE]: 'Dar alta de cliente',
  [Permiso.DAR_BAJA_CLIENTE]: 'Dar baja de cliente',
};

export function obtenerEtiquetaPermiso(permiso: Permiso): string {
  return ETIQUETAS_PERMISO[permiso] ?? permiso;
}

const ETIQUETAS_CANAL: Record<CanalNotificacion, string> = {
  [CanalNotificacion.EMAIL]: 'Correo electrónico',
  [CanalNotificacion.SMS]: 'Mensaje de texto',
  [CanalNotificacion.PUSH]: 'Notificación push',
};

export function obtenerEtiquetaCanal(canal: CanalNotificacion): string {
  return ETIQUETAS_CANAL[canal] ?? canal;
}
