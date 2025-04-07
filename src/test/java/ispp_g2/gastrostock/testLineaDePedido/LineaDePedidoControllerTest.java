package ispp_g2.gastrostock.testLineaDePedido;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import ispp_g2.gastrostock.empleado.Empleado;
import ispp_g2.gastrostock.lineaDePedido.LineaDePedido;
import ispp_g2.gastrostock.lineaDePedido.LineaDePedidoController;
import ispp_g2.gastrostock.lineaDePedido.LineaDePedidoService;
import ispp_g2.gastrostock.mesa.Mesa;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.pedido.Pedido;
import ispp_g2.gastrostock.productoVenta.ProductoVenta;
import ispp_g2.gastrostock.ventas.Venta;
import ispp_g2.gastrostock.categorias.Categoria;

import org.junit.jupiter.api.Assertions;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class LineaDePedidoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LineaDePedidoService lineaDePedidoService;

    @InjectMocks
    private LineaDePedidoController lineaDePedidoController;

    private ObjectMapper objectMapper;
    
    private LineaDePedido lineaNormal, linea;
    private LineaDePedido lineaCantidadGrande;
    private LineaDePedido lineaPrecioAlto;
    private LineaDePedido lineaInvalida;
    private List<LineaDePedido> lineasDePedido;
    private Pedido pedido1;
    private Pedido pedido2;
    private ProductoVenta producto1;
    private ProductoVenta producto2;
    private Mesa mesa;
    private Empleado empleado;
    private Negocio negocio;
    private Categoria categoria;
    private Venta venta;

    @BeforeEach
    void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(lineaDePedidoController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
    
    // Configurar ObjectMapper con soporte para Java 8 date/time
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    
        
        // Crear objetos necesarios para las pruebas
        
        // Crear negocio
        negocio = new Negocio();
        negocio.setName("Restaurante Test");
        negocio.setDireccion("Calle Test 123");
        negocio.setCiudad("Sevilla");
        negocio.setPais("Espana");
        negocio.setCodigoPostal("41001");
        negocio.setTokenNegocio(12345);
        
        // Crear mesa
        mesa = new Mesa();
        mesa.setName("Mesa 1");
        mesa.setNumeroAsientos(4);
        mesa.setNegocio(negocio);
        
        // Crear empleado
        empleado = new Empleado();
        empleado.setFirstName("Antonio");
        empleado.setLastName("García");
        empleado.setEmail("antonio@test.com");
        empleado.setNumTelefono("666111222");
        empleado.setTokenEmpleado("TOKEN123");
        empleado.setNegocio(negocio);
        
        // Crear categoría
        categoria = new Categoria();
        categoria.setName("Bebidas");
        categoria.setNegocio(negocio);
        
        // Crear productos
        producto1 = new ProductoVenta();
        producto1.setName("Cerveza");
        producto1.setPrecioVenta(3.0);
        producto1.setCategoria(categoria);
        
        producto2 = new ProductoVenta();
        producto2.setId(2);
        producto2.setName("Vino");
        producto2.setPrecioVenta(5.0);
        producto2.setCategoria(categoria);

        //Crear ventas
        venta = new Venta();
        venta.setId(1);
        venta.setNegocio(negocio);
        
        // Crear pedidos
        pedido1 = new Pedido();
        pedido1.setFecha(LocalDateTime.now().minusHours(1));
        pedido1.setPrecioTotal(50.75);
        pedido1.setMesa(mesa);
        pedido1.setEmpleado(empleado);
        pedido1.setVenta(venta);
        
        pedido2 = new Pedido();
        pedido2.setId(2);
        pedido2.setFecha(LocalDateTime.now());
        pedido2.setPrecioTotal(75.50);
        pedido2.setMesa(mesa);
        pedido2.setEmpleado(empleado);
        pedido2.setVenta(venta);
        
        // Crear líneas de pedido
        lineaNormal = new LineaDePedido();
        lineaNormal.setCantidad(5);
        lineaNormal.setPrecioUnitario(3.0);
        lineaNormal.setProducto(producto1);
        lineaNormal.setPedido(pedido1);

        linea = new LineaDePedido();
        linea.setId(1);
        linea.setCantidad(3);
        linea.setPrecioUnitario(3.0);
        linea.setProducto(producto1);
        linea.setPedido(pedido1);
        
        lineaCantidadGrande = new LineaDePedido();
        lineaCantidadGrande.setId(2);
        lineaCantidadGrande.setCantidad(10);
        lineaCantidadGrande.setPrecioUnitario(3.0);
        lineaCantidadGrande.setProducto(producto1);
        lineaCantidadGrande.setPedido(pedido1);
        
        lineaPrecioAlto = new LineaDePedido();
        lineaPrecioAlto.setId(3);
        lineaPrecioAlto.setCantidad(4);
        lineaPrecioAlto.setPrecioUnitario(4.0);
        lineaPrecioAlto.setProducto(producto2);
        lineaPrecioAlto.setPedido(pedido2);
        
        lineaInvalida = new LineaDePedido();
        lineaInvalida.setCantidad(1);
        // Sin establecer más datos, será inválida
        
        lineasDePedido = Arrays.asList(lineaNormal, lineaCantidadGrande, lineaPrecioAlto);
    }
    
    // Global exception handler para manejar excepciones en los tests
    public class GlobalExceptionHandler {
        // Puedes agregar métodos para manejar excepciones específicas si es necesario
    }
    
    // TESTS PARA findAll()
    
    @Test
    void testFindAll_Success() throws Exception {
        // Given
        when(lineaDePedidoService.getLineasDePedido()).thenReturn(lineasDePedido);
        
        // When & Then
        mockMvc.perform(get("/api/lineasDePedido"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[2].id", is(3)));
        
        verify(lineaDePedidoService, atLeastOnce()).getLineasDePedido();
    }
    
    @Test
    void testFindAll_EmptyList() throws Exception {
        // Given
        when(lineaDePedidoService.getLineasDePedido()).thenReturn(Collections.emptyList());
        
        // When & Then
        mockMvc.perform(get("/api/lineasDePedido"))
                .andExpect(status().isNoContent());
        
        verify(lineaDePedidoService).getLineasDePedido();
    }
    
    // TESTS PARA findById()
    
    @Test
    void testFindById_Success() throws Exception {
        // Given
        when(lineaDePedidoService.getById(1)).thenReturn(linea);
        
        // When & Then
        mockMvc.perform(get("/api/lineasDePedido/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.cantidad", is(3)))
                .andExpect(jsonPath("$.precioLinea", is(9.0)));
        
        verify(lineaDePedidoService).getById(1);
    }
    
    @Test
    void testFindById_NotFound() throws Exception {
        // Given
        when(lineaDePedidoService.getById(999)).thenReturn(null);
        
        // When & Then
        mockMvc.perform(get("/api/lineasDePedido/999"))
                .andExpect(status().isNotFound());
        
        verify(lineaDePedidoService).getById(999);
    }
    
    // TESTS PARA findByCantidad()
    
    @Test
    void testFindByCantidad_Success() throws Exception {
        // Given
        List<LineaDePedido> lineasCantidad3 = Collections.singletonList(linea);
        when(lineaDePedidoService.getLineasDePedidoByCantidad(3)).thenReturn(lineasCantidad3);
        
        // When & Then
        mockMvc.perform(get("/api/lineasDePedido/cantidad/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].cantidad", is(3)));
        
        verify(lineaDePedidoService).getLineasDePedidoByCantidad(3);
    }
    
    @Test
    void testFindByCantidad_NotFound() throws Exception {
        // Given
        when(lineaDePedidoService.getLineasDePedidoByCantidad(999)).thenReturn(null);
        
        // When & Then
        mockMvc.perform(get("/api/lineasDePedido/cantidad/999"))
                .andExpect(status().isNotFound());
        
        verify(lineaDePedidoService).getLineasDePedidoByCantidad(999);
    }
    
    @Test
    void testFindByCantidad_EmptyList() throws Exception {
        // Given
        when(lineaDePedidoService.getLineasDePedidoByCantidad(999)).thenReturn(Collections.emptyList());
        
        // When & Then
        mockMvc.perform(get("/api/lineasDePedido/cantidad/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
        
        verify(lineaDePedidoService).getLineasDePedidoByCantidad(999);
    }
    
    // TESTS PARA findByPrecioLinea()
    
    @Test
    void testFindByPrecioLinea_Success() throws Exception {
        // Given
        List<LineaDePedido> lineasPrecio9 = Collections.singletonList(linea);
        when(lineaDePedidoService.getLineasDePedidoByPrecioLinea(9.0)).thenReturn(lineasPrecio9);
        
        // When & Then
        mockMvc.perform(get("/api/lineasDePedido/precioLinea/9.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].precioLinea", is(9.0)));
        
        verify(lineaDePedidoService).getLineasDePedidoByPrecioLinea(9.0);
    }
    
    @Test
    void testFindByPrecioLinea_NotFound() throws Exception {
        // Given
        when(lineaDePedidoService.getLineasDePedidoByPrecioLinea(999.0)).thenReturn(null);
        
        // When & Then
        mockMvc.perform(get("/api/lineasDePedido/precioLinea/999.0"))
                .andExpect(status().isNotFound());
        
        verify(lineaDePedidoService).getLineasDePedidoByPrecioLinea(999.0);
    }
    
    @Test
    void testFindByPrecioLinea_EmptyList() throws Exception {
        // Given
        when(lineaDePedidoService.getLineasDePedidoByPrecioLinea(999.0)).thenReturn(Collections.emptyList());
        
        // When & Then
        mockMvc.perform(get("/api/lineasDePedido/precioLinea/999.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
        
        verify(lineaDePedidoService).getLineasDePedidoByPrecioLinea(999.0);
    }
    
    // TESTS PARA findByPedidoId()
    
    @Test
    void testFindByPedidoId_Success() throws Exception {
        // Given
        List<LineaDePedido> lineasPedido1 = Arrays.asList(linea, lineaCantidadGrande);
        when(lineaDePedidoService.getLineasDePedidoByPedidoId(1)).thenReturn(lineasPedido1);
        
        // When & Then
        mockMvc.perform(get("/api/lineasDePedido/pedido/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));
        
        verify(lineaDePedidoService).getLineasDePedidoByPedidoId(1);
    }
    
    @Test
    void testFindByPedidoId_NotFound() throws Exception {
        // Given
        when(lineaDePedidoService.getLineasDePedidoByPedidoId(999)).thenReturn(null);
        
        // When & Then
        mockMvc.perform(get("/api/lineasDePedido/pedido/999"))
                .andExpect(status().isNotFound());
        
        verify(lineaDePedidoService).getLineasDePedidoByPedidoId(999);
    }
    
    @Test
    void testFindByPedidoId_EmptyList() throws Exception {
        // Given
        when(lineaDePedidoService.getLineasDePedidoByPedidoId(999)).thenReturn(Collections.emptyList());
        
        // When & Then
        mockMvc.perform(get("/api/lineasDePedido/pedido/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
        
        verify(lineaDePedidoService).getLineasDePedidoByPedidoId(999);
    }
    
    // TESTS PARA findByProductoId()
    
    @Test
    void testFindByProductoId_Success() throws Exception {
        // Given
        List<LineaDePedido> lineasProducto1 = Arrays.asList(linea, lineaCantidadGrande);
        when(lineaDePedidoService.getLineasDePedidoByProductoId(1)).thenReturn(lineasProducto1);
        
        // When & Then
        mockMvc.perform(get("/api/lineasDePedido/producto/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));
        
        verify(lineaDePedidoService).getLineasDePedidoByProductoId(1);
    }
    
    @Test
    void testFindByProductoId_NotFound() throws Exception {
        // Given
        when(lineaDePedidoService.getLineasDePedidoByProductoId(999)).thenReturn(null);
        
        // When & Then
        mockMvc.perform(get("/api/lineasDePedido/producto/999"))
                .andExpect(status().isNotFound());
        
        verify(lineaDePedidoService).getLineasDePedidoByProductoId(999);
    }
    
    @Test
    void testFindByProductoId_EmptyList() throws Exception {
        // Given
        when(lineaDePedidoService.getLineasDePedidoByProductoId(999)).thenReturn(Collections.emptyList());
        
        // When & Then
        mockMvc.perform(get("/api/lineasDePedido/producto/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
        
        verify(lineaDePedidoService).getLineasDePedidoByProductoId(999);
    }
    
    // TESTS PARA findByProductoIdAndCantidad()
    
    @Test
    void testFindByProductoIdAndCantidad_Success() throws Exception {
        // Given
        List<LineaDePedido> lineasProducto1Cantidad3 = Collections.singletonList(linea);
        when(lineaDePedidoService.getLineasDePedidoByProductoIdAndCantidad(1, 3)).thenReturn(lineasProducto1Cantidad3);
        
        // When & Then
        mockMvc.perform(get("/api/lineasDePedido/producto/1/cantidad/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].cantidad", is(3)));
        
        verify(lineaDePedidoService).getLineasDePedidoByProductoIdAndCantidad(1, 3);
    }
    
    @Test
    void testFindByProductoIdAndCantidad_NotFound() throws Exception {
        // Given
        when(lineaDePedidoService.getLineasDePedidoByProductoIdAndCantidad(1, 999)).thenReturn(null);
        
        // When & Then
        mockMvc.perform(get("/api/lineasDePedido/producto/1/cantidad/999"))
                .andExpect(status().isNotFound());
        
        verify(lineaDePedidoService).getLineasDePedidoByProductoIdAndCantidad(1, 999);
    }
    
    @Test
    void testFindByProductoIdAndCantidad_EmptyList() throws Exception {
        // Given
        when(lineaDePedidoService.getLineasDePedidoByProductoIdAndCantidad(1, 999)).thenReturn(Collections.emptyList());
        
        // When & Then
        mockMvc.perform(get("/api/lineasDePedido/producto/1/cantidad/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
        
        verify(lineaDePedidoService).getLineasDePedidoByProductoIdAndCantidad(1, 999);
    }
    
    // TESTS PARA findByProductoIdAndPrecioLinea()
    
    @Test
    void testFindByProductoIdAndPrecioLinea_Success() throws Exception {
        // Given
        List<LineaDePedido> lineasProducto1Precio9 = Collections.singletonList(linea);
        when(lineaDePedidoService.getLineasDePedidoByProductoIdAndPrecioLinea(1, 9.0)).thenReturn(lineasProducto1Precio9);
        
        // When & Then
        mockMvc.perform(get("/api/lineasDePedido/producto/1/precioLinea/9.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].precioLinea", is(9.0)));
        
        verify(lineaDePedidoService).getLineasDePedidoByProductoIdAndPrecioLinea(1, 9.0);
    }
    
    @Test
    void testFindByProductoIdAndPrecioLinea_NotFound() throws Exception {
        // Given
        when(lineaDePedidoService.getLineasDePedidoByProductoIdAndPrecioLinea(1, 999.0)).thenReturn(null);
        
        // When & Then
        mockMvc.perform(get("/api/lineasDePedido/producto/1/precioLinea/999.0"))
                .andExpect(status().isNotFound());
        
        verify(lineaDePedidoService).getLineasDePedidoByProductoIdAndPrecioLinea(1, 999.0);
    }
    
    @Test
    void testFindByProductoIdAndPrecioLinea_EmptyList() throws Exception {
        // Given
        when(lineaDePedidoService.getLineasDePedidoByProductoIdAndPrecioLinea(1, 999.0)).thenReturn(Collections.emptyList());
        
        // When & Then
        mockMvc.perform(get("/api/lineasDePedido/producto/1/precioLinea/999.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
        
        verify(lineaDePedidoService).getLineasDePedidoByProductoIdAndPrecioLinea(1, 999.0);
    }
    
    // TESTS PARA save()
    
    @Test
    void testSave_Success() throws Exception {
        // Given
        when(lineaDePedidoService.save(any(LineaDePedido.class))).thenReturn(linea);
        
        // When & Then
        mockMvc.perform(post("/api/lineasDePedido")
                .with(csrf())   
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(linea)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.cantidad", is(3)))
                .andExpect(jsonPath("$.precioLinea", is(9.0)));
        
        verify(lineaDePedidoService).save(any(LineaDePedido.class));
    }

    
    @Test
    void testSave_InvalidBody() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/lineasDePedido")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }
    
    // TESTS PARA update()
    
    @Test
    void testUpdate_Success() throws Exception {
        // Given
        LineaDePedido updatedLinea = new LineaDePedido();
        updatedLinea.setCantidad(5); // Actualizamos cantidad
        updatedLinea.setPrecioUnitario(3.0); // Actualizamos precio
        updatedLinea.setProducto(producto1);
        updatedLinea.setPedido(pedido1);
        
        when(lineaDePedidoService.getById(1)).thenReturn(linea);
        when(lineaDePedidoService.save(any(LineaDePedido.class))).thenReturn(updatedLinea);
        
        // When & Then
        mockMvc.perform(put("/api/lineasDePedido/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedLinea)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantidad", is(5)))
                .andExpect(jsonPath("$.precioLinea", is(15.0)));
        
        verify(lineaDePedidoService).getById(1);
        verify(lineaDePedidoService).save(any(LineaDePedido.class));
    }
    
    @Test
    void testUpdate_NotFound() throws Exception {
        // Given
        when(lineaDePedidoService.getById(999)).thenReturn(null);
        
        // When & Then
        mockMvc.perform(put("/api/lineasDePedido/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(linea)))
                .andExpect(status().isNotFound());
        
        verify(lineaDePedidoService).getById(999);
        verify(lineaDePedidoService, never()).save(any(LineaDePedido.class));
    }
    
    @Test
    void testUpdate_NullBody() throws Exception {
        // When & Then
        mockMvc.perform(put("/api/lineasDePedido/1")
                .with(csrf())  
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest()); 
    }
    @Test
    void testUpdate_InvalidBody() throws Exception {
        // When & Then
        mockMvc.perform(put("/api/lineasDePedido/1")
                .with(csrf())  // Anade el token CSRF
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lineaInvalida)))
                .andExpect(status().isBadRequest());
        
    }
    
    // TESTS PARA delete()
    
    @Test
    void testDelete_Success() throws Exception {
        // Given
        when(lineaDePedidoService.getById(1)).thenReturn(lineaNormal);
        doNothing().when(lineaDePedidoService).delete(1);
        
        // When & Then
        mockMvc.perform(delete("/api/lineasDePedido/1"))
                .andExpect(status().isNoContent());
        
        verify(lineaDePedidoService).getById(1);
        verify(lineaDePedidoService).delete(1);
    }
    
    @Test
    void testDelete_NotFound() throws Exception {
        // Given
        when(lineaDePedidoService.getById(999)).thenReturn(null);
        
        // When & Then
        mockMvc.perform(delete("/api/lineasDePedido/999"))
                .andExpect(status().isNotFound());
        
        verify(lineaDePedidoService).getById(999);
        verify(lineaDePedidoService, never()).delete(anyInt());
    }
}