package ispp_g2.gastrostock.testCategoria;

import com.fasterxml.jackson.databind.ObjectMapper;
import ispp_g2.gastrostock.categorias.Categoria;
import ispp_g2.gastrostock.categorias.CategoriaController;
import ispp_g2.gastrostock.categorias.CategoriaService;
import ispp_g2.gastrostock.categorias.Pertenece;
import ispp_g2.gastrostock.config.SecurityConfiguration;
import ispp_g2.gastrostock.config.jwt.JwtAuthFilter;
import ispp_g2.gastrostock.config.jwt.JwtService;
import ispp_g2.gastrostock.empleado.EmpleadoService;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioService;
import ispp_g2.gastrostock.user.Authorities;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserService;

import java.util.Collections;
import java.util.List;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoriaController.class)
@Import({SecurityConfiguration.class, JwtAuthFilter.class})
@ActiveProfiles("test")
@WithMockUser(username = "admin", roles = {"admin"})
public class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;

    // Servicios que inyecta CategoriaController:
    @MockBean
    private CategoriaService categoriaService;

    @MockBean
    private NegocioService negocioService;

    @MockBean
    private UserService userService;

    @MockBean
    private EmpleadoService empleadoService;

    // Estos son del filtro de seguridad:
    @MockBean private JwtService jwtService;
    @MockBean private AuthenticationProvider authenticationProvider;
    @MockBean private UserDetailsService userDetailsService;
    
    private Categoria categoria;
    private Negocio negocio;
    private User adminUser;

    @BeforeEach
    void setUp() {
        // Negocio y categoría de prueba
        negocio = new Negocio();
        negocio.setId(1);

        categoria = new Categoria();
        categoria.setId(1);
        categoria.setName("Bebidas");
        categoria.setNegocio(negocio);
        categoria.setPertenece(Pertenece.INVENTARIO);

        // Usuario admin simulado
        adminUser = new User();
        adminUser.setId(1);
        Authorities auth = new Authorities();
        auth.setAuthority("admin");
        adminUser.setAuthority(auth);

        // Mocks de seguridad y usuario
        when(jwtService.getUserNameFromJwtToken(anyString())).thenReturn("admin");
        when(jwtService.validateJwtToken(anyString(), any())).thenReturn(true);
        when(userService.findCurrentUser()).thenReturn(adminUser);
    }

    @Test
    public void testFindAll_ReturnsCategorias() throws Exception {
        when(categoriaService.getCategorias()).thenReturn(List.of(categoria));
        
        mockMvc.perform(get("/api/categorias")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].name", is("Bebidas")));
        
        verify(categoriaService, times(2)).getCategorias();
        }
    
    @Test
    public void testFindAll_ReturnsNoContent() throws Exception {
        when(categoriaService.getCategorias()).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/api/categorias")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
        
        verify(categoriaService).getCategorias();
    }
    
    // GET /api/categorias/{id}
    @Test
    public void testFindById_ExistingCategoria() throws Exception {
        when(categoriaService.getById(1)).thenReturn(categoria);
        
        mockMvc.perform(get("/api/categorias/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("Bebidas")));
        
        verify(categoriaService).getById(1);
    }
    
    @Test
    public void testFindById_NonExistingCategoria() throws Exception {
        when(categoriaService.getById(99)).thenReturn(null);
    
        mockMvc.perform(get("/api/categorias/99")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())          
            .andExpect(content().string(""));
    
        verify(categoriaService).getById(99);
    }
    
    
    // GET /api/categorias/negocio/{negocioId}
    @Test
    public void testFindByNegocioId_ReturnsCategorias() throws Exception {
        when(categoriaService.getCategoriasByNegocioId(1)).thenReturn(List.of(categoria));
        
        mockMvc.perform(get("/api/categorias/negocio/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));
        
        verify(categoriaService, times(2)).getCategoriasByNegocioId(1);
    }
    
    @Test
    public void testFindByNegocioId_ReturnsNoContent() throws Exception {
        when(categoriaService.getCategoriasByNegocioId(1)).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/api/categorias/negocio/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
        
        verify(categoriaService).getCategoriasByNegocioId(1);
    }
    
    // GET /api/categorias/negocio/{negocioId}/inventario
    @Test
    public void testFindByNegocioIdInventario_ReturnsCategorias() throws Exception {
        when(categoriaService.getCategoriasInventarioByNegocioId(1))
            .thenReturn(List.of(categoria));
        
        mockMvc.perform(get("/api/categorias/negocio/1/inventario")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())            // ahora espera 200
            .andExpect(jsonPath("$", hasSize(1)));
        
        verify(categoriaService, times(2)).getCategoriasInventarioByNegocioId(1);
    }
    
    
    @Test
    public void testFindByNegocioIdInventario_ReturnsNoContent() throws Exception {
        when(categoriaService.getCategoriasInventarioByNegocioId(1))
            .thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/api/categorias/negocio/1/inventario")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
        
        verify(categoriaService).getCategoriasInventarioByNegocioId(1);
    }
    
    
    // GET /api/categorias/negocio/{negocioId}/venta
    @Test
    public void testFindByNegocioIdVenta_ReturnsNoContentForEmptyListAsAdmin() throws Exception {
        when(categoriaService.getCategoriasVentaByNegocioId(1))
            .thenReturn(Collections.emptyList());
    
        mockMvc.perform(get("/api/categorias/negocio/1/venta")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));
    
        verify(categoriaService).getCategoriasVentaByNegocioId(1);
    }
    
    
    // GET /api/categorias/nombre/{name}
    @Test
    public void testFindByName_ReturnsCategorias() throws Exception {
        when(categoriaService.getCategoriasByName("Bebidas"))
            .thenReturn(List.of(categoria));
    
        mockMvc.perform(get("/api/categorias/nombre/Bebidas")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));
    
        verify(categoriaService, times(2)).getCategoriasByName("Bebidas");
    }
    
    
    @Test
    public void testFindByName_ReturnsNoContent() throws Exception {
        when(categoriaService.getCategoriasByName("NoExiste")).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/api/categorias/nombre/NoExiste")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
        
        verify(categoriaService).getCategoriasByName("NoExiste");
    }
    
    // POST /api/categorias
    @Test
    public void testSave_ReturnsCreated() throws Exception {
        when(categoriaService.save(any(Categoria.class))).thenReturn(categoria);
        
        mockMvc.perform(post("/api/categorias")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoria)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name", is("Bebidas")));
        
        verify(categoriaService).save(any(Categoria.class));
    }
    
    @Test
    public void testSave_ReturnsBadRequestWhenNull() throws Exception {
        // Enviar un JSON vacío debería provocar error de validación
        mockMvc.perform(post("/api/categorias")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }
    
    // PUT /api/categorias/{id}
    @Test
    public void testUpdate_ExistingCategoria() throws Exception {
        Categoria categoriaActualizada = new Categoria();
        categoriaActualizada.setId(1);
        categoriaActualizada.setName("Bebidas Actualizadas");
        categoriaActualizada.setNegocio(negocio);
        categoriaActualizada.setPertenece(Pertenece.INVENTARIO);
        
        when(categoriaService.getById(1)).thenReturn(categoria);
        when(categoriaService.update(eq(1), any(Categoria.class)))
            .thenReturn(categoriaActualizada);
        
        mockMvc.perform(put("/api/categorias/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoriaActualizada)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("Bebidas Actualizadas")));
        
        verify(categoriaService).getById(1);
        verify(categoriaService).update(eq(1), any(Categoria.class));
        verify(categoriaService, never()).save(any());
    }
    
    
    @Test
    public void testUpdate_NonExistingCategoria() throws Exception {
        when(categoriaService.getById(99)).thenReturn(null);
        when(categoriaService.update(eq(99), any(Categoria.class)))
            .thenReturn(categoria);

        mockMvc.perform(put("/api/categorias/99")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoria)))
            .andExpect(status().isOk())   
            .andExpect(jsonPath("$.name", is("Bebidas")));  // y el cuerpo con la categoría

        verify(categoriaService).getById(99);
        verify(categoriaService).update(eq(99), any(Categoria.class));
        verify(categoriaService, never()).save(any());
    }

    
    @Test
    public void testUpdate_NullCategoria() throws Exception {
        mockMvc.perform(put("/api/categorias/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }
    
    // DELETE /api/categorias/{id}
    @Test
    public void testDelete_ExistingCategoria() throws Exception {
        doNothing().when(categoriaService).delete(1);
        
        mockMvc.perform(delete("/api/categorias/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
        
        verify(categoriaService).delete(1);
        verify(categoriaService, never()).getById(anyInt());
    }

    
    @Test
    public void testDelete_NonExistingCategoria() throws Exception {
        doNothing().when(categoriaService).delete(99);

        mockMvc.perform(delete("/api/categorias/99")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        verify(categoriaService).delete(99);
        verify(categoriaService, never()).getById(anyInt());
    }

}
