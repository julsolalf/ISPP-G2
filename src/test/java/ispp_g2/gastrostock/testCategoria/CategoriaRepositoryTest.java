package ispp_g2.gastrostock.testCategoria;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import ispp_g2.gastrostock.categorias.Categoria;
import ispp_g2.gastrostock.categorias.CategoriaRepository;
import ispp_g2.gastrostock.categorias.Pertenece;
import ispp_g2.gastrostock.dueno.Dueno;
import ispp_g2.gastrostock.dueno.DuenoRepository;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioRepository;
import ispp_g2.gastrostock.user.Authorities;
import ispp_g2.gastrostock.user.AuthoritiesRepository;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
class CategoriaRepositoryTest {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private NegocioRepository negocioRepository;

    @Autowired
    private DuenoRepository duenoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthoritiesRepository authoritiesRepository;
    
    private Categoria categoria1, categoria2, categoriaInvalida;
    private Categoria categoriaInventario1, categoriaInventario2, categoriaVenta1, categoriaVenta2;
    private Negocio negocio, negocio2;

    @BeforeEach
    void setUp() {
        // Crear autoridad
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
        dueno1 = duenoRepository.save(dueno1);

        // Crear un segundo usuario y dueño
        User user2 = new User();
        user2.setUsername("mariolopez");
        user2.setPassword("password456");
        user2.setAuthority(authority);
        user2 = userRepository.save(user2);

        Dueno dueno2 = new Dueno();
        dueno2.setFirstName("Mario");
        dueno2.setLastName("López");
        dueno2.setEmail("mario@example.com");
        dueno2.setNumTelefono("666333444");
        dueno2.setTokenDueno("TOKEN888");
        dueno2.setUser(user2);
        dueno2 = duenoRepository.save(dueno2);

        negocio = new Negocio();
        negocio.setName("Restaurante La Tasca");
        negocio.setDireccion("Calle Principal 123");
        negocio.setCiudad("Sevilla");
        negocio.setPais("Espana");
        negocio.setCodigoPostal("41001");
        negocio.setTokenNegocio(12345);
        negocio.setDueno(dueno1);
        negocio = negocioRepository.save(negocio);
        
        negocio2 = new Negocio();
        negocio2.setName("Bar El Rincón");
        negocio2.setDireccion("Avenida Libertad 45");
        negocio2.setCiudad("Málaga");
        negocio2.setPais("Espana");
        negocio2.setCodigoPostal("29001");
        negocio2.setTokenNegocio(54321);
        negocio2.setDueno(dueno2);
        negocio2 = negocioRepository.save(negocio2);

        // Crear categoría válida
        categoria1 = new Categoria();
        categoria1.setName("Bebidas");
        categoria1.setNegocio(negocio);
        categoria1.setPertenece(Pertenece.INVENTARIO);
        categoria1 = categoriaRepository.save(categoria1);

        // Crear otra categoría válida
        categoria2 = new Categoria();
        categoria2.setName("Comidas");
        categoria2.setNegocio(negocio);
        categoria2.setPertenece(Pertenece.VENTA);
        categoria2 = categoriaRepository.save(categoria2);
        
        // Categorías adicionales para pruebas específicas
        categoriaInventario1 = new Categoria();
        categoriaInventario1.setName("Materias Primas");
        categoriaInventario1.setNegocio(negocio);
        categoriaInventario1.setPertenece(Pertenece.INVENTARIO);
        categoriaInventario1 = categoriaRepository.save(categoriaInventario1);
        
        categoriaInventario2 = new Categoria();
        categoriaInventario2.setName("Utensilios");
        categoriaInventario2.setNegocio(negocio);
        categoriaInventario2.setPertenece(Pertenece.INVENTARIO);
        categoriaInventario2 = categoriaRepository.save(categoriaInventario2);
        
        categoriaVenta1 = new Categoria();
        categoriaVenta1.setName("Postres");
        categoriaVenta1.setNegocio(negocio);
        categoriaVenta1.setPertenece(Pertenece.VENTA);
        categoriaVenta1 = categoriaRepository.save(categoriaVenta1);
        
        categoriaVenta2 = new Categoria();
        categoriaVenta2.setName("Entrantes");
        categoriaVenta2.setNegocio(negocio2);
        categoriaVenta2.setPertenece(Pertenece.VENTA);
        categoriaVenta2 = categoriaRepository.save(categoriaVenta2);

        // Crear categoría con datos inválidos: sin name, sin negocio y sin pertenece
        categoriaInvalida = new Categoria();
        categoriaInvalida.setName("");
        categoriaInvalida.setNegocio(null);
        categoriaInvalida.setPertenece(null);
    }
    @Test
    void testSave() {
        // Crear una nueva categoría
        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setName("Postres");
        nuevaCategoria.setNegocio(negocio);
        nuevaCategoria.setPertenece(Pertenece.INVENTARIO);
        
        Categoria saved = categoriaRepository.save(nuevaCategoria);
        
        // Verificar que se guardó correctamente
        assertNotNull(saved.getId());
        assertEquals("Postres", saved.getName());
    }
    
    @Test
    void testFindAll() {
        // Obtener todas las categorías
        Iterable<Categoria> categorias = categoriaRepository.findAll();
        
        // Verificar que se encontraron al menos 2 (las creadas en setUp)
        long count = StreamSupport.stream(categorias.spliterator(), false).count();
        assertEquals(6, count);
    }
    
    @Test
    void testFindById() {
        // Buscar categoría por ID existente convirtiendo el id a String
        Optional<Categoria> found = categoriaRepository.findById(categoria1.getId());
        
        assertTrue(found.isPresent());
        assertEquals("Bebidas", found.get().getName());
    }
    
    @Test
    void testFindById_NotFound() {
        // Buscar categoría por un ID que no existe
        Optional<Categoria> notFound = categoriaRepository.findById(999);
        assertFalse(notFound.isPresent());
    }
    
    @Test
    void testDelete() {
        // Eliminar categoría2
        Iterable<Categoria> todos = categoriaRepository.findAll();
        long countBeforeDelete = StreamSupport.stream(todos.spliterator(), false).count();
        categoriaRepository.delete(categoria2);
        
        // Verificar que se eliminó
        Optional<Categoria> shouldBeDeleted = categoriaRepository.findById(categoria2.getId());
        assertFalse(shouldBeDeleted.isPresent());
        
        // Verificar que queda 1 categoría (la de Bebidas)
        Iterable<Categoria> remaining = categoriaRepository.findAll();
        long count = StreamSupport.stream(remaining.spliterator(), false).count();
        assertEquals(countBeforeDelete-1, count);
    }
    
    // TESTS PARA QUERIES PERSONALIZADAS

    @Test
    void testFindByName_Success() {
        // Buscar por name existente: "Bebidas"
        List<Categoria> found = categoriaRepository.findByName("Bebidas");
        assertNotNull(found);
        assertEquals(1, found.size());
        assertEquals("Bebidas", found.get(0).getName());
    }
    
    @Test
    void testFindByName_NotFound() {
        // Buscar por name que no existe
        List<Categoria> notFound = categoriaRepository.findByName("Inexistente");
        assertNotNull(notFound);
        assertTrue(notFound.isEmpty());
    }
    
    @Test
    void testFindByNegocioId_Success() {
        // Buscar categorías asociadas al negocio con ID "1"
        List<Categoria> found = categoriaRepository.findByNegocioId(negocio.getId());
        assertNotNull(found);
        // Se esperan al menos 2 categorías creadas en setUp
        assertTrue(found.size() >= 2);
    }
    
    @Test
    void testFindByNegocioId_NotFound() {
        // Buscar categorías para un negocio que no existe
        List<Categoria> notFound = categoriaRepository.findByNegocioId(999);
        assertNotNull(notFound);
        assertTrue(notFound.isEmpty());
    }

    @Test
    void testFindInventarioByNegocioId_Success() {
        List<Categoria> inventarioCategories = categoriaRepository.findInventarioByNegocioId(negocio.getId());
        
        assertNotNull(inventarioCategories);
        assertEquals(3, inventarioCategories.size());
        
        boolean foundBebidas = false;
        boolean foundMateriasPrimas = false;
        boolean foundUtensilios = false;
        
        for(Categoria c : inventarioCategories) {
            assertEquals(Pertenece.INVENTARIO, c.getPertenece());
            assertEquals(negocio.getId(), c.getNegocio().getId());
            
            if("Bebidas".equals(c.getName())) foundBebidas = true;
            if("Materias Primas".equals(c.getName())) foundMateriasPrimas = true;
            if("Utensilios".equals(c.getName())) foundUtensilios = true;
        }
        
        assertTrue(foundBebidas);
        assertTrue(foundMateriasPrimas);
        assertTrue(foundUtensilios);
    }
    
    @Test
    void testFindInventarioByNegocioId_EmptyResult() {
        List<Categoria> emptyList = categoriaRepository.findInventarioByNegocioId(999);
        
        assertNotNull(emptyList);
        assertTrue(emptyList.isEmpty());
    }
    
    @Test
    void testFindInventarioByNegocioId_DifferentNegocio() {
        List<Categoria> categoriasNegocio2 = categoriaRepository.findInventarioByNegocioId(negocio2.getId());
        
        assertNotNull(categoriasNegocio2);
        assertTrue(categoriasNegocio2.isEmpty());
    }
    
    // TESTS PARA findVentaByNegocioId
    
    @Test
    void testFindVentaByNegocioId_Success() {
        List<Categoria> ventaCategories = categoriaRepository.findVentaByNegocioId(negocio.getId());
        
        assertNotNull(ventaCategories);
        assertEquals(2, ventaCategories.size());
        
        boolean foundComidas = false;
        boolean foundPostres = false;
        
        for(Categoria c : ventaCategories) {
            assertEquals(Pertenece.VENTA, c.getPertenece());
            assertEquals(negocio.getId(), c.getNegocio().getId());
            
            if("Comidas".equals(c.getName())) foundComidas = true;
            if("Postres".equals(c.getName())) foundPostres = true;
        }
        
        assertTrue(foundComidas);
        assertTrue(foundPostres);
    }
    
    @Test
    void testFindVentaByNegocioId_OtherNegocio() {
        List<Categoria> ventaCategories = categoriaRepository.findVentaByNegocioId(negocio2.getId());
        
        assertNotNull(ventaCategories);
        assertEquals(1, ventaCategories.size());
        assertEquals("Entrantes", ventaCategories.get(0).getName());
    }
    
    @Test
    void testFindVentaByNegocioId_EmptyResult() {
        List<Categoria> emptyList = categoriaRepository.findVentaByNegocioId(999);
        
        assertNotNull(emptyList);
        assertTrue(emptyList.isEmpty());
    }
    
    // TESTS ADICIONALES PARA MÉTODOS HEREDADOS
    
    @Test
    void testDeleteById() {
        categoriaRepository.deleteById(categoria1.getId());
        
        Optional<Categoria> deleted = categoriaRepository.findById(categoria1.getId());
        assertFalse(deleted.isPresent());
        
        long countAfterDelete = StreamSupport.stream(categoriaRepository.findAll().spliterator(), false).count();
        assertEquals(5, countAfterDelete);
    }
    
    @Test
    void testExistsById() {
        assertTrue(categoriaRepository.existsById(categoriaInventario1.getId()));
        assertFalse(categoriaRepository.existsById(999));
    }
    
    @Test
    void testCount() {
        long count = categoriaRepository.count();
        assertEquals(6, count);
    }
    
    @Test
    void testSaveAll() {
        List<Categoria> newCategories = List.of(
            createTestCategoria("Licores", negocio, Pertenece.INVENTARIO),
            createTestCategoria("Pescados", negocio, Pertenece.VENTA)
        );
        
        Iterable<Categoria> savedCategories = categoriaRepository.saveAll(newCategories);
        long countNewCategories = StreamSupport.stream(savedCategories.spliterator(), false).count();
        assertEquals(2, countNewCategories);
        
        long totalCount = categoriaRepository.count();
        assertEquals(8, totalCount);
    }
    
    @Test
    void testFindAllById() {
        Iterable<Categoria> foundCategories = categoriaRepository.findAllById(
            List.of(categoria1.getId(), categoriaInventario1.getId(), categoriaVenta1.getId())
        );
        
        long count = StreamSupport.stream(foundCategories.spliterator(), false).count();
        assertEquals(3, count);
    }
    
    @Test
    void testDeleteAll() {
        categoriaRepository.deleteAll();
        
        long count = categoriaRepository.count();
        assertEquals(0, count);
    }
    
    @Test
    void testDeleteAllById() {
        categoriaRepository.deleteAllById(List.of(categoria1.getId(), categoria2.getId()));
        
        assertFalse(categoriaRepository.existsById(categoria1.getId()));
        assertFalse(categoriaRepository.existsById(categoria2.getId()));
        
        long count = categoriaRepository.count();
        assertEquals(4, count);
    }
    
    private Categoria createTestCategoria(String name, Negocio negocio, Pertenece pertenece) {
        Categoria categoria = new Categoria();
        categoria.setName(name);
        categoria.setNegocio(negocio);
        categoria.setPertenece(pertenece);
        return categoria;
    }
    

}
