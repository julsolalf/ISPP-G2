package ispp_g2.gastrostock.testLineaDeCarrito;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;

import ispp_g2.gastrostock.carrito.Carrito;
import ispp_g2.gastrostock.carrito.CarritoRepository;
import ispp_g2.gastrostock.categorias.Categoria;
import ispp_g2.gastrostock.categorias.CategoriaRepository;
import ispp_g2.gastrostock.categorias.Pertenece;
import ispp_g2.gastrostock.dueno.Dueno;
import ispp_g2.gastrostock.dueno.DuenoRepository;
import ispp_g2.gastrostock.lineaDeCarrito.LineaDeCarrito;
import ispp_g2.gastrostock.lineaDeCarrito.LineaDeCarritoRepository;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioRepository;
import ispp_g2.gastrostock.productoInventario.ProductoInventario;
import ispp_g2.gastrostock.productoInventario.ProductoInventarioRepository;
import ispp_g2.gastrostock.proveedores.Proveedor;
import ispp_g2.gastrostock.proveedores.ProveedorRepository;
import ispp_g2.gastrostock.user.Authorities;
import ispp_g2.gastrostock.user.AuthoritiesRepository;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserRepository;
import jakarta.validation.ConstraintViolationException;

@DataJpaTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
class LineaDeCarritoRepositoryTest {

    @Autowired
    private LineaDeCarritoRepository lineaDeCarritoRepository;
    
    @Autowired
    private CarritoRepository carritoRepository;
    
    @Autowired
    private ProductoInventarioRepository productoInventarioRepository;
    
    @Autowired
    private ProveedorRepository proveedorRepository;
    
    @Autowired
    private NegocioRepository negocioRepository;
    
    @Autowired
    private DuenoRepository duenoRepository;
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AuthoritiesRepository authoritiesRepository;
    
    private LineaDeCarrito lineaNormal, lineaCantidadGrande, lineaPrecioAlto;
    private ProductoInventario producto1, producto2;
    private Carrito carrito1, carrito2;
    private Proveedor proveedor;
    private Negocio negocio;
    private Dueno dueno;
    private Categoria categoria;
    
    @BeforeEach
    void setUp() {
        // Limpiar los repositorios para asegurar un estado consistente
        lineaDeCarritoRepository.deleteAll();
        carritoRepository.deleteAll();
        productoInventarioRepository.deleteAll();
        proveedorRepository.deleteAll();
        categoriaRepository.deleteAll();
        negocioRepository.deleteAll();
        duenoRepository.deleteAll();
        
        // Crear autoridad
        Authorities authority = new Authorities();
        authority.setAuthority("DUENO");
        authority = authoritiesRepository.save(authority);

        // Crear usuario
        User user = new User();
        user.setUsername("juangarcia");
        user.setPassword("password123");
        user.setAuthority(authority);
        user = userRepository.save(user);

        // Crear dueño
        dueno = new Dueno();
        dueno.setFirstName("Juan");
        dueno.setLastName("García");
        dueno.setEmail("juan@example.com");
        dueno.setNumTelefono("666111222");
        dueno.setTokenDueno("TOKEN999");
        dueno.setUser(user);
        dueno = duenoRepository.save(dueno);

        // Crear negocio
        negocio = new Negocio();
        negocio.setName("Restaurante Test");
        negocio.setDireccion("Calle Test 123");
        negocio.setCiudad("Ciudad Test");
        negocio.setPais("País Test");
        negocio.setCodigoPostal("12345");
        negocio.setTokenNegocio(12345);
        negocio.setDueno(dueno);
        negocio = negocioRepository.save(negocio);

        // Crear proveedor
        proveedor = new Proveedor();
        proveedor.setName("Proveedor Test");
        proveedor.setEmail("proveedor@test.com");
        proveedor.setTelefono("123456789");
        proveedor.setDireccion("Dirección Test");
        proveedor.setNegocio(negocio);
        proveedor = proveedorRepository.save(proveedor);
        
        // Crear categoría
        categoria = new Categoria();
        categoria.setName("Alimentos");
        categoria.setNegocio(negocio);
        categoria.setPertenece(Pertenece.INVENTARIO);
        categoria = categoriaRepository.save(categoria);

        // Crear productos de inventario
        producto1 = new ProductoInventario();
        producto1.setName("Harina");
        producto1.setCategoria(categoria);
        producto1.setPrecioCompra(2.5);
        producto1.setCantidadAviso(5);
        producto1.setCantidadDeseada(10);
        producto1.setProveedor(proveedor);
        producto1 = productoInventarioRepository.save(producto1);
        
        producto2 = new ProductoInventario();
        producto2.setName("Azúcar");
        producto2.setCategoria(categoria);
        producto2.setPrecioCompra(1.8);
        producto2.setCantidadAviso(3);
        producto2.setCantidadDeseada(8);
        producto2.setProveedor(proveedor);
        producto2 = productoInventarioRepository.save(producto2);

        // Crear carritos
        carrito1 = new Carrito();
        carrito1.setPrecioTotal(100.0);
        carrito1.setProveedor(proveedor);
        carrito1.setDiaEntrega(LocalDate.now().plusDays(1));
        carrito1 = carritoRepository.save(carrito1);

        carrito2 = new Carrito();
        carrito2.setPrecioTotal(200.0);
        carrito2.setProveedor(proveedor);
        carrito2.setDiaEntrega(LocalDate.now().plusDays(2));
        carrito2 = carritoRepository.save(carrito2);

        // Crear líneas de carrito con diferentes características para las pruebas
        lineaNormal = new LineaDeCarrito();
        lineaNormal.setCantidad(3);
        lineaNormal.setPrecioLinea(7.5);
        lineaNormal.setProducto(producto1);
        lineaNormal.setCarrito(carrito1);
        lineaNormal = lineaDeCarritoRepository.save(lineaNormal);

        lineaCantidadGrande = new LineaDeCarrito();
        lineaCantidadGrande.setCantidad(10);
        lineaCantidadGrande.setPrecioLinea(25.0);
        lineaCantidadGrande.setProducto(producto1);
        lineaCantidadGrande.setCarrito(carrito1);
        lineaCantidadGrande = lineaDeCarritoRepository.save(lineaCantidadGrande);

        lineaPrecioAlto = new LineaDeCarrito();
        lineaPrecioAlto.setCantidad(4);
        lineaPrecioAlto.setPrecioLinea(7.2);
        lineaPrecioAlto.setProducto(producto2);
        lineaPrecioAlto.setCarrito(carrito2);
        lineaPrecioAlto = lineaDeCarritoRepository.save(lineaPrecioAlto);
    }



    @Test
    void testSave() {
        LineaDeCarrito nuevaLinea = new LineaDeCarrito();
        nuevaLinea.setCantidad(2);
        nuevaLinea.setPrecioLinea(5.0);
        nuevaLinea.setProducto(producto1);
        nuevaLinea.setCarrito(carrito1);
        
        LineaDeCarrito saved = lineaDeCarritoRepository.save(nuevaLinea);
        
        assertNotNull(saved.getId());
        assertEquals(2, saved.getCantidad());
        assertEquals(5.0, saved.getPrecioLinea());
        
        LineaDeCarrito retrieved = lineaDeCarritoRepository.findById(saved.getId()).orElse(null);
        assertNotNull(retrieved);
        assertEquals(2, retrieved.getCantidad());
        assertEquals(5.0, retrieved.getPrecioLinea());
    }
    
    @Test
    void testSave_WithInvalidData() {
        LineaDeCarrito invalida = new LineaDeCarrito();
        invalida.setCantidad(-1);
        invalida.setPrecioLinea(3.0);
        invalida.setProducto(producto1);
        invalida.setCarrito(carrito1);
        
        assertThrows(ConstraintViolationException.class, () -> {
            lineaDeCarritoRepository.save(invalida);
            lineaDeCarritoRepository.findAll();
        });
    }
    
    @Test
    void testSave_WithNullData() {
        LineaDeCarrito invalida = new LineaDeCarrito();
        invalida.setCantidad(null);
        invalida.setPrecioLinea(3.0);
        invalida.setProducto(producto1);
        invalida.setCarrito(carrito1);
        
        assertThrows(ConstraintViolationException.class, () -> {
            lineaDeCarritoRepository.save(invalida);
            lineaDeCarritoRepository.findAll();
        });
    }
    
    @Test
    void testSave_WithNullPrecioLinea() {
        LineaDeCarrito invalida = new LineaDeCarrito();
        invalida.setCantidad(3);
        invalida.setPrecioLinea(null);
        invalida.setProducto(producto1);
        invalida.setCarrito(carrito1);
        
        assertThrows(ConstraintViolationException.class, () -> {
            lineaDeCarritoRepository.save(invalida);
            lineaDeCarritoRepository.findAll(); 
        });
    }
    
    

    @Test
    void testFindById() {
        Optional<LineaDeCarrito> found = lineaDeCarritoRepository.findById(lineaNormal.getId());

        assertTrue(found.isPresent());
        assertEquals(3, found.get().getCantidad());
        assertEquals(7.5, found.get().getPrecioLinea());
    }
    
    @Test
    void testFindById_NotFound() {
        Optional<LineaDeCarrito> notFound = lineaDeCarritoRepository.findById(999);
        
        assertFalse(notFound.isPresent());
    }
    
    @Test
    void testFindAll() {

        Iterable<LineaDeCarrito> lineas = lineaDeCarritoRepository.findAll();
        
        long count = StreamSupport.stream(lineas.spliterator(), false).count();
        
        assertEquals(3, count);
    }
    
    @Test
    void testDelete() {

        lineaDeCarritoRepository.delete(lineaPrecioAlto);

        Optional<LineaDeCarrito> shouldBeDeleted = lineaDeCarritoRepository.findById(lineaPrecioAlto.getId());
        assertFalse(shouldBeDeleted.isPresent());

        long count = StreamSupport.stream(lineaDeCarritoRepository.findAll().spliterator(), false).count();
        assertEquals(2, count);
    }
    
    @Test
    void testDeleteById() {
        lineaDeCarritoRepository.deleteById(lineaNormal.getId());
        

        Optional<LineaDeCarrito> shouldBeDeleted = lineaDeCarritoRepository.findById(lineaNormal.getId());
        assertFalse(shouldBeDeleted.isPresent());
        
        long count = StreamSupport.stream(lineaDeCarritoRepository.findAll().spliterator(), false).count();
        assertEquals(2, count);
    }

    // TESTS PARA MÉTODOS DE CONSULTA PERSONALIZADOS
    
    @Test
    void testFindLineaDeCarritoByCarritoId_Success() {
        List<LineaDeCarrito> lineas = lineaDeCarritoRepository.findLineaDeCarritoByCarritoId(carrito1.getId());
        
        assertEquals(2, lineas.size());
        
        for (LineaDeCarrito linea : lineas) {
            assertEquals(carrito1.getId(), linea.getCarrito().getId());
        }
    }
    
    @Test
    void testFindLineaDeCarritoByCarritoId_NotFound() {
        List<LineaDeCarrito> lineas = lineaDeCarritoRepository.findLineaDeCarritoByCarritoId(999);
        
        assertTrue(lineas.isEmpty());
    }
    
    @Test
    void testFindLineaDeCarritoByCarritoIdAndProductoId_Success() {
        List<LineaDeCarrito> lineas = lineaDeCarritoRepository.findLineaDeCarritoByCarritoIdAndProductoId(
            carrito1.getId(), producto1.getId());

        assertEquals(2, lineas.size());
        
        for (LineaDeCarrito linea : lineas) {
            assertEquals(carrito1.getId(), linea.getCarrito().getId());
            assertEquals(producto1.getId(), linea.getProducto().getId());
        }
    }
    
    @Test
    void testFindLineaDeCarritoByCarritoIdAndProductoId_NotFound() {

        List<LineaDeCarrito> lineas = lineaDeCarritoRepository.findLineaDeCarritoByCarritoIdAndProductoId(
            carrito1.getId(), 999);
        
        assertTrue(lineas.isEmpty());
    }
    
    @Test
    void testFindLineaDeCarritoByCarritoIdAndProductoId_CarritoNoExiste() {

        List<LineaDeCarrito> lineas = lineaDeCarritoRepository.findLineaDeCarritoByCarritoIdAndProductoId(
            999, producto1.getId());
        
        assertTrue(lineas.isEmpty());
    }
    
    @Test
    void testFindLineaDeCarritoByCarritoId_NullId() {

        List<LineaDeCarrito> linea = lineaDeCarritoRepository.findLineaDeCarritoByCarritoId(null);
        assertTrue(linea.isEmpty());
        
    }
    
    @Test
    void testFindLineaDeCarritoByCarritoIdAndProductoId_NullIds() {

        List<LineaDeCarrito> linea = lineaDeCarritoRepository.findLineaDeCarritoByCarritoIdAndProductoId(null, null);
        assertTrue(linea.isEmpty());
    }
    
    @Test
    void testDeleteAll() {
        lineaDeCarritoRepository.deleteAll();
        
        long count = StreamSupport.stream(lineaDeCarritoRepository.findAll().spliterator(), false).count();
        assertEquals(0, count);
    }
    
    @Test
    void testExistsById() {
        assertTrue(lineaDeCarritoRepository.existsById(lineaNormal.getId()));
        assertFalse(lineaDeCarritoRepository.existsById(999));
    }
    
    @Test
    void testCount() {
        assertEquals(3, lineaDeCarritoRepository.count());
    }
}