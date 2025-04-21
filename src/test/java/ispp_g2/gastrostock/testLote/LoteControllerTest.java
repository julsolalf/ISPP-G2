package ispp_g2.gastrostock.testLote;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import ispp_g2.gastrostock.config.jwt.JwtService;
import ispp_g2.gastrostock.dueno.DuenoService;
import ispp_g2.gastrostock.empleado.EmpleadoService;
import ispp_g2.gastrostock.exceptions.ExceptionHandlerController;
import ispp_g2.gastrostock.lote.Lote;
import ispp_g2.gastrostock.lote.LoteController;
import ispp_g2.gastrostock.lote.LoteService;
import ispp_g2.gastrostock.negocio.NegocioService;
import ispp_g2.gastrostock.productoInventario.ProductoInventario;
import ispp_g2.gastrostock.reabastecimiento.Reabastecimiento;
import ispp_g2.gastrostock.reabastecimiento.ReabastecimientoService;
import ispp_g2.gastrostock.user.AuthoritiesService;
import ispp_g2.gastrostock.user.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class LoteControllerTest {

    private MockMvc mockMvc;
    
    @Mock
    private LoteService loteService;

    @Mock
    private UserService userService;

    @Mock 
    private AuthoritiesService authorityService;

    @Mock
    private NegocioService negocioService;

    @Mock
    private ReabastecimientoService reabastecimientoService;

    @Mock
    private DuenoService duenoService;

    @Mock
    private EmpleadoService empleadoService;
        
    @Mock
    private UserDetailsService userDetailsService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @InjectMocks
    private LoteController loteController;

    private Lote lote1, lote2, lote3;
    private ProductoInventario producto;
    private Reabastecimiento reabastecimiento;
    
    @BeforeEach
    void setUp() {
         // Configurar MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(loteController)
                .setControllerAdvice(new ExceptionHandlerController())
                .build();
        
        // Configurar ObjectMapper
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        
        // Crear producto
        producto = new ProductoInventario();
        producto.setId(1);
        producto.setName("Harina");
        producto.setPrecioCompra(2.5);
        
        // Crear reabastecimiento
        reabastecimiento = new Reabastecimiento();
        reabastecimiento.setId(1);
        reabastecimiento.setReferencia("REF001");
        
        // Crear lotes
        lote1 = new Lote();
        lote1.setId(1);
        lote1.setCantidad(100);
        lote1.setFechaCaducidad(LocalDate.now().plusMonths(3));
        lote1.setProducto(producto);
        lote1.setReabastecimiento(reabastecimiento);
        
        lote2 = new Lote();
        lote2.setId(2);
        lote2.setCantidad(50);
        lote2.setFechaCaducidad(LocalDate.now().plusMonths(6));
        lote2.setProducto(producto);
        lote2.setReabastecimiento(reabastecimiento);
        
        // Caso extremo
        lote3 = new Lote();
        lote3.setId(3);
        lote3.setCantidad(0);
        lote3.setFechaCaducidad(LocalDate.now().minusDays(1)); // Caducado
        lote3.setProducto(producto);
        lote3.setReabastecimiento(reabastecimiento);


    }
    
    // TESTS PARA findAll
    
    @Test
    void testFindAll_Success() throws Exception {
        // Arrange
        List<Lote> lotes = Arrays.asList(lote1, lote2);
        when(loteService.getLotes()).thenReturn(lotes);
        
        // Act & Assert
        mockMvc.perform(get("/api/lotes"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[1].id", is(2)));
        
        verify(loteService, atLeastOnce()).getLotes();
    }
    
    @Test
    void testFindAll_NoContent() throws Exception {
        // Arrange
        when(loteService.getLotes()).thenReturn(new ArrayList<>());
        
        // Act & Assert
        mockMvc.perform(get("/api/lotes"))
            .andExpect(status().isNoContent());
        
        verify(loteService).getLotes();
    }
    
    // TESTS PARA findById
    
    @Test
    void testFindById_Success() throws Exception {
        // Arrange
        when(loteService.getById(1)).thenReturn(lote1);
        
        // Act & Assert
        mockMvc.perform(get("/api/lotes/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.cantidad", is(100)));
        
        verify(loteService).getById(1);
    }
    
    @Test
    void testFindById_NotFound() throws Exception {
        // Arrange
        when(loteService.getById(999)).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(get("/api/lotes/999"))
            .andExpect(status().isNotFound());
        
        verify(loteService).getById(999);
    }
    
    // TESTS PARA findByCantidad
    
    @Test
    void testFindByCantidad_Success() throws Exception {
        // Arrange
        List<Lote> lotes = Arrays.asList(lote1);
        when(loteService.getLotesByCantidad(100)).thenReturn(lotes);
        
        // Act & Assert
        mockMvc.perform(get("/api/lotes/cantidad/100"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].cantidad", is(100)));
        
        verify(loteService).getLotesByCantidad(100);
    }
    
    @Test
    void testFindByCantidad_NotFound() throws Exception {
        // Arrange
        when(loteService.getLotesByCantidad(999)).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(get("/api/lotes/cantidad/999"))
            .andExpect(status().isNotFound());
        
        verify(loteService).getLotesByCantidad(999);
    }
    
    @Test
    void testFindByCantidad_ZeroCantidad() throws Exception {
        // Arrange
        List<Lote> lotes = Arrays.asList(lote3);
        when(loteService.getLotesByCantidad(0)).thenReturn(lotes);
        
        // Act & Assert
        mockMvc.perform(get("/api/lotes/cantidad/0"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id", is(3)))
            .andExpect(jsonPath("$[0].cantidad", is(0)));
        
        verify(loteService).getLotesByCantidad(0);
    }
    
    // TESTS PARA findByFechaCaducidad
    
    @Test
    void testFindByFechaCaducidad_Success() throws Exception {
        // Arrange
        LocalDate fecha = LocalDate.now().plusMonths(3);
        List<Lote> lotes = Arrays.asList(lote1);
        when(loteService.getLotesByFechaCaducidad(any(LocalDate.class))).thenReturn(lotes);
        
        // Act & Assert
        mockMvc.perform(get("/api/lotes/fechaCaducidad/" + fecha.toString()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id", is(1)));
        
        verify(loteService).getLotesByFechaCaducidad(any(LocalDate.class));
    }
    
    @Test
    void testFindByFechaCaducidad_NotFound() throws Exception {
        // Arrange
        LocalDate fecha = LocalDate.now().plusYears(10);
        when(loteService.getLotesByFechaCaducidad(any(LocalDate.class))).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(get("/api/lotes/fechaCaducidad/" + fecha.toString()))
            .andExpect(status().isNotFound());
        
        verify(loteService).getLotesByFechaCaducidad(any(LocalDate.class));
    }
    
    // TESTS PARA findByProductoId
    
    @Test
    void testFindByProductoId_Success() throws Exception {
        // Arrange
        List<Lote> lotes = Arrays.asList(lote1, lote2);
        when(loteService.getLotesByProductoId(1)).thenReturn(lotes);
        
        // Act & Assert
        mockMvc.perform(get("/api/lotes/producto/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[1].id", is(2)));
        
        verify(loteService).getLotesByProductoId(1);
    }
    
    @Test
    void testFindByProductoId_NotFound() throws Exception {
        // Arrange
        when(loteService.getLotesByProductoId(999)).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(get("/api/lotes/producto/999"))
            .andExpect(status().isNotFound());
        
        verify(loteService).getLotesByProductoId(999);
    }
    
    // TESTS PARA findByReabastecimientoId
    
    @Test
    void testFindByReabastecimientoId_Success() throws Exception {
        // Arrange
        List<Lote> lotes = Arrays.asList(lote1, lote2);
        when(loteService.getLotesByReabastecimientoId(1)).thenReturn(lotes);
        
        // Act & Assert
        mockMvc.perform(get("/api/lotes/reabastecimiento/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[1].id", is(2)));
        
        verify(loteService).getLotesByReabastecimientoId(1);
    }
    
    @Test
    void testFindByReabastecimientoId_NotFound() throws Exception {
        // Arrange
        when(loteService.getLotesByReabastecimientoId(999)).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(get("/api/lotes/reabastecimiento/999"))
            .andExpect(status().isNotFound());
        
        verify(loteService).getLotesByReabastecimientoId(999);
    }
    
    // TESTS PARA save
    
    @Test
    void testSave_Success() throws Exception {
        // Arrange
        Lote nuevoLote = new Lote();
        nuevoLote.setCantidad(200);
        nuevoLote.setFechaCaducidad(LocalDate.now().plusMonths(9));
        nuevoLote.setProducto(producto);
        nuevoLote.setReabastecimiento(reabastecimiento);
        
        Lote loteGuardado = new Lote();
        loteGuardado.setId(4);
        loteGuardado.setCantidad(200);
        loteGuardado.setFechaCaducidad(LocalDate.now().plusMonths(9));
        loteGuardado.setProducto(producto);
        loteGuardado.setReabastecimiento(reabastecimiento);
        
        when(loteService.save(any(Lote.class))).thenReturn(loteGuardado);
        
        // Act & Assert
        mockMvc.perform(post("/api/lotes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoLote)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(4)))
            .andExpect(jsonPath("$.cantidad", is(200)));
        
        verify(loteService).save(any(Lote.class));
    }
    
    @Test
    void testSave_ValidationFailed() throws Exception {
        // Arrange - Lote con valores inválidos
        Lote loteInvalido = new Lote();
        // No establecemos valores requeridos
        
        // Act & Assert
        mockMvc.perform(post("/api/lotes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loteInvalido)))
            .andExpect(status().isBadRequest());
        
        verify(loteService, never()).save(any(Lote.class));
    }
    
    // TESTS PARA update
    
    @Test
    void testUpdate_Success() throws Exception {
        // Arrange
        Lote loteActualizado = new Lote();
        loteActualizado.setCantidad(150);
        loteActualizado.setFechaCaducidad(LocalDate.now().plusMonths(4));
        loteActualizado.setProducto(producto);
        loteActualizado.setReabastecimiento(reabastecimiento);
        
        Lote loteGuardado = new Lote();
        loteGuardado.setId(1);
        loteGuardado.setCantidad(150);
        loteGuardado.setFechaCaducidad(LocalDate.now().plusMonths(4));
        loteGuardado.setProducto(producto);
        loteGuardado.setReabastecimiento(reabastecimiento);
        
        when(loteService.getById(1)).thenReturn(lote1);
        when(loteService.save(any(Lote.class))).thenReturn(loteGuardado);
        
        // Act & Assert
        mockMvc.perform(put("/api/lotes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loteActualizado)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.cantidad", is(150)));
        
        verify(loteService).getById(1);
        verify(loteService).save(any(Lote.class));
    }
    
    @Test
    void testUpdate_NotFound() throws Exception {
        // Arrange
        Lote loteActualizado = new Lote();
        loteActualizado.setCantidad(150);
        loteActualizado.setFechaCaducidad(LocalDate.now().plusMonths(4));
        loteActualizado.setProducto(producto);
        loteActualizado.setReabastecimiento(reabastecimiento);
        
        when(loteService.getById(999)).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(put("/api/lotes/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loteActualizado)))
            .andExpect(status().isNotFound());
        
        verify(loteService).getById(999);
        verify(loteService, never()).save(any(Lote.class));
    }
    
    // TESTS PARA delete
    
    @Test
    void testDelete_Success() throws Exception {
        // Arrange
        when(loteService.getById(1)).thenReturn(lote1);
        doNothing().when(loteService).delete(1);
        
        // Act & Assert
        mockMvc.perform(delete("/api/lotes/1"))
            .andExpect(status().isNoContent());
        
        verify(loteService).getById(1);
        verify(loteService).delete(1);
    }
    
    @Test
    void testDelete_NotFound() throws Exception {
        // Arrange
        when(loteService.getById(999)).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(delete("/api/lotes/999"))
            .andExpect(status().isNotFound());
        
        verify(loteService).getById(999);
        verify(loteService, never()).delete(anyInt());
    }
}