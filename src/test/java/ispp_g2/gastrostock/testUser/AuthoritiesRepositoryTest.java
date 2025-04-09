package ispp_g2.gastrostock.testUser;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ispp_g2.gastrostock.user.Authorities;
import ispp_g2.gastrostock.user.AuthoritiesRepository;

@DataJpaTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class AuthoritiesRepositoryTest {

    @Autowired
    private AuthoritiesRepository authoritiesRepository;
    
    private Authorities authority1;
    private Authorities authority2;
    
    @BeforeEach
    void setUp() {
        authoritiesRepository.deleteAll();
        
        authority1 = new Authorities();
        authority1.setAuthority("ROLE_USER");
        authority1 = authoritiesRepository.save(authority1);
        
        authority2 = new Authorities();
        authority2.setAuthority("ROLE_ADMIN");
        authority2 = authoritiesRepository.save(authority2);
    }
    
    
    @Test
    void testFindByAuthority_Success() {
        Authorities result = authoritiesRepository.findByAuthority("ROLE_USER");
        assertNotNull(result, "Debe encontrar la autoridad ROLE_USER.");
        assertEquals("ROLE_USER", result.getAuthority());
    }
    
    @Test
    void testFindByAuthority_NotFound() {
        Authorities result = authoritiesRepository.findByAuthority("NON_EXISTENT");
        assertNull(result, "No se debe encontrar una autoridad inexistente.");
    }
    
    
    @Test
    void testSaveAndFindById() {
        Optional<Authorities> optAuth = authoritiesRepository.findById(authority1.getId());
        assertTrue(optAuth.isPresent(), "El Optional debe estar presente tras guardar la autoridad.");
        assertEquals("ROLE_USER", optAuth.get().getAuthority());
    }
    
    @Test
    void testFindAllAuthorities() {
        Iterable<Authorities> allAuthorities = authoritiesRepository.findAll();
        long count = StreamSupport.stream(allAuthorities.spliterator(), false).count();
        assertEquals(2, count, "Se deben recuperar 2 registros.");
    }
    
    @Test
    void testCountAuthorities() {
        long count = authoritiesRepository.count();
        assertEquals(2, count, "El count debe ser 2 tras insertar 2 registros.");
    }
    
    @Test
    void testUpdateAuthorities() {
        authority1.setAuthority("ROLE_UPDATED");
        authority1 = authoritiesRepository.save(authority1);
        Optional<Authorities> optAuth = authoritiesRepository.findById(authority1.getId());
        assertTrue(optAuth.isPresent());
        assertEquals("ROLE_UPDATED", optAuth.get().getAuthority(), "La autoridad debe estar actualizada.");
    }
    
    @Test
    void testDeleteAuthorities() {
        authoritiesRepository.delete(authority2);
        Optional<Authorities> optAuth = authoritiesRepository.findById(authority2.getId());
        assertFalse(optAuth.isPresent(), "authority2 debe haber sido eliminada.");
    }
    
    @Test
    void testDeleteById() {
        Integer id = authority1.getId();
        authoritiesRepository.deleteById(id);
        Optional<Authorities> optAuth = authoritiesRepository.findById(id);
        assertFalse(optAuth.isPresent(), "La autoridad eliminada por id ya no debe existir.");
    }
    

    
    @Test
    void testFindByAuthorityWithNull() {
        
        Authorities result = authoritiesRepository.findByAuthority(null);
        assertNull(result, "Buscar con argumento null debería devolver null.");
    }
    
    @Test
    void testSaveDuplicateAuthorities() {
        Authorities duplicate = new Authorities();
        duplicate.setAuthority("ROLE_USER");
        Authorities savedDuplicate = authoritiesRepository.save(duplicate);
        assertNotNull(savedDuplicate.getId(), "El duplicado debe tener ID asignado.");
        assertNotEquals(authority1.getId(), savedDuplicate.getId(), "El duplicado debe tener un ID diferente.");
        assertEquals("ROLE_USER", savedDuplicate.getAuthority());
    }
    

}
