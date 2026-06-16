/**
 * Convierte un archivo de imagen a Data URL (base64) para enviarlo al backend.
 */
export function archivoImagenADataUrl(archivo: File): Promise<string> {
  return new Promise((resolve, reject) => {
    const lector = new FileReader();
    lector.onload = () => resolve(lector.result as string);
    lector.onerror = () => reject(new Error('No se pudo leer la imagen'));
    lector.readAsDataURL(archivo);
  });
}

const TIPOS_PERMITIDOS = ['image/jpeg', 'image/png', 'image/webp', 'image/gif'];
const TAMANO_MAXIMO_MB = 2;

export function validarArchivoImagen(archivo: File): string | null {
  if (!TIPOS_PERMITIDOS.includes(archivo.type)) {
    return 'Formato no válido. Usá JPG, PNG, WEBP o GIF.';
  }
  if (archivo.size > TAMANO_MAXIMO_MB * 1024 * 1024) {
    return `La imagen no puede superar los ${TAMANO_MAXIMO_MB} MB.`;
  }
  return null;
}
