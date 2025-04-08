package ispp_g2.gastrostock.testCarrito;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ispp_g2.gastrostock.carrito.Carrito;
import ispp_g2.gastrostock.carrito.CarritoController;
import ispp_g2.gastrostock.carrito.CarritoService;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class CarritoControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    @Mock
    private CarritoService carritoService;
    @InjectMocks
    private CarritoController carritoController;
    private Carrito carrito1, carrito2, carritoNuevo, carritoInvalido;

    @BeforeEach
    void  setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(carritoController).build();
    
        // Configurar ObjectMapper para manejar LocalDate
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        carrito1 = new Carrito();
        carrito1.setId(1);
        carrito1.setPrecioTotal(100.0);
        carrito1.setDiaEntrega(LocalDate.now().plusDays(1));
        carrito2 = new Carrito();
        carrito2.setId(2);
        carrito2.setPrecioTotal(200.0);
        carrito2.setDiaEntrega(LocalDate.now().plusDays(2));
        carritoNuevo = new Carrito();
        carritoNuevo.setPrecioTotal(150.0);
        carritoNuevo.setDiaEntrega(LocalDate.now().plusDays(3));
        carritoInvalido = new Carrito();
    }

    @Test
    void testFindAll_Success() throws Exception {
        List<Carrito> lista = Arrays.asList(carrito1, carrito2);
        when(carritoService.findAll()).thenReturn(lista);
        mockMvc.perform(get("/api/carritos").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].precioTotal", is(100.0)))
                .andExpect(jsonPath("$[1].precioTotal", is(200.0)));
        verify(carritoService, times(2)).findAll();
    }

    @Test
    void testFindAll_NoContent() throws Exception {
        when(carritoService.findAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/carritos").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(carritoService).findAll();
    }

    @Test
    void testFindById_Success() throws Exception {
        when(carritoService.findById(1)).thenReturn(carrito1);
        mockMvc.perform(get("/api/carritos/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.precioTotal", is(100.0)));
        verify(carritoService).findById(1);
    }

    @Test
    void testFindById_NotFound() throws Exception {
        when(carritoService.findById(999)).thenReturn(null);
        mockMvc.perform(get("/api/carritos/999").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(carritoService).findById(999);
    }

    @Test
    void testFindByProveedor_Success() throws Exception {
        List<Carrito> lista = Collections.singletonList(carrito1);
        when(carritoService.findByProveedorId(10)).thenReturn(lista);
        mockMvc.perform(get("/api/carritos/proveedor/10").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].precioTotal", is(100.0)));
        verify(carritoService).findByProveedorId(10);
    }

    @Test
    void testFindByProveedor_NotFound() throws Exception {
        when(carritoService.findByProveedorId(10)).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/carritos/proveedor/10").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(carritoService).findByProveedorId(10);
    }

    @Test
    void testFindByNegocio_Success() throws Exception {
        List<Carrito> lista = Collections.singletonList(carrito2);
        when(carritoService.findByNegocioId(20)).thenReturn(lista);
        mockMvc.perform(get("/api/carritos/negocio/20").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].precioTotal", is(200.0)));
        verify(carritoService).findByNegocioId(20);
    }

    @Test
    void testFindByNegocio_NotFound() throws Exception {
        when(carritoService.findByNegocioId(20)).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/carritos/negocio/20").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(carritoService).findByNegocioId(20);
    }

    @Test
    void testSave_Success() throws Exception {
        when(carritoService.save(any(Carrito.class))).thenReturn(carrito1);
        mockMvc.perform(post("/api/carritos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(carritoNuevo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.precioTotal", is(100.0)));
        verify(carritoService).save(any(Carrito.class));
    }

    @Test
    void testSave_NullCarrito() throws Exception {
        mockMvc.perform(post("/api/carritos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest());
        verify(carritoService, never()).save(any(Carrito.class));
    }

    @Test
    void testUpdate_Success() throws Exception {
        when(carritoService.findById(1)).thenReturn(carrito1);
        carritoNuevo.setPrecioTotal(175.0);
        carritoNuevo.setDiaEntrega(LocalDate.now().plusDays(4));
        when(carritoService.save(any(Carrito.class))).thenReturn(carritoNuevo);
        mockMvc.perform(put("/api/carritos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(carritoNuevo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.precioTotal", is(175.0)));
        verify(carritoService).findById(1);
        verify(carritoService).save(any(Carrito.class));
    }

    @Test
    void testUpdate_NotFound() throws Exception {
        when(carritoService.findById(999)).thenReturn(null);
        mockMvc.perform(put("/api/carritos/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(carritoNuevo)))
                .andExpect(status().isNotFound());
        verify(carritoService).findById(999);
        verify(carritoService, never()).save(any(Carrito.class));
    }

    @Test
    void testUpdate_NullCarrito() throws Exception {
        mockMvc.perform(put("/api/carritos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest());
        verify(carritoService, never()).findById(anyInt());
        verify(carritoService, never()).save(any(Carrito.class));
    }

    @Test
    void testDelete_Success() throws Exception {
        when(carritoService.findById(1)).thenReturn(carrito1);
        doNothing().when(carritoService).deleteById(1);
        mockMvc.perform(delete("/api/carritos/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(carritoService).findById(1);
        verify(carritoService).deleteById(1);
    }

    @Test
    void testDelete_NotFound() throws Exception {
        when(carritoService.findById(999)).thenReturn(null);
        mockMvc.perform(delete("/api/carritos/999").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(carritoService).findById(999);
        verify(carritoService, never()).deleteById(anyInt());
    }
}
