package ispp_g2.gastrostock.testLineaDePedido;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import ispp_g2.gastrostock.lineaDePedido.LineaDePedido;
import ispp_g2.gastrostock.lineaDePedido.LineaDePedidoController;
import ispp_g2.gastrostock.lineaDePedido.LineaDePedidoService;
import ispp_g2.gastrostock.pedido.Pedido;
import ispp_g2.gastrostock.productoVenta.ProductoVenta;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Collections;
import java.util.List;

class LineaDePedidoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LineaDePedidoService lineaDePedidoService;

    @InjectMocks
    private LineaDePedidoController lineaDePedidoController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(lineaDePedidoController).build();
    }

    @Test
    void findAll_ShouldReturnOk() throws Exception {
        when(lineaDePedidoService.getLineasDePedido()).thenReturn(List.of(new LineaDePedido()));
        mockMvc.perform(get("/lineasDePedido"))
                .andExpect(status().isOk());
    }

    @Test
    void findAll_ShouldReturnNoContent() throws Exception {
        when(lineaDePedidoService.getLineasDePedido()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/lineasDePedido"))
                .andExpect(status().isNoContent());
    }

    @Test
    void findById_ShouldReturnOk() throws Exception {
        when(lineaDePedidoService.getById("1")).thenReturn(new LineaDePedido());
        mockMvc.perform(get("/lineasDePedido/1"))
                .andExpect(status().isOk());
    }

    @Test
    void findById_ShouldReturnNotFound() throws Exception {
        when(lineaDePedidoService.getById("1")).thenReturn(null);
        mockMvc.perform(get("/lineasDePedido/1"))
                .andExpect(status().isNotFound());
    }

@Test
void save_ShouldReturnCreated() throws Exception {
    
    Pedido pedido = new Pedido();  
    pedido.setId(1); 
    
    ProductoVenta producto = new ProductoVenta();
    producto.setId(5); 
    
    
    LineaDePedido linea = new LineaDePedido();
    linea.setCantidad(2);
    linea.setPrecioLinea(100.0);
    linea.setPedido(pedido);
    linea.setProducto(producto);

   
    when(lineaDePedidoService.save(any(LineaDePedido.class))).thenReturn(linea);

    
    mockMvc.perform(post("/lineasDePedido")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(linea)))
            .andExpect(status().isCreated());
}

@Test
void update_ShouldReturnOk() throws Exception {
    Pedido pedido = new Pedido();  
    pedido.setId(1); 
    
    ProductoVenta producto = new ProductoVenta();
    producto.setId(5);  
    
    
    LineaDePedido linea = new LineaDePedido();
    linea.setCantidad(2);
    linea.setPrecioLinea(100.0);
    linea.setPedido(pedido);
    linea.setProducto(producto);

    
    when(lineaDePedidoService.getById("1")).thenReturn(linea);
    when(lineaDePedidoService.save(any(LineaDePedido.class))).thenReturn(linea);

    
    mockMvc.perform(put("/lineasDePedido/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(linea)))  
            .andExpect(status().isOk());
}


    @Test
    void delete_ShouldReturnNoContent() throws Exception {
        when(lineaDePedidoService.getById("1")).thenReturn(new LineaDePedido());
        doNothing().when(lineaDePedidoService).delete("1");
        mockMvc.perform(delete("/lineasDePedido/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_ShouldReturnNotFound() throws Exception {
        when(lineaDePedidoService.getById("1")).thenReturn(null);
        mockMvc.perform(delete("/lineasDePedido/1"))
                .andExpect(status().isNotFound());
    }
}