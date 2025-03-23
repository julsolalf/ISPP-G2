package ispp_g2.gastrostock.testEmpleado;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ispp_g2.gastrostock.empleado.Empleado;
import ispp_g2.gastrostock.empleado.EmpleadoController;
import ispp_g2.gastrostock.empleado.EmpleadoService;
import ispp_g2.gastrostock.empleado.Rol;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class EmpleadoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmpleadoService empleadoService;

    @InjectMocks
    private EmpleadoController empleadoController;

    private Empleado empleado;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(empleadoController).build();
        empleado = new Empleado();
        empleado.setId(1);
        empleado.setTokenEmpleado("testToken");
    }

    @Test
    void testGetAllEmpleados() throws Exception {
        when(empleadoService.getAllEmpleados()).thenReturn(List.of(empleado));

        mockMvc.perform(get("/api/v1/empleados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testGetEmpleadoById() throws Exception {
        when(empleadoService.getEmpleadoById(String.valueOf(1))).thenReturn(empleado);

        mockMvc.perform(get("/api/v1/empleados/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testCreateEmpleado() throws Exception {
        when(empleadoService.saveEmpleado(any(Empleado.class))).thenReturn(empleado);

        mockMvc.perform(post("/api/v1/empleados")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"tokenEmpleado\": \"testToken\", \"rol\": \"BARRA\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteEmpleado() throws Exception {
        doNothing().when(empleadoService).deleteEmpleado(String.valueOf(1));

        mockMvc.perform(delete("/api/v1/empleados/1"))
                .andExpect(status().isNoContent());
    }
    
}
