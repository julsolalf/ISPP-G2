package ispp_g2.gastrostock.testPedido;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import ispp_g2.gastrostock.empleado.Empleado;
import ispp_g2.gastrostock.mesa.Mesa;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.pedido.Pedido;
import ispp_g2.gastrostock.pedido.PedidoRepository;
import ispp_g2.gastrostock.pedido.PedidoService;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {
    
    @Mock
    private PedidoRepository pedidoRepository;
    
    @InjectMocks
    private PedidoService pedidoService;
    
    private Pedido pedido;
    private Mesa mesa;
    private Empleado empleado;
    private Negocio negocio;
    private LocalDateTime fecha;
    
    @BeforeEach
    void setUp() {
        fecha = LocalDateTime.now();
        
        // Set up Mesa
        mesa = new Mesa();
        mesa.setId(1);
        mesa.setName("Mesa 1");
        mesa.setNumeroAsientos(4);
        
        // Set up Empleado
        empleado = new Empleado();
        empleado.setId(1);
        empleado.setName("Empleado Test");
        empleado.setTokenEmpleado("EMP123");
        
        // Set up Negocio
        negocio = new Negocio();
        negocio.setId(1);
        negocio.setName("Restaurante Test");
        
        // Set up Pedido
        pedido = new Pedido();
        pedido.setId(1);
        pedido.setFecha(fecha);
        pedido.setPrecioTotal(45.50);
        pedido.setMesa(mesa);
        pedido.setEmpleado(empleado);
        pedido.setNegocio(negocio);
    }
    
    @Test
    void testGetById_Success() {
        // Given
        when(pedidoRepository.findById("1")).thenReturn(Optional.of(pedido));
        
        // When
        Pedido result = pedidoService.getById("1");
        
        // Then
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(45.50, result.getPrecioTotal());
        verify(pedidoRepository, times(1)).findById("1");
    }
    
    @Test
    void testGetById_NotFound() {
        // Given
        when(pedidoRepository.findById("999")).thenReturn(Optional.empty());
        
        // When
        Pedido result = pedidoService.getById("999");
        
        // Then
        assertNull(result);
        verify(pedidoRepository, times(1)).findById("999");
    }
    
    @Test
    void testGetAll_Success() {
        // Given
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
        
        when(pedidoRepository.findAll()).thenReturn(pedidos);
        
        // When
        List<Pedido> results = pedidoService.getAll();
        
        // Then
        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals(1, results.get(0).getId());
        assertEquals(2, results.get(1).getId());
        verify(pedidoRepository, times(1)).findAll();
    }
    
    @Test
    void testGetAll_EmptyList() {
        // Given
        when(pedidoRepository.findAll()).thenReturn(Collections.emptyList());
        
        // When
        List<Pedido> results = pedidoService.getAll();
        
        // Then
        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(pedidoRepository, times(1)).findAll();
    }
    
    @Test
    void testGetPedidoByFecha_Success() {
        // Given
        List<Pedido> pedidos = Collections.singletonList(pedido);
        when(pedidoRepository.findPedidoByFecha(fecha)).thenReturn(pedidos);
        
        // When
        List<Pedido> results = pedidoService.getPedidoByFecha(fecha);
        
        // Then
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(1, results.get(0).getId());
        verify(pedidoRepository, times(1)).findPedidoByFecha(fecha);
    }
    
    @Test
    void testGetPedidoByFecha_NoResults() {
        // Given
        LocalDateTime otherDate = fecha.plusDays(1);
        when(pedidoRepository.findPedidoByFecha(otherDate)).thenReturn(Collections.emptyList());
        
        // When
        List<Pedido> results = pedidoService.getPedidoByFecha(otherDate);
        
        // Then
        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(pedidoRepository, times(1)).findPedidoByFecha(otherDate);
    }
    
    @Test
    void testGetPedidoByPrecioTotal_Success() {
        // Given
        List<Pedido> pedidos = Collections.singletonList(pedido);
        when(pedidoRepository.findPedidoByPrecioTotal(45.50)).thenReturn(pedidos);
        
        // When
        List<Pedido> results = pedidoService.getPedidoByPrecioTotal(45.50);
        
        // Then
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(45.50, results.get(0).getPrecioTotal());
        verify(pedidoRepository, times(1)).findPedidoByPrecioTotal(45.50);
    }
    
    @Test
    void testGetPedidoByPrecioTotal_NoResults() {
        // Given
        when(pedidoRepository.findPedidoByPrecioTotal(99.99)).thenReturn(Collections.emptyList());
        
        // When
        List<Pedido> results = pedidoService.getPedidoByPrecioTotal(99.99);
        
        // Then
        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(pedidoRepository, times(1)).findPedidoByPrecioTotal(99.99);
    }
    
    @Test
    void testGetPedidoByMesaId_Success() {
        // Given
        List<Pedido> pedidos = Collections.singletonList(pedido);
        when(pedidoRepository.findPedidoByMesaId(1)).thenReturn(pedidos);
        
        // When
        List<Pedido> results = pedidoService.getPedidoByMesaId(1);
        
        // Then
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(1, results.get(0).getMesa().getId());
        verify(pedidoRepository, times(1)).findPedidoByMesaId(1);
    }
    
    @Test
    void testGetPedidoByMesaId_NoResults() {
        // Given
        when(pedidoRepository.findPedidoByMesaId(999)).thenReturn(Collections.emptyList());
        
        // When
        List<Pedido> results = pedidoService.getPedidoByMesaId(999);
        
        // Then
        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(pedidoRepository, times(1)).findPedidoByMesaId(999);
    }
    
    @Test
    void testGetPedidoByEmpleadoId_Success() {
        // Given
        List<Pedido> pedidos = Collections.singletonList(pedido);
        when(pedidoRepository.findPedidoByEmpleadoId(1)).thenReturn(pedidos);
        
        // When
        List<Pedido> results = pedidoService.getPedidoByEmpleadoId(1);
        
        // Then
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(1, results.get(0).getEmpleado().getId());
        verify(pedidoRepository, times(1)).findPedidoByEmpleadoId(1);
    }
    
    @Test
    void testGetPedidoByEmpleadoId_NoResults() {
        // Given
        when(pedidoRepository.findPedidoByEmpleadoId(999)).thenReturn(Collections.emptyList());
        
        // When
        List<Pedido> results = pedidoService.getPedidoByEmpleadoId(999);
        
        // Then
        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(pedidoRepository, times(1)).findPedidoByEmpleadoId(999);
    }
    
    @Test
    void testGetPedidoByNegocioId_Success() {
        // Given
        List<Pedido> pedidos = Collections.singletonList(pedido);
        when(pedidoRepository.findPedidoByNegocioId(1)).thenReturn(pedidos);
        
        // When
        List<Pedido> results = pedidoService.getPedidoByNegocioId(1);
        
        // Then
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(1, results.get(0).getNegocio().getId());
        verify(pedidoRepository, times(1)).findPedidoByNegocioId(1);
    }
    
    @Test
    void testGetPedidoByNegocioId_NoResults() {
        // Given
        when(pedidoRepository.findPedidoByNegocioId(999)).thenReturn(Collections.emptyList());
        
        // When
        List<Pedido> results = pedidoService.getPedidoByNegocioId(999);
        
        // Then
        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(pedidoRepository, times(1)).findPedidoByNegocioId(999);
    }
    
    @Test
    void testSave_Success() {
        // Given
        when(pedidoRepository.save(pedido)).thenReturn(pedido);
        
        // When
        Pedido result = pedidoService.save(pedido);
        
        // Then
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(45.50, result.getPrecioTotal());
        verify(pedidoRepository, times(1)).save(pedido);
    }
    
    @Test
    void testSave_NewPedido() {
        // Given
        Pedido newPedido = new Pedido();
        newPedido.setFecha(LocalDateTime.now());
        newPedido.setPrecioTotal(75.25);
        newPedido.setMesa(mesa);
        newPedido.setEmpleado(empleado);
        newPedido.setNegocio(negocio);
        
        Pedido savedPedido = new Pedido();
        savedPedido.setId(2);
        savedPedido.setFecha(newPedido.getFecha());
        savedPedido.setPrecioTotal(newPedido.getPrecioTotal());
        savedPedido.setMesa(newPedido.getMesa());
        savedPedido.setEmpleado(newPedido.getEmpleado());
        savedPedido.setNegocio(newPedido.getNegocio());
        
        when(pedidoRepository.save(newPedido)).thenReturn(savedPedido);
        
        // When
        Pedido result = pedidoService.save(newPedido);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.getId());
        assertEquals(75.25, result.getPrecioTotal());
        verify(pedidoRepository, times(1)).save(newPedido);
    }
    
    @Test
    void testDelete_Success() {
        // Given
        doNothing().when(pedidoRepository).deleteById("1");
        
        // When
        pedidoService.delete("1");
        
        // Then
        verify(pedidoRepository, times(1)).deleteById("1");
    }
    
    @Test
    void testDelete_NonexistentId() {
        // Given
        doThrow(new RuntimeException("Entity not found")).when(pedidoRepository).deleteById("999");
        
        // When & Then
        assertThrows(RuntimeException.class, () -> pedidoService.delete("999"));
        verify(pedidoRepository, times(1)).deleteById("999");
    }
    
    // Edge cases
    
    @Test
    void testGetById_NullId() {
        // Given
        when(pedidoRepository.findById(null)).thenThrow(new IllegalArgumentException("ID cannot be null"));
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> pedidoService.getById(null));
        verify(pedidoRepository, times(1)).findById(null);
    }
    
    @Test
    void testGetPedidoByFecha_NullDate() {
        // Given
        when(pedidoRepository.findPedidoByFecha(null)).thenThrow(new IllegalArgumentException("Date cannot be null"));
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> pedidoService.getPedidoByFecha(null));
        verify(pedidoRepository, times(1)).findPedidoByFecha(null);
    }
    
    @Test
    void testGetPedidoByPrecioTotal_NegativePrice() {
        // Given
        when(pedidoRepository.findPedidoByPrecioTotal(-10.0)).thenReturn(Collections.emptyList());
        
        // When
        List<Pedido> results = pedidoService.getPedidoByPrecioTotal(-10.0);
        
        // Then
        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(pedidoRepository, times(1)).findPedidoByPrecioTotal(-10.0);
    }
    
    @Test
    void testSave_NullPedido() {
        // Given
        when(pedidoRepository.save(null)).thenThrow(new IllegalArgumentException("Pedido cannot be null"));
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> pedidoService.save(null));
        verify(pedidoRepository, times(1)).save(null);
    }
}

