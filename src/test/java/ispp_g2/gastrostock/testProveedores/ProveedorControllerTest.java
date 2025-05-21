package ispp_g2.gastrostock.testProveedores;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import ispp_g2.gastrostock.dueno.DuenoService;
import ispp_g2.gastrostock.empleado.EmpleadoService;
import ispp_g2.gastrostock.negocio.NegocioService;
import ispp_g2.gastrostock.proveedores.Proveedor;
import ispp_g2.gastrostock.proveedores.ProveedorController;
import ispp_g2.gastrostock.proveedores.ProveedorDTO;
import ispp_g2.gastrostock.proveedores.ProveedorService;
import ispp_g2.gastrostock.user.Authorities;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserService;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@WithMockUser(username = "admin", roles = {"admin"})
class ProveedorControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProveedorService proveedorService;
    @Mock 
    private UserService userService;
    @Mock 
    private DuenoService duenoService;
    @Mock 
    private EmpleadoService empleadoService;
    @Mock 
    private NegocioService negocioService;

    @InjectMocks
    private ProveedorController proveedorController;

    private ObjectMapper objectMapper;
    private Proveedor proveedor1;
    private Proveedor proveedor2;
    private Proveedor proveedor3;
    private User adminUser;
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
        
        adminUser = new User();
        adminUser.setId(99);
        Authorities auth = new Authorities();
        auth.setAuthority("admin");
        adminUser.setAuthority(auth);

        // Cuando el controlador pregunte por el usuario actual, devolvemos el admin
        lenient().when(userService.findCurrentUser()).thenReturn(adminUser);
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
        mockMvc.perform(get("/api/proveedores")
                .contentType(MediaType.APPLICATION_JSON))
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
            .andExpect(status().isOk());
        
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
    void testFindById_NotFound_ReturnsOkAndEmptyBody() throws Exception {
        when(proveedorService.findById(999)).thenReturn(null);
    
        mockMvc.perform(get("/api/proveedores/999"))
            .andExpect(status().isOk())
            .andExpect(content().string(""));
    
        verify(proveedorService).findById(999);
    }
    

@Test
void testFindById_InvalidId() throws Exception {
    // Act & Assert
    mockMvc.perform(get("/api/proveedores/invalid"))
        .andExpect(status().isBadRequest());
    
    verify(proveedorService, never()).findById(anyInt());
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
            .andExpect(status().isOk())
            .andExpect(content().string(""));

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
            .andExpect(status().isOk())
            .andExpect(content().string(""));
        
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
            .andExpect(status().isOk())
            .andExpect(content().string(""));
        
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
            .andExpect(status().isOk())
            .andExpect(content().string(""));
        
        verify(proveedorService).findByNombre("Nombre inexistente");
    }

    // TESTS PARA save()

    @Test
    void testSave_Success() throws Exception {
        // Arrange
        ProveedorDTO nuevoProveedorDTO = new ProveedorDTO();
        nuevoProveedorDTO.setName("Nuevo Proveedor S.A.");
        nuevoProveedorDTO.setEmail("nuevo@example.com");
        nuevoProveedorDTO.setTelefono("954777888");
        nuevoProveedorDTO.setDireccion("Calle Nueva, 123");
        nuevoProveedorDTO.setNegocioId(1); 
        
        Proveedor proveedorConvertido = new Proveedor();
        proveedorConvertido.setName("Nuevo Proveedor S.A.");
        proveedorConvertido.setEmail("nuevo@example.com");
        proveedorConvertido.setTelefono("954777888");
        proveedorConvertido.setDireccion("Calle Nueva, 123");
        
        Proveedor proveedorGuardado = new Proveedor();
        proveedorGuardado.setId(4);
        proveedorGuardado.setName("Nuevo Proveedor S.A.");
        proveedorGuardado.setEmail("nuevo@example.com");
        proveedorGuardado.setTelefono("954777888");
        proveedorGuardado.setDireccion("Calle Nueva, 123");
        
        when(proveedorService.convertirDTOProveedor(any(ProveedorDTO.class))).thenReturn(proveedorConvertido);
        when(proveedorService.save(any(Proveedor.class))).thenReturn(proveedorGuardado);
        
        // Act & Assert
        mockMvc.perform(post("/api/proveedores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoProveedorDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.name", is("Nuevo Proveedor S.A.")))
                .andExpect(jsonPath("$.email", is("nuevo@example.com")));
        
        verify(proveedorService).convertirDTOProveedor(any(ProveedorDTO.class));
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
        // DTO de entrada
        ProveedorDTO proveedorDTO = new ProveedorDTO();
        proveedorDTO.setName("Distribuciones Alimentarias Actualizado");
        proveedorDTO.setEmail("actualizado@example.com");
        proveedorDTO.setTelefono("954111333");
        proveedorDTO.setDireccion("Polígono Industrial, Nave 8");
        proveedorDTO.setNegocioId(1);
    
        // Objeto convertido desde DTO
        Proveedor proveedorConvertido = new Proveedor();
        proveedorConvertido.setName(proveedorDTO.getName());
        proveedorConvertido.setEmail(proveedorDTO.getEmail());
        proveedorConvertido.setTelefono(proveedorDTO.getTelefono());
        proveedorConvertido.setDireccion(proveedorDTO.getDireccion());
        proveedorConvertido.setNegocio(proveedor1.getNegocio());
    
        // Resultado esperado tras update()
        Proveedor proveedorGuardado = new Proveedor();
        proveedorGuardado.setId(1);
        proveedorGuardado.setName(proveedorDTO.getName());
        proveedorGuardado.setEmail(proveedorDTO.getEmail());
        proveedorGuardado.setTelefono(proveedorDTO.getTelefono());
        proveedorGuardado.setDireccion(proveedorDTO.getDireccion());
        proveedorGuardado.setNegocio(proveedor1.getNegocio());
    
        // Mocks: convert y update
        when(proveedorService.convertirDTOProveedor(any(ProveedorDTO.class)))
            .thenReturn(proveedorConvertido);
        when(proveedorService.update(eq(1), any(Proveedor.class)))
            .thenReturn(proveedorGuardado);
    
        // Ejecutar PUT
        mockMvc.perform(put("/api/proveedores/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(proveedorDTO)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.name", is("Distribuciones Alimentarias Actualizado")))
            .andExpect(jsonPath("$.email", is("actualizado@example.com")));
    
        // Verificaciones: solo los métodos realmente invocados
        verify(proveedorService, times(2)).convertirDTOProveedor(any(ProveedorDTO.class));
        verify(proveedorService).update(eq(1), any(Proveedor.class));
        verify(proveedorService, never()).findById(anyInt());
        verify(proveedorService, never()).save(any());
    }
    
    

    @Test
    void testUpdate_InvalidEmail() throws Exception {
        ProveedorDTO proveedorDTO = new ProveedorDTO();
        proveedorDTO.setName("Proveedor Test");
        proveedorDTO.setEmail("email-invalido"); 
        proveedorDTO.setTelefono("954111333");
        proveedorDTO.setDireccion("Dirección test");
        proveedorDTO.setNegocioId(1);
        
        mockMvc.perform(put("/api/proveedores/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(proveedorDTO)))
            .andExpect(status().isBadRequest());
}

@Test
void testUpdate_NonExistingDto_ReturnsOk() throws Exception {
    // Preparamos el DTO de entrada
    ProveedorDTO proveedorDTO = new ProveedorDTO();
    proveedorDTO.setName("Distribuciones Alimentarias Actualizado");
    proveedorDTO.setEmail("actualizado@example.com");
    proveedorDTO.setTelefono("954111333");
    proveedorDTO.setDireccion("Polígono Industrial, Nave 8");
    proveedorDTO.setNegocioId(1);

    // Mock de conversión
    Proveedor convertido = new Proveedor();
    convertido.setName(proveedorDTO.getName());
    convertido.setEmail(proveedorDTO.getEmail());
    convertido.setTelefono(proveedorDTO.getTelefono());
    convertido.setDireccion(proveedorDTO.getDireccion());
    // Mock de update (se devuelve el mismo convertido para simplificar)
    when(proveedorService.convertirDTOProveedor(any(ProveedorDTO.class)))
        .thenReturn(convertido);
    when(proveedorService.update(eq(999), any(Proveedor.class)))
        .thenReturn(convertido);

    // Ejecutamos el PUT y esperamos 200 OK
    mockMvc.perform(put("/api/proveedores/999")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(proveedorDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is("Distribuciones Alimentarias Actualizado")))
        .andExpect(jsonPath("$.email", is("actualizado@example.com")));

    // Verificamos las invocaciones reales
    verify(proveedorService, times(2)).convertirDTOProveedor(any(ProveedorDTO.class));
    verify(proveedorService).update(eq(999), any(Proveedor.class));
    verify(proveedorService, never()).findById(anyInt());
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
    public void testDeleteById_NoContent() throws Exception {

        when(proveedorService.findById(999)).thenReturn(null);
        doNothing().when(proveedorService).deleteById(999);
    
        // Act & Assert
        mockMvc.perform(delete("/api/proveedores/999"))
               .andExpect(status().isNoContent());
    
        verify(proveedorService).findById(999);
        // Ahora esperamos que sí se llame a deleteById
        verify(proveedorService).deleteById(999);
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