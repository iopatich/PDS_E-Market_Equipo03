import { Link } from 'react-router-dom';

export function Footer() {
  return (
    <footer className="mt-auto border-t border-wood-100 bg-wood-900 text-wood-200">
      <div className="mx-auto max-w-7xl px-4 py-12 sm:px-6 lg:px-8">
        <div className="grid gap-8 md:grid-cols-3">
          <div>
            <h3 className="font-serif text-xl font-semibold text-white">E-Market</h3>
            <p className="mt-3 text-sm text-wood-300">
              Muebles premium con diseño atemporal. Calidad artesanal para transformar tu hogar.
            </p>
          </div>
          <div>
            <h4 className="text-sm font-semibold uppercase tracking-wider text-white">
              Navegación
            </h4>
            <ul className="mt-3 space-y-2 text-sm">
              <li>
                <Link to="/" className="hover:text-white">
                  Catálogo
                </Link>
              </li>
              <li>
                <Link to="/ingresar" className="hover:text-white">
                  Ingresar
                </Link>
              </li>
              <li>
                <Link to="/registro" className="hover:text-white">
                  Registrarse
                </Link>
              </li>
            </ul>
          </div>
          <div>
            <h4 className="text-sm font-semibold uppercase tracking-wider text-white">Contacto</h4>
            <ul className="mt-3 space-y-2 text-sm text-wood-300">
              <li>contacto@emarket.com</li>
              <li>+54 11 4000-0000</li>
              <li>Buenos Aires, Argentina</li>
            </ul>
          </div>
        </div>
        <div className="mt-8 border-t border-wood-700 pt-8 text-center text-xs text-wood-400">
          © {new Date().getFullYear()} E-Market. Todos los derechos reservados.
        </div>
      </div>
    </footer>
  );
}
