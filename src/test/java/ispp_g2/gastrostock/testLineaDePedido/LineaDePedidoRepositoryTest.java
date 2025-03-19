package ispp_g2.gastrostock.testLineaDePedido;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import ispp_g2.gastrostock.empleado.Empleado;
import ispp_g2.gastrostock.empleado.EmpleadoRepository;
import ispp_g2.gastrostock.empleado.Rol;
import ispp_g2.gastrostock.lineaDePedido.LineaDePedido;
import ispp_g2.gastrostock.lineaDePedido.LineaDePedidoRepository;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioRepository;
import ispp_g2.gastrostock.productoVenta.ProductoVenta;
import ispp_g2.gastrostock.productoVenta.ProductoVentaRepository;
import ispp_g2.gastrostock.pedido.Pedido;
import ispp_g2.gastrostock.pedido.PedidoRepository;
import ispp_g2.gastrostock.productoVenta.CategoriasVenta;
import ispp_g2.gastrostock.dueño.Dueño;
import ispp_g2.gastrostock.dueño.DueñoRepository;
import ispp_g2.gastrostock.mesa.Mesa;
import ispp_g2.gastrostock.mesa.MesaRepository;

@DataJpaTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class LineaDePedidoRepositoryTest {

    @Autowired
    private LineaDePedidoRepository lineaDePedidoRepository;
    @Autowired
    private PedidoRepository PedidoRepository;
    @Autowired
    private ProductoVentaRepository ProductoVentaRepository;
    @Autowired
    private NegocioRepository negocioRepository;
    @Autowired
    private EmpleadoRepository empleadoRepository;
    @Autowired
    private DueñoRepository dueñoRepository;
    @Autowired
    private MesaRepository mesaRepository;

    
    private LineaDePedido linea1, linea2;
    private ProductoVenta producto;
    private Pedido pedido;
    private Dueño dueño;
    private Negocio negocio;
    private Empleado empleado;
    private Mesa mesa1;

    @BeforeEach
    void setUp() {
        
        lineaDePedidoRepository.deleteAll();
        ProductoVentaRepository.deleteAll();
        PedidoRepository.deleteAll();

        dueño = new Dueño();
        dueño.setFirstName("Juan");
        dueño.setLastName("Dueño");
        dueño.setEmail("juan@gastrostock.com");
        dueño.setNumTelefono("689594895");
        dueño.setTokenDueño("TOKEN123");
        dueño = dueñoRepository.save(dueño);

        
        negocio = new Negocio();
        negocio.setId(400);
        negocio.setName("Restaurante La Tasca");
        negocio.setDireccion("Calle Principal 123");
        negocio.setCiudad("Sevilla");
        negocio.setPais("España");
        negocio.setCodigoPostal("41001");
        negocio.setTokenNegocio(12345);
        negocio.setDueño(dueño);
        negocio = negocioRepository.save(negocio);

        
        mesa1 = new Mesa();
        mesa1.setId(101);
        mesa1.setName("Mesa Exterior 1");
        mesa1.setNumeroAsientos(4);
        mesa1.setNegocio(negocio);
        mesa1 = mesaRepository.save(mesa1);


        
        empleado = new Empleado();
        empleado.setName("Carlos Camarero");
        empleado.setTokenEmpleado("EMP-TOKEN-123");
        empleado.setNegocio(negocio);
        empleado = empleadoRepository.save(empleado);

        
    
        producto = new ProductoVenta();
        producto.setNombre("Producto A");
        producto.setPrecioVenta(5.0);
        producto.setCategoriaVenta(CategoriasVenta.PLATOS);
        ProductoVentaRepository.save(producto);

        
        
        pedido = new Pedido();
        pedido.setFecha(LocalDateTime.now());
        pedido.setPrecioTotal(45.50);
        pedido.setEmpleado(empleado);
        pedido.setMesa(mesa1);
        pedido.setNegocio(negocio);
        PedidoRepository.save(pedido);
        

        
        linea1 = new LineaDePedido();
        linea1.setCantidad(5);
        linea1.setPrecioLinea(50.0);
        linea1.setProducto(producto);
        linea1.setPedido(pedido);

        linea2 = new LineaDePedido();
        linea2.setCantidad(3);
        linea2.setPrecioLinea(30.0);
        linea2.setProducto(producto);
        linea2.setPedido(pedido);

        
        lineaDePedidoRepository.save(linea1);
        lineaDePedidoRepository.save(linea2);
        
    }


    @Test
    void testFindAll() {
        
        List<LineaDePedido> lineas = (List<LineaDePedido>) lineaDePedidoRepository.findAll();

        assertNotNull(lineas);
        assertEquals(2, lineas.size());  
    }

    @Test
    void testFindByCantidad() {
        
        List<LineaDePedido> lineas = lineaDePedidoRepository.findLineaDePedidosByCantidad(5);

        assertNotNull(lineas);
        assertEquals(1, lineas.size());
        assertEquals(5, lineas.get(0).getCantidad());
    }

    @Test
    void testFindByPrecioLinea() {
        
        List<LineaDePedido> lineas = lineaDePedidoRepository.findLineaDePedidosByPrecioLinea(50.0);

        assertNotNull(lineas);
        assertEquals(1, lineas.size());
        assertEquals(50.0, lineas.get(0).getPrecioLinea());
    }

    @Test
    void testFindByPedidoId() {
        
        List<LineaDePedido> lineas = lineaDePedidoRepository.findLineaDePedidosByPedidoId(pedido.getId());

        assertNotNull(lineas);
        assertTrue(lineas.size() >= 2);  
    }

    @Test
    void testFindByProductoId() {
        
        List<LineaDePedido> lineas = lineaDePedidoRepository.findLineaDePedidosByProductoId(producto.getId());

        assertNotNull(lineas);
        assertTrue(lineas.size() >= 2);  
    }

    @Test
    void testFindByProductoIdAndCantidad() {
        
        List<LineaDePedido> lineas = lineaDePedidoRepository.findLineaDePedidosByProductoIdAndCantidad(producto.getId(), 5);

        assertNotNull(lineas);
        assertEquals(1, lineas.size());
        assertEquals(5, lineas.get(0).getCantidad());
    }

    @Test
    void testFindByProductoIdAndPrecioLinea() {
        
        List<LineaDePedido> lineas = lineaDePedidoRepository.findLineaDePedidosByProductoIdAndPrecioLinea(producto.getId(), 50.0);

        assertNotNull(lineas);
        assertEquals(1, lineas.size());
        assertEquals(50.0, lineas.get(0).getPrecioLinea());
    }
}

