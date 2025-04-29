package ispp_g2.gastrostock.testLineaDePedido;
import ispp_g2.gastrostock.exceptions.ResourceNotFoundException;
import ispp_g2.gastrostock.ingrediente.Ingrediente;
import ispp_g2.gastrostock.ingrediente.IngredienteRepository;
import ispp_g2.gastrostock.lineaDePedido.LineaDePedido;
import ispp_g2.gastrostock.lineaDePedido.LineaDePedidoDTO;
import ispp_g2.gastrostock.lineaDePedido.LineaDePedidoRepository;
import ispp_g2.gastrostock.lineaDePedido.LineaDePedidoService;
import ispp_g2.gastrostock.lote.Lote;
import ispp_g2.gastrostock.lote.LoteRepository;
import ispp_g2.gastrostock.mesa.Mesa;
import ispp_g2.gastrostock.pedido.Pedido;
import ispp_g2.gastrostock.pedido.PedidoRepository;
import ispp_g2.gastrostock.productoInventario.ProductoInventario;
import ispp_g2.gastrostock.productoVenta.ProductoVenta;
import ispp_g2.gastrostock.productoVenta.ProductoVentaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class LineaDePedidoServiceTest {

    @Mock
    private LineaDePedidoRepository repository;

    @Mock
    private ProductoVentaRepository productoVentaRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private IngredienteRepository ingredienteRepository;

    @Mock
    private LoteRepository loteRepository;

    @InjectMocks
    private LineaDePedidoService service;

    private LineaDePedido linea;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        linea = new LineaDePedido();
        linea.setCantidad(2);
        linea.setPrecioUnitario(100.0);
        linea.setPedido(new Pedido());
        linea.setProducto(new ProductoVenta());
    }

    @Test
    void testGetById() {
        when(repository.findById(1)).thenReturn(Optional.of(linea));
        LineaDePedido result = service.getById(1);
        assertNotNull(result);
        assertEquals(2, result.getCantidad());
    }

    @Test
    void testGetLineasDePedido() {
        when(repository.findAll()).thenReturn(List.of(linea));
        List<LineaDePedido> result = service.getLineasDePedido();
        assertFalse(result.isEmpty());
    }

    @Test
    void testGetLineasDePedidoByCantidad() {
        when(repository.findLineaDePedidosByCantidad(2)).thenReturn(List.of(linea));
        List<LineaDePedido> result = service.getLineasDePedidoByCantidad(2);
        assertFalse(result.isEmpty());
    }

    @Test
    void testGetLineasDePedidoByPrecioLinea() {
        when(repository.findLineaDePedidosByPrecioUnitario(100.0)).thenReturn(List.of(linea));
        List<LineaDePedido> result = service.getLineasDePedidoByPrecioLinea(100.0);
        assertFalse(result.isEmpty());
    }

    @Test
    void testGetLineasDePedidoByPedidoId() {
        when(repository.findLineaDePedidosByPedidoId(1)).thenReturn(List.of(linea));
        List<LineaDePedido> result = service.getLineasDePedidoByPedidoId(1);
        assertFalse(result.isEmpty());
    }

    @Test
    void testGetLineasDePedidoByProductoId() {
        when(repository.findLineaDePedidosByProductoId(1)).thenReturn(List.of(linea));
        List<LineaDePedido> result = service.getLineasDePedidoByProductoId(1);
        assertFalse(result.isEmpty());
    }

    @Test
    void testGetLineasDePedidoByProductoIdAndCantidad() {
        when(repository.findLineaDePedidosByProductoIdAndCantidad(1, 2)).thenReturn(List.of(linea));
        List<LineaDePedido> result = service.getLineasDePedidoByProductoIdAndCantidad(1, 2);
        assertFalse(result.isEmpty());
    }

    @Test
    void testGetLineasDePedidoByProductoIdAndPrecioLinea() {
        when(repository.findLineaDePedidosByProductoIdAndPrecioUnitario(1, 100.0)).thenReturn(List.of(linea));
        List<LineaDePedido> result = service.getLineasDePedidoByProductoIdAndPrecioUnitario(1, 100.0);
        assertFalse(result.isEmpty());
    }

    @Test
    void testSave() {
        when(repository.save(any(LineaDePedido.class))).thenReturn(linea);
        LineaDePedido result = service.save(linea);
        assertNotNull(result);
        verify(repository, times(1)).save(linea);
    }

    @Test
    void testDelete() {
        doNothing().when(repository).deleteById(1);
        service.delete(1);
        verify(repository, times(1)).deleteById(1);
    }

    @Test
    void testUpdate() {
        LineaDePedido updated = new LineaDePedido();
        updated.setCantidad(5);
        updated.setPrecioUnitario(15.0);
        updated.setSalioDeCocina(true);
        updated.setPedido(linea.getPedido());
        updated.setProducto(linea.getProducto());

        when(repository.findById(1)).thenReturn(Optional.of(linea));
        when(repository.save(any())).thenReturn(linea);

        LineaDePedido result = service.update(1, updated);
        assertEquals(5, result.getCantidad()); 
    }

    @Test
    void testUpdateThrowsException() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.update(1, linea));
    }

    @Test
    void testCambiarEstadoYaSalio() {
        linea.setSalioDeCocina(true);
        when(repository.findById(1)).thenReturn(Optional.of(linea));
        LineaDePedido result = service.cambiarEstado(1);
        assertTrue(result.getSalioDeCocina());
        verify(repository, never()).save(any());
    }

    @Test
    void testCambiarEstadoNormal() {
        linea.setSalioDeCocina(false);
    
        Ingrediente ingrediente = new Ingrediente();
        ingrediente.setCantidad(1);
        ProductoInventario inventario = new ProductoInventario();
        inventario.setId(1);
        ingrediente.setProductoInventario(inventario);
    
        Lote lote = new Lote();
        lote.setCantidad(10);
        lote.setFechaCaducidad(LocalDate.now().plusDays(1));
    
        ProductoVenta productoVenta = new ProductoVenta();
        productoVenta.setId(1);
        linea.setProducto(productoVenta);
    
        Pedido pedido = new Pedido();
        pedido.setPrecioTotal(0.0); 
        linea.setPedido(pedido);
    
        when(repository.findById(1)).thenReturn(Optional.of(linea));
        when(ingredienteRepository.findByProductoVentaId(1)).thenReturn(List.of(ingrediente));
        when(loteRepository.findByProductoId(1)).thenReturn(List.of(lote));
        when(repository.save(any())).thenReturn(linea);
    
        LineaDePedido result = service.cambiarEstado(1);
        assertTrue(result.getSalioDeCocina());
        verify(loteRepository).saveAll(any());
    }
    
    

    @Test
    void testCambiarEstadoThrowsException() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.cambiarEstado(1));
    }

    @Test
    void testConvertDtoLineaDePedido() {
        LineaDePedidoDTO dto = new LineaDePedidoDTO();
        dto.setCantidad(2);
        dto.setPrecioUnitario(10.0);
        dto.setEstado(false);
        dto.setPedidoId(1);
        dto.setNombreProducto("Tortilla");

        ProductoVenta producto = new ProductoVenta();
        producto.setId(1);
        ispp_g2.gastrostock.negocio.Negocio negocio = new ispp_g2.gastrostock.negocio.Negocio();
        negocio.setId(1);

        Pedido pedido = new Pedido();
        Mesa mesa = new Mesa();
        mesa.setNegocio(negocio);
        pedido.setMesa(mesa);

        when(pedidoRepository.findById(1)).thenReturn(Optional.of(pedido));
        when(productoVentaRepository.findProductoVentaByNombreAndNegocioId("Tortilla", 1)).thenReturn(Optional.of(producto));

        LineaDePedido result = service.convertDtoLineaDePedido(dto);
        assertEquals(2, result.getCantidad());
        assertEquals(producto, result.getProducto());
    }

    @Test
    void testConvertDtoLineaDePedidoPedidoNoExiste() {
        LineaDePedidoDTO dto = new LineaDePedidoDTO();
        dto.setPedidoId(1);
        when(pedidoRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.convertDtoLineaDePedido(dto));
    }

    @Test
    void testConvertDtoLineaDePedidoProductoNoExiste() {
        LineaDePedidoDTO dto = new LineaDePedidoDTO();
        dto.setPedidoId(1);
        dto.setNombreProducto("Inexistente");

        Pedido pedido = new Pedido();
        Mesa mesa = new Mesa();
        ispp_g2.gastrostock.negocio.Negocio negocio = new ispp_g2.gastrostock.negocio.Negocio();
        negocio.setId(1);
        mesa.setNegocio(negocio);
        pedido.setMesa(mesa);

        when(pedidoRepository.findById(1)).thenReturn(Optional.of(pedido));
        when(productoVentaRepository.findProductoVentaByNombreAndNegocioId("Inexistente", 1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.convertDtoLineaDePedido(dto));
    }
}
