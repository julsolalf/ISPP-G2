package ispp_g2.gastrostock.testProveedores;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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
import ispp_g2.gastrostock.exceptions.ExceptionHandlerController;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class ProveedorControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProveedorService proveedorService;

    @InjectMocks
    private ProveedorController proveedorController;

    private ObjectMapper objectMapper;
    private Proveedor proveedor1;
    private Proveedor proveedor2;
    private Proveedor proveedor3;
    private List<Proveedor> proveedores;
    private List<Proveedor> emptyList;

    @BeforeEach
    void setUp() {
        // Configurar MockMvc con manejo de excepciones
        mockMvc = MockMvcBuilders.standaloneSetup(proveedorController)
                .setControllerAdvice(new ispp_g2.gastrostock.exceptions.ExceptionHandlerController())
                .build();
        
        objectMapper = new ObjectMapper();
        
        // Crear proveedores de prueba
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
        
        proveedor3 = new Proveedor();
        proveedor3.setId(3);
        proveedor3.setName("Distribuciones Rápidas");
        proveedor3.setEmail("rapidas@example.com");
        proveedor3.setTelefono("954555666");
        proveedor3.setDireccion("Calle Comercio, 15");
        
        // Lista de proveedores
        proveedores = Arrays.asList(proveedor1, proveedor2, proveedor3);
        emptyList = Collections.emptyList();
    }

    // TESTS PARA findAll()

    @Test
    void testFindAll_Success() throws Exception {
        // Arrange
        when(proveedorService.findAll()).thenReturn(proveedores);
        
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
        
        verify(proveedorService, atLeastOnce()).findAll();
    }

    @Test
    void testFindAll_EmptyList() throws Exception {
        // Arrange
        when(proveedorService.findAll()).thenReturn(emptyList);
        
        // Act & Assert
        mockMvc.perform(get("/api/proveedores"))
            .andExpect(status().isNoContent());
        
        verify(proveedorService).findAll();
    }

    @Test
    void testFindAll_ServiceThrowsException() throws Exception {
        // Arrange
        when(proveedorService.findAll()).thenThrow(new RuntimeException("Database error"));
        
        // Act & Assert
        mockMvc.perform(get("/api/proveedores"))
            .andExpect(status().isInternalServerError());
        
        verify(proveedorService).findAll();
    }

    // TESTS PARA findById()

    @Test
    void testFindById_Success() throws Exception {
        // Arrange
        when(proveedorService.findById(1)).thenReturn(proveedor1);
        
        // Act & Assert
        mockMvc.perform(get("/api/proveedores/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.name", is("Distribuciones Alimentarias S.L.")))
            .andExpect(jsonPath("$.email", is("distribuciones@example.com")));
        
        verify(proveedorService).findById(1);
    }

    @Test
    void testFindById_NotFound() throws Exception {
        // Arrange
        when(proveedorService.findById(999)).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(get("/api/proveedores/999"))
            .andExpect(status().isNotFound());
        
        verify(proveedorService).findById(999);
    }

    @Test
    void testFindById_InvalidId() throws Exception {
        // Arrange - Simular que el servicio lanza una excepción al recibir un ID no válido
        when(proveedorService.findById(999999)).thenThrow(new NumberFormatException("Invalid ID"));
        
        // Act & Assert
        mockMvc.perform(get("/api/proveedores/invalid"))
            .andExpect(status().isBadRequest());
        
        verify(proveedorService).findById(999999);
    }

    // TESTS PARA findByEmail()

    @Test
    void testFindByEmail_Success() throws Exception {
        // Arrange
        when(proveedorService.findByEmail("distribuciones@example.com")).thenReturn(proveedor1);
        
        // Act & Assert
        mockMvc.perform(get("/api/proveedores/email/distribuciones@example.com"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.name", is("Distribuciones Alimentarias S.L.")));
        
        verify(proveedorService).findByEmail("distribuciones@example.com");
    }

    @Test
    void testFindByEmail_NotFound() throws Exception {
        // Arrange
        when(proveedorService.findByEmail("noexiste@example.com")).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(get("/api/proveedores/email/noexiste@example.com"))
            .andExpect(status().isNotFound());
        
        verify(proveedorService).findByEmail("noexiste@example.com");
    }

    // TESTS PARA findByTelefono()

    @Test
    void testFindByTelefono_Success() throws Exception {
        // Arrange
        when(proveedorService.findByTelefono("954111222")).thenReturn(proveedor1);
        
        // Act & Assert
        mockMvc.perform(get("/api/proveedores/telefono/954111222"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.name", is("Distribuciones Alimentarias S.L.")));
        
        verify(proveedorService).findByTelefono("954111222");
    }

    @Test
    void testFindByTelefono_NotFound() throws Exception {
        // Arrange
        when(proveedorService.findByTelefono("999999999")).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(get("/api/proveedores/telefono/999999999"))
            .andExpect(status().isNotFound());
        
        verify(proveedorService).findByTelefono("999999999");
    }

    // TESTS PARA findByDireccion()

    @Test
    void testFindByDireccion_Success() throws Exception {
        // Arrange
        when(proveedorService.findByDireccion("Polígono Industrial, Nave 7")).thenReturn(proveedor1);
        
        // Act & Assert
        mockMvc.perform(get("/api/proveedores/direccion/Polígono Industrial, Nave 7"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.name", is("Distribuciones Alimentarias S.L.")));
        
        verify(proveedorService).findByDireccion("Polígono Industrial, Nave 7");
    }

    @Test
    void testFindByDireccion_NotFound() throws Exception {
        // Arrange
        when(proveedorService.findByDireccion("Dirección inexistente")).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(get("/api/proveedores/direccion/Dirección inexistente"))
            .andExpect(status().isNotFound());
        
        verify(proveedorService).findByDireccion("Dirección inexistente");
    }

    // TESTS PARA findByNombre()

    @Test
    void testFindByNombre_Success() throws Exception {
        // Arrange
        when(proveedorService.findByNombre("Distribuciones Alimentarias S.L.")).thenReturn(proveedor1);
        
        // Act & Assert
        mockMvc.perform(get("/api/proveedores/nombre/Distribuciones Alimentarias S.L."))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.email", is("distribuciones@example.com")));
        
        verify(proveedorService).findByNombre("Distribuciones Alimentarias S.L.");
    }

    @Test
    void testFindByNombre_NotFound() throws Exception {
        // Arrange
        when(proveedorService.findByNombre("Nombre inexistente")).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(get("/api/proveedores/nombre/Nombre inexistente"))
            .andExpect(status().isNotFound());
        
        verify(proveedorService).findByNombre("Nombre inexistente");
    }

    // TESTS PARA save()

    @Test
    void testSave_Success() throws Exception {
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
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.name", is("Nuevo Proveedor S.A.")))
                .andExpect(jsonPath("$.email", is("nuevo@example.com")));
        
        verify(proveedorService).save(any(Proveedor.class));
    }

    @Test
    void testSave_NullProveedor() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/proveedores")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")) // Enviar un objeto JSON vacío
                .andExpect(status().isBadRequest());
        
        verify(proveedorService, never()).save(any(Proveedor.class));
    }

    @Test
    void testSave_InvalidProveedor() throws Exception {
        // Arrange
        Proveedor proveedorIncompleto = new Proveedor(); // Sin campos obligatorios
        
        // Act & Assert
        mockMvc.perform(post("/api/proveedores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(proveedorIncompleto)))
                .andExpect(status().isBadRequest());
        
        // Verificar que el servicio nunca fue llamado
        verify(proveedorService, never()).save(any(Proveedor.class));
    }

    // TESTS PARA update()

    @Test
    void testUpdate_Success() throws Exception {
        // Arrange
        Proveedor proveedorActualizado = new Proveedor();
        proveedorActualizado.setName("Distribuciones Alimentarias Actualizado");
        proveedorActualizado.setEmail("actualizado@example.com");
        proveedorActualizado.setTelefono("954111333");
        proveedorActualizado.setDireccion("Polígono Industrial, Nave 8");
        
        Proveedor proveedorGuardado = new Proveedor();
        proveedorGuardado.setId(1);
        proveedorGuardado.setName("Distribuciones Alimentarias Actualizado");
        proveedorGuardado.setEmail("actualizado@example.com");
        proveedorGuardado.setTelefono("954111333");
        proveedorGuardado.setDireccion("Polígono Industrial, Nave 8");
        
        when(proveedorService.findById(1)).thenReturn(proveedor1);
        when(proveedorService.save(any(Proveedor.class))).thenReturn(proveedorGuardado);
        
        // Act & Assert
        mockMvc.perform(put("/api/proveedores/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(proveedorActualizado)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Distribuciones Alimentarias Actualizado")))
                .andExpect(jsonPath("$.email", is("actualizado@example.com")));
        
        verify(proveedorService).findById(1);
        verify(proveedorService).save(any(Proveedor.class));
    }

    @Test
    void testUpdate_NotFound() throws Exception {
        // Arrange
        Proveedor proveedorActualizado = new Proveedor();
        proveedorActualizado.setName("Distribuciones Alimentarias Actualizado");
        proveedorActualizado.setEmail("actualizado@example.com");
        proveedorActualizado.setTelefono("954111333"); // Anadir teléfono
        proveedorActualizado.setDireccion("Polígono Industrial, Nave 8"); // Anadir dirección
        // Anadir otros campos obligatorios si los hay
        
        when(proveedorService.findById(999)).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(put("/api/proveedores/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(proveedorActualizado)))
                .andExpect(status().isNotFound());
        
        verify(proveedorService).findById(999);
        verify(proveedorService, never()).save(any(Proveedor.class));
    }

    @Test
    void testUpdate_NullProveedor() throws Exception {
        // Act & Assert
        mockMvc.perform(put("/api/proveedores/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")) // Enviar un objeto JSON vacío
                .andExpect(status().isBadRequest());
        
        verify(proveedorService, never()).findById(anyInt());
        verify(proveedorService, never()).save(any(Proveedor.class));
    }

    // TESTS PARA deleteById()

    @Test
    void testDeleteById_Success() throws Exception {
        // Arrange
        when(proveedorService.findById(1)).thenReturn(proveedor1);
        doNothing().when(proveedorService).deleteById(1);
        
        // Act & Assert
        mockMvc.perform(delete("/api/proveedores/1"))
                .andExpect(status().isNoContent());
        
        verify(proveedorService).findById(1);
        verify(proveedorService).deleteById(1);
    }

    @Test
    void testDeleteById_NotFound() throws Exception {
        // Arrange
        when(proveedorService.findById(999)).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(delete("/api/proveedores/999"))
                .andExpect(status().isNotFound());
        
        verify(proveedorService).findById(999);
        verify(proveedorService, never()).deleteById(999);
    }

    @Test
    void testDeleteById_ServiceThrowsException() throws Exception {
        // Arrange
        when(proveedorService.findById(1)).thenReturn(proveedor1);
        doThrow(new RuntimeException("Database error")).when(proveedorService).deleteById(1);
        
        // Act & Assert
        mockMvc.perform(delete("/api/proveedores/1"))
                .andExpect(status().isInternalServerError());
        
        verify(proveedorService).findById(1);
        verify(proveedorService).deleteById(1);
    }
}