package ispp_g2.gastrostock.testNegocio;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import ispp_g2.gastrostock.dueño.Dueño;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioController;
import ispp_g2.gastrostock.negocio.NegocioService;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class NegocioControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NegocioService negocioService;
    
    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private NegocioController negocioController;

    private Negocio negocio1, negocio2;
    private List<Negocio> negocioList;
    private Dueño dueño;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Configurar MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(negocioController)
                .build();
        
        objectMapper = new ObjectMapper();
        
        // Crear datos de prueba
        dueño = new Dueño();
        dueño.setId(1);
        dueño.setFirstName("Juan Propietario");
        dueño.setLastName("García");
        dueño.setEmail("juan@gastrostock.com");
        dueño.setTokenDueño("TOKEN123");
        
        negocio1 = new Negocio();
        negocio1.setId(1);
        negocio1.setName("Restaurante La Tasca");
        negocio1.setDireccion("Calle Principal 123");
        negocio1.setCiudad("Sevilla");
        negocio1.setPais("España");
        negocio1.setCodigoPostal("41001");
        negocio1.setTokenNegocio(12345);
        negocio1.setDueño(dueño);
        
        negocio2 = new Negocio();
        negocio2.setId(2);
        negocio2.setName("Bar El Rincón");
        negocio2.setDireccion("Avenida de la Constitución 45");
        negocio2.setCiudad("Sevilla");
        negocio2.setPais("España");
        negocio2.setCodigoPostal("41001");
        negocio2.setTokenNegocio(67890);
        negocio2.setDueño(dueño);
        
        negocioList = Arrays.asList(negocio1, negocio2);
    }

    @Test
    void testFindAll() throws Exception {

        when(negocioService.getNegocios()).thenReturn(negocioList);
        
        mockMvc.perform(get("/api/negocios")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Restaurante La Tasca")))
                .andExpect(jsonPath("$[1].name", is("Bar El Rincón")));
        
        verify(negocioService, atLeastOnce()).getNegocios();
    }
    
    @Test
    void testFindNegocioById() throws Exception {

        when(negocioService.getById(String.valueOf(1))).thenReturn(negocio1);
        
        mockMvc.perform(get("/api/negocios/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Restaurante La Tasca")));
        
        verify(negocioService).getById(String.valueOf(1));
    }
    
    @Test
    void testFindNegocioById_NotFound() throws Exception {

        when(negocioService.getById(String.valueOf(999))).thenReturn(null);
        
        mockMvc.perform(get("/api/negocios/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(negocioService).getById(String.valueOf(999));
    }
    
    @Test
    void testFindNegocioByToken() throws Exception {

        when(negocioService.getByToken(12345)).thenReturn(negocio1);
        
        mockMvc.perform(get("/api/negocios/token/12345")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tokenNegocio", is(12345)))
                .andExpect(jsonPath("$.name", is("Restaurante La Tasca")));
        
        verify(negocioService).getByToken(12345);
    }
    
    @Test
    void testFindNegocioByToken_NotFound() throws Exception {

        when(negocioService.getByToken(9999)).thenReturn(null);
        
        mockMvc.perform(get("/api/negocios/token/9999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(negocioService).getByToken(9999);
    }
    
    @Test
    void testFindNegocioByName() throws Exception {

        when(negocioService.getByName("Restaurante La Tasca")).thenReturn(negocio1);
        
        mockMvc.perform(get("/api/negocios/name/Restaurante La Tasca")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Restaurante La Tasca")));
        
        verify(negocioService).getByName("Restaurante La Tasca");
    }
    
    @Test
    void testFindNegocioByCiudad() throws Exception {

        when(negocioService.getByCiudad("Sevilla")).thenReturn(negocioList);
        
        mockMvc.perform(get("/api/negocios/ciudad/Sevilla")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].ciudad", is("Sevilla")));
        
        verify(negocioService).getByCiudad("Sevilla");
    }
    
    @Test
    void testFindNegocioByCodigoPostal() throws Exception {

        when(negocioService.getByCodigoPostal("41001")).thenReturn(negocioList);
        
        mockMvc.perform(get("/api/negocios/codigoPostal/41001")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].codigoPostal", is("41001")));
        
        verify(negocioService).getByCodigoPostal("41001");
    }
    
    @Test
    void testFindNegocioByPais() throws Exception {

        when(negocioService.getByPais("España")).thenReturn(negocioList);
        
        mockMvc.perform(get("/api/negocios/pais/España")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].pais", is("España")));
        
        verify(negocioService).getByPais("España");
    }
    @Test
    void testFindNegocioByDireccion() throws Exception {
        // Crear lista con solo negocio1
        List<Negocio> negociosByDireccion = Arrays.asList(negocio1);
        
        // Mock para retornar una lista en lugar de un solo negocio
        when(negocioService.getByDireccion("Calle Principal 123")).thenReturn(negociosByDireccion);
    
        mockMvc.perform(get("/api/negocios/direccion/Calle Principal 123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].direccion", is("Calle Principal 123")));
    
        verify(negocioService).getByDireccion("Calle Principal 123");
    }
    
    @Test
    void testCreateNegocio() throws Exception {
        // Usando save() en lugar de saveNegocio()
        Dueño dueñoact = new Dueño();
        dueñoact.setFirstName("Anton");
        dueñoact.setLastName("García");
        dueñoact.setEmail("anton@example.com");
        dueñoact.setNumTelefono("652349978");
        dueñoact.setTokenDueño("TOKEN333");

        // Crear negocio
        Negocio negocioActualizado = new Negocio();
        negocioActualizado.setName("Restaurante 2 Tasca");
        negocioActualizado.setDireccion("Calle Principal 123");
        negocioActualizado.setCiudad("Sevilla");
        negocioActualizado.setPais("España");
        negocioActualizado.setCodigoPostal("41001");
        negocioActualizado.setTokenNegocio(12995);
        negocioActualizado.setDueño(dueñoact);

        when(negocioService.save(any(Negocio.class))).thenReturn(negocioActualizado);
    
        mockMvc.perform(post("/api/negocios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(negocioActualizado)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Restaurante 2 Tasca")));
    
        verify(negocioService).save(any(Negocio.class));
    }
    
    @Test
    void testModifyNegocio() throws Exception {

        Negocio updatedNegocio = new Negocio();
        updatedNegocio.setName("Restaurante Actualizado");
        updatedNegocio.setDireccion("Nueva Dirección 123");
        updatedNegocio.setCiudad("Sevilla");
        updatedNegocio.setPais("España");
        updatedNegocio.setCodigoPostal("41001");
        updatedNegocio.setTokenNegocio(12345);
        updatedNegocio.setDueño(dueño);
        
        when(negocioService.getById(String.valueOf(1))).thenReturn(negocio1);
        when(negocioService.save(any(Negocio.class))).thenReturn(updatedNegocio);

        mockMvc.perform(put("/api/negocios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedNegocio)))
                .andExpect(status().isOk());
        
        verify(negocioService).save(any(Negocio.class));
    }
    
    @Test
    void testModifyNegocio_InvalidId() throws Exception {

        Negocio invalidNegocio = new Negocio();
        invalidNegocio.setName("Restaurante Inválido");
        invalidNegocio.setDireccion("Calle Principal 123");
        invalidNegocio.setCiudad("Sevilla");
        invalidNegocio.setPais("España");
        invalidNegocio.setCodigoPostal("41001");
        invalidNegocio.setTokenNegocio(9999);
        invalidNegocio.setDueño(dueño);
        
        when(negocioService.getById(String.valueOf(1))).thenReturn(null);

        mockMvc.perform(put("/api/negocios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidNegocio)))
                .andExpect(status().isNotFound());
    }
// TEMPORAL FIX NOW IS BY ID
//    @Test
//    void testDeleteNegocio() throws Exception {
//
//        when(negocioService.getByToken(12345)).thenReturn(negocio1);
//        doNothing().when(negocioService).deleteNegocioByToken(12345);
//
//
//        mockMvc.perform(delete("/api/negocios/12345")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNoContent());
//
//        verify(negocioService).deleteNegocioByToken(12345);
//    }
    
@Test
void testDeleteNegocio_NotFound() throws Exception {
    // Arrange - Usar getById en lugar de getByToken
    when(negocioService.getById("9999")).thenReturn(null);
    
    // Act & Assert
    mockMvc.perform(delete("/api/negocios/9999")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
            
    // Verify
    verify(negocioService).getById("9999");
}
}