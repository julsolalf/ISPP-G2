package ispp_g2.gastrostock.testIngrediente;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ispp_g2.gastrostock.config.SecurityConfiguration;
import ispp_g2.gastrostock.config.jwt.JwtAuthFilter;
import ispp_g2.gastrostock.config.jwt.JwtService;
import ispp_g2.gastrostock.ingrediente.Ingrediente;
import ispp_g2.gastrostock.ingrediente.IngredienteController;
import ispp_g2.gastrostock.ingrediente.IngredienteService;
import ispp_g2.gastrostock.productoInventario.ProductoInventario;
import ispp_g2.gastrostock.productoVenta.ProductoVenta;

@WebMvcTest({IngredienteController.class})
@Import({SecurityConfiguration.class, JwtAuthFilter.class})
@ActiveProfiles("test")
@WithMockUser(username = "owner", roles = {"owner"})
public class IngredienteControllerTest {

@Autowired
    private MockMvc mockMvc;

    @MockBean
    private IngredienteService ingredienteService;

    @MockBean
    private JwtService jwtService;
    
    @MockBean
    private AuthenticationProvider authenticationProvider;
    
    @MockBean
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    private Ingrediente ingrediente1;
    private Ingrediente ingrediente2;
    private Ingrediente ingredienteInvalido;
    private ProductoInventario productoInventario1;
    private ProductoInventario productoInventario2;
    private ProductoVenta productoVenta1;
    private ProductoVenta productoVenta2;
    private List<Ingrediente> ingredientes;

    @BeforeEach
    void setUp() {
        
        // Configurar productos de inventario
        productoInventario1 = new ProductoInventario();
        productoInventario1.setId(1);
        productoInventario1.setName("Patatas");

        productoInventario2 = new ProductoInventario();
        productoInventario2.setId(2);
        productoInventario2.setName("Cebollas");

        // Configurar productos de venta
        productoVenta1 = new ProductoVenta();
        productoVenta1.setId(1);
        productoVenta1.setName("Patatas Fritas");

        productoVenta2 = new ProductoVenta();
        productoVenta2.setId(2);
        productoVenta2.setName("Tortilla");

        // Configurar ingredientes
        ingrediente1 = new Ingrediente();
        ingrediente1.setId(1);
        ingrediente1.setCantidad(2);
        ingrediente1.setProductoInventario(productoInventario1);
        ingrediente1.setProductoVenta(productoVenta1);

        ingrediente2 = new Ingrediente();
        ingrediente2.setId(2);
        ingrediente2.setCantidad(3);
        ingrediente2.setProductoInventario(productoInventario2);
        ingrediente2.setProductoVenta(productoVenta2);

        // Ingrediente inválido para casos negativos
        ingredienteInvalido = new Ingrediente();
        // No se establecen campos obligatorios

        // Lista de ingredientes para tests
        ingredientes = Arrays.asList(ingrediente1, ingrediente2);
        when(jwtService.getUserNameFromJwtToken(anyString())).thenReturn("admin");
        when(jwtService.validateJwtToken(anyString(), any())).thenReturn(true);
    }

    // TESTS PARA findAll()

    @Test
    void testFindAll_Success() throws Exception {
        when(ingredienteService.getIngredientes()).thenReturn(ingredientes);
        
        mockMvc.perform(get("/api/ingredientes"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].cantidad").value(2))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[1].cantidad").value(3));
        
        verify(ingredienteService, atLeastOnce()).getIngredientes();
    }

    @Test
    void testFindAll_EmptyList() throws Exception {
        when(ingredienteService.getIngredientes()).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/api/ingredientes"))
            .andExpect(status().isNoContent());
        
        verify(ingredienteService).getIngredientes();
    }

    @Test
    void testFindAll_ServiceError() throws Exception {
        when(ingredienteService.getIngredientes()).thenThrow(new RuntimeException("Database error"));
        
        mockMvc.perform(get("/api/ingredientes"))
            .andExpect(status().isInternalServerError());
        
        verify(ingredienteService).getIngredientes();
    }

    // TESTS PARA findById()

    @Test
    void testFindById_Success() throws Exception {
        when(ingredienteService.getById(1)).thenReturn(ingrediente1);
        
        mockMvc.perform(get("/api/ingredientes/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.cantidad").value(2))
            .andExpect(jsonPath("$.productoInventario.id").value(1))
            .andExpect(jsonPath("$.productoVenta.id").value(1));
        
        verify(ingredienteService).getById(1);
    }

    @Test
    void testFindById_NotFound() throws Exception {
        when(ingredienteService.getById(999)).thenReturn(null);
        
        mockMvc.perform(get("/api/ingredientes/999"))
            .andExpect(status().isNotFound());
        
        verify(ingredienteService).getById(999);
    }

    @Test
    void testFindById_InvalidId() throws Exception {
        mockMvc.perform(get("/api/ingredientes/invalid"))
            .andExpect(status().isBadRequest());
    }

    // TESTS PARA findByCantidad()

    @Test
    void testFindByCantidad_Success() throws Exception {
        List<Ingrediente> ingredientesCantidad2 = List.of(ingrediente1);
        when(ingredienteService.getIngredientesByCantidad(2)).thenReturn(ingredientesCantidad2);
        
        mockMvc.perform(get("/api/ingredientes/cantidad/2"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].cantidad").value(2));
        
        verify(ingredienteService).getIngredientesByCantidad(2);
    }

    @Test
    void testFindByCantidad_Empty() throws Exception {
        when(ingredienteService.getIngredientesByCantidad(0)).thenReturn(null);
        
        mockMvc.perform(get("/api/ingredientes/cantidad/0"))
            .andExpect(status().isNotFound());
        
        verify(ingredienteService).getIngredientesByCantidad(0);
    }

    @Test
    void testFindByCantidad_NegativeValue() throws Exception {
        when(ingredienteService.getIngredientesByCantidad(-1)).thenReturn(null);
        
        mockMvc.perform(get("/api/ingredientes/cantidad/-1"))
            .andExpect(status().isNotFound());
        
        verify(ingredienteService).getIngredientesByCantidad(-1);
    }

    // TESTS PARA findByProductoInventarioId()

    @Test
    void testFindByProductoInventarioId_Success() throws Exception {
        List<Ingrediente> ingredientesProducto1 = List.of(ingrediente1);
        when(ingredienteService.getIngredientesByProductoInventarioId(1)).thenReturn(ingredientesProducto1);
        
        mockMvc.perform(get("/api/ingredientes/productoInventario/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].productoInventario.id").value(1));
        
        verify(ingredienteService).getIngredientesByProductoInventarioId(1);
    }

    @Test
    void testFindByProductoInventarioId_Empty() throws Exception {
        when(ingredienteService.getIngredientesByProductoInventarioId(99)).thenReturn(null);
        
        mockMvc.perform(get("/api/ingredientes/productoInventario/99"))
            .andExpect(status().isNotFound());
        
        verify(ingredienteService).getIngredientesByProductoInventarioId(99);
    }

@SuppressWarnings("null")
@Test
void testFindByProductoInventarioId_InvalidId() throws Exception {
    when(ingredienteService.getIngredientesByProductoInventarioId(-1)).thenThrow(new IllegalArgumentException("Invalid ID"));
    
    mockMvc.perform(get("/api/ingredientes/productoInventario/-1")
            .with(csrf()))
        .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
        .andExpect(result -> assertEquals("Invalid ID", result.getResolvedException().getMessage()));
    
    verify(ingredienteService).getIngredientesByProductoInventarioId(-1);
}

    // TESTS PARA findByProductoVentaId()

    @Test
    void testFindByProductoVentaId_Success() throws Exception {
        List<Ingrediente> ingredientesProductoVenta1 = List.of(ingrediente1);
        when(ingredienteService.getIngredientesByProductoVentaId(1)).thenReturn(ingredientesProductoVenta1);
        
        mockMvc.perform(get("/api/ingredientes/productoVenta/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].productoVenta.id").value(1));
        
        verify(ingredienteService).getIngredientesByProductoVentaId(1);
    }

    @Test
    void testFindByProductoVentaId_Empty() throws Exception {
        when(ingredienteService.getIngredientesByProductoVentaId(99)).thenReturn(null);
        
        mockMvc.perform(get("/api/ingredientes/productoVenta/99"))
            .andExpect(status().isNotFound());
        
        verify(ingredienteService).getIngredientesByProductoVentaId(99);
    }

    @SuppressWarnings("null")
    @Test
    void testFindByProductoVentaId_InvalidId() throws Exception {
        when(ingredienteService.getIngredientesByProductoVentaId(-1)).thenThrow(new IllegalArgumentException("Invalid ID"));
        
        mockMvc.perform(get("/api/ingredientes/productoVenta/-1")
                .with(csrf()))
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
            .andExpect(result -> assertEquals("Invalid ID", result.getResolvedException().getMessage()));
        
        verify(ingredienteService).getIngredientesByProductoVentaId(-1);
    }
    // TESTS PARA save()

    @Test
    void testSave_Success() throws Exception {
        Ingrediente nuevoIngrediente = new Ingrediente();
        nuevoIngrediente.setCantidad(5);
        nuevoIngrediente.setProductoInventario(productoInventario1);
        nuevoIngrediente.setProductoVenta(productoVenta2);
        
        Ingrediente ingredienteGuardado = new Ingrediente();
        ingredienteGuardado.setId(3);
        ingredienteGuardado.setCantidad(5);
        ingredienteGuardado.setProductoInventario(productoInventario1);
        ingredienteGuardado.setProductoVenta(productoVenta2);
        
        when(ingredienteService.save(any(Ingrediente.class))).thenReturn(ingredienteGuardado);
        
        mockMvc.perform(post("/api/ingredientes")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoIngrediente)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(3))
            .andExpect(jsonPath("$.cantidad").value(5))
            .andExpect(jsonPath("$.productoInventario.id").value(1))
            .andExpect(jsonPath("$.productoVenta.id").value(2));
        
        verify(ingredienteService).save(any(Ingrediente.class));
    }

    @Test
    void testSave_MissingRequiredFields() throws Exception {
        mockMvc.perform(post("/api/ingredientes")
        .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ingredienteInvalido)))
            .andExpect(status().isBadRequest());
        
        verify(ingredienteService, never()).save(any(Ingrediente.class));
    }

@Test
void testSave_NullRequest() throws Exception {
    mockMvc.perform(post("/api/ingredientes")
        .with(csrf())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(result -> assertTrue(
            result.getResolvedException() instanceof HttpMessageNotReadableException));
    
    verify(ingredienteService, never()).save(any(Ingrediente.class));
}

    @Test
    void testSave_ServiceError() throws Exception {
        Ingrediente nuevoIngrediente = new Ingrediente();
        nuevoIngrediente.setCantidad(5);
        nuevoIngrediente.setProductoInventario(productoInventario1);
        nuevoIngrediente.setProductoVenta(productoVenta2);
        
        when(ingredienteService.save(any(Ingrediente.class))).thenThrow(new RuntimeException("Database error"));
        
        mockMvc.perform(post("/api/ingredientes")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoIngrediente)))
            .andExpect(status().isInternalServerError());
        
        verify(ingredienteService).save(any(Ingrediente.class));
    }

    // TESTS PARA update()

    @Test
    void testUpdate_Success() throws Exception {
        Ingrediente ingredienteActualizado = new Ingrediente();
        ingredienteActualizado.setCantidad(10);
        ingredienteActualizado.setProductoInventario(productoInventario2);
        ingredienteActualizado.setProductoVenta(productoVenta1);
        
        Ingrediente ingredienteGuardado = new Ingrediente();
        ingredienteGuardado.setId(1);
        ingredienteGuardado.setCantidad(10);
        ingredienteGuardado.setProductoInventario(productoInventario2);
        ingredienteGuardado.setProductoVenta(productoVenta1);
        
        when(ingredienteService.getById(1)).thenReturn(ingrediente1);
        when(ingredienteService.save(any(Ingrediente.class))).thenReturn(ingredienteGuardado);
        
        mockMvc.perform(put("/api/ingredientes/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ingredienteActualizado)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.cantidad").value(10))
            .andExpect(jsonPath("$.productoInventario.id").value(2))
            .andExpect(jsonPath("$.productoVenta.id").value(1));
        
        verify(ingredienteService).getById(1);
        verify(ingredienteService).save(any(Ingrediente.class));
    }

    @Test
    void testUpdate_NotFound() throws Exception {
        Ingrediente ingredienteActualizado = new Ingrediente();
        ingredienteActualizado.setCantidad(10);
        ingredienteActualizado.setProductoInventario(productoInventario2);
        ingredienteActualizado.setProductoVenta(productoVenta1);
        
        when(ingredienteService.getById(999)).thenReturn(null);
        
        mockMvc.perform(put("/api/ingredientes/999")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ingredienteActualizado)))
            .andExpect(status().isNotFound());
        
        verify(ingredienteService).getById(999);
        verify(ingredienteService, never()).save(any(Ingrediente.class));
    }

    @Test
    void testUpdate_MissingRequiredFields() throws Exception {
        when(ingredienteService.getById(1)).thenReturn(ingrediente1);
        
        mockMvc.perform(put("/api/ingredientes/1")
        .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ingredienteInvalido)))
            .andExpect(status().isBadRequest());
        
        verify(ingredienteService, never()).save(any(Ingrediente.class));
    }

    @Test
    void testUpdate_InvalidId() throws Exception {
        Ingrediente ingredienteActualizado = new Ingrediente();
        ingredienteActualizado.setCantidad(10);
        ingredienteActualizado.setProductoInventario(productoInventario2);
        ingredienteActualizado.setProductoVenta(productoVenta1);
        
        when(ingredienteService.getById(999)).thenThrow(new NumberFormatException("Invalid ID"));
        
        mockMvc.perform(put("/api/ingredientes/invalid")    
        .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ingredienteActualizado)))
            .andExpect(status().isBadRequest());
        
       
    }

    // TESTS PARA delete()

    @Test
    void testDelete_Success() throws Exception {
        when(ingredienteService.getById(1)).thenReturn(ingrediente1);
        doNothing().when(ingredienteService).deleteById(1);
        
        mockMvc.perform(delete("/api/ingredientes/1").with(csrf()))
            .andExpect(status().isNoContent());
        
        verify(ingredienteService).getById(1);
        verify(ingredienteService).deleteById(1);
    }

    @Test
    void testDelete_NotFound() throws Exception {
        when(ingredienteService.getById(999)).thenReturn(null);
        
        mockMvc.perform(delete("/api/ingredientes/999").with(csrf()))
            .andExpect(status().isNotFound());
        
        verify(ingredienteService).getById(999);
        verify(ingredienteService, never()).deleteById(999);
    }

    @Test
    void testDelete_InvalidId() throws Exception {
        
        mockMvc.perform(delete("/api/ingredientes/invalid").with(csrf()))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testDelete_ServiceError() throws Exception {
        when(ingredienteService.getById(1)).thenReturn(ingrediente1);
        doThrow(new RuntimeException("Database error")).when(ingredienteService).deleteById(1);
        
        mockMvc.perform(delete("/api/ingredientes/1")
        .with(csrf()))
            .andExpect(status().isInternalServerError());
        
        verify(ingredienteService).getById(1);
        verify(ingredienteService).deleteById(1);
    }
}