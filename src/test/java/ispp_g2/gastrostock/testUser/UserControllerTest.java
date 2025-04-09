package ispp_g2.gastrostock.testUser;

import com.fasterxml.jackson.databind.ObjectMapper;

import ispp_g2.gastrostock.user.Authorities;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserController;
import ispp_g2.gastrostock.user.UserService;

import java.util.Arrays;
import java.util.Collections;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

abstract class UserMixin {
    @JsonIgnore
    public abstract Collection<?> getAuthorities();
}


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;
    
    @Mock
    private UserService userService;
    
    @InjectMocks
    private UserController userController;
    
    private ObjectMapper objectMapper;
    
    // Usuarios de ejemplo y autoridades
    private User user1;
    private User user2;
    private Authorities authUser;
    private Authorities authAdmin;
    
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
        // Registra el mixin para ignorar la propiedad authorities
        objectMapper.addMixIn(User.class, UserMixin.class);

        // Configuración de autoridades
        authUser = new Authorities();
        authUser.setId(1);
        authUser.setAuthority("user");

        authAdmin = new Authorities();
        authAdmin.setId(2);
        authAdmin.setAuthority("admin");

        // Configuración de usuarios
        user1 = new User();
        user1.setId(1);
        user1.setUsername("johndoe");
        user1.setPassword("password123");
        user1.setAuthority(authUser);
        
        user2 = new User();
        user2.setId(2);
        user2.setUsername("admin");
        user2.setPassword("adminpass");
        user2.setAuthority(authAdmin);
    }
    
    @Test
    void testFindAll_NonEmpty() throws Exception {
        List<User> userList = Arrays.asList(user1, user2);
        when(userService.findAll()).thenReturn(userList);
        
        mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(jsonPath("$[0].username", is("johndoe")))
               .andExpect(jsonPath("$[1].username", is("admin")));
        
        verify(userService, atLeastOnce()).findAll();
    }
    
    @Test
    void testFindAll_Empty() throws Exception {
        when(userService.findAll()).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNoContent());
        
        verify(userService, atLeastOnce()).findAll();
    }
    
    @Test
    void testFindById_Found() throws Exception {
        when(userService.findUserById(1)).thenReturn(user1);
        
        mockMvc.perform(get("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.username", is("johndoe")));
        
        verify(userService).findUserById(1);
    }
    
    @Test
    void testFindById_NotFound() throws Exception {
        when(userService.findUserById(999)).thenReturn(null);
        
        mockMvc.perform(get("/api/users/999")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNotFound());
        
        verify(userService).findUserById(999);
    }
    
    @Test
    void testFindByUsername_Found() throws Exception {
        when(userService.findUserByUsername("johndoe")).thenReturn(user1);
        
        mockMvc.perform(get("/api/users/username/johndoe")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.username", is("johndoe")));
        
        verify(userService).findUserByUsername("johndoe");
    }
    
    @Test
    void testFindByUsername_NotFound() throws Exception {
        when(userService.findUserByUsername("nonexistent")).thenReturn(null);
        
        mockMvc.perform(get("/api/users/username/nonexistent")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNotFound());
        
        verify(userService).findUserByUsername("nonexistent");
    }
    
    @Test
    void testFindByAuthority_Found() throws Exception {
        // Suponemos que findUserByAuthority devuelve un usuario (aunque en un diseño real podría ser una lista)
        when(userService.findUserByAuthority("admin")).thenReturn(user2);
        
        mockMvc.perform(get("/api/users/authority/admin")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.username", is("admin")));
        
        verify(userService).findUserByAuthority("admin");
    }
    
    @Test
    void testFindByAuthority_NotFound() throws Exception {
        when(userService.findUserByAuthority("nonexistent")).thenReturn(null);
        
        mockMvc.perform(get("/api/users/authority/nonexistent")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNotFound());
        
        verify(userService).findUserByAuthority("nonexistent");
    }
    
    @Test
    void testFindByUsernameAndPassword_Found() throws Exception {
        when(userService.findUserByUsernameAndPassword("johndoe", "password123"))
            .thenReturn(user1);
        
        mockMvc.perform(get("/api/users/usernameAndPassword/johndoe/password123")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.username", is("johndoe")));
        
        verify(userService).findUserByUsernameAndPassword("johndoe", "password123");
    }
    
    @Test
    void testFindByUsernameAndPassword_NotFound() throws Exception {
        when(userService.findUserByUsernameAndPassword("johndoe", "wrongpass"))
            .thenReturn(null);
        
        mockMvc.perform(get("/api/users/usernameAndPassword/johndoe/wrongpass")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNotFound());
        
        verify(userService).findUserByUsernameAndPassword("johndoe", "wrongpass");
    }
    
    @Test
    void testSaveUser_Success() throws Exception {
        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setPassword("newpass");
        Authorities newAuth = new Authorities();
        newAuth.setId(3);
        newAuth.setAuthority("user");
        newUser.setAuthority(newAuth);
        
        User savedUser = new User();
        savedUser.setId(3);
        savedUser.setUsername("newuser");
        savedUser.setPassword("newpass");
        savedUser.setAuthority(newAuth);
        
         when(userService.saveUser(ArgumentMatchers.<User>any())).thenReturn(savedUser);
        
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id", is(3)))
               .andExpect(jsonPath("$.username", is("newuser")));
        
        verify(userService).saveUser(any(User.class));
    }
    
    @Test
    void testSaveUser_NullUser_ThrowsException() throws Exception {
        // Enviar "null" como contenido (JSON "null") debería dar error de deserialización (400 Bad Request)
        String nullJson = "null";
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(nullJson))
               .andExpect(status().isBadRequest());
        
        verify(userService, never()).saveUser(any(User.class));
    }
    
    @Test
    void testUpdateUser_Success() throws Exception {
        User updateUser = new User();
        updateUser.setUsername("updatedUser");
        updateUser.setPassword("updatedPass");
        updateUser.setAuthority(authUser);
        
        User existingUser = user1;
        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setUsername("updatedUser");
        updatedUser.setPassword("updatedPass");
        updatedUser.setAuthority(authUser);
        
        when(userService.findUserById(1)).thenReturn(existingUser);
        when(userService.saveUser(any(User.class))).thenReturn(updatedUser);
        
        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUser)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id", is(1)))
               .andExpect(jsonPath("$.username", is("updatedUser")));
        
        verify(userService).findUserById(1);
        verify(userService).saveUser(any(User.class));
    }
    
    @Test
    void testUpdateUser_NotFound() throws Exception {
        User updateUser = new User();
        updateUser.setUsername("updatedUser");
        updateUser.setPassword("updatedPass");
        updateUser.setAuthority(authUser);
        
        when(userService.findUserById(1)).thenReturn(null);
        
        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUser)))
               .andExpect(status().isNotFound());
        
        verify(userService).findUserById(1);
        verify(userService, never()).saveUser(any(User.class));
    }
    
    @Test
    void testUpdateUser_NullUser_ThrowsException() throws Exception {
        String nullJson = "null";
        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(nullJson))
               .andExpect(status().isBadRequest());
        
        verify(userService, never()).findUserById(anyInt());
        verify(userService, never()).saveUser(any(User.class));
    }
    
    @Test
    void testDeleteUser_Success() throws Exception {
        when(userService.findUserById(1)).thenReturn(user1);
        
        mockMvc.perform(delete("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNoContent());
        
        verify(userService).findUserById(1);
        verify(userService).deleteUser(1);
    }
    
    @Test
    void testDeleteUser_NotFound() throws Exception {
        when(userService.findUserById(999)).thenReturn(null);
        
        mockMvc.perform(delete("/api/users/999")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNotFound());
        
        verify(userService).findUserById(999);
        verify(userService, never()).deleteUser(anyInt());
    }
    
    @Test
    void testGetCurrentUser_Success() throws Exception {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("johndoe");
        when(userService.findUserByUsername("johndoe")).thenReturn(user1);
        
        mockMvc.perform(get("/api/users/me")
                .principal(auth)
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.username", is("johndoe")));
        
        verify(userService).findUserByUsername("johndoe");
    }
    
    @Test
    void testGetCurrentUser_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/users/me")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isUnauthorized());
        
        verify(userService, never()).findUserByUsername(anyString());
    }
    
    @Test
    void testGetCurrentUser_NotFound() throws Exception {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("nonexistent");
        when(userService.findUserByUsername("nonexistent")).thenReturn(null);
        
        mockMvc.perform(get("/api/users/me")
                .principal(auth)
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNotFound());
        
        verify(userService).findUserByUsername("nonexistent");
    }
    
    // CASOS ADICIONALES: pruebas límite o con parámetros vacíos.
    @Test
    void testFindByUsername_EmptyString() throws Exception {
      
        when(userService.findUserByUsername(anyString())).thenReturn(null);
        
        mockMvc.perform(get("/api/users/username/%20")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNotFound());
        
        verify(userService).findUserByUsername(anyString());
    }
    
    
    @Test
    void testFindByUsernameAndPassword_EmptyValues() throws Exception {
        lenient().when(userService.findUserByUsernameAndPassword("", "")).thenReturn(null);

        
        // Se utiliza la codificación de URL para cadenas vacías.
        mockMvc.perform(get("/api/users/usernameAndPassword//")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isBadRequest());
    }
}

