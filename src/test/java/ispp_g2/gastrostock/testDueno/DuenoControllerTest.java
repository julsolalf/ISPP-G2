package ispp_g2.gastrostock.testDueno;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import org.springframework.security.crypto.password.PasswordEncoder;
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
import ispp_g2.gastrostock.exceptions.BadRequestException;
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
    private PasswordEncoder passwordEncoder;

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
    
    private User adminUser;
    private Authorities adminAuthority;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        authority = new Authorities();
        authority.setId(1);
        authority.setAuthority("admin");
        
        user = new User();
        user.setId(1);
        user.setUsername("juangarcia");
        user.setPassword("password123");
        user.setAuthority(authority);
        
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
        negocio.setName("Restaurante La Tasca");
        negocio.setDireccion("Calle Principal 123");
        negocio.setCiudad("Sevilla");
        negocio.setPais("España");
        negocio.setCodigoPostal("41001");
        negocio.setTokenNegocio(12345);

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

        duenosList = new ArrayList<>();
        duenosList.add(duenoNormal);
        duenosList.add(duenoConNegocio);
        
        duenoDTO = new DuenoDTO();
        duenoDTO.setFirstName("Juan");
        duenoDTO.setLastName("García");
        duenoDTO.setEmail("juan@example.com");
        duenoDTO.setNumTelefono("652345678");
        duenoDTO.setTokenDueno("TOKEN123");
        duenoDTO.setUsername("juangarcia");
        duenoDTO.setPassword("Password1!");
        
        duenoInvalidoDTO = new DuenoDTO();
        
        duenosDTOList = new ArrayList<>();
        duenosDTOList.add(duenoDTO);
        
        when(duenoService.convertirDuenoDTO(any(Dueno.class))).thenReturn(duenoDTO);
        when(duenoService.convertirDTODueno(any(DuenoDTO.class))).thenReturn(duenoNormal);
        when(jwtService.getUserNameFromJwtToken(anyString())).thenReturn("admin");

        adminAuthority = new Authorities();
        adminAuthority.setId(100);
        adminAuthority.setAuthority("admin");
        
        adminUser = new User();
        adminUser.setId(999);
        adminUser.setUsername("admin");
        adminUser.setPassword("adminpass");
        adminUser.setAuthority(adminAuthority);

        when(userService.findCurrentUser()).thenReturn(adminUser);
    }

    
    @Test
    void testFindAll_Success() throws Exception {
        when(duenoService.getAllDuenos()).thenReturn(duenosList);
        
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
        when(duenoService.getAllDuenos()).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/api/duenos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        
        verify(duenoService).getAllDuenos();
    }

    
    @Test
    void testFindAllDTO_Success() throws Exception {
        when(duenoService.getAllDuenos()).thenReturn(duenosList);
        
        mockMvc.perform(get("/api/duenos/dto")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
        
        verify(duenoService, times(2)).getAllDuenos();
        verify(duenoService, times(2)).convertirDuenoDTO(any(Dueno.class));
    }

    @Test
    void testFindAllDTO_EmptyList() throws Exception {
        when(duenoService.getAllDuenos()).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/api/duenos/dto")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        
        verify(duenoService).getAllDuenos();
        verify(duenoService, never()).convertirDuenoDTO(any(Dueno.class));
    }

    
    @Test
    void testFindById_Success() throws Exception {
        when(duenoService.getDuenoById(1)).thenReturn(duenoNormal);
        
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
        when(duenoService.getDuenoById(999)).thenReturn(null);
        
        mockMvc.perform(get("/api/duenos/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(duenoService).getDuenoById(999);
    }

    
    @Test
    void testFindDTOById_Success() throws Exception {
        when(duenoService.getDuenoById(1)).thenReturn(duenoNormal);
        when(duenoService.convertirDuenoDTO(duenoNormal)).thenReturn(duenoDTO);
        
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
        when(duenoService.getDuenoById(999)).thenReturn(null);
        
        mockMvc.perform(get("/api/duenos/dto/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(duenoService).getDuenoById(999);
        verify(duenoService, never()).convertirDuenoDTO(any(Dueno.class));
    }

    
    @Test
    void testFindByToken_Success() throws Exception {
        when(duenoService.getDuenoByToken("TOKEN123")).thenReturn(duenoNormal);
        
        mockMvc.perform(get("/api/duenos/token/TOKEN123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Juan")));
        
        verify(duenoService, times(2)).getDuenoByToken("TOKEN123");
    }

    @Test
    void testFindByToken_NotFound() throws Exception {
        when(duenoService.getDuenoByToken("NONEXISTENT")).thenReturn(null);
        
        mockMvc.perform(get("/api/duenos/token/NONEXISTENT")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(duenoService).getDuenoByToken("NONEXISTENT");
    }

    
    @Test
    void testFindByEmail_Success() throws Exception {
        when(duenoService.getDuenoByEmail("juan@example.com")).thenReturn(duenoNormal);
        
        mockMvc.perform(get("/api/duenos/email/juan@example.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Juan")));
        
        verify(duenoService, times(2)).getDuenoByEmail("juan@example.com");
    }

    @Test
    void testFindByEmail_NotFound() throws Exception {
        when(duenoService.getDuenoByEmail("nonexistent@example.com")).thenReturn(null);
        
        mockMvc.perform(get("/api/duenos/email/nonexistent@example.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(duenoService).getDuenoByEmail("nonexistent@example.com");
    }

    
    @Test
    void testFindByNombre_Success() throws Exception {
        List<Dueno> duenosJuan = List.of(duenoNormal);
        when(duenoService.getDuenoByNombre("Juan")).thenReturn(duenosJuan);
        
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
        when(duenoService.getDuenoByNombre("Nonexistent")).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/api/duenos/nombre/Nonexistent")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(duenoService).getDuenoByNombre("Nonexistent");
    }

    
    @Test
    void testFindByApellido_Success() throws Exception {
        List<Dueno> duenosGarcia = List.of(duenoNormal);
        when(duenoService.getDuenoByApellido("García")).thenReturn(duenosGarcia);
        
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
        when(duenoService.getDuenoByApellido("Nonexistent")).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/api/duenos/apellido/Nonexistent")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(duenoService).getDuenoByApellido("Nonexistent");
    }

    
    @Test
    void testFindByTelefono_Success() throws Exception {
        when(duenoService.getDuenoByTelefono("652345678")).thenReturn(duenoNormal);
        
        mockMvc.perform(get("/api/duenos/telefono/652345678")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.numTelefono", is("652345678")));
        
        verify(duenoService, times(2)).getDuenoByTelefono("652345678");
    }

    @Test
    void testFindByTelefono_NotFound() throws Exception {
        when(duenoService.getDuenoByTelefono("999999999")).thenReturn(null);
        
        mockMvc.perform(get("/api/duenos/telefono/999999999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(duenoService).getDuenoByTelefono("999999999");
    }

    
    @Test
    void testFindByUser_Success() throws Exception {
        when(duenoService.getDuenoByUser(1)).thenReturn(duenoNormal);
        
        mockMvc.perform(get("/api/duenos/user/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Juan")));
        
        verify(duenoService, times(2)).getDuenoByUser(1);
    }

    @Test
    void testFindByUser_NotFound() throws Exception {
        when(duenoService.getDuenoByUser(999)).thenReturn(null);
        
        mockMvc.perform(get("/api/duenos/user/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(duenoService).getDuenoByUser(999);
    }

    
    @Test
    void testSave_Success() throws Exception {
        when(userService.findUserByUsername(anyString())).thenReturn(null);
        when(duenoService.saveDueno(any(Dueno.class))).thenReturn(duenoNormal);
        
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
        when(userService.findUserByUsername(anyString())).thenReturn(user);
        
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
        mockMvc.perform(post("/api/duenos")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duenoInvalidoDTO)))
                .andExpect(status().isBadRequest());
        
        verify(duenoService, never()).saveDueno(any(Dueno.class));
    }

    @Test
    void testUpdate_ValidationError() throws Exception {
        mockMvc.perform(put("/api/duenos/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duenoInvalidoDTO)))
                .andExpect(status().isBadRequest());
        
        verify(duenoService, never()).saveDueno(any(Dueno.class));
    }

    
    @Test
    void testDelete_Success() throws Exception {
        when(duenoService.getDuenoById(1)).thenReturn(duenoNormal);
        doNothing().when(duenoService).deleteDueno(1);
        
        mockMvc.perform(delete("/api/duenos/1")
                .with(csrf()))
                .andExpect(status().isNoContent());
        
        verify(duenoService).getDuenoById(1);
        verify(duenoService).deleteDueno(1);
    }

    @Test
    void testDelete_NotFound() throws Exception {
        when(duenoService.getDuenoById(999)).thenReturn(null);
        
        mockMvc.perform(delete("/api/duenos/999")
                .with(csrf()))
                .andExpect(status().isNotFound());
        
        verify(duenoService).getDuenoById(999);
        verify(duenoService, never()).deleteDueno(anyInt());
    }

    @Test
    void update_successfulUpdate_returns200() throws Exception {
        when(duenoService.getDuenoById(1)).thenReturn(duenoNormal);
        when(userService.findUserByUsername("juangarcia")).thenReturn(null);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        mockMvc.perform(put("/api/duenos/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duenoDTO)))
            .andExpect(status().isOk());

        verify(userService).updateUser(eq(user.getId()), any(User.class));
        verify(duenoService).updateDueno(eq(1), any(Dueno.class));
        verify(duenoService, times(2)).getDuenoById(1);
    }

    @Test
    void update_nullDto_throwsIllegalArgumentException() throws Exception {
        mockMvc.perform(put("/api/duenos/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
            .andExpect(status().isBadRequest()); 
    }

    @Test
    void update_invalidPassword_throwsBadRequest() throws Exception {
        duenoDTO.setPassword("bad");      
        duenoDTO.setNumTelefono("652345678");
        when(duenoService.getDuenoById(1)).thenReturn(duenoNormal);
    
        mockMvc.perform(put("/api/duenos/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duenoDTO)))
            .andExpect(status().isInternalServerError())
            .andExpect(result -> {
                Throwable ex = result.getResolvedException();
                assertNotNull(ex);
                assertTrue(ex instanceof BadRequestException);
                assertEquals(
                    "La contraseña debe tener entre 8 y 32 caracteres, 1 mayúscula, " +
                    "1 minúscula, un número y un caracter especial",
                    ex.getMessage()
                );
            });
    }
    

    @Test
    void update_invalidPhone_throwsBadRequest() throws Exception {
        duenoDTO.setPassword("Password1!");
        duenoDTO.setNumTelefono("abc"); // inválido
        when(duenoService.getDuenoById(1)).thenReturn(duenoNormal);

        mockMvc.perform(put("/api/duenos/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duenoDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void update_usernameAlreadyUsed_throwsBadRequest() throws Exception {
        when(duenoService.getDuenoById(1)).thenReturn(duenoNormal);
    
        User otherUser = new User();
        otherUser.setId(99);
        when(userService.findUserByUsernameNull("juangarcia"))
            .thenReturn(otherUser);
    
        mockMvc.perform(put("/api/duenos/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duenoDTO)))
            .andExpect(status().isInternalServerError())
            .andExpect(result -> {
                Throwable ex = result.getResolvedException();
                assertNotNull(ex);
                assertTrue(ex instanceof BadRequestException);
                assertEquals("El nombre de usuario ya está en uso", ex.getMessage());
            });
    }
    

    @Test
    void update_userWithoutPermission_returnsForbidden() throws Exception {
          

        User nonAdminUser = new User();
        nonAdminUser.setId(99);
        Authorities duenoAuth = new Authorities();
        duenoAuth.setAuthority("dueno");
        nonAdminUser.setAuthority(duenoAuth);
        when(userService.findCurrentUser()).thenReturn(nonAdminUser);
    
        when(duenoService.getDuenoById(1)).thenReturn(duenoNormal);
    
        mockMvc.perform(put("/api/duenos/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duenoDTO)))
            .andExpect(status().isForbidden());
    }   

}
