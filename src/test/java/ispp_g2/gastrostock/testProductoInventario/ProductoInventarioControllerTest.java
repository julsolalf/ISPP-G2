package ispp_g2.gastrostock.testProductoInventario;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import ispp_g2.gastrostock.user.Authorities;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserService;
import ispp_g2.gastrostock.empleado.EmpleadoService;
import ispp_g2.gastrostock.dueno.Dueno;
import ispp_g2.gastrostock.dueno.DuenoService;
import ispp_g2.gastrostock.categorias.CategoriaService;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioService;
import ispp_g2.gastrostock.categorias.Categoria;

import ispp_g2.gastrostock.config.SecurityConfiguration;
import ispp_g2.gastrostock.config.jwt.JwtAuthFilter;
import ispp_g2.gastrostock.config.jwt.JwtService;

import ispp_g2.gastrostock.productoInventario.ProductoInventario;
import ispp_g2.gastrostock.productoInventario.ProductoInventarioController;
import ispp_g2.gastrostock.productoInventario.ProductoInventarioDTO;
import ispp_g2.gastrostock.productoInventario.ProductoInventarioService;
import ispp_g2.gastrostock.productoVenta.ProductoVenta;
import ispp_g2.gastrostock.productoVenta.ProductoVentaDTO;

import org.mockito.InjectMocks;

@WebMvcTest(ProductoInventarioController.class)
@Import({SecurityConfiguration.class, JwtAuthFilter.class})
@ActiveProfiles("test")
@WithMockUser(username = "admin", roles = {"admin"})
class ProductoInventarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoInventarioService productoInventarioService;

    @MockBean
    private UserService userService;
    
    @MockBean
    private EmpleadoService empleadoService;
    
    @MockBean
    private DuenoService duenoService;
    
    @MockBean 
    private CategoriaService categoriaService;
    
    @MockBean
    private NegocioService negocioService;

    @InjectMocks
    private ProductoInventarioController productoInventarioController;
        
    @MockBean
    private JwtService jwtService;
    
    @MockBean
    private AuthenticationProvider authenticationProvider;
    
    @MockBean
    private UserDetailsService userDetailsService;
    

    private ProductoInventario sampleProduct;
  

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
        sampleProduct = new ProductoInventario ();
        sampleProduct.setId(1);
        sampleProduct.setName("Cerveza");
        sampleProduct.setCategoria(bebidas);
        sampleProduct.setPrecioCompra(10.5);;
    
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
        when(productoInventarioService.getProductosInventario()).thenReturn(List.of(sampleProduct));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/productosInventario"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void testFindAll_WhenNoData() throws Exception {
        when(productoInventarioService.getProductosInventario()).thenReturn(List.of());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/productosInventario"))
            .andExpect(status().isNoContent());
    }

    @Test
    void testFindProductoInventario_Found() throws Exception {
        when(productoInventarioService.getById(1)).thenReturn(sampleProduct);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/productosInventario/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Cerveza"));
    }

    @Test
    void testFindProductoInventario_NotFound() throws Exception {
        when(productoInventarioService.getById(99)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/productosInventario/99"))
            .andExpect(status().isNotFound());
    }

@Test
void testCreateProductoInventario_Success() throws Exception {

    when(categoriaService.getById(1)).thenReturn(bebidas);

    // Stubear la conversión del DTO a ProductoVenta
    when(productoInventarioService.convertirDTOProductoInventario(any(ProductoInventarioDTO.class)))
        .thenReturn(sampleProduct);

    // Stubear el save del productoVentaService
    when(productoInventarioService.save(any())).thenReturn(sampleProduct); 

    mockMvc.perform(MockMvcRequestBuilders.post("/api/productosInventario")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\":\"Cerveza\", \"precioCompra\":10.5, \"cantidadDeseada\":100, \"cantidadAviso\":10, \"categoriaId\":1, \"proveedorId\":1}"))
            .andExpect(status().isCreated());
}

    @Test
    void testCreateProductoInventario_BadRequest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/productosInventario")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"id\":\"1\",\"name\":}")) 
            .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateProductoInventario_Success() throws Exception {
        // Usamos sampleProduct que ya tiene categoría y proveedor seteados en el setup
        when(productoInventarioService.getById(1)).thenReturn(sampleProduct);
    
        // Stub de la conversión del DTO a entidad (producto actualizado)
        ProductoInventario productoActualizado = new ProductoInventario();
        productoActualizado.setId(1);
        productoActualizado.setName("Cerveza Modificada");
        productoActualizado.setPrecioCompra(12.0);
        productoActualizado.setCantidadDeseada(120);
        productoActualizado.setCantidadAviso(12);
        productoActualizado.setCategoria(bebidas);  // Ya creada en setup
    
        when(productoInventarioService.convertirDTOProductoInventario(any(ProductoInventarioDTO.class)))
            .thenReturn(productoActualizado);
    
        when(productoInventarioService.save(any())).thenReturn(productoActualizado);
        when(categoriaService.getById(1)).thenReturn(bebidas);
    
        mockMvc.perform(MockMvcRequestBuilders.put("/api/productosInventario/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{"
                        + "\"name\": \"Cerveza Modificada\","
                        + "\"precioCompra\": 12.0,"
                        + "\"cantidadDeseada\": 120,"
                        + "\"cantidadAviso\": 12,"
                        + "\"categoriaId\": 1,"
                        + "\"proveedorId\": 1"
                        + "}"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateProductoInventario_NotFound() throws Exception {
        when(productoInventarioService.getById(99)).thenReturn(null);
    
        mockMvc.perform(MockMvcRequestBuilders.put("/api/productosInventario/99")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\":\"No Existente\", \"precioCompra\":10.0, \"cantidadDeseada\":50, \"cantidadAviso\":5, \"categoriaId\":1, \"proveedorId\":1}"))
            .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteProductoInventario_Success() throws Exception {
        when(productoInventarioService.getById(1)).thenReturn(sampleProduct);
        doNothing().when(productoInventarioService).delete(1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/productosInventario/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteProductoInventario_NotFound() throws Exception {
        when(productoInventarioService.getById(99)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/productosInventario/99"))
            .andExpect(status().isNotFound());
    }


    @Test
    void testUpdateProductoInventario_BadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/productosInventario/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"id\":\"1\", \"name\":\"\", \"categoriaInventario\": {\"id\": 1, \"nombre\": \"\"}, \"precioCompra\": -1, \"cantidadDeseada\": -10, \"cantidadAviso\": -5}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateProductoInventario_InvalidData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/productosInventario")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"id\":\"1\", \"name\":\"\", \"categoriaInventario\": {\"id\": 1, \"nombre\": \"\"}, \"precioCompra\": -10.5, \"cantidadDeseada\": -100, \"cantidadAviso\": -10}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testInternalServerError() throws Exception {
        when(productoInventarioService.getById(1)).thenThrow(new RuntimeException("Error interno"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/productosInventario/1"))
            .andExpect(status().isInternalServerError());
    }

    @Test
    void testGetProductoInventario_ByName() throws Exception {
    
        when(productoInventarioService.getByName("Cerveza")).thenReturn(sampleProduct);
    
        mockMvc.perform(MockMvcRequestBuilders.get("/api/productosInventario/name/Cerveza"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Cerveza")); 
    }
    

    @Test
    void testGetProductoInventario_InvalidId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/productosInventario/invalidId"))
            .andExpect(status().isBadRequest());
    }
    
    @Test
    void testDeleteProductoInventario_InvalidId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/productosInventario/invalidId"))
            .andExpect(status().isBadRequest());
    }
}
