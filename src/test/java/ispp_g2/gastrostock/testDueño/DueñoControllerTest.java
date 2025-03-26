package ispp_g2.gastrostock.testDueño;

import static org.mockito.ArgumentMatchers.any;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import com.fasterxml.jackson.databind.ObjectMapper;

import ispp_g2.gastrostock.dueño.Dueño;
import ispp_g2.gastrostock.dueño.DueñoController;
import ispp_g2.gastrostock.dueño.DueñoService;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.user.Authorities;
import ispp_g2.gastrostock.user.User;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(DueñoController.class)
@WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
public class DueñoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DueñoService dueñoService;

    @InjectMocks
    private DueñoController dueñoController;

    private ObjectMapper objectMapper;
    private Dueño dueñoNormal;
    private Dueño dueñoConNegocio;
    private Dueño dueñoInvalidoSinEmail;
    private List<Dueño> dueñosList;
    private User user;
    private Negocio negocio;
    private Authorities authoriti;

    @BeforeEach
    void setUp() {

        objectMapper = new ObjectMapper();

        authoriti = new Authorities();
        authoriti.setAuthority("DUEÑO");
        
        // Crear usuario para asociar al dueño
        user = new User();
        user.setId(1);
        user.setUsername("juangarcia");
        user.setPassword("password123");
        user.setAuthority(authoriti);
        // Crear usuario para pruebas

        
        // Crear un dueño normal
        dueñoNormal = new Dueño();
        dueñoNormal.setId(1);
        dueñoNormal.setFirstName("Juan");
        dueñoNormal.setLastName("García");
        dueñoNormal.setEmail("juan@example.com");
        dueñoNormal.setNumTelefono("652345678");
        dueñoNormal.setTokenDueño("TOKEN123");
        dueñoNormal.setUser(user);

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
        dueñoConNegocio = new Dueño();
        dueñoConNegocio.setId(2);
        dueñoConNegocio.setFirstName("Ana");
        dueñoConNegocio.setLastName("Martínez");
        dueñoConNegocio.setEmail("ana@example.com");
        dueñoConNegocio.setNumTelefono("654321987");
        dueñoConNegocio.setTokenDueño("TOKEN456");
        
        // Asignar el dueño al negocio
        negocio.setDueño(dueñoConNegocio);

        // Crear un dueño con datos inválidos (sin email)
        dueñoInvalidoSinEmail = new Dueño();
        dueñoInvalidoSinEmail.setId(3);
        dueñoInvalidoSinEmail.setFirstName("Pedro");
        dueñoInvalidoSinEmail.setLastName("Pérez");
        dueñoInvalidoSinEmail.setNumTelefono("666777888");
        dueñoInvalidoSinEmail.setTokenDueño("TOKEN789");

        // Lista de dueños para tests
        dueñosList = new ArrayList<>();
        dueñosList.add(dueñoNormal);
        dueñosList.add(dueñoConNegocio);
    }

    // TESTS PARA findAll()

    @Test
    void testFindAll_Success() throws Exception {
        // Arrange
        when(dueñoService.getAllDueños()).thenReturn(dueñosList);
        
        // Act & Assert
        mockMvc.perform(get("/api/dueños")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("Juan")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].firstName", is("Ana")));
        
        verify(dueñoService, times(2)).getAllDueños(); // Se llama 2 veces: una para verificar si está vacío y otra para devolver datos
    }

    @Test
    void testFindAll_EmptyList() throws Exception {
        // Arrange
        when(dueñoService.getAllDueños()).thenReturn(Collections.emptyList());
        
        // Act & Assert
        mockMvc.perform(get("/api/dueños")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        
        verify(dueñoService).getAllDueños();
    }

    // TESTS PARA findById()

    @Test
    void testFindById_Success() throws Exception {
        // Arrange
        when(dueñoService.getDueñoById("1")).thenReturn(dueñoNormal);
        
        // Act & Assert
        mockMvc.perform(get("/api/dueños/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Juan")))
                .andExpect(jsonPath("$.lastName", is("García")));
        
        verify(dueñoService).getDueñoById("1");
    }

    @Test
    void testFindById_NotFound() throws Exception {
        // Arrange
        when(dueñoService.getDueñoById("999")).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(get("/api/dueños/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(dueñoService).getDueñoById("999");
    }

    // TESTS PARA findByToken()

    @Test
    void testFindByToken_Success() throws Exception {
        // Arrange
        when(dueñoService.getDueñoByToken("TOKEN123")).thenReturn(dueñoNormal);
        
        // Act & Assert
        mockMvc.perform(get("/api/dueños/token/TOKEN123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Juan")));
        
        verify(dueñoService, times(2)).getDueñoByToken("TOKEN123"); // Se llama 2 veces en el controlador
    }

    @Test
    void testFindByToken_NotFound() throws Exception {
        // Arrange
        when(dueñoService.getDueñoByToken("INVALID_TOKEN")).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(get("/api/dueños/token/INVALID_TOKEN")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(dueñoService).getDueñoByToken("INVALID_TOKEN");
    }

    // TESTS PARA findByEmail()

    @Test
    void testFindByEmail_Success() throws Exception {
        // Arrange
        when(dueñoService.getDueñoByEmail("juan@example.com")).thenReturn(dueñoNormal);
        
        // Act & Assert
        mockMvc.perform(get("/api/dueños/email/juan@example.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is("juan@example.com")));
        
        verify(dueñoService, times(2)).getDueñoByEmail("juan@example.com"); // Se llama 2 veces en el controlador
    }

    @Test
    void testFindByEmail_NotFound() throws Exception {
        // Arrange
        when(dueñoService.getDueñoByEmail("nonexistent@example.com")).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(get("/api/dueños/email/nonexistent@example.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(dueñoService).getDueñoByEmail("nonexistent@example.com");
    }

    // TESTS PARA findByNombre()

    @Test
    void testFindByNombre_Success() throws Exception {
        // Arrange
        when(dueñoService.getDueñoByNombre("Juan")).thenReturn(Collections.singletonList(dueñoNormal));
        
        // Act & Assert
        mockMvc.perform(get("/api/dueños/nombre/Juan")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is("Juan")));
        
        verify(dueñoService).getDueñoByNombre("Juan");
    }

    @Test
    void testFindByNombre_NotFound() throws Exception {
        // Arrange
        when(dueñoService.getDueñoByNombre("Inexistente")).thenReturn(Collections.emptyList());
        
        // Act & Assert
        mockMvc.perform(get("/api/dueños/nombre/Inexistente")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(dueñoService).getDueñoByNombre("Inexistente");
    }

    // TESTS PARA findByApellido()

    @Test
    void testFindByApellido_Success() throws Exception {
        // Arrange
        when(dueñoService.getDueñoByApellido("García")).thenReturn(Collections.singletonList(dueñoNormal));
        
        // Act & Assert
        mockMvc.perform(get("/api/dueños/apellido/García")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].lastName", is("García")));
        
        verify(dueñoService, times(2)).getDueñoByApellido("García"); // Se llama 2 veces en el controlador
    }

    @Test
    void testFindByApellido_NotFound() throws Exception {
        // Arrange
        when(dueñoService.getDueñoByApellido("Inexistente")).thenReturn(Collections.emptyList());
        
        // Act & Assert
        mockMvc.perform(get("/api/dueños/apellido/Inexistente")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(dueñoService).getDueñoByApellido("Inexistente");
    }

    // TESTS PARA findByTelefono()

    @Test
    void testFindByTelefono_Success() throws Exception {
        // Arrange
        when(dueñoService.getDueñoByTelefono("652345678")).thenReturn(dueñoNormal);
        
        // Act & Assert
        mockMvc.perform(get("/api/dueños/telefono/652345678")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numTelefono", is("652345678")));
        
        verify(dueñoService, times(2)).getDueñoByTelefono("652345678"); // Se llama 2 veces en el controlador
    }

    @Test
    void testFindByTelefono_NotFound() throws Exception {
        // Arrange
        when(dueñoService.getDueñoByTelefono("999999999")).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(get("/api/dueños/telefono/999999999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(dueñoService).getDueñoByTelefono("999999999");
    }

    // TESTS PARA findByUser()

    @Test
    void testFindByUser_Success() throws Exception {
        // Arrange
        when(dueñoService.getDueñoByUser("1")).thenReturn(dueñoNormal);
        
        // Act & Assert
        mockMvc.perform(get("/api/dueños/user/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
        
        verify(dueñoService, times(2)).getDueñoByUser("1"); // Se llama 2 veces en el controlador
    }

    @Test
    void testFindByUser_NotFound() throws Exception {
        // Arrange
        when(dueñoService.getDueñoByUser("999")).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(get("/api/dueños/user/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(dueñoService).getDueñoByUser("999");
    }

    // TESTS PARA save()

    @Test
    void testSave_Success() throws Exception {
        // Arrange
        when(dueñoService.saveDueño(any(Dueño.class))).thenReturn(dueñoNormal);
            
        // Act & Assert
        mockMvc.perform(post("/api/dueños")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dueñoNormal)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Juan")));
        
        verify(dueñoService).saveDueño(any(Dueño.class));
    }

    @Test
    void testSave_NullDueño() throws Exception {
        // Act & Assert - No podemos enviar un cuerpo nulo directamente, pero podemos probar con un objeto vacío
        mockMvc.perform(post("/api/dueños")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")) // Objeto JSON vacío
                .andExpect(status().isBadRequest()); // Debería fallar la validación
    }

    @Test
    void testSave_InvalidDueño() throws Exception {
        // Arrange
        // No necesitamos configurar el mock porque esperamos una excepción de validación
        
        // Act & Assert
        mockMvc.perform(post("/api/dueños")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dueñoInvalidoSinEmail)))
                .andExpect(status().isBadRequest()); // Debería fallar la validación si hay restricciones @NotNull
    }

    // TESTS PARA update()

    @Test
    void testUpdate_Success() throws Exception {
        // Arrange
        Dueño dueñoActualizado = new Dueño();
        dueñoActualizado.setId(1);
        dueñoActualizado.setFirstName("Juan Actualizado");
        dueñoActualizado.setLastName("García");
        dueñoActualizado.setEmail("juan@example.com");
        dueñoActualizado.setNumTelefono("652345678");
        dueñoActualizado.setTokenDueño("TOKEN123");
        
        when(dueñoService.getDueñoById("1")).thenReturn(dueñoNormal);
        when(dueñoService.saveDueño(any(Dueño.class))).thenReturn(dueñoActualizado);
        
        // Act & Assert
        mockMvc.perform(put("/api/dueños/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dueñoActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Juan Actualizado")));
        
        verify(dueñoService).getDueñoById("1");
        verify(dueñoService).saveDueño(any(Dueño.class));
    }

    @Test
    void testUpdate_NotFound() throws Exception {
        // Arrange
        when(dueñoService.getDueñoById("999")).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(put("/api/dueños/999")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dueñoNormal)))
                .andExpect(status().isNotFound());
        
        verify(dueñoService).getDueñoById("999");
        verify(dueñoService, never()).saveDueño(any(Dueño.class));
    }

    @Test
    void testUpdate_NullDueño() throws Exception {
        // Act & Assert
        mockMvc.perform(put("/api/dueños/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")) // Objeto JSON vacío
                .andExpect(status().isBadRequest()); // Cambiado a BAD_REQUEST (400)
    }

    // TESTS PARA delete()

    @Test
    void testDelete_Success() throws Exception {
        // Arrange
        when(dueñoService.getDueñoById("1")).thenReturn(dueñoNormal);
        doNothing().when(dueñoService).deleteDueño("1");
        
        // Act & Assert
        mockMvc.perform(delete("/api/dueños/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        
        verify(dueñoService).getDueñoById("1");
        verify(dueñoService).deleteDueño("1");
    }

    @Test
    void testDelete_NotFound() throws Exception {
        // Arrange
        when(dueñoService.getDueñoById("999")).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(delete("/api/dueños/999")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(dueñoService).getDueñoById("999");
        verify(dueñoService, never()).deleteDueño("999");
    }
}