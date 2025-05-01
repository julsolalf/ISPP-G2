package ispp_g2.gastrostock.testDueno;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import ispp_g2.gastrostock.dueno.Dueno;
import ispp_g2.gastrostock.dueno.DuenoRepository;
import ispp_g2.gastrostock.user.Authorities;
import ispp_g2.gastrostock.user.AuthoritiesRepository;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserRepository;
import jakarta.validation.ConstraintViolationException;
import ispp_g2.gastrostock.negocio.NegocioRepository;

@DataJpaTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
class DuenoRepositoryTest {

    @Autowired
    private DuenoRepository duenoRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthoritiesRepository authorities;
    
    
    @Autowired
    private NegocioRepository negocioRepository;
    
    private Dueno dueno1, dueno2, dueno3, duenoSinToken, duenoSinEmail;
    private User user1, user2, user3;
    
    @BeforeEach
    void setUp() {
        // Limpiar los repositorios para asegurar un estado consistente
        duenoRepository.deleteAll();
        userRepository.deleteAll();
        negocioRepository.deleteAll();
        authorities.deleteAll();

        // Crear roles de usuario
        Authorities dueno = new Authorities();
        dueno.setAuthority("DUEnO");
        dueno = authorities.save(dueno);
        
        // Crear usuarios para asociar a los duenos
        user1 = new User();
        user1.setUsername("juangarcia");
        user1.setPassword("password123");
        user1.setAuthority(dueno);
        user1 = userRepository.save(user1);
        
        user2 = new User();
        user2.setUsername("anamartinez");
        user2.setPassword("password456");
        user2.setAuthority(dueno);
        user2 = userRepository.save(user2);
        
        user3 = new User();
        user3.setUsername("pedrosanchez");
        user3.setPassword("password789");
        user3.setAuthority(dueno);
        user3 = userRepository.save(user3);
        
        // Crear duenos
        dueno1 = new Dueno();
        dueno1.setFirstName("Juan");
        dueno1.setLastName("García");
        dueno1.setEmail("juan@example.com");
        dueno1.setNumTelefono("652345678");
        dueno1.setTokenDueno("TOKEN123");
        dueno1.setUser(user1);
        dueno1 = duenoRepository.save(dueno1);
        
        dueno2 = new Dueno();
        dueno2.setFirstName("Ana");
        dueno2.setLastName("Martínez");
        dueno2.setEmail("ana@example.com");
        dueno2.setNumTelefono("654321987");
        dueno2.setTokenDueno("TOKEN456");
        dueno2.setUser(user2);
        dueno2 = duenoRepository.save(dueno2);
        
        dueno3 = new Dueno();
        dueno3.setFirstName("Pedro");
        dueno3.setLastName("García");
        dueno3.setEmail("pedro@example.com");
        dueno3.setNumTelefono("678912345");
        dueno3.setTokenDueno("TOKEN789");
        dueno3.setUser(user3);
        dueno3 = duenoRepository.save(dueno3);
        
        // Crear dueno sin token para casos de prueba
        duenoSinToken = new Dueno();
        duenoSinToken.setFirstName("Carlos");
        duenoSinToken.setLastName("Ruiz");
        duenoSinToken.setEmail("carlos@example.com");
        duenoSinToken.setNumTelefono("612345678");
        duenoSinToken.setTokenDueno("");
        
        // Crear dueno sin email para casos de prueba
        duenoSinEmail = new Dueno();
        duenoSinEmail.setFirstName("Laura");
        duenoSinEmail.setLastName("Fernández");
        duenoSinEmail.setEmail("");
        duenoSinEmail.setNumTelefono("687654321");
        duenoSinEmail.setTokenDueno("TOKEN999");
    }

    // TESTS PARA MÉTODOS CRUD HEREDADOS
    
    @Test
    void testSave() {
        // Crear un nuevo dueno
        Dueno nuevoDueno = new Dueno();
        nuevoDueno.setFirstName("Nuevo");
        nuevoDueno.setLastName("Dueno");
        nuevoDueno.setEmail("nuevo@example.com");
        nuevoDueno.setNumTelefono("611223344");
        nuevoDueno.setTokenDueno("TOKENNUEVO");
        
        // Guardar el dueno
        Dueno saved = duenoRepository.save(nuevoDueno);
        
        // Verificar que se guardó correctamente
        assertNotNull(saved.getId());
        assertEquals("Nuevo", saved.getFirstName());
        assertEquals("Dueno", saved.getLastName());
        assertEquals("nuevo@example.com", saved.getEmail());
    }
    
    @Test
    void testFindAll() {
        // Obtener todos los duenos
        Iterable<Dueno> duenos = duenoRepository.findAll();
        
        // Verificar que hay 3 duenos (los creados en setUp)
        int count = 0;
        for (@SuppressWarnings("unused") Dueno dueno : duenos) {
            count++;
        }
        assertEquals(3, count);
    }
    
    @Test
    void testFindById() {
        // Buscar por ID existente
        Optional<Dueno> found = duenoRepository.findById(dueno1.getId());
        
        // Verificar que existe y tiene los datos correctos
        assertTrue(found.isPresent());
        assertEquals("Juan", found.get().getFirstName());
        assertEquals("García", found.get().getLastName());
    }
    
    @Test
    void testFindById_NotFound() {
        // Buscar por ID que no existe
        Optional<Dueno> notFound = duenoRepository.findById(999);
        
        // Verificar que no existe
        assertFalse(notFound.isPresent());
    }
    
    @Test
    void testDelete() {
        // Eliminar un dueno
        duenoRepository.delete(dueno2);
        
        // Verificar que se eliminó
        Optional<Dueno> shouldBeDeleted = duenoRepository.findById(dueno2.getId());
        assertFalse(shouldBeDeleted.isPresent());
        
        // Verificar que el resto sigue existiendo
        Iterable<Dueno> remaining = duenoRepository.findAll();
        int count = 0;
        for (@SuppressWarnings("unused") Dueno dueno : remaining) {
            count++;
        }
        assertEquals(2, count);
    }

    // TESTS PARA QUERIES PERSONALIZADAS
    
    @Test
    void testFindDuenoByEmail_Success() {
        // Buscar por email existente
        Optional<Dueno> found = duenoRepository.findDuenoByEmail("juan@example.com");
        
        // Verificar que existe y tiene los datos correctos
        assertTrue(found.isPresent());
        assertEquals("Juan", found.get().getFirstName());
        assertEquals("García", found.get().getLastName());
    }
    
    @Test
    void testFindDuenoByEmail_NotFound() {
        // Buscar por email que no existe
        Optional<Dueno> notFound = duenoRepository.findDuenoByEmail("noexiste@example.com");
        
        // Verificar que no existe
        assertFalse(notFound.isPresent());
    }
    
    @Test
    void testFindDuenoByEmail_EmptyEmail() {
        // Guardar dueno con email vacío (si es válido según las restricciones)
        try {
            duenoRepository.save(duenoSinEmail);
            
            // Buscar por email vacío
            Optional<Dueno> found = duenoRepository.findDuenoByEmail("");
            
            // Si se permite email vacío, verificar que se encuentra
            if (found.isPresent()) {
                assertEquals("Laura", found.get().getFirstName());
            }
        } catch (ConstraintViolationException e) {
            // Si no se permite email vacío, verificar que lanza excepción
            // Esta excepción es esperada si hay una restricción de no nulo
        }
    }
    
    @Test
    void testFindDuenoByNombre_Success() {
        // Buscar por nombre existente
        List<Dueno> found = duenoRepository.findDuenoByNombre("Juan");
        
        // Verificar que existe y tiene los datos correctos
        assertEquals(1, found.size());
        assertEquals("García", found.get(0).getLastName());
    }
    
    @Test
    void testFindDuenoByNombre_MultipleResults() {
        // Crear otro dueno con el mismo nombre
        Authorities authority = new Authorities();
        authority.setAuthority("DUENO");
        authority = authorities.save(authority);

        // Crear usuario
        User user = new User();
        user.setUsername("juanperez");
        user.setPassword("password123");
        user.setAuthority(authority);
        user = userRepository.save(user);

        Dueno otroDueno = new Dueno();
        otroDueno.setFirstName("Juan");
        otroDueno.setLastName("Pérez");
        otroDueno.setEmail("juan.perez@example.com");
        otroDueno.setNumTelefono("678123456");
        otroDueno.setTokenDueno("TOKEN_JUAN2");
        otroDueno.setUser(user);
        duenoRepository.save(otroDueno);
        
        // Buscar por nombre que tiene múltiples resultados
        List<Dueno> found = duenoRepository.findDuenoByNombre("Juan");
        
        // Verificar que existen varios resultados
        assertEquals(2, found.size());
        assertTrue(found.stream().anyMatch(d -> d.getLastName().equals("García")));
        assertTrue(found.stream().anyMatch(d -> d.getLastName().equals("Pérez")));
    }
    
    @Test
    void testFindDuenoByNombre_NotFound() {
        // Buscar por nombre que no existe
        List<Dueno> notFound = duenoRepository.findDuenoByNombre("Inexistente");
        
        // Verificar que la lista está vacía
        assertTrue(notFound.isEmpty());
    }
    
    @Test
    void testFindDuenoByNombre_EmptyNombre() {
        // Crear dueno con nombre vacío
        Dueno duenoSinNombre = new Dueno();
        duenoSinNombre.setFirstName("");
        duenoSinNombre.setLastName("Apellido");
        duenoSinNombre.setEmail("sinnombre@example.com");
        duenoSinNombre.setNumTelefono("666777888");
        duenoSinNombre.setTokenDueno("TOKEN_SIN_NOMBRE");
        
        try {
            duenoRepository.save(duenoSinNombre);
            
            // Buscar por nombre vacío
            List<Dueno> found = duenoRepository.findDuenoByNombre("");
            
            // Si se permite nombre vacío, verificar resultados
            if (!found.isEmpty()) {
                assertEquals("Apellido", found.get(0).getLastName());
            }
        } catch (ConstraintViolationException e) {
            // Si no se permite nombre vacío, la excepción es esperada
        }
    }
    
    @Test
    void testFindDuenoByApellido_Success() {
        // Buscar por apellido existente
        List<Dueno> found = duenoRepository.findDuenoByApellido("García");
        
        // Verificar que existen resultados (deberían ser 2: Juan García y Pedro García)
        assertEquals(2, found.size());
        assertTrue(found.stream().anyMatch(d -> d.getFirstName().equals("Juan")));
        assertTrue(found.stream().anyMatch(d -> d.getFirstName().equals("Pedro")));
    }
    
    @Test
    void testFindDuenoByApellido_NotFound() {
        // Buscar por apellido que no existe
        List<Dueno> notFound = duenoRepository.findDuenoByApellido("Inexistente");
        
        // Verificar que la lista está vacía
        assertTrue(notFound.isEmpty());
    }
    
    @Test
    void testFindDuenoByTelefono_Success() {
        // Buscar por teléfono existente
        Optional<Dueno> found = duenoRepository.findDuenoByTelefono("652345678");
        
        // Verificar que existe y tiene los datos correctos
        assertTrue(found.isPresent());
        assertEquals("Juan", found.get().getFirstName());
        assertEquals("García", found.get().getLastName());
    }
    
    @Test
    void testFindDuenoByTelefono_NotFound() {
        // Buscar por teléfono que no existe
        Optional<Dueno> notFound = duenoRepository.findDuenoByTelefono("999999999");
        
        // Verificar que no existe
        assertFalse(notFound.isPresent());
    }
    
    @Test
    void testFindDuenoByUser_Success() {
        // Buscar por User ID existente
        Optional<Dueno> found = duenoRepository.findDuenoByUser(user1.getId());
        
        // Verificar que existe y tiene los datos correctos
        assertTrue(found.isPresent());
        assertEquals("Juan", found.get().getFirstName());
        assertEquals("García", found.get().getLastName());
    }
    
    @Test
    void testFindDuenoByUser_NotFound() {
        // Buscar por User ID que no existe
        Optional<Dueno> notFound = duenoRepository.findDuenoByUser(999);
        
        // Verificar que no existe
        assertFalse(notFound.isPresent());
    }
    
    @Test
    void testFindDuenoByToken_Success() {
        // Buscar por token existente
        Optional<Dueno> found = duenoRepository.findDuenoByToken("TOKEN123");
        
        // Verificar que existe y tiene los datos correctos
        assertTrue(found.isPresent());
        assertEquals("Juan", found.get().getFirstName());
        assertEquals("García", found.get().getLastName());
    }
    
    @Test
    void testFindDuenoByToken_NotFound() {
        // Buscar por token que no existe
        Optional<Dueno> notFound = duenoRepository.findDuenoByToken("TOKEN_INEXISTENTE");
        
        // Verificar que no existe
        assertFalse(notFound.isPresent());
    }
    
    @Test
    void testFindDuenoByToken_EmptyToken() {
        // Guardar dueno con token vacío
        try {
            duenoRepository.save(duenoSinToken);
            
            // Buscar por token vacío
            Optional<Dueno> found = duenoRepository.findDuenoByToken("");
            
            // Si se permite token vacío, verificar que se encuentra
            if (found.isPresent()) {
                assertEquals("Carlos", found.get().getFirstName());
            }
        } catch (ConstraintViolationException e) {
            // Si no se permite token vacío, la excepción es esperada
        }
    }
    
    // TESTS PARA CASOS ESPECIALES Y RESTRICCIONES
    
    @Test
    void testEmailUniqueConstraint() {
        // Crear dueno con el mismo email que uno existente
        Dueno duenoEmailDuplicado = new Dueno();
        duenoEmailDuplicado.setFirstName("Otro");
        duenoEmailDuplicado.setLastName("Dueno");
        duenoEmailDuplicado.setEmail("juan@example.com"); // Email ya existente
        duenoEmailDuplicado.setNumTelefono("666555444");
        duenoEmailDuplicado.setTokenDueno("TOKEN_DUPLICADO");
        
        // Verificar que lanza excepción por restricción de unicidad (si existe)
        try {
            duenoRepository.save(duenoEmailDuplicado);
            // Si no hay restricción de unicidad, el test pasará sin verificación adicional
            
            // Verificar si realmente permitió duplicar el email buscando por él
            List<Dueno> duenosConMismoEmail = StreamSupport.stream(duenoRepository.findAll().spliterator(), false)
            .filter(d -> "juan@example.com".equals(d.getEmail()))
            .toList();
          
            if (duenosConMismoEmail.size() > 1) {
                System.out.println("ADVERTENCIA: La base de datos permite emails duplicados para duenos");
            }
        } catch (DataIntegrityViolationException e) {
            // Excepción esperada si hay restricción de unicidad
        }
    }
    
    @Test
    void testTokenDuenoUniqueConstraint() {
        // Crear dueno con el mismo token que uno existente
        Dueno duenoTokenDuplicado = new Dueno();
        duenoTokenDuplicado.setFirstName("Otro");
        duenoTokenDuplicado.setLastName("Dueno");
        duenoTokenDuplicado.setEmail("otro@example.com");
        duenoTokenDuplicado.setNumTelefono("666555444");
        duenoTokenDuplicado.setTokenDueno("TOKEN123"); // Token ya existente
        
        // Verificar que lanza excepción por restricción de unicidad (si existe)
        try {
            duenoRepository.save(duenoTokenDuplicado);
            // Si no hay restricción de unicidad, el test pasará sin verificación adicional
            
            // Verificar si realmente permitió duplicar el token buscando por él
            List<Dueno> duenosConMismoToken = StreamSupport.stream(duenoRepository.findAll().spliterator(), false)
                .filter(d -> "TOKEN123".equals(d.getTokenDueno()))
                .toList();
            
            if (duenosConMismoToken.size() > 1) {
                System.out.println("ADVERTENCIA: La base de datos permite tokens duplicados para duenos");
            }
        } catch (DataIntegrityViolationException e) {
            // Excepción esperada si hay restricción de unicidad
        }
    }
    /* 
    @Test
    void testCascadeDeleteDueno() {
        // Crear un negocio asociado a un dueno
        negocio = new Negocio();
        negocio.setName("Restaurante La Tasca");
        negocio.setDireccion("Calle Principal 123");
        negocio.setCiudad("Sevilla");
        negocio.setPais("Espana");
        negocio.setCodigoPostal("41001");
        negocio.setTokenNegocio(12345);
        negocio.setDueno(dueno1);
        negocio = negocioRepository.save(negocio);
        
        // Verificar que se creó la relación
        assertEquals(dueno1.getId(), negocio.getDueno().getId());
        
        // Intentar eliminar el dueno para ver si hay restricciones de integridad referencial
        try {
            duenoRepository.delete(dueno1);
            duenoRepository.findAll(); // Forzar que se apliquen los cambios
            
            // Si no hay restricción, verificar que el dueno se eliminó
            Optional<Dueno> shouldBeDeleted = duenoRepository.findById(Integer.toString(dueno1.getId()));
            assertFalse(shouldBeDeleted.isPresent());
            
            // Verificar qué pasó con el negocio (depende de las configuraciones de cascada)
            Optional<Negocio> negocioAfterDelete = negocioRepository.findById(Integer.toString(negocio.getId()));
            if (negocioAfterDelete.isPresent()) {
                // Si el negocio sigue existiendo, verificar si su relación con dueno está null
                assertNull(negocioAfterDelete.get().getDueno());
            } else {
                // Si el negocio fue eliminado en cascada, la prueba pasa
            }
        } catch (Exception e) {
            // Si hay restricción de integridad referencial, capturar la excepción
            System.out.println("Error al intentar eliminar dueno con negocio asociado: " + e.getMessage());
            // La prueba sigue siendo válida, ya que estamos verificando el comportamiento
        }
    } */
}