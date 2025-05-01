package ispp_g2.gastrostock.testReabastecimiento;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
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
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import ispp_g2.gastrostock.dueno.Dueno;
import ispp_g2.gastrostock.dueno.DuenoService;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioService;
import ispp_g2.gastrostock.proveedores.Proveedor;
import ispp_g2.gastrostock.reabastecimiento.Reabastecimiento;
import ispp_g2.gastrostock.reabastecimiento.ReabastecimientoController;
import ispp_g2.gastrostock.reabastecimiento.ReabastecimientoService;
import ispp_g2.gastrostock.user.Authorities;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserService;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@WithMockUser(username = "admin", roles = {"admin"})
public class ReabastecimientoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReabastecimientoService reabastecimientoService;

    @Mock
    private UserService userService;

    @Mock
    private DuenoService duenoService;

    @Mock
    private NegocioService negocioService;

    @InjectMocks
    private ReabastecimientoController reabastecimientoController;

    private ObjectMapper objectMapper;

    private Reabastecimiento reabastecimiento1;
    private Reabastecimiento reabastecimiento2;
    private List<Reabastecimiento> reabastecimientos;
    private Proveedor proveedor;
    private Negocio negocio;
    private Dueno dueno;
    private LocalDate fecha1;
    private LocalDate fecha2;
    private User user;
    private User adminUser;
    private Authorities authority;
    private Authorities adminAuth;

    @BeforeEach
    void setUp() {
        // Configurar MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(reabastecimientoController)
                .build();
        
        // Configurar ObjectMapper para manejar LocalDate
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        

         adminUser = new User();
         adminUser.setId(1);
         adminUser.setUsername("adminUser");
         adminAuth = new Authorities();
         adminAuth.setAuthority("admin");
         adminUser.setAuthority(adminAuth);

        // Crear dueno
        dueno = new Dueno();
        dueno.setId(1);
        dueno.setFirstName("Juan");
        dueno.setLastName("García");
        dueno.setEmail("juan@example.com");
        dueno.setNumTelefono("652345678");
        dueno.setUser(adminUser);
        dueno.setTokenDueno("TOKEN123");
        // Crear negocio
        negocio = new Negocio();
        negocio.setId(1);
        negocio.setName("Restaurante La Tasca");
        negocio.setDireccion("Calle Principal 123");
        negocio.setCiudad("Sevilla");
        negocio.setPais("Espana");
        negocio.setCodigoPostal("41001");
        negocio.setTokenNegocio(12345);
        negocio.setDueno(dueno);

        // Crear proveedor
        proveedor = new Proveedor();
        proveedor.setId(1);
        proveedor.setName("Distribuciones Alimentarias S.L.");
        proveedor.setEmail("distribuciones@example.com");
        proveedor.setTelefono("954111222");
        proveedor.setDireccion("Polígono Industrial, Nave 7");

        // Preparar fechas
        fecha1 = LocalDate.of(2023, 3, 15);
        fecha2 = LocalDate.of(2023, 4, 10);

        // Crear reabastecimientos
        reabastecimiento1 = new Reabastecimiento();
        reabastecimiento1.setId(1);
        reabastecimiento1.setFecha(fecha1);
        reabastecimiento1.setPrecioTotal(1250.75);
        reabastecimiento1.setReferencia("REF-001");
        reabastecimiento1.setProveedor(proveedor);
        reabastecimiento1.setNegocio(negocio);

        reabastecimiento2 = new Reabastecimiento();
        reabastecimiento2.setId(2);
        reabastecimiento2.setFecha(fecha2);
        reabastecimiento2.setPrecioTotal(875.30);
        reabastecimiento2.setReferencia("REF-002");
        reabastecimiento2.setProveedor(proveedor);
        reabastecimiento2.setNegocio(negocio);

        reabastecimientos = Arrays.asList(reabastecimiento1, reabastecimiento2);
    }

    // TEST PARA findAll()
    
    @Test
    void testFindAll_Success() throws Exception {
        // Given
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(reabastecimientoService.getAll()).thenReturn(reabastecimientos);
        
        // When & Then
        mockMvc.perform(get("/api/reabastecimientos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].referencia").value("REF-001"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].referencia").value("REF-002"));
        
        verify(reabastecimientoService, atLeastOnce()).getAll();
    }
    
    @Test
    void testFindAll_NoContent() throws Exception {
        // Given
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(reabastecimientoService.getAll()).thenReturn(Collections.emptyList());
        
        // When & Then
        mockMvc.perform(get("/api/reabastecimientos"))
                .andExpect(status().isNoContent());
        
        verify(reabastecimientoService).getAll();
    }
    
    // TEST PARA findById()
    
    @Test
    void testFindById_Success() throws Exception {
        // Given
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(reabastecimientoService.getById(1)).thenReturn(reabastecimiento1);
        
        // When & Then
        mockMvc.perform(get("/api/reabastecimientos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.referencia").value("REF-001"))
                .andExpect(jsonPath("$.precioTotal").value(1250.75));
        
        verify(reabastecimientoService).getById(1);
    }
    
    @Test
    void testFindById_NotFound() throws Exception {
        // Given
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(reabastecimientoService.getById(999)).thenReturn(null);
        
        // When & Then
        mockMvc.perform(get("/api/reabastecimientos/999"))
                .andExpect(status().isNotFound());
        
        verify(reabastecimientoService).getById(999);
    }
    
    // TEST PARA findByFecha()
    
    @Test
    void testFindByFecha_Success() throws Exception {
        // Given
        when(userService.findCurrentUser()).thenReturn(adminUser);
        List<Reabastecimiento> reabastecimientosFecha = Collections.singletonList(reabastecimiento1);
        when(reabastecimientoService.getByFecha(fecha1)).thenReturn(reabastecimientosFecha);
        
        // When & Then
        mockMvc.perform(get("/api/reabastecimientos/fecha/2023-03-15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].referencia").value("REF-001"));
        
        verify(reabastecimientoService).getByFecha(fecha1);
    }
    
    @Test
    void testFindByFecha_NotFound() throws Exception {
        // Given
        when(userService.findCurrentUser()).thenReturn(adminUser);
        LocalDate fechaNoExistente = LocalDate.of(2020, 1, 1);
        when(reabastecimientoService.getByFecha(fechaNoExistente))
            .thenReturn(Collections.emptyList());
        
        // When & Then
        mockMvc.perform(get("/api/reabastecimientos/fecha/2020-01-01"))
               .andExpect(status().isNotFound());
        
        verify(reabastecimientoService).getByFecha(fechaNoExistente);
    }
    
        
    // TEST PARA findByFechaBetween()
    
    @Test
    void testFindByFechaBetween_Success() throws Exception {
        // Given
        when(userService.findCurrentUser()).thenReturn(adminUser);
        LocalDate startDate = LocalDate.of(2023, 3, 1);
        LocalDate endDate = LocalDate.of(2023, 5, 1);
        when(reabastecimientoService.getByFechaBetween(startDate, endDate)).thenReturn(reabastecimientos);
        
        // When & Then
        mockMvc.perform(get("/api/reabastecimientos/fecha/2023-03-01/2023-05-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
        
        verify(reabastecimientoService).getByFechaBetween(startDate, endDate);
    }
    
    @Test
    void testFindByFechaBetween_NotFound() throws Exception {
        // Given
        when(userService.findCurrentUser()).thenReturn(adminUser);
        LocalDate startDate = LocalDate.of(2020, 1, 1);
        LocalDate endDate   = LocalDate.of(2020, 2, 1);
        when(reabastecimientoService.getByFechaBetween(startDate, endDate))
            .thenReturn(Collections.emptyList());
    
        // When & Then
        mockMvc.perform(get("/api/reabastecimientos/fecha/2020-01-01/2020-02-01"))
               .andExpect(status().isNotFound());
    
        verify(reabastecimientoService).getByFechaBetween(startDate, endDate);
    }
    
    // TEST PARA findByPrecioTotal()
    
    @Test
    void testFindByPrecioTotal_Success() throws Exception {
        // Given
        when(userService.findCurrentUser()).thenReturn(adminUser);
        List<Reabastecimiento> reabastecimientosPrecio = Collections.singletonList(reabastecimiento1);
        when(reabastecimientoService.getByPrecioTotal(1250.75)).thenReturn(reabastecimientosPrecio);
        
        // When & Then
        mockMvc.perform(get("/api/reabastecimientos/precioTotal/1250.75"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].precioTotal").value(1250.75));
        
        verify(reabastecimientoService).getByPrecioTotal(1250.75);
    }
    
    @Test
    void testFindByPrecioTotal_NotFound() throws Exception {
        // Given
        when(userService.findCurrentUser()).thenReturn(adminUser);
        Double precioNoExistente = 999.99;
        when(reabastecimientoService.getByPrecioTotal(precioNoExistente)).thenReturn(Collections.emptyList());
        
        // When & Then
        mockMvc.perform(get("/api/reabastecimientos/precioTotal/" + precioNoExistente))
                .andExpect(status().isNotFound());
        
        verify(reabastecimientoService).getByPrecioTotal(precioNoExistente);
    }
    
    // TEST PARA findByReferencia()
    
    @Test
    void testFindByReferencia_Success() throws Exception {
        // Given
        when(userService.findCurrentUser()).thenReturn(adminUser);
        List<Reabastecimiento> reabastecimientosReferencia = Collections.singletonList(reabastecimiento1);
        when(reabastecimientoService.getByReferencia("REF-001")).thenReturn(reabastecimientosReferencia);
        
        // When & Then
        mockMvc.perform(get("/api/reabastecimientos/referencia/REF-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].referencia").value("REF-001"));
        
        verify(reabastecimientoService).getByReferencia("REF-001");
    }
    
    @Test
    void testFindByReferencia_NotFound() throws Exception {
        // Given
        when(userService.findCurrentUser()).thenReturn(adminUser);
        String referenciaNoExistente = "REF-INVALID";
        when(reabastecimientoService.getByReferencia(referenciaNoExistente)).thenReturn(Collections.emptyList());
        
        // When & Then
        mockMvc.perform(get("/api/reabastecimientos/referencia/REF-INVALID"))
                .andExpect(status().isNotFound());
        
        verify(reabastecimientoService).getByReferencia(referenciaNoExistente);
    }
    
    // TEST PARA findByProveedor()
    
    @Test
    void testFindByProveedor_Success() throws Exception {
        // Given
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(reabastecimientoService.getByProveedor(1)).thenReturn(reabastecimientos);
        
        // When & Then
        mockMvc.perform(get("/api/reabastecimientos/proveedor/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
        
        verify(reabastecimientoService).getByProveedor(1);
    }
    
    @Test
    void testFindByProveedor_NotFound() throws Exception {
        // Given
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(reabastecimientoService.getByProveedor(999)).thenReturn(Collections.emptyList());
        
        // When & Then
        mockMvc.perform(get("/api/reabastecimientos/proveedor/999"))
                .andExpect(status().isNotFound());
        
        verify(reabastecimientoService).getByProveedor(999);
    }
    
    // TEST PARA findByNegocio()
    
    @Test
    void testFindByNegocio_Success() throws Exception {
        // Given
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(reabastecimientoService.getByNegocio(1)).thenReturn(reabastecimientos);
        
        // When & Then
        mockMvc.perform(get("/api/reabastecimientos/negocio/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
        
        verify(reabastecimientoService).getByNegocio(1);
    }
    
    @Test
    void testFindByNegocio_NotFound() throws Exception {
        // Given
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(reabastecimientoService.getByNegocio(999)).thenReturn(Collections.emptyList());
        
        // When & Then
        mockMvc.perform(get("/api/reabastecimientos/negocio/999"))
                .andExpect(status().isNotFound());
        
        verify(reabastecimientoService).getByNegocio(999);
    }
    
    // TEST PARA save()
    
    @Test
    void testSave_Success() throws Exception {
        Dueno nuevoDueno = new Dueno();
        nuevoDueno.setFirstName("Juan");
        nuevoDueno.setLastName("García");
        nuevoDueno.setEmail("juan@example.com");
        nuevoDueno.setNumTelefono("652345678");
        nuevoDueno.setTokenDueno("TOKEN123");
    
        Negocio nuevoNegocio = new Negocio();
        nuevoNegocio.setId(1);
        nuevoNegocio.setName("Restaurante La Tasca");
        nuevoNegocio.setDireccion("Calle Principal 123");
        nuevoNegocio.setCiudad("Sevilla");
        nuevoNegocio.setPais("Espana");
        nuevoNegocio.setCodigoPostal("41001");
        nuevoNegocio.setTokenNegocio(12345);
        nuevoNegocio.setDueno(nuevoDueno);
    
        Proveedor nuevoProveedor = new Proveedor();
        nuevoProveedor.setName("Distribuciones Alimentarias S.L.");
        nuevoProveedor.setEmail("distribuciones@example.com");
        nuevoProveedor.setTelefono("954111222");
        nuevoProveedor.setDireccion("Polígono Industrial, Nave 7");
    
        Reabastecimiento nuevoReabastecimiento = new Reabastecimiento();
        nuevoReabastecimiento.setFecha(LocalDate.now());
        nuevoReabastecimiento.setPrecioTotal(500.0);
        nuevoReabastecimiento.setReferencia("REF-NEW");
        nuevoReabastecimiento.setProveedor(nuevoProveedor);
        nuevoReabastecimiento.setNegocio(nuevoNegocio);
    
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(negocioService.getById(1)).thenReturn(nuevoNegocio);
    
        Reabastecimiento reabastecimientoGuardado = new Reabastecimiento();
        reabastecimientoGuardado.setId(42);
        reabastecimientoGuardado.setFecha(nuevoReabastecimiento.getFecha());
        reabastecimientoGuardado.setPrecioTotal(nuevoReabastecimiento.getPrecioTotal());
        reabastecimientoGuardado.setReferencia(nuevoReabastecimiento.getReferencia());
        reabastecimientoGuardado.setProveedor(nuevoProveedor);
        reabastecimientoGuardado.setNegocio(nuevoNegocio);
        when(reabastecimientoService.save(any(Reabastecimiento.class)))
            .thenReturn(reabastecimientoGuardado);
    
        mockMvc.perform(post("/api/reabastecimientos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoReabastecimiento)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(42))
            .andExpect(jsonPath("$.referencia").value("REF-NEW"))
            .andExpect(jsonPath("$.precioTotal").value(500.0));
    
        verify(negocioService).getById(1);
        verify(reabastecimientoService).save(any(Reabastecimiento.class));
    }
    
    
    
    @Test
    void testSave_BadRequest() throws Exception {
        // When & Then - Enviar un objeto JSON vacío debe resultar en error de validación
        mockMvc.perform(post("/api/reabastecimientos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void testSave_NullRequest() throws Exception {
        // When & Then - Enviar null debe lanzar una excepción
        mockMvc.perform(post("/api/reabastecimientos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    
    // TEST PARA update()
    
    @Test
    void testUpdate_Success() throws Exception {
        // given
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(duenoService.getDuenoByUser(adminUser.getId())).thenReturn(dueno);
    
        // Mock del existente en BD
        Reabastecimiento existente = new Reabastecimiento();
        existente.setId(1);
        existente.setNegocio(negocio);
        when(reabastecimientoService.getById(1)).thenReturn(existente);
    
        // Mock del guardado por el service
        Reabastecimiento guardado = new Reabastecimiento();
        guardado.setId(1);
        guardado.setFecha(LocalDate.of(2025, 4, 23));
        guardado.setPrecioTotal(123.45);
        guardado.setReferencia("UPDATED");
        guardado.setProveedor(proveedor);
        guardado.setNegocio(negocio);
        when(reabastecimientoService.save(any(Reabastecimiento.class))).thenReturn(guardado);
    
        // payload JSON — sólo IDs en las relaciones para evitar errores de deserialización
        String json = """
          {
            "id": 1,
            "fecha": "2025-04-23",
            "precioTotal": 123.45,
            "referencia": "UPDATED",
            "proveedor": { "id": %d },
            "negocio":   { "id": %d }
          }
        """.formatted(proveedor.getId(), negocio.getId());
    
        // when & then
        mockMvc.perform(put("/api/reabastecimientos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.referencia").value("UPDATED"))
            .andExpect(jsonPath("$.precioTotal").value(123.45));
    
        // verify
        verify(userService).findCurrentUser();
        verify(duenoService).getDuenoByUser(adminUser.getId());
        verify(reabastecimientoService).save(any(Reabastecimiento.class));
    }
    
    
    
    
    @Test
    void testUpdate_NotFound() throws Exception {
        // Given
        Reabastecimiento reabastecimientoSinId = new Reabastecimiento();
        // No asignamos ID para que reabastecimiento.getId() sea null
        reabastecimientoSinId.setReferencia("REF-999");
        reabastecimientoSinId.setPrecioTotal(999.99);
        
        // When & Then
        mockMvc.perform(put("/api/reabastecimientos/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reabastecimientoSinId)))
                .andExpect(status().isBadRequest());
        
        verify(reabastecimientoService, never()).save(any(Reabastecimiento.class));
    }
    
    @Test
    void testUpdate_BadRequest() throws Exception {
        // When & Then - Enviar un objeto JSON vacío debe resultar en error de validación
        mockMvc.perform(put("/api/reabastecimientos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }
    
    // TEST PARA delete()
    
    @Test
    void testDelete_Success() throws Exception {
        // Given
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(reabastecimientoService.getById(1)).thenReturn(reabastecimiento1);
        doNothing().when(reabastecimientoService).deleteById(1);
        
        // When & Then
        mockMvc.perform(delete("/api/reabastecimientos/1"))
                .andExpect(status().isNoContent());
        
        verify(reabastecimientoService).getById(1);
        verify(reabastecimientoService).deleteById(1);
    }
    
    @Test
    void testDelete_NotFound() throws Exception {
        // Given
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(reabastecimientoService.getById(999)).thenReturn(null);
        
        // When & Then
        mockMvc.perform(delete("/api/reabastecimientos/999"))
                .andExpect(status().isNotFound());
        
        verify(reabastecimientoService).getById(999);
        verify(reabastecimientoService, never()).deleteById(anyInt());
    }
    
    
    
}