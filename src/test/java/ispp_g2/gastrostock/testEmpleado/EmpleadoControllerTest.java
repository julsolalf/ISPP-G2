package ispp_g2.gastrostock.testEmpleado;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
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
import ispp_g2.gastrostock.user.AuthoritiesService;

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
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private AuthoritiesService authorityService;

    private ObjectMapper objectMapper;
    private Empleado empleado1, empleado;
    private Empleado empleado2;
    private Empleado empleadoInvalido;
    private List<Empleado> empleadosList;
    private Negocio negocio;
    private User user, user1;
    private Dueno duenoNormal;
    private Authorities authority;
    private User adminUser;
    private Authorities adminAuthority;
    private User duenoUser;
    private User empleadoUser;
    private Authorities duenoAuthority;
    private Authorities empleAuthority;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(empleadoController)
                .setControllerAdvice(new ExceptionHandlerController())
                .build();

        objectMapper = new ObjectMapper();

        encoder = mock(PasswordEncoder.class);
 

        authority = new Authorities();  
        authority.setId(1);
        authority.setAuthority("empleado");

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

        adminAuthority = new Authorities();
        adminAuthority.setId(100);
        adminAuthority.setAuthority("admin");

        adminUser = new User();
        adminUser.setId(999);
        adminUser.setUsername("admin");
        adminUser.setPassword("adminpass");
        adminUser.setAuthority(adminAuthority);

        lenient().when(userService.findCurrentUser()).thenReturn(adminUser);


        duenoNormal = new Dueno();
        duenoNormal.setId(1);
        duenoNormal.setFirstName("Juan");
        duenoNormal.setLastName("García");
        duenoNormal.setEmail("juan@example.com");
        duenoNormal.setNumTelefono("652345678");
        duenoNormal.setTokenDueno("TOKEN123");
        duenoNormal.setUser(user);
        
        negocio = new Negocio();
        negocio.setId(1);
        negocio.setName("Restaurante Test");
        negocio.setDireccion("Calle Test 123");
        negocio.setCiudad("Sevilla");
        negocio.setPais("Espana");
        negocio.setDueno(duenoNormal);
        negocio.setCodigoPostal("41001");
        negocio.setTokenNegocio(12345);

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

        empleadoInvalido = new Empleado();

        empleadosList = new ArrayList<>();
        empleadosList.add(empleado1);
        empleadosList.add(empleado2);

        duenoAuthority = new Authorities();
        duenoAuthority.setId(200);
        duenoAuthority.setAuthority("dueno");
        duenoUser = new User();
        duenoUser.setId(10);
        duenoUser.setUsername("dueno");
        duenoUser.setAuthority(duenoAuthority);

        empleAuthority = new Authorities();
        empleAuthority.setId(300);
        empleAuthority.setAuthority("empleado");
        empleadoUser = new User();
        empleadoUser.setId(20);
        empleadoUser.setUsername("empleado");
        empleadoUser.setAuthority(empleAuthority);
    }


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


    @Test
    void testFindByNegocio_Success() throws Exception {
        when(empleadoService.getEmpleadoByNegocio(1)).thenReturn(empleadosList);
        when(duenoService.getDuenoByUser(anyInt())).thenReturn(null); 

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


    @Test
    void testSave_Success() throws Exception {
        EmpleadoDTO empleadoDTO = new EmpleadoDTO();
        empleadoDTO.setUsername("anton");
        empleadoDTO.setPassword("Password123!"); // Password válido
        empleadoDTO.setFirstName("Antonio");
        empleadoDTO.setLastName("Almanza");
        empleadoDTO.setEmail("antonio@example.com");
        empleadoDTO.setNumTelefono("666151222"); // Formato válido
        empleadoDTO.setDescripcion("Camarero principal");
        empleadoDTO.setNegocio(1);
    
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(duenoService.getDuenoByUser(adminUser.getId())).thenReturn(duenoNormal);
        when(negocioService.getById(1)).thenReturn(negocio);
        when(authorityService.findByAuthority(anyString())).thenReturn(authority);
        when(userService.saveUser(any(User.class))).thenReturn(user);
        when(empleadoService.saveEmpleado(any(Empleado.class))).thenReturn(empleado);
        
        mockMvc.perform(post("/api/empleados")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is("Antonio")));
        
        verify(negocioService).getById(1);
        verify(authorityService).findByAuthority(anyString());
        verify(userService).saveUser(any(User.class));
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
        when(empleadoService.convertirDTOEmpleado(any(EmpleadoDTO.class))).thenReturn(empleadoActualizado);
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

    @Test
    void testFindAll_AsDueno_ReturnsOk() throws Exception {
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(duenoService.getDuenoByUser(duenoUser.getId())).thenReturn(duenoNormal);
        when(empleadoService.getEmpleadoByDueno(duenoNormal.getId())).thenReturn(empleadosList);

        mockMvc.perform(get("/api/empleados"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(2)));

        verify(empleadoService).getEmpleadoByDueno(duenoNormal.getId());
    }

    @Test
    void testFindAll_AsEmpleado_ReturnsForbidden() throws Exception {
        when(userService.findCurrentUser()).thenReturn(empleadoUser);

        mockMvc.perform(get("/api/empleados"))
               .andExpect(status().isForbidden());
    }


    @Test
    void testFindAllDTO_AsDueno_ReturnsOk() throws Exception {
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(duenoService.getDuenoByUser(duenoUser.getId())).thenReturn(duenoNormal);
        when(empleadoService.getEmpleadoByDueno(duenoNormal.getId())).thenReturn(empleadosList);
        when(empleadoService.convertirEmpleadoDTO(empleado1)).thenReturn(new EmpleadoDTO());
        when(empleadoService.convertirEmpleadoDTO(empleado2)).thenReturn(new EmpleadoDTO());

        mockMvc.perform(get("/api/empleados/dto"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(2)));

        verify(empleadoService, times(2)).convertirEmpleadoDTO(any());
    }

    @Test
    void testFindAllDTO_AsEmpleado_ReturnsForbidden() throws Exception {
        when(userService.findCurrentUser()).thenReturn(empleadoUser);

        mockMvc.perform(get("/api/empleados/dto"))
               .andExpect(status().isForbidden());
    }


    @Test
    void testFindById_AsDueno_ReturnsOk() throws Exception {
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(empleadoService.getEmpleadoById(1)).thenReturn(empleado1);
        empleado1.getNegocio().getDueno().setUser(duenoUser);

        mockMvc.perform(get("/api/empleados/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void testFindById_AsEmpleadoSelf_ReturnsOk() throws Exception {
        empleado1.getUser().setId(empleadoUser.getId());
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoById(1)).thenReturn(empleado1);

        mockMvc.perform(get("/api/empleados/1"))
               .andExpect(status().isOk());
    }

    @Test
    void testFindById_AsEmpleadoOther_ReturnsForbidden() throws Exception {
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoById(1)).thenReturn(empleado1);

        mockMvc.perform(get("/api/empleados/1"))
               .andExpect(status().isForbidden());
    }


    @Test
    void testFindDTOById_AsDueno_ReturnsOk() throws Exception {
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(empleadoService.getEmpleadoById(1)).thenReturn(empleado1);
        empleado1.getNegocio().getDueno().setUser(duenoUser);
        when(empleadoService.convertirEmpleadoDTO(empleado1)).thenReturn(new EmpleadoDTO());

        mockMvc.perform(get("/api/empleados/dto/1"))
               .andExpect(status().isOk());
    }

    @Test
    void testFindDTOById_AsEmpleadoSelf_ReturnsOk() throws Exception {
        empleado1.getUser().setId(empleadoUser.getId());
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoById(1)).thenReturn(empleado1);
        when(empleadoService.convertirEmpleadoDTO(empleado1)).thenReturn(new EmpleadoDTO());

        mockMvc.perform(get("/api/empleados/dto/1"))
               .andExpect(status().isOk());
    }

    @Test
    void testFindDTOById_AsEmpleadoOther_ReturnsForbidden() throws Exception {
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoById(1)).thenReturn(empleado1);

        mockMvc.perform(get("/api/empleados/dto/1"))
               .andExpect(status().isForbidden());
    }


    @Test
    void testFindByEmail_AsDueno_ReturnsForbidden() throws Exception {
        when(userService.findCurrentUser()).thenReturn(duenoUser);

        mockMvc.perform(get("/api/empleados/email/{email}", empleado1.getEmail()))
               .andExpect(status().isForbidden());
    }

    @Test
    void testFindByEmail_AsEmpleado_ReturnsForbidden() throws Exception {
        when(userService.findCurrentUser()).thenReturn(empleadoUser);

        mockMvc.perform(get("/api/empleados/email/{email}", empleado1.getEmail()))
               .andExpect(status().isForbidden());
    }


    @Test
    void testFindByNegocio_AsDueno_InvalidNegocio_ReturnsNotFound() throws Exception {
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(duenoService.getDuenoByUser(duenoUser.getId())).thenReturn(duenoNormal);
        when(negocioService.getById(99)).thenReturn(null);
        when(empleadoService.getEmpleadoByNegocio(99)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/empleados/negocio/99"))
               .andExpect(status().isNotFound());
    }

    @Test
    void testFindByNegocio_AsEmpleado_ReturnsForbidden() throws Exception {
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoByNegocio(1)).thenReturn(empleadosList);

        mockMvc.perform(get("/api/empleados/negocio/1"))
               .andExpect(status().isForbidden());
    }


    @Test
    void testFindByUser_AsEmpleadoSelf_ReturnsOk() throws Exception {
        empleado1.getUser().setId(empleadoUser.getId());
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoByUser(empleadoUser.getId())).thenReturn(empleado1);

        mockMvc.perform(get("/api/empleados/user/{id}", empleadoUser.getId()))
               .andExpect(status().isOk());
    }

    @Test
    void testFindByUser_AsDuenoOther_ReturnsForbidden() throws Exception {
        when(userService.findCurrentUser()).thenReturn(duenoUser);

        mockMvc.perform(get("/api/empleados/user/{id}", empleadoUser.getId()))
               .andExpect(status().isForbidden());
    }


    @Test
    void testSave_AsDueno_Success() throws Exception {
        EmpleadoDTO dto = new EmpleadoDTO();
        dto.setUsername("nuevo");
        dto.setPassword("Password1!");
        dto.setFirstName("Nuevo");
        dto.setLastName("Empleado");
        dto.setEmail("n@e.com");
        dto.setNumTelefono("666123456");
        dto.setNegocio(1);

        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(duenoService.getDuenoByUser(duenoUser.getId())).thenReturn(duenoNormal);
        when(negocioService.getById(1)).thenReturn(negocio);
        when(authorityService.findByAuthority(anyString())).thenReturn(authority);
        when(userService.saveUser(any())).thenReturn(user);
        when(empleadoService.saveEmpleado(any())).thenReturn(empleado);

        mockMvc.perform(post("/api/empleados")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
               .andExpect(status().isCreated());

        verify(empleadoService).saveEmpleado(any());
    }

    @Test
    void testFindAllDTO_NoContent_AsDueno() throws Exception {
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(duenoService.getDuenoByUser(duenoUser.getId())).thenReturn(duenoNormal);
        when(empleadoService.getEmpleadoByDueno(duenoNormal.getId())).thenReturn(Collections.emptyList());
    
        mockMvc.perform(get("/api/empleados/dto"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(0)));
    }
    

    @Test
    void testFindAllDTO_AsEmpleado_Forbidden() throws Exception {
        when(userService.findCurrentUser()).thenReturn(empleadoUser);

        mockMvc.perform(get("/api/empleados/dto"))
               .andExpect(status().isForbidden());
    }

    @Test
    void testFindByEmail_AsAdminDTO() throws Exception {
        String email = "juan.perez@example.com";
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(empleadoService.getEmpleadoByEmail(email)).thenReturn(empleado1);
    
        EmpleadoDTO dto = new EmpleadoDTO();
        dto.setEmail(email);
        dto.setFirstName("Juan");
        when(empleadoService.convertirEmpleadoDTO(empleado1))
            .thenReturn(dto);
    
        mockMvc.perform(get("/api/empleados/dto/email/{email}", email)
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.email", is(email)))
               .andExpect(jsonPath("$.firstName", is("Juan")));
    }
    

    @Test
    void testFindDTOByEmail_AsDueno_Forbidden() throws Exception {
        when(userService.findCurrentUser()).thenReturn(duenoUser);

        mockMvc.perform(get("/api/empleados/dto/email/{email}", empleado1.getEmail()))
               .andExpect(status().isForbidden());
    }

    @Test
    void testFindByNombre_AsAdminDTO() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(empleadoService.getEmpleadoByNombre("Juan")).thenReturn(Collections.singletonList(empleado1));
        EmpleadoDTO dto = new EmpleadoDTO();
        dto.setFirstName("Juan");
        when(empleadoService.convertirEmpleadoDTO(empleado1)).thenReturn(dto);
    
        mockMvc.perform(get("/api/empleados/dto/nombre/{nombre}", "Juan")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].firstName", is("Juan")));
    }
    

    @Test
    void testFindDTOByNombre_AsDueno_Forbidden() throws Exception {
        when(userService.findCurrentUser()).thenReturn(duenoUser);

        mockMvc.perform(get("/api/empleados/dto/nombre/{nombre}", "Juan"))
               .andExpect(status().isForbidden());
    }

    @Test
    void testFindByApellido_AsAdminDTO() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(empleadoService.getEmpleadoByApellido("García"))
            .thenReturn(Collections.singletonList(empleado2));
    
        EmpleadoDTO dto = new EmpleadoDTO();
        dto.setLastName("García");
        when(empleadoService.convertirEmpleadoDTO(empleado2))
            .thenReturn(dto);
    
        mockMvc.perform(get("/api/empleados/dto/apellido/{apellido}", "García")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].lastName", is("García")));
    }

    @Test
    void testFindDTOByApellido_AsEmpleado_Forbidden() throws Exception {
        when(userService.findCurrentUser()).thenReturn(empleadoUser);

        mockMvc.perform(get("/api/empleados/dto/apellido/{apellido}", "García"))
               .andExpect(status().isForbidden());
    }

    @Test
    void testFindByTelefono_AsAdminDTO() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(empleadoService.getEmpleadoByTelefono("666111222"))
            .thenReturn(empleado1);
        EmpleadoDTO dto = new EmpleadoDTO();
        dto.setNumTelefono("666111222");
        when(empleadoService.convertirEmpleadoDTO(empleado1))
            .thenReturn(dto);
    
        mockMvc.perform(get("/api/empleados/dto/telefono/{telefono}", "666111222"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.numTelefono", is("666111222")));
    }
    

    @Test
    void testFindDTOByTelefono_AsDueno_Forbidden() throws Exception {
        when(userService.findCurrentUser()).thenReturn(duenoUser);

        mockMvc.perform(get("/api/empleados/dto/telefono/{telefono}", "666111222"))
               .andExpect(status().isForbidden());
    }

    @Test
    void testFindByNegocio_AsDueno_Mismatch_Forbidden() throws Exception {
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(duenoService.getDuenoByUser(duenoUser.getId())).thenReturn(duenoNormal);
        Negocio other = new Negocio(); other.setId(2);
        other.setDueno(new Dueno()); other.getDueno().setUser(duenoUser);
        when(negocioService.getById(1)).thenReturn(other);
        when(empleadoService.getEmpleadoByNegocio(1)).thenReturn(empleadosList);

        mockMvc.perform(get("/api/empleados/negocio/{id}", 1))
               .andExpect(status().isForbidden());
    }

    @Test
    void testFindDTOByNegocio_AsDueno_Success() throws Exception {
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(duenoService.getDuenoByUser(duenoUser.getId())).thenReturn(duenoNormal);
        negocio.setDueno(duenoNormal);
        when(negocioService.getById(1)).thenReturn(negocio);
        when(empleadoService.getEmpleadoByNegocio(1)).thenReturn(empleadosList);
        when(empleadoService.convertirEmpleadoDTO(any())).thenReturn(new EmpleadoDTO());

        mockMvc.perform(get("/api/empleados/dto/negocio/{id}", 1))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void testFindByUser_AsAdmin_Success() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(empleadoService.getEmpleadoByUser(1)).thenReturn(empleado1);

        mockMvc.perform(get("/api/empleados/user/{id}", 1))
               .andExpect(status().isOk());
    }

    @Test
    void testFindByUser_AsEmpleadoOther_Forbidden() throws Exception {
        when(userService.findCurrentUser()).thenReturn(empleadoUser);

        mockMvc.perform(get("/api/empleados/user/{id}", 1))
               .andExpect(status().isForbidden());
    }

    @Test
    void testFindByTokenEmpleado_AsDueno_Forbidden() throws Exception {
        when(userService.findCurrentUser()).thenReturn(duenoUser);

        mockMvc.perform(get("/api/empleados/token/{token}", "TOKEN123"))
               .andExpect(status().isForbidden());
    }

    @Test
    void testFindDTOByTokenEmpleado_AsEmpleado_Forbidden() throws Exception {
        when(userService.findCurrentUser()).thenReturn(empleadoUser);

        mockMvc.perform(get("/api/empleados/dto/token/{token}", "TOKEN123"))
               .andExpect(status().isForbidden());
    }

    @Test
    void testUpdate_AsEmpleadoSelf_Success() throws Exception {
        EmpleadoDTO dto = new EmpleadoDTO();
        dto.setUsername("juanperez");
        dto.setPassword("Password1!");
        dto.setFirstName("Juan");
        dto.setLastName("Pérez");
        dto.setEmail("juan.perez@example.com");
        dto.setNumTelefono("666111222");
        dto.setNegocio(1);
        empleado1.getUser().setId(empleadoUser.getId());
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoById(1)).thenReturn(empleado1);
        when(negocioService.getById(1)).thenReturn(negocio);
        when(empleadoService.convertirDTOEmpleado(any())).thenReturn(empleado1);
        when(empleadoService.saveEmpleado(any())).thenReturn(empleado1);

        mockMvc.perform(put("/api/empleados/{id}", 1)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
               .andExpect(status().isOk());
    }

    @Test
    void testDelete_AsEmpleado_Forbidden() throws Exception {
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoById(1)).thenReturn(empleado1);

        mockMvc.perform(delete("/api/empleados/{id}", 1)
                .with(csrf()))
               .andExpect(status().isForbidden());
    }

    @Test
    void testFindDTOByUser_AsAdmin_Success() throws Exception {
        EmpleadoDTO dto = new EmpleadoDTO();
        dto.setFirstName("Juan");
        dto.setLastName("Pérez");
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(empleadoService.getEmpleadoByUser(1)).thenReturn(empleado1);
        when(empleadoService.convertirEmpleadoDTO(empleado1)).thenReturn(dto);

        mockMvc.perform(get("/api/empleados/dto/user/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.firstName", is("Juan")))
               .andExpect(jsonPath("$.lastName", is("Pérez")));
    }

    @Test
    void testFindDTOByUser_AsAdmin_NotFound() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(empleadoService.getEmpleadoByUser(2)).thenReturn(null);

        mockMvc.perform(get("/api/empleados/dto/user/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNotFound());
    }

    @Test
    void testFindDTOByUser_AsDueno_Forbidden() throws Exception {
        when(userService.findCurrentUser()).thenReturn(duenoUser);

        mockMvc.perform(get("/api/empleados/dto/user/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isForbidden());
    }

    @Test
    void testFindDTOByUser_AsEmpleado_Forbidden() throws Exception {
        when(userService.findCurrentUser()).thenReturn(empleadoUser);

        mockMvc.perform(get("/api/empleados/dto/user/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isForbidden());
    }

}
