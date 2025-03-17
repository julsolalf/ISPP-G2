package ispp_g2.gastrostock.testNegocio;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import ispp_g2.gastrostock.dueño.Dueño;
import ispp_g2.gastrostock.dueño.DueñoRepository;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioRepository;

@DataJpaTest
@AutoConfigureTestDatabase
class NegocioRepositoryTest {

    @Autowired
    private NegocioRepository negocioRepository;
    
    @Autowired
    private DueñoRepository dueñoRepository;
    
    private Dueño dueño1, dueño2;
    private Negocio negocio1, negocio2, negocio3;
    
    @BeforeEach
    void setUp() {
        // Clean repositories
        negocioRepository.deleteAll();
        dueñoRepository.deleteAll();
        
        // Create dueños
        dueño1 = new Dueño();
        dueño1.setFirstName("Juan");
        dueño1.setLastName("García");
        dueño1.setEmail("juan@example.com");
        dueño1.setNumTelefono("666111222");
        dueño1.setTokenDueño("TOKEN123");
        dueño1 = dueñoRepository.save(dueño1);
        
        dueño2 = new Dueño();
        dueño2.setFirstName("María");
        dueño2.setLastName("López");
        dueño2.setEmail("maria@example.com");
        dueño2.setNumTelefono("666333444");
        dueño2.setTokenDueño("TOKEN456");
        dueño2 = dueñoRepository.save(dueño2);
        
        // Create negocios
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
    
    @Test
    void testFindAll() {
        List<Negocio> negocios = negocioRepository.findAll();
        
        assertNotNull(negocios);
        assertEquals(3, negocios.size());
    }
    
    @Test
    void testFindById() {
        Negocio found = negocioRepository.findById(negocio1.getId());
        
        assertNotNull(found);
        assertEquals("Restaurante La Tasca", found.getName());
        assertEquals("Calle Principal 123", found.getDireccion());
    }
    
    @Test
    void testFindByName() {
        Negocio found = negocioRepository.findByName("Bar El Rincón");
        
        assertNotNull(found);
        assertEquals(67890, found.getTokenNegocio());
        assertEquals("Sevilla", found.getCiudad());
    }
    
    @Test
    void testFindByDireccion() {
        Negocio found = negocioRepository.findByDireccion("Plaza Mayor 10");
        
        assertNotNull(found);
        assertEquals("Café Central", found.getName());
        assertEquals("Madrid", found.getCiudad());
    }
    
    @Test
    void testFindByCiudad() {
        List<Negocio> negocios = negocioRepository.findByCiudad("Sevilla");
        
        assertNotNull(negocios);
        assertEquals(2, negocios.size());
        assertTrue(negocios.stream().allMatch(n -> "Sevilla".equals(n.getCiudad())));
    }
    
    @Test
    void testFindByCodigoPostal() {
        List<Negocio> negocios = negocioRepository.findByCodigoPostal("41001");
        
        assertNotNull(negocios);
        assertEquals(2, negocios.size());
        assertTrue(negocios.stream().allMatch(n -> "41001".equals(n.getCodigoPostal())));
    }
    
    @Test
    void testFindByPais() {
        List<Negocio> negocios = negocioRepository.findByPais("España");
        
        assertNotNull(negocios);
        assertEquals(3, negocios.size());
        assertTrue(negocios.stream().allMatch(n -> "España".equals(n.getPais())));
    }
    
    @Test
    void testFindByTokenNegocio() {
        Negocio found = negocioRepository.findByTokenNegocio(54321);
        
        assertNotNull(found);
        assertEquals("Café Central", found.getName());
        assertEquals("Madrid", found.getCiudad());
    }
    
    @Test
    void testSaveNegocio() {
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
        
        // Verify it was saved to DB
        Negocio retrieved = negocioRepository.findByTokenNegocio(11223);
        assertNotNull(retrieved);
        assertEquals("Valencia", retrieved.getCiudad());
    }
    
    @Test
    void testDeleteNegocio() {
        // Get the initial count
        int initialCount = negocioRepository.findAll().size();
        
        // Delete one negocio
        negocioRepository.delete(negocio3);
        
        // Verify it's deleted
        List<Negocio> remaining = negocioRepository.findAll();
        assertEquals(initialCount - 1, remaining.size());
        
        // Try to find the deleted one
        Negocio shouldBeNull = negocioRepository.findByTokenNegocio(54321);
        assertNull(shouldBeNull);
    }
    
    @Test
    void testFindByDueño() {
        // Assuming there's a method to find by dueño
        // If not, you would add this to your repository
        List<Negocio> negocios1 = negocioRepository.findAll().stream()
            .filter(n -> n.getDueño().getId().equals(dueño1.getId()))
            .toList();
            
        List<Negocio> negocios2 = negocioRepository.findAll().stream()
            .filter(n -> n.getDueño().getId().equals(dueño2.getId()))
            .toList();
        
        assertEquals(2, negocios1.size());
        assertEquals(1, negocios2.size());
        assertEquals("Café Central", negocios2.get(0).getName());
    }
}