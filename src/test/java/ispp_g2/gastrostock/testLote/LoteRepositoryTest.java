package ispp_g2.gastrostock.testLote;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import ispp_g2.gastrostock.lote.Lote;
import ispp_g2.gastrostock.lote.LoteRepository;
import ispp_g2.gastrostock.productoInventario.ProductoInventario;
import ispp_g2.gastrostock.productoInventario.ProductoInventarioRepository;
import ispp_g2.gastrostock.reabastecimiento.Reabastecimiento;
import ispp_g2.gastrostock.reabastecimiento.ReabastecimientoRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioRepository;
import ispp_g2.gastrostock.categorias.Categoria;
import ispp_g2.gastrostock.categorias.CategoriaRepository;
import ispp_g2.gastrostock.dueño.Dueño;
import ispp_g2.gastrostock.dueño.DueñoRepository;
import ispp_g2.gastrostock.proveedores.Proveedor;
import ispp_g2.gastrostock.proveedores.ProveedorRepository;

@DataJpaTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class LoteRepositoryTest {

    @Autowired
    private LoteRepository loteRepository;
    
    @Autowired
    private ProductoInventarioRepository productoInventarioRepository;
    
    @Autowired
    private ReabastecimientoRepository reabastecimientoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Autowired
    private NegocioRepository negocioRepository;
    
    @Autowired
    private DueñoRepository dueñoRepository;
    
    @Autowired
    private ProveedorRepository proveedorRepository;

    private jakarta.validation.Validator validator;

    
    private Lote lote1, lote2, lote3;
    private ProductoInventario producto1, producto2;
    private Reabastecimiento reabastecimiento1, reabastecimiento2;
    private Negocio negocio;
    private Dueño dueño;
    private Proveedor proveedor1, proveedor2;
    private Categoria categoria;
    
    @BeforeEach
    void setUp() {
        // Limpiar repositorios
        loteRepository.deleteAll();
        reabastecimientoRepository.deleteAll();
        productoInventarioRepository.deleteAll();
        proveedorRepository.deleteAll();
        negocioRepository.deleteAll();
        dueñoRepository.deleteAll();
        categoriaRepository.deleteAll();
        
        // Crear dueño
        dueño = new Dueño();
        dueño.setFirstName("Juan");
        dueño.setLastName("García");
        dueño.setEmail("juan@example.com");
        dueño.setNumTelefono("652345678");
        dueño.setTokenDueño("TOKEN123");
        dueño = dueñoRepository.save(dueño);
        
        // Crear negocio
        negocio = new Negocio();
        negocio.setName("Restaurante La Tasca");
        negocio.setDireccion("Calle Principal 123");
        negocio.setCiudad("Sevilla");
        negocio.setPais("España");
        negocio.setCodigoPostal("41001");
        negocio.setTokenNegocio(12345);
        negocio.setDueño(dueño);
        negocio = negocioRepository.save(negocio);
        
        // Crear proveedores
        proveedor1 = new Proveedor();
        proveedor1.setName("Distribuciones Alimentarias S.L.");
        proveedor1.setEmail("distri@example.com");
        proveedor1.setTelefono("954111222");
        proveedor1.setDireccion("Polígono Industrial, Nave 7");
        proveedor1 = proveedorRepository.save(proveedor1);
        
        proveedor2 = new Proveedor();
        proveedor2.setName("Productos Frescos del Sur");
        proveedor2.setEmail("frescos@example.com");
        proveedor2.setTelefono("954333444");
        proveedor2.setDireccion("Avenida de la Industria, 42");
        proveedor2 = proveedorRepository.save(proveedor2);
    
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    
        // Crear categorías
        categoria = new Categoria();
        categoria.setName("Harinas");
        categoria.setNegocio(negocio); // Asignar negocio a categoría
        categoria = categoriaRepository.save(categoria);
        
        Categoria categoriaBebidas = new Categoria();
        categoriaBebidas.setName("Bebidas");
        categoriaBebidas.setNegocio(negocio);
        categoriaBebidas = categoriaRepository.save(categoriaBebidas);
        
        // Crear productos de inventario
        producto1 = new ProductoInventario();
        producto1.setName("Harina");
        producto1.setCategoria(categoria); // Asignar categoría al producto
        producto1.setPrecioCompra(2.5);
        producto1.setCantidadAviso(5);
        producto1.setCantidadDeseada(10);
        producto1 = productoInventarioRepository.save(producto1);
        
        producto2 = new ProductoInventario();
        producto2.setName("Azúcar");
        producto2.setCategoria(categoria);
        producto2.setPrecioCompra(1.8);
        producto2.setCantidadAviso(3);
        producto2.setCantidadDeseada(8);
        producto2 = productoInventarioRepository.save(producto2);
        
        // Crear productos adicionales para pruebas más exhaustivas
        ProductoInventario producto3 = new ProductoInventario();
        producto3.setName("Refrescos");
        producto3.setCategoria(categoriaBebidas);
        producto3.setPrecioCompra(0.8);
        producto3.setCantidadAviso(10);
        producto3.setCantidadDeseada(50);
        productoInventarioRepository.save(producto3);
        
        // Crear reabastecimientos
        reabastecimiento1 = new Reabastecimiento();
        reabastecimiento1.setReferencia("REF001");
        reabastecimiento1.setPrecioTotal(100.0);
        reabastecimiento1.setFecha(LocalDate.now().minusDays(15));
        reabastecimiento1.setProveedor(proveedor1);
        reabastecimiento1.setNegocio(negocio);
        reabastecimiento1 = reabastecimientoRepository.save(reabastecimiento1);
        
        reabastecimiento2 = new Reabastecimiento();
        reabastecimiento2.setReferencia("REF002");
        reabastecimiento2.setPrecioTotal(150.0);
        reabastecimiento2.setFecha(LocalDate.now().minusDays(7));
        reabastecimiento2.setProveedor(proveedor2);
        reabastecimiento2.setNegocio(negocio);
        reabastecimiento2 = reabastecimientoRepository.save(reabastecimiento2);
        
        // Crear reabastecimiento adicional para pruebas exhaustivas
        Reabastecimiento reabastecimiento3 = new Reabastecimiento();
        reabastecimiento3.setReferencia("REF003");
        reabastecimiento3.setPrecioTotal(75.5);
        reabastecimiento3.setFecha(LocalDate.now().minusDays(2));
        reabastecimiento3.setProveedor(proveedor1);
        reabastecimiento3.setNegocio(negocio);
        reabastecimientoRepository.save(reabastecimiento3);
        
        // Crear lotes para las pruebas
        lote1 = new Lote();
        lote1.setCantidad(100);
        lote1.setFechaCaducidad(LocalDate.now().plusMonths(3));
        lote1.setProducto(producto1);
        lote1.setReabastecimiento(reabastecimiento1);
        lote1 = loteRepository.save(lote1);
        
        lote2 = new Lote();
        lote2.setCantidad(50);
        lote2.setFechaCaducidad(LocalDate.now().plusMonths(6));
        lote2.setProducto(producto2);
        lote2.setReabastecimiento(reabastecimiento1);
        lote2 = loteRepository.save(lote2);
        
        lote3 = new Lote();
        lote3.setCantidad(1); // Caso extremo: lote con cantidad 1
        lote3.setFechaCaducidad(LocalDate.now().minusDays(1)); // Caso extremo: lote caducado
        lote3.setProducto(producto1);
        lote3.setReabastecimiento(reabastecimiento2);
        lote3 = loteRepository.save(lote3);
    }
    
    // Test métodos CRUD estándar
    
    @Test
    void testSave() {
        // Crear un nuevo lote
        Lote nuevoLote = new Lote();
        nuevoLote.setCantidad(75);
        nuevoLote.setFechaCaducidad(LocalDate.now().plusMonths(5));
        nuevoLote.setProducto(producto2);
        nuevoLote.setReabastecimiento(reabastecimiento2);
        
        // Guardar el lote
        Lote saved = loteRepository.save(nuevoLote);
        
        // Verificar que se guardó correctamente
        assertNotNull(saved.getId());
        assertEquals(75, saved.getCantidad());
        
        // Verificar que se puede recuperar de la base de datos
        Lote retrieved = loteRepository.findById(saved.getId().toString()).orElse(null);
        assertNotNull(retrieved);
        assertEquals(75, retrieved.getCantidad());
        assertEquals(LocalDate.now().plusMonths(5), retrieved.getFechaCaducidad());
    }
    
    @Test
    void testFindById() {
        // Buscar un lote existente por ID
        Optional<Lote> found = loteRepository.findById(lote1.getId().toString());
        
        // Verificar que existe y tiene los datos correctos
        assertTrue(found.isPresent());
        assertEquals(100, found.get().getCantidad());
        assertEquals(producto1.getId(), found.get().getProducto().getId());
    }
    
    @Test
    void testFindById_NotFound() {
        // Buscar un lote que no existe
        Optional<Lote> notFound = loteRepository.findById("999");
        
        // Verificar que no existe
        assertFalse(notFound.isPresent());
    }
    
    @Test
    void testFindAll() {
        // Obtener todos los lotes
        Iterable<Lote> lotes = loteRepository.findAll();
        
        // Convertir a lista para facilitar la verificación
        List<Lote> lotesList = (List<Lote>) lotes;
        
        // Verificar que hay 3 lotes (los creados en setUp)
        assertEquals(3, lotesList.size());
    }
    
    @Test
    void testDelete() {
        // Eliminar un lote
        loteRepository.delete(lote1);
        
        // Verificar que se eliminó
        Optional<Lote> shouldBeDeleted = loteRepository.findById(lote1.getId().toString());
        assertFalse(shouldBeDeleted.isPresent());
        
        // Verificar que sólo quedan 2 lotes
        assertEquals(2, ((List<Lote>) loteRepository.findAll()).size());
    }
    
    @Test
    void testDeleteById() {
        // Eliminar un lote por ID
        loteRepository.deleteById(lote2.getId().toString());
        
        // Verificar que se eliminó
        Optional<Lote> shouldBeDeleted = loteRepository.findById(lote2.getId().toString());
        assertFalse(shouldBeDeleted.isPresent());
        
        // Verificar que sólo quedan 2 lotes
        assertEquals(2, ((List<Lote>) loteRepository.findAll()).size());
    }
    
    // Test métodos de consulta personalizados
    
    @Test
    void testFindByCantidad() {
        // Buscar lotes por cantidad
        List<Lote> lotes = loteRepository.findByCantidad(100);
        
        // Verificar que se encontró 1 lote con cantidad = 100
        assertEquals(1, lotes.size());
        assertEquals(lote1.getId(), lotes.get(0).getId());
    }
    
    @Test
    void testFindByCantidad_One() {
        // Buscar lotes con cantidad cero
        List<Lote> lotes = loteRepository.findByCantidad(1);
        
        // Verificar que se encontró 1 lote con cantidad = 0
        assertEquals(1, lotes.size());
        assertEquals(lote3.getId(), lotes.get(0).getId());
    }
    
    @Test
    void testFindByCantidad_NotFound() {
        // Buscar lotes con una cantidad que no existe
        List<Lote> lotes = loteRepository.findByCantidad(999);
        
        // Verificar que no se encontraron lotes
        assertTrue(lotes.isEmpty());
    }
    
    @Test
    void testFindByFechaCaducidad() {
        // Obtener fecha de caducidad del lote1
        LocalDate fechaCaducidad = lote1.getFechaCaducidad();
        
        // Buscar lotes por fecha de caducidad
        List<Lote> lotes = loteRepository.findByFechaCaducidad(fechaCaducidad);
        
        // Verificar que se encontró 1 lote con la fecha de caducidad correcta
        assertEquals(1, lotes.size());
        assertEquals(lote1.getId(), lotes.get(0).getId());
    }
    
    @Test
    void testFindByFechaCaducidad_PastDate() {
        // Obtener fecha de caducidad del lote3 (fecha pasada)
        LocalDate fechaCaducidad = lote3.getFechaCaducidad();
        
        // Buscar lotes por fecha de caducidad pasada
        List<Lote> lotes = loteRepository.findByFechaCaducidad(fechaCaducidad);
        
        // Verificar que se encontró 1 lote con la fecha de caducidad pasada
        assertEquals(1, lotes.size());
        assertEquals(lote3.getId(), lotes.get(0).getId());
    }
    
    @Test
    void testFindByFechaCaducidad_NotFound() {
        // Buscar lotes con una fecha de caducidad que no existe
        LocalDate fechaNoExistente = LocalDate.now().plusYears(10);
        List<Lote> lotes = loteRepository.findByFechaCaducidad(fechaNoExistente);
        
        // Verificar que no se encontraron lotes
        assertTrue(lotes.isEmpty());
    }
    
    @Test
    void testFindByProductoId() {
        // Buscar lotes por ID de producto
        List<Lote> lotes = loteRepository.findByProductoId(producto1.getId());
        
        // Verificar que se encontraron 2 lotes con el producto1
        assertEquals(2, lotes.size());
        assertTrue(lotes.stream().allMatch(l -> l.getProducto().getId().equals(producto1.getId())));
    }
    
    @Test
    void testFindByProductoId_NotFound() {
        // Buscar lotes con un ID de producto que no existe
        List<Lote> lotes = loteRepository.findByProductoId(999);
        
        // Verificar que no se encontraron lotes
        assertTrue(lotes.isEmpty());
    }
    
    @Test
    void testFindByReabastecimientoId() {
        // Buscar lotes por ID de reabastecimiento
        List<Lote> lotes = loteRepository.findByReabastecimientoId(reabastecimiento1.getId());
        
        // Verificar que se encontraron 2 lotes con el reabastecimiento1
        assertEquals(2, lotes.size());
        assertTrue(lotes.stream().allMatch(l -> l.getReabastecimiento().getId().equals(reabastecimiento1.getId())));
    }
    
    @Test
    void testFindByReabastecimientoId_NotFound() {
        // Buscar lotes con un ID de reabastecimiento que no existe
        List<Lote> lotes = loteRepository.findByReabastecimientoId(999);
        
        // Verificar que no se encontraron lotes
        assertTrue(lotes.isEmpty());
    }
    
    // Tests adicionales: escenarios complejos y bordes
    
    @Test
    void testSaveWithNullFechaCaducidad() {
        // Crear un lote con fecha de caducidad nula
        Lote loteInvalido = new Lote();
        loteInvalido.setCantidad(100);
        loteInvalido.setProducto(producto1);
        loteInvalido.setReabastecimiento(reabastecimiento1);
        
        // Validar manualmente
        Set<ConstraintViolation<Lote>> violations = validator.validate(loteInvalido);
        
        // Verificar que hay violaciones
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("fechaCaducidad")));
    }
    @Test
    void testSaveWithNegativeCantidad() {
        // Crear un lote con cantidad negativa (debería fallar por la restricción @Positive)
        Lote loteInvalido = new Lote();
        loteInvalido.setCantidad(-10);
        loteInvalido.setFechaCaducidad(LocalDate.now().plusMonths(1));
        loteInvalido.setProducto(producto1);
        loteInvalido.setReabastecimiento(reabastecimiento1);
        
        // Validar manualmente
        Set<ConstraintViolation<Lote>> violations = validator.validate(loteInvalido);
        
        // Verificar que hay violaciones
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("cantidad")));
    }

}