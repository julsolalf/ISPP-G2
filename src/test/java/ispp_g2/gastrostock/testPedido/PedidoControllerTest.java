package ispp_g2.gastrostock.testPedido;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import ispp_g2.gastrostock.empleado.Empleado;
import ispp_g2.gastrostock.mesa.Mesa;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.pedido.Pedido;
import ispp_g2.gastrostock.pedido.PedidoController;
import ispp_g2.gastrostock.pedido.PedidoService;

@ExtendWith(MockitoExtension.class)
class PedidoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PedidoService pedidoService;

    @InjectMocks
    private PedidoController pedidoController;

    private ObjectMapper objectMapper;
    private Pedido pedido;
    private LocalDateTime fecha;
    private Mesa mesa;
    private Empleado empleado;
    private Negocio negocio;

    @BeforeEach
    void setUp() {
        // Configurar MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(pedidoController)
                .build();
        
        // Configurar ObjectMapper para manejar LocalDateTime
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        
        // Crear fecha de prueba
        fecha = LocalDateTime.now();
        
        // Configurar Mesa
        mesa = new Mesa();
        mesa.setId(1);
        mesa.setName("Mesa 1");
        mesa.setNumeroAsientos(4);
        
        // Configurar Empleado
        empleado = new Empleado();
        empleado.setId(1);
        empleado.setName("Empleado Test");
        empleado.setTokenEmpleado("EMP123");
        
        // Configurar Negocio
        negocio = new Negocio();
        negocio.setId(1);
        negocio.setName("Restaurante Test");
        
        // Configurar Pedido
        pedido = new Pedido();
        pedido.setId(1);
        pedido.setFecha(fecha);
        pedido.setPrecioTotal(45.50);
        pedido.setMesa(mesa);
        pedido.setEmpleado(empleado);
        pedido.setNegocio(negocio);
    }

    // Test para findAll() - Caso éxito
    @Test
    void testFindAll_Success() throws Exception {
        // Crear lista de pedidos
        List<Pedido> pedidos = new ArrayList<>();
        pedidos.add(pedido);
        
        Pedido pedido2 = new Pedido();
        pedido2.setId(2);
        pedido2.setFecha(fecha.plusHours(1));
        pedido2.setPrecioTotal(55.75);
        pedido2.setMesa(mesa);
        pedido2.setEmpleado(empleado);
        pedido2.setNegocio(negocio);
        pedidos.add(pedido2);
        
        when(pedidoService.getAll()).thenReturn(pedidos);
        
        mockMvc.perform(get("/api/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
        
        verify(pedidoService, atLeastOnce()).getAll();
    }
    
    // Test para findAll() - Lista vacía
    @Test
    void testFindAll_EmptyList() throws Exception {
        when(pedidoService.getAll()).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/api/pedidos"))
                .andExpect(status().isNoContent());
        
        verify(pedidoService).getAll();
    }
    
    // Test para findById() - Caso éxito
    @Test
    void testFindById_Success() throws Exception {
        when(pedidoService.getById("1")).thenReturn(pedido);
        
        mockMvc.perform(get("/api/pedidos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.precioTotal").value(45.50));
        
        verify(pedidoService).getById("1");
    }
    
    // Test para findById() - Pedido no encontrado
    @Test
    void testFindById_NotFound() throws Exception {
        when(pedidoService.getById("999")).thenReturn(null);
        
        mockMvc.perform(get("/api/pedidos/999"))
                .andExpect(status().isNotFound());
        
        verify(pedidoService).getById("999");
    }
    
    // Test para findByFecha() - Caso éxito
    @Test
    void testFindByFecha_Success() throws Exception {
        List<Pedido> pedidos = Collections.singletonList(pedido);
        // Formato ISO para LocalDateTime en URL
        String fechaStr = fecha.format(DateTimeFormatter.ISO_DATE_TIME);
        
        when(pedidoService.getPedidoByFecha(any(LocalDateTime.class))).thenReturn(pedidos);
        
        mockMvc.perform(get("/api/pedidos/fecha/{fecha}", fechaStr))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1));
    }
    
    // Test para findByFecha() - No resultados
    @Test
    void testFindByFecha_NotFound() throws Exception {
        String fechaStr = fecha.format(DateTimeFormatter.ISO_DATE_TIME);
        when(pedidoService.getPedidoByFecha(any(LocalDateTime.class))).thenReturn(null);
        
        mockMvc.perform(get("/api/pedidos/fecha/{fecha}", fechaStr))
                .andExpect(status().isNotFound());
    }
    
    // Test para findByPrecioTotal() - Caso éxito
    @Test
    void testFindByPrecioTotal_Success() throws Exception {
        List<Pedido> pedidos = Collections.singletonList(pedido);
        when(pedidoService.getPedidoByPrecioTotal(45.50)).thenReturn(pedidos);
        
        mockMvc.perform(get("/api/pedidos/precioTotal/45.50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].precioTotal").value(45.50));
        
        verify(pedidoService).getPedidoByPrecioTotal(45.50);
    }
    
    // Test para findByPrecioTotal() - No resultados
    @Test
    void testFindByPrecioTotal_NotFound() throws Exception {
        when(pedidoService.getPedidoByPrecioTotal(99.99)).thenReturn(null);
        
        mockMvc.perform(get("/api/pedidos/precioTotal/99.99"))
                .andExpect(status().isNotFound());
        
        verify(pedidoService).getPedidoByPrecioTotal(99.99);
    }
    
    // Test para findByPrecioTotal() - Precio negativo (caso límite)
    @Test
    void testFindByPrecioTotal_NegativePrice() throws Exception {
        List<Pedido> pedidos = Collections.emptyList();
        when(pedidoService.getPedidoByPrecioTotal(-10.0)).thenReturn(pedidos);
        
        mockMvc.perform(get("/api/pedidos/precioTotal/-10.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
        
        verify(pedidoService).getPedidoByPrecioTotal(-10.0);
    }
    
    // Test para findByMesaId() - Caso éxito
    @Test
    void testFindByMesaId_Success() throws Exception {
        List<Pedido> pedidos = Collections.singletonList(pedido);
        when(pedidoService.getPedidoByMesaId(1)).thenReturn(pedidos);
        
        mockMvc.perform(get("/api/pedidos/mesa/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].mesa.id").value(1));
        
        verify(pedidoService).getPedidoByMesaId(1);
    }
    
    // Test para findByMesaId() - No resultados
    @Test
    void testFindByMesaId_NotFound() throws Exception {
        when(pedidoService.getPedidoByMesaId(999)).thenReturn(null);
        
        mockMvc.perform(get("/api/pedidos/mesa/999"))
                .andExpect(status().isNotFound());
        
        verify(pedidoService).getPedidoByMesaId(999);
    }
    
    // Test para findByEmpleadoId() - Caso éxito
    @Test
    void testFindByEmpleadoId_Success() throws Exception {
        List<Pedido> pedidos = Collections.singletonList(pedido);
        when(pedidoService.getPedidoByEmpleadoId(1)).thenReturn(pedidos);
        
        mockMvc.perform(get("/api/pedidos/empleado/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].empleado.id").value(1));
        
        verify(pedidoService).getPedidoByEmpleadoId(1);
    }
    
    // Test para findByEmpleadoId() - No resultados
    @Test
    void testFindByEmpleadoId_NotFound() throws Exception {
        when(pedidoService.getPedidoByEmpleadoId(999)).thenReturn(null);
        
        mockMvc.perform(get("/api/pedidos/empleado/999"))
                .andExpect(status().isNotFound());
        
        verify(pedidoService).getPedidoByEmpleadoId(999);
    }
    
    // Test para findByNegocioId() - Caso éxito
    @Test
    void testFindByNegocioId_Success() throws Exception {
        List<Pedido> pedidos = Collections.singletonList(pedido);
        when(pedidoService.getPedidoByNegocioId(1)).thenReturn(pedidos);
        
        mockMvc.perform(get("/api/pedidos/negocio/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].negocio.id").value(1));
        
        verify(pedidoService).getPedidoByNegocioId(1);
    }
    
    // Test para findByNegocioId() - No resultados
    @Test
    void testFindByNegocioId_NotFound() throws Exception {
        when(pedidoService.getPedidoByNegocioId(999)).thenReturn(null);
        
        mockMvc.perform(get("/api/pedidos/negocio/999"))
                .andExpect(status().isNotFound());
        
        verify(pedidoService).getPedidoByNegocioId(999);
    }
    
    // Test para save() - Caso éxito
    @Test
    void testSave_Success() throws Exception {
        when(pedidoService.save(any(Pedido.class))).thenReturn(pedido);
        
        mockMvc.perform(post("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pedido)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.precioTotal").value(45.50));
        
        verify(pedidoService).save(any(Pedido.class));
    }
    
    // Test para save() - Entrada nula
    @Test
    void testSave_NullInput() throws Exception {
        mockMvc.perform(post("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(status().isBadRequest());
    }
    
    // Test para update() - Caso éxito
    @Test
    void testUpdate_Success() throws Exception {
        Pedido updatedPedido = new Pedido();
        updatedPedido.setId(1);
        updatedPedido.setFecha(fecha);
        updatedPedido.setPrecioTotal(60.75); // Precio actualizado
        updatedPedido.setMesa(mesa);
        updatedPedido.setEmpleado(empleado);
        updatedPedido.setNegocio(negocio);
        
        when(pedidoService.save(any(Pedido.class))).thenReturn(updatedPedido);
        
        mockMvc.perform(put("/api/pedidos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedPedido)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.precioTotal").value(60.75));
        
        verify(pedidoService).save(any(Pedido.class));
    }
    
    // Test para update() - ID no coincide
    @Test
    void testUpdate_IdNotMatch() throws Exception {
        Pedido invalidPedido = new Pedido();
        // Sin establecer ID, debería causar un error
        invalidPedido.setPrecioTotal(60.75);
        invalidPedido.setMesa(mesa);
        invalidPedido.setEmpleado(empleado);
        invalidPedido.setNegocio(negocio);
        
        mockMvc.perform(put("/api/pedidos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidPedido)))
                .andExpect(status().isBadRequest());
    }
    
    // Test para update() - Entrada nula
    @Test
    void testUpdate_NullInput() throws Exception {
        mockMvc.perform(put("/api/pedidos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(status().isBadRequest());
    }
    
    // Test para delete() - Caso éxito
    @Test
    void testDelete_Success() throws Exception {
        doNothing().when(pedidoService).delete("1");
        
        mockMvc.perform(delete("/api/pedidos/1"))
                .andExpect(status().isNoContent());
        
        verify(pedidoService).delete("1");
    }
    
    // Test para delete() - ID inválido (caso límite con ID no numérico)
/* 
    @Test
    void testDelete_InvalidId() throws Exception {
        doThrow(new NumberFormatException()).when(pedidoService).delete("999");
        
        mockMvc.perform(delete("/api/pedidos/999"))
                .andExpect(status().isBadRequest());
    }
*/
}