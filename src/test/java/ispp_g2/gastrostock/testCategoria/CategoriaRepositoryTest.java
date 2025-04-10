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
    private Negocio negocio;

    @BeforeEach
    void setUp() {
        // Limpiar repositorios se realiza automáticamente en un contexto de DataJpaTest

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

        negocio = new Negocio();
        negocio.setName("Restaurante La Tasca");
        negocio.setDireccion("Calle Principal 123");
        negocio.setCiudad("Sevilla");
        negocio.setPais("Espana");
        negocio.setCodigoPostal("41001");
        negocio.setTokenNegocio(12345);
        negocio.setDueno(dueno1);
        negocio = negocioRepository.save(negocio);

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

        // Crear categoría con datos inválidos: sin name, sin negocio y sin pertenece
        categoriaInvalida = new Categoria();
        categoriaInvalida.setName("");
        categoriaInvalida.setNegocio(null);
        categoriaInvalida.setPertenece(null);
    }
    
    // TESTS PARA MÉTODOS CRUD HEREDADOS

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
        assertEquals(2, count);
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
        categoriaRepository.delete(categoria2);
        
        // Verificar que se eliminó
        Optional<Categoria> shouldBeDeleted = categoriaRepository.findById(categoria2.getId());
        assertFalse(shouldBeDeleted.isPresent());
        
        // Verificar que queda 1 categoría (la de Bebidas)
        Iterable<Categoria> remaining = categoriaRepository.findAll();
        long count = StreamSupport.stream(remaining.spliterator(), false).count();
        assertEquals(1, count);
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
    

}
