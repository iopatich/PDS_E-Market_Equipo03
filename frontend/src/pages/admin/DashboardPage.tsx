import { useEffect, useState } from 'react';
import { productoService } from '../../services/producto.service';
import { categoriaService } from '../../services/categoria.service';
import { varianteService } from '../../services/variante.service';
import { clienteService } from '../../services/cliente.service';
import { pedidoService } from '../../services/pedido.service';
import { Card } from '../../components/ui/Card';
import { Spinner } from '../../components/ui/Spinner';
import { getErrorMessage } from '../../core/api/errorAdapter';

interface Stats {
  productos: number;
  categorias: number;
  variantes: number;
  clientes: number;
  pedidos: number;
}

export function DashboardPage() {
  const [stats, setStats] = useState<Stats | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    Promise.all([
      productoService.listar(),
      categoriaService.listar(),
      varianteService.listar(),
      clienteService.listar(),
      pedidoService.listarTodos(),
    ])
      .then(([productos, categorias, variantes, clientes, pedidos]) => {
        setStats({
          productos: productos.length,
          categorias: categorias.length,
          variantes: variantes.length,
          clientes: clientes.length,
          pedidos: pedidos.length,
        });
      })
      .catch((err) => setError(getErrorMessage(err)))
      .finally(() => setLoading(false));
  }, []);

  if (loading) {
    return (
      <div className="flex items-center justify-center py-20">
        <Spinner size="lg" />
      </div>
    );
  }

  const cards = [
    { label: 'Productos', value: stats?.productos ?? 0, color: 'bg-blue-500' },
    { label: 'Categorías', value: stats?.categorias ?? 0, color: 'bg-green-500' },
    { label: 'Variantes', value: stats?.variantes ?? 0, color: 'bg-purple-500' },
    { label: 'Clientes', value: stats?.clientes ?? 0, color: 'bg-orange-500' },
    { label: 'Pedidos', value: stats?.pedidos ?? 0, color: 'bg-accent' },
  ];

  return (
    <div className="animate-fade-in">
      <h2 className="font-serif text-2xl font-bold text-wood-900">Panel de control</h2>
      <p className="mt-1 text-wood-500">Resumen general del comercio</p>

      {error && <div className="mt-4 rounded-lg bg-red-50 p-4 text-red-700">{error}</div>}

      <div className="mt-8 grid gap-6 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-5">
        {cards.map((card) => (
          <Card key={card.label} className="relative overflow-hidden">
            <div className={`absolute right-0 top-0 h-1 w-full ${card.color}`} />
            <p className="text-sm text-wood-500">{card.label}</p>
            <p className="mt-2 text-3xl font-bold text-wood-900">{card.value}</p>
          </Card>
        ))}
      </div>
    </div>
  );
}
