package ispp_g2.gastrostock.testEmpleado;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import ispp_g2.gastrostock.dueño.Dueño;
import ispp_g2.gastrostock.dueño.DueñoRepository;
import ispp_g2.gastrostock.empleado.Empleado;
import ispp_g2.gastrostock.empleado.EmpleadoRepository;
import ispp_g2.gastrostock.empleado.Rol;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioRepository;

import java.util.Optional;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase
class EmpleadoRepositoryTest {

    @Autowired
    private EmpleadoRepository repo;

    @Autowired
    private NegocioRepository negocioRep;

    @Autowired
    private DueñoRepository dueñoRep;

    @Test
    void testSaveAndFindById() {
        Empleado empleado = new Empleado();
        empleado.setTokenEmpleado("testToken");

        empleado = repo.save(empleado);

        Optional<Empleado> found = repo.findById(empleado.getId());
        assertTrue(found.isPresent());
        assertEquals("testToken", found.get().getTokenEmpleado());
    }

    @Test
    void testFindByTokenEmpleado() {

        Dueño dueño = new Dueño();
        dueño.setFirstName("Juan Propietario");
        dueño.setLastName("García");
        dueño.setEmail("juan@gastrostock.com");
        dueño.setTokenDueño("TOKEN123");
        dueño.setNumTelefono("652345678");
        dueñoRep.save(dueño);
        
        Negocio negocio1 = new Negocio();
        negocio1.setName("Restaurante La Tasca");
        negocio1.setDireccion("Calle Principal 123");
        negocio1.setCiudad("Sevilla");
        negocio1.setPais("España");
        negocio1.setCodigoPostal("41001");
        negocio1.setTokenNegocio(12345);
        negocio1.setDueño(dueño);
        negocioRep.save(negocio1);

        Empleado empleado = new Empleado();
        empleado.setTokenEmpleado("testToken");
        empleado.setNegocio(negocio1);
        empleado.setFirstName("Antinio");
        empleado.setLastName("García");
        empleado.setEmail("antoninio@test.com");
        empleado.setNumTelefono("666111222");

        repo.save(empleado);


        Optional<Empleado> found = repo.findByTokenEmpleado("testToken");
        assertTrue(found.isPresent());
        assertEquals("testToken", found.get().getTokenEmpleado());
    }

    @Test
    void testDeleteById() {
        Empleado empleado = new Empleado();
        empleado.setTokenEmpleado("testToken");

        empleado = repo.save(empleado);
        int id = empleado.getId();

        repo.deleteById(id);

        Optional<Empleado> found = repo.findById(id);
        assertFalse(found.isPresent());
    }
}

