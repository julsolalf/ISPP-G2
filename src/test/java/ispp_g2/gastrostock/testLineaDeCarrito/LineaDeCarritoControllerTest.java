package ispp_g2.gastrostock.testLineaDeCarrito;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import ispp_g2.gastrostock.carrito.Carrito;
import ispp_g2.gastrostock.exceptions.ExceptionHandlerController;
import ispp_g2.gastrostock.lineaDeCarrito.LineaDeCarrito;
import ispp_g2.gastrostock.lineaDeCarrito.LineaDeCarritoController;
import ispp_g2.gastrostock.lineaDeCarrito.LineaDeCarritoService;
import ispp_g2.gastrostock.productoInventario.ProductoInventario;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class LineaDeCarritoControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private LineaDeCarritoService lineaDeCarritoService;

    @InjectMocks
    private LineaDeCarritoController lineaDeCarritoController;

    private LineaDeCarrito lineaNormal;
    private LineaDeCarrito lineaCantidadGrande;
    private LineaDeCarrito lineaPrecioAlto;
    private LineaDeCarrito nuevaLinea;
    private List<LineaDeCarrito> lineasDeCarrito;
    private Carrito carrito1;
    private Carrito carrito2;
    private ProductoInventario producto1;
    private ProductoInventario producto2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(lineaDeCarritoController)
                .setControllerAdvice(new ExceptionHandlerController())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        carrito1 = new Carrito();
        carrito1.setId(1);
        carrito1.setPrecioTotal(100.0);

        carrito2 = new Carrito();
        carrito2.setId(2);
        carrito2.setPrecioTotal(150.0);

        producto1 = new ProductoInventario();
        producto1.setId(1);
        producto1.setName("Harina");
        producto1.setPrecioCompra(2.5);

        producto2 = new ProductoInventario();
        producto2.setId(2);
        producto2.setName("Azúcar");
        producto2.setPrecioCompra(1.8);

        lineaNormal = new LineaDeCarrito();
        lineaNormal.setId(1);
        lineaNormal.setCantidad(3);
        lineaNormal.setPrecioLinea(7.5);
        lineaNormal.setCarrito(carrito1);
        lineaNormal.setProducto(producto1);

        lineaCantidadGrande = new LineaDeCarrito();
        lineaCantidadGrande.setId(2);
        lineaCantidadGrande.setCantidad(10);
        lineaCantidadGrande.setPrecioLinea(25.0);
        lineaCantidadGrande.setCarrito(carrito1);
        lineaCantidadGrande.setProducto(producto1);

        lineaPrecioAlto = new LineaDeCarrito();
        lineaPrecioAlto.setId(3);
        lineaPrecioAlto.setCantidad(4);
        lineaPrecioAlto.setPrecioLinea(20.0);
        lineaPrecioAlto.setCarrito(carrito2);
        lineaPrecioAlto.setProducto(producto2);

        nuevaLinea = new LineaDeCarrito();
        nuevaLinea.setCantidad(5);
        nuevaLinea.setPrecioLinea(12.5);
        nuevaLinea.setCarrito(carrito2);
        nuevaLinea.setProducto(producto1);

        lineasDeCarrito = Arrays.asList(lineaNormal, lineaCantidadGrande, lineaPrecioAlto);
    }

    @Test
    void testFindAll_Success() throws Exception {
        when(lineaDeCarritoService.findAll()).thenReturn(lineasDeCarrito);

        mockMvc.perform(get("/api/lineasDeCarrito"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].cantidad", is(3)))
                .andExpect(jsonPath("$[0].precioLinea", is(7.5)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[2].id", is(3)));

        verify(lineaDeCarritoService,atLeastOnce()).findAll();
    }

    @Test
    void testFindAll_NoContent() throws Exception {
        when(lineaDeCarritoService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/lineasDeCarrito"))
                .andExpect(status().isNoContent());

        verify(lineaDeCarritoService).findAll();
    }

    @Test
    void testFindById_Success() throws Exception {
        when(lineaDeCarritoService.findLineaDeCarritoById(1)).thenReturn(lineaNormal);

        mockMvc.perform(get("/api/lineasDeCarrito/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.cantidad", is(3)))
                .andExpect(jsonPath("$.precioLinea", is(7.5)));

        verify(lineaDeCarritoService).findLineaDeCarritoById(1);
    }

    @Test
    void testFindById_NotFound() throws Exception {
        when(lineaDeCarritoService.findLineaDeCarritoById(999)).thenReturn(null);

        mockMvc.perform(get("/api/lineasDeCarrito/999"))
                .andExpect(status().isNotFound());

        verify(lineaDeCarritoService).findLineaDeCarritoById(999);
    }

    @Test
    void testFindByCarritoId_Success() throws Exception {
        when(lineaDeCarritoService.findLineaDeCarritoByCarritoId(1))
                .thenReturn(Arrays.asList(lineaNormal, lineaCantidadGrande));

        mockMvc.perform(get("/api/lineasDeCarrito/carrito/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));

        verify(lineaDeCarritoService).findLineaDeCarritoByCarritoId(1);
    }

    @Test
    void testFindByCarritoId_NotFound() throws Exception {
        when(lineaDeCarritoService.findLineaDeCarritoByCarritoId(999)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/lineasDeCarrito/carrito/999"))
                .andExpect(status().isNotFound());

        verify(lineaDeCarritoService).findLineaDeCarritoByCarritoId(999);
    }

    @Test
    void testFindByCarritoIdAndProductoId_Success() throws Exception {
        when(lineaDeCarritoService.findLineaDeCarritoByCarritoIdAndProductoId(1, 1))
                .thenReturn(Arrays.asList(lineaNormal, lineaCantidadGrande));

        mockMvc.perform(get("/api/lineasDeCarrito/carrito/1/producto/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));

        verify(lineaDeCarritoService).findLineaDeCarritoByCarritoIdAndProductoId(1, 1);
    }

    @Test
    void testFindByCarritoIdAndProductoId_NotFound() throws Exception {
        when(lineaDeCarritoService.findLineaDeCarritoByCarritoIdAndProductoId(1, 999))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/lineasDeCarrito/carrito/1/producto/999"))
                .andExpect(status().isNotFound());

        verify(lineaDeCarritoService).findLineaDeCarritoByCarritoIdAndProductoId(1, 999);
    }

    @Test
    void testSave_Success() throws Exception {
        when(lineaDeCarritoService.save(any(LineaDeCarrito.class))).thenReturn(nuevaLinea);

        mockMvc.perform(post("/api/lineasDeCarrito")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevaLinea)))
                .andExpect(status().isCreated());

        verify(lineaDeCarritoService).save(any(LineaDeCarrito.class));
    }
    
    @Test
    void testSave_BadRequest() throws Exception {
        LineaDeCarrito lineaInvalida = new LineaDeCarrito();
        // No establecer propiedades requeridas para que falle la validación
        
        mockMvc.perform(post("/api/lineasDeCarrito")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lineaInvalida)))
                .andExpect(status().isBadRequest());
        
        verify(lineaDeCarritoService, never()).save(any(LineaDeCarrito.class));
    }

    @Test
    void testSave_NullLineaDeCarrito() throws Exception {
        mockMvc.perform(post("/api/lineasDeCarrito")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest());

        verify(lineaDeCarritoService, never()).save(any(LineaDeCarrito.class));
    }

    @Test
    void testUpdate_Success() throws Exception {
        when(lineaDeCarritoService.findLineaDeCarritoById(1)).thenReturn(lineaNormal);
        when(lineaDeCarritoService.save(any(LineaDeCarrito.class))).thenReturn(lineaNormal);

        LineaDeCarrito lineaActualizada = new LineaDeCarrito();
        lineaActualizada.setId(1);
        lineaActualizada.setCantidad(5);
        lineaActualizada.setPrecioLinea(12.5);
        lineaActualizada.setCarrito(carrito1);
        lineaActualizada.setProducto(producto1);

        mockMvc.perform(put("/api/lineasDeCarrito/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lineaActualizada)))
                .andExpect(status().isOk());

        verify(lineaDeCarritoService).findLineaDeCarritoById(1);
        verify(lineaDeCarritoService).save(any(LineaDeCarrito.class));
    }

    @Test
    void testUpdate_NotFound() throws Exception {
        when(lineaDeCarritoService.findLineaDeCarritoById(999)).thenReturn(null);

        LineaDeCarrito lineaActualizada = new LineaDeCarrito();
        lineaActualizada.setId(999);
        lineaActualizada.setCantidad(5);
        lineaActualizada.setPrecioLinea(12.5);
        lineaActualizada.setCarrito(carrito1);
        lineaActualizada.setProducto(producto1);

        mockMvc.perform(put("/api/lineasDeCarrito/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lineaActualizada)))
                .andExpect(status().isNotFound());

        verify(lineaDeCarritoService).findLineaDeCarritoById(999);
        verify(lineaDeCarritoService, never()).save(any(LineaDeCarrito.class));
    }

    @Test
    void testUpdate_BadRequest() throws Exception {
        LineaDeCarrito lineaInvalida = new LineaDeCarrito();
        // No establecer propiedades requeridas para que falle la validación

        mockMvc.perform(put("/api/lineasDeCarrito/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lineaInvalida)))
                .andExpect(status().isBadRequest());

        verify(lineaDeCarritoService, never()).findLineaDeCarritoById(anyInt());
        verify(lineaDeCarritoService, never()).save(any(LineaDeCarrito.class));
    }

    @Test
    void testUpdate_NullLineaDeCarrito() throws Exception {
        mockMvc.perform(put("/api/lineasDeCarrito/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest());

        verify(lineaDeCarritoService, never()).findLineaDeCarritoById(anyInt());
        verify(lineaDeCarritoService, never()).save(any(LineaDeCarrito.class));
    }

    @Test
    void testDelete_Success() throws Exception {
        when(lineaDeCarritoService.findLineaDeCarritoById(1)).thenReturn(lineaNormal);
        doNothing().when(lineaDeCarritoService).delete(any(LineaDeCarrito.class));

        mockMvc.perform(delete("/api/lineasDeCarrito/1"))
                .andExpect(status().isNoContent());

        verify(lineaDeCarritoService).findLineaDeCarritoById(1);
        verify(lineaDeCarritoService).delete(any(LineaDeCarrito.class));
    }

    @Test
    void testDelete_NotFound() throws Exception {
        when(lineaDeCarritoService.findLineaDeCarritoById(999)).thenReturn(null);

        mockMvc.perform(delete("/api/lineasDeCarrito/999"))
                .andExpect(status().isNotFound());

        verify(lineaDeCarritoService).findLineaDeCarritoById(999);
        verify(lineaDeCarritoService, never()).delete(any(LineaDeCarrito.class));
    }
    
    @Test
    void testExceptionHandling_InternalServerError() throws Exception {
        when(lineaDeCarritoService.findAll()).thenThrow(new RuntimeException("Error interno del servidor"));

        mockMvc.perform(get("/api/lineasDeCarrito"))
                .andExpect(status().isInternalServerError());

        verify(lineaDeCarritoService).findAll();
    }
    
    @Test
    void testFindById_WithExceptionInService() throws Exception {
        when(lineaDeCarritoService.findLineaDeCarritoById(anyInt())).thenThrow(new RuntimeException("Error en el servicio"));

        mockMvc.perform(get("/api/lineasDeCarrito/1"))
                .andExpect(status().isInternalServerError());

        verify(lineaDeCarritoService).findLineaDeCarritoById(1);
    }
    
    @Test
    void testFindByCarritoId_WithExceptionInService() throws Exception {
        when(lineaDeCarritoService.findLineaDeCarritoByCarritoId(anyInt())).thenThrow(new RuntimeException("Error en el servicio"));

        mockMvc.perform(get("/api/lineasDeCarrito/carrito/1"))
                .andExpect(status().isInternalServerError());

        verify(lineaDeCarritoService).findLineaDeCarritoByCarritoId(1);
    }
    
    @Test
    void testFindByCarritoIdAndProductoId_WithExceptionInService() throws Exception {
        when(lineaDeCarritoService.findLineaDeCarritoByCarritoIdAndProductoId(anyInt(), anyInt()))
            .thenThrow(new RuntimeException("Error en el servicio"));

        mockMvc.perform(get("/api/lineasDeCarrito/carrito/1/producto/1"))
                .andExpect(status().isInternalServerError());

        verify(lineaDeCarritoService).findLineaDeCarritoByCarritoIdAndProductoId(1, 1);
    }
    
    @Test
    void testSave_WithExceptionInService() throws Exception {
        when(lineaDeCarritoService.save(any(LineaDeCarrito.class))).thenThrow(new RuntimeException("Error al guardar"));

        mockMvc.perform(post("/api/lineasDeCarrito")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevaLinea)))
                .andExpect(status().isInternalServerError());

        verify(lineaDeCarritoService).save(any(LineaDeCarrito.class));
    }
    
    @Test
    void testUpdate_WithExceptionInService() throws Exception {
        when(lineaDeCarritoService.findLineaDeCarritoById(1)).thenReturn(lineaNormal);
        when(lineaDeCarritoService.save(any(LineaDeCarrito.class))).thenThrow(new RuntimeException("Error al actualizar"));

        mockMvc.perform(put("/api/lineasDeCarrito/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lineaNormal)))
                .andExpect(status().isInternalServerError());

        verify(lineaDeCarritoService).findLineaDeCarritoById(1);
        verify(lineaDeCarritoService).save(any(LineaDeCarrito.class));
    }
    
    @Test
    void testDelete_WithExceptionInService() throws Exception {
        when(lineaDeCarritoService.findLineaDeCarritoById(1)).thenReturn(lineaNormal);
        doThrow(new RuntimeException("Error al eliminar")).when(lineaDeCarritoService).delete(any(LineaDeCarrito.class));

        mockMvc.perform(delete("/api/lineasDeCarrito/1"))
                .andExpect(status().isInternalServerError());

        verify(lineaDeCarritoService).findLineaDeCarritoById(1);
        verify(lineaDeCarritoService).delete(any(LineaDeCarrito.class));
    }
}