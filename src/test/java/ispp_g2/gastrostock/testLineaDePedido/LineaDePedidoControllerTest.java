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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import ispp_g2.gastrostock.empleado.Empleado;
import ispp_g2.gastrostock.empleado.EmpleadoService;
import ispp_g2.gastrostock.ingrediente.IngredienteService;
import ispp_g2.gastrostock.lineaDePedido.LineaDePedido;
import ispp_g2.gastrostock.lineaDePedido.LineaDePedidoController;
import ispp_g2.gastrostock.lineaDePedido.LineaDePedidoDTO;
import ispp_g2.gastrostock.lineaDePedido.LineaDePedidoService;
import ispp_g2.gastrostock.mesa.Mesa;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.pedido.Pedido;
import ispp_g2.gastrostock.pedido.PedidoService;
import ispp_g2.gastrostock.productoVenta.ProductoVenta;
import ispp_g2.gastrostock.user.Authorities;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserService;
import ispp_g2.gastrostock.categorias.Categoria;
import ispp_g2.gastrostock.config.jwt.JwtService;
import ispp_g2.gastrostock.dueno.Dueno;
import ispp_g2.gastrostock.dueno.DuenoService;

import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class LineaDePedidoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LineaDePedidoService lineaDePedidoService;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserService userService;
    @Mock
    private DuenoService duenoService;
    @Mock
    private EmpleadoService empleadoService;
    
    @Mock
    private IngredienteService ingredienteService;

    @Mock
    private PedidoService pedidoService;


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
    private User user, adminUser;
    private Authorities adminAuth; 
    private Authorities authority; 
    private Authorities duenoAuth;
    private User duenoUser;
    private User empleadoUser;
    private Authorities empleadoAuth;
    private Dueno dueno;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(lineaDePedidoController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    
        authority = new Authorities();
        authority.setId(1);
        authority.setAuthority("empleado");
    
        user = new User();
        user.setId(1);
        user.setUsername("juanperez");
        user.setPassword("password123");
        user.setAuthority(authority);
    
        adminAuth = new Authorities();
        adminAuth.setAuthority("admin");
        adminUser = new User();
        adminUser.setId(1);
        adminUser.setUsername("adminUser");
        adminUser.setAuthority(adminAuth);
    
        duenoAuth = new Authorities();
        duenoAuth.setAuthority("dueno");
        duenoUser = new User();
        duenoUser.setId(2);
        duenoUser.setUsername("duenoUser");
        duenoUser.setAuthority(duenoAuth);
    
        empleadoAuth = new Authorities();
        empleadoAuth.setAuthority("empleado");
        empleadoUser = new User();
        empleadoUser.setId(3);
        empleadoUser.setUsername("empleadoUser");
        empleadoUser.setAuthority(empleadoAuth);
    
        dueno = new Dueno();
        dueno.setId(1);
        dueno.setUser(duenoUser);
    
        negocio = new Negocio();
        negocio.setId(1);
        negocio.setName("Restaurante Test");
        negocio.setDireccion("Calle Test 123");
        negocio.setCiudad("Sevilla");
        negocio.setPais("Espana");
        negocio.setCodigoPostal("41001");
        negocio.setTokenNegocio(12345);
        negocio.setDueno(dueno);
    
        mesa = new Mesa();
        mesa.setName("Mesa 1");
        mesa.setNumeroAsientos(4);
        mesa.setNegocio(negocio);
    
        empleado = new Empleado();
        empleado.setFirstName("Antonio");
        empleado.setLastName("García");
        empleado.setEmail("antonio@test.com");
        empleado.setNumTelefono("666111222");
        empleado.setTokenEmpleado("TOKEN123");
        empleado.setUser(user);
        empleado.setNegocio(negocio);
    
        categoria = new Categoria();
        categoria.setName("Bebidas");
        categoria.setNegocio(negocio);
    
        producto1 = new ProductoVenta();
        producto1.setId(1);
        producto1.setName("Cerveza");
        producto1.setPrecioVenta(3.0);
        producto1.setCategoria(categoria);
    
        producto2 = new ProductoVenta();
        producto2.setId(2);
        producto2.setName("Vino");
        producto2.setPrecioVenta(5.0);
        producto2.setCategoria(categoria);
    
        pedido1 = new Pedido();
        pedido1.setId(1);
        pedido1.setFecha(LocalDateTime.now().minusHours(1));
        pedido1.setPrecioTotal(50.75);
        pedido1.setMesa(mesa);
        pedido1.setEmpleado(empleado);
        pedido1.setNegocio(negocio);
    
        pedido2 = new Pedido();
        pedido2.setId(2);
        pedido2.setFecha(LocalDateTime.now());
        pedido2.setPrecioTotal(75.50);
        pedido2.setMesa(mesa);
        pedido2.setEmpleado(empleado);
        pedido2.setNegocio(negocio);
    
        lineaNormal = new LineaDePedido();
        lineaNormal.setId(1);
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
    
        lineasDePedido = Arrays.asList(lineaNormal, lineaCantidadGrande, lineaPrecioAlto);
    
        lenient().when(empleadoService.getEmpleadoByUser(anyInt())).thenReturn(empleado);
        lenient().when(lineaDePedidoService.getById(anyInt())).thenReturn(lineaNormal);
    }
    
    
    // Global exception handler para manejar excepciones en los tests
    public class GlobalExceptionHandler {
        // Puedes agregar métodos para manejar excepciones específicas si es necesario
    }
    
    // TESTS PARA findAll()
    
    @Test
    void testFindAll_Success() throws Exception {
        // Given
        when(userService.findCurrentUser()).thenReturn(user);
        when(empleadoService.getEmpleadoByUser(user.getId())).thenReturn(empleado);
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
        when(userService.findCurrentUser()).thenReturn(user);
        when(empleadoService.getEmpleadoByUser(user.getId())).thenReturn(empleado);
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
        when(userService.findCurrentUser()).thenReturn(user);
        when(empleadoService.getEmpleadoByUser(user.getId())).thenReturn(empleado);
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
        // Crear un usuario admin de prueba
    
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(lineaDePedidoService.getById(999)).thenReturn(null);
        
        mockMvc.perform(get("/api/lineasDePedido/999"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
        
        verify(lineaDePedidoService).getById(999);
    }
    
    // TESTS PARA findByCantidad()
    
    @Test
    void testFindByCantidad_Success() throws Exception {
        // Given
        List<LineaDePedido> lineasCantidad3 = Collections.singletonList(linea);
        when(userService.findCurrentUser()).thenReturn(adminUser);
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

        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(lineaDePedidoService.getLineasDePedidoByCantidad(999)).thenReturn(null);
        
        // When & Then
        mockMvc.perform(get("/api/lineasDePedido/cantidad/999"))
                .andExpect(status().isNotFound());
        
        verify(lineaDePedidoService).getLineasDePedidoByCantidad(999);
    }
    
    @Test
    void testFindByCantidad_EmptyList() throws Exception {
        // Given
        when(userService.findCurrentUser()).thenReturn(adminUser);

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
        when(userService.findCurrentUser()).thenReturn(adminUser);
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
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(lineaDePedidoService.getLineasDePedidoByPrecioLinea(999.0)).thenReturn(null);
        
        // When & Then
        mockMvc.perform(get("/api/lineasDePedido/precioLinea/999.0"))
                .andExpect(status().isNotFound());
        
        verify(lineaDePedidoService).getLineasDePedidoByPrecioLinea(999.0);
    }
    
    @Test
    void testFindByPrecioLinea_EmptyList() throws Exception {
        // Given
        when(userService.findCurrentUser()).thenReturn(adminUser);
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
        when(userService.findCurrentUser()).thenReturn(adminUser);
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
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(lineaDePedidoService.getLineasDePedidoByPedidoId(999)).thenReturn(null);
        
        // When & Then
        mockMvc.perform(get("/api/lineasDePedido/pedido/999"))
                .andExpect(status().isNotFound());
        
        verify(lineaDePedidoService).getLineasDePedidoByPedidoId(999);
    }
    
    @Test
    void testFindByPedidoId_EmptyList() throws Exception {
        // Given
        when(userService.findCurrentUser()).thenReturn(adminUser);
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
        when(userService.findCurrentUser()).thenReturn(adminUser);
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
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(lineaDePedidoService.getLineasDePedidoByProductoId(999)).thenReturn(null);
        
        // When & Then
        mockMvc.perform(get("/api/lineasDePedido/producto/999"))
                .andExpect(status().isNotFound());
        
        verify(lineaDePedidoService).getLineasDePedidoByProductoId(999);
    }
    
    @Test
    void testFindByProductoId_EmptyList() throws Exception {
        // Given
        when(userService.findCurrentUser()).thenReturn(adminUser);
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
        when(userService.findCurrentUser()).thenReturn(adminUser);
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
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(lineaDePedidoService.getLineasDePedidoByProductoIdAndCantidad(1, 999)).thenReturn(null);
        
        // When & Then
        mockMvc.perform(get("/api/lineasDePedido/producto/1/cantidad/999"))
                .andExpect(status().isNotFound());
        
        verify(lineaDePedidoService).getLineasDePedidoByProductoIdAndCantidad(1, 999);
    }
    
    @Test
    void testFindByProductoIdAndCantidad_EmptyList() throws Exception {
        // Given
        when(userService.findCurrentUser()).thenReturn(adminUser);
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
        when(userService.findCurrentUser()).thenReturn(adminUser);
        List<LineaDePedido> lineasProducto1Precio9 = Collections.singletonList(linea);
        when(lineaDePedidoService.getLineasDePedidoByProductoIdAndPrecioUnitario(1, 9.0)).thenReturn(lineasProducto1Precio9);
        
        // When & Then: Cambiar 'precioLinea' por 'precioUnitario'
        mockMvc.perform(get("/api/lineasDePedido/producto/1/precioUnitario/9.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].precioLinea", is(9.0)));
        
        verify(lineaDePedidoService).getLineasDePedidoByProductoIdAndPrecioUnitario(1, 9.0);
    }
    
    @Test
    void testFindByProductoIdAndPrecioLinea_NotFound() throws Exception {
        // Given
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(lineaDePedidoService.getLineasDePedidoByProductoIdAndPrecioUnitario(1, 999.0)).thenReturn(null);
        
        // When & Then
        mockMvc.perform(get("/api/lineasDePedido/producto/1/precioUnitario/999.0"))
                .andExpect(status().isNotFound());
        
        verify(lineaDePedidoService).getLineasDePedidoByProductoIdAndPrecioUnitario(1, 999.0);
    }
    
    @Test
    void testFindByProductoIdAndPrecioLinea_EmptyList() throws Exception {
        // Given
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(lineaDePedidoService.getLineasDePedidoByProductoIdAndPrecioUnitario(1, 999.0)).thenReturn(Collections.emptyList());
        
        // When & Then
        mockMvc.perform(get("/api/lineasDePedido/producto/1/precioUnitario/999.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
        
        verify(lineaDePedidoService).getLineasDePedidoByProductoIdAndPrecioUnitario(1, 999.0);
    }
    
    // TESTS PARA save()
    
    @Test
    void testSave_Success() throws Exception {        
        // Crear LineaDePedidoDTO con los campos requeridos (sin idProducto)
        LineaDePedidoDTO lineaDto = new LineaDePedidoDTO();
        lineaDto.setCantidad(3);
        lineaDto.setPrecioUnitario(3.0);
        lineaDto.setPedidoId(1);
        lineaDto.setCategoriaProducto("Bebidas");
        lineaDto.setEstado(false);
        lineaDto.setNombreProducto("Cerveza");
        
        // Stub del servicio: se retorna 'linea' (preparado en setUp)
        when(userService.findCurrentUser()).thenReturn(user);
        when(empleadoService.getEmpleadoByUser(user.getId())).thenReturn(empleado);
        when(lineaDePedidoService.convertDtoLineaDePedido(any(LineaDePedidoDTO.class))).thenReturn(linea);
        when(lineaDePedidoService.save(any(LineaDePedido.class))).thenReturn(linea);
        

        mockMvc.perform(post("/api/lineasDePedido")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lineaDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
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
        // Create LineaDePedidoDTO with updated values
        LineaDePedidoDTO lineaDto = new LineaDePedidoDTO();
        lineaDto.setCantidad(3);
        lineaDto.setPrecioUnitario(3.0);
        lineaDto.setPedidoId(1);
        lineaDto.setCategoriaProducto("Bebidas");
        lineaDto.setNombreProducto("Cerveza");
        lineaDto.setEstado(false);

    
        // Create expected updated entity
        LineaDePedido lineaActualizada = new LineaDePedido();
        lineaActualizada.setCantidad(5);
        lineaActualizada.setPrecioUnitario(3.0);
        lineaActualizada.setProducto(producto1);
        lineaActualizada.setPedido(pedido1);
    
        // Setup mocks in the correct order
        when(userService.findCurrentUser()).thenReturn(user);
        when(empleadoService.getEmpleadoByUser(user.getId())).thenReturn(empleado);
        when(lineaDePedidoService.getById(1)).thenReturn(linea);
        when(lineaDePedidoService.convertDtoLineaDePedido(any(LineaDePedidoDTO.class))).thenReturn(lineaActualizada);
        when(lineaDePedidoService.update(eq(1), any(LineaDePedido.class))).thenReturn(lineaActualizada);
    
        // Act & Assert
        mockMvc.perform(put("/api/lineasDePedido/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lineaDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(lineaActualizada.getId())))
                .andExpect(jsonPath("$.cantidad", is(5)))
                .andExpect(jsonPath("$.precioLinea", is(15.0)));
    
        verify(lineaDePedidoService).getById(1);
        verify(lineaDePedidoService).convertDtoLineaDePedido(any(LineaDePedidoDTO.class));
        verify(lineaDePedidoService, never()).save(any(LineaDePedido.class));
    }
    /*
    @Test
    void testUpdate_NotFound() throws Exception {
        // Create DTO with updated values
        LineaDePedidoDTO lineaDto = new LineaDePedidoDTO();
        lineaDto.setCantidad(3);
        lineaDto.setPrecioUnitario(3.0);
        lineaDto.setPedidoId(1);
        lineaDto.setCategoriaProducto("Bebidas");
        lineaDto.setNombreProducto("Cerveza");
        lineaDto.setEstado(false);
    
        // Stub the service to simulate that no entity is found for the given ID
        when(userService.findCurrentUser()).thenReturn(user);
        when(empleadoService.getEmpleadoByUser(user.getId())).thenReturn(empleado);
        when(lineaDePedidoService.getById(999)).thenReturn(null);
    
        // Act & Assert: Given that the controller does not handle null,
        // a NullPointerException is thrown, resulting in a 500 Internal Server Error.
        mockMvc.perform(put("/api/lineasDePedido/999")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lineaDto)))
                .andExpect(status().isInternalServerError());
    
        verify(lineaDePedidoService).getById(999);
        verify(lineaDePedidoService, never()).convertDtoLineaDePedido(any(LineaDePedidoDTO.class));
        verify(lineaDePedidoService, never()).update(anyInt(), any(LineaDePedido.class));
    }
    */
    
    
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
        // Create an invalid DTO by not setting required fields
        LineaDePedidoDTO invalidLineaDto = new LineaDePedidoDTO();
        // For instance, if 'cantidad' and 'precioUnitario' are required, we leave them null
        
        mockMvc.perform(put("/api/lineasDePedido/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidLineaDto)))
                .andExpect(status().isBadRequest());
    }
    
    // TESTS PARA delete()
    
    @Test
    void testDelete_Success() throws Exception {
        // Given
        when(userService.findCurrentUser()).thenReturn(user);
        when(empleadoService.getEmpleadoByUser(user.getId())).thenReturn(empleado);
        when(lineaDePedidoService.getById(1)).thenReturn(lineaNormal);
        doNothing().when(lineaDePedidoService).delete(1);
        
        // When & Then
        mockMvc.perform(delete("/api/lineasDePedido/1"))
                .andExpect(status().isNoContent());
        
        verify(lineaDePedidoService).getById(1);
        verify(lineaDePedidoService).delete(1);
    }
    
  /* 
    @Test
    void testDelete_NotFound() throws Exception {
        // Given
        when(userService.findCurrentUser()).thenReturn(user);
        when(empleadoService.getEmpleadoByUser(user.getId())).thenReturn(empleado);
        // Simulamos que no se encuentra la línea de pedido:
        when(lineaDePedidoService.getById(999)).thenReturn(null);
        
        // When & Then: Esperamos un error interno (500) debido al NPE
        mockMvc.perform(delete("/api/lineasDePedido/999"))
                .andExpect(status().isInternalServerError());
        
        verify(lineaDePedidoService).getById(999);
        verify(lineaDePedidoService, never()).delete(anyInt());
    }
        */

    @Test
    void testFindAllDto_AsAdmin() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(lineaDePedidoService.getLineasDePedido()).thenReturn(lineasDePedido);
        mockMvc.perform(get("/api/lineasDePedido/dto"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void testFindAllDto_AsDueno_NoContent() throws Exception {
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(duenoService.getDuenoByUser(duenoUser.getId())).thenReturn(dueno);
    
        // Setup correcto del dueño en la línea de pedido
        Dueno otroDueno = new Dueno();
        otroDueno.setId(999); // distinto al dueño logueado
    
        Negocio negocioConOtroDueno = new Negocio();
        negocioConOtroDueno.setDueno(otroDueno);
    
        Mesa mesa = new Mesa();
        mesa.setNegocio(negocioConOtroDueno);
    
        Pedido pedido = new Pedido();
        pedido.setMesa(mesa);
    
        lineaNormal.setPedido(pedido); // línea de pedido con negocio no asociado al dueño logueado
    
        when(lineaDePedidoService.getLineasDePedido()).thenReturn(Collections.singletonList(lineaNormal));
    
        mockMvc.perform(get("/api/lineasDePedido/dto"))
               .andExpect(status().isNoContent());
    }

    @Test
    void testFindAllDto_AsEmpleado_NoContent() throws Exception {
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoByUser(empleadoUser.getId())).thenReturn(empleado);
        when(lineaDePedidoService.getLineasDePedido()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/lineasDePedido/dto"))
               .andExpect(status().isNoContent());
    }
    

    @Test
    void testFindByIdDto_AsAdmin() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(lineaDePedidoService.getById(1)).thenReturn(linea);
        mockMvc.perform(get("/api/lineasDePedido/dto/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.cantidad", is(linea.getCantidad())));
    }

    @Test
    void testFindByIdDto_AsDueno_Forbidden() throws Exception {
        User duenoLogged = spy(duenoUser);
        when(userService.findCurrentUser()).thenReturn(duenoLogged);
    
        Dueno duenoObj = new Dueno();
        duenoObj.setId(500);
        when(duenoService.getDuenoByUser(duenoLogged.getId())).thenReturn(duenoObj);
    
        Negocio negLinea = new Negocio();
        negLinea.setId(1);
        Dueno duenoLinea = new Dueno();
        duenoLinea.setId(501);
        negLinea.setDueno(duenoLinea);
        Mesa mesaLinea = new Mesa();
        mesaLinea.setNegocio(negLinea);
        Pedido pedido = new Pedido();
        pedido.setMesa(mesaLinea);
    
        LineaDePedido lineaObj = new LineaDePedido();
        lineaObj.setPedido(pedido);
    
        when(lineaDePedidoService.getById(1)).thenReturn(lineaObj);
    
        mockMvc.perform(get("/api/lineasDePedido/dto/1"))
               .andExpect(status().isForbidden());
    }
    
    @Test
    void testFindByIdDto_AsEmpleado_Forbidden() throws Exception {
        User empleadoLogged = spy(empleadoUser);
        when(userService.findCurrentUser()).thenReturn(empleadoLogged);
    
        Negocio negEmpleado = new Negocio();
        negEmpleado.setId(300);
        Empleado empleadoObj = new Empleado();
        empleadoObj.setNegocio(negEmpleado);
        when(empleadoService.getEmpleadoByUser(empleadoLogged.getId())).thenReturn(empleadoObj);
    
        Negocio negLinea = new Negocio();
        negLinea.setId(1);
        Mesa mesaLinea = new Mesa();
        mesaLinea.setNegocio(negLinea);
        Pedido pedido = new Pedido();
        pedido.setMesa(mesaLinea);
    
        LineaDePedido lineaObj = new LineaDePedido();
        lineaObj.setPedido(pedido);
    
        when(lineaDePedidoService.getById(1)).thenReturn(lineaObj);
    
        mockMvc.perform(get("/api/lineasDePedido/dto/1"))
               .andExpect(status().isForbidden());
    }
    

    @Test
    void testSave_AsDueno_Success() throws Exception {
        LineaDePedidoDTO dto = new LineaDePedidoDTO();
        dto.setCantidad(2);
        dto.setPrecioUnitario(3.0);
        dto.setPedidoId(1);
        dto.setNombreProducto("Cerveza");
        dto.setCategoriaProducto("Bebidas");
        dto.setEstado(false);

        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(duenoService.getDuenoByUser(duenoUser.getId())).thenReturn(dueno);
        when(lineaDePedidoService.convertDtoLineaDePedido(any())).thenReturn(linea);
        linea.getPedido().getMesa().getNegocio().getDueno().setId(dueno.getId());
        when(lineaDePedidoService.save(any())).thenReturn(linea);

        mockMvc.perform(post("/api/lineasDePedido")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.cantidad", is(3)));
    }

    @Test
    void testSave_AsEmpleado_Forbidden() throws Exception {
        LineaDePedidoDTO dto = new LineaDePedidoDTO();
        dto.setCantidad(1);
        dto.setPrecioUnitario(2.0);
        dto.setEstado(false);
        dto.setPedidoId(1);
        dto.setNombreProducto("ProductoX");
        dto.setCategoriaProducto("CategoriaY");
    
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoByUser(empleadoUser.getId())).thenReturn(empleado);
    
        Mesa mesa2 = new Mesa();
        Negocio negocio2 = new Negocio();
        negocio2.setId(2);
        mesa2.setNegocio(negocio2);
        Pedido pedidoConv = new Pedido();
        pedidoConv.setMesa(mesa2);
        LineaDePedido conv = new LineaDePedido();
        conv.setPedido(pedidoConv);
        conv.setCantidad(1);
        conv.setPrecioUnitario(2.0);
    
        when(lineaDePedidoService.convertDtoLineaDePedido(any(LineaDePedidoDTO.class)))
            .thenReturn(conv);
    
        mockMvc.perform(post("/api/lineasDePedido")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
               .andExpect(status().isForbidden());
    }
    

    @Test
    void testUpdateEstado_AsDueno_Success() throws Exception {
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(duenoService.getDuenoByUser(duenoUser.getId())).thenReturn(dueno);
        when(lineaDePedidoService.getById(1)).thenReturn(linea);
        linea.getPedido().getMesa().getNegocio().getDueno().setId(dueno.getId());
        when(lineaDePedidoService.cambiarEstado(1)).thenReturn(linea);

        mockMvc.perform(put("/api/lineasDePedido/1/saleDeCocina"))
               .andExpect(status().isOk());
    }

    

    @Test
    void testDelete_AsDueno_Success() throws Exception {
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(duenoService.getDuenoByUser(duenoUser.getId())).thenReturn(dueno);
        when(lineaDePedidoService.getById(1)).thenReturn(lineaNormal);
        lineaNormal.getPedido().getMesa().getNegocio().getDueno().setId(dueno.getId());
        doNothing().when(lineaDePedidoService).delete(1);

        mockMvc.perform(delete("/api/lineasDePedido/1"))
               .andExpect(status().isNoContent());
    }

    @Test
    void testDelete_AsEmpleado_Forbidden() throws Exception {
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        Negocio otroNegocio = new Negocio();
        otroNegocio.setId(999);
        empleado.setNegocio(otroNegocio);
        when(empleadoService.getEmpleadoByUser(empleadoUser.getId()))
            .thenReturn(empleado);
    
        when(lineaDePedidoService.getById(1)).thenReturn(lineaNormal);
    
        mockMvc.perform(delete("/api/lineasDePedido/1"))
               .andExpect(status().isForbidden());
    }

    @Test
    void testUpdate_AsAdmin_Created() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(ingredienteService.getIngredientesByProductoVentaId(anyInt())).thenReturn(Collections.emptyList());
        LineaDePedidoDTO dto = new LineaDePedidoDTO();
        dto.setCantidad(2);
        dto.setPrecioUnitario(5.0);
        dto.setPedidoId(1);
        dto.setEstado(false);
        dto.setNombreProducto("Cerveza");
        dto.setCategoriaProducto("Bebidas");
        LineaDePedido conv = new LineaDePedido();
        conv.setId(99);
        conv.setCantidad(2);
        conv.setPrecioUnitario(5.0);
        conv.setProducto(producto1);
        conv.setPedido(pedido1);
        when(lineaDePedidoService.convertDtoLineaDePedido(any())).thenReturn(conv);
        LineaDePedido updated = new LineaDePedido();
        updated.setId(99);
        updated.setCantidad(2);
        updated.setPrecioUnitario(5.0);
        updated.setProducto(producto1);
        updated.setPedido(pedido1);
        when(lineaDePedidoService.update(eq(99), any())).thenReturn(updated);
    
        mockMvc.perform(put("/api/lineasDePedido/99")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id", is(99)))
               .andExpect(jsonPath("$.cantidad", is(2)))
               .andExpect(jsonPath("$.precioLinea", is(10.0)));
    }
    
    @Test
    void testUpdate_AsDueno_Ok() throws Exception {
        dueno.setId(1);
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(duenoService.getDuenoByUser(duenoUser.getId())).thenReturn(dueno);
        when(lineaDePedidoService.getById(1)).thenReturn(lineaNormal);
        when(ingredienteService.getIngredientesByProductoVentaId(anyInt())).thenReturn(Collections.emptyList());
        LineaDePedidoDTO dto = new LineaDePedidoDTO();
        dto.setCantidad(3);
        dto.setPrecioUnitario(3.0);
        dto.setPedidoId(1);
        dto.setEstado(false);
        dto.setNombreProducto("Cerveza");
        dto.setCategoriaProducto("Bebidas");
        when(lineaDePedidoService.convertDtoLineaDePedido(any())).thenReturn(lineaNormal);
        when(lineaDePedidoService.update(eq(1), any())).thenReturn(lineaNormal);
    
        mockMvc.perform(put("/api/lineasDePedido/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id", is(1)))
               .andExpect(jsonPath("$.cantidad", is(5))); // línea normal tiene cantidad 5
    }
    

    
    @Test
    void testUpdate_AsEmpleado_Ok() throws Exception {
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoByUser(empleadoUser.getId())).thenReturn(empleado);
        when(lineaDePedidoService.getById(1)).thenReturn(lineaNormal);
        when(ingredienteService.getIngredientesByProductoVentaId(anyInt())).thenReturn(Collections.emptyList());
        LineaDePedidoDTO dto = new LineaDePedidoDTO();
        dto.setCantidad(4);
        dto.setPrecioUnitario(3.0);
        dto.setPedidoId(1);
        dto.setEstado(false);
        dto.setNombreProducto("Cerveza");
        dto.setCategoriaProducto("Bebidas");
        LineaDePedido conv = new LineaDePedido();
        conv.setId(1);
        conv.setCantidad(4);
        conv.setPrecioUnitario(3.0);
        conv.setProducto(producto1);
        conv.setPedido(pedido1);
        when(lineaDePedidoService.convertDtoLineaDePedido(any())).thenReturn(conv);
        when(lineaDePedidoService.update(eq(1), any())).thenReturn(conv);
    
        mockMvc.perform(put("/api/lineasDePedido/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.cantidad", is(4)))
               .andExpect(jsonPath("$.precioLinea", is(12.0)));
    }
    
    @Test
    void testFindByPedidoIdDto_AsAdmin_Found() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(lineaDePedidoService.getLineasDePedidoByPedidoId(1)).thenReturn(lineasDePedido);
    
        mockMvc.perform(get("/api/lineasDePedido/dto/pedido/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(3)))
               .andExpect(jsonPath("$[0].cantidad", is(5)))
               .andExpect(jsonPath("$[1].cantidad", is(10)))
               .andExpect(jsonPath("$[2].cantidad", is(4)));
    }
    
    @Test
    void testFindByPedidoIdDto_AsAdmin_NotFound() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(lineaDePedidoService.getLineasDePedidoByPedidoId(1)).thenReturn(null);
    
        mockMvc.perform(get("/api/lineasDePedido/dto/pedido/1"))
               .andExpect(status().isNotFound());
    }
    
    @Test
    void testFindByPedidoIdDto_AsDueno_Found() throws Exception {
        dueno.setId(1);
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(duenoService.getDuenoByUser(duenoUser.getId())).thenReturn(dueno);
        when(pedidoService.getById(1)).thenReturn(pedido1);
        when(lineaDePedidoService.getLineasDePedidoByPedidoId(1)).thenReturn(lineasDePedido);
    
        mockMvc.perform(get("/api/lineasDePedido/dto/pedido/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(3)));
    }
    
    @Test
    void testFindByPedidoIdDto_AsDueno_Forbidden() throws Exception {
        Dueno otroDueno = new Dueno();
        otroDueno.setId(99);
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(duenoService.getDuenoByUser(duenoUser.getId())).thenReturn(otroDueno);
        when(pedidoService.getById(1)).thenReturn(pedido1);
    
        mockMvc.perform(get("/api/lineasDePedido/dto/pedido/1"))
               .andExpect(status().isForbidden());
    }
    
    @Test
    void testFindByPedidoIdDto_AsEmpleado_Found() throws Exception {
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoByUser(empleadoUser.getId())).thenReturn(empleado);
        when(pedidoService.getById(1)).thenReturn(pedido1);
        when(lineaDePedidoService.getLineasDePedidoByPedidoId(1)).thenReturn(lineasDePedido);
    
        mockMvc.perform(get("/api/lineasDePedido/dto/pedido/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(3)));
    }
    
    @Test
    void testFindByPedidoIdDto_AsEmpleado_Forbidden() throws Exception {
        Pedido pedidoOtroNegocio = new Pedido();
        Mesa otraMesa = new Mesa();
        Negocio otroNegocio = new Negocio();
        otroNegocio.setId(999);
        otraMesa.setNegocio(otroNegocio);
        pedidoOtroNegocio.setMesa(otraMesa);
    
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoByUser(empleadoUser.getId())).thenReturn(empleado);
        when(pedidoService.getById(1)).thenReturn(pedidoOtroNegocio);
    
        mockMvc.perform(get("/api/lineasDePedido/dto/pedido/1"))
               .andExpect(status().isForbidden());
    }
    
    @Test
    void testFindByPedidoId_AsDueno_Authorized() throws Exception {
        dueno.setId(1); 
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(duenoService.getDuenoByUser(duenoUser.getId())).thenReturn(dueno);
        when(pedidoService.getById(1)).thenReturn(pedido1); 
        when(lineaDePedidoService.getLineasDePedidoByPedidoId(1)).thenReturn(lineasDePedido);

        mockMvc.perform(get("/api/lineasDePedido/pedido/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].cantidad", is(5)));
    }

    @Test
    void testFindByPedidoId_AsDueno_Forbidden() throws Exception {
        Dueno otroDueno = new Dueno();
        otroDueno.setId(999); // distinto ID
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(duenoService.getDuenoByUser(duenoUser.getId())).thenReturn(otroDueno);
        when(pedidoService.getById(1)).thenReturn(pedido1);

        mockMvc.perform(get("/api/lineasDePedido/pedido/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testFindByPedidoId_AsEmpleado_Authorized() throws Exception {
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoByUser(empleadoUser.getId())).thenReturn(empleado);
        when(pedidoService.getById(1)).thenReturn(pedido1); 
        when(lineaDePedidoService.getLineasDePedidoByPedidoId(1)).thenReturn(lineasDePedido);

        mockMvc.perform(get("/api/lineasDePedido/pedido/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[1].cantidad", is(10)));
    }

    @Test
    void testFindByPedidoId_AsEmpleado_Forbidden() throws Exception {
        Pedido pedidoOtroNegocio = new Pedido();
        Mesa otraMesa = new Mesa();
        Negocio otroNegocio = new Negocio();
        otroNegocio.setId(999); 
        otraMesa.setNegocio(otroNegocio);
        pedidoOtroNegocio.setMesa(otraMesa);
    
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoByUser(empleadoUser.getId())).thenReturn(empleado);
        when(pedidoService.getById(1)).thenReturn(pedidoOtroNegocio);
    
        mockMvc.perform(get("/api/lineasDePedido/pedido/1"))
                .andExpect(status().isForbidden());
    }
    @Test
    void testFindAll_AsAdmin_WithContent() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(lineaDePedidoService.getLineasDePedido()).thenReturn(lineasDePedido);
    
        mockMvc.perform(get("/api/lineasDePedido"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }
    
    @Test
    void testFindAll_AsAdmin_Empty() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(lineaDePedidoService.getLineasDePedido()).thenReturn(Collections.emptyList());
    
        mockMvc.perform(get("/api/lineasDePedido"))
                .andExpect(status().isNoContent());
    }
    
    @Test
    void testFindAll_AsDueno_WithContent() throws Exception {
        dueno.setId(1);
        lineaNormal.getPedido().getMesa().getNegocio().setDueno(dueno);
        lineaCantidadGrande.getPedido().getMesa().getNegocio().setDueno(dueno);
        lineaPrecioAlto.getPedido().getMesa().getNegocio().setDueno(dueno);
    
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(duenoService.getDuenoByUser(duenoUser.getId())).thenReturn(dueno);
        when(lineaDePedidoService.getLineasDePedido()).thenReturn(lineasDePedido);
    
        mockMvc.perform(get("/api/lineasDePedido"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }
    
    @Test
    void testFindAll_AsDueno_Empty() throws Exception {
        dueno.setId(999);
        lineaNormal.getPedido().getMesa().getNegocio().getDueno().setId(1);
    
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(duenoService.getDuenoByUser(duenoUser.getId())).thenReturn(dueno);
        when(lineaDePedidoService.getLineasDePedido()).thenReturn(lineasDePedido);
    
        mockMvc.perform(get("/api/lineasDePedido"))
                .andExpect(status().isOk());
    }
    
    @Test
    void testFindAll_AsEmpleado_WithContent() throws Exception {
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoByUser(empleadoUser.getId())).thenReturn(empleado);
        when(lineaDePedidoService.getLineasDePedido()).thenReturn(lineasDePedido);
    
        mockMvc.perform(get("/api/lineasDePedido"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }
    
    @Test
    void testFindAll_AsEmpleado_Empty() throws Exception {
        empleado.getNegocio().setId(999);
    
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoByUser(empleadoUser.getId())).thenReturn(empleado);
        when(lineaDePedidoService.getLineasDePedido()).thenReturn(lineasDePedido);
    
        mockMvc.perform(get("/api/lineasDePedido"))
                .andExpect(status().isOk());
    }
    
    @Test
    void testFindAll_UnauthorizedRole() throws Exception {
        User userSinRol = new User();
        Authorities noRole = new Authorities();
        noRole.setAuthority("cliente");
        userSinRol.setAuthority(noRole);
    
        when(userService.findCurrentUser()).thenReturn(userSinRol);
    
        mockMvc.perform(get("/api/lineasDePedido"))
                .andExpect(status().isForbidden());
    }
    

    
}