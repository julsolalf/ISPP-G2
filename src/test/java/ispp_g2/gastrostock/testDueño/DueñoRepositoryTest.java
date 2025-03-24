package ispp_g2.gastrostock.testDueño;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import ispp_g2.gastrostock.dueño.Dueño;
import ispp_g2.gastrostock.dueño.DueñoRepository;


import java.util.List;
import java.util.Optional;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase
class DueñoRepositoryTest {

    @Autowired
    private DueñoRepository repo;

    @Test
    void testSaveAndFindById() {
        // Crear un Dueño directamente sin asociar a un User
        Dueño dueño = new Dueño();
        dueño.setTokenDueño("testToken");

        // Guardar el Dueño
        dueño = repo.save(dueño);

        // Verificar que el Dueño se guardó correctamente
        Optional<Dueño> found = repo.findById(String.valueOf(dueño.getId()));
        assertTrue(found.isPresent());
        assertEquals("testToken", found.get().getTokenDueño());
    }

    @Test
    void testFindByEmail() {
        // Crear un Dueño directamente sin asociar a un User
        Dueño dueño = new Dueño();
        dueño.setTokenDueño("testToken");
        dueño.setFirstName("Manolo");
        dueño.setLastName("García");
        dueño.setNumTelefono("652345678");
        // Si la entidad Dueño tiene email, asignar el email
        dueño.setEmail("test@example.com");

        // Guardar el Dueño
        repo.save(dueño);

        // Buscar por email
        Optional<Dueño> found = repo.findDueñoByEmail("test@example.com");
        assertTrue(found.isPresent());
        assertEquals("test@example.com", found.get().getEmail());
    }

    @Test
    void testFindAll() {
        Dueño dueño = new Dueño();
        dueño.setTokenDueño("testToken");
        dueño.setFirstName("Manolo");
        dueño.setLastName("García");
        dueño.setNumTelefono("652345678");
        dueño.setEmail("test@example.com");

        Dueño dueño1 = new Dueño();
        dueño1.setTokenDueño("testToken1");
        dueño1.setFirstName("Antonio");
        dueño1.setLastName("García");
        dueño1.setNumTelefono("655545678");
        dueño1.setEmail("test1@example.com");

        repo.save(dueño);
        repo.save(dueño1);

        // Verificar que ambos Dueños fueron guardados
        var dueños = repo.findAll();
         List<Dueño> dueñosAux= (List<Dueño>) dueños;
        assertEquals(2, dueñosAux.size());
    }

    @Test
    void testDeleteById() {
        // Crear un Dueño directamente sin asociar a un User
        Dueño dueño = new Dueño();
        dueño.setTokenDueño("testToken");
        dueño = repo.save(dueño);
        int id = dueño.getId();

        //Verificar que el Dueño se guardó correctamente
        Optional<Dueño> foundDueño = repo.findById(String.valueOf(id));
        assertTrue(foundDueño.isPresent());

        // Borrar el Dueño
        repo.deleteById(String.valueOf(id));

        // Verificar que el Dueño ha sido borrado
        Optional<Dueño> found = repo.findById(String.valueOf(id));
        assertFalse(found.isPresent());
    }

    @Test
    void testFindByTokenDueño() {

        Dueño dueño = new Dueño();
        dueño.setFirstName("Juan Propietario");
        dueño.setLastName("García");
        dueño.setEmail("juan@gastrostock.com");
        dueño.setTokenDueño("testToken");
        dueño.setNumTelefono("652345678");
        repo.save(dueño);

        Optional<Dueño> found = repo.findDueñoByToken("testToken");
        assertTrue(found.isPresent());
        assertEquals("testToken", found.get().getTokenDueño());
    }

}
