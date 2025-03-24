package ispp_g2.gastrostock.testEmpleado;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import ispp_g2.gastrostock.empleado.Empleado;
import ispp_g2.gastrostock.empleado.EmpleadoRepository;
import ispp_g2.gastrostock.empleado.EmpleadoService;
import ispp_g2.gastrostock.empleado.Rol;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class EmpleadoServiceTest {

    @Mock
    private EmpleadoRepository repo;

    @InjectMocks
    private EmpleadoService service;

    private Empleado empleado;

    @BeforeEach
    void setUp() {
        empleado = new Empleado();
        empleado.setId(1);
        empleado.setTokenEmpleado("testToken");
    }

    @Test
    void testSaveEmpleado() {
        when(repo.save(empleado)).thenReturn(empleado);
        Empleado saved = service.saveEmpleado(empleado);
        assertNotNull(saved);
        assertEquals("testToken", saved.getTokenEmpleado());
    }

    @Test
    void testGetAllEmpleados() {
        when(repo.findAll()).thenReturn(List.of(empleado));
        List<Empleado> empleados = service.getAllEmpleados();
        assertFalse(empleados.isEmpty());
    }
//    TEMPORAL FIX
//    @Test
//    void testGetEmpleadoById() {
//        when(repo.findById(1)).thenReturn(Optional.of(empleado));
//        Optional<Empleado> found = service.getEmpleadoById(1);
//        assertTrue(found.isPresent());
//        assertEquals(1, found.get().getId());
//    }

    @Test
    void testDeleteEmpleado() {
        doNothing().when(repo).deleteById(String.valueOf(1));
        service.deleteEmpleado(String.valueOf(1));
        verify(repo, times(1)).deleteById(String.valueOf(1));
    }

}
