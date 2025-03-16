/*
package ispp_g2.gastrostock.testDueño;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import ispp_g2.gastrostock.dueño.Dueño;
import ispp_g2.gastrostock.dueño.DueñoRepository;

import java.util.Optional;

@DataJpaTest
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
        Optional<Dueño> found = repo.findById(dueño.getId());
        assertTrue(found.isPresent());
        assertEquals("testToken", found.get().getTokenDueño());
    }

    @Test
    void testFindByEmail() {
        // Crear un Dueño directamente sin asociar a un User
        Dueño dueño = new Dueño();
        dueño.setTokenDueño("testToken");
        // Si la entidad Dueño tiene email, asignar el email
        dueño.setEmail("test@example.com");

        // Guardar el Dueño
        repo.save(dueño);

        // Buscar por email
        Optional<Dueño> found = repo.findByEmail("test@example.com");
        assertTrue(found.isPresent());
        assertEquals("test@example.com", found.get().getEmail());
    }

    @Test
    void testFindAll() {
        // Crear Dueños sin asociar User
        Dueño dueño1 = new Dueño();
        Dueño dueño2 = new Dueño();
        repo.save(dueño1);
        repo.save(dueño2);

        // Verificar que ambos Dueños fueron guardados
        var dueños = repo.findAll();
        assertEquals(2, dueños.size());
    }

    @Test
    void testDeleteById() {
        // Crear un Dueño directamente sin asociar a un User
        Dueño dueño = new Dueño();
        dueño.setTokenDueño("testToken");
        dueño = repo.save(dueño);
        int id = dueño.getId();

        // Borrar el Dueño
        repo.deleteById(id);

        // Verificar que el Dueño ha sido borrado
        Optional<Dueño> found = repo.findById(id);
        assertFalse(found.isPresent());
    }
}
*/