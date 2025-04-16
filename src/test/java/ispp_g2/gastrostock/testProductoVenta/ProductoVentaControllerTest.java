package ispp_g2.gastrostock.testProductoVenta;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import ispp_g2.gastrostock.categorias.Categoria;
import ispp_g2.gastrostock.categorias.CategoriaService;
import ispp_g2.gastrostock.config.SecurityConfiguration;
import ispp_g2.gastrostock.config.jwt.JwtAuthFilter;
import ispp_g2.gastrostock.config.jwt.JwtService;
import ispp_g2.gastrostock.dueno.Dueno;
import ispp_g2.gastrostock.dueno.DuenoService;
import ispp_g2.gastrostock.empleado.EmpleadoService;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioService;
import ispp_g2.gastrostock.productoVenta.ProductoVenta;
import ispp_g2.gastrostock.productoVenta.ProductoVentaController;
import ispp_g2.gastrostock.productoVenta.ProductoVentaDTO;
import ispp_g2.gastrostock.productoVenta.ProductoVentaService;
import ispp_g2.gastrostock.user.Authorities;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserService;

import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@WebMvcTest(ProductoVentaController.class)
@Import({SecurityConfiguration.class, JwtAuthFilter.class})
@ActiveProfiles("test")
@WithMockUser(username = "admin", roles = {"admin"})
class ProductoVentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoVentaService productoVentaService;

    @InjectMocks
    private ProductoVentaController productoVentaController;

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
    private NegocioService negocioService;
    
    @MockBean
    private CategoriaService categoriaService;

    private ProductoVenta sampleProductoVenta;

    private Categoria bebidas;
    private Negocio negocio;
    @BeforeEach
    void setUp() {
        // Configurar una categoría válida con negocio
        bebidas = new Categoria();
        bebidas.setId(1);  // Asegurate de setear el ID, ya que el DTO usará categoriaId:1
        bebidas.setName("Bebidas");
        
        negocio = new Negocio();
        negocio.setId(100);
        // (Opcional) Seteá otros atributos del negocio
        bebidas.setNegocio(negocio);
        
        // Configurar el producto de venta con la categoría definida
        sampleProductoVenta = new ProductoVenta();
        sampleProductoVenta.setId(1);
        sampleProductoVenta.setName("Cerveza");
        sampleProductoVenta.setCategoria(bebidas);
        sampleProductoVenta.setPrecioVenta(10.5);
    
        // Stubear el jwtService, etc.
        when(jwtService.getUserNameFromJwtToken(anyString())).thenReturn("admin");
        when(jwtService.validateJwtToken(anyString(), any())).thenReturn(true);
    
        // Stubear userService para que retorne un usuario admin consistente
        User adminUser = new User();
        adminUser.setId(1);
        adminUser.setUsername("admin");
        adminUser.setPassword("adminpass");
        Authorities authAdmin = new Authorities();
        authAdmin.setId(1);
        authAdmin.setAuthority("admin");
        adminUser.setAuthority(authAdmin);
        when(userService.findCurrentUser()).thenReturn(adminUser);
    }
    @Test
    void testFindAll_WhenDataExists() throws Exception {

        when(productoVentaService.getProductosVenta()).thenReturn(List.of(sampleProductoVenta));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/productosVenta"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void testFindAll_WhenNoData() throws Exception {
        when(productoVentaService.getProductosVenta()).thenReturn(List.of());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/productosVenta"))
            .andExpect(status().isNoContent());
    }

    @Test
    void testFindProductoVenta_Found() throws Exception {
        when(productoVentaService.getById(1)).thenReturn(sampleProductoVenta);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/productosVenta/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Cerveza"));
    }

    @Test
    void testFindProductoVenta_NotFound() throws Exception {
        when(productoVentaService.getById(99)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/productosVenta/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void testCreateProductoVenta_Success() throws Exception {
        when(categoriaService.getById(1)).thenReturn(bebidas);

        // Stubear la conversión del DTO a ProductoVenta
        when(productoVentaService.convertirDTOProductoVenta(any(ProductoVentaDTO.class)))
            .thenReturn(sampleProductoVenta);

        // Stubear el save del productoVentaService
        when(productoVentaService.save(any())).thenReturn(sampleProductoVenta);    
        mockMvc.perform(MockMvcRequestBuilders.post("/api/productosVenta")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\":\"Cerveza\", \"precioVenta\":10.5, \"categoriaId\":1}"))
            .andExpect(status().isCreated());
    }

    @Test
    void testCreateProductoVenta_BadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/productosVenta")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}")) // JSON vacío
            .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateProductoVenta_Success() throws Exception {

        // Stubear el método de categoriaService para el ID 1
        when(categoriaService.getById(1)).thenReturn(bebidas);
        
        // Actualizar sampleProductoVenta para que tenga la categoría 'bebidas' con negocio
        sampleProductoVenta.setCategoria(bebidas);
        
        // Stubear la obtención del producto existente
        when(productoVentaService.getById(1)).thenReturn(sampleProductoVenta);
        
        // Stubear la conversión del DTO a entidad: 
        ProductoVenta productoActualizado = new ProductoVenta();
        productoActualizado.setId(1);
        productoActualizado.setName("Cerveza Modificada");
        productoActualizado.setPrecioVenta(12.0);
        // Asegurarse de que el producto actualizado tiene la misma categoría
        productoActualizado.setCategoria(bebidas);
        when(productoVentaService.convertirDTOProductoVenta(any(ProductoVentaDTO.class)))
            .thenReturn(productoActualizado);
        
        // Stubear el guardado del producto
        when(productoVentaService.save(any())).thenReturn(productoActualizado);
        
        // Ejecutar la petición PUT con el JSON correcto (usando "categoriaId")
        mockMvc.perform(MockMvcRequestBuilders.put("/api/productosVenta/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Cerveza Modificada\", \"categoriaId\":1, \"precioVenta\":12.0}"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateProductoVenta_NotFound() throws Exception {
        when(productoVentaService.getById(99)).thenReturn(null);
    
        mockMvc.perform(MockMvcRequestBuilders.put("/api/productosVenta/99")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\":\"No Existente\", \"categoriaId\":1, \"precioVenta\":10.0}"))
            .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteProductoVenta_Success() throws Exception {
        when(productoVentaService.getById(1)).thenReturn(sampleProductoVenta);
        doNothing().when(productoVentaService).delete(1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/productosVenta/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteProductoVenta_NotFound() throws Exception {
        when(productoVentaService.getById(99)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/productosVenta/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void testCreateProductoVenta_InvalidData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/productosVenta")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"id\":\"1\", \"name\":\"\", \"categoriaVenta\": \"\", \"precioVenta\": -10.5}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateProductoVenta_BadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/productosVenta/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"id\":\"1\", \"name\":\"\", \"categoriaVenta\": \"\", \"precioVenta\": -1.0}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testInternalServerError() throws Exception {
        when(productoVentaService.getById(1)).thenThrow(new RuntimeException("Error interno"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/productosVenta/1"))
            .andExpect(status().isInternalServerError());
    }

    @Test
    void testFindByNombre() throws Exception {
        when(productoVentaService.getProductosVentaByNombre("Cerveza")).thenReturn(List.of(sampleProductoVenta));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/productosVenta/nombre/Cerveza"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("Cerveza"));
    }

    @Test
    void testFindByCategoriaVenta() throws Exception {
        when(productoVentaService.getProductosVentaByCategoriaVenta("Bebidas")).thenReturn(List.of(sampleProductoVenta));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/productosVenta/categoriaVenta/Bebidas"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].categoria.name").value("Bebidas"));
    }

    @Test
    void testFindByPrecioVenta() throws Exception {
        when(productoVentaService.getProductosVentaByPrecioVenta(10.5)).thenReturn(List.of(sampleProductoVenta));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/productosVenta/precioVenta/10.5"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].precioVenta").value(10.5));
    }

    @Test
    void testFindByCategoriaVentaAndPrecioVenta() throws Exception {
        when(productoVentaService.getProductosVentaByCategoriaVentaAndPrecioVenta("Bebidas", 10.5))
            .thenReturn(List.of(sampleProductoVenta));
    
        mockMvc.perform(MockMvcRequestBuilders.get("/api/productosVenta/categoriaVenta/Bebidas/precioVenta/10.5"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].categoria.name").value("Bebidas")) 
            .andExpect(jsonPath("$[0].precioVenta").value(10.5));
    }
}
