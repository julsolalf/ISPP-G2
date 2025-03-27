package ispp_g2.gastrostock.testProductoVenta;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import ispp_g2.gastrostock.categorias.Categoria;
import ispp_g2.gastrostock.categorias.CategoriaRepository;
import ispp_g2.gastrostock.categorias.Pertenece;
import ispp_g2.gastrostock.dueno.Dueno;
import ispp_g2.gastrostock.dueno.DuenoRepository;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioRepository;
import ispp_g2.gastrostock.productoVenta.ProductoVenta;
import ispp_g2.gastrostock.productoVenta.ProductoVentaRepository;

@DataJpaTest
@ActiveProfiles("test")
class ProductoVentaRepositoryTest {

    @Autowired
    private ProductoVentaRepository productoVentaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private DuenoRepository duenoRepository;

    @Autowired
    private NegocioRepository negocioRepository;


    private Categoria categoriaBebidas;
    private Categoria categoriaAlimentos;

    private ProductoVenta prod1;
    private ProductoVenta prod2;
    private ProductoVenta prod3;

    @BeforeEach
    void setUp() {

        Dueno dueno1 = new Dueno();
        dueno1.setFirstName("Juan");
        dueno1.setLastName("García");
        dueno1.setEmail("juan@example.com");
        dueno1.setNumTelefono("666111222");
        dueno1.setTokenDueno("TOKEN999");
        duenoRepository.save(dueno1);

        Negocio negocio1 = new Negocio();
        negocio1.setId(1);
        negocio1.setName("Restaurante La Tasca");
        negocio1.setDireccion("Calle Principal 123");
        negocio1.setCiudad("Sevilla");
        negocio1.setPais("España");
        negocio1.setCodigoPostal("41001");
        negocio1.setTokenNegocio(12345);
        negocio1.setDueno(dueno1);
        negocio1 = negocioRepository.save(negocio1);
   
        categoriaBebidas = new Categoria();
        categoriaBebidas.setName("Bebidas");
        categoriaBebidas.setNegocio(negocio1);
        categoriaBebidas.setPertenece(Pertenece.INVENTARIO);
        categoriaRepository.save(categoriaBebidas);
        
        categoriaAlimentos = new Categoria();
        categoriaAlimentos.setName("Alimentos");
        categoriaAlimentos.setNegocio(negocio1);
        categoriaAlimentos.setPertenece(Pertenece.INVENTARIO);
        categoriaRepository.save(categoriaAlimentos);
        
        prod1 = new ProductoVenta();
        prod1.setName("Cerveza");
        prod1.setCategoria(categoriaBebidas);
        prod1.setPrecioVenta(10.5);

        
        prod2 = new ProductoVenta();
        prod2.setName("Agua Mineral");
        prod2.setCategoria(categoriaBebidas);
        prod2.setPrecioVenta(5.0);

        
        prod3 = new ProductoVenta();
        prod3.setName("Pan");
        prod3.setCategoria(categoriaAlimentos);
        prod3.setPrecioVenta(2.0);

        

        productoVentaRepository.save(prod1);
        productoVentaRepository.save(prod2);
        productoVentaRepository.save(prod3);
    }

    @Test
    void testFindProductoVentaByNombre() {
        List<ProductoVenta> result = productoVentaRepository.findProductoVentaByNombre("Cerveza");

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getName()).isEqualTo("Cerveza");

        // Buscar un producto que no existe
        List<ProductoVenta> notFound = productoVentaRepository.findProductoVentaByNombre("NoExiste");
        assertThat(notFound).isEmpty();
    }

    @Test
    void testFindProductoVentaByCategoriaVenta() {
        List<ProductoVenta> result = productoVentaRepository.findProductoVentaByCategoriaVenta("Bebidas");

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(2);  // Cerveza y Vino

        // Buscar una categoría que no existe
        List<ProductoVenta> noFound = productoVentaRepository.findProductoVentaByCategoriaVenta("Comida");
        assertThat(noFound).isEmpty();
    }

    @Test
    void testFindProductoVentaByPrecioVenta() {
        List<ProductoVenta> result = productoVentaRepository.findProductoVentaByPrecioVenta(10.5);

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getPrecioVenta()).isEqualTo(10.5);
        assertThat(result.get(0).getName()).isEqualTo("Cerveza");

        // Caso donde el precio no existe
        List<ProductoVenta> noFound = productoVentaRepository.findProductoVentaByPrecioVenta(100.0);
        assertThat(noFound).isEmpty();
    }

    @Test
    void testFindProductoVentaByCategoriaVentaAndPrecioVenta() {
        List<ProductoVenta> result = productoVentaRepository.findProductoVentaByCategoriaVentaAndPrecioVenta("Bebidas", 10.5);


        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getName()).isEqualTo("Cerveza");

        // Buscar una combinación que no existe
        List<ProductoVenta> noFound = productoVentaRepository.findProductoVentaByCategoriaVentaAndPrecioVenta("Bebidas", 999.0);
        assertThat(noFound).isEmpty();
    }

    @Test
    void testSaveProductoVenta() {
        ProductoVenta nuevoProd = new ProductoVenta();
        nuevoProd.setName("Whisky");
        nuevoProd.setPrecioVenta(50.0);
        nuevoProd.setCategoria(categoriaBebidas);

        ProductoVenta savedProd = productoVentaRepository.save(nuevoProd);

        assertThat(savedProd).isNotNull();
        assertThat(savedProd.getId()).isEqualTo(nuevoProd.getId());

    
        List<ProductoVenta> found = productoVentaRepository.findProductoVentaByNombre("Whisky");
        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getName()).isEqualTo("Whisky");
    }

    @Test
    void testDeleteProductoVenta() {
        productoVentaRepository.delete(prod1);

        List<ProductoVenta> found = productoVentaRepository.findProductoVentaByNombre("Cerveza");
        assertThat(found).isEmpty();
    }

    @Test
    void testUpdateProductoVenta() {
        ProductoVenta producto = productoVentaRepository.findProductoVentaByNombre("Pan").get(0);
        producto.setPrecioVenta(3.0);
        productoVentaRepository.save(producto);

        ProductoVenta updated = productoVentaRepository.findProductoVentaByNombre("Pan").get(0);
        assertThat(updated.getPrecioVenta()).isEqualTo(3.0);
    }

    @Test
    void testFindProductoVentaByNombre_Null() {
        List<ProductoVenta> result = productoVentaRepository.findProductoVentaByNombre(null);
        assertThat(result).isEmpty();
    }

    @Test
    void testFindProductoVentaByCategoriaVenta_CaseSensitivity() {
        List<ProductoVenta> lowerCase = productoVentaRepository.findProductoVentaByCategoriaVenta("bebidas");
        List<ProductoVenta> upperCase = productoVentaRepository.findProductoVentaByCategoriaVenta("BEBIDAS");
    
        assertThat(lowerCase).isEmpty();
        assertThat(upperCase).isEmpty();
    }

        @Test
    void testFindProductoVentaByNombre_EmptyString() {
        List<ProductoVenta> result = productoVentaRepository.findProductoVentaByNombre("");

        assertThat(result).isEmpty();
    }

    @Test
    void testFindProductoVentaByCategoriaVenta_Null() {
        List<ProductoVenta> result = productoVentaRepository.findProductoVentaByCategoriaVenta(null);

        assertThat(result).isEmpty();
    }

    @Test
    void testFindProductoVentaByPrecioVenta_Null() {
        List<ProductoVenta> result = productoVentaRepository.findProductoVentaByPrecioVenta(null);
        assertThat(result).isEmpty();
    }

    @Test
    void testFindProductoVentaByPrecioVenta_Zero() {

        ProductoVenta prodZero = new ProductoVenta();
        prodZero.setName("Agua Gratis");
        prodZero.setCategoria(categoriaBebidas);
        prodZero.setPrecioVenta(0.0);
        productoVentaRepository.save(prodZero);

        List<ProductoVenta> result = productoVentaRepository.findProductoVentaByPrecioVenta(0.0);
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getName()).isEqualTo("Agua Gratis");
    }

    @Test
    void testFindProductoVentaByPrecioVenta_Negative() {

        ProductoVenta prodNegative = new ProductoVenta();
        prodNegative.setName("Producto Erróneo");
        prodNegative.setCategoria(categoriaBebidas);
        prodNegative.setPrecioVenta(-5.0);
        productoVentaRepository.save(prodNegative);

        List<ProductoVenta> result = productoVentaRepository.findProductoVentaByPrecioVenta(-5.0);
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getName()).isEqualTo("Producto Erróneo");
    }

    @Test
    void testFindProductoVentaByCategoriaVentaAndPrecioVenta_NullParameters() {

        List<ProductoVenta> result1 = productoVentaRepository.findProductoVentaByCategoriaVentaAndPrecioVenta(null, 10.5);
        List<ProductoVenta> result2 = productoVentaRepository.findProductoVentaByCategoriaVentaAndPrecioVenta("Bebidas", null);
        List<ProductoVenta> result3 = productoVentaRepository.findProductoVentaByCategoriaVentaAndPrecioVenta(null, null);
        assertThat(result1).isEmpty();
        assertThat(result2).isEmpty();
        assertThat(result3).isEmpty();
    }


    @Test
    void testDeleteNonExistentProductoVenta() {

        ProductoVenta prodFake = new ProductoVenta();
        prodFake.setName("Fake");
        prodFake.setCategoria(categoriaBebidas);
        prodFake.setPrecioVenta(100.0);

        productoVentaRepository.delete(prodFake);

        List<ProductoVenta> result = productoVentaRepository.findProductoVentaByNombre("Cerveza");
        assertThat(result).isNotEmpty();
    }

}
