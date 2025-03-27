package ispp_g2.gastrostock.testMesa;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import ispp_g2.gastrostock.dueño.Dueño;
import ispp_g2.gastrostock.exceptions.ExceptionHandlerController;
import ispp_g2.gastrostock.mesa.Mesa;
import ispp_g2.gastrostock.mesa.MesaController;
import ispp_g2.gastrostock.mesa.MesaService;
import ispp_g2.gastrostock.negocio.Negocio;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class MesaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MesaService mesaService;

    @InjectMocks
    private MesaController mesaController;

    private ObjectMapper objectMapper;

    private Mesa mesa1, mesa2, mesa3, mesaInvalida, mesaNueva;
    private Negocio negocio;
    private Dueño dueño;
    private List<Mesa> mesasList;

    @BeforeEach
    void setUp() {
        // Configurar MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(mesaController)
                .setControllerAdvice(new ExceptionHandlerController())
                .build();
        
        // Configurar ObjectMapper
        objectMapper = new ObjectMapper();
        
        // Crear dueño
        dueño = new Dueño();
        dueño.setId(1);
        dueño.setFirstName("Juan");
        dueño.setLastName("García");
        dueño.setEmail("juan@example.com");
        dueño.setNumTelefono("652345678");
        dueño.setTokenDueño("TOKEN123");

        // Crear negocio
        negocio = new Negocio();
        negocio.setId(1);
        negocio.setName("Restaurante La Tasca");
        negocio.setDireccion("Calle Principal 123");
        negocio.setCiudad("Sevilla");
        negocio.setPais("España");
        negocio.setCodigoPostal("41001");
        negocio.setTokenNegocio(12345);
        negocio.setDueño(dueño);

        // Crear mesas
        mesa1 = new Mesa();
        mesa1.setId(1);
        mesa1.setName("Mesa Exterior");
        mesa1.setNumeroAsientos(4);
        mesa1.setNegocio(negocio);

        mesa2 = new Mesa();
        mesa2.setId(2);
        mesa2.setName("Mesa VIP");
        mesa2.setNumeroAsientos(6);
        mesa2.setNegocio(negocio);

        mesa3 = new Mesa();
        mesa3.setId(3);
        mesa3.setName("Mesa Barra");
        mesa3.setNumeroAsientos(2);
        mesa3.setNegocio(negocio);

        // Mesa inválida (para probar casos negativos)
        mesaInvalida = new Mesa();
        // No establecemos ningún campo para que sea inválida

        // Mesa para probar creación
        mesaNueva = new Mesa();
        mesaNueva.setName("Mesa Nueva");
        mesaNueva.setNumeroAsientos(8);
        mesaNueva.setNegocio(negocio);

        // Lista de mesas
        mesasList = Arrays.asList(mesa1, mesa2, mesa3);
    }

    // TESTS PARA findAll()

    @Test
    void testFindAll_Success() throws Exception {
        // Arrange
        when(mesaService.getMesas()).thenReturn(mesasList);
        
        // Act & Assert
        mockMvc.perform(get("/api/mesas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Mesa Exterior")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Mesa VIP")))
                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[2].name", is("Mesa Barra")));
        
        verify(mesaService,atLeastOnce()).getMesas();
    }
    
    @Test
    void testFindAll_NoContent() throws Exception {
        // Arrange
        when(mesaService.getMesas()).thenReturn(Collections.emptyList());
        
        // Act & Assert
        mockMvc.perform(get("/api/mesas"))
                .andExpect(status().isNoContent());
        
        verify(mesaService).getMesas();
    }

    // TESTS PARA findById()
    
    @Test
    void testFindById_Success() throws Exception {
        // Arrange
        when(mesaService.getById("1")).thenReturn(mesa1);
        
        // Act & Assert
        mockMvc.perform(get("/api/mesas/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Mesa Exterior")))
                .andExpect(jsonPath("$.numeroAsientos", is(4)));
        
        verify(mesaService).getById("1");
    }
    
    @Test
    void testFindById_NotFound() throws Exception {
        // Arrange
        when(mesaService.getById("999")).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(get("/api/mesas/999"))
                .andExpect(status().isNotFound());
        
        verify(mesaService).getById("999");
    }

    // TESTS PARA findByNumeroAsientos()
    
    @Test
    void testFindByNumeroAsientos_Success() throws Exception {
        // Arrange
        List<Mesa> mesasCuatroAsientos = Collections.singletonList(mesa1);
        when(mesaService.getMesasByNumeroAsientos(4)).thenReturn(mesasCuatroAsientos);
        
        // Act & Assert
        mockMvc.perform(get("/api/mesas/numeroAsientos/4"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].numeroAsientos", is(4)));
        
        verify(mesaService).getMesasByNumeroAsientos(4);
    }
    
    @Test
    void testFindByNumeroAsientos_NotFound() throws Exception {
        // Arrange
        when(mesaService.getMesasByNumeroAsientos(10)).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(get("/api/mesas/numeroAsientos/10"))
                .andExpect(status().isNotFound());
        
        verify(mesaService).getMesasByNumeroAsientos(10);
    }
    
    @Test
    void testFindByNumeroAsientos_EmptyList() throws Exception {
        // Arrange
        when(mesaService.getMesasByNumeroAsientos(10)).thenReturn(Collections.emptyList());
        
        // Act & Assert
        mockMvc.perform(get("/api/mesas/numeroAsientos/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
        
        verify(mesaService).getMesasByNumeroAsientos(10);
    }
    
    @Test
    void testFindByNumeroAsientos_ZeroSeats() throws Exception {
        // Arrange
        when(mesaService.getMesasByNumeroAsientos(0)).thenReturn(Collections.emptyList());
        
        // Act & Assert
        mockMvc.perform(get("/api/mesas/numeroAsientos/0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
        
        verify(mesaService).getMesasByNumeroAsientos(0);
    }
    
    @Test
    void testFindByNumeroAsientos_NegativeSeats() throws Exception {
        // Arrange
        when(mesaService.getMesasByNumeroAsientos(-1)).thenReturn(Collections.emptyList());
        
        // Act & Assert
        mockMvc.perform(get("/api/mesas/numeroAsientos/-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
        
        verify(mesaService).getMesasByNumeroAsientos(-1);
    }

    // TESTS PARA findByNegocio()
    
    @Test
    void testFindByNegocio_Success() throws Exception {
        // Arrange
        when(mesaService.getMesasByNegocio("1")).thenReturn(mesasList);
        
        // Act & Assert
        mockMvc.perform(get("/api/mesas/negocio/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[2].id", is(3)));
        
        verify(mesaService).getMesasByNegocio("1");
    }
    
    @Test
    void testFindByNegocio_NotFound() throws Exception {
        // Arrange
        when(mesaService.getMesasByNegocio("999")).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(get("/api/mesas/negocio/999"))
                .andExpect(status().isNotFound());
        
        verify(mesaService).getMesasByNegocio("999");
    }
    
    @Test
    void testFindByNegocio_EmptyList() throws Exception {
        // Arrange
        when(mesaService.getMesasByNegocio("999")).thenReturn(Collections.emptyList());
        
        // Act & Assert
        mockMvc.perform(get("/api/mesas/negocio/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
        
        verify(mesaService).getMesasByNegocio("999");
    }

    // TESTS PARA create()
    
    @Test
    void testCreate_Success() throws Exception {
       
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
        
        Mesa mesaCreada = new Mesa();
        mesaCreada.setName("Mesa Nueva");
        mesaCreada.setNumeroAsientos(8);
        mesaCreada.setNegocio(negocioActualizado);

        
        
        when(mesaService.save(any(Mesa.class))).thenReturn(mesaCreada);
        
        // Act & Assert
        mockMvc.perform(post("/api/mesas")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mesaCreada)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Mesa Nueva")))
                .andExpect(jsonPath("$.numeroAsientos", is(8)));
        
        verify(mesaService).save(any(Mesa.class));
    }
    /* 
    @Test
    void testCreate_NullMesa() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/mesas")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest());
        
        verify(mesaService, never()).save(any(Mesa.class));
    }
    */
    @Test
    void testCreate_InvalidMesa() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/mesas")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mesaInvalida)))
                .andExpect(status().isBadRequest());
        
        verify(mesaService, never()).save(any(Mesa.class));
    }

    // TESTS PARA update()
    
    @Test
    void testUpdate_Success() throws Exception {
       
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

        Mesa mesaActualizada = new Mesa();
        mesaActualizada.setName("Mesa Exterior Actualizada");
        mesaActualizada.setNumeroAsientos(5);
        mesaActualizada.setNegocio(negocioActualizado);
        
        when(mesaService.getById("1")).thenReturn(mesa1);
        when(mesaService.save(any(Mesa.class))).thenReturn(mesaActualizada);
        
        // Act & Assert
        mockMvc.perform(put("/api/mesas/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mesaActualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Mesa Exterior Actualizada")))
                .andExpect(jsonPath("$.numeroAsientos", is(5)));
        
        verify(mesaService).getById("1");
        verify(mesaService).save(any(Mesa.class));
    }
    /*
    @Test
    void testUpdate_NotFound() throws Exception {
        // Arrange
        when(mesaService.getById("999")).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(put("/api/mesas/999")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mesa1)))
                .andExpect(status().isNotFound());
        
        verify(mesaService).getById("999");
        verify(mesaService, never()).save(any(Mesa.class));
    }
    
    @Test
    void testUpdate_NullMesa() throws Exception {
        // Act & Assert
        mockMvc.perform(put("/api/mesas/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest());
        
        verify(mesaService, never()).getById(anyString());
        verify(mesaService, never()).save(any(Mesa.class));
    }
    
    @Test
    void testUpdate_InvalidMesa() throws Exception {
        // Arrange
        when(mesaService.getById("1")).thenReturn(mesa1);
        
        // Act & Assert
        mockMvc.perform(put("/api/mesas/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mesaInvalida)))
                .andExpect(status().isBadRequest());
        
        verify(mesaService, never()).save(any(Mesa.class));
    } */

    // TESTS PARA delete()
    
    @Test
    void testDelete_Success() throws Exception {
        // Arrange
        when(mesaService.getById("1")).thenReturn(mesa1);
        doNothing().when(mesaService).deleteById("1");
        
        // Act & Assert
        mockMvc.perform(delete("/api/mesas/1")
                .with(csrf()))
                .andExpect(status().isNoContent());
        
        verify(mesaService).getById("1");
        verify(mesaService).deleteById("1");
    }
    
    @Test
    void testDelete_NotFound() throws Exception {
        // Arrange
        when(mesaService.getById("999")).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(delete("/api/mesas/999")
                .with(csrf()))
                .andExpect(status().isNotFound());
        
        verify(mesaService).getById("999");
        verify(mesaService, never()).deleteById(anyString());
    }
}