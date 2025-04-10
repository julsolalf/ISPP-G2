package ispp_g2.gastrostock.testProductoInventario;

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
import ispp_g2.gastrostock.productoInventario.ProductoInventario;
import ispp_g2.gastrostock.productoInventario.ProductoInventarioRepository;
import ispp_g2.gastrostock.user.Authorities;
import ispp_g2.gastrostock.user.AuthoritiesRepository;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserRepository;

@DataJpaTest
@ActiveProfiles("test")
class ProductoInventarioRepositoryTest {

    @Autowired
    private ProductoInventarioRepository productoInventarioRepository;
    
    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private DuenoRepository duenoRepository;

    @Autowired
    private NegocioRepository negocioRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthoritiesRepository authoritiesRepository;

    private Categoria categoriaBebidas;
    private Categoria categoriaAlimentos;
    
    private ProductoInventario prod1;
    private ProductoInventario prod2;
    private ProductoInventario prod3;
    
    @BeforeEach
    void setUp() {

        Authorities authority = new Authorities();
        authority.setAuthority("DUENO");
        authority = authoritiesRepository.save(authority);

        // Crear usuario
        User user = new User();
        user.setUsername("juangarcia");
        user.setPassword("password123");
        user.setAuthority(authority);
        user = userRepository.save(user);

        Dueno dueno1 = new Dueno();
        dueno1.setFirstName("Juan");
        dueno1.setLastName("García");
        dueno1.setEmail("juan@example.com");
        dueno1.setNumTelefono("666111222");
        dueno1.setTokenDueno("TOKEN999");
        dueno1.setUser(user);
        duenoRepository.save(dueno1);

        Negocio negocio1 = new Negocio();
        negocio1.setId(1);
        negocio1.setName("Restaurante La Tasca");
        negocio1.setDireccion("Calle Principal 123");
        negocio1.setCiudad("Sevilla");
        negocio1.setPais("Espana");
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
        
        prod1 = new ProductoInventario();
        prod1.setName("Cerveza");
        prod1.setCategoria(categoriaBebidas);
        prod1.setPrecioCompra(10.5);
        prod1.setCantidadDeseada(100);
        prod1.setCantidadAviso(10);
        
        prod2 = new ProductoInventario();
        prod2.setName("Agua Mineral");
        prod2.setCategoria(categoriaBebidas);
        prod2.setPrecioCompra(5.0);
        prod2.setCantidadDeseada(200);
        prod2.setCantidadAviso(20);
        
        prod3 = new ProductoInventario();
        prod3.setName("Pan");
        prod3.setCategoria(categoriaAlimentos);
        prod3.setPrecioCompra(2.0);
        prod3.setCantidadDeseada(50);
        prod3.setCantidadAviso(5);
        

        productoInventarioRepository.save(prod1);
        productoInventarioRepository.save(prod2);
        productoInventarioRepository.save(prod3);
    }
    
    @Test
    void testFindByName() {
        ProductoInventario found = productoInventarioRepository.findByName("Cerveza");

        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Cerveza");
        
        ProductoInventario notFound = productoInventarioRepository.findByName("NoExiste");
        assertThat(notFound).isNull();
    }
    
    @Test
    void testFindByName_Null() {
        ProductoInventario result = productoInventarioRepository.findByName(null);
        assertThat(result).isNull();
    }
    
    @Test
    void testFindByName_CaseSensitivity() {
        ProductoInventario lowerCase = productoInventarioRepository.findByName("cerveza");
        ProductoInventario upperCase = productoInventarioRepository.findByName("CERVEZA");

        assertThat(lowerCase).isNull();
        assertThat(upperCase).isNull();
    }
    
    @Test
    void testFindByCategoriaName() {
        List<ProductoInventario> bebidas = productoInventarioRepository.findByCategoriaName("Bebidas");
        assertThat(bebidas).isNotEmpty();
        assertThat(bebidas).hasSize(2);
        
        List<ProductoInventario> alimentos = productoInventarioRepository.findByCategoriaName("Alimentos");
        assertThat(alimentos).isNotEmpty();
        assertThat(alimentos).hasSize(1);
        
        List<ProductoInventario> noExiste = productoInventarioRepository.findByCategoriaName("NoExiste");
        assertThat(noExiste).isEmpty();
    }
    
    @Test
    void testFindByPrecioCompra() {
        List<ProductoInventario> result = productoInventarioRepository.findByPrecioCompra(10.5);
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Cerveza");
        
        // Caso límite: precio que no existe
        List<ProductoInventario> none = productoInventarioRepository.findByPrecioCompra(999.9);
        assertThat(none).isEmpty();
    }
    
    @Test
    void testFindByPrecioCompra_Extremes() {
        List<ProductoInventario> zero = productoInventarioRepository.findByPrecioCompra(0.0);
        List<ProductoInventario> negative = productoInventarioRepository.findByPrecioCompra(-10.0);
        assertThat(zero).isEmpty();
        assertThat(negative).isEmpty();
    }
    
    @Test
    void testFindByCantidadDeseada() {
        List<ProductoInventario> result = productoInventarioRepository.findByCantidadDeseada(200);
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Agua Mineral");
        

        List<ProductoInventario> none = productoInventarioRepository.findByCantidadDeseada(0);
        assertThat(none).isEmpty();
    }
    
    @Test
    void testFindByCantidadDeseada_Extremes() {
        List<ProductoInventario> negative = productoInventarioRepository.findByCantidadDeseada(-5);
        List<ProductoInventario> extreme = productoInventarioRepository.findByCantidadDeseada(Integer.MAX_VALUE);
        assertThat(negative).isEmpty();
        assertThat(extreme).isEmpty();
    }
    
    @Test
    void testFindByCantidadAviso() {
        List<ProductoInventario> result = productoInventarioRepository.findByCantidadAviso(10);
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Cerveza");
        
        List<ProductoInventario> none = productoInventarioRepository.findByCantidadAviso(-1);
        assertThat(none).isEmpty();
    }
    
    @Test
    void testSaveProductoInventario() {
        ProductoInventario nuevoProd = new ProductoInventario();
        nuevoProd.setName("Vino");
        nuevoProd.setCategoria(categoriaBebidas);
        nuevoProd.setPrecioCompra(15.0);
        nuevoProd.setCantidadDeseada(50);
        nuevoProd.setCantidadAviso(5);

        ProductoInventario savedProd = productoInventarioRepository.save(nuevoProd);
        
        assertThat(savedProd).isNotNull();
        assertThat(savedProd.getId()).isNotNull();

        ProductoInventario found = productoInventarioRepository.findByName("Vino");
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Vino");
    }
    
    @Test
    void testDeleteProductoInventario() {
        productoInventarioRepository.delete(prod1);

        ProductoInventario found = productoInventarioRepository.findByName("Cerveza");
        assertThat(found).isNull();
    }
    
    @Test
    void testSaveProductoInventario_NullCategoria() {
        ProductoInventario prodNullCat = new ProductoInventario();
        prodNullCat.setName("Jugo");
        // No se asigna categoría
        prodNullCat.setPrecioCompra(3.0);
        prodNullCat.setCantidadDeseada(30);
        prodNullCat.setCantidadAviso(3);
    
        ProductoInventario saved = productoInventarioRepository.save(prodNullCat);
        assertThat(saved).isNotNull();
        // En este caso se espera que la categoría quede null
        assertThat(saved.getCategoria()).isNull();
    }
    
    @Test
    void testUpdateProductoInventario() {
        // Recuperamos un producto existente
        ProductoInventario producto = productoInventarioRepository.findByName("Pan");
        assertThat(producto).isNotNull();
        producto.setPrecioCompra(2.5);
        producto.setCantidadDeseada(60);
        productoInventarioRepository.save(producto);

        ProductoInventario updated = productoInventarioRepository.findByName("Pan");
        assertThat(updated.getPrecioCompra()).isEqualTo(2.5);
        assertThat(updated.getCantidadDeseada()).isEqualTo(60);
    }
    
    @Test
    void testFindByCantidadAviso_Extreme() {
        List<ProductoInventario> result = productoInventarioRepository.findByCantidadAviso(9999);
        assertThat(result).isEmpty();
    }
    
    @Test
    void testFindByCategoriaName_Inexistente() {
        List<ProductoInventario> result = productoInventarioRepository.findByCategoriaName("Electrónica");
        assertThat(result).isEmpty();
    }
}

