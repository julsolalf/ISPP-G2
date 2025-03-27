package ispp_g2.gastrostock.testLote;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import ispp_g2.gastrostock.lote.Lote;
import ispp_g2.gastrostock.lote.LoteRepository;
import ispp_g2.gastrostock.lote.LoteService;
import ispp_g2.gastrostock.productoInventario.ProductoInventario;
import ispp_g2.gastrostock.reabastecimiento.Reabastecimiento;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class LoteServiceTest {
        
    @Mock
    private LoteRepository loteRepository;
    
    @InjectMocks
    private LoteService loteService;
    
    private Lote lote1, lote2, lote3;
    private ProductoInventario producto;
    private Reabastecimiento reabastecimiento;
    
    @BeforeEach
    void setUp() {
        // Crear producto
        producto = new ProductoInventario();
        producto.setId(1);
        producto.setName("Harina");
        producto.setPrecioCompra(2.5);
        
        // Crear reabastecimiento
        reabastecimiento = new Reabastecimiento();
        reabastecimiento.setId(1);
        reabastecimiento.setReferencia("REF001");
        
        // Crear lotes
        lote1 = new Lote();
        lote1.setId(1);
        lote1.setCantidad(100);
        lote1.setFechaCaducidad(LocalDate.now().plusMonths(3));
        lote1.setProducto(producto);
        lote1.setReabastecimiento(reabastecimiento);
        
        lote2 = new Lote();
        lote2.setId(2);
        lote2.setCantidad(50);
        lote2.setFechaCaducidad(LocalDate.now().plusMonths(6));
        lote2.setProducto(producto);
        lote2.setReabastecimiento(reabastecimiento);
        
        lote3 = new Lote();
        lote3.setId(3);
        lote3.setCantidad(0); // Caso extremo: cantidad cero
        lote3.setFechaCaducidad(LocalDate.now().minusDays(1)); // Caso extremo: caducado
        lote3.setProducto(producto);
        lote3.setReabastecimiento(reabastecimiento);
    }
    
    // TESTS PARA getById
    
    @Test
    void testGetById_Success() {
        // Arrange
        when(loteRepository.findById("1")).thenReturn(Optional.of(lote1));
        
        // Act
        Lote result = loteService.getById("1");
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(100, result.getCantidad());
        verify(loteRepository).findById("1");
    }
    
    @Test
    void testGetById_NotFound() {
        // Arrange
        when(loteRepository.findById("999")).thenReturn(Optional.empty());
        
        // Act
        Lote result = loteService.getById("999");
        
        // Assert
        assertNull(result);
        verify(loteRepository).findById("999");
    }
    /* 
    @Test
    void testGetById_NullId() {
        // Arrange & Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            loteService.getById(null);
        });
    }
    */
    // TESTS PARA getLotes
    
    @Test
    void testGetLotes_Success() {
        // Arrange
        List<Lote> lotes = Arrays.asList(lote1, lote2);
        when(loteRepository.findAll()).thenReturn(lotes);
        
        // Act
        List<Lote> result = loteService.getLotes();
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(2, result.get(1).getId());
        verify(loteRepository).findAll();
    }
    
    @Test
    void testGetLotes_EmptyList() {
        // Arrange
        when(loteRepository.findAll()).thenReturn(new ArrayList<>());
        
        // Act
        List<Lote> result = loteService.getLotes();
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(loteRepository).findAll();
    }
    
    // TESTS PARA getLotesByCantidad
    
    @Test
    void testGetLotesByCantidad_Success() {
        // Arrange
        List<Lote> lotes = Arrays.asList(lote1);
        when(loteRepository.findByCantidad(100)).thenReturn(lotes);
        
        // Act
        List<Lote> result = loteService.getLotesByCantidad(100);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(100, result.get(0).getCantidad());
        verify(loteRepository).findByCantidad(100);
    }
    
    @Test
    void testGetLotesByCantidad_NotFound() {
        // Arrange
        when(loteRepository.findByCantidad(999)).thenReturn(new ArrayList<>());
        
        // Act
        List<Lote> result = loteService.getLotesByCantidad(999);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(loteRepository).findByCantidad(999);
    }
    
    @Test
    void testGetLotesByCantidad_ZeroCantidad() {
        // Arrange
        List<Lote> lotes = Arrays.asList(lote3);
        when(loteRepository.findByCantidad(0)).thenReturn(lotes);
        
        // Act
        List<Lote> result = loteService.getLotesByCantidad(0);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(0, result.get(0).getCantidad());
        verify(loteRepository).findByCantidad(0);
    }
    
    // TESTS PARA getLotesByFechaCaducidad
    
    @Test
    void testGetLotesByFechaCaducidad_Success() {
        // Arrange
        LocalDate fecha = LocalDate.now().plusMonths(3);
        List<Lote> lotes = Arrays.asList(lote1);
        when(loteRepository.findByFechaCaducidad(fecha)).thenReturn(lotes);
        
        // Act
        List<Lote> result = loteService.getLotesByFechaCaducidad(fecha);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(fecha, result.get(0).getFechaCaducidad());
        verify(loteRepository).findByFechaCaducidad(fecha);
    }
    
    @Test
    void testGetLotesByFechaCaducidad_NotFound() {
        // Arrange
        LocalDate fecha = LocalDate.now().plusYears(10);
        when(loteRepository.findByFechaCaducidad(fecha)).thenReturn(new ArrayList<>());
        
        // Act
        List<Lote> result = loteService.getLotesByFechaCaducidad(fecha);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(loteRepository).findByFechaCaducidad(fecha);
    }
    
    @Test
    void testGetLotesByFechaCaducidad_PastDate() {
        // Arrange
        LocalDate fechaPasada = LocalDate.now().minusDays(1);
        List<Lote> lotes = Arrays.asList(lote3);
        when(loteRepository.findByFechaCaducidad(fechaPasada)).thenReturn(lotes);
        
        // Act
        List<Lote> result = loteService.getLotesByFechaCaducidad(fechaPasada);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(fechaPasada, result.get(0).getFechaCaducidad());
        verify(loteRepository).findByFechaCaducidad(fechaPasada);
    }
    
    // TESTS PARA getLotesByProductoId
    
    @Test
    void testGetLotesByProductoId_Success() {
        // Arrange
        List<Lote> lotes = Arrays.asList(lote1, lote2);
        when(loteRepository.findByProductoId(1)).thenReturn(lotes);
        
        // Act
        List<Lote> result = loteService.getLotesByProductoId(1);
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getProducto().getId());
        assertEquals(1, result.get(1).getProducto().getId());
        verify(loteRepository).findByProductoId(1);
    }
    
    @Test
    void testGetLotesByProductoId_NotFound() {
        // Arrange
        when(loteRepository.findByProductoId(999)).thenReturn(new ArrayList<>());
        
        // Act
        List<Lote> result = loteService.getLotesByProductoId(999);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(loteRepository).findByProductoId(999);
    }
    
    // TESTS PARA getLotesByReabastecimientoId
    
    @Test
    void testGetLotesByReabastecimientoId_Success() {
        // Arrange
        List<Lote> lotes = Arrays.asList(lote1, lote2);
        when(loteRepository.findByReabastecimientoId(1)).thenReturn(lotes);
        
        // Act
        List<Lote> result = loteService.getLotesByReabastecimientoId(1);
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getReabastecimiento().getId());
        assertEquals(1, result.get(1).getReabastecimiento().getId());
        verify(loteRepository).findByReabastecimientoId(1);
    }
    
    @Test
    void testGetLotesByReabastecimientoId_NotFound() {
        // Arrange
        when(loteRepository.findByReabastecimientoId(999)).thenReturn(new ArrayList<>());
        
        // Act
        List<Lote> result = loteService.getLotesByReabastecimientoId(999);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(loteRepository).findByReabastecimientoId(999);
    }
    
    // TESTS PARA save
    
    @Test
    void testSave_NewLote() {
        // Arrange
        Lote nuevoLote = new Lote();
        nuevoLote.setCantidad(200);
        nuevoLote.setFechaCaducidad(LocalDate.now().plusMonths(9));
        nuevoLote.setProducto(producto);
        nuevoLote.setReabastecimiento(reabastecimiento);
        
        when(loteRepository.save(any(Lote.class))).thenAnswer(invocation -> {
            Lote loteSaved = invocation.getArgument(0);
            loteSaved.setId(4); // Simular asignación de ID
            return loteSaved;
        });
        
        // Act
        Lote result = loteService.save(nuevoLote);
        
        // Assert
        assertNotNull(result);
        assertEquals(4, result.getId());
        assertEquals(200, result.getCantidad());
        verify(loteRepository).save(nuevoLote);
    }
    
    @Test
    void testSave_UpdateLote() {
        // Arrange
        lote1.setCantidad(150); // Cambiar cantidad
        
        when(loteRepository.save(any(Lote.class))).thenReturn(lote1);
        
        // Act
        Lote result = loteService.save(lote1);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(150, result.getCantidad());
        verify(loteRepository).save(lote1);
    }
    /* 
    @Test
    void testSave_NullLote() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            loteService.save(null);
        });
    }
    */
    // TESTS PARA delete
    
    @Test
    void testDelete_Success() {
        // Arrange - No setup needed for void methods
        doNothing().when(loteRepository).deleteById("1");
        
        // Act
        loteService.delete("1");
        
        // Assert
        verify(loteRepository).deleteById("1");
    }
    
    @Test
    void testDelete_NonExistentId() {
        // Arrange
        doThrow(new RuntimeException("ID not found")).when(loteRepository).deleteById("999");
        
        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            loteService.delete("999");
        });
        verify(loteRepository).deleteById("999");
    }
    /* 
    @Test
    void testDelete_NullId() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            loteService.delete(null);
        });
    }
*/
}