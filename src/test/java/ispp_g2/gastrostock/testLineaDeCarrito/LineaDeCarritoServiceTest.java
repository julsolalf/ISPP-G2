package ispp_g2.gastrostock.testLineaDeCarrito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import ispp_g2.gastrostock.carrito.Carrito;
import ispp_g2.gastrostock.lineaDeCarrito.LineaDeCarrito;
import ispp_g2.gastrostock.lineaDeCarrito.LineaDeCarritoRepository;
import ispp_g2.gastrostock.lineaDeCarrito.LineaDeCarritoService;
import ispp_g2.gastrostock.productoInventario.ProductoInventario;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class LineaDeCarritoServiceTest {

    @Mock
    private LineaDeCarritoRepository lineaDeCarritoRepository;

    @InjectMocks
    private LineaDeCarritoService lineaDeCarritoService;

    // Variables para usar en los tests
    private LineaDeCarrito lineaNormal;
    private LineaDeCarrito lineaCantidadGrande;
    private LineaDeCarrito lineaPrecioAlto;
    private LineaDeCarrito nuevaLinea;
    private List<LineaDeCarrito> lineasCarrito1;
    private List<LineaDeCarrito> lineasProducto1;

    @BeforeEach
    void setUp() {
        Carrito carrito1 = new Carrito();
        carrito1.setId(1);
        
        Carrito carrito2 = new Carrito();
        carrito2.setId(2);
        
        ProductoInventario producto1 = new ProductoInventario();
        producto1.setId(1);
        
        ProductoInventario producto2 = new ProductoInventario();
        producto2.setId(2);
        
        lineaNormal = new LineaDeCarrito();
        lineaNormal.setId(1);
        lineaNormal.setCantidad(3);
        lineaNormal.setPrecioLinea(7.5);
        lineaNormal.setCarrito(carrito1);
        lineaNormal.setProducto(producto1);
        
        lineaCantidadGrande = new LineaDeCarrito();
        lineaCantidadGrande.setId(2);
        lineaCantidadGrande.setCantidad(10);
        lineaCantidadGrande.setPrecioLinea(25.0);
        lineaCantidadGrande.setCarrito(carrito1);
        lineaCantidadGrande.setProducto(producto1);
        
        lineaPrecioAlto = new LineaDeCarrito();
        lineaPrecioAlto.setId(3);
        lineaPrecioAlto.setCantidad(4);
        lineaPrecioAlto.setPrecioLinea(20.0);
        lineaPrecioAlto.setCarrito(carrito2);
        lineaPrecioAlto.setProducto(producto2);
        
        nuevaLinea = new LineaDeCarrito();
        nuevaLinea.setCantidad(5);
        nuevaLinea.setPrecioLinea(12.5);
        nuevaLinea.setCarrito(carrito2);
        nuevaLinea.setProducto(producto1);
        
        lineasCarrito1 = Arrays.asList(lineaNormal, lineaCantidadGrande);
        lineasProducto1 = Arrays.asList(lineaNormal, lineaCantidadGrande, nuevaLinea);
    }


    @Test
    void testSave_Success() {
        when(lineaDeCarritoRepository.save(nuevaLinea)).thenReturn(nuevaLinea);
        
        LineaDeCarrito resultado = lineaDeCarritoService.save(nuevaLinea);
        
        assertNotNull(resultado);
        assertEquals(nuevaLinea.getCantidad(), resultado.getCantidad());
        assertEquals(nuevaLinea.getPrecioLinea(), resultado.getPrecioLinea());
        
        verify(lineaDeCarritoRepository).save(nuevaLinea);
    }

    @Test
    void testSave_Exception() {
        when(lineaDeCarritoRepository.save(any(LineaDeCarrito.class)))
                .thenThrow(new RuntimeException("Error al guardar"));
        
        assertThrows(RuntimeException.class, () -> {
            lineaDeCarritoService.save(nuevaLinea);
        });
        
        verify(lineaDeCarritoRepository).save(any(LineaDeCarrito.class));
    }

    @Test
    void testSave_NullLineaDeCarrito() {
        when(lineaDeCarritoRepository.save(null)).thenThrow(new IllegalArgumentException("LineaDeCarrito no puede ser null"));
        
        assertThrows(IllegalArgumentException.class, () -> {
            lineaDeCarritoService.save(null);
        });
    }

    // TESTS PARA delete()

    @Test
    void testDelete_Success() {
        doNothing().when(lineaDeCarritoRepository).delete(lineaNormal);
        
        lineaDeCarritoService.delete(1);
        
        verify(lineaDeCarritoRepository).deleteById(1);
    }

    @Test
    void testDelete_Exception() {
        doThrow(new RuntimeException("Error al eliminar")).when(lineaDeCarritoRepository).delete(lineaNormal);
        
        assertThrows(RuntimeException.class, () -> {
            lineaDeCarritoService.delete(1);
        });
        
        verify(lineaDeCarritoRepository).delete(lineaNormal);
    }

    @Test
    void testDelete_NullLineaDeCarrito() {
        doThrow(new IllegalArgumentException("LineaDeCarrito no puede ser null"))
            .when(lineaDeCarritoRepository).delete(null);
        
        assertThrows(IllegalArgumentException.class, () -> {
            lineaDeCarritoService.delete(null);
        });
    }


    @Test
    void testFindLineaDeCarritoById_Found() {
        when(lineaDeCarritoRepository.findById(1)).thenReturn(Optional.of(lineaNormal));
        
        LineaDeCarrito resultado = lineaDeCarritoService.findLineaDeCarritoById(1);
        
        assertNotNull(resultado);
        assertEquals(lineaNormal.getId(), resultado.getId());
        assertEquals(lineaNormal.getCantidad(), resultado.getCantidad());
        
        verify(lineaDeCarritoRepository).findById(1);
    }

    @Test
    void testFindLineaDeCarritoById_NotFound() {
        when(lineaDeCarritoRepository.findById(999)).thenReturn(Optional.empty());
        
        LineaDeCarrito resultado = lineaDeCarritoService.findLineaDeCarritoById(999);
        
        assertNull(resultado);
        
        verify(lineaDeCarritoRepository).findById(999);
    }

    @Test
    void testFindLineaDeCarritoById_NullId() {
        when(lineaDeCarritoRepository.findById(null)).thenReturn(Optional.empty());
        
        LineaDeCarrito resultado = lineaDeCarritoService.findLineaDeCarritoById(null);
        
        assertNull(resultado);
        
        verify(lineaDeCarritoRepository).findById(null);
    }

    @Test
    void testFindLineaDeCarritoById_Exception() {
        when(lineaDeCarritoRepository.findById(anyInt()))
                .thenThrow(new RuntimeException("Error al buscar por ID"));
        
        assertThrows(RuntimeException.class, () -> {
            lineaDeCarritoService.findLineaDeCarritoById(1);
        });
        
        verify(lineaDeCarritoRepository).findById(1);
    }


    @Test
    void testFindAll_ExistenLineas() {
        List<LineaDeCarrito> lineas = Arrays.asList(lineaNormal, lineaCantidadGrande, lineaPrecioAlto);
        when(lineaDeCarritoRepository.findAll()).thenReturn(lineas);
        
        List<LineaDeCarrito> resultado = lineaDeCarritoService.findAll();
        
        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        assertTrue(resultado.contains(lineaNormal));
        assertTrue(resultado.contains(lineaCantidadGrande));
        assertTrue(resultado.contains(lineaPrecioAlto));
        
        verify(lineaDeCarritoRepository).findAll();
    }

    @Test
    void testFindAll_SinLineas() {
        when(lineaDeCarritoRepository.findAll()).thenReturn(Collections.emptyList());
        
        List<LineaDeCarrito> resultado = lineaDeCarritoService.findAll();
        
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        
        verify(lineaDeCarritoRepository).findAll();
    }

    @Test
    void testFindAll_Exception() {
        when(lineaDeCarritoRepository.findAll())
                .thenThrow(new RuntimeException("Error al buscar todas las líneas"));
        
        assertThrows(RuntimeException.class, () -> {
            lineaDeCarritoService.findAll();
        });
        
        verify(lineaDeCarritoRepository).findAll();
    }


    @Test
    void testFindLineaDeCarritoByCarritoId_Found() {
        when(lineaDeCarritoRepository.findLineaDeCarritoByCarritoId(1)).thenReturn(lineasCarrito1);
        
        List<LineaDeCarrito> resultado = lineaDeCarritoService.findLineaDeCarritoByCarritoId(1);
        
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(lineaNormal, resultado.get(0));
        assertEquals(lineaCantidadGrande, resultado.get(1));
        
        verify(lineaDeCarritoRepository).findLineaDeCarritoByCarritoId(1);
    }

    @Test
    void testFindLineaDeCarritoByCarritoId_NotFound() {
        when(lineaDeCarritoRepository.findLineaDeCarritoByCarritoId(999)).thenReturn(Collections.emptyList());
        
        List<LineaDeCarrito> resultado = lineaDeCarritoService.findLineaDeCarritoByCarritoId(999);
        
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        
        verify(lineaDeCarritoRepository).findLineaDeCarritoByCarritoId(999);
    }

    @Test
    void testFindLineaDeCarritoByCarritoId_NullId() {
        when(lineaDeCarritoRepository.findLineaDeCarritoByCarritoId(null)).thenReturn(Collections.emptyList());
        
        List<LineaDeCarrito> resultado = lineaDeCarritoService.findLineaDeCarritoByCarritoId(null);
        
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        

        verify(lineaDeCarritoRepository).findLineaDeCarritoByCarritoId(null);
    }

    @Test
    void testFindLineaDeCarritoByCarritoId_Exception() {

        when(lineaDeCarritoRepository.findLineaDeCarritoByCarritoId(anyInt()))
                .thenThrow(new RuntimeException("Error al buscar por carrito"));

        assertThrows(RuntimeException.class, () -> {
            lineaDeCarritoService.findLineaDeCarritoByCarritoId(1);
        });
        
        verify(lineaDeCarritoRepository).findLineaDeCarritoByCarritoId(1);
    }


    @Test
    void testFindLineaDeCarritoByCarritoIdAndProductoId_Found() {
        when(lineaDeCarritoRepository.findLineaDeCarritoByCarritoIdAndProductoId(1, 1))
                .thenReturn(Arrays.asList(lineaNormal, lineaCantidadGrande));
        
        List<LineaDeCarrito> resultado = lineaDeCarritoService.findLineaDeCarritoByCarritoIdAndProductoId(1, 1);
        
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(lineaNormal, resultado.get(0));
        assertEquals(lineaCantidadGrande, resultado.get(1));
        

        verify(lineaDeCarritoRepository).findLineaDeCarritoByCarritoIdAndProductoId(1, 1);
    }

    @Test
    void testFindLineaDeCarritoByCarritoIdAndProductoId_NotFound() {
        when(lineaDeCarritoRepository.findLineaDeCarritoByCarritoIdAndProductoId(1, 999))
                .thenReturn(Collections.emptyList());
        
        List<LineaDeCarrito> resultado = lineaDeCarritoService.findLineaDeCarritoByCarritoIdAndProductoId(1, 999);
        
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        
        verify(lineaDeCarritoRepository).findLineaDeCarritoByCarritoIdAndProductoId(1, 999);
    }

    @Test
    void testFindLineaDeCarritoByCarritoIdAndProductoId_NullIds() {
        when(lineaDeCarritoRepository.findLineaDeCarritoByCarritoIdAndProductoId(null, null))
                .thenReturn(Collections.emptyList());
        
        List<LineaDeCarrito> resultado = lineaDeCarritoService.findLineaDeCarritoByCarritoIdAndProductoId(null, null);
        
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        
        verify(lineaDeCarritoRepository).findLineaDeCarritoByCarritoIdAndProductoId(null, null);
    }

    @Test
    void testFindLineaDeCarritoByCarritoIdAndProductoId_Exception() {
        when(lineaDeCarritoRepository.findLineaDeCarritoByCarritoIdAndProductoId(anyInt(), anyInt()))
                .thenThrow(new RuntimeException("Error al buscar por carrito y producto"));
        
        assertThrows(RuntimeException.class, () -> {
            lineaDeCarritoService.findLineaDeCarritoByCarritoIdAndProductoId(1, 1);
        });
        
        verify(lineaDeCarritoRepository).findLineaDeCarritoByCarritoIdAndProductoId(1, 1);
    }
}