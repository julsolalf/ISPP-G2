package ispp_g2.gastrostock.testCarrito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import ispp_g2.gastrostock.carrito.Carrito;
import ispp_g2.gastrostock.carrito.CarritoRepository;
import ispp_g2.gastrostock.carrito.CarritoService;
import ispp_g2.gastrostock.proveedores.Proveedor;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class CarritoServiceTest {

    @Mock
    private CarritoRepository carritoRepository;

    @InjectMocks
    private CarritoService carritoService;

    private Carrito carrito;
    private Proveedor proveedor;

    @BeforeEach
    void setUp() {
        // Configuramos un Proveedor de ejemplo
        proveedor = new Proveedor();
        proveedor.setId(1);
        proveedor.setName("Proveedor Test");
        proveedor.setEmail("test@provider.com");
        proveedor.setTelefono("123456789");
        proveedor.setDireccion("Calle Falsa 123");

        // Configuramos un Carrito de ejemplo
        carrito = new Carrito();
        carrito.setId(1);
        carrito.setPrecioTotal(100.0);
        carrito.setProveedor(proveedor);
        carrito.setDiaEntrega(LocalDate.now());
    }

    // ----- Tests para el método save() -----

    @Test
    void testSaveCarrito_Success() {
        // Arrange
        when(carritoRepository.save(carrito)).thenReturn(carrito);
        // Act
        Carrito saved = carritoService.save(carrito);
        // Assert
        assertNotNull(saved);
        assertEquals(1, saved.getId());
        assertEquals(100.0, saved.getPrecioTotal());
        verify(carritoRepository, times(1)).save(carrito);
    }

    @SuppressWarnings("null")
    @Test
    void testSaveCarrito_NullCarrito() {
        // Arrange
        when(carritoRepository.save(null)).thenReturn(null);
        // Act
        Carrito saved = carritoService.save(null);
        // Assert
        assertNull(saved);
        verify(carritoRepository, times(1)).save(null);
    }

    @Test
    void testSaveCarrito_ExceptionThrown() {
        // Arrange
        when(carritoRepository.save(carrito)).thenThrow(new RuntimeException("Database error"));
        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> carritoService.save(carrito));
        assertEquals("Database error", ex.getMessage());
        verify(carritoRepository, times(1)).save(carrito);
    }

    // ----- Tests nuevos para el método save() -----

    @Test
    void testSaveCarrito_InvalidPrecioTotal() {
        // Arrange: Establecemos un precio negativo, simulando que el repositorio lanza excepción
        carrito.setPrecioTotal(-50.0);
        when(carritoRepository.save(carrito)).thenThrow(new IllegalArgumentException("Precio total debe ser positivo"));
        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> carritoService.save(carrito));
        assertEquals("Precio total debe ser positivo", ex.getMessage());
        verify(carritoRepository, times(1)).save(carrito);
    }

    @Test
    void testSaveCarrito_NullDiaEntrega() {
        // Arrange: Se establece la fecha de entrega en null y se simula excepción del repositorio
        carrito.setDiaEntrega(null);
        when(carritoRepository.save(carrito)).thenThrow(new IllegalArgumentException("Dia de entrega no puede ser null"));
        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> carritoService.save(carrito));
        assertEquals("Dia de entrega no puede ser null", ex.getMessage());
        verify(carritoRepository, times(1)).save(carrito);
    }

    // ----- Tests para el método deleteById() -----

    @Test
    void testDeleteById_Success() {
        // Arrange
        doNothing().when(carritoRepository).deleteById(1);
        // Act
        carritoService.deleteById(1);
        // Assert
        verify(carritoRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteById_NotFound() {
        // Arrange: Simulamos que al borrar un id inexistente se lanza excepción
        doThrow(new RuntimeException("Carrito not found")).when(carritoRepository).deleteById(999);
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> carritoService.deleteById(999));
        assertEquals("Carrito not found", exception.getMessage());
        verify(carritoRepository, times(1)).deleteById(999);
    }

    @SuppressWarnings("null")
    @Test
    void testDeleteById_NullId() {
        // Arrange: Simulamos que se pasa null
        doThrow(new IllegalArgumentException("ID cannot be null")).when(carritoRepository).deleteById(null);
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> carritoService.deleteById(null));
        assertEquals("ID cannot be null", exception.getMessage());
        verify(carritoRepository, times(1)).deleteById(null);
    }

    // ----- Tests para el método findById() -----

    @Test
    void testFindById_Found() {
        // Arrange
        when(carritoRepository.findById(1)).thenReturn(java.util.Optional.of(carrito));
        // Act
        Carrito found = carritoService.findById(1);
        // Assert
        assertNotNull(found);
        assertEquals(1, found.getId());
        verify(carritoRepository, times(1)).findById(1);
    }

    @Test
    void testFindById_NotFound() {
        // Arrange
        when(carritoRepository.findById(2)).thenReturn(java.util.Optional.empty());
        // Act
        Carrito found = carritoService.findById(2);
        // Assert
        assertNull(found);
        verify(carritoRepository, times(1)).findById(2);
    }

    @SuppressWarnings("null")
    @Test
    void testFindById_Null() {
        // Arrange
        when(carritoRepository.findById(null)).thenThrow(new IllegalArgumentException("ID cannot be null"));
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> carritoService.findById(null));
        assertEquals("ID cannot be null", exception.getMessage());
        verify(carritoRepository, times(1)).findById(null);
    }

    // ----- Tests para el método findAll() -----

    @Test
    void testFindAll_CarritosFound() {
        // Arrange
        Carrito carrito2 = new Carrito();
        carrito2.setId(2);
        carrito2.setPrecioTotal(200.0);
        carrito2.setProveedor(proveedor);
        carrito2.setDiaEntrega(LocalDate.now().plusDays(1));

        List<Carrito> carritoList = Arrays.asList(carrito, carrito2);
        when(carritoRepository.findAll()).thenReturn(carritoList);
        // Act
        List<Carrito> result = carritoService.findAll();
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(2, result.get(1).getId());
        verify(carritoRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_EmptyList() {
        // Arrange
        when(carritoRepository.findAll()).thenReturn(Collections.emptyList());
        // Act
        List<Carrito> result = carritoService.findAll();
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(carritoRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_NullIterable() {
        // Arrange: Simulamos que el repositorio retorna null
        when(carritoRepository.findAll()).thenReturn(null);
        // Act & Assert
        assertThrows(NullPointerException.class, () -> carritoService.findAll());
        verify(carritoRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_WithCustomIterable() {
        // Arrange: Utilizamos un Iterable personalizado
        Iterable<Carrito> iterable = new Iterable<Carrito>() {
            @Override
            public Iterator<Carrito> iterator() {
                return Arrays.asList(carrito).iterator();
            }
        };
        when(carritoRepository.findAll()).thenReturn(iterable);
        // Act
        List<Carrito> result = carritoService.findAll();
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(carritoRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_WithNullElements() {
        // Arrange: Lista con un elemento null junto a un carrito válido
        List<Carrito> carritoList = Arrays.asList(carrito, null);
        when(carritoRepository.findAll()).thenReturn(carritoList);
        // Act
        List<Carrito> result = carritoService.findAll();
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertNotNull(result.get(0));
        assertNull(result.get(1));
        verify(carritoRepository, times(1)).findAll();
    }

    // ----- Tests para el método findByProveedorId() -----

    @Test
    void testFindByProveedorId_Found() {
        // Arrange
        List<Carrito> carritoList = Collections.singletonList(carrito);
        when(carritoRepository.findByProveedorId(proveedor.getId())).thenReturn(carritoList);
        // Act
        List<Carrito> result = carritoService.findByProveedorId(proveedor.getId());
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(carritoRepository, times(1)).findByProveedorId(proveedor.getId());
    }

    @Test
    void testFindByProveedorId_EmptyList() {
        // Arrange
        when(carritoRepository.findByProveedorId(999)).thenReturn(Collections.emptyList());
        // Act
        List<Carrito> result = carritoService.findByProveedorId(999);
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(carritoRepository, times(1)).findByProveedorId(999);
    }

    @Test
    void testFindByProveedorId_NullId() {
        // Arrange
        when(carritoRepository.findByProveedorId(null))
            .thenThrow(new IllegalArgumentException("Proveedor ID cannot be null"));
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> carritoService.findByProveedorId(null));
        assertEquals("Proveedor ID cannot be null", exception.getMessage());
        verify(carritoRepository, times(1)).findByProveedorId(null);
    }

    @Test
    void testFindByProveedorId_Exception() {
        // Arrange
        when(carritoRepository.findByProveedorId(any()))
            .thenThrow(new RuntimeException("Error in repository"));
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> carritoService.findByProveedorId(1));
        assertEquals("Error in repository", exception.getMessage());
        verify(carritoRepository, times(1)).findByProveedorId(1);
    }

    @Test
    void testFindByProveedorId_NegativeId() {
        // Arrange: Simulamos que se pasa un ID negativo
        when(carritoRepository.findByProveedorId(-1))
            .thenThrow(new IllegalArgumentException("Proveedor ID must be positive"));
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> carritoService.findByProveedorId(-1));
        assertEquals("Proveedor ID must be positive", exception.getMessage());
        verify(carritoRepository, times(1)).findByProveedorId(-1);
    }

    // ----- Tests para el método findByNegocioId() -----

    @Test
    void testFindByNegocioId_Found() {
        // Arrange
        List<Carrito> carritoList = Collections.singletonList(carrito);
        when(carritoRepository.findByNegocioId(10)).thenReturn(carritoList);
        // Act
        List<Carrito> result = carritoService.findByNegocioId(10);
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(carritoRepository, times(1)).findByNegocioId(10);
    }

    @Test
    void testFindByNegocioId_EmptyList() {
        // Arrange
        when(carritoRepository.findByNegocioId(20)).thenReturn(Collections.emptyList());
        // Act
        List<Carrito> result = carritoService.findByNegocioId(20);
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(carritoRepository, times(1)).findByNegocioId(20);
    }

    @Test
    void testFindByNegocioId_NullId() {
        // Arrange
        when(carritoRepository.findByNegocioId(null))
            .thenThrow(new IllegalArgumentException("Negocio ID cannot be null"));
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> carritoService.findByNegocioId(null));
        assertEquals("Negocio ID cannot be null", exception.getMessage());
        verify(carritoRepository, times(1)).findByNegocioId(null);
    }

    @Test
    void testFindByNegocioId_Exception() {
        // Arrange
        when(carritoRepository.findByNegocioId(any()))
            .thenThrow(new RuntimeException("Error in repository"));
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> carritoService.findByNegocioId(1));
        assertEquals("Error in repository", exception.getMessage());
        verify(carritoRepository, times(1)).findByNegocioId(1);
    }

    @Test
    void testFindByNegocioId_ZeroId() {
        // Arrange: Simulamos el caso donde se pasa un ID 0
        when(carritoRepository.findByNegocioId(0)).thenReturn(Collections.emptyList());
        // Act
        List<Carrito> result = carritoService.findByNegocioId(0);
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(carritoRepository, times(1)).findByNegocioId(0);
    }
}
