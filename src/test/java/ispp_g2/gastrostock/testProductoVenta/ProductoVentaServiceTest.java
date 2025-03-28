package ispp_g2.gastrostock.testProductoVenta;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import ispp_g2.gastrostock.categorias.Categoria;
import ispp_g2.gastrostock.productoVenta.ProductoVenta;
import ispp_g2.gastrostock.productoVenta.ProductoVentaRepository;
import ispp_g2.gastrostock.productoVenta.ProductoVentaService;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ProductoVentaServiceTest {

    @Mock
    private ProductoVentaRepository productoVentaRepository;

    @InjectMocks
    private ProductoVentaService productoVentaService;

    private ProductoVenta sampleProduct;
    private Categoria categoriaBebidas;

    @BeforeEach
    void setUp() {
        categoriaBebidas = new Categoria();
        categoriaBebidas.setId(1);
        categoriaBebidas.setName("Bebidas");

        sampleProduct = new ProductoVenta();

        sampleProduct.setName("Cerveza");
        sampleProduct.setCategoria(categoriaBebidas);
        sampleProduct.setPrecioVenta(10.5);
    }

    @Test
    void testGetById_Found() {
        when(productoVentaRepository.findById(1)).thenReturn(Optional.of(sampleProduct));

        ProductoVenta result = productoVentaService.getById(1);
        assertNotNull(result);
        assertEquals("Cerveza", result.getName());
    }

    @Test
    void testGetById_NotFound() {
        when(productoVentaRepository.findById(99)).thenReturn(Optional.empty());

        ProductoVenta result = productoVentaService.getById(99);
        assertNull(result);
    }

    @Test
    void testGetById_InvalidId() {
        ProductoVenta result = productoVentaService.getById(999999);
        assertNull(result);
    }

    @Test
    void testGetProductosVenta() {
        when(productoVentaRepository.findAll()).thenReturn(List.of(sampleProduct));

        List<ProductoVenta> result = productoVentaService.getProductosVenta();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Cerveza", result.get(0).getName());
    }

    @Test
    void testGetByNombre_Found() {
        when(productoVentaRepository.findProductoVentaByNombre("Cerveza")).thenReturn(List.of(sampleProduct));

        List<ProductoVenta> result = productoVentaService.getProductosVentaByNombre("Cerveza");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Cerveza", result.get(0).getName());
    }

    @Test
    void testGetByNombre_NotFound() {
        when(productoVentaRepository.findProductoVentaByNombre("Agua")).thenReturn(List.of());

        List<ProductoVenta> result = productoVentaService.getProductosVentaByNombre("Agua");
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testGetProductosVentaByCategoria() {
        when(productoVentaRepository.findProductoVentaByCategoriaVenta("Bebidas")).thenReturn(List.of(sampleProduct));

        List<ProductoVenta> result = productoVentaService.getProductosVentaByCategoriaVenta("Bebidas");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Cerveza", result.get(0).getName());
    }

    @Test
    void testGetProductosVentaByPrecioVenta() {
        when(productoVentaRepository.findProductoVentaByPrecioVenta(10.5)).thenReturn(List.of(sampleProduct));

        List<ProductoVenta> result = productoVentaService.getProductosVentaByPrecioVenta(10.5);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Cerveza", result.get(0).getName());
    }

    @Test
    void testGetProductosVentaByCategoriaAndPrecio() {
        when(productoVentaRepository.findProductoVentaByCategoriaVentaAndPrecioVenta("Bebidas", 10.5)).thenReturn(List.of(sampleProduct));

        List<ProductoVenta> result = productoVentaService.getProductosVentaByCategoriaVentaAndPrecioVenta("Bebidas", 10.5);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Cerveza", result.get(0).getName());
    }

    @Test
    void testSave() {
        when(productoVentaRepository.save(sampleProduct)).thenReturn(sampleProduct);

        ProductoVenta result = productoVentaService.save(sampleProduct);
        assertNotNull(result);
        assertEquals("Cerveza", result.getName());
    }

    @Test
    void testSave_InvalidProduct() {
        ProductoVenta invalidProduct = new ProductoVenta();
        invalidProduct.setName("");  // Producto sin nombre

        when(productoVentaRepository.save(invalidProduct))
                .thenThrow(new IllegalArgumentException("Producto inválido"));

        assertThrows(IllegalArgumentException.class, () -> productoVentaService.save(invalidProduct));
    }

    @Test
    void testDelete() {
        doNothing().when(productoVentaRepository).deleteById(1);

        productoVentaService.delete(1);

        verify(productoVentaRepository, times(1)).deleteById(1);
    }

    @Test
    void testDelete_InvalidId() {
        doNothing().when(productoVentaRepository).deleteById(999999);

        productoVentaService.delete(999999);

        verify(productoVentaRepository, times(1)).deleteById(999999);
    }

    @Test
    void testGetProductosVentaByCategoria_NotFound() {
        when(productoVentaRepository.findProductoVentaByCategoriaVenta("Comidas")).thenReturn(List.of());

        List<ProductoVenta> result = productoVentaService.getProductosVentaByCategoriaVenta("Comidas");
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testGetProductosVentaByPrecioVenta_NotFound() {
        when(productoVentaRepository.findProductoVentaByPrecioVenta(99.99)).thenReturn(List.of());

        List<ProductoVenta> result = productoVentaService.getProductosVentaByPrecioVenta(99.99);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testSaveWithNullProduct() {
        ProductoVenta result = productoVentaService.save(null);
        assertNull(result, "El producto no debería guardarse si es nulo");
    }

    @Test
    void testGetByIdWithNullId() {
        ProductoVenta result = productoVentaService.getById(null);
        assertNull(result, "El producto no debería existir cuando el ID es nulo");
    }

    @SuppressWarnings("null")
    @Test
    void testDeleteWithNullId() {
        productoVentaService.delete(null);
        verify(productoVentaRepository, times(1)).deleteById(null);
    }

    @Test
    void testGetProductosVentaByNombreWithNullValue() {
        List<ProductoVenta> result = productoVentaService.getProductosVentaByNombre(null);
        assertNotNull(result, "La lista no debe ser nula");
        assertEquals(0, result.size(), "La lista debería estar vacía");
    }

    @Test
    void testSaveWithEmptyName() {
        ProductoVenta invalidProduct = new ProductoVenta();
        invalidProduct.setName("");  

        when(productoVentaRepository.save(invalidProduct)).thenThrow(new IllegalArgumentException("El nombre no puede estar vacío"));

        assertThrows(IllegalArgumentException.class, () -> productoVentaService.save(invalidProduct));
    }
}
