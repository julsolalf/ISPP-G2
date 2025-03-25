package ispp_g2.gastrostock.testProveedores;

import static org.junit.jupiter.api.Assertions.*;

import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import ispp_g2.gastrostock.proveedores.Proveedor;
import ispp_g2.gastrostock.proveedores.ProveedorRepository;
import ispp_g2.gastrostock.diaReparto.DiaReparto;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioRepository;
import ispp_g2.gastrostock.dueño.Dueño;
import ispp_g2.gastrostock.dueño.DueñoRepository;

@DataJpaTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class ProveedorRepositoryTest {

    @Autowired
    private ProveedorRepository proveedorRepository;
    
    
    @Autowired
    private NegocioRepository negocioRepository;
    
    @Autowired
    private DueñoRepository dueñoRepository;
    
    private Proveedor proveedor1, proveedor2, proveedor3;
    private DiaReparto diaLunes, diaMartes, diaMiercoles, diaViernes;
    private Negocio negocio;
    private Dueño dueño;
    
    @BeforeEach
    void setUp() {
        // Limpiar los datos existentes
        proveedorRepository.deleteAll();
        negocioRepository.deleteAll();
        dueñoRepository.deleteAll();
        
        // Crear un dueño
        dueño = new Dueño();
        dueño.setFirstName("Juan");
        dueño.setLastName("García");
        dueño.setEmail("juan@example.com");
        dueño.setNumTelefono("652345678");
        dueño.setTokenDueño("TOKEN123");
        dueño = dueñoRepository.save(dueño);
        
        // Crear un negocio
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
        proveedor1.setEmail("distribuciones@example.com");
        proveedor1.setTelefono("954111222");
        proveedor1.setDireccion("Polígono Industrial, Nave 7");
        Set<DiaReparto> diasReparto1 = new HashSet<>();
        diasReparto1.add(diaLunes);
        diasReparto1.add(diaMiercoles);
        proveedor1 = proveedorRepository.save(proveedor1);
        
        proveedor2 = new Proveedor();
        proveedor2.setName("Productos Frescos del Sur");
        proveedor2.setEmail("frescos@example.com");
        proveedor2.setTelefono("954333444");
        proveedor2.setDireccion("Avenida de la Industria, 42");
        Set<DiaReparto> diasReparto2 = new HashSet<>();
        diasReparto2.add(diaMartes);
        diasReparto2.add(diaViernes);
        proveedor2 = proveedorRepository.save(proveedor2);
        
        // Proveedor con casos especiales
        proveedor3 = new Proveedor();
        proveedor3.setName("Distribuciones Rápidas");
        proveedor3.setEmail("rapidas@example.com");
        proveedor3.setTelefono("954555666");
        proveedor3.setDireccion("Calle Comercio, 15");
        proveedor3 = proveedorRepository.save(proveedor3);
    }
    
    // PRUEBAS PARA MÉTODOS BÁSICOS CRUD
    
    @Test
    void testSave() {
        // Crear un nuevo proveedor
        Proveedor nuevoProveedor = new Proveedor();
        nuevoProveedor.setName("Nuevo Proveedor S.A.");
        nuevoProveedor.setEmail("nuevo@example.com");
        nuevoProveedor.setTelefono("954777888");
        nuevoProveedor.setDireccion("Calle Nueva, 123");
        // Guardar
        Proveedor saved = proveedorRepository.save(nuevoProveedor);
        
        // Verificar que tiene ID asignado
        assertNotNull(saved.getId());
        
        // Verificar que se guardó correctamente
        Optional<Proveedor> found = proveedorRepository.findById(Integer.toString(saved.getId()));
        assertTrue(found.isPresent());
        assertEquals("Nuevo Proveedor S.A.", found.get().getName());
    }
    
    @Test
    void testFindById() {
        // Buscar un proveedor existente
        Optional<Proveedor> found = proveedorRepository.findById(Integer.toString(proveedor1.getId()));
        
        // Verificar que se encontró y los datos son correctos
        assertTrue(found.isPresent());
        assertEquals("Distribuciones Alimentarias S.L.", found.get().getName());
        assertEquals("954111222", found.get().getTelefono());
    }
    
    @Test
    void testFindById_NotFound() {
        // Buscar un proveedor con ID inexistente
        Optional<Proveedor> notFound = proveedorRepository.findById("999");
        
        // Verificar que no se encuentra
        assertFalse(notFound.isPresent());
    }
    
    @Test
    void testFindAll() {
        // Recuperar todos los proveedores
        List<Proveedor> proveedores = (List<Proveedor>) proveedorRepository.findAll();
        
        // Verificar que hay 3 proveedores (los creados en setUp)
        assertEquals(3, proveedores.size());
        
        // Verificar que están los proveedores esperados
        boolean encontrado1 = false;
        boolean encontrado2 = false;
        boolean encontrado3 = false;
        
        for (Proveedor p : proveedores) {
            if (p.getId().equals(proveedor1.getId())) encontrado1 = true;
            if (p.getId().equals(proveedor2.getId())) encontrado2 = true;
            if (p.getId().equals(proveedor3.getId())) encontrado3 = true;
        }
        
        assertTrue(encontrado1);
        assertTrue(encontrado2);
        assertTrue(encontrado3);
    }
    
    @Test
    void testDelete() {
        // Eliminar un proveedor
        proveedorRepository.delete(proveedor3);
        
        // Verificar que ya no existe
        Optional<Proveedor> shouldBeDeleted = proveedorRepository.findById(Integer.toString(proveedor3.getId()));
        assertFalse(shouldBeDeleted.isPresent());
        
        // Verificar que solo quedan 2 proveedores
        assertEquals(2, proveedorRepository.findAll());
    }
    
    @Test
    void testDeleteById() {
        // Eliminar un proveedor por ID
        proveedorRepository.deleteById(Integer.toString(proveedor2.getId()));
        
        // Verificar que ya no existe
        Optional<Proveedor> shouldBeDeleted = proveedorRepository.findById(Integer.toString(proveedor2.getId()));
        assertFalse(shouldBeDeleted.isPresent());
        
        // Verificar que solo quedan 2 proveedores
        assertEquals(2, proveedorRepository.findAll());
    }
    
    // PRUEBAS PARA MÉTODOS PERSONALIZADOS
    /* 
    @Test
    void testFindByFirstNameContainingIgnoreCase() {
        // Buscar proveedores que contengan "Dist" en el nombre (ignorando mayúsculas/minúsculas)
        List<Proveedor> proveedores = proveedorRepository.findByFirstNameContainingIgnoreCase("dist");
        
        // Verificar que se encontraron los dos proveedores con "Distribuciones" en el nombre
        assertEquals(2, proveedores.size());
        boolean encontrado1 = false;
        boolean encontrado3 = false;
        
        for (Proveedor p : proveedores) {
            if (p.getId().equals(proveedor1.getId())) encontrado1 = true;
            if (p.getId().equals(proveedor3.getId())) encontrado3 = true;
        }
        
        assertTrue(encontrado1);
        assertTrue(encontrado3);
    }
    
    @Test
    void testFindByFirstNameContainingIgnoreCase_NotFound() {
        // Buscar proveedores con un término que no existe
        List<Proveedor> proveedores = proveedorRepository.findByFirstNameContainingIgnoreCase("xyz");
        
        // Verificar que no se encuentra ninguno
        assertTrue(proveedores.isEmpty());
    }
    
    @Test
    void testFindByFirstNameContainingIgnoreCase_CaseInsensitive() {
        // Buscar con diferentes casos
        List<Proveedor> result1 = proveedorRepository.findByFirstNameContainingIgnoreCase("DISTRIBUCIONES");
        List<Proveedor> result2 = proveedorRepository.findByFirstNameContainingIgnoreCase("distribuciones");
        List<Proveedor> result3 = proveedorRepository.findByFirstNameContainingIgnoreCase("DiStRiBuCiOnEs");
        
        // Verificar que todas las búsquedas dan el mismo resultado
        assertEquals(2, result1.size());
        assertEquals(2, result2.size());
        assertEquals(2, result3.size());
    }
    
    @Test
    void testFindByDiasRepartoContaining_Lunes() {
        // Buscar proveedores que repartan el lunes
        List<Proveedor> proveedores = proveedorRepository.findByDiasRepartoContaining(DayOfWeek.MONDAY);
        
        // Verificar que se encuentra el proveedor1
        assertEquals(1, proveedores.size());
        assertEquals(proveedor1.getId(), proveedores.get(0).getId());
    }
    
    @Test
    void testFindByDiasRepartoContaining_Martes() {
        // Buscar proveedores que repartan el martes
        List<Proveedor> proveedores = proveedorRepository.findByDiasRepartoContaining(DayOfWeek.TUESDAY);
        
        // Verificar que se encuentra el proveedor2
        assertEquals(1, proveedores.size());
        assertEquals(proveedor2.getId(), proveedores.get(0).getId());
    }
    
    @Test
    void testFindByDiasRepartoContaining_NotFound() {
        // Buscar proveedores que repartan el domingo (ninguno)
        List<Proveedor> proveedores = proveedorRepository.findByDiasRepartoContaining(DayOfWeek.SUNDAY);
        
        // Verificar que no se encuentra ninguno
        assertTrue(proveedores.isEmpty());
    }
*/
}
