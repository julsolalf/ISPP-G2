package ispp_g2.gastrostock.testProductoVenta;

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
import ispp_g2.gastrostock.productoVenta.ProductoVenta;
import ispp_g2.gastrostock.productoVenta.ProductoVentaController;
import ispp_g2.gastrostock.productoVenta.ProductoVentaService;

import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@WebMvcTest(ProductoVentaController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class ProductoVentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoVentaService productoVentaService;

    @InjectMocks
    private ProductoVentaController productoVentaController;

    private ProductoVenta sampleProductoVenta;

    @BeforeEach
    void setUp() {
        Categoria bebidas = new Categoria();
        bebidas.setName("Bebidas");
        sampleProductoVenta = new ProductoVenta();
        sampleProductoVenta.setId(1);
        sampleProductoVenta.setName("Cerveza");
        sampleProductoVenta.setCategoria(bebidas);
        sampleProductoVenta.setPrecioVenta(10.5);
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
        when(productoVentaService.save(any())).thenReturn(sampleProductoVenta);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/productosVenta")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"id\":\"1\", \"name\":\"Cerveza\", \"categoriaVenta\": \"Bebidas\", \"precioVenta\":10.5}"))
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
        when(productoVentaService.getById(1)).thenReturn(sampleProductoVenta);
        when(productoVentaService.save(any())).thenReturn(sampleProductoVenta);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/productosVenta/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"id\":\"1\", \"name\":\"Cerveza Modificada\", \"categoriaVenta\": \"Bebidas\", \"precioVenta\":12.0}"))
            .andExpect(status().isOk());
    }

    @Test
    void testUpdateProductoVenta_NotFound() throws Exception {
        when(productoVentaService.getById(99)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/productosVenta/99")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"id\":\"99\", \"name\":\"No Existente\", \"categoriaVenta\": \"Bebidas\", \"precioVenta\":10.0}"))
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
