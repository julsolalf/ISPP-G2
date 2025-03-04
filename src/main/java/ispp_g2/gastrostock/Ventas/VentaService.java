package ispp_g2.gastrostock.Ventas;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import ispp_g2.gastrostock.Productos.Producto;
import ispp_g2.gastrostock.Productos.ProductoRepository;
import ispp_g2.gastrostock.plato.Plato;
import ispp_g2.gastrostock.plato.PlatoRepository;

@Service
public class VentaService {
    
    private final VentaRepository repo;
    private final ProductoRepository productoRepository;
    private final PlatoRepository platoRepository;
    private final PlatoVentaRepository platoVentaRepository;

    @Autowired
    public VentaService(VentaRepository repo, ProductoRepository productoRepository, PlatoRepository platoRepository, PlatoVentaRepository platoVentaRepository) {
        this.repo = repo;
        this.productoRepository = productoRepository;
        this.platoRepository = platoRepository;
        this.platoVentaRepository = platoVentaRepository;
        
    }

    @Transactional(readOnly = true)
    public List<Venta> getVentas() {
        return repo.findAll();
    }

    public Optional<Venta> obtenerVentaPorId(Integer id) {
        return repo.findById(id);
    }

    @Transactional
    public Venta agregarVenta(Venta venta) {
        if (venta.getProductos() == null || venta.getProductos().isEmpty()) {
            throw new IllegalArgumentException("La venta debe contener al menos un producto.");
        }

        // Inicializar el total de la venta a 0
        double total = 0;

        // Recorrer los productos (platos) de la venta y calcular el total
        for (Plato plato : venta.getProductos()) {
            // Aquí puedes agregar más lógica para calcular la cantidad de cada plato si fuera necesario
            total += plato.getPrecio();  // Asumimos que 'getPrecio()' está disponible en la clase Plato
        }

        // Establecer el total de la venta
        venta.setTotal(total);
        venta.setFecha(LocalDate.now());  // Fecha de la venta es la fecha actual

        // Guardar la venta (esto también manejará la persistencia de la relación con los platos)
        return repo.save(venta);
    }

    public void agregarProductoAVenta(Integer ventaId, Integer productoId, Integer cantidad) {
        Venta venta = repo.findById(ventaId)
                .orElseThrow(() -> new IllegalArgumentException("Venta no encontrada"));
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        
        Plato plato = platoRepository.findById(producto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Plato no encontrado para el producto"));

        // Crear la relación entre el plato y la venta
        PlatoVenta platoVenta = new PlatoVenta();
        platoVenta.setPlato(plato);
        platoVenta.setVenta(venta);
        platoVenta.setCantidad(cantidad);

        // Guardar la relación (esto se puede hacer a través de un servicio o repositorio adicional)
        platoVentaRepository.save(platoVenta);
    }

    public void eliminarVenta(Integer id) {
        repo.deleteById(id);
    }

    public List<Venta> obtenerVentasPorFecha(LocalDate fecha) {
        return repo.findByFecha(fecha);
    }
    

    
}
