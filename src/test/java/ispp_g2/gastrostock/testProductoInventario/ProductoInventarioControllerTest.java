package ispp_g2.gastrostock.testProductoInventario;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import ispp_g2.gastrostock.categorias.Categoria;
import ispp_g2.gastrostock.productoInventario.ProductoInventario;
import ispp_g2.gastrostock.productoInventario.ProductoInventarioController;
import ispp_g2.gastrostock.productoInventario.ProductoInventarioService;

import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@WebMvcTest(ProductoInventarioController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false) 
@ActiveProfiles("test")
class ProductoInventarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoInventarioService productoInventarioService;

    @InjectMocks
    private ProductoInventarioController productoInventarioController;

    private ProductoInventario sampleProduct;
    private Categoria categoriaBebidas;

    @BeforeEach
    void setUp() {
        // Crear la categoría
        categoriaBebidas = new Categoria();
        categoriaBebidas.setId(1);
        categoriaBebidas.setName("Bebidas");

        // Crear el producto
        sampleProduct = new ProductoInventario();
        sampleProduct.setId(1);
        sampleProduct.setName("Cerveza");
        sampleProduct.setCategoria(categoriaBebidas);
        sampleProduct.setPrecioCompra(10.5);
        sampleProduct.setCantidadDeseada(100);
        sampleProduct.setCantidadAviso(10);
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
        when(productoInventarioService.getById("1")).thenReturn(sampleProduct);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/productosInventario/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Cerveza"));
    }

    @Test
    void testFindProductoInventario_NotFound() throws Exception {
        when(productoInventarioService.getById("99")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/productosInventario/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void testCreateProductoInventario_Success() throws Exception {
        when(productoInventarioService.save(any())).thenReturn(sampleProduct);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/productosInventario")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"id\":\"1\", \"name\":\"Cerveza\", \"categoriaInventario\": {\"id\": 1, \"nombre\": \"Bebidas\"}, \"precioCompra\":10.5, \"cantidadDeseada\":100, \"cantidadAviso\":10}"))
            .andExpect(status().isCreated());
    }

    @Test
    void testCreateProductoInventario_BadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/productosInventario")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}")) // JSON vacío
            .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateProductoInventario_Success() throws Exception {
        when(productoInventarioService.getById("1")).thenReturn(sampleProduct);
        when(productoInventarioService.save(any())).thenReturn(sampleProduct);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/productosInventario/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"id\":\"1\", \"name\":\"Cerveza Modificada\", \"categoriaInventario\": {\"id\": 1, \"nombre\": \"Bebidas\"}, \"precioCompra\":12.0, \"cantidadDeseada\":120, \"cantidadAviso\":12}"))
            .andExpect(status().isOk());
    }

    @Test
    void testUpdateProductoInventario_NotFound() throws Exception {
        when(productoInventarioService.getById("99")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/productosInventario/99")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"id\":\"99\", \"name\":\"No Existente\", \"categoriaInventario\": {\"id\": 1, \"nombre\": \"Bebidas\"}, \"precioCompra\":10.0, \"cantidadDeseada\":50, \"cantidadAviso\":5}"))
            .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteProductoInventario_Success() throws Exception {
        when(productoInventarioService.getById("1")).thenReturn(sampleProduct);
        doNothing().when(productoInventarioService).delete("1");

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/productosInventario/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteProductoInventario_NotFound() throws Exception {
        when(productoInventarioService.getById("99")).thenReturn(null);

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
        when(productoInventarioService.getById("1")).thenThrow(new RuntimeException("Error interno"));

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
            .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteProductoInventario_InvalidId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/productosInventario/invalidId"))
            .andExpect(status().isNotFound());
    }
}
