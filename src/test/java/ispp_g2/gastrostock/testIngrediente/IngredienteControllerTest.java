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

import ispp_g2.gastrostock.categorias.Categoria;
import ispp_g2.gastrostock.categorias.CategoriaService;
import ispp_g2.gastrostock.config.SecurityConfiguration;
import ispp_g2.gastrostock.config.jwt.JwtAuthFilter;
import ispp_g2.gastrostock.config.jwt.JwtService;
import ispp_g2.gastrostock.dueno.DuenoService;
import ispp_g2.gastrostock.empleado.EmpleadoService;
import ispp_g2.gastrostock.ingrediente.Ingrediente;
import ispp_g2.gastrostock.ingrediente.IngredienteController;
import ispp_g2.gastrostock.ingrediente.IngredienteService;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioService;
import ispp_g2.gastrostock.productoInventario.ProductoInventario;
import ispp_g2.gastrostock.productoInventario.ProductoInventarioService;
import ispp_g2.gastrostock.productoVenta.ProductoVenta;
import ispp_g2.gastrostock.user.Authorities;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserService;

@WebMvcTest({IngredienteController.class})
@Import({SecurityConfiguration.class, JwtAuthFilter.class})
@ActiveProfiles("test")
@WithMockUser(username = "admin", roles = {"admin"})
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

    
    @MockBean
    private UserService userService;    
    
    @MockBean
    private EmpleadoService empleadoService;

    @MockBean
    private DuenoService duenoService;

    @MockBean
    private CategoriaService categoriaService;

    @MockBean
    private ProductoInventarioService productoInventarioService;

    @MockBean
    private NegocioService negocioService;
    
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
    private User admin;
    private Authorities adminAuth;

    @BeforeEach
    void setUp() {
        admin = new User();
        adminAuth = new Authorities();
        adminAuth.setAuthority("admin");
        admin.setAuthority(adminAuth);
        productoInventario1 = new ProductoInventario();
        productoInventario1.setId(1);
        productoInventario1.setName("Patatas");

        productoInventario2 = new ProductoInventario();
        productoInventario2.setId(2);
        productoInventario2.setName("Cebollas");

        productoVenta1 = new ProductoVenta();
        productoVenta1.setId(1);
        productoVenta1.setName("Patatas Fritas");

        productoVenta2 = new ProductoVenta();
        productoVenta2.setId(2);
        productoVenta2.setName("Tortilla");

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

        ingredienteInvalido = new Ingrediente();

        ingredientes = Arrays.asList(ingrediente1, ingrediente2);
        when(jwtService.getUserNameFromJwtToken(anyString())).thenReturn("admin");
        when(jwtService.validateJwtToken(anyString(), any())).thenReturn(true);
    }


    @Test
    void testFindAll_Success() throws Exception {


        when(userService.findCurrentUser()).thenReturn(admin);
        when(ingredienteService.getIngredientes()).thenReturn(ingredientes);

        mockMvc.perform(get("/api/ingredientes"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].cantidad").value(2))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[1].cantidad").value(3));

        verify(userService).findCurrentUser();
        verify(ingredienteService, atLeastOnce()).getIngredientes();
    }


    @Test
    void testFindAll_EmptyList() throws Exception {

        when(userService.findCurrentUser()).thenReturn(admin);
        when(ingredienteService.getIngredientes()).thenReturn(Collections.emptyList());
    
        mockMvc.perform(get("/api/ingredientes"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json("[]")); 
    
        verify(ingredienteService).getIngredientes();
    }
    

    @Test
    void testFindAll_ServiceError() throws Exception {
        when(userService.findCurrentUser()).thenReturn(admin);
        when(ingredienteService.getIngredientes()).thenThrow(new RuntimeException("Database error"));
        
        mockMvc.perform(get("/api/ingredientes"))
            .andExpect(status().isInternalServerError());
        
        verify(ingredienteService).getIngredientes();
    }


    @Test
    void testFindById_Success() throws Exception {
        when(userService.findCurrentUser()).thenReturn(admin);
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
        when(userService.findCurrentUser()).thenReturn(admin);
        when(ingredienteService.getById(999)).thenReturn(null);
    
        mockMvc.perform(get("/api/ingredientes/999"))
            .andExpect(status().isOk())
            .andExpect(content().string("")); 
    
        verify(ingredienteService).getById(999);
    }

    @Test
    void testFindById_InvalidId() throws Exception {
        mockMvc.perform(get("/api/ingredientes/invalid"))
            .andExpect(status().isBadRequest());
    }


    @Test
    void testFindByCantidad_Success() throws Exception {
        when(userService.findCurrentUser()).thenReturn(admin);
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
        when(userService.findCurrentUser()).thenReturn(admin);
        when(ingredienteService.getIngredientesByCantidad(0)).thenReturn(null);
    
        mockMvc.perform(get("/api/ingredientes/cantidad/0"))
            .andExpect(status().isOk())
            .andExpect(content().string(""));
    
        verify(ingredienteService).getIngredientesByCantidad(0);
    }
   
    @Test
    void testFindByCantidad_NegativeValue() throws Exception {
        when(userService.findCurrentUser()).thenReturn(admin);
        when(ingredienteService.getIngredientesByCantidad(-1)).thenReturn(Collections.emptyList());
    
        mockMvc.perform(get("/api/ingredientes/cantidad/-1"))
            .andExpect(status().isOk()) 
            .andExpect(content().json("[]"));  
        
        verify(ingredienteService).getIngredientesByCantidad(-1);
    }
    
    
    @Test
    void testFindByProductoInventarioId_Success() throws Exception {
        List<Ingrediente> ingredientesProducto1 = List.of(ingrediente1);
        when(userService.findCurrentUser()).thenReturn(admin);
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
        when(userService.findCurrentUser()).thenReturn(admin);
        when(ingredienteService.getIngredientesByProductoInventarioId(99)).thenReturn(null);
        
        mockMvc.perform(get("/api/ingredientes/productoInventario/99"))
            .andExpect(status().isOk())
            .andExpect(content().string(""));
        
        verify(ingredienteService).getIngredientesByProductoInventarioId(99);
    }

    @SuppressWarnings("null")
    @Test
    void testFindByProductoInventarioId_InvalidId() throws Exception {
        when(userService.findCurrentUser()).thenReturn(admin);
        when(ingredienteService.getIngredientesByProductoInventarioId(-1)).thenThrow(new IllegalArgumentException("Invalid ID"));
        
        mockMvc.perform(get("/api/ingredientes/productoInventario/-1")
                .with(csrf()))
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
            .andExpect(result -> assertEquals("Invalid ID", result.getResolvedException().getMessage()));
        
        verify(ingredienteService).getIngredientesByProductoInventarioId(-1);
    }


    @Test
    void testFindByProductoVentaId_Success() throws Exception {
        List<Ingrediente> ingredientesProductoVenta1 = List.of(ingrediente1);
        when(userService.findCurrentUser()).thenReturn(admin);
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
        when(userService.findCurrentUser()).thenReturn(admin);
        when(ingredienteService.getIngredientesByProductoVentaId(99)).thenReturn(null);
        
        mockMvc.perform(get("/api/ingredientes/productoVenta/99"))
            .andExpect(status().isOk())
            .andExpect(content().string(""));
        
        verify(ingredienteService).getIngredientesByProductoVentaId(99);
    }

    @SuppressWarnings("null")
    @Test
    void testFindByProductoVentaId_InvalidId() throws Exception {
        when(userService.findCurrentUser()).thenReturn(admin);
        when(ingredienteService.getIngredientesByProductoVentaId(-1)).thenThrow(new IllegalArgumentException("Invalid ID"));
        
        mockMvc.perform(get("/api/ingredientes/productoVenta/-1")
                .with(csrf()))
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
            .andExpect(result -> assertEquals("Invalid ID", result.getResolvedException().getMessage()));
        
        verify(ingredienteService).getIngredientesByProductoVentaId(-1);
    }

    @Test
    void testSave_Success() throws Exception {
        Categoria categoria = new Categoria();
        categoria.setId(1);
        Negocio negocioMock = new Negocio();
        negocioMock.setId(1);
        categoria.setNegocio(negocioMock);

        productoInventario1.setCategoria(categoria);

        Ingrediente nuevoIngrediente = new Ingrediente();
        nuevoIngrediente.setCantidad(5);
        nuevoIngrediente.setProductoInventario(productoInventario1);
        nuevoIngrediente.setProductoVenta(productoVenta2);

        Ingrediente ingredienteGuardado = new Ingrediente();
        ingredienteGuardado.setId(3);
        ingredienteGuardado.setCantidad(5);
        ingredienteGuardado.setProductoInventario(productoInventario1);
        ingredienteGuardado.setProductoVenta(productoVenta2);

        when(userService.findCurrentUser()).thenReturn(admin);
        when(productoInventarioService.getById(1)).thenReturn(productoInventario1);
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

        verify(productoInventarioService).getById(1);
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
        Categoria categoria = new Categoria();
        categoria.setId(1);
        Negocio negocioMock = new Negocio();
        negocioMock.setId(1);
        categoria.setNegocio(negocioMock);
        productoInventario1.setCategoria(categoria);

        Ingrediente nuevoIngrediente = new Ingrediente();
        nuevoIngrediente.setCantidad(5);
        nuevoIngrediente.setProductoInventario(productoInventario1);
        nuevoIngrediente.setProductoVenta(productoVenta2);

        when(userService.findCurrentUser()).thenReturn(admin);
        when(productoInventarioService.getById(1)).thenReturn(productoInventario1);
        when(ingredienteService.save(any(Ingrediente.class)))
            .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(post("/api/ingredientes")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoIngrediente)))
            .andExpect(status().isInternalServerError());

        verify(productoInventarioService).getById(1);
        verify(ingredienteService).save(any(Ingrediente.class));
    }

    
/* 
    @Test
    void testUpdateIngredienteAuthorized() throws Exception {
        // 1) Construyo el grafo de objetos completo:
        // — Dueno y Negocio
        Dueno dueno = new Dueno();
        dueno.setId(10);
        Negocio negocio = new Negocio();
        negocio.setId(20);
        negocio.setDueno(dueno);
        
        // — Categoria asociada al Negocio
        Categoria categoria = new Categoria();
        categoria.setId(30);
        categoria.setNegocio(negocio);
        
        // — ProductoInventario existente y actualizado
        productoInventario1.setCategoria(categoria);  // IMPORTANTE
        // — El ingrediente “viejo” que devuelve ingredienteService.getById
        ingrediente1.setProductoInventario(productoInventario1);
        
        // — El ingrediente que esperamos salvar
        Ingrediente updated = new Ingrediente();
        updated.setId(1);
        updated.setCantidad(5);
        updated.setProductoInventario(productoInventario1);
        updated.setProductoVenta(productoVenta1);
    
        // 2) Mockeo todos los servicios que entra en juego:
        when(userService.findCurrentUser()).thenReturn(admin);
        // currDueno en el método
        when(duenoService.getDuenoByUser(admin.getId())).thenReturn(dueno);
        // el ingrediente “viejo”
        when(ingredienteService.getById(1)).thenReturn(ingrediente1);
        // el lookup de la categoría/négocio
        when(categoriaService.getById(categoria.getId()))
            .thenReturn(categoria);
        // la lista de negocios del dueño (contiene el negocio)
        when(negocioService.getByDueno(dueno.getId()))
            .thenReturn(Collections.singletonList(negocio));
        // finalmente, el save
        when(ingredienteService.save(any(Ingrediente.class)))
            .thenReturn(updated);
    
        // 3) Disparo el MockMvc (y ojo a la URL: asumo que tu controller está mapeado en "/ingredientes")
        mockMvc.perform(put("/ingredientes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated))
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.cantidad").value(5));
    }

    @Test
    void testUpdate_NotFound() throws Exception {
        Ingrediente ingredienteActualizado = new Ingrediente();
        ingredienteActualizado.setCantidad(10);
        ingredienteActualizado.setProductoInventario(productoInventario2);
        ingredienteActualizado.setProductoVenta(productoVenta1);
        
        when(ingredienteService.getById(999)).thenReturn(null);
        when(userService.findCurrentUser()).thenReturn(admin);

        mockMvc.perform(put("/api/ingredientes/999")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ingredienteActualizado)))
            .andExpect(status().isNotFound());
        
        verify(ingredienteService).getById(999);
        verify(ingredienteService, never()).save(any(Ingrediente.class));
    }
    */
    @Test
    void testUpdate_MissingRequiredFields() throws Exception {
        when(ingredienteService.getById(1)).thenReturn(ingrediente1);
        when(userService.findCurrentUser()).thenReturn(admin);

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
        when(userService.findCurrentUser()).thenReturn(admin);

        mockMvc.perform(put("/api/ingredientes/invalid")    
        .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ingredienteActualizado)))
            .andExpect(status().isBadRequest());
        
       
    }

/* 
    @Test
    void testDelete_Success() throws Exception {
        when(ingredienteService.getById(1)).thenReturn(ingrediente1);
        doNothing().when(ingredienteService).deleteById(1);
        when(userService.findCurrentUser()).thenReturn(admin);

        mockMvc.perform(delete("/api/ingredientes/1").with(csrf()))
            .andExpect(status().isNoContent());
        
        verify(ingredienteService).getById(1);
        verify(ingredienteService).deleteById(1);
    }
*/
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
/* 
    @Test
    void testDelete_ServiceError() throws Exception {
        when(ingredienteService.getById(1)).thenReturn(ingrediente1);
        doThrow(new RuntimeException("Database error")).when(ingredienteService).deleteById(1);
        when(userService.findCurrentUser()).thenReturn(admin);

        mockMvc.perform(delete("/api/ingredientes/1")
        .with(csrf()))
            .andExpect(status().isInternalServerError());
        
        verify(ingredienteService).getById(1);
        verify(ingredienteService).deleteById(1);
    }
    */
}