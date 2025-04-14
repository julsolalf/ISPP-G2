package ispp_g2.gastrostock.testCarrito;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;


import ispp_g2.gastrostock.carrito.Carrito;
import ispp_g2.gastrostock.carrito.CarritoRepository;
import ispp_g2.gastrostock.dueno.Dueno;
import ispp_g2.gastrostock.dueno.DuenoRepository;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioRepository;
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
class CarritoRepositoryTest {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AuthoritiesRepository authoritiesRepository;
    
    @Autowired
    private NegocioRepository negocioRepository;
    
    @Autowired
    private DuenoRepository duenoRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Carrito carrito1, carrito2, carritoInvalido;
    private Negocio negocio;
    private Proveedor proveedor;
    private Dueno dueno;

    @BeforeEach
    void setUp() {
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

        // Crear carritos válidos
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

        // Crear carrito inválido
        carritoInvalido = new Carrito();
        carritoInvalido.setPrecioTotal(-100.0);
        carritoInvalido.setProveedor(null);
        carritoInvalido.setDiaEntrega(null);
    }

    @Test
    void testSave() {
        Carrito nuevoCarrito = new Carrito();
        nuevoCarrito.setPrecioTotal(300.0);
        nuevoCarrito.setProveedor(proveedor);
        nuevoCarrito.setDiaEntrega(LocalDate.now().plusDays(3));
        
        Carrito saved = carritoRepository.save(nuevoCarrito);
        
        assertNotNull(saved.getId());
        assertEquals(300.0, saved.getPrecioTotal());
    }

    @Test
    void testFindAll() {
        Iterable<Carrito> carritos = carritoRepository.findAll();
        long count = StreamSupport.stream(carritos.spliterator(), false).count();
        assertEquals(2, count);
    }

    @Test
    void testFindById() {
        Optional<Carrito> found = carritoRepository.findById(carrito1.getId());
        assertTrue(found.isPresent());
        assertEquals(100.0, found.get().getPrecioTotal());
    }

    @Test
    void testFindById_NotFound() {
        Optional<Carrito> notFound = carritoRepository.findById(999);
        assertFalse(notFound.isPresent());
    }

    @Test
    void testDelete() {
        carritoRepository.delete(carrito2);
        
        Optional<Carrito> shouldBeDeleted = carritoRepository.findById(carrito2.getId());
        assertFalse(shouldBeDeleted.isPresent());
        
        Iterable<Carrito> remaining = carritoRepository.findAll();
        long count = StreamSupport.stream(remaining.spliterator(), false).count();
        assertEquals(1, count);
    }

    @Test
    void testFindByProveedorId_Success() {
        List<Carrito> found = carritoRepository.findByProveedorId(proveedor.getId());
        assertEquals(2, found.size());
        assertTrue(found.stream().allMatch(c -> c.getProveedor().getId().equals(proveedor.getId())));
    }

    @Test
    void testFindByProveedorId_NotFound() {
        List<Carrito> notFound = carritoRepository.findByProveedorId(999);
        assertTrue(notFound.isEmpty());
    }

    @Test
    void testSave_WithPreexistingId() {
        // Intentar guardar un carrito con un ID que ya existe
        Carrito carritoConIdExistente = new Carrito();
        carritoConIdExistente.setId(carrito1.getId());
        carritoConIdExistente.setPrecioTotal(500.0);
        carritoConIdExistente.setProveedor(proveedor);
        carritoConIdExistente.setDiaEntrega(LocalDate.now().plusDays(3));

        Carrito saved = carritoRepository.save(carritoConIdExistente);
        
        assertEquals(carrito1.getId(), saved.getId());
        assertEquals(500.0, saved.getPrecioTotal());
    }

    @Test
    void testSave_MultipleCarritos() {
        // Guardar varios carritos en secuencia
        List<Carrito> carritosAGuardar = new ArrayList<>();
        
        for (int i = 0; i < 3; i++) {
            Carrito carrito = new Carrito();
            carrito.setPrecioTotal(100.0 * (i + 1));
            carrito.setProveedor(proveedor);
            carrito.setDiaEntrega(LocalDate.now().plusDays(i + 1));
            carritosAGuardar.add(carrito);
        }
        
        carritosAGuardar.forEach(c -> carritoRepository.save(c));
        
        long count = StreamSupport.stream(carritoRepository.findAll().spliterator(), false).count();
        assertEquals(5, count); // 2 del setup + 3 nuevos
    }

    @Test
    void testFindByProveedorId_MultipleResults() {
        // Crear y guardar más carritos para el mismo proveedor
        Carrito nuevoCarrito = new Carrito();
        nuevoCarrito.setPrecioTotal(300.0);
        nuevoCarrito.setProveedor(proveedor);
        nuevoCarrito.setDiaEntrega(LocalDate.now().plusDays(3));
        carritoRepository.save(nuevoCarrito);
        
        List<Carrito> found = carritoRepository.findByProveedorId(proveedor.getId());
        assertEquals(3, found.size());
        assertTrue(found.stream().allMatch(c -> c.getProveedor().getId().equals(proveedor.getId())));
    }

    @Test
    void testDeleteAll() {
        carritoRepository.deleteAll();
        
        Iterable<Carrito> remaining = carritoRepository.findAll();
        assertEquals(0, StreamSupport.stream(remaining.spliterator(), false).count());
    }

    @Test
    void testFindAll_EmptyRepository() {
        carritoRepository.deleteAll();
        
        Iterable<Carrito> carritos = carritoRepository.findAll();
        assertEquals(0, StreamSupport.stream(carritos.spliterator(), false).count());
    }

    @Test
    void testDeleteById() {
        carritoRepository.deleteById(carrito1.getId());
        
        Optional<Carrito> shouldBeDeleted = carritoRepository.findById(carrito1.getId());
        assertFalse(shouldBeDeleted.isPresent());
        
        long count = StreamSupport.stream(carritoRepository.findAll().spliterator(), false).count();
        assertEquals(1, count);
    }

    @Test
    void testSaveAll() {
        List<Carrito> carritosNuevos = Arrays.asList(
            createTestCarrito(400.0, 4),
            createTestCarrito(500.0, 5)
        );
        
        carritoRepository.saveAll(carritosNuevos);
        
        long count = StreamSupport.stream(carritoRepository.findAll().spliterator(), false).count();
        assertEquals(4, count); // 2 originales + 2 nuevos
    }

    // Método auxiliar para crear carritos de prueba
    private Carrito createTestCarrito(double precio, int diasEntrega) {
        Carrito carrito = new Carrito();
        carrito.setPrecioTotal(precio);
        carrito.setProveedor(proveedor);
        carrito.setDiaEntrega(LocalDate.now().plusDays(diasEntrega));
        return carrito;
    }

    @Test
    void testExistsById() {
        assertTrue(carritoRepository.existsById(carrito1.getId()));
        assertFalse(carritoRepository.existsById(999));
    }

    @Test
    void testCount() {
        assertEquals(2, carritoRepository.count());
    }

    @Test
    void testDeleteAll_Collection() {
        List<Carrito> toDelete = Arrays.asList(carrito1, carrito2);
        carritoRepository.deleteAll(toDelete);
        assertEquals(0, carritoRepository.count());
    }

    @Test
    void testSave_NullDiaEntrega() {
        Carrito carrito = new Carrito();
        carrito.setPrecioTotal(200.0);
        carrito.setProveedor(proveedor);
        carrito.setDiaEntrega(null); // Viola @NotNull de diaEntrega

        assertThrows(ConstraintViolationException.class, () -> {
            carritoRepository.save(carrito);
            entityManager.flush();
        });
    }

    @Test
    void testSave_NullPrecioTotal() {
        Carrito carrito = new Carrito();
        carrito.setPrecioTotal(null); // Viola @NotNull de precioTotal
        carrito.setProveedor(proveedor);
        carrito.setDiaEntrega(LocalDate.now().plusDays(1));

        assertThrows(ConstraintViolationException.class, () -> {
            carritoRepository.save(carrito);
            entityManager.flush();
        });
    }

    @Test
    void testSave_NegativePrecioTotal() {
        Carrito carrito = new Carrito();
        carrito.setPrecioTotal(-100.0); // Viola @Positive de precioTotal
        carrito.setProveedor(proveedor);
        carrito.setDiaEntrega(LocalDate.now().plusDays(1));

        assertThrows(ConstraintViolationException.class, () -> {
            carritoRepository.save(carrito);
            entityManager.flush();
        });
    }

    @Test
    void testSave_ZeroPrecioTotal() {
        Carrito carrito = new Carrito();
        carrito.setPrecioTotal(0.0); // Viola @Positive de precioTotal
        carrito.setProveedor(proveedor);
        carrito.setDiaEntrega(LocalDate.now().plusDays(1));

        assertThrows(ConstraintViolationException.class, () -> {
            carritoRepository.save(carrito);
            entityManager.flush();
        });
    }
}
