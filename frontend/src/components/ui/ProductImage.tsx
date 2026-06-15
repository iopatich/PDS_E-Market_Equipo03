interface ProductImageProps {
  urlImagen?: string | null;
  nombre: string;
  className?: string;
  fallbackClassName?: string;
}

export function ProductImage({
  urlImagen,
  nombre,
  className = 'h-full w-full object-cover',
  fallbackClassName = 'flex h-full w-full items-center justify-center bg-wood-100 font-serif text-4xl text-wood-300',
}: ProductImageProps) {
  if (urlImagen) {
    return (
      <img
        src={urlImagen}
        alt={nombre}
        className={className}
        loading="lazy"
      />
    );
  }

  return (
    <div className={fallbackClassName}>
      <span>{nombre.charAt(0).toUpperCase()}</span>
    </div>
  );
}
