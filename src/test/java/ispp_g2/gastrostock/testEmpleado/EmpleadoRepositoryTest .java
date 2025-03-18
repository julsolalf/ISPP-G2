package ispp_g2.gastrostock.testEmpleado;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import ispp_g2.gastrostock.empleado.Empleado;
import ispp_g2.gastrostock.empleado.EmpleadoRepository;
import ispp_g2.gastrostock.empleado.Rol;

import java.util.Optional;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase
class EmpleadoRepositoryTest {

    @Autowired
    private EmpleadoRepository repo;

    @Test
    void testSaveAndFindById() {
        Empleado empleado = new Empleado();
        empleado.setTokenEmpleado("testToken");
        empleado.setRol(Rol.BARRA);

        empleado = repo.save(empleado);

        Optional<Empleado> found = repo.findById(empleado.getId());
        assertTrue(found.isPresent());
        assertEquals("testToken", found.get().getTokenEmpleado());
    }

    @Test
    void testFindByTokenEmpleado() {
        Empleado empleado = new Empleado();
        empleado.setTokenEmpleado("testToken");
        empleado.setRol(Rol.COCINA);

        repo.save(empleado);

        Optional<Empleado> found = repo.findByTokenEmpleado("testToken");
        assertTrue(found.isPresent());
        assertEquals("testToken", found.get().getTokenEmpleado());
    }

    @Test
    void testDeleteById() {
        Empleado empleado = new Empleado();
        empleado.setTokenEmpleado("testToken");
        empleado.setRol(Rol.EXTERIOR);

        empleado = repo.save(empleado);
        int id = empleado.getId();

        repo.deleteById(id);

        Optional<Empleado> found = repo.findById(id);
        assertFalse(found.isPresent());
    }
}

