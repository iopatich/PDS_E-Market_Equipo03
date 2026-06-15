package com.emarket.config;

import com.emarket.entity.Categoria;
import com.emarket.entity.Producto;
import com.emarket.entity.VarianteProducto;
import com.emarket.repository.CategoriaRepository;
import com.emarket.repository.ItemCarritoRepository;
import com.emarket.repository.ItemPedidoRepository;
import com.emarket.repository.NotificacionRepository;
import com.emarket.repository.PedidoRepository;
import com.emarket.repository.ProductoRepository;
import com.emarket.repository.VarianteProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CatalogoInicialService {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;
    private final VarianteProductoRepository varianteProductoRepository;
    private final ItemCarritoRepository itemCarritoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final PedidoRepository pedidoRepository;
    private final NotificacionRepository notificacionRepository;

    @Transactional
    public void ejecutarCarga(boolean forzarRecarga) {
        long productosActivos = productoRepository.findAllByActivoTrue().size();

        if (productosActivos > 0 && !forzarRecarga) {
            log.info("Catálogo con {} productos activos. Carga inicial omitida.", productosActivos);
            return;
        }

        if (forzarRecarga) {
            log.warn("Forzando recarga del catálogo...");
            limpiarCatalogo();
        }

        if (!productoRepository.findAllByActivoTrue().isEmpty()) {
            log.info("El catálogo ya tiene productos activos.");
            return;
        }

        log.info("Cargando catálogo inicial de E-Market...");
        cargarCatalogo();
        log.info("Catálogo inicial cargado: 4 categorías y 12 productos con variantes.");
    }

    private void limpiarCatalogo() {
        notificacionRepository.deleteAllInBatch();
        itemPedidoRepository.deleteAllInBatch();
        pedidoRepository.deleteAllInBatch();
        itemCarritoRepository.deleteAllInBatch();
        varianteProductoRepository.deleteAllInBatch();
        productoRepository.deleteAllInBatch();
        categoriaRepository.deleteAllInBatch();
        log.info("Datos anteriores del catálogo eliminados.");
    }

    private void cargarCatalogo() {
        Categoria living = crearCategoria("Living", null);
        Categoria dormitorio = crearCategoria("Dormitorio", null);
        Categoria comedor = crearCategoria("Comedor", null);
        Categoria oficina = crearCategoria("Oficina", null);

        crearProductoConVariantes(
                "Sofá Milano",
                "Sofá de tres cuerpos con estructura de madera maciza y tapizado premium. Ideal para living amplios.",
                450000.0,
                living,
                "/productos/sofa-milano.jpg",
                List.of(
                        variante("Gris perla", 8, 0.0),
                        variante("Beige arena", 6, 15000.0),
                        variante("Verde oliva", 4, 25000.0)
                )
        );

        crearProductoConVariantes(
                "Mesa ratona Nórdica",
                "Mesa baja de diseño escandinavo con terminación natural y patas cónicas.",
                120000.0,
                living,
                "/productos/mesa-ratona.jpg",
                List.of(
                        variante("Roble claro", 12, 0.0),
                        variante("Nogal", 7, 18000.0)
                )
        );

        crearProductoConVariantes(
                "Estantería Loft",
                "Estantería modular de cinco niveles para libros, decoración y organización del living.",
                95000.0,
                living,
                "/productos/estanteria-loft.jpg",
                List.of(
                        variante("Negro mate", 10, 0.0),
                        variante("Blanco", 9, 8000.0)
                )
        );

        crearProductoConVariantes(
                "Cama King Oslo",
                "Cama king size con cabecera acolchada y base reforzada. Máximo confort para el descanso.",
                380000.0,
                dormitorio,
                "/productos/cama-oslo.jpg",
                List.of(
                        variante("Gris topo", 5, 0.0),
                        variante("Azul marino", 3, 35000.0),
                        variante("Crema", 4, 20000.0)
                )
        );

        crearProductoConVariantes(
                "Placard Premium",
                "Placard de cuatro puertas con espejo central, interiores configurables y guías soft-close.",
                520000.0,
                dormitorio,
                "/productos/placard-premium.jpg",
                List.of(
                        variante("Blanco brillante", 4, 0.0),
                        variante("Wengue", 3, 45000.0)
                )
        );

        crearProductoConVariantes(
                "Mesa de luz Aura",
                "Mesa de luz con un cajón y puerto USB integrado. Diseño minimalista para dormitorio.",
                65000.0,
                dormitorio,
                "/productos/mesa-luz-aura.jpg",
                List.of(
                        variante("Natural", 15, 0.0),
                        variante("Negro", 11, 5000.0)
                )
        );

        crearProductoConVariantes(
                "Mesa comedor Roble",
                "Mesa extensible para seis comensales en madera de roble con protección anti-manchas.",
                280000.0,
                comedor,
                "/productos/mesa-comedor.jpg",
                List.of(
                        variante("Roble natural", 6, 0.0),
                        variante("Roble oscuro", 4, 30000.0)
                )
        );

        crearProductoConVariantes(
                "Silla Tulip",
                "Silla de comedor con asiento tapizado y respaldo ergonómico. Se vende por unidad.",
                75000.0,
                comedor,
                "/productos/sillas-tulip.jpg",
                List.of(
                        variante("Blanco", 20, 0.0),
                        variante("Negro", 18, 0.0),
                        variante("Mostaza", 10, 8000.0)
                )
        );

        crearProductoConVariantes(
                "Vitrina Clásica",
                "Vitrina de dos puertas con vidrio templado y estantes regulables para vajilla y decoración.",
                195000.0,
                comedor,
                "/productos/vitrina-clasica.jpg",
                List.of(
                        variante("Cedro", 5, 0.0),
                        variante("Blanco vintage", 4, 22000.0)
                )
        );

        crearProductoConVariantes(
                "Escritorio Ejecutivo",
                "Escritorio amplio con pasacables y cajón con llave. Pensado para home office profesional.",
                210000.0,
                oficina,
                "/productos/escritorio.jpg",
                List.of(
                        variante("Nogal", 8, 0.0),
                        variante("Gris cemento", 6, 15000.0)
                )
        );

        crearProductoConVariantes(
                "Sillón ergonómico Pro",
                "Sillón de oficina con apoyo lumbar, brazos regulables y base giratoria reforzada.",
                165000.0,
                oficina,
                "/productos/sillon-oficina.jpg",
                List.of(
                        variante("Negro", 14, 0.0),
                        variante("Gris", 12, 0.0),
                        variante("Azul", 7, 12000.0)
                )
        );

        crearProductoConVariantes(
                "Biblioteca modular",
                "Biblioteca de tres módulos combinables para oficina o estudio. Estructura resistente.",
                135000.0,
                oficina,
                "/productos/biblioteca.jpg",
                List.of(
                        variante("Roble", 9, 0.0),
                        variante("Blanco", 8, 10000.0)
                )
        );
    }

    private Categoria crearCategoria(String nombre, Categoria padre) {
        Categoria categoria = new Categoria();
        categoria.setNombre(nombre);
        categoria.setActivo(true);
        categoria.setCategoriaPadre(padre);
        return categoriaRepository.save(categoria);
    }

    private void crearProductoConVariantes(
            String nombre,
            String descripcion,
            Double precioBase,
            Categoria categoria,
            String urlImagen,
            List<VarianteProducto> variantes
    ) {
        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecioBase(precioBase);
        producto.setUrlImagen(urlImagen);
        producto.setActivo(true);
        producto.setCategoriaPadre(categoria);
        Producto guardado = productoRepository.save(producto);

        for (VarianteProducto variante : variantes) {
            variante.setProducto(guardado);
            varianteProductoRepository.save(variante);
        }
    }

    private VarianteProducto variante(String color, int stock, double precioAdicional) {
        VarianteProducto variante = new VarianteProducto();
        variante.setColor(color);
        variante.setStock(stock);
        variante.setPrecioAdicional(precioAdicional);
        variante.setActivo(true);
        return variante;
    }
}
