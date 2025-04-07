package ispp_g2.gastrostock.testLineaDePedido;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;

import ispp_g2.gastrostock.categorias.Categoria;
import ispp_g2.gastrostock.categorias.CategoriaRepository;
import ispp_g2.gastrostock.categorias.Pertenece;
import ispp_g2.gastrostock.dueno.Dueno;
import ispp_g2.gastrostock.dueno.DuenoRepository;
import ispp_g2.gastrostock.empleado.Empleado;
import ispp_g2.gastrostock.empleado.EmpleadoRepository;
import ispp_g2.gastrostock.lineaDePedido.LineaDePedido;
import ispp_g2.gastrostock.lineaDePedido.LineaDePedidoRepository;
import ispp_g2.gastrostock.mesa.Mesa;
import ispp_g2.gastrostock.mesa.MesaRepository;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioRepository;
import ispp_g2.gastrostock.pedido.Pedido;
import ispp_g2.gastrostock.pedido.PedidoRepository;
import ispp_g2.gastrostock.productoVenta.ProductoVenta;
import ispp_g2.gastrostock.productoVenta.ProductoVentaRepository;
import ispp_g2.gastrostock.user.Authorities;
import ispp_g2.gastrostock.user.AuthoritiesRepository;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserRepository;
import ispp_g2.gastrostock.ventas.Venta;
import ispp_g2.gastrostock.ventas.VentaRepository;

@DataJpaTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class LineaDePedidoRepositoryTest {

    @Autowired
    private LineaDePedidoRepository lineaDePedidoRepository;
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private ProductoVentaRepository productoVentaRepository;
    
    @Autowired
    private NegocioRepository negocioRepository;
    
    @Autowired
    private EmpleadoRepository empleadoRepository;
    
    @Autowired
    private DuenoRepository duenoRepository;
    
    @Autowired
    private MesaRepository mesaRepository;
    
    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthoritiesRepository authoritiesRepository;

    @Autowired
    private VentaRepository ventaRepository;

    private LineaDePedido lineaNormal, lineaCantidadGrande, lineaPrecioAlto, lineaMinima;
    private ProductoVenta producto1, producto2;
    private Pedido pedido1, pedido2;
    private Dueno dueno;
    private Negocio negocio;
    private Empleado empleado;
    private Mesa mesa1, mesa2;
    private Categoria categoria;
    private Venta venta;

    @BeforeEach
    void setUp() {
        // Limpiar los repositorios para asegurar un estado consistente
        lineaDePedidoRepository.deleteAll();
        productoVentaRepository.deleteAll();
        pedidoRepository.deleteAll();
        mesaRepository.deleteAll();
        empleadoRepository.deleteAll();
        categoriaRepository.deleteAll();
        negocioRepository.deleteAll();
        duenoRepository.deleteAll();
        
        Authorities authority = new Authorities();
        authority.setAuthority("DUENO");
        authority = authoritiesRepository.save(authority);

        // Crear usuario
        User user = new User();
        user.setUsername("juangarcia");
        user.setPassword("password123");
        user.setAuthority(authority);
        user = userRepository.save(user);

        // Crear dueno
        dueno = new Dueno();
        dueno.setFirstName("Juan");
        dueno.setLastName("Dueno");
        dueno.setEmail("juan@gastrostock.com");
        dueno.setNumTelefono("689594895");
        dueno.setTokenDueno("TOKEN123");
        dueno.setUser(user);
        dueno = duenoRepository.save(dueno);

        // Crear negocio
        negocio = new Negocio();
        negocio.setName("Restaurante La Tasca");
        negocio.setDireccion("Calle Principal 123");
        negocio.setCiudad("Sevilla");
        negocio.setPais("Espana");
        negocio.setCodigoPostal("41001");
        negocio.setTokenNegocio(12345);
        negocio.setDueno(dueno);
        negocio = negocioRepository.save(negocio);

        // Crear mesas
        mesa1 = new Mesa();
        mesa1.setName("Mesa 1");
        mesa1.setNumeroAsientos(4);
        mesa1.setNegocio(negocio);
        mesa1 = mesaRepository.save(mesa1);
        
        mesa2 = new Mesa();
        mesa2.setName("Mesa 2");
        mesa2.setNumeroAsientos(2);
        mesa2.setNegocio(negocio);
        mesa2 = mesaRepository.save(mesa2);

        // Crear empleado
        empleado = new Empleado();
        empleado.setTokenEmpleado("testToken");
        empleado.setNegocio(negocio);
        empleado.setFirstName("Antonio");
        empleado.setLastName("García");
        empleado.setEmail("antonio@test.com");
        empleado.setNumTelefono("666111222");
        empleado.setUser(user);
        empleado = empleadoRepository.save(empleado);

        // Crear categoría
        categoria = new Categoria();
        categoria.setName("Bebidas");
        categoria.setNegocio(negocio);
        categoria.setPertenece(Pertenece.INVENTARIO);
        categoria = categoriaRepository.save(categoria);

        // Crear productos
        producto1 = new ProductoVenta();
        producto1.setName("Cerveza");
        producto1.setPrecioVenta(3.0);
        producto1.setCategoria(categoria);
        producto1 = productoVentaRepository.save(producto1);
        
        producto2 = new ProductoVenta();
        producto2.setName("Vino");
        producto2.setPrecioVenta(5.0);
        producto2.setCategoria(categoria);
        producto2 = productoVentaRepository.save(producto2);

        //Crear venta
        venta = new Venta();
        venta.setNegocio(negocio);
        venta = ventaRepository.save(venta);
        
        // Crear pedidos
        pedido1 = new Pedido();
        pedido1.setFecha(LocalDateTime.now().minusHours(1));
        pedido1.setPrecioTotal(50.75);
        pedido1.setMesa(mesa1);
        pedido1.setEmpleado(empleado);
        pedido1.setVenta(venta);
        pedido1 = pedidoRepository.save(pedido1);
        
        pedido2 = new Pedido();
        pedido2.setFecha(LocalDateTime.now());
        pedido2.setPrecioTotal(75.50);
        pedido2.setMesa(mesa2);
        pedido2.setEmpleado(empleado);
        pedido2.setVenta(venta);
        pedido2 = pedidoRepository.save(pedido2);

        // Crear líneas de pedido con diferentes características para las pruebas
        lineaNormal = new LineaDePedido();
        lineaNormal.setCantidad(3);
        lineaNormal.setPrecioLinea(9.0);
        lineaNormal.setProducto(producto1);
        lineaNormal.setPedido(pedido1);
        lineaNormal = lineaDePedidoRepository.save(lineaNormal);

        lineaCantidadGrande = new LineaDePedido();
        lineaCantidadGrande.setCantidad(10);
        lineaCantidadGrande.setPrecioLinea(30.0);
        lineaCantidadGrande.setProducto(producto1);
        lineaCantidadGrande.setPedido(pedido1);
        lineaCantidadGrande = lineaDePedidoRepository.save(lineaCantidadGrande);

        lineaPrecioAlto = new LineaDePedido();
        lineaPrecioAlto.setCantidad(4);
        lineaPrecioAlto.setPrecioLinea(20.0);
        lineaPrecioAlto.setProducto(producto2);
        lineaPrecioAlto.setPedido(pedido2);
        lineaPrecioAlto = lineaDePedidoRepository.save(lineaPrecioAlto);

        lineaMinima = new LineaDePedido();
        lineaMinima.setCantidad(1);
        lineaMinima.setPrecioLinea(5.0);
        lineaMinima.setProducto(producto2);
        lineaMinima.setPedido(pedido2);
        lineaMinima = lineaDePedidoRepository.save(lineaMinima);
    }

    // TESTS PARA MÉTODOS CRUD ESTÁNDAR

    @Test
    void testSave() {
        // Crear una nueva línea de pedido
        LineaDePedido nuevaLinea = new LineaDePedido();
        nuevaLinea.setCantidad(2);
        nuevaLinea.setPrecioLinea(6.0);
        nuevaLinea.setProducto(producto1);
        nuevaLinea.setPedido(pedido1);
        
        // Guardar la línea
        LineaDePedido saved = lineaDePedidoRepository.save(nuevaLinea);
        
        // Verificar que se guardó correctamente
        assertNotNull(saved.getId());
        assertEquals(2, saved.getCantidad());
        assertEquals(6.0, saved.getPrecioLinea());
        
        // Verificar que se puede recuperar de la base de datos
        LineaDePedido retrieved = lineaDePedidoRepository.findById(saved.getId()).orElse(null);
        assertNotNull(retrieved);
        assertEquals(2, retrieved.getCantidad());
    }
    
    @Test
    void testSave_WithInvalidData() {
        // Intentar guardar una línea sin pedido (debería fallar)
        LineaDePedido invalida = new LineaDePedido();
        invalida.setCantidad(2);
        invalida.setPrecioLinea(6.0);
        invalida.setProducto(producto1);
        // No establezco pedido
        
        // Esta operación debería fallar con alguna excepción
        Exception exception = assertThrows(Exception.class, () -> {
            lineaDePedidoRepository.save(invalida);
            lineaDePedidoRepository.findAll(); // Forzar flush
        });
        
        // Verificar que la excepción está relacionada con restricciones de integridad
        assertTrue(exception.getMessage().contains("constraint") || 
                   exception.getMessage().contains("not-null") || 
                   exception.getMessage().contains("null") ||
                   exception.getMessage().contains("foreign key"),
                   "La excepción no está relacionada con restricciones: " + exception.getMessage());
    }

    @Test
    void testFindById() {
        // Buscar una línea existente por ID
        Optional<LineaDePedido> found = lineaDePedidoRepository.findById(lineaNormal.getId());
        
        // Verificar que existe y tiene los datos correctos
        assertTrue(found.isPresent());
        assertEquals(3, found.get().getCantidad());
        assertEquals(9.0, found.get().getPrecioLinea());
    }
    
    @Test
    void testFindById_NotFound() {
        // Buscar una línea que no existe
        Optional<LineaDePedido> notFound = lineaDePedidoRepository.findById(999);
        
        // Verificar que no existe
        assertFalse(notFound.isPresent());
    }
    
    @Test
    void testFindAll() {
        // Obtener todas las líneas
        Iterable<LineaDePedido> lineas = lineaDePedidoRepository.findAll();
        
        // Contar cuántas líneas hay
        int count = 0;
        for (LineaDePedido linea : lineas) {
            count++;
        }
        
        // Verificar que hay 4 líneas (las creadas en setUp)
        assertEquals(4, count);
    }
    
    @Test
    void testDelete() {
        // Eliminar una línea
        lineaDePedidoRepository.delete(lineaPrecioAlto);
        
        // Verificar que se eliminó
        Optional<LineaDePedido> shouldBeDeleted = lineaDePedidoRepository.findById(lineaPrecioAlto.getId());
        assertFalse(shouldBeDeleted.isPresent());
        
        // Verificar que el resto sigue existiendo
        Iterable<LineaDePedido> remaining = lineaDePedidoRepository.findAll();
        int count = 0;
        for (LineaDePedido linea : remaining) {
            count++;
        }
        assertEquals(3, count);
    }
    
    @Test
    void testDeleteById() {
        // Eliminar una línea por ID
        lineaDePedidoRepository.deleteById(lineaMinima.getId());
        
        // Verificar que se eliminó
        Optional<LineaDePedido> shouldBeDeleted = lineaDePedidoRepository.findById(lineaMinima.getId());
        assertFalse(shouldBeDeleted.isPresent());
        
        // Verificar que el resto sigue existiendo
        assertEquals(3, ((List<LineaDePedido>) lineaDePedidoRepository.findAll()).size());
    }

    // TESTS PARA MÉTODOS DE CONSULTA PERSONALIZADOS
    
    @Test
    void testFindLineaDePedidosByCantidad_Success() {
        // Buscar líneas con cantidad 3
        List<LineaDePedido> lineas = lineaDePedidoRepository.findLineaDePedidosByCantidad(3);
        
        // Verificar que se encontró 1 línea con cantidad = 3
        assertEquals(1, lineas.size());
        assertEquals(lineaNormal.getId(), lineas.get(0).getId());
    }
    
    @Test
    void testFindLineaDePedidosByCantidad_Multiple() {
        // Crear otra línea con la misma cantidad para probar múltiples resultados
        LineaDePedido otraLinea = new LineaDePedido();
        otraLinea.setCantidad(3);
        otraLinea.setPrecioLinea(15.0);
        otraLinea.setProducto(producto2);
        otraLinea.setPedido(pedido1);
        lineaDePedidoRepository.save(otraLinea);
        
        // Buscar líneas con cantidad 3
        List<LineaDePedido> lineas = lineaDePedidoRepository.findLineaDePedidosByCantidad(3);
        
        // Verificar que se encontraron 2 líneas con cantidad = 3
        assertEquals(2, lineas.size());
    }
    
    @Test
    void testFindLineaDePedidosByCantidad_NotFound() {
        // Buscar líneas con una cantidad que no existe
        List<LineaDePedido> lineas = lineaDePedidoRepository.findLineaDePedidosByCantidad(999);
        
        // Verificar que no se encontraron líneas
        assertTrue(lineas.isEmpty());
    }
    
    
    @Test
    void testFindLineaDePedidosByPrecioLinea_Success() {
        // Buscar líneas con precioLinea 9.0
        List<LineaDePedido> lineas = lineaDePedidoRepository.findLineaDePedidosByPrecioLinea(9.0);
        
        // Verificar que se encontró 1 línea con precioLinea = 9.0
        assertEquals(1, lineas.size());
        assertEquals(lineaNormal.getId(), lineas.get(0).getId());
    }
    
    @Test
    void testFindLineaDePedidosByPrecioLinea_NotFound() {
        // Buscar líneas con un precio que no existe
        List<LineaDePedido> lineas = lineaDePedidoRepository.findLineaDePedidosByPrecioLinea(999.0);
        
        // Verificar que no se encontraron líneas
        assertTrue(lineas.isEmpty());
    }
    
    
    @Test
    void testFindLineaDePedidosByPrecioLinea_Null() {
        // Buscar con precio null (debería devolver una lista vacía)
        List<LineaDePedido> lineas = lineaDePedidoRepository.findLineaDePedidosByPrecioLinea(null);
        
        // Verificar que devuelve lista vacía
        assertTrue(lineas.isEmpty(), "Se esperaba una lista vacía cuando se busca con precioLinea null");
    }
    @Test
    void testFindLineaDePedidosByPedidoId_Success() {
        // Buscar líneas por ID de pedido existente
        List<LineaDePedido> lineas = lineaDePedidoRepository.findLineaDePedidosByPedidoId(pedido1.getId());
        
        // Verificar que se encontraron 2 líneas para el pedido1
        assertEquals(2, lineas.size());
        
        // Verificar que las líneas corresponden al pedido correcto
        for (LineaDePedido linea : lineas) {
            assertEquals(pedido1.getId(), linea.getPedido().getId());
        }
    }
    
    @Test
    void testFindLineaDePedidosByPedidoId_NotFound() {
        // Buscar líneas con un ID de pedido que no existe
        List<LineaDePedido> lineas = lineaDePedidoRepository.findLineaDePedidosByPedidoId(999);
        
        // Verificar que no se encontraron líneas
        assertTrue(lineas.isEmpty());
    }
    
    @Test
    void testFindLineaDePedidosByProductoId_Success() {
        // Buscar líneas por ID de producto existente
        List<LineaDePedido> lineas = lineaDePedidoRepository.findLineaDePedidosByProductoId(producto1.getId());
        
        // Verificar que se encontraron 2 líneas para el producto1
        assertEquals(2, lineas.size());
        
        // Verificar que las líneas corresponden al producto correcto
        for (LineaDePedido linea : lineas) {
            assertEquals(producto1.getId(), linea.getProducto().getId());
        }
    }
    
    @Test
    void testFindLineaDePedidosByProductoId_NotFound() {
        // Buscar líneas con un ID de producto que no existe
        List<LineaDePedido> lineas = lineaDePedidoRepository.findLineaDePedidosByProductoId(999);
        
        // Verificar que no se encontraron líneas
        assertTrue(lineas.isEmpty());
    }
    
    @Test
    void testFindLineaDePedidosByProductoIdAndCantidad_Success() {
        // Buscar líneas por ID de producto y cantidad existente
        List<LineaDePedido> lineas = lineaDePedidoRepository.findLineaDePedidosByProductoIdAndCantidad(
            producto1.getId(), 3);
        
        // Verificar que se encontró 1 línea con el producto y cantidad correctos
        assertEquals(1, lineas.size());
        assertEquals(producto1.getId(), lineas.get(0).getProducto().getId());
        assertEquals(3, lineas.get(0).getCantidad());
    }
    
    @Test
    void testFindLineaDePedidosByProductoIdAndCantidad_NotFound() {
        // Buscar líneas con combinación que no existe
        List<LineaDePedido> lineas = lineaDePedidoRepository.findLineaDePedidosByProductoIdAndCantidad(
            producto1.getId(), 999);
        
        // Verificar que no se encontraron líneas
        assertTrue(lineas.isEmpty());
    }
    
    @Test
    void testFindLineaDePedidosByProductoIdAndCantidad_ProductoNoExiste() {
        // Buscar con producto que no existe
        List<LineaDePedido> lineas = lineaDePedidoRepository.findLineaDePedidosByProductoIdAndCantidad(
            999, 3);
        
        // Verificar que no se encontraron líneas
        assertTrue(lineas.isEmpty());
    }
    
    @Test
    void testFindLineaDePedidosByProductoIdAndPrecioLinea_Success() {
        // Buscar líneas por ID de producto y precio existente
        List<LineaDePedido> lineas = lineaDePedidoRepository.findLineaDePedidosByProductoIdAndPrecioLinea(
            producto1.getId(), 9.0);
        
        // Verificar que se encontró 1 línea con el producto y precio correctos
        assertEquals(1, lineas.size());
        assertEquals(producto1.getId(), lineas.get(0).getProducto().getId());
        assertEquals(9.0, lineas.get(0).getPrecioLinea());
    }
    
    @Test
    void testFindLineaDePedidosByProductoIdAndPrecioLinea_NotFound() {
        // Buscar líneas con combinación que no existe
        List<LineaDePedido> lineas = lineaDePedidoRepository.findLineaDePedidosByProductoIdAndPrecioLinea(
            producto1.getId(), 999.0);
        
        // Verificar que no se encontraron líneas
        assertTrue(lineas.isEmpty());
    }
    
    @Test
    void testFindLineaDePedidosByProductoIdAndPrecioLinea_ProductoNoExiste() {
        // Buscar con producto que no existe
        List<LineaDePedido> lineas = lineaDePedidoRepository.findLineaDePedidosByProductoIdAndPrecioLinea(
            999, 9.0);
        
        // Verificar que no se encontraron líneas
        assertTrue(lineas.isEmpty());
    }


}
