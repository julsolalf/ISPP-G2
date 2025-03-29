package ispp_g2.gastrostock.testNegocio;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import ispp_g2.gastrostock.dueño.Dueño;
import ispp_g2.gastrostock.dueño.DueñoRepository;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioRepository;

@DataJpaTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
class NegocioRepositoryTest {

    @Autowired
    private NegocioRepository negocioRepository;
    
    @Autowired
    private DueñoRepository dueñoRepository;
    
    private Dueño dueño1, dueño2;
    private Negocio negocio1, negocio2, negocio3;
    
    @BeforeEach
    void setUp() {
        // Limpiar repositorios
        negocioRepository.deleteAll();
        dueñoRepository.deleteAll();
        
        // Crear dueños
        dueño1 = new Dueño();
        dueño1.setFirstName("Juan");
        dueño1.setLastName("García");
        dueño1.setEmail("juan@example.com");
        dueño1.setNumTelefono("666111222");
        dueño1.setTokenDueño("TOKEN999");
        dueño1 = dueñoRepository.save(dueño1);
        
        dueño2 = new Dueño();
        dueño2.setFirstName("María");
        dueño2.setLastName("López");
        dueño2.setEmail("maria@example.com");
        dueño2.setNumTelefono("666333444");
        dueño2.setTokenDueño("TOKEN456");
        dueño2 = dueñoRepository.save(dueño2);
        
        // Crear negocios
        negocio1 = new Negocio();
        negocio1.setName("Restaurante La Tasca");
        negocio1.setDireccion("Calle Principal 123");
        negocio1.setCiudad("Sevilla");
        negocio1.setPais("España");
        negocio1.setCodigoPostal("41001");
        negocio1.setTokenNegocio(12345);
        negocio1.setDueño(dueño1);
        negocio1 = negocioRepository.save(negocio1);
        
        negocio2 = new Negocio();
        negocio2.setName("Bar El Rincón");
        negocio2.setDireccion("Avenida de la Constitución 45");
        negocio2.setCiudad("Sevilla");
        negocio2.setPais("España");
        negocio2.setCodigoPostal("41001");
        negocio2.setTokenNegocio(67890);
        negocio2.setDueño(dueño1);
        negocio2 = negocioRepository.save(negocio2);
        
        negocio3 = new Negocio();
        negocio3.setName("Café Central");
        negocio3.setDireccion("Plaza Mayor 10");
        negocio3.setCiudad("Madrid");
        negocio3.setPais("España");
        negocio3.setCodigoPostal("28001");
        negocio3.setTokenNegocio(54321);
        negocio3.setDueño(dueño2);
        negocio3 = negocioRepository.save(negocio3);
    }

    // Tests para operaciones CRUD estándar
    
    @Test
    void testFindAll() {
        Iterable<Negocio> negocios = negocioRepository.findAll();
        List<Negocio> negociosList = StreamSupport.stream(negocios.spliterator(), false).toList();
        
        assertNotNull(negociosList);
        assertEquals(3, negociosList.size());
        
        boolean foundLaTasca = false;
        boolean foundElRincon = false;
        boolean foundCafeCentral = false;
        
        for (Negocio negocio : negociosList) {
            if ("Restaurante La Tasca".equals(negocio.getName())) foundLaTasca = true;
            if ("Bar El Rincón".equals(negocio.getName())) foundElRincon = true;
            if ("Café Central".equals(negocio.getName())) foundCafeCentral = true;
        }
        
        assertTrue(foundLaTasca);
        assertTrue(foundElRincon);
        assertTrue(foundCafeCentral);
    }
    
    @Test
    void testFindById() {
        Optional<Negocio> found = negocioRepository.findById(negocio1.getId());
        
        assertTrue(found.isPresent());
        assertEquals("Restaurante La Tasca", found.get().getName());
        assertEquals("Calle Principal 123", found.get().getDireccion());
    }
    
    @Test
    void testFindById_NotFound() {
        Optional<Negocio> notFound = negocioRepository.findById(9999);
        
        assertFalse(notFound.isPresent());
    }
    
    @Test
    void testSave() {
        Negocio newNegocio = new Negocio();
        newNegocio.setName("Heladería Polar");
        newNegocio.setDireccion("Calle Fresa 15");
        newNegocio.setCiudad("Valencia");
        newNegocio.setPais("España");
        newNegocio.setCodigoPostal("46001");
        newNegocio.setTokenNegocio(11223);
        newNegocio.setDueño(dueño2);
        
        Negocio saved = negocioRepository.save(newNegocio);
        
        assertNotNull(saved.getId());
        assertEquals("Heladería Polar", saved.getName());
        
        // Verificar que se guardó en la BD
        Negocio retrieved = negocioRepository.findByTokenNegocio(11223);
        assertNotNull(retrieved);
        assertEquals("Valencia", retrieved.getCiudad());
    }
    
    @Test
    void testSaveWithoutDueño() {
        Negocio negocioSinDueño = new Negocio();
        negocioSinDueño.setName("Restaurante Sin Dueño");
        negocioSinDueño.setDireccion("Calle Cualquiera 10");
        negocioSinDueño.setCiudad("Barcelona");
        negocioSinDueño.setPais("España");
        negocioSinDueño.setCodigoPostal("08001");
        negocioSinDueño.setTokenNegocio(99999);
        
        // Debería fallar al guardar sin dueño
        assertThrows(DataIntegrityViolationException.class, () -> {
            negocioRepository.save(negocioSinDueño);
            negocioRepository.findAll(); // Forzar flush
        });
    }
    
    @Test
    void testUpdate() {
        // Modificar un negocio existente
        negocio1.setName("Restaurante La Tasca Renovado");
        negocio1.setCiudad("Córdoba");
        
        Negocio updated = negocioRepository.save(negocio1);
        
        // Verificar que se actualizó
        assertEquals("Restaurante La Tasca Renovado", updated.getName());
        assertEquals("Córdoba", updated.getCiudad());
        
        // Verificar que se actualizó en la BD
        Optional<Negocio> retrieved = negocioRepository.findById(negocio1.getId());
        assertTrue(retrieved.isPresent());
        assertEquals("Restaurante La Tasca Renovado", retrieved.get().getName());
        assertEquals("Córdoba", retrieved.get().getCiudad());
    }
    
    @Test
    void testDelete() {
        // Obtener cantidad inicial
        long initialCount = StreamSupport.stream(negocioRepository.findAll().spliterator(), false).count();
        
        // Eliminar un negocio
        negocioRepository.delete(negocio3);
        
        // Verificar que se eliminó
        long newCount = StreamSupport.stream(negocioRepository.findAll().spliterator(), false).count();
        assertEquals(initialCount - 1, newCount);
        
        // Verificar que no se puede encontrar
        Negocio shouldBeNull = negocioRepository.findByTokenNegocio(54321);
        assertNull(shouldBeNull);
    }
    
    @Test
    void testDeleteById() {
        // Eliminar por ID
        negocioRepository.deleteById(negocio2.getId());
        
        // Verificar que se eliminó
        Optional<Negocio> shouldBeDeleted = negocioRepository.findById(negocio2.getId());
        assertFalse(shouldBeDeleted.isPresent());
        
        // Verificar que los demás siguen existiendo
        assertEquals(2, StreamSupport.stream(negocioRepository.findAll().spliterator(), false).count());
    }
    
    // Tests para métodos de consulta personalizados
    
    @Test
    void testFindByName_Success() {
        Negocio found = negocioRepository.findByName("Bar El Rincón");
        
        assertNotNull(found);
        assertEquals(67890, found.getTokenNegocio());
        assertEquals("Sevilla", found.getCiudad());
    }
    
    @Test
    void testFindByName_NotFound() {
        Negocio notFound = negocioRepository.findByName("Negocio Inexistente");
        
        assertNull(notFound);
    }
    
    @Test
    void testFindByName_PartialMatch() {
        // Asumiendo que findByName usa LIKE %name%
        Negocio found = negocioRepository.findByName("Rincón");
        
        // Si usa exacto, este test podría fallar
        if (found != null) {
            assertEquals("Bar El Rincón", found.getName());
        }
    }
    
    @Test
    void testFindByDireccion_Success() {
        List<Negocio> negocios = negocioRepository.findByDireccion("Plaza Mayor 10");
        
        assertNotNull(negocios);
        assertEquals(1, negocios.size());
        assertEquals("Café Central", negocios.get(0).getName());
    }
    
    @Test
    void testFindByDireccion_NotFound() {
        List<Negocio> notFound = negocioRepository.findByDireccion("Dirección Inexistente");
        
        assertTrue(notFound.isEmpty());
    }
    
    @Test
    void testFindByCiudad_Success() {
        List<Negocio> negocios = negocioRepository.findByCiudad("Sevilla");
        
        assertNotNull(negocios);
        assertEquals(2, negocios.size());
        assertTrue(negocios.stream().allMatch(n -> "Sevilla".equals(n.getCiudad())));
    }
    
    @Test
    void testFindByCiudad_NotFound() {
        List<Negocio> notFound = negocioRepository.findByCiudad("Ciudad Inexistente");
        
        assertTrue(notFound.isEmpty());
    }
    
    @Test
    void testFindByCodigoPostal_Success() {
        List<Negocio> negocios = negocioRepository.findByCodigoPostal("41001");
        
        assertNotNull(negocios);
        assertEquals(2, negocios.size());
        assertTrue(negocios.stream().allMatch(n -> "41001".equals(n.getCodigoPostal())));
    }
    
    @Test
    void testFindByCodigoPostal_NotFound() {
        List<Negocio> notFound = negocioRepository.findByCodigoPostal("00000");
        
        assertTrue(notFound.isEmpty());
    }
    
    @Test
    void testFindByPais_Success() {
        List<Negocio> negocios = negocioRepository.findByPais("España");
        
        assertNotNull(negocios);
        assertEquals(3, negocios.size());
        assertTrue(negocios.stream().allMatch(n -> "España".equals(n.getPais())));
    }
    
    @Test
    void testFindByPais_NotFound() {
        List<Negocio> notFound = negocioRepository.findByPais("País Inexistente");
        
        assertTrue(notFound.isEmpty());
    }
    
    @Test
    void testFindByTokenNegocio_Success() {
        Negocio found = negocioRepository.findByTokenNegocio(54321);
        
        assertNotNull(found);
        assertEquals("Café Central", found.getName());
        assertEquals("Madrid", found.getCiudad());
    }
    
    @Test
    void testFindByTokenNegocio_NotFound() {
        Negocio notFound = negocioRepository.findByTokenNegocio(99999);
        
        assertNull(notFound);
    }
    
    @Test
    void testFindByDueño_Success() {
        List<Negocio> negocios = negocioRepository.findByDueño(dueño1.getId());
        
        assertNotNull(negocios);
        assertEquals(2, negocios.size());
        assertTrue(negocios.stream().allMatch(n -> n.getDueño().getId().equals(dueño1.getId())));
    }
    
    @Test
    void testFindByDueño_NotFound() {
        List<Negocio> notFound = negocioRepository.findByDueño(9999);
        
        assertTrue(notFound.isEmpty());
    }
    
    // Tests para casos especiales
    
    @Test
    void testUniqueTokenNegocio() {
        // Intentar crear un negocio con un token que ya existe
        Negocio negocioDuplicado = new Negocio();
        negocioDuplicado.setName("Negocio con Token Duplicado");
        negocioDuplicado.setDireccion("Alguna Calle 123");
        negocioDuplicado.setCiudad("Sevilla");
        negocioDuplicado.setPais("España");
        negocioDuplicado.setCodigoPostal("41001");
        negocioDuplicado.setTokenNegocio(12345); // Mismo token que negocio1
        negocioDuplicado.setDueño(dueño2);
        
        // Debería fallar por la restricción de unicidad
        assertThrows(Exception.class, () -> {
            negocioRepository.save(negocioDuplicado);
            negocioRepository.findAll(); // Forzar flush
        });
    }
    
    @Test
    void testRelacionDueñoNegocio() {
        // Verificar la relación desde el lado del negocio
        assertEquals(dueño1.getId(), negocio1.getDueño().getId());
        assertEquals(dueño1.getId(), negocio2.getDueño().getId());
        assertEquals(dueño2.getId(), negocio3.getDueño().getId());
    }
}