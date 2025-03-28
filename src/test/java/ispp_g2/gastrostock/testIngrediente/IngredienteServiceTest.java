package ispp_g2.gastrostock.testIngrediente;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;

import ispp_g2.gastrostock.categorias.Categoria;
import ispp_g2.gastrostock.categorias.Pertenece;
import ispp_g2.gastrostock.ingrediente.Ingrediente;
import ispp_g2.gastrostock.ingrediente.IngredienteRepository;
import ispp_g2.gastrostock.ingrediente.IngredienteService;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.productoInventario.ProductoInventario;
import ispp_g2.gastrostock.productoVenta.ProductoVenta;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class IngredienteServiceTest {

    @Mock
    private IngredienteRepository ingredienteRepository;

    @InjectMocks
    private IngredienteService ingredienteService;

    @Captor
    private ArgumentCaptor<Ingrediente> ingredienteCaptor;

    private Ingrediente ingrediente1, ingrediente2, ingrediente3;
    private ProductoInventario productoInventario1, productoInventario2;
    private ProductoVenta productoVenta1, productoVenta2;
    private List<Ingrediente> ingredientes;
    private Negocio negocio;
    private Categoria categoriaInventario, categoriaVenta;

    @BeforeEach
    void setUp() {
        // Crear negocio
        negocio = new Negocio();
        negocio.setId(1);
        negocio.setName("Restaurante La Tasca");
        negocio.setDireccion("Calle Principal 123");
        negocio.setCiudad("Sevilla");
        negocio.setPais("España");
        negocio.setCodigoPostal("41001");
        negocio.setTokenNegocio(12345);

        // Crear categorías
        categoriaInventario = new Categoria();
        categoriaInventario.setId(1);
        categoriaInventario.setName("Alimentos");
        categoriaInventario.setNegocio(negocio);
        categoriaInventario.setPertenece(Pertenece.INVENTARIO);

        categoriaVenta = new Categoria();
        categoriaVenta.setId(2);
        categoriaVenta.setName("Platos");
        categoriaVenta.setNegocio(negocio);
        categoriaVenta.setPertenece(Pertenece.VENTA);

        // Crear productos de inventario
        productoInventario1 = new ProductoInventario();
        productoInventario1.setId(1);
        productoInventario1.setName("Patatas");
        productoInventario1.setCantidadDeseada(10);
        productoInventario1.setCantidadAviso(2);
        productoInventario1.setPrecioCompra(1.5);
        productoInventario1.setCantidadDeseada(20);
        productoInventario1.setCantidadAviso(5);
        productoInventario1.setCategoria(categoriaInventario);

        productoInventario2 = new ProductoInventario();
        productoInventario2.setId(2);
        productoInventario2.setName("Tomates");
        productoInventario2.setCantidadDeseada(20);
        productoInventario2.setCantidadAviso(5);
        productoInventario2.setPrecioCompra(0.8);
        productoInventario2.setCantidadDeseada(30);
        productoInventario2.setCantidadAviso(8);
        productoInventario2.setCategoria(categoriaInventario);

        // Crear productos de venta
        productoVenta1 = new ProductoVenta();
        productoVenta1.setId(1);
        productoVenta1.setName("Patatas Fritas");
        productoVenta1.setPrecioVenta(3.50);
        productoVenta1.setCategoria(categoriaVenta);

        productoVenta2 = new ProductoVenta();
        productoVenta2.setId(2);
        productoVenta2.setName("Ensalada");
        productoVenta2.setPrecioVenta(5.00);
        productoVenta2.setCategoria(categoriaVenta);

        // Crear ingredientes
        ingrediente1 = new Ingrediente();
        ingrediente1.setId(1);
        ingrediente1.setCantidad(2);
        ingrediente1.setProductoInventario(productoInventario1);
        ingrediente1.setProductoVenta(productoVenta1);

        ingrediente2 = new Ingrediente();
        ingrediente2.setId(2);
        ingrediente2.setCantidad(3);
        ingrediente2.setProductoInventario(productoInventario2);
        ingrediente2.setProductoVenta(productoVenta1);

        ingrediente3 = new Ingrediente();
        ingrediente3.setId(3);
        ingrediente3.setCantidad(2);
        ingrediente3.setProductoInventario(productoInventario2);
        ingrediente3.setProductoVenta(productoVenta2);

        // Lista de ingredientes para tests
        ingredientes = new ArrayList<>();
        ingredientes.add(ingrediente1);
        ingredientes.add(ingrediente2);
        ingredientes.add(ingrediente3);
    }

    // TESTS PARA getById()

    @Test
    void testGetById_Success() {
        // Arrange
        when(ingredienteRepository.findById(1)).thenReturn(Optional.of(ingrediente1));
        
        // Act
        Ingrediente result = ingredienteService.getById(1);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(2, result.getCantidad());
        assertEquals(productoInventario1.getId(), result.getProductoInventario().getId());
        assertEquals(productoVenta1.getId(), result.getProductoVenta().getId());
        
        verify(ingredienteRepository).findById(1);
    }
    
    @Test
    void testGetById_NotFound() {
        // Arrange
        when(ingredienteRepository.findById(999)).thenReturn(Optional.empty());
        
        // Act
        Ingrediente result = ingredienteService.getById(999);
        
        // Assert
        assertNull(result);
        
        verify(ingredienteRepository).findById(999);
    }
    
    @Test
    void testGetById_NullId() {
        // Arrange
        when(ingredienteRepository.findById(null)).thenThrow(new IllegalArgumentException("ID cannot be null"));
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            ingredienteService.getById(null);
        });
        
        verify(ingredienteRepository).findById(null);
    }

    // TESTS PARA getIngredientes()
    
    @Test
    void testGetIngredientes_Success() {
        // Arrange
        when(ingredienteRepository.findAll()).thenReturn(ingredientes);
        
        // Act
        List<Ingrediente> result = ingredienteService.getIngredientes();
        
        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(2, result.get(1).getId());
        assertEquals(3, result.get(2).getId());
        
        verify(ingredienteRepository).findAll();
    }
    
    @Test
    void testGetIngredientes_EmptyList() {
        // Arrange
        when(ingredienteRepository.findAll()).thenReturn(new ArrayList<>());
        
        // Act
        List<Ingrediente> result = ingredienteService.getIngredientes();
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        verify(ingredienteRepository).findAll();
    }
    
    @Test
    void testGetIngredientes_RepositoryException() {
        // Arrange
        when(ingredienteRepository.findAll()).thenThrow(new DataAccessException("Database error") {});
        
        // Act & Assert
        assertThrows(DataAccessException.class, () -> {
            ingredienteService.getIngredientes();
        });
        
        verify(ingredienteRepository).findAll();
    }

    // TESTS PARA getIngredientesByCantidad()
    
    @Test
    void testGetIngredientesByCantidad_Success() {
        // Arrange
        List<Ingrediente> ingredientesConCantidad2 = new ArrayList<>();
        ingredientesConCantidad2.add(ingrediente1);
        ingredientesConCantidad2.add(ingrediente3);
        
        when(ingredienteRepository.findByCantidad(2)).thenReturn(ingredientesConCantidad2);
        
        // Act
        List<Ingrediente> result = ingredienteService.getIngredientesByCantidad(2);
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(3, result.get(1).getId());
        
        verify(ingredienteRepository).findByCantidad(2);
    }
    
    @Test
    void testGetIngredientesByCantidad_NotFound() {
        // Arrange
        when(ingredienteRepository.findByCantidad(99)).thenReturn(new ArrayList<>());
        
        // Act
        List<Ingrediente> result = ingredienteService.getIngredientesByCantidad(99);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        verify(ingredienteRepository).findByCantidad(99);
    }
    
    @Test
    void testGetIngredientesByCantidad_NullCantidad() {
        // Arrange
        when(ingredienteRepository.findByCantidad(null)).thenThrow(new IllegalArgumentException("Cantidad cannot be null"));
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            ingredienteService.getIngredientesByCantidad(null);
        });
        
        verify(ingredienteRepository).findByCantidad(null);
    }
    
    @Test
    void testGetIngredientesByCantidad_NegativeCantidad() {
        // Arrange
        when(ingredienteRepository.findByCantidad(-1)).thenReturn(new ArrayList<>());
        
        // Act
        List<Ingrediente> result = ingredienteService.getIngredientesByCantidad(-1);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        verify(ingredienteRepository).findByCantidad(-1);
    }

    // TESTS PARA getIngredientesByProductoInventarioId()
    
    @Test
    void testGetIngredientesByProductoInventarioId_Success() {
        // Arrange
        List<Ingrediente> ingredientesConProducto2 = new ArrayList<>();
        ingredientesConProducto2.add(ingrediente2);
        ingredientesConProducto2.add(ingrediente3);
        
        when(ingredienteRepository.findByProductoInventarioId(2)).thenReturn(ingredientesConProducto2);
        
        // Act
        List<Ingrediente> result = ingredienteService.getIngredientesByProductoInventarioId(2);
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(2, result.get(0).getId());
        assertEquals(3, result.get(1).getId());
        
        verify(ingredienteRepository).findByProductoInventarioId(2);
    }
    
    @Test
    void testGetIngredientesByProductoInventarioId_NotFound() {
        // Arrange
        when(ingredienteRepository.findByProductoInventarioId(99)).thenReturn(new ArrayList<>());
        
        // Act
        List<Ingrediente> result = ingredienteService.getIngredientesByProductoInventarioId(99);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        verify(ingredienteRepository).findByProductoInventarioId(99);
    }
    
    @Test
    void testGetIngredientesByProductoInventarioId_NullId() {
        // Arrange
        when(ingredienteRepository.findByProductoInventarioId(null)).thenThrow(new IllegalArgumentException("ProductoInventario ID cannot be null"));
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            ingredienteService.getIngredientesByProductoInventarioId(null);
        });
        
        verify(ingredienteRepository).findByProductoInventarioId(null);
    }

    // TESTS PARA getIngredientesByProductoVentaId()
    
    @Test
    void testGetIngredientesByProductoVentaId_Success() {
        // Arrange
        List<Ingrediente> ingredientesConProductoVenta1 = new ArrayList<>();
        ingredientesConProductoVenta1.add(ingrediente1);
        ingredientesConProductoVenta1.add(ingrediente2);
        
        when(ingredienteRepository.findByProductoVentaId(1)).thenReturn(ingredientesConProductoVenta1);
        
        // Act
        List<Ingrediente> result = ingredienteService.getIngredientesByProductoVentaId(1);
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(2, result.get(1).getId());
        
        verify(ingredienteRepository).findByProductoVentaId(1);
    }
    
    @Test
    void testGetIngredientesByProductoVentaId_NotFound() {
        // Arrange
        when(ingredienteRepository.findByProductoVentaId(99)).thenReturn(new ArrayList<>());
        
        // Act
        List<Ingrediente> result = ingredienteService.getIngredientesByProductoVentaId(99);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        verify(ingredienteRepository).findByProductoVentaId(99);
    }
    
    @Test
    void testGetIngredientesByProductoVentaId_NullId() {
        // Arrange
        when(ingredienteRepository.findByProductoVentaId(null)).thenThrow(new IllegalArgumentException("ProductoVenta ID cannot be null"));
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            ingredienteService.getIngredientesByProductoVentaId(null);
        });
        
        verify(ingredienteRepository).findByProductoVentaId(null);
    }

    // TESTS PARA save()
    
    @Test
    void testSave_NewIngrediente() {
        // Arrange
        Ingrediente nuevoIngrediente = new Ingrediente();
        nuevoIngrediente.setCantidad(5);
        nuevoIngrediente.setProductoInventario(productoInventario1);
        nuevoIngrediente.setProductoVenta(productoVenta2);
        
        Ingrediente ingredienteGuardado = new Ingrediente();
        ingredienteGuardado.setId(4);
        ingredienteGuardado.setCantidad(5);
        ingredienteGuardado.setProductoInventario(productoInventario1);
        ingredienteGuardado.setProductoVenta(productoVenta2);
        
        when(ingredienteRepository.save(any(Ingrediente.class))).thenReturn(ingredienteGuardado);
        
        // Act
        Ingrediente result = ingredienteService.save(nuevoIngrediente);
        
        // Assert
        assertNotNull(result);
        assertEquals(4, result.getId());
        assertEquals(5, result.getCantidad());
        assertEquals(productoInventario1.getId(), result.getProductoInventario().getId());
        assertEquals(productoVenta2.getId(), result.getProductoVenta().getId());
        
        verify(ingredienteRepository).save(nuevoIngrediente);
    }
    
    @Test
    void testSave_UpdateIngrediente() {
        // Arrange
        ingrediente1.setCantidad(10);
        
        when(ingredienteRepository.save(any(Ingrediente.class))).thenReturn(ingrediente1);
        
        // Act
        Ingrediente result = ingredienteService.save(ingrediente1);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(10, result.getCantidad());
        
        verify(ingredienteRepository).save(ingrediente1);
    }
    
    @Test
    void testSave_EmptyIngrediente() {
        // Arrange
        Ingrediente ingredienteVacio = new Ingrediente();
        // Aquí se puede ajustar el comportamiento esperado según la implementación real
        when(ingredienteRepository.save(any(Ingrediente.class))).thenThrow(
            new DataIntegrityViolationException("Cannot save ingredient with null fields"));
        
        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> {
            ingredienteService.save(ingredienteVacio);
        });
        
        verify(ingredienteRepository).save(ingredienteVacio);
    }
    
    @Test
    void testSave_InvalidIngrediente() {
        // Arrange - Ingrediente sin ProductoInventario ni ProductoVenta
        Ingrediente ingredienteInvalido = new Ingrediente();
        ingredienteInvalido.setCantidad(5);
        
        when(ingredienteRepository.save(any(Ingrediente.class))).thenThrow(new DataAccessException("Invalid ingredient data") {});
        
        // Act & Assert
        assertThrows(DataAccessException.class, () -> {
            ingredienteService.save(ingredienteInvalido);
        });
        
        verify(ingredienteRepository).save(ingredienteInvalido);
    }

    // TESTS PARA deleteById()
    
    @Test
    void testDeleteById_Success() {
        // Arrange
        doNothing().when(ingredienteRepository).deleteById(1);
        
        // Act
        ingredienteService.deleteById(1);
        
        // Assert
        verify(ingredienteRepository).deleteById(1);
    }
    
    @Test
    void testDeleteById_NotFound() {
        // Arrange
        doThrow(new EmptyResultDataAccessException(1)).when(ingredienteRepository).deleteById(999);
        
        // Act & Assert
        assertThrows(EmptyResultDataAccessException.class, () -> {
            ingredienteService.deleteById(999);
        });
        
        verify(ingredienteRepository).deleteById(999);
    }
    
    @Test
    void testDeleteById_NullId() {
        // Arrange
        doThrow(new IllegalArgumentException("ID cannot be null")).when(ingredienteRepository).deleteById(null);
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            ingredienteService.deleteById(null);
        });
        
        verify(ingredienteRepository).deleteById(null);
    }
}