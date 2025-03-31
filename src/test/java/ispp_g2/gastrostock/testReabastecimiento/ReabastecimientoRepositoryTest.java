package ispp_g2.gastrostock.testReabastecimiento;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import ispp_g2.gastrostock.dueno.Dueno;
import ispp_g2.gastrostock.dueno.DuenoRepository;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioRepository;
import ispp_g2.gastrostock.proveedores.Proveedor;
import ispp_g2.gastrostock.proveedores.ProveedorRepository;
import ispp_g2.gastrostock.reabastecimiento.Reabastecimiento;
import ispp_g2.gastrostock.reabastecimiento.ReabastecimientoRepository;

@DataJpaTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class ReabastecimientoRepositoryTest {

    @Autowired
    private ReabastecimientoRepository reabastecimientoRepository;
    
    @Autowired
    private ProveedorRepository proveedorRepository;
    
    @Autowired
    private NegocioRepository negocioRepository;
    
    @Autowired
    private DuenoRepository duenoRepository;
    
    private Proveedor proveedor1, proveedor2;
    private Negocio negocio1, negocio2;
    private Dueno dueno1, dueno2;
    private Reabastecimiento reabastecimiento1, reabastecimiento2, reabastecimiento3;
    private LocalDate fecha1, fecha2, fecha3;
    
    @BeforeEach
    void setUp() {
        // Limpiar repositorios
        reabastecimientoRepository.deleteAll();
        proveedorRepository.deleteAll();
        negocioRepository.deleteAll();
        duenoRepository.deleteAll();
        
        // Crear duenos
        dueno1 = new Dueno();
        dueno1.setFirstName("Juan");
        dueno1.setLastName("García");
        dueno1.setEmail("juan@example.com");
        dueno1.setNumTelefono("652345678");
        dueno1.setTokenDueno("TOKEN123");
        dueno1 = duenoRepository.save(dueno1);
        
        dueno2 = new Dueno();
        dueno2.setFirstName("María");
        dueno2.setLastName("López");
        dueno2.setEmail("maria@example.com");
        dueno2.setNumTelefono("652345679");
        dueno2.setTokenDueno("TOKEN456");
        dueno2 = duenoRepository.save(dueno2);
        
        // Crear negocios
        negocio1 = new Negocio();
        negocio1.setName("Restaurante La Tasca");
        negocio1.setDireccion("Calle Principal 123");
        negocio1.setCiudad("Sevilla");
        negocio1.setPais("Espana");
        negocio1.setCodigoPostal("41001");
        negocio1.setTokenNegocio(12345);
        negocio1.setDueno(dueno1);
        negocio1 = negocioRepository.save(negocio1);
        
        negocio2 = new Negocio();
        negocio2.setName("Bar El Rincón");
        negocio2.setDireccion("Plaza Mayor 10");
        negocio2.setCiudad("Madrid");
        negocio2.setPais("Espana");
        negocio2.setCodigoPostal("28001");
        negocio2.setTokenNegocio(67890);
        negocio2.setDueno(dueno2);
        negocio2 = negocioRepository.save(negocio2);
        
        // Crear proveedores
        proveedor1 = new Proveedor();
        proveedor1.setName("Distribuciones Alimentarias S.L.");
        proveedor1.setEmail("distribuciones@example.com");
        proveedor1.setTelefono("954111222");
        proveedor1.setDireccion("Polígono Industrial, Nave 7");
        proveedor1 = proveedorRepository.save(proveedor1);
        
        proveedor2 = new Proveedor();
        proveedor2.setName("Bebidas del Sur");
        proveedor2.setEmail("bebidas@example.com");
        proveedor2.setTelefono("954333444");
        proveedor2.setDireccion("Carretera de Málaga km 5");
        proveedor2 = proveedorRepository.save(proveedor2);
        
        // Preparar fechas
        fecha1 = LocalDate.of(2023, 3, 15);
        fecha2 = LocalDate.of(2023, 4, 10);
        fecha3 = LocalDate.of(2023, 5, 5);
        
        // Crear reabastecimientos
        reabastecimiento1 = new Reabastecimiento();
        reabastecimiento1.setFecha(fecha1);
        reabastecimiento1.setPrecioTotal(1250.75);
        reabastecimiento1.setReferencia("REF-001");
        reabastecimiento1.setProveedor(proveedor1);
        reabastecimiento1.setNegocio(negocio1);
        reabastecimiento1 = reabastecimientoRepository.save(reabastecimiento1);
        
        reabastecimiento2 = new Reabastecimiento();
        reabastecimiento2.setFecha(fecha2);
        reabastecimiento2.setPrecioTotal(875.30);
        reabastecimiento2.setReferencia("REF-002");
        reabastecimiento2.setProveedor(proveedor1);
        reabastecimiento2.setNegocio(negocio1);
        reabastecimiento2 = reabastecimientoRepository.save(reabastecimiento2);
        
        reabastecimiento3 = new Reabastecimiento();
        reabastecimiento3.setFecha(fecha3);
        reabastecimiento3.setPrecioTotal(500.00);
        reabastecimiento3.setReferencia("REF-003");
        reabastecimiento3.setProveedor(proveedor2);
        reabastecimiento3.setNegocio(negocio2);
        reabastecimiento3 = reabastecimientoRepository.save(reabastecimiento3);
    }
    
    // --- TESTS PARA OPERACIONES BÁSICAS CRUD ---
    
    @Test
    void testSave() {
        // Crear un nuevo reabastecimiento
        Reabastecimiento nuevoReabastecimiento = new Reabastecimiento();
        nuevoReabastecimiento.setFecha(LocalDate.now());
        nuevoReabastecimiento.setPrecioTotal(300.00);
        nuevoReabastecimiento.setReferencia("REF-NEW");
        nuevoReabastecimiento.setProveedor(proveedor1);
        nuevoReabastecimiento.setNegocio(negocio1);
        
        // Guardar y verificar que se asignó un ID
        Reabastecimiento savedReabastecimiento = reabastecimientoRepository.save(nuevoReabastecimiento);
        assertNotNull(savedReabastecimiento.getId());
        
        // Recuperar por ID y verificar todos los campos
        Optional<Reabastecimiento> retrieved = reabastecimientoRepository.findById(savedReabastecimiento.getId());
        assertTrue(retrieved.isPresent());
        assertEquals("REF-NEW", retrieved.get().getReferencia());
        assertEquals(300.00, retrieved.get().getPrecioTotal());
    }
    
    @Test
    void testFindById() {
        // Buscar un reabastecimiento existente
        Optional<Reabastecimiento> found = reabastecimientoRepository.findById(reabastecimiento1.getId());
        
        // Verificar que existe y que los datos son correctos
        assertTrue(found.isPresent());
        assertEquals("REF-001", found.get().getReferencia());
        assertEquals(1250.75, found.get().getPrecioTotal());
    }
    
    @Test
    void testFindById_NotFound() {
        // Buscar un ID que no existe
        Optional<Reabastecimiento> notFound = reabastecimientoRepository.findById(999);
        
        // Verificar que no existe
        assertFalse(notFound.isPresent());
    }
    
    @Test
    void testFindAll() {
        // Recuperar todos los reabastecimientos
        Iterable<Reabastecimiento> all = reabastecimientoRepository.findAll();
        
        // Convertir a lista y verificar el tamano
        List<Reabastecimiento> allList = (List<Reabastecimiento>) all;
        assertEquals(3, allList.size());
    }
    
    @Test
    void testDelete() {
        // Verificar que hay 3 reabastecimientos inicialmente
        assertEquals(3, ((List<Reabastecimiento>) reabastecimientoRepository.findAll()).size());
        
        // Eliminar uno
        reabastecimientoRepository.delete(reabastecimiento1);
        
        // Verificar que ahora hay 2
        assertEquals(2, ((List<Reabastecimiento>) reabastecimientoRepository.findAll()).size());
        
        // Verificar que el eliminado ya no existe
        Optional<Reabastecimiento> deletedReabastecimiento = reabastecimientoRepository.findById(reabastecimiento1.getId());
        assertFalse(deletedReabastecimiento.isPresent());
    }
    
    @Test
    void testDeleteById() {
        // Eliminar por ID
        reabastecimientoRepository.deleteById(reabastecimiento2.getId());
        
        // Verificar que ya no existe
        Optional<Reabastecimiento> deletedReabastecimiento = reabastecimientoRepository.findById(reabastecimiento2.getId());
        assertFalse(deletedReabastecimiento.isPresent());
    }
    
    // --- TESTS PARA MÉTODOS PERSONALIZADOS DE BÚSQUEDA ---
    
    @Test
    void testFindByFecha() {
        // Buscar por una fecha existente
        List<Reabastecimiento> result = reabastecimientoRepository.findByFecha(fecha1);
        
        // Verificar que devuelve el reabastecimiento correcto
        assertEquals(1, result.size());
        assertEquals("REF-001", result.get(0).getReferencia());
    }
    
    @Test
    void testFindByFecha_NotExists() {
        // Buscar por una fecha que no existe
        LocalDate nonExistentDate = LocalDate.of(2021, 1, 1);
        List<Reabastecimiento> result = reabastecimientoRepository.findByFecha(nonExistentDate);
        
        // Verificar que no encuentra nada
        assertTrue(result.isEmpty());
    }
    
    @Test
    void testFindByFechaBetween() {
        // Buscar en un rango que incluye 2 fechas
        LocalDate start = LocalDate.of(2023, 3, 1);
        LocalDate end = LocalDate.of(2023, 4, 30);
        List<Reabastecimiento> result = reabastecimientoRepository.findByFechaBetween(start, end);
        
        // Verificar que encuentra los 2 reabastecimientos esperados
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(r -> r.getReferencia().equals("REF-001")));
        assertTrue(result.stream().anyMatch(r -> r.getReferencia().equals("REF-002")));
    }
    
    @Test
    void testFindByFechaBetween_NoResults() {
        // Buscar en un rango sin reabastecimientos
        LocalDate start = LocalDate.of(2022, 1, 1);
        LocalDate end = LocalDate.of(2022, 12, 31);
        List<Reabastecimiento> result = reabastecimientoRepository.findByFechaBetween(start, end);
        
        // Verificar que no encuentra nada
        assertTrue(result.isEmpty());
    }
    
    @Test
    void testFindByFechaBetween_SameDate() {
        // Buscar con fecha inicio = fecha fin
        List<Reabastecimiento> result = reabastecimientoRepository.findByFechaBetween(fecha1, fecha1);
        
        // Verificar que encuentra el reabastecimiento de esa fecha
        assertEquals(1, result.size());
        assertEquals("REF-001", result.get(0).getReferencia());
    }
    
    @Test
    void testFindByFechaBetween_InvertedDates() {
        // Buscar con fecha fin anterior a fecha inicio
        LocalDate start = LocalDate.of(2023, 5, 1);
        LocalDate end = LocalDate.of(2023, 3, 1);
        List<Reabastecimiento> result = reabastecimientoRepository.findByFechaBetween(start, end);
        
        // En la mayoría de bases de datos SQL, esto no devuelve resultados
        assertTrue(result.isEmpty());
    }
    
    @Test
    void testFindByPrecioTotal() {
        // Buscar por precio exacto
        List<Reabastecimiento> result = reabastecimientoRepository.findByPrecioTotal(1250.75);
        
        // Verificar que encuentra el reabastecimiento correcto
        assertEquals(1, result.size());
        assertEquals("REF-001", result.get(0).getReferencia());
    }
    
    @Test
    void testFindByPrecioTotal_NotExists() {
        // Buscar por un precio que no existe
        List<Reabastecimiento> result = reabastecimientoRepository.findByPrecioTotal(999.99);
        
        // Verificar que no encuentra nada
        assertTrue(result.isEmpty());
    }
    
    @Test
    void testFindByPrecioTotal_ZeroOne() {
        // Crear un reabastecimiento con precio 0
        Reabastecimiento zeroPriceReabastecimiento = new Reabastecimiento();
        zeroPriceReabastecimiento.setFecha(LocalDate.now());
        zeroPriceReabastecimiento.setPrecioTotal(0.1);
        zeroPriceReabastecimiento.setReferencia("REF-ZERO");
        zeroPriceReabastecimiento.setProveedor(proveedor1);
        zeroPriceReabastecimiento.setNegocio(negocio1);
        reabastecimientoRepository.save(zeroPriceReabastecimiento);
        
        // Buscar por precio 0
        List<Reabastecimiento> result = reabastecimientoRepository.findByPrecioTotal(0.1);
        
        // Verificar que encuentra el reabastecimiento correcto
        assertEquals(1, result.size());
        assertEquals("REF-ZERO", result.get(0).getReferencia());
    }
    
    @Test
    void testFindByReferencia() {
        // Buscar por referencia existente
        List<Reabastecimiento> result = reabastecimientoRepository.findByReferencia("REF-002");
        
        // Verificar que encuentra el reabastecimiento correcto
        assertEquals(1, result.size());
        assertEquals(875.30, result.get(0).getPrecioTotal());
    }
    
    @Test
    void testFindByReferencia_NotExists() {
        // Buscar por una referencia que no existe
        List<Reabastecimiento> result = reabastecimientoRepository.findByReferencia("REF-NONEXISTENT");
        
        // Verificar que no encuentra nada
        assertTrue(result.isEmpty());
    }
    
    @Test
    void testFindByReferencia_CaseSensitive() {
        // Buscar con diferente caso (mayúsculas/minúsculas)
        List<Reabastecimiento> result = reabastecimientoRepository.findByReferencia("ref-001");
        
        // Por defecto, las consultas JPQL son case sensitive
        assertTrue(result.isEmpty());
    }
    
    @Test
    void testFindByProveedor() {
        // Buscar por ID del proveedor1
        List<Reabastecimiento> result = reabastecimientoRepository.findByProveedor(proveedor1.getId());
        
        // Verificar que encuentra los reabastecimientos del proveedor1
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(r -> r.getProveedor().getId().equals(proveedor1.getId())));
    }
    
    @Test
    void testFindByProveedor_NotExists() {
        // Buscar por un ID de proveedor que no existe
        List<Reabastecimiento> result = reabastecimientoRepository.findByProveedor(999);
        
        // Verificar que no encuentra nada
        assertTrue(result.isEmpty());
    }
    
    @Test
    void testFindByNegocio() {
        // Buscar por ID del negocio2
        List<Reabastecimiento> result = reabastecimientoRepository.findByNegocio(negocio2.getId());
        
        // Verificar que encuentra el reabastecimiento del negocio2
        assertEquals(1, result.size());
        assertEquals("REF-003", result.get(0).getReferencia());
    }
    
    @Test
    void testFindByNegocio_NotExists() {
        // Buscar por un ID de negocio que no existe
        List<Reabastecimiento> result = reabastecimientoRepository.findByNegocio(999);
        
        // Verificar que no encuentra nada
        assertTrue(result.isEmpty());
    }
    
    // --- TESTS ADICIONALES PARA CASOS EXTREMOS ---
    
    @Test
    void testFindByReferencia_NullReferencia() {
        // Intentar buscar con referencia null
        List<Reabastecimiento> result = reabastecimientoRepository.findByReferencia(null);
        
        // Deberíamos esperar una lista vacía (comportamiento típico de JPA)
        assertTrue(result.isEmpty());
    }
    
    @Test
    void testFindByProveedor_NullProveedor() {
        // Intentar buscar con proveedor null
        List<Reabastecimiento> result = reabastecimientoRepository.findByProveedor(null);
        
        // Deberíamos esperar una lista vacía (comportamiento típico de JPA)
        assertTrue(result.isEmpty());
    }
    
    @Test
    void testCascadeDeleteProveedor() {
        // Contar reabastecimientos del proveedor1
        int initialCount = reabastecimientoRepository.findByProveedor(proveedor1.getId()).size();
        assertEquals(2, initialCount);
        
        // Intentar eliminar el proveedor1
        try {
            proveedorRepository.delete(proveedor1);
            proveedorRepository.findAll();
            
            // Verificar que los reabastecimientos asociados también se eliminaron (o se comportan según la configuración de CASCADE)
            List<Reabastecimiento> remaining = reabastecimientoRepository.findByProveedor(proveedor1.getId());
            
            // Si llegamos aquí, es que el proveedor se eliminó. Verificamos el comportamiento esperado según la configuración:
            // - Si hay eliminación en cascada, no debería haber reabastecimientos
            // - Si no hay eliminación en cascada, debería fallar antes de llegar aquí con una excepción
            assertTrue(remaining.isEmpty(), "Deberían haberse eliminado los reabastecimientos asociados al proveedor");
            
        } catch (Exception e) {
            // Si falla con una excepción de integridad referencial, es el comportamiento esperado si no hay CASCADE DELETE
            // Lo importante es verificar que el comportamiento es consistente con la configuración de JPA
            assertTrue(e.getMessage().contains("constraint") || 
                       e.getMessage().contains("integrity") || 
                       e.getMessage().contains("foreign key"),
                      "La excepción debería estar relacionada con integridad referencial: " + e.getMessage());
        }
    }
    
    @Test
    void testUpdateReabastecimiento() {
        // Modificar campos de un reabastecimiento existente
        reabastecimiento1.setPrecioTotal(1500.00);
        reabastecimiento1.setReferencia("REF-001-UPDATED");
        
        // Guardar los cambios
        reabastecimientoRepository.save(reabastecimiento1);
        
        // Recuperar de nuevo y verificar los cambios
        Optional<Reabastecimiento> updated = reabastecimientoRepository.findById(reabastecimiento1.getId());
        assertTrue(updated.isPresent());
        assertEquals(1500.00, updated.get().getPrecioTotal());
        assertEquals("REF-001-UPDATED", updated.get().getReferencia());
    }
}