package ispp_g2.gastrostock.testProductoInventario;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import ispp_g2.gastrostock.categorias.Categoria;
import ispp_g2.gastrostock.productoInventario.ProductoInventario;
import ispp_g2.gastrostock.productoInventario.ProductoInventarioRepository;
import ispp_g2.gastrostock.productoInventario.ProductoInventarioService;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ProductoInventarioServiceTest {

    @Mock
    private ProductoInventarioRepository productoInventarioRepository;

    @InjectMocks
    private ProductoInventarioService productoInventarioService;

    private ProductoInventario sampleProduct;
    private Categoria categoriaBebidas;

    @BeforeEach
    void setUp() {
        categoriaBebidas = new Categoria();
        categoriaBebidas.setId(1);
        categoriaBebidas.setName("Bebidas");

        sampleProduct = new ProductoInventario();
        sampleProduct.setName("Cerveza");
        sampleProduct.setCategoria(categoriaBebidas);
        sampleProduct.setPrecioCompra(10.5);
        sampleProduct.setCantidadDeseada(100);
        sampleProduct.setCantidadAviso(10);
    }

    @Test
    void testGetById_Found() {
        when(productoInventarioRepository.findById("1")).thenReturn(Optional.of(sampleProduct));

        ProductoInventario result = productoInventarioService.getById("1");
        assertNotNull(result);
        assertEquals("Cerveza", result.getName());
    }

    @Test
    void testGetById_NotFound() {
        when(productoInventarioRepository.findById("99")).thenReturn(Optional.empty());

        ProductoInventario result = productoInventarioService.getById("99");
        assertNull(result);
    }

    @Test
    void testGetProductosInventario() {
        when(productoInventarioRepository.findAll()).thenReturn(List.of(sampleProduct));

        List<ProductoInventario> result = productoInventarioService.getProductosInventario();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Cerveza", result.get(0).getName());
    }

    @Test
    void testGetByName_Found() {
        when(productoInventarioRepository.findByName("Cerveza")).thenReturn(sampleProduct);

        ProductoInventario result = productoInventarioService.getByName("Cerveza");
        assertNotNull(result);
        assertEquals("Cerveza", result.getName());
    }

    @Test
    void testGetByName_NotFound() {
        when(productoInventarioRepository.findByName("Agua")).thenReturn(null);

        ProductoInventario result = productoInventarioService.getByName("Agua");
        assertNull(result);
    }

    @Test
    void testGetProductoInventarioByCategoriaName() {
        when(productoInventarioRepository.findByCategoriaName("Bebidas")).thenReturn(List.of(sampleProduct));

        List<ProductoInventario> result = productoInventarioService.getProductoInventarioByCategoriaName("Bebidas");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Cerveza", result.get(0).getName());
    }

    @Test
    void testGetProductoInventarioByPrecioCompra() {
        when(productoInventarioRepository.findByPrecioCompra(10.5)).thenReturn(List.of(sampleProduct));

        List<ProductoInventario> result = productoInventarioService.getProductoInventarioByPrecioCompra(10.5);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Cerveza", result.get(0).getName());
    }

    @Test
    void testGetProductoInventarioByCantidadDeseada() {
        when(productoInventarioRepository.findByCantidadDeseada(100)).thenReturn(List.of(sampleProduct));

        List<ProductoInventario> result = productoInventarioService.getProductoInventarioByCantidadDeseada(100);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Cerveza", result.get(0).getName());
    }

    @Test
    void testGetProductoInventarioByCantidadAviso() {
        when(productoInventarioRepository.findByCantidadAviso(10)).thenReturn(List.of(sampleProduct));

        List<ProductoInventario> result = productoInventarioService.getProductoInventarioByCantidadAviso(10);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Cerveza", result.get(0).getName());
    }

    @Test
    void testSave() {
        when(productoInventarioRepository.save(any(ProductoInventario.class))).thenReturn(sampleProduct);

        ProductoInventario result = productoInventarioService.save(sampleProduct);
        assertNotNull(result);
        assertEquals("Cerveza", result.getName());
    }

    @Test
    void testDelete() {
        doNothing().when(productoInventarioRepository).deleteById("1");

        productoInventarioService.delete("1");

        verify(productoInventarioRepository, times(1)).deleteById("1");
    }

    @Test
    void testGetProductoInventario_InvalidId() {
        when(productoInventarioRepository.findById("invalidId")).thenReturn(Optional.empty());

        ProductoInventario result = productoInventarioService.getById("invalidId");
        assertNull(result);  
    }

    @Test
    void testDeleteProductoInventario_InvalidId() {
        productoInventarioService.delete("invalidId");
        verify(productoInventarioRepository, times(1)).deleteById("invalidId");
    }
    

    @Test
    void testSave_InvalidProduct() {
        ProductoInventario invalidProduct = new ProductoInventario();
        invalidProduct.setId(2);
        invalidProduct.setName(""); 
    
        when(productoInventarioRepository.save(invalidProduct))
                .thenThrow(new IllegalArgumentException("Producto inválido"));
    
        assertThrows(IllegalArgumentException.class, () -> productoInventarioService.save(invalidProduct));
    }
}

