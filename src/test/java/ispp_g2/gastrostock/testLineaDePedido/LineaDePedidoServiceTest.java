package ispp_g2.gastrostock.testLineaDePedido;
import ispp_g2.gastrostock.lineaDePedido.LineaDePedido;
import ispp_g2.gastrostock.lineaDePedido.LineaDePedidoRepository;
import ispp_g2.gastrostock.lineaDePedido.LineaDePedidoService;
import ispp_g2.gastrostock.pedido.Pedido;
import ispp_g2.gastrostock.productoVenta.ProductoVenta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class LineaDePedidoServiceTest {

    @Mock
    private LineaDePedidoRepository repository;

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
}
