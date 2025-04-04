package ispp_g2.gastrostock.testEmpleado;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import ispp_g2.gastrostock.empleado.EmpleadoDTO;
import org.mockito.Mock;
import ispp_g2.gastrostock.negocio.NegocioService;
import ispp_g2.gastrostock.user.UserService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import ispp_g2.gastrostock.empleado.Empleado;
import ispp_g2.gastrostock.empleado.EmpleadoController;
import ispp_g2.gastrostock.empleado.EmpleadoService;
import ispp_g2.gastrostock.exceptions.ExceptionHandlerController;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.user.Authorities;
import ispp_g2.gastrostock.user.User;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class EmpleadoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmpleadoService empleadoService;

    @InjectMocks
    private EmpleadoController empleadoController;

    @Mock
    private NegocioService  negocioService;

    @Mock
    private UserService userService;

    private ObjectMapper objectMapper;
    private Empleado empleado1, empleadoNuevo, empleado;
    private Empleado empleado2;
    private Empleado empleadoInvalido;
    private List<Empleado> empleadosList;
    private Negocio negocio;
    private User user, user1;
    private Authorities authority;

    @BeforeEach
    void setUp() {
        // Configurar MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(empleadoController)
                .setControllerAdvice(new ExceptionHandlerController())
                .build();
        
        // Configurar ObjectMapper para serialización/deserialización
        objectMapper = new ObjectMapper();
        
        // Crear autoridad
        authority = new Authorities();
        authority.setId(1);
        authority.setAuthority("EMPLEADO");
        
        // Crear usuario
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
        
        // Crear negocio
        negocio = new Negocio();
        negocio.setId(1);
        negocio.setName("Restaurante Test");
        negocio.setDireccion("Calle Test 123");
        negocio.setCiudad("Sevilla");
        negocio.setPais("Espana");
        negocio.setCodigoPostal("41001");
        negocio.setTokenNegocio(12345);
        
        // Crear empleados para tests
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
        
        // Empleado con datos inválidos
        empleadoInvalido = new Empleado();
        // No se establecen los campos requeridos
        
        // Lista de empleados para tests
        empleadosList = new ArrayList<>();
        empleadosList.add(empleado1);
        empleadosList.add(empleado2);
    }

    // TESTS PARA findAll()
    
    @Test
    void testFindAll_Success() throws Exception {
        // Arrange
        when(empleadoService.getAllEmpleados()).thenReturn(empleadosList);
        
        // Act & Assert
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
        // Arrange
        when(empleadoService.getAllEmpleados()).thenReturn(Collections.emptyList());
        
        // Act & Assert
        mockMvc.perform(get("/api/empleados")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        
        verify(empleadoService).getAllEmpleados();
    }

    // TESTS PARA findById()
    
    @Test
    void testFindById_Success() throws Exception {
        // Arrange
        when(empleadoService.getEmpleadoById(1)).thenReturn(empleado1);
        
        // Act & Assert
        mockMvc.perform(get("/api/empleados/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Juan")));
        
        verify(empleadoService).getEmpleadoById(1);
    }
    
    @Test
    void testFindById_NotFound() throws Exception {
        // Arrange
        when(empleadoService.getEmpleadoById(999)).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(get("/api/empleados/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(empleadoService).getEmpleadoById(999);
    }

    // TESTS PARA findByEmail()
    
    @Test
    void testFindByEmail_Success() throws Exception {
        // Arrange
        String email = "juan.perez@example.com";
        when(empleadoService.getEmpleadoByEmail(email)).thenReturn(empleado1);
        
        // Act & Assert
        mockMvc.perform(get("/api/empleados/email/{email}", email)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(email)))
                .andExpect(jsonPath("$.firstName", is("Juan")));
        
        // Verify that the service method was called exactly twice (as per controller implementation)
        verify(empleadoService, times(1)).getEmpleadoByEmail(email);
    }
    
    @Test
    void testFindByEmail_NotFound() throws Exception {
        // Arrange
        when(empleadoService.getEmpleadoByEmail("noexiste@example.com")).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(get("/api/empleados/email/noexiste@example.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(empleadoService).getEmpleadoByEmail("noexiste@example.com");
    }

    // TESTS PARA findByNombre()
    
    @Test
    void testFindByNombre_Success() throws Exception {
        // Arrange
        List<Empleado> empleadosJuan = Collections.singletonList(empleado1);
        when(empleadoService.getEmpleadoByNombre("Juan")).thenReturn(empleadosJuan);
        
        // Act & Assert
        mockMvc.perform(get("/api/empleados/nombre/Juan")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is("Juan")));
        
        verify(empleadoService, times(1)).getEmpleadoByNombre("Juan");
    }
    
    @Test
    void testFindByNombre_NotFound() throws Exception {
        // Arrange
        when(empleadoService.getEmpleadoByNombre("NoExiste")).thenReturn(Collections.emptyList());
        
        // Act & Assert
        mockMvc.perform(get("/api/empleados/nombre/NoExiste")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(empleadoService).getEmpleadoByNombre("NoExiste");
    }

    // TESTS PARA findByApellido()
    
    @Test
    void testFindByApellido_Success() throws Exception {
        // Arrange
        List<Empleado> empleadosGarcia = Collections.singletonList(empleado2);
        when(empleadoService.getEmpleadoByApellido("García")).thenReturn(empleadosGarcia);
        
        // Act & Assert
        mockMvc.perform(get("/api/empleados/apellido/García")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].lastName", is("García")));
        
        verify(empleadoService, times(2)).getEmpleadoByApellido("García");
    }
    
    @Test
    void testFindByApellido_NotFound() throws Exception {
        // Arrange
        when(empleadoService.getEmpleadoByApellido("NoExiste")).thenReturn(Collections.emptyList());
        
        // Act & Assert
        mockMvc.perform(get("/api/empleados/apellido/NoExiste")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(empleadoService).getEmpleadoByApellido("NoExiste");
    }

    // TESTS PARA findByTelefono()
    
    @Test
    void testFindByTelefono_Success() throws Exception {
        // Arrange
        when(empleadoService.getEmpleadoByTelefono("666111222")).thenReturn(empleado1);
        
        // Act & Assert
        mockMvc.perform(get("/api/empleados/telefono/666111222")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numTelefono", is("666111222")));
        
        verify(empleadoService, times(2)).getEmpleadoByTelefono("666111222");
    }
    
    @Test
    void testFindByTelefono_NotFound() throws Exception {
        // Arrange
        when(empleadoService.getEmpleadoByTelefono("999999999")).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(get("/api/empleados/telefono/999999999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(empleadoService).getEmpleadoByTelefono("999999999");
    }

    // TESTS PARA findByNegocio()
    
    @Test
    void testFindByNegocio_Success() throws Exception {
        // Arrange
        when(empleadoService.getEmpleadoByNegocio(1)).thenReturn(empleadosList);
        
        // Act & Assert
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
        // Arrange
        when(empleadoService.getEmpleadoByNegocio(999)).thenReturn(Collections.emptyList());
        
        // Act & Assert
        mockMvc.perform(get("/api/empleados/negocio/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(empleadoService).getEmpleadoByNegocio(999);
    }

    // TESTS PARA findByUser()
    
    @Test
    void testFindByUser_Success() throws Exception {
        // Arrange
        when(empleadoService.getEmpleadoByUser(1)).thenReturn(empleado1);
        
        // Act & Assert
        mockMvc.perform(get("/api/empleados/user/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Juan")));
        
        verify(empleadoService, times(2)).getEmpleadoByUser(1);
    }
    
    @Test
    void testFindByUser_NotFound() throws Exception {
        // Arrange
        when(empleadoService.getEmpleadoByUser(999)).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(get("/api/empleados/user/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(empleadoService).getEmpleadoByUser(999);
    }

    // TESTS PARA findByTokenEmpleado()
    
    @Test
    void testFindByTokenEmpleado_Success() throws Exception {
        // Arrange
        when(empleadoService.getEmpleadoByTokenEmpleado("TOKEN123")).thenReturn(empleado1);
        
        // Act & Assert
        mockMvc.perform(get("/api/empleados/token/TOKEN123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tokenEmpleado", is("TOKEN123")));
        
        verify(empleadoService, times(2)).getEmpleadoByTokenEmpleado("TOKEN123");
    }
    
    @Test
    void testFindByTokenEmpleado_NotFound() throws Exception {
        // Arrange
        when(empleadoService.getEmpleadoByTokenEmpleado("TOKEN_NONEXISTENT")).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(get("/api/empleados/token/TOKEN_NONEXISTENT")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(empleadoService).getEmpleadoByTokenEmpleado("TOKEN_NONEXISTENT");
    }

    // TESTS PARA save()
    
    @Test
    void testSave_Success() throws Exception {
        // Arrange - Crear un EmpleadoDTO
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
        
        // Configurar los mocks necesarios
        when(negocioService.getById(1)).thenReturn(negocio);
        when(userService.findUserByUsername("anton")).thenReturn(null); // Usuario no existe
        when(empleadoService.convertirDTOEmpleado(any(EmpleadoDTO.class), eq(negocio))).thenReturn(empleado);
        when(empleadoService.saveEmpleado(any(Empleado.class))).thenReturn(empleado1);
        
        // Act & Assert
        mockMvc.perform(post("/api/empleados")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is("Juan")));
        
        // Verificar las llamadas correctas
        verify(negocioService).getById(1);
        verify(userService).findUserByUsername("anton"); // Verificar findUserByUsername en lugar de findUserById
        verify(empleadoService).saveEmpleado(any(Empleado.class));
    }
    @Test
    void testSave_InvalidData() throws Exception {
        // Act & Assert - Probar con objeto vacío que debería fallar validación
        mockMvc.perform(post("/api/empleados")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoInvalido)))
                .andExpect(status().isBadRequest());
    }

    // TESTS PARA update()
    
@Test
void testUpdate_Success() throws Exception {
    // Arrange
    // Crear un EmpleadoDTO en lugar de un Empleado
    EmpleadoDTO empleadoDTO = new EmpleadoDTO();
    empleadoDTO.setUsername("anton");
    empleadoDTO.setPassword("password123");
    empleadoDTO.setFirstName("Juan Actualizado");
    empleadoDTO.setLastName("Pérez");
    empleadoDTO.setEmail("juan.perez@example.com");
    empleadoDTO.setNumTelefono("666111222");
    empleadoDTO.setTokenEmpleado("TOKEN123");
    empleadoDTO.setDescripcion("Camarero principal actualizado");
    empleadoDTO.setNegocio(1); // ID como String
    
    // Preparar un Empleado actualizado para el resultado
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
    
    // Configurar todos los mocks necesarios
    when(empleadoService.getEmpleadoById(1)).thenReturn(empleado1);
    when(negocioService.getById(1)).thenReturn(negocio); // Mock para buscar negocio
    when(empleadoService.convertirDTOEmpleado(any(EmpleadoDTO.class), eq(negocio))).thenReturn(empleadoActualizado);
    when(empleadoService.saveEmpleado(any(Empleado.class))).thenReturn(empleadoActualizado);
    
    // Act & Assert
    mockMvc.perform(put("/api/empleados/1")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(empleadoDTO))) // Enviar el DTO, no la entidad
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName", is("Juan Actualizado")));
    
    // Verificar que los métodos correctos fueron llamados
    verify(empleadoService, atLeastOnce()).getEmpleadoById(1);
    verify(negocioService).getById(1);
    verify(empleadoService).saveEmpleado(any(Empleado.class));
}
    
@Test
void testUpdate_NotFound() throws Exception {
    // Arrange
    // Crear un EmpleadoDTO
    EmpleadoDTO empleadoDTO = new EmpleadoDTO();
    empleadoDTO.setUsername("noexiste");
    empleadoDTO.setPassword("password123");
    empleadoDTO.setFirstName("No");
    empleadoDTO.setLastName("Existe");
    empleadoDTO.setEmail("noexiste@example.com");
    empleadoDTO.setNumTelefono("999888777");
    empleadoDTO.setTokenEmpleado("TOKEN999");
    empleadoDTO.setDescripcion("Descripción test");
    // ID como String
    empleadoDTO.setNegocio(1); // ID como String
    
    // Configurar el mock para retornar null (no encontrado)
    when(empleadoService.getEmpleadoById(999)).thenReturn(null);
    
    // Act & Assert
    mockMvc.perform(put("/api/empleados/999")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(empleadoDTO))) // Enviar el DTO, no la entidad
            .andExpect(status().isNotFound());
    
    verify(empleadoService, atLeastOnce()).getEmpleadoById(999);
    verify(empleadoService, never()).saveEmpleado(any(Empleado.class));
}
    
    @Test
    void testUpdate_InvalidData() throws Exception {

        empleadoNuevo = new Empleado();
        
        // Act & Assert - Probar con objeto vacío que debería fallar validación
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
        // Arrange
        when(empleadoService.getEmpleadoById(1)).thenReturn(empleado1);
        doNothing().when(empleadoService).deleteEmpleado(1);
        
        // Act & Assert
        mockMvc.perform(delete("/api/empleados/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        
        verify(empleadoService).getEmpleadoById(1);
        verify(empleadoService).deleteEmpleado(1);
    }
    
    @Test
    void testDelete_NotFound() throws Exception {
        // Arrange
        when(empleadoService.getEmpleadoById(999)).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(delete("/api/empleados/999")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(empleadoService).getEmpleadoById(999);
        verify(empleadoService, never()).deleteEmpleado(anyInt());
    }
}