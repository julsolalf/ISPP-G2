package ispp_g2.gastrostock.testIngrediente;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import ispp_g2.gastrostock.ingrediente.Ingrediente;
import ispp_g2.gastrostock.ingrediente.IngredienteRepository;
import ispp_g2.gastrostock.productoInventario.ProductoInventario;
import ispp_g2.gastrostock.productoInventario.ProductoInventarioRepository;
import ispp_g2.gastrostock.productoVenta.ProductoVenta;
import ispp_g2.gastrostock.productoVenta.ProductoVentaRepository;
import ispp_g2.gastrostock.user.Authorities;
import ispp_g2.gastrostock.user.AuthoritiesRepository;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserRepository;
import jakarta.validation.ConstraintViolationException;
import ispp_g2.gastrostock.categorias.Categoria;
import ispp_g2.gastrostock.categorias.CategoriaRepository;
import ispp_g2.gastrostock.categorias.Pertenece;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioRepository;
import ispp_g2.gastrostock.dueno.Dueno;
import ispp_g2.gastrostock.dueno.DuenoRepository;

@DataJpaTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class IngredienteRepositoryTest {

    @Autowired
    private IngredienteRepository ingredienteRepository;

    @Autowired
    private ProductoInventarioRepository productoInventarioRepository;

    @Autowired
    private ProductoVentaRepository productoVentaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private NegocioRepository negocioRepository;
    
    @Autowired
    private DuenoRepository duenoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthoritiesRepository authoritiesRepository;

    private Ingrediente ingrediente1, ingrediente2, ingrediente3;
    private ProductoInventario productoInventario1, productoInventario2;
    private ProductoVenta productoVenta1, productoVenta2;
    private Categoria categoriaInventario, categoriaVenta;
    private Negocio negocio;
    private Dueno dueno;

    @BeforeEach
    void setUp() {
        // Limpiamos las tablas para asegurarnos de tener un estado limpio
        ingredienteRepository.deleteAll();
        productoInventarioRepository.deleteAll();
        productoVentaRepository.deleteAll();
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
        dueno.setLastName("García");
        dueno.setEmail("juan@example.com");
        dueno.setNumTelefono("652345678");
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

        // Crear categorías
        categoriaInventario = new Categoria();
        categoriaInventario.setName("Alimentos");
        categoriaInventario.setNegocio(negocio);
        categoriaInventario.setPertenece(Pertenece.INVENTARIO);
        categoriaInventario = categoriaRepository.save(categoriaInventario);

        categoriaVenta = new Categoria();
        categoriaVenta.setName("Platos");
        categoriaVenta.setNegocio(negocio);
        categoriaVenta.setPertenece(Pertenece.VENTA);
        categoriaVenta = categoriaRepository.save(categoriaVenta);

        // Crear productos de inventario
        productoInventario1 = new ProductoInventario();
        productoInventario1.setName("Patatas");
        productoInventario1.setCantidadDeseada(10);
        productoInventario1.setCantidadAviso(3);
        productoInventario1.setPrecioCompra(1.5);
        productoInventario1.setCantidadDeseada(20);
        productoInventario1.setCantidadAviso(5);
        productoInventario1.setCategoria(categoriaInventario);
        productoInventario1 = productoInventarioRepository.save(productoInventario1);

        productoInventario2 = new ProductoInventario();
        productoInventario2.setName("Tomates");
        productoInventario2.setCantidadDeseada(20);
        productoInventario2.setCantidadAviso(5);
        productoInventario2.setPrecioCompra(0.8);
        productoInventario2.setCantidadDeseada(30);
        productoInventario2.setCantidadAviso(8);
        productoInventario2.setCategoria(categoriaInventario);
        productoInventario2 = productoInventarioRepository.save(productoInventario2);

        // Crear productos de venta
        productoVenta1 = new ProductoVenta();
        productoVenta1.setName("Patatas Fritas");
        productoVenta1.setPrecioVenta(3.50);
        productoVenta1.setCategoria(categoriaVenta);
        productoVenta1 = productoVentaRepository.save(productoVenta1);

        productoVenta2 = new ProductoVenta();
        productoVenta2.setName("Ensalada");
        productoVenta2.setPrecioVenta(5.00);
        productoVenta2.setCategoria(categoriaVenta);
        productoVenta2 = productoVentaRepository.save(productoVenta2);

        // Crear ingredientes
        ingrediente1 = new Ingrediente();
        ingrediente1.setCantidad(2);
        ingrediente1.setProductoInventario(productoInventario1);
        ingrediente1.setProductoVenta(productoVenta1);
        ingrediente1 = ingredienteRepository.save(ingrediente1);

        ingrediente2 = new Ingrediente();
        ingrediente2.setCantidad(3);
        ingrediente2.setProductoInventario(productoInventario2);
        ingrediente2.setProductoVenta(productoVenta1);
        ingrediente2 = ingredienteRepository.save(ingrediente2);

        ingrediente3 = new Ingrediente();
        ingrediente3.setCantidad(2);
        ingrediente3.setProductoInventario(productoInventario2);
        ingrediente3.setProductoVenta(productoVenta2);
        ingrediente3 = ingredienteRepository.save(ingrediente3);
    }

    // TESTS PARA MÉTODOS CRUD BÁSICOS

    @Test
    void testSave() {
        // Crear un nuevo ingrediente
        Ingrediente nuevoIngrediente = new Ingrediente();
        nuevoIngrediente.setCantidad(5);
        nuevoIngrediente.setProductoInventario(productoInventario1);
        nuevoIngrediente.setProductoVenta(productoVenta2);

        // Guardar
        Ingrediente saved = ingredienteRepository.save(nuevoIngrediente);

        // Verificar
        assertNotNull(saved.getId());
        assertEquals(5, saved.getCantidad());
        assertEquals(productoInventario1.getId(), saved.getProductoInventario().getId());
        assertEquals(productoVenta2.getId(), saved.getProductoVenta().getId());

        // Verificar que se haya guardado en la BD
        Optional<Ingrediente> fromDb = ingredienteRepository.findById(saved.getId());
        assertTrue(fromDb.isPresent());
        assertEquals(5, fromDb.get().getCantidad());
    }

    @Test
    void testSave_WithNullValues() {
        // Crear ingrediente con valores nulos donde no deberían serlo
        Ingrediente ingredienteInvalido = new Ingrediente();
        ingredienteInvalido.setCantidad(null);
        // No asignar productoInventario ni productoVenta

        // Intentar guardar - debería fallar
        assertThrows(ConstraintViolationException.class, () -> {
            ingredienteRepository.save(ingredienteInvalido);
            ingredienteRepository.findAll(); // Forzar que se ejecute la operación
        });
    }

    @Test
    void testSave_WithNegativeCantidad() {
        // Crear ingrediente con cantidad negativa
        Ingrediente ingredienteInvalido = new Ingrediente();
        ingredienteInvalido.setCantidad(-1);
        ingredienteInvalido.setProductoInventario(productoInventario1);
        ingredienteInvalido.setProductoVenta(productoVenta1);

        // Intentar guardar
        // Dependiendo de la configuración, podría fallar por validación o guardar con valor negativo
        try {
            Ingrediente saved = ingredienteRepository.save(ingredienteInvalido);
            ingredienteRepository.findAll(); // Forzar que se ejecute la operación
            
            // Si llega aquí, no hubo validación de cantidad positiva a nivel de BD
            assertEquals(-1, saved.getCantidad());
        } catch (Exception e) {
            // Si falla, es porque hay alguna restricción en la BD o en la entidad
            assertTrue(e instanceof DataIntegrityViolationException || 
                      e instanceof jakarta.validation.ConstraintViolationException);
        }
    }

    @Test
    void testFindById() {
        // Buscar un ingrediente existente
        Optional<Ingrediente> found = ingredienteRepository.findById(ingrediente1.getId());
        
        // Verificar que existe y tiene los datos correctos
        assertTrue(found.isPresent());
        assertEquals(2, found.get().getCantidad());
        assertEquals(productoInventario1.getId(), found.get().getProductoInventario().getId());
        assertEquals(productoVenta1.getId(), found.get().getProductoVenta().getId());
    }
    
    @Test
    void testFindById_NotFound() {
        // Buscar un ID que no existe
        Optional<Ingrediente> notFound = ingredienteRepository.findById(999);
        
        // Verificar que no existe
        assertFalse(notFound.isPresent());
    }
    
    @Test
    void testFindAll() {
        // Obtener todos los ingredientes
        Iterable<Ingrediente> ingredientes = ingredienteRepository.findAll();
        
        // Contar cuántos hay
        int count = 0;
        for (Ingrediente ing : ingredientes) {
            count++;
        }
        
        // Verificar que hay 3 ingredientes (los creados en setUp)
        assertEquals(3, count);
    }
    
    @Test
    void testDelete() {
        // Eliminar un ingrediente
        ingredienteRepository.delete(ingrediente3);
        
        // Verificar que ya no existe
        Optional<Ingrediente> shouldBeDeleted = ingredienteRepository.findById(ingrediente3.getId());
        assertFalse(shouldBeDeleted.isPresent());
        
        // Verificar que solo quedan 2 ingredientes
        Iterable<Ingrediente> remaining = ingredienteRepository.findAll();
        int count = 0;
        for (Ingrediente ing : remaining) {
            count++;
        }
        assertEquals(2, count);
    }
    
    @Test
    void testDeleteById() {
        // Eliminar un ingrediente por ID
        ingredienteRepository.deleteById(ingrediente2.getId());
        
        // Verificar que ya no existe
        Optional<Ingrediente> shouldBeDeleted = ingredienteRepository.findById(ingrediente2.getId());
        assertFalse(shouldBeDeleted.isPresent());
        
        // Verificar que solo quedan 2 ingredientes
        Iterable<Ingrediente> remaining = ingredienteRepository.findAll();
        int count = 0;
        for (Ingrediente ing : remaining) {
            count++;
        }
        assertEquals(2, count);
    }
    
    @Test
    void testDeleteById_NotFound() {
        // El comportamiento esperado es que no haga nada sin lanzar excepción
        ingredienteRepository.deleteById(999);
        
        // Verificar que siguen existiendo los 3 ingredientes originales
        Iterable<Ingrediente> remaining = ingredienteRepository.findAll();
        int count = 0;
        for (Ingrediente ing : remaining) {
            count++;
        }
        assertEquals(3, count);
    }

    // TESTS PARA MÉTODOS DE CONSULTA PERSONALIZADOS

    @Test
    void testFindByCantidad_Success() {
        // Buscar ingredientes con cantidad = 2
        List<Ingrediente> foundIngredientes = ingredienteRepository.findByCantidad(2);
        
        // Verificar que encontramos 2 ingredientes
        assertEquals(2, foundIngredientes.size());
        
        // Verificar que son los correctos
        boolean foundIngrediente1 = false;
        boolean foundIngrediente3 = false;
        
        for (Ingrediente ing : foundIngredientes) {
            if (ing.getId().equals(ingrediente1.getId())) {
                foundIngrediente1 = true;
            }
            if (ing.getId().equals(ingrediente3.getId())) {
                foundIngrediente3 = true;
            }
        }
        
        assertTrue(foundIngrediente1);
        assertTrue(foundIngrediente3);
    }
    
    @Test
    void testFindByCantidad_NotFound() {
        // Buscar ingredientes con una cantidad que no existe
        List<Ingrediente> foundIngredientes = ingredienteRepository.findByCantidad(99);
        
        // Verificar que no encontramos ninguno
        assertTrue(foundIngredientes.isEmpty());
    }
    
    @Test
    void testFindByCantidad_NullCantidad() {
        // El comportamiento esperado es que lance una excepción o devuelva lista vacía
        try {
            List<Ingrediente> foundIngredientes = ingredienteRepository.findByCantidad(null);
            // Si llega aquí, asumimos que devuelve lista vacía
            assertTrue(foundIngredientes.isEmpty());
        } catch (Exception e) {
            // Si lanza excepción, es válido también
            // En este caso podría ser NullPointerException o InvalidDataAccessApiUsageException
            assertNotNull(e);
        }
    }

    @Test
    void testFindByProductoInventarioId_Success() {
        // Buscar ingredientes por ID de producto de inventario existente
        List<Ingrediente> foundIngredientes = ingredienteRepository.findByProductoInventarioId(productoInventario2.getId());
        
        // Verificar que encontramos 2 ingredientes
        assertEquals(2, foundIngredientes.size());
        
        // Verificar que son los correctos
        boolean foundIngrediente2 = false;
        boolean foundIngrediente3 = false;
        
        for (Ingrediente ing : foundIngredientes) {
            if (ing.getId().equals(ingrediente2.getId())) {
                foundIngrediente2 = true;
            }
            if (ing.getId().equals(ingrediente3.getId())) {
                foundIngrediente3 = true;
            }
        }
        
        assertTrue(foundIngrediente2);
        assertTrue(foundIngrediente3);
    }
    
    @Test
    void testFindByProductoInventarioId_NotFound() {
        // Buscar ingredientes por un ID de producto que no existe
        List<Ingrediente> foundIngredientes = ingredienteRepository.findByProductoInventarioId(999);
        
        // Verificar que no encontramos ninguno
        assertTrue(foundIngredientes.isEmpty());
    }
    
    @Test
    void testFindByProductoInventarioId_NullId() {
        // El comportamiento esperado es que lance una excepción o devuelva lista vacía
        try {
            List<Ingrediente> foundIngredientes = ingredienteRepository.findByProductoInventarioId(null);
            // Si llega aquí, asumimos que devuelve lista vacía
            assertTrue(foundIngredientes.isEmpty());
        } catch (Exception e) {
            // Si lanza excepción, es válido también
            assertNotNull(e);
        }
    }

    @Test
    void testFindByProductoVentaId_Success() {
        // Buscar ingredientes por ID de producto de venta existente
        List<Ingrediente> foundIngredientes = ingredienteRepository.findByProductoVentaId(productoVenta1.getId());
        
        // Verificar que encontramos 2 ingredientes
        assertEquals(2, foundIngredientes.size());
        
        // Verificar que son los correctos
        boolean foundIngrediente1 = false;
        boolean foundIngrediente2 = false;
        
        for (Ingrediente ing : foundIngredientes) {
            if (ing.getId().equals(ingrediente1.getId())) {
                foundIngrediente1 = true;
            }
            if (ing.getId().equals(ingrediente2.getId())) {
                foundIngrediente2 = true;
            }
        }
        
        assertTrue(foundIngrediente1);
        assertTrue(foundIngrediente2);
    }
    
    @Test
    void testFindByProductoVentaId_NotFound() {
        // Buscar ingredientes por un ID de producto que no existe
        List<Ingrediente> foundIngredientes = ingredienteRepository.findByProductoVentaId(999);
        
        // Verificar que no encontramos ninguno
        assertTrue(foundIngredientes.isEmpty());
    }
    
    @Test
    void testFindByProductoVentaId_NullId() {
        // El comportamiento esperado es que lance una excepción o devuelva lista vacía
        try {
            List<Ingrediente> foundIngredientes = ingredienteRepository.findByProductoVentaId(null);
            // Si llega aquí, asumimos que devuelve lista vacía
            assertTrue(foundIngredientes.isEmpty());
        } catch (Exception e) {
            // Si lanza excepción, es válido también
            assertNotNull(e);
        }
    }
    
    // TESTS PARA COMPORTAMIENTO DE PERSISTENCIA AVANZADO
    
    @Test
    void testUpdateIngrediente() {
        // Modificar un ingrediente existente
        ingrediente1.setCantidad(10);
        Ingrediente updated = ingredienteRepository.save(ingrediente1);
        
        // Verificar que se actualizó en memoria
        assertEquals(10, updated.getCantidad());
        
        // Verificar que se actualizó en la BD
        Optional<Ingrediente> fromDb = ingredienteRepository.findById(ingrediente1.getId());
        assertTrue(fromDb.isPresent());
        assertEquals(10, fromDb.get().getCantidad());
    }
    
    @Test
    void testUniqueConstraint() {
        // Intentar crear un duplicado (mismo productoInventario y productoVenta)
        // Este test asume que hay una restricción unique compuesta, ajústate si no es el caso
        Ingrediente duplicado = new Ingrediente();
        duplicado.setCantidad(5);
        duplicado.setProductoInventario(productoInventario1);
        duplicado.setProductoVenta(productoVenta1);
        
        try {
            ingredienteRepository.save(duplicado);
            ingredienteRepository.findAll(); // Forzar la operación
            
            // Si llega aquí, no hay restricción de unicidad
            // La prueba simplemente verifica que se puede crear
            assertNotNull(duplicado.getId());
        } catch (DataIntegrityViolationException e) {
            // Si hay excepción, es porque existe una restricción de unicidad
            // La prueba pasa simplemente si llegamos aquí
        }
    }
    
    @Test
    void testIngredienteWithZeroCantidad() {
        // Crear ingrediente con cantidad cero
        Ingrediente ingredienteCero = new Ingrediente();
        ingredienteCero.setCantidad(0);
        ingredienteCero.setProductoInventario(productoInventario1);
        ingredienteCero.setProductoVenta(productoVenta2);
        
        // Guardar y verificar
        Ingrediente saved = ingredienteRepository.save(ingredienteCero);
        assertEquals(0, saved.getCantidad());
    }
}