package ispp_g2.gastrostock.testEmpleado;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import com.fasterxml.jackson.databind.ObjectMapper;
import ispp_g2.gastrostock.empleado.Empleado;
import ispp_g2.gastrostock.empleado.EmpleadoController;
import ispp_g2.gastrostock.empleado.EmpleadoDTO;
import ispp_g2.gastrostock.empleado.EmpleadoService;
import ispp_g2.gastrostock.exceptions.ExceptionHandlerController;
import ispp_g2.gastrostock.negocio.Negocio;     
import ispp_g2.gastrostock.negocio.NegocioService;
import ispp_g2.gastrostock.dueno.DuenoService;
import ispp_g2.gastrostock.dueno.Dueno;
import ispp_g2.gastrostock.user.UserService;       
import ispp_g2.gastrostock.user.User;  
import ispp_g2.gastrostock.user.Authorities;  

import java.util.ArrayList;
import java.util.Collections;
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

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class EmpleadoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmpleadoService empleadoService;

    @InjectMocks
    private EmpleadoController empleadoController;

    @Mock
    private NegocioService negocioService;

    @Mock
    private UserService userService;

    @Mock
    private DuenoService duenoService;

    private ObjectMapper objectMapper;
    private Empleado empleado1, empleadoNuevo, empleado;
    private Empleado empleado2;
    private Empleado empleadoInvalido;
    private List<Empleado> empleadosList;
    private Negocio negocio;
    private User user, user1;
    private Dueno duenoNormal;
    private Authorities authority;

    // --- Configuración de usuario admin para simular la seguridad ---
    private User adminUser;
    private Authorities adminAuthority;

    @BeforeEach
    void setUp() {
        // Configurar MockMvc con ControllerAdvice
        mockMvc = MockMvcBuilders.standaloneSetup(empleadoController)
                .setControllerAdvice(new ExceptionHandlerController())
                .build();

        objectMapper = new ObjectMapper();

        // Crear autoridad para empleado
        authority = new Authorities();
        authority.setId(1);
        authority.setAuthority("empleado");

        // Crear usuarios de prueba
        user = new User();
        user.setId(1);
        user.setUsername("juanperez");
        user.setPassword("password123");
        user.setAuthority(authority);

        user1 = new User();
        user1.setId(2);
        user1.setUsername("anton");
        user1.setPassword("password123");
        user1.setAuthority(authority);

        // Configurar usuario administrador para la seguridad de los endpoints
        adminAuthority = new Authorities();
        adminAuthority.setId(100);
        adminAuthority.setAuthority("admin");

        adminUser = new User();
        adminUser.setId(999);
        adminUser.setUsername("admin");
        adminUser.setPassword("adminpass");
        adminUser.setAuthority(adminAuthority);

        // Configurar que findCurrentUser() retorne al usuario admin
        lenient().when(userService.findCurrentUser()).thenReturn(adminUser);


        Dueno duenoNormal = new Dueno();
        duenoNormal.setId(1);
        duenoNormal.setFirstName("Juan");
        duenoNormal.setLastName("García");
        duenoNormal.setEmail("juan@example.com");
        duenoNormal.setNumTelefono("652345678");
        duenoNormal.setTokenDueno("TOKEN123");
        duenoNormal.setUser(user);
        
        // Crear un negocio de prueba
        negocio = new Negocio();
        negocio.setId(1);
        negocio.setName("Restaurante Test");
        negocio.setDireccion("Calle Test 123");
        negocio.setCiudad("Sevilla");
        negocio.setPais("Espana");
        negocio.setDueno(duenoNormal);
        negocio.setCodigoPostal("41001");
        negocio.setTokenNegocio(12345);

        // Crear empleados para los tests
        empleado1 = new Empleado();
        empleado1.setId(1);
        empleado1.setFirstName("Juan");
        empleado1.setLastName("Pérez");
        empleado1.setEmail("juan.perez@example.com");
        empleado1.setNumTelefono("666111222");
        empleado1.setTokenEmpleado("TOKEN123");
        empleado1.setDescripcion("Camarero principal");
        empleado1.setUser(user1);
        empleado1.setNegocio(negocio);

        empleado = new Empleado();
        empleado.setFirstName("Antonio");
        empleado.setLastName("Almanza");
        empleado.setEmail("antonio@example.com");
        empleado.setNumTelefono("666151222");
        empleado.setTokenEmpleado("TOKEN129");
        empleado.setDescripcion("Camarero principal");
        empleado.setUser(user);
        empleado.setNegocio(negocio);

        empleado2 = new Empleado();
        empleado2.setFirstName("Ana");
        empleado2.setLastName("García");
        empleado2.setEmail("ana.garcia@example.com");
        empleado2.setNumTelefono("666333444");
        empleado2.setTokenEmpleado("TOKEN456");
        empleado2.setDescripcion("Cocinera");
        empleado2.setNegocio(negocio);

        // Empleado inválido (sin campos requeridos)
        empleadoInvalido = new Empleado();

        // Lista de empleados para pruebas
        empleadosList = new ArrayList<>();
        empleadosList.add(empleado1);
        empleadosList.add(empleado2);
    }

    // TESTS PARA findAll()

    @Test
    void testFindAll_Success() throws Exception {
        when(empleadoService.getAllEmpleados()).thenReturn(empleadosList);

        mockMvc.perform(get("/api/empleados")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", is("Juan")))
                .andExpect(jsonPath("$[1].firstName", is("Ana")));

        verify(empleadoService, times(2)).getAllEmpleados();
    }

    @Test
    void testFindAll_NoContent() throws Exception {
        when(empleadoService.getAllEmpleados()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/empleados")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(empleadoService).getAllEmpleados();
    }

    // TESTS PARA findById()

    @Test
    void testFindById_Success() throws Exception {
        when(empleadoService.getEmpleadoById(1)).thenReturn(empleado1);

        mockMvc.perform(get("/api/empleados/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Juan")));

        verify(empleadoService).getEmpleadoById(1);
    }

    @Test
    void testFindById_NotFound() throws Exception {
        when(empleadoService.getEmpleadoById(999)).thenReturn(null);

        mockMvc.perform(get("/api/empleados/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(empleadoService).getEmpleadoById(999);
    }

    // TESTS PARA findByEmail()

    @Test
    void testFindByEmail_Success() throws Exception {
        String email = "juan.perez@example.com";
        when(empleadoService.getEmpleadoByEmail(email)).thenReturn(empleado1);

        mockMvc.perform(get("/api/empleados/email/{email}", email)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(email)))
                .andExpect(jsonPath("$.firstName", is("Juan")));

        verify(empleadoService, times(1)).getEmpleadoByEmail(email);
    }

    @Test
    void testFindByEmail_NotFound() throws Exception {
        when(empleadoService.getEmpleadoByEmail("noexiste@example.com")).thenReturn(null);

        mockMvc.perform(get("/api/empleados/email/noexiste@example.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(empleadoService).getEmpleadoByEmail("noexiste@example.com");
    }

    // TESTS PARA findByNombre()

    @Test
    void testFindByNombre_Success() throws Exception {
        when(empleadoService.getEmpleadoByNombre("Juan")).thenReturn(Collections.singletonList(empleado1));

        mockMvc.perform(get("/api/empleados/nombre/Juan")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is("Juan")));

        verify(empleadoService, times(1)).getEmpleadoByNombre("Juan");
    }

    @Test
    void testFindByNombre_NotFound() throws Exception {
        when(empleadoService.getEmpleadoByNombre("NoExiste")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/empleados/nombre/NoExiste")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(empleadoService).getEmpleadoByNombre("NoExiste");
    }

    // TESTS PARA findByApellido()

    @Test
    void testFindByApellido_Success() throws Exception {
        when(empleadoService.getEmpleadoByApellido("García")).thenReturn(Collections.singletonList(empleado2));

        mockMvc.perform(get("/api/empleados/apellido/García")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].lastName", is("García")));

        verify(empleadoService, times(2)).getEmpleadoByApellido("García");
    }

    @Test
    void testFindByApellido_NotFound() throws Exception {
        when(empleadoService.getEmpleadoByApellido("NoExiste")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/empleados/apellido/NoExiste")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(empleadoService).getEmpleadoByApellido("NoExiste");
    }

    // TESTS PARA findByTelefono()

    @Test
    void testFindByTelefono_Success() throws Exception {
        when(empleadoService.getEmpleadoByTelefono("666111222")).thenReturn(empleado1);

        mockMvc.perform(get("/api/empleados/telefono/666111222")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numTelefono", is("666111222")));

        verify(empleadoService, times(2)).getEmpleadoByTelefono("666111222");
    }

    @Test
    void testFindByTelefono_NotFound() throws Exception {
        when(empleadoService.getEmpleadoByTelefono("999999999")).thenReturn(null);

        mockMvc.perform(get("/api/empleados/telefono/999999999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(empleadoService).getEmpleadoByTelefono("999999999");
    }

    // TESTS PARA findByNegocio()

    @Test
    void testFindByNegocio_Success() throws Exception {
        when(empleadoService.getEmpleadoByNegocio(1)).thenReturn(empleadosList);
        // Para endpoints que validan la relación con el dueño, se puede omitir la validación al usar admin
        when(duenoService.getDuenoByUser(anyInt())).thenReturn(null); // No se usa cuando el usuario es admin

        mockMvc.perform(get("/api/empleados/negocio/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].firstName", is("Ana")));

        verify(empleadoService, times(2)).getEmpleadoByNegocio(1);
    }

    @Test
    void testFindByNegocio_NotFound() throws Exception {
        when(empleadoService.getEmpleadoByNegocio(999)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/empleados/negocio/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(empleadoService).getEmpleadoByNegocio(999);
    }

    // TESTS PARA findByUser()

    @Test
    void testFindByUser_Success() throws Exception {
        when(empleadoService.getEmpleadoByUser(1)).thenReturn(empleado1);

        mockMvc.perform(get("/api/empleados/user/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Juan")));

        verify(empleadoService, times(2)).getEmpleadoByUser(1);
    }

    @Test
    void testFindByUser_NotFound() throws Exception {
        when(empleadoService.getEmpleadoByUser(999)).thenReturn(null);

        mockMvc.perform(get("/api/empleados/user/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(empleadoService).getEmpleadoByUser(999);
    }

    // TESTS PARA findByTokenEmpleado()

    @Test
    void testFindByTokenEmpleado_Success() throws Exception {
        when(empleadoService.getEmpleadoByTokenEmpleado("TOKEN123")).thenReturn(empleado1);

        mockMvc.perform(get("/api/empleados/token/TOKEN123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tokenEmpleado", is("TOKEN123")));

        verify(empleadoService, times(2)).getEmpleadoByTokenEmpleado("TOKEN123");
    }

    @Test
    void testFindByTokenEmpleado_NotFound() throws Exception {
        when(empleadoService.getEmpleadoByTokenEmpleado("TOKEN_NONEXISTENT")).thenReturn(null);

        mockMvc.perform(get("/api/empleados/token/TOKEN_NONEXISTENT")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(empleadoService).getEmpleadoByTokenEmpleado("TOKEN_NONEXISTENT");
    }

    // TESTS PARA save()

    @Test
    void testSave_Success() throws Exception {
        // Crear EmpleadoDTO de prueba
        EmpleadoDTO empleadoDTO = new EmpleadoDTO();
        empleadoDTO.setUsername("anton");
        empleadoDTO.setPassword("password123");
        empleadoDTO.setFirstName("Antonio");
        empleadoDTO.setLastName("Almanza");
        empleadoDTO.setEmail("antonio@example.com");
        empleadoDTO.setNumTelefono("666151222");
        empleadoDTO.setTokenEmpleado("TOKEN129");
        empleadoDTO.setDescripcion("Camarero principal");
        empleadoDTO.setNegocio(1);

        when(negocioService.getById(1)).thenReturn(negocio);
        when(userService.findUserByUsername("anton")).thenReturn(null);
        when(empleadoService.convertirDTOEmpleado(any(EmpleadoDTO.class), eq(negocio))).thenReturn(empleado);
        when(empleadoService.saveEmpleado(any(Empleado.class))).thenReturn(empleado1);

        mockMvc.perform(post("/api/empleados")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is("Juan")));

        verify(negocioService).getById(1);
        verify(userService).findUserByUsername("anton");
        verify(empleadoService).saveEmpleado(any(Empleado.class));
    }

    @Test
    void testSave_InvalidData() throws Exception {
        mockMvc.perform(post("/api/empleados")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoInvalido)))
                .andExpect(status().isBadRequest());

        verify(empleadoService, never()).saveEmpleado(any(Empleado.class));
    }

    // TESTS PARA update()

    @Test
    void testUpdate_Success() throws Exception {
        EmpleadoDTO empleadoDTO = new EmpleadoDTO();
        empleadoDTO.setUsername("anton");
        empleadoDTO.setPassword("password123");
        empleadoDTO.setFirstName("Juan Actualizado");
        empleadoDTO.setLastName("Pérez");
        empleadoDTO.setEmail("juan.perez@example.com");
        empleadoDTO.setNumTelefono("666111222");
        empleadoDTO.setTokenEmpleado("TOKEN123");
        empleadoDTO.setDescripcion("Camarero principal actualizado");
        empleadoDTO.setNegocio(1);

        Empleado empleadoActualizado = new Empleado();
        empleadoActualizado.setId(1);
        empleadoActualizado.setFirstName("Juan Actualizado");
        empleadoActualizado.setLastName("Pérez");
        empleadoActualizado.setEmail("juan.perez@example.com");
        empleadoActualizado.setNumTelefono("666111222");
        empleadoActualizado.setTokenEmpleado("TOKEN123");
        empleadoActualizado.setDescripcion("Camarero principal actualizado");
        empleadoActualizado.setUser(user);
        empleadoActualizado.setNegocio(negocio);

        when(empleadoService.getEmpleadoById(1)).thenReturn(empleado1);
        when(negocioService.getById(1)).thenReturn(negocio);
        when(empleadoService.convertirDTOEmpleado(any(EmpleadoDTO.class), eq(negocio))).thenReturn(empleadoActualizado);
        when(empleadoService.saveEmpleado(any(Empleado.class))).thenReturn(empleadoActualizado);

        mockMvc.perform(put("/api/empleados/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Juan Actualizado")));

        verify(empleadoService, atLeastOnce()).getEmpleadoById(1);
        verify(negocioService).getById(1);
        verify(empleadoService).saveEmpleado(any(Empleado.class));
    }

    @Test
    void testUpdate_NotFound() throws Exception {
        EmpleadoDTO empleadoDTO = new EmpleadoDTO();
        empleadoDTO.setUsername("noexiste");
        empleadoDTO.setPassword("password123");
        empleadoDTO.setFirstName("No");
        empleadoDTO.setLastName("Existe");
        empleadoDTO.setEmail("noexiste@example.com");
        empleadoDTO.setNumTelefono("999888777");
        empleadoDTO.setTokenEmpleado("TOKEN999");
        empleadoDTO.setDescripcion("Descripción test");
        empleadoDTO.setNegocio(1);

        when(empleadoService.getEmpleadoById(999)).thenReturn(null);

        mockMvc.perform(put("/api/empleados/999")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoDTO)))
                .andExpect(status().isNotFound());

        verify(empleadoService, atLeastOnce()).getEmpleadoById(999);
        verify(empleadoService, never()).saveEmpleado(any(Empleado.class));
    }

    @Test
    void testUpdate_InvalidData() throws Exception {
        mockMvc.perform(put("/api/empleados/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoInvalido)))
                .andExpect(status().isBadRequest());

        verify(empleadoService, never()).saveEmpleado(any(Empleado.class));
    }

    // TESTS PARA delete()

    @Test
    void testDelete_Success() throws Exception {
        when(empleadoService.getEmpleadoById(1)).thenReturn(empleado1);
        doNothing().when(empleadoService).deleteEmpleado(1);

        mockMvc.perform(delete("/api/empleados/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(empleadoService).getEmpleadoById(1);
        verify(empleadoService).deleteEmpleado(1);
    }

    @Test
    void testDelete_NotFound() throws Exception {
        when(empleadoService.getEmpleadoById(999)).thenReturn(null);

        mockMvc.perform(delete("/api/empleados/999")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(empleadoService).getEmpleadoById(999);
        verify(empleadoService, never()).deleteEmpleado(anyInt());
    }
}
