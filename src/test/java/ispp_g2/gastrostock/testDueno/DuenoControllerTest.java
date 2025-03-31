package ispp_g2.gastrostock.testDueno;

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

import ispp_g2.gastrostock.dueno.Dueno;
import ispp_g2.gastrostock.dueno.DuenoController;
import ispp_g2.gastrostock.dueno.DuenoService;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.user.Authorities;
import ispp_g2.gastrostock.user.User;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(DuenoController.class)
@WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
public class DuenoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DuenoService duenoService;

    @InjectMocks
    private DuenoController duenoController;

    private ObjectMapper objectMapper;
    private Dueno duenoNormal;
    private Dueno duenoConNegocio;
    private Dueno duenoInvalidoSinEmail;
    private List<Dueno> duenosList;
    private User user;
    private Negocio negocio;
    private Authorities authoriti;

    @BeforeEach
    void setUp() {

        objectMapper = new ObjectMapper();

        authoriti = new Authorities();
        authoriti.setAuthority("DUEnO");
        
        // Crear usuario para asociar al dueno
        user = new User();
        user.setId(1);
        user.setUsername("juangarcia");
        user.setPassword("password123");
        user.setAuthority(authoriti);
        // Crear usuario para pruebas

        
        // Crear un dueno normal
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
        negocio.setPais("Espana");
        negocio.setCodigoPostal("41001");
        negocio.setTokenNegocio(12345);

        // Crear un dueno con negocio
        duenoConNegocio = new Dueno();
        duenoConNegocio.setId(2);
        duenoConNegocio.setFirstName("Ana");
        duenoConNegocio.setLastName("Martínez");
        duenoConNegocio.setEmail("ana@example.com");
        duenoConNegocio.setNumTelefono("654321987");
        duenoConNegocio.setTokenDueno("TOKEN456");
        
        // Asignar el dueno al negocio
        negocio.setDueno(duenoConNegocio);

        // Crear un dueno con datos inválidos (sin email)
        duenoInvalidoSinEmail = new Dueno();
        duenoInvalidoSinEmail.setId(3);
        duenoInvalidoSinEmail.setFirstName("Pedro");
        duenoInvalidoSinEmail.setLastName("Pérez");
        duenoInvalidoSinEmail.setNumTelefono("666777888");
        duenoInvalidoSinEmail.setTokenDueno("TOKEN789");

        // Lista de duenos para tests
        duenosList = new ArrayList<>();
        duenosList.add(duenoNormal);
        duenosList.add(duenoConNegocio);
    }

    // TESTS PARA findAll()

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
        
        verify(duenoService, times(2)).getAllDuenos(); // Se llama 2 veces: una para verificar si está vacío y otra para devolver datos
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

    // TESTS PARA findById()

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
                .andExpect(jsonPath("$.lastName", is("García")));
        
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

    // TESTS PARA findByToken()

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
        
        verify(duenoService, times(2)).getDuenoByToken("TOKEN123"); // Se llama 2 veces en el controlador
    }

    @Test
    void testFindByToken_NotFound() throws Exception {
        // Arrange
        when(duenoService.getDuenoByToken("INVALID_TOKEN")).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(get("/api/duenos/token/INVALID_TOKEN")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(duenoService).getDuenoByToken("INVALID_TOKEN");
    }

    // TESTS PARA findByEmail()

    @Test
    void testFindByEmail_Success() throws Exception {
        // Arrange
        when(duenoService.getDuenoByEmail("juan@example.com")).thenReturn(duenoNormal);
        
        // Act & Assert
        mockMvc.perform(get("/api/duenos/email/juan@example.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is("juan@example.com")));
        
        verify(duenoService, times(2)).getDuenoByEmail("juan@example.com"); // Se llama 2 veces en el controlador
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

    // TESTS PARA findByNombre()

    @Test
    void testFindByNombre_Success() throws Exception {
        // Arrange
        when(duenoService.getDuenoByNombre("Juan")).thenReturn(Collections.singletonList(duenoNormal));
        
        // Act & Assert
        mockMvc.perform(get("/api/duenos/nombre/Juan")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is("Juan")));
        
        verify(duenoService).getDuenoByNombre("Juan");
    }

    @Test
    void testFindByNombre_NotFound() throws Exception {
        // Arrange
        when(duenoService.getDuenoByNombre("Inexistente")).thenReturn(Collections.emptyList());
        
        // Act & Assert
        mockMvc.perform(get("/api/duenos/nombre/Inexistente")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(duenoService).getDuenoByNombre("Inexistente");
    }

    // TESTS PARA findByApellido()

    @Test
    void testFindByApellido_Success() throws Exception {
        // Arrange
        when(duenoService.getDuenoByApellido("García")).thenReturn(Collections.singletonList(duenoNormal));
        
        // Act & Assert
        mockMvc.perform(get("/api/duenos/apellido/García")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].lastName", is("García")));
        
        verify(duenoService, times(2)).getDuenoByApellido("García"); // Se llama 2 veces en el controlador
    }

    @Test
    void testFindByApellido_NotFound() throws Exception {
        // Arrange
        when(duenoService.getDuenoByApellido("Inexistente")).thenReturn(Collections.emptyList());
        
        // Act & Assert
        mockMvc.perform(get("/api/duenos/apellido/Inexistente")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(duenoService).getDuenoByApellido("Inexistente");
    }

    // TESTS PARA findByTelefono()

    @Test
    void testFindByTelefono_Success() throws Exception {
        // Arrange
        when(duenoService.getDuenoByTelefono("652345678")).thenReturn(duenoNormal);
        
        // Act & Assert
        mockMvc.perform(get("/api/duenos/telefono/652345678")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numTelefono", is("652345678")));
        
        verify(duenoService, times(2)).getDuenoByTelefono("652345678"); // Se llama 2 veces en el controlador
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

    // TESTS PARA findByUser()

    @Test
    void testFindByUser_Success() throws Exception {
        // Arrange
        when(duenoService.getDuenoByUser(1)).thenReturn(duenoNormal);
        
        // Act & Assert
        mockMvc.perform(get("/api/duenos/user/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
        
        verify(duenoService, times(2)).getDuenoByUser(1); // Se llama 2 veces en el controlador
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

    // TESTS PARA save()

    @Test
    void testSave_Success() throws Exception {
        // Arrange
        when(duenoService.saveDueno(any(Dueno.class))).thenReturn(duenoNormal);
            
        // Act & Assert
        mockMvc.perform(post("/api/duenos")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duenoNormal)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Juan")));
        
        verify(duenoService).saveDueno(any(Dueno.class));
    }

    @Test
    void testSave_NullDueno() throws Exception {
        // Act & Assert - No podemos enviar un cuerpo nulo directamente, pero podemos probar con un objeto vacío
        mockMvc.perform(post("/api/duenos")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")) // Objeto JSON vacío
                .andExpect(status().isBadRequest()); // Debería fallar la validación
    }

    @Test
    void testSave_InvalidDueno() throws Exception {
        // Arrange
        // No necesitamos configurar el mock porque esperamos una excepción de validación
        
        // Act & Assert
        mockMvc.perform(post("/api/duenos")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duenoInvalidoSinEmail)))
                .andExpect(status().isBadRequest()); // Debería fallar la validación si hay restricciones @NotNull
    }

    // TESTS PARA update()

    @Test
    void testUpdate_Success() throws Exception {
        // Arrange
        Dueno duenoActualizado = new Dueno();
        duenoActualizado.setId(1);
        duenoActualizado.setFirstName("Juan Actualizado");
        duenoActualizado.setLastName("García");
        duenoActualizado.setEmail("juan@example.com");
        duenoActualizado.setNumTelefono("652345678");
        duenoActualizado.setTokenDueno("TOKEN123");
        
        when(duenoService.getDuenoById(1)).thenReturn(duenoNormal);
        when(duenoService.saveDueno(any(Dueno.class))).thenReturn(duenoActualizado);
        
        // Act & Assert
        mockMvc.perform(put("/api/duenos/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duenoActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Juan Actualizado")));
        
        verify(duenoService).getDuenoById(1);
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
                .content(objectMapper.writeValueAsString(duenoNormal)))
                .andExpect(status().isNotFound());
        
        verify(duenoService).getDuenoById(999);
        verify(duenoService, never()).saveDueno(any(Dueno.class));
    }

    @Test
    void testUpdate_NullDueno() throws Exception {
        // Act & Assert
        mockMvc.perform(put("/api/duenos/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")) // Objeto JSON vacío
                .andExpect(status().isBadRequest()); // Cambiado a BAD_REQUEST (400)
    }

    // TESTS PARA delete()

    @Test
    void testDelete_Success() throws Exception {
        // Arrange
        when(duenoService.getDuenoById(1)).thenReturn(duenoNormal);
        doNothing().when(duenoService).deleteDueno(1);
        
        // Act & Assert
        mockMvc.perform(delete("/api/duenos/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
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
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(duenoService).getDuenoById(999);
        verify(duenoService, never()).deleteDueno(999);
    }
}