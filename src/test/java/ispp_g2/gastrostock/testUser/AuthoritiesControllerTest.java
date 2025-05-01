package ispp_g2.gastrostock.testUser;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import ispp_g2.gastrostock.config.jwt.JwtService;
import ispp_g2.gastrostock.user.Authorities;
import ispp_g2.gastrostock.user.AuthoritiesController;
import ispp_g2.gastrostock.user.AuthoritiesService;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
@ActiveProfiles("test")
@WebMvcTest(AuthoritiesController.class)
@WithMockUser(username = "admin", roles = {"admin"})
class AuthoritiesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthoritiesService authoritiesService;
    
    @MockBean
    private UserService userService; 

    @MockBean
    private JwtService jwtService;

    private Authorities auth1;
    private Authorities auth2;
    private User user2;
    private List<Authorities> authList;

    @BeforeEach
    void setUp() {
        // Configurar user2 (usuario admin)
        user2 = new User();
        user2.setId(2);
        user2.setUsername("admin");
        user2.setPassword("adminpass");
        Authorities authAdmin = new Authorities();
        authAdmin.setId(2);
        authAdmin.setAuthority("admin");
        user2.setAuthority(authAdmin);

        // Configurar authorities de prueba:
        auth1 = new Authorities();
        auth1.setId(1);
        auth1.setAuthority("ROLE_USER");

        auth2 = new Authorities();
        auth2.setId(2);
        auth2.setAuthority("ROLE_ADMIN");

        authList = Arrays.asList(auth1, auth2);
        
        // Stub: findCurrentUser debe devolver user2
        lenient().when(userService.findCurrentUser()).thenReturn(user2);
    }

    @Test
    void testFindAll_WithContent() throws Exception {
        when(authoritiesService.findAll()).thenReturn(authList);
        
        mockMvc.perform(get("/api/authorities")
                .header("Authorization", "Bearer dummyToken")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(jsonPath("$[0].authority", is("ROLE_USER")))
               .andExpect(jsonPath("$[1].authority", is("ROLE_ADMIN")));
        
        // Nota: Si el controller llama a findAll() dos veces, se puede verificar con times(2)
        verify(authoritiesService, atLeastOnce()).findAll();
    }

    
    @Test
    void testFindAll_NoContent() throws Exception {
        // Stub: Devuelve lista vacía.
        when(authoritiesService.findAll()).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/api/authorities")
                .header("Authorization", "Bearer dummyToken")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNoContent());
        
        verify(authoritiesService, times(1)).findAll();
    }
    
    
    @Test
    void testFindById_Found() throws Exception {
        when(authoritiesService.findById(1)).thenReturn(auth1);
        
        mockMvc.perform(get("/api/authorities/1")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id", is(1)))
               .andExpect(jsonPath("$.authority", is("ROLE_USER")));
        
        verify(authoritiesService).findById(1);
    }
    
    @Test
    void testFindById_NotFound() throws Exception {
        when(authoritiesService.findById(999)).thenReturn(null);
        
        mockMvc.perform(get("/api/authorities/999")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNotFound());
        
        verify(authoritiesService).findById(999);
    }
    
    
    @Test
    void testFindByAuthority_Found() throws Exception {
        when(authoritiesService.findByAuthority("ROLE_ADMIN")).thenReturn(auth2);
        
        mockMvc.perform(get("/api/authorities/authority/ROLE_ADMIN")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id", is(2)))
               .andExpect(jsonPath("$.authority", is("ROLE_ADMIN")));
        
        verify(authoritiesService).findByAuthority("ROLE_ADMIN");
    }
    
    @Test
    void testFindByAuthority_NotFound() throws Exception {
        when(authoritiesService.findByAuthority("NON_EXISTENT")).thenReturn(null);
        
        mockMvc.perform(get("/api/authorities/authority/NON_EXISTENT")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNotFound());
        
        verify(authoritiesService).findByAuthority("NON_EXISTENT");
    }
    
    

    @Test
    void testSaveAuthorities_Success() throws Exception {
        when(userService.findCurrentUser()).thenReturn(user2);
        Authorities newAuth = new Authorities();
        newAuth.setAuthority("admin");
        
        Authorities savedAuth = new Authorities();
        savedAuth.setId(3);
        savedAuth.setAuthority("admin");
        
        when(authoritiesService.saveAuthorities(any(Authorities.class))).thenReturn(savedAuth);
        
        mockMvc.perform(post("/api/authorities")
                .with(csrf())  
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newAuth)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id", is(3)))
               .andExpect(jsonPath("$.authority", is("admin")));
        
        verify(authoritiesService).saveAuthorities(any(Authorities.class));
    }
 /*     
    @Test
    void testSaveAuthorities_NullAuthorities() throws Exception {
        when(userService.findCurrentUser()).thenReturn(user2);
        String nullJson = "null";
        mockMvc.perform(post("/api/authorities")
                .with(csrf())  
                .contentType(MediaType.APPLICATION_JSON)
                .content(nullJson))
               .andExpect(status().isBadRequest());  // Ahora debería ser 400, dado que el cuerpo es inválido
    
        verify(authoritiesService, never()).saveAuthorities(any(Authorities.class));
    }
    */   
    
    @Test
    void testUpdateAuthorities_Success() throws Exception {

        Authorities updatedAuth = new Authorities();
        updatedAuth.setAuthority("ROLE_UPDATED");
        
        Authorities existingAuth = auth1; // auth1 con id 1 y "ROLE_USER"
        Authorities savedUpdatedAuth = new Authorities();
        savedUpdatedAuth.setId(1);
        savedUpdatedAuth.setAuthority("ROLE_UPDATED");
        when(userService.findCurrentUser()).thenReturn(user2);
        when(authoritiesService.findById(1)).thenReturn(existingAuth);
        when(authoritiesService.saveAuthorities(any(Authorities.class))).thenReturn(savedUpdatedAuth);
        
        mockMvc.perform(put("/api/authorities/1")
                .with(csrf()) 
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedAuth)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id", is(1)))
               .andExpect(jsonPath("$.authority", is("ROLE_UPDATED")));
        
        verify(authoritiesService).findById(1);
        verify(authoritiesService).saveAuthorities(any(Authorities.class));
    }
    
    @Test
    void testUpdateAuthorities_NotFound() throws Exception {
        Authorities updatedAuth = new Authorities();
        updatedAuth.setAuthority("ROLE_UPDATED");
        
        when(authoritiesService.findById(1)).thenReturn(null);
        when(userService.findCurrentUser()).thenReturn(user2);

        mockMvc.perform(put("/api/authorities/1")
                .with(csrf()) 
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedAuth)))
               .andExpect(status().isNotFound());
        
        verify(authoritiesService).findById(1);
        verify(authoritiesService, never()).saveAuthorities(any(Authorities.class));
    }
    
    
    @Test
    void testDeleteAuthorities_Success() throws Exception {
        when(authoritiesService.findById(1)).thenReturn(auth1);
        when(userService.findCurrentUser()).thenReturn(user2);

        mockMvc.perform(delete("/api/authorities/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNoContent());
        
        verify(authoritiesService).findById(1);
        verify(authoritiesService).delete(1);
    }
    
    @Test
    void testDeleteAuthorities_NotFound() throws Exception {
        when(userService.findCurrentUser()).thenReturn(user2);
        when(authoritiesService.findById(999)).thenReturn(null);
        
        mockMvc.perform(delete("/api/authorities/999")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNotFound());
        
        verify(authoritiesService).findById(999);
        verify(authoritiesService, never()).delete(anyInt());
    }
}
