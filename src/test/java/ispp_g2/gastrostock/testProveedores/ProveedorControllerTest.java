package ispp_g2.gastrostock.testProveedores;

/* 
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import ispp_g2.gastrostock.proveedores.Proveedor;
import ispp_g2.gastrostock.proveedores.ProveedorController;
import ispp_g2.gastrostock.proveedores.ProveedorService;
import ispp_g2.gastrostock.diaReparto.DiaReparto;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.dueño.Dueño;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class ProveedorControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProveedorService proveedorService;

    @InjectMocks
    private ProveedorController proveedorController;

    private ObjectMapper objectMapper;
    private Proveedor proveedor1, proveedor2, proveedor3;
    private List<Proveedor> proveedores;
    private Negocio negocio;
    private Dueño dueño;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(proveedorController).build();
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
        
        // Crear días de reparto
        Set<DiaReparto> diasReparto1 = new HashSet<>();
        DiaReparto diaLunes = new DiaReparto();
        diaLunes.setId(1);
        diaLunes.setDiaSemana(DayOfWeek.MONDAY);
        diasReparto1.add(diaLunes);
        
        DiaReparto diaMiercoles = new DiaReparto();
        diaMiercoles.setId(2);
        diaMiercoles.setDiaSemana(DayOfWeek.WEDNESDAY);
        diasReparto1.add(diaMiercoles);
        
        Set<DiaReparto> diasReparto2 = new HashSet<>();
        DiaReparto diaMartes = new DiaReparto();
        diaMartes.setId(3);
        diaMartes.setDiaSemana(DayOfWeek.TUESDAY);
        diasReparto2.add(diaMartes);
        
        DiaReparto diaViernes = new DiaReparto();
        diaViernes.setId(4);
        diaViernes.setDiaSemana(DayOfWeek.FRIDAY);
        diasReparto2.add(diaViernes);
        
        // Crear proveedores
        proveedor1 = new Proveedor();
        proveedor1.setId(1);
        proveedor1.setName("Distribuciones Alimentarias S.L.");
        proveedor1.setEmail("distribuciones@example.com");
        proveedor1.setTelefono("954111222");
        proveedor1.setDireccion("Polígono Industrial, Nave 7");

        
        proveedor2 = new Proveedor();
        proveedor2.setId(2);
        proveedor2.setName("Productos Frescos del Sur");
        proveedor2.setEmail("frescos@example.com");
        proveedor2.setTelefono("954333444");
        proveedor2.setDireccion("Avenida de la Industria, 42");

        
        // Proveedor con caso especial: sin días de reparto
        proveedor3 = new Proveedor();
        proveedor3.setId(3);
        proveedor3.setName("Distribuciones Rápidas");
        proveedor3.setEmail("rapidas@example.com");
        proveedor3.setTelefono("954555666");
        proveedor3.setDireccion("Calle Comercio, 15");

        
        proveedores = Arrays.asList(proveedor1, proveedor2, proveedor3);
    }
    
    // Tests para getAll()
    
    @Test
    void testGetAll_Success() throws Exception {
        // Arrange
        when(proveedorService.getAll()).thenReturn(proveedores);
        
        // Act & Assert
        mockMvc.perform(get("/api/proveedores"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].name", is("Distribuciones Alimentarias S.L.")))
            .andExpect(jsonPath("$[1].id", is(2)))
            .andExpect(jsonPath("$[1].name", is("Productos Frescos del Sur")))
            .andExpect(jsonPath("$[2].id", is(3)))
            .andExpect(jsonPath("$[2].name", is("Distribuciones Rápidas")));
        
        verify(proveedorService, times(1)).getAll();
    }
    
    @Test
    void testGetAll_EmptyList() throws Exception {
        // Arrange
        when(proveedorService.getAll()).thenReturn(Collections.emptyList());
        
        // Act & Assert
        mockMvc.perform(get("/api/proveedores"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(0)));
        
        verify(proveedorService, times(1)).getAll();
    }
    
    // Tests para getById()
    
    @Test
    void testGetById_Success() throws Exception {
        // Arrange
        when(proveedorService.getById(1)).thenReturn(Optional.of(proveedor1));
        
        // Act & Assert
        mockMvc.perform(get("/api/proveedores/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.name", is("Distribuciones Alimentarias S.L.")))
            .andExpect(jsonPath("$.email", is("distribuciones@example.com")));
        
        verify(proveedorService, times(1)).getById(1);
    }
    
    @Test
    void testGetById_NotFound() throws Exception {
        // Arrange
        when(proveedorService.getById(999)).thenReturn(Optional.empty());
        
        // Act & Assert
        mockMvc.perform(get("/api/proveedores/999"))
            .andExpect(status().isOk())
            .andExpect(content().string("null"));
        
        verify(proveedorService, times(1)).getById(999);
    }
    
    // Tests para getByFirstName()
    
    @Test
    void testGetByFirstName_Success() throws Exception {
        // Arrange
        List<Proveedor> distribuciones = Arrays.asList(proveedor1, proveedor3);
        when(proveedorService.getByFirstName("Distribuciones")).thenReturn(distribuciones);
        
        // Act & Assert
        mockMvc.perform(get("/api/proveedores/nombre")
                .param("firstName", "Distribuciones"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].name", is("Distribuciones Alimentarias S.L.")))
            .andExpect(jsonPath("$[1].id", is(3)))
            .andExpect(jsonPath("$[1].name", is("Distribuciones Rápidas")));
        
        verify(proveedorService, times(1)).getByFirstName("Distribuciones");
    }
    
    @Test
    void testGetByFirstName_NotFound() throws Exception {
        // Arrange
        when(proveedorService.getByFirstName("Inexistente")).thenReturn(Collections.emptyList());
        
        // Act & Assert
        mockMvc.perform(get("/api/proveedores/nombre")
                .param("firstName", "Inexistente"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(0)));
        
        verify(proveedorService, times(1)).getByFirstName("Inexistente");
    }
    
    @Test
    void testGetByFirstName_EmptyString() throws Exception {
        // Arrange
        when(proveedorService.getByFirstName("")).thenReturn(proveedores);
        
        // Act & Assert
        mockMvc.perform(get("/api/proveedores/nombre")
                .param("firstName", ""))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(3)));
        
        verify(proveedorService, times(1)).getByFirstName("");
    }
    
    // Tests para getByDiaReparto()
    
    @Test
    void testGetByDiaReparto_Success() throws Exception {
        // Arrange
        List<Proveedor> proveedoresLunes = Collections.singletonList(proveedor1);
        when(proveedorService.getByDiaReparto(DayOfWeek.MONDAY)).thenReturn(proveedoresLunes);
        
        // Act & Assert
        mockMvc.perform(get("/api/proveedores/dia-reparto/MONDAY"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].name", is("Distribuciones Alimentarias S.L.")));
        
        verify(proveedorService, times(1)).getByDiaReparto(DayOfWeek.MONDAY);
    }
    
    @Test
    void testGetByDiaReparto_NotFound() throws Exception {
        // Arrange
        when(proveedorService.getByDiaReparto(DayOfWeek.SUNDAY)).thenReturn(Collections.emptyList());
        
        // Act & Assert
        mockMvc.perform(get("/api/proveedores/dia-reparto/SUNDAY"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(0)));
        
        verify(proveedorService, times(1)).getByDiaReparto(DayOfWeek.SUNDAY);
    }
    
    // Tests para create()
    
    @Test
    void testCreate_Success() throws Exception {
        // Arrange
        Proveedor nuevoProveedor = new Proveedor();
        nuevoProveedor.setName("Nuevo Proveedor S.A.");
        nuevoProveedor.setEmail("nuevo@example.com");
        nuevoProveedor.setTelefono("954777888");
        nuevoProveedor.setDireccion("Calle Nueva, 123");
        
        Proveedor proveedorGuardado = new Proveedor();
        proveedorGuardado.setId(4);
        proveedorGuardado.setName("Nuevo Proveedor S.A.");
        proveedorGuardado.setEmail("nuevo@example.com");
        proveedorGuardado.setTelefono("954777888");
        proveedorGuardado.setDireccion("Calle Nueva, 123");

        
        when(proveedorService.save(any(Proveedor.class))).thenReturn(proveedorGuardado);
        
        // Act & Assert
        mockMvc.perform(post("/api/proveedores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoProveedor)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(4)))
            .andExpect(jsonPath("$.name", is("Nuevo Proveedor S.A.")))
            .andExpect(jsonPath("$.email", is("nuevo@example.com")));
        
        verify(proveedorService, times(1)).save(any(Proveedor.class));
    }
    
    @Test
    void testCreate_MissingRequiredFields() throws Exception {
        // Arrange
        Proveedor proveedorIncompleto = new Proveedor();
        // No establecemos campos obligatorios
        
        // Act & Assert
        mockMvc.perform(post("/api/proveedores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(proveedorIncompleto)))
            .andExpect(status().isOk()); // El controlador no valida, sólo pasa al servicio
        
        verify(proveedorService, times(1)).save(any(Proveedor.class));
    }
    
    // Tests para delete()
    
    @Test
    void testDelete_Success() throws Exception {
        // Arrange
        doNothing().when(proveedorService).deleteById(1);
        
        // Act & Assert
        mockMvc.perform(delete("/api/proveedores/1"))
            .andExpect(status().isOk());
        
        verify(proveedorService, times(1)).deleteById(1);
    }
      /* 
    @Test
    void testDelete_Exception() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Proveedor not found")).when(proveedorService).deleteById(999);
        
        // Act & Assert
        mockMvc.perform(delete("/api/proveedores/999"))
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
            .andExpect(result -> assertEquals("Proveedor not found", result.getResolvedException().getMessage()));
        
        verify(proveedorService, times(1)).deleteById(999);
    }
    
    // Test adicional: manejo de excepciones en general
  
    @Test
    void testHandleExceptions() throws Exception {
        // Arrange
        when(proveedorService.getAll()).thenThrow(new RuntimeException("Error inesperado"));
        
        // Act & Assert
        mockMvc.perform(get("/api/proveedores"))
            .andExpect(status().isInternalServerError());
        
        verify(proveedorService, times(1)).getAll();
    }
        */


