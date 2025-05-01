package ispp_g2.gastrostock.testDueno;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ispp_g2.gastrostock.config.SecurityConfiguration;
import ispp_g2.gastrostock.config.jwt.JwtAuthFilter;
import ispp_g2.gastrostock.config.jwt.JwtService;
import ispp_g2.gastrostock.dueno.Dueno;
import ispp_g2.gastrostock.dueno.DuenoController;
import ispp_g2.gastrostock.dueno.DuenoDTO;
import ispp_g2.gastrostock.dueno.DuenoService;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.user.Authorities;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserService;

@WebMvcTest(DuenoController.class)
@Import({SecurityConfiguration.class, JwtAuthFilter.class})
@ActiveProfiles("test")
@WithMockUser(username = "admin", roles = {"admin"})
class DuenoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DuenoService duenoService;
    
    @MockBean 
    private UserService userService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AuthenticationProvider authenticationProvider;
    
    @MockBean
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    private Dueno duenoNormal;
    private Dueno duenoConNegocio;
    private Dueno duenoInvalidoSinEmail;
    private DuenoDTO duenoDTO;
    private DuenoDTO duenoInvalidoDTO;
    private List<Dueno> duenosList;
    private List<DuenoDTO> duenosDTOList;
    private User user;
    private Negocio negocio;
    private Authorities authority;
    
    // Usuario admin para simular la seguridad requerida
    private User adminUser;
    private Authorities adminAuthority;

    @BeforeEach
    void setUp() {
        // Inicializar ObjectMapper para manejar JSON
        objectMapper = new ObjectMapper();

        // Crear autoridad de dueño
        authority = new Authorities();
        authority.setId(1);
        authority.setAuthority("admin");
        
        // Crear usuario dueño
        user = new User();
        user.setId(1);
        user.setUsername("juangarcia");
        user.setPassword("password123");
        user.setAuthority(authority);
        
        // Crear un dueño normal
        duenoNormal = new Dueno();
        duenoNormal.setId(1);
        duenoNormal.setFirstName("Juan");
        duenoNormal.setLastName("García");
        duenoNormal.setEmail("juan@example.com");
        duenoNormal.setNumTelefono("652345678");
        duenoNormal.setTokenDueno("TOKEN123");
        duenoNormal.setUser(user);

        // Crear un negocio
        negocio = new Negocio();
        negocio.setId(1);
        negocio.setName("Restaurante La Tasca");
        negocio.setDireccion("Calle Principal 123");
        negocio.setCiudad("Sevilla");
        negocio.setPais("España");
        negocio.setCodigoPostal("41001");
        negocio.setTokenNegocio(12345);

        // Crear un dueño con negocio
        duenoConNegocio = new Dueno();
        duenoConNegocio.setId(2);
        duenoConNegocio.setFirstName("Ana");
        duenoConNegocio.setLastName("Martínez");
        duenoConNegocio.setEmail("ana@example.com");
        duenoConNegocio.setNumTelefono("654321987");
        duenoConNegocio.setTokenDueno("TOKEN456");
        
        User user2 = new User();
        user2.setId(2);
        user2.setUsername("anamartinez");
        user2.setPassword("password456");
        user2.setAuthority(authority);
        duenoConNegocio.setUser(user2);
        negocio.setDueno(duenoConNegocio);

        // Crear un dueño con datos inválidos (sin email)
        duenoInvalidoSinEmail = new Dueno();
        duenoInvalidoSinEmail.setId(3);
        duenoInvalidoSinEmail.setFirstName("Pedro");
        duenoInvalidoSinEmail.setLastName("Pérez");
        duenoInvalidoSinEmail.setNumTelefono("666777888");
        duenoInvalidoSinEmail.setTokenDueno("TOKEN789");
        
        User user3 = new User();
        user3.setId(3);
        user3.setUsername("pedroperez");
        user3.setPassword("password789");
        user3.setAuthority(authority);
        duenoInvalidoSinEmail.setUser(user3);

        // Lista de dueños para tests
        duenosList = new ArrayList<>();
        duenosList.add(duenoNormal);
        duenosList.add(duenoConNegocio);
        
        // Crear DTOs válidos e inválidos
        duenoDTO = new DuenoDTO();
        duenoDTO.setFirstName("Juan");
        duenoDTO.setLastName("García");
        duenoDTO.setEmail("juan@example.com");
        duenoDTO.setNumTelefono("652345678");
        duenoDTO.setTokenDueno("TOKEN123");
        duenoDTO.setUsername("juangarcia");
        duenoDTO.setPassword("password123");
        
        duenoInvalidoDTO = new DuenoDTO();
        // Sin campos requeridos
        
        duenosDTOList = new ArrayList<>();
        duenosDTOList.add(duenoDTO);
        
        // Configurar comportamiento básico del servicio de dueño
        when(duenoService.convertirDuenoDTO(any(Dueno.class))).thenReturn(duenoDTO);
        when(duenoService.convertirDTODueno(any(DuenoDTO.class))).thenReturn(duenoNormal);
        when(jwtService.getUserNameFromJwtToken(anyString())).thenReturn("admin");

        // Configurar un usuario administrador para sortear la seguridad
        adminAuthority = new Authorities();
        adminAuthority.setId(100);
        adminAuthority.setAuthority("admin");
        
        adminUser = new User();
        adminUser.setId(999);
        adminUser.setUsername("admin");
        adminUser.setPassword("adminpass");
        adminUser.setAuthority(adminAuthority);

        // Configurar que el usuario actual (findCurrentUser) sea admin
        when(userService.findCurrentUser()).thenReturn(adminUser);
    }

    /* TESTS PARA findAll() */
    
    @Test
    void testFindAll_Success() throws Exception {
        // Arrange
        when(duenoService.getAllDuenos()).thenReturn(duenosList);
        
        // Act & Assert
        mockMvc.perform(get("/api/duenos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("Juan")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].firstName", is("Ana")));
        
        verify(duenoService, times(2)).getAllDuenos();
    }

    @Test
    void testFindAll_EmptyList() throws Exception {
        // Arrange
        when(duenoService.getAllDuenos()).thenReturn(Collections.emptyList());
        
        // Act & Assert
        mockMvc.perform(get("/api/duenos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        
        verify(duenoService).getAllDuenos();
    }

    /* TESTS PARA findAllDTO() */
    
    @Test
    void testFindAllDTO_Success() throws Exception {
        // Arrange
        when(duenoService.getAllDuenos()).thenReturn(duenosList);
        
        // Act & Assert
        mockMvc.perform(get("/api/duenos/dto")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
        
        verify(duenoService, times(2)).getAllDuenos();
        verify(duenoService, times(2)).convertirDuenoDTO(any(Dueno.class));
    }

    @Test
    void testFindAllDTO_EmptyList() throws Exception {
        // Arrange
        when(duenoService.getAllDuenos()).thenReturn(Collections.emptyList());
        
        // Act & Assert
        mockMvc.perform(get("/api/duenos/dto")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        
        verify(duenoService).getAllDuenos();
        verify(duenoService, never()).convertirDuenoDTO(any(Dueno.class));
    }

    /* TESTS PARA findById() */
    
    @Test
    void testFindById_Success() throws Exception {
        // Arrange
        when(duenoService.getDuenoById(1)).thenReturn(duenoNormal);
        
        // Act & Assert
        mockMvc.perform(get("/api/duenos/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Juan")))
                .andExpect(jsonPath("$.lastName", is("García")))
                .andExpect(jsonPath("$.email", is("juan@example.com")));
        
        verify(duenoService).getDuenoById(1);
    }

    @Test
    void testFindById_NotFound() throws Exception {
        // Arrange
        when(duenoService.getDuenoById(999)).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(get("/api/duenos/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(duenoService).getDuenoById(999);
    }

    /* TESTS PARA findDTOById() */
    
    @Test
    void testFindDTOById_Success() throws Exception {
        // Arrange
        when(duenoService.getDuenoById(1)).thenReturn(duenoNormal);
        when(duenoService.convertirDuenoDTO(duenoNormal)).thenReturn(duenoDTO);
        
        // Act & Assert
        mockMvc.perform(get("/api/duenos/dto/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Juan")))
                .andExpect(jsonPath("$.lastName", is("García")))
                .andExpect(jsonPath("$.email", is("juan@example.com")));
        
        verify(duenoService).getDuenoById(1);
        verify(duenoService).convertirDuenoDTO(duenoNormal);
    }

    @Test
    void testFindDTOById_NotFound() throws Exception {
        // Arrange
        when(duenoService.getDuenoById(999)).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(get("/api/duenos/dto/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(duenoService).getDuenoById(999);
        verify(duenoService, never()).convertirDuenoDTO(any(Dueno.class));
    }

    /* TESTS PARA findByToken() */
    
    @Test
    void testFindByToken_Success() throws Exception {
        // Arrange
        when(duenoService.getDuenoByToken("TOKEN123")).thenReturn(duenoNormal);
        
        // Act & Assert
        mockMvc.perform(get("/api/duenos/token/TOKEN123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Juan")));
        
        verify(duenoService, times(2)).getDuenoByToken("TOKEN123");
    }

    @Test
    void testFindByToken_NotFound() throws Exception {
        // Arrange
        when(duenoService.getDuenoByToken("NONEXISTENT")).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(get("/api/duenos/token/NONEXISTENT")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(duenoService).getDuenoByToken("NONEXISTENT");
    }

    /* TESTS PARA findByEmail() */
    
    @Test
    void testFindByEmail_Success() throws Exception {
        // Arrange
        when(duenoService.getDuenoByEmail("juan@example.com")).thenReturn(duenoNormal);
        
        // Act & Assert
        mockMvc.perform(get("/api/duenos/email/juan@example.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Juan")));
        
        verify(duenoService, times(2)).getDuenoByEmail("juan@example.com");
    }

    @Test
    void testFindByEmail_NotFound() throws Exception {
        // Arrange
        when(duenoService.getDuenoByEmail("nonexistent@example.com")).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(get("/api/duenos/email/nonexistent@example.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(duenoService).getDuenoByEmail("nonexistent@example.com");
    }

    /* TESTS PARA findByNombre() */
    
    @Test
    void testFindByNombre_Success() throws Exception {
        // Arrange
        List<Dueno> duenosJuan = List.of(duenoNormal);
        when(duenoService.getDuenoByNombre("Juan")).thenReturn(duenosJuan);
        
        // Act & Assert
        mockMvc.perform(get("/api/duenos/nombre/Juan")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("Juan")));
        
        verify(duenoService).getDuenoByNombre("Juan");
    }

    @Test
    void testFindByNombre_NotFound() throws Exception {
        // Arrange
        when(duenoService.getDuenoByNombre("Nonexistent")).thenReturn(Collections.emptyList());
        
        // Act & Assert
        mockMvc.perform(get("/api/duenos/nombre/Nonexistent")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(duenoService).getDuenoByNombre("Nonexistent");
    }

    /* TESTS PARA findByApellido() */
    
    @Test
    void testFindByApellido_Success() throws Exception {
        // Arrange
        List<Dueno> duenosGarcia = List.of(duenoNormal);
        when(duenoService.getDuenoByApellido("García")).thenReturn(duenosGarcia);
        
        // Act & Assert
        mockMvc.perform(get("/api/duenos/apellido/García")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].lastName", is("García")));
        
        verify(duenoService, times(2)).getDuenoByApellido("García");
    }

    @Test
    void testFindByApellido_NotFound() throws Exception {
        // Arrange
        when(duenoService.getDuenoByApellido("Nonexistent")).thenReturn(Collections.emptyList());
        
        // Act & Assert
        mockMvc.perform(get("/api/duenos/apellido/Nonexistent")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(duenoService).getDuenoByApellido("Nonexistent");
    }

    /* TESTS PARA findByTelefono() */
    
    @Test
    void testFindByTelefono_Success() throws Exception {
        // Arrange
        when(duenoService.getDuenoByTelefono("652345678")).thenReturn(duenoNormal);
        
        // Act & Assert
        mockMvc.perform(get("/api/duenos/telefono/652345678")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.numTelefono", is("652345678")));
        
        verify(duenoService, times(2)).getDuenoByTelefono("652345678");
    }

    @Test
    void testFindByTelefono_NotFound() throws Exception {
        // Arrange
        when(duenoService.getDuenoByTelefono("999999999")).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(get("/api/duenos/telefono/999999999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(duenoService).getDuenoByTelefono("999999999");
    }

    /* TESTS PARA findByUser() */
    
    @Test
    void testFindByUser_Success() throws Exception {
        // Arrange
        when(duenoService.getDuenoByUser(1)).thenReturn(duenoNormal);
        
        // Act & Assert
        mockMvc.perform(get("/api/duenos/user/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Juan")));
        
        verify(duenoService, times(2)).getDuenoByUser(1);
    }

    @Test
    void testFindByUser_NotFound() throws Exception {
        // Arrange
        when(duenoService.getDuenoByUser(999)).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(get("/api/duenos/user/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(duenoService).getDuenoByUser(999);
    }

    /* TESTS PARA save() */
    
    @Test
    void testSave_Success() throws Exception {
        // Arrange
        when(userService.findUserByUsername(anyString())).thenReturn(null);
        when(duenoService.saveDueno(any(Dueno.class))).thenReturn(duenoNormal);
        
        // Act & Assert
        mockMvc.perform(post("/api/duenos")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duenoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Juan")));
        
        verify(duenoService).convertirDTODueno(any(DuenoDTO.class));
        verify(userService).findUserByUsername(anyString());
        verify(duenoService).saveDueno(any(Dueno.class));
    }

    @Test
    void testSave_UsernameAlreadyExists() throws Exception {
        // Arrange
        when(userService.findUserByUsername(anyString())).thenReturn(user);
        
        // Act & Assert
        mockMvc.perform(post("/api/duenos")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duenoDTO)))
                .andExpect(status().isConflict());
        
        verify(duenoService).convertirDTODueno(any(DuenoDTO.class));
        verify(userService).findUserByUsername(anyString());
        verify(duenoService, never()).saveDueno(any(Dueno.class));
    }
    
    @Test
    void testSave_ValidationError() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/duenos")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duenoInvalidoDTO)))
                .andExpect(status().isBadRequest());
        
        verify(duenoService, never()).saveDueno(any(Dueno.class));
    }

    /* TESTS PARA update() */
    
    @Test
    void testUpdate_Success() throws Exception {
        // Arrange
        when(duenoService.getDuenoById(1)).thenReturn(duenoNormal);
        when(userService.findUserByUsername(anyString())).thenReturn(null);
        
        Dueno duenoActualizado = new Dueno();
        duenoActualizado.setId(1);
        duenoActualizado.setFirstName("Juan Actualizado");
        duenoActualizado.setLastName("García");
        duenoActualizado.setEmail("juan@example.com");
        duenoActualizado.setNumTelefono("652345678");
        duenoActualizado.setTokenDueno("TOKEN123");
        duenoActualizado.setUser(user);
        
        when(duenoService.saveDueno(any(Dueno.class))).thenReturn(duenoActualizado);
        
        // Asegurar que todos los campos requeridos estén presentes
        DuenoDTO duenoActualizadoDTO = new DuenoDTO();
        duenoActualizadoDTO.setFirstName("Juan Actualizado");
        duenoActualizadoDTO.setLastName("García");
        duenoActualizadoDTO.setEmail("juan@example.com");
        duenoActualizadoDTO.setNumTelefono("652345678");
        duenoActualizadoDTO.setTokenDueno("TOKEN123");
        duenoActualizadoDTO.setUsername("juangarcia");
        duenoActualizadoDTO.setPassword("password123");
        
        when(duenoService.convertirDTODueno(any(DuenoDTO.class))).thenReturn(duenoActualizado);
        
        // Act & Assert
        mockMvc.perform(put("/api/duenos/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duenoActualizadoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Juan Actualizado")));
        
        verify(duenoService, times(2)).getDuenoById(1);
        verify(duenoService).convertirDTODueno(any(DuenoDTO.class));
        verify(duenoService).saveDueno(any(Dueno.class));
    }
    
    @Test
    void testUpdate_NotFound() throws Exception {
        // Arrange
        when(duenoService.getDuenoById(999)).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(put("/api/duenos/999")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duenoDTO)))
                .andExpect(status().isNotFound());
        
        verify(duenoService).getDuenoById(999);
        verify(duenoService, never()).saveDueno(any(Dueno.class));
    }
    
    @Test
    void testUpdate_UsernameConflict() throws Exception {
        // Arrange
        when(duenoService.getDuenoById(1)).thenReturn(duenoNormal);
        
        // Simular que el username ya está en uso pero por otro usuario
        User conflictUser = new User();
        conflictUser.setId(5);
        when(userService.findUserByUsername(anyString())).thenReturn(conflictUser);
        
        // Act & Assert
        mockMvc.perform(put("/api/duenos/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duenoDTO)))
                .andExpect(status().isConflict());
        
        verify(duenoService, times(2)).getDuenoById(1);
        verify(userService).findUserByUsername(anyString());
        verify(duenoService, never()).saveDueno(any(Dueno.class));
    }
    
    @Test
    void testUpdate_ValidationError() throws Exception {
        // Act & Assert
        mockMvc.perform(put("/api/duenos/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duenoInvalidoDTO)))
                .andExpect(status().isBadRequest());
        
        verify(duenoService, never()).saveDueno(any(Dueno.class));
    }

    /* TESTS PARA delete() */
    
    @Test
    void testDelete_Success() throws Exception {
        // Arrange
        when(duenoService.getDuenoById(1)).thenReturn(duenoNormal);
        doNothing().when(duenoService).deleteDueno(1);
        
        // Act & Assert
        mockMvc.perform(delete("/api/duenos/1")
                .with(csrf()))
                .andExpect(status().isNoContent());
        
        verify(duenoService).getDuenoById(1);
        verify(duenoService).deleteDueno(1);
    }

    @Test
    void testDelete_NotFound() throws Exception {
        // Arrange
        when(duenoService.getDuenoById(999)).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(delete("/api/duenos/999")
                .with(csrf()))
                .andExpect(status().isNotFound());
        
        verify(duenoService).getDuenoById(999);
        verify(duenoService, never()).deleteDueno(anyInt());
    }
}
