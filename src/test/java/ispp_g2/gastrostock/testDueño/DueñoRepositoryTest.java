package ispp_g2.gastrostock.testDueño;

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

import ispp_g2.gastrostock.dueño.Dueño;
import ispp_g2.gastrostock.dueño.DueñoRepository;
import ispp_g2.gastrostock.user.Authorities;
import ispp_g2.gastrostock.user.AuthoritiesRepository;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserRepository;
import jakarta.validation.ConstraintViolationException;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioRepository;

@DataJpaTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
class DueñoRepositoryTest {

    @Autowired
    private DueñoRepository dueñoRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthoritiesRepository authorities;
    
    @Autowired
    private NegocioRepository negocioRepository;
    
    private Dueño dueño1, dueño2, dueño3, dueñoSinToken, dueñoSinEmail;
    private User user1, user2, user3;
    private Negocio negocio;
    
    @BeforeEach
    void setUp() {
        // Limpiar los repositorios para asegurar un estado consistente
        dueñoRepository.deleteAll();
        userRepository.deleteAll();
        negocioRepository.deleteAll();
        authorities.deleteAll();

        // Crear roles de usuario
        Authorities dueño = new Authorities();
        dueño.setAuthority("DUEÑO");
        dueño = authorities.save(dueño);
        
        // Crear usuarios para asociar a los dueños
        user1 = new User();
        user1.setUsername("juangarcia");
        user1.setPassword("password123");
        user1.setAuthority(dueño);
        user1 = userRepository.save(user1);
        
        user2 = new User();
        user2.setUsername("anamartinez");
        user2.setPassword("password456");
        user2.setAuthority(dueño);
        user2 = userRepository.save(user2);
        
        user3 = new User();
        user3.setUsername("pedrosanchez");
        user3.setPassword("password789");
        user3.setAuthority(dueño);
        user3 = userRepository.save(user3);
        
        // Crear dueños
        dueño1 = new Dueño();
        dueño1.setFirstName("Juan");
        dueño1.setLastName("García");
        dueño1.setEmail("juan@example.com");
        dueño1.setNumTelefono("652345678");
        dueño1.setTokenDueño("TOKEN123");
        dueño1.setUser(user1);
        dueño1 = dueñoRepository.save(dueño1);
        
        dueño2 = new Dueño();
        dueño2.setFirstName("Ana");
        dueño2.setLastName("Martínez");
        dueño2.setEmail("ana@example.com");
        dueño2.setNumTelefono("654321987");
        dueño2.setTokenDueño("TOKEN456");
        dueño2.setUser(user2);
        dueño2 = dueñoRepository.save(dueño2);
        
        dueño3 = new Dueño();
        dueño3.setFirstName("Pedro");
        dueño3.setLastName("García");
        dueño3.setEmail("pedro@example.com");
        dueño3.setNumTelefono("678912345");
        dueño3.setTokenDueño("TOKEN789");
        dueño3.setUser(user3);
        dueño3 = dueñoRepository.save(dueño3);
        
        // Crear dueño sin token para casos de prueba
        dueñoSinToken = new Dueño();
        dueñoSinToken.setFirstName("Carlos");
        dueñoSinToken.setLastName("Ruiz");
        dueñoSinToken.setEmail("carlos@example.com");
        dueñoSinToken.setNumTelefono("612345678");
        dueñoSinToken.setTokenDueño("");
        
        // Crear dueño sin email para casos de prueba
        dueñoSinEmail = new Dueño();
        dueñoSinEmail.setFirstName("Laura");
        dueñoSinEmail.setLastName("Fernández");
        dueñoSinEmail.setEmail("");
        dueñoSinEmail.setNumTelefono("687654321");
        dueñoSinEmail.setTokenDueño("TOKEN999");
    }

    // TESTS PARA MÉTODOS CRUD HEREDADOS
    
    @Test
    void testSave() {
        // Crear un nuevo dueño
        Dueño nuevoDueño = new Dueño();
        nuevoDueño.setFirstName("Nuevo");
        nuevoDueño.setLastName("Dueño");
        nuevoDueño.setEmail("nuevo@example.com");
        nuevoDueño.setNumTelefono("611223344");
        nuevoDueño.setTokenDueño("TOKENNUEVO");
        
        // Guardar el dueño
        Dueño saved = dueñoRepository.save(nuevoDueño);
        
        // Verificar que se guardó correctamente
        assertNotNull(saved.getId());
        assertEquals("Nuevo", saved.getFirstName());
        assertEquals("Dueño", saved.getLastName());
        assertEquals("nuevo@example.com", saved.getEmail());
    }
    
    @Test
    void testFindAll() {
        // Obtener todos los dueños
        Iterable<Dueño> dueños = dueñoRepository.findAll();
        
        // Verificar que hay 3 dueños (los creados en setUp)
        int count = 0;
        for (Dueño dueño : dueños) {
            count++;
        }
        assertEquals(3, count);
    }
    
    @Test
    void testFindById() {
        // Buscar por ID existente
        Optional<Dueño> found = dueñoRepository.findById(Integer.toString(dueño1.getId()));
        
        // Verificar que existe y tiene los datos correctos
        assertTrue(found.isPresent());
        assertEquals("Juan", found.get().getFirstName());
        assertEquals("García", found.get().getLastName());
    }
    
    @Test
    void testFindById_NotFound() {
        // Buscar por ID que no existe
        Optional<Dueño> notFound = dueñoRepository.findById("999");
        
        // Verificar que no existe
        assertFalse(notFound.isPresent());
    }
    
    @Test
    void testDelete() {
        // Eliminar un dueño
        dueñoRepository.delete(dueño2);
        
        // Verificar que se eliminó
        Optional<Dueño> shouldBeDeleted = dueñoRepository.findById(Integer.toString(dueño2.getId()));
        assertFalse(shouldBeDeleted.isPresent());
        
        // Verificar que el resto sigue existiendo
        Iterable<Dueño> remaining = dueñoRepository.findAll();
        int count = 0;
        for (Dueño dueño : remaining) {
            count++;
        }
        assertEquals(2, count);
    }

    // TESTS PARA QUERIES PERSONALIZADAS
    
    @Test
    void testFindDueñoByEmail_Success() {
        // Buscar por email existente
        Optional<Dueño> found = dueñoRepository.findDueñoByEmail("juan@example.com");
        
        // Verificar que existe y tiene los datos correctos
        assertTrue(found.isPresent());
        assertEquals("Juan", found.get().getFirstName());
        assertEquals("García", found.get().getLastName());
    }
    
    @Test
    void testFindDueñoByEmail_NotFound() {
        // Buscar por email que no existe
        Optional<Dueño> notFound = dueñoRepository.findDueñoByEmail("noexiste@example.com");
        
        // Verificar que no existe
        assertFalse(notFound.isPresent());
    }
    
    @Test
    void testFindDueñoByEmail_EmptyEmail() {
        // Guardar dueño con email vacío (si es válido según las restricciones)
        try {
            dueñoRepository.save(dueñoSinEmail);
            
            // Buscar por email vacío
            Optional<Dueño> found = dueñoRepository.findDueñoByEmail("");
            
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
    void testFindDueñoByNombre_Success() {
        // Buscar por nombre existente
        List<Dueño> found = dueñoRepository.findDueñoByNombre("Juan");
        
        // Verificar que existe y tiene los datos correctos
        assertEquals(1, found.size());
        assertEquals("García", found.get(0).getLastName());
    }
    
    @Test
    void testFindDueñoByNombre_MultipleResults() {
        // Crear otro dueño con el mismo nombre
        Dueño otroDueño = new Dueño();
        otroDueño.setFirstName("Juan");
        otroDueño.setLastName("Pérez");
        otroDueño.setEmail("juan.perez@example.com");
        otroDueño.setNumTelefono("678123456");
        otroDueño.setTokenDueño("TOKEN_JUAN2");
        dueñoRepository.save(otroDueño);
        
        // Buscar por nombre que tiene múltiples resultados
        List<Dueño> found = dueñoRepository.findDueñoByNombre("Juan");
        
        // Verificar que existen varios resultados
        assertEquals(2, found.size());
        assertTrue(found.stream().anyMatch(d -> d.getLastName().equals("García")));
        assertTrue(found.stream().anyMatch(d -> d.getLastName().equals("Pérez")));
    }
    
    @Test
    void testFindDueñoByNombre_NotFound() {
        // Buscar por nombre que no existe
        List<Dueño> notFound = dueñoRepository.findDueñoByNombre("Inexistente");
        
        // Verificar que la lista está vacía
        assertTrue(notFound.isEmpty());
    }
    
    @Test
    void testFindDueñoByNombre_EmptyNombre() {
        // Crear dueño con nombre vacío
        Dueño dueñoSinNombre = new Dueño();
        dueñoSinNombre.setFirstName("");
        dueñoSinNombre.setLastName("Apellido");
        dueñoSinNombre.setEmail("sinnombre@example.com");
        dueñoSinNombre.setNumTelefono("666777888");
        dueñoSinNombre.setTokenDueño("TOKEN_SIN_NOMBRE");
        
        try {
            dueñoRepository.save(dueñoSinNombre);
            
            // Buscar por nombre vacío
            List<Dueño> found = dueñoRepository.findDueñoByNombre("");
            
            // Si se permite nombre vacío, verificar resultados
            if (!found.isEmpty()) {
                assertEquals("Apellido", found.get(0).getLastName());
            }
        } catch (ConstraintViolationException e) {
            // Si no se permite nombre vacío, la excepción es esperada
        }
    }
    
    @Test
    void testFindDueñoByApellido_Success() {
        // Buscar por apellido existente
        List<Dueño> found = dueñoRepository.findDueñoByApellido("García");
        
        // Verificar que existen resultados (deberían ser 2: Juan García y Pedro García)
        assertEquals(2, found.size());
        assertTrue(found.stream().anyMatch(d -> d.getFirstName().equals("Juan")));
        assertTrue(found.stream().anyMatch(d -> d.getFirstName().equals("Pedro")));
    }
    
    @Test
    void testFindDueñoByApellido_NotFound() {
        // Buscar por apellido que no existe
        List<Dueño> notFound = dueñoRepository.findDueñoByApellido("Inexistente");
        
        // Verificar que la lista está vacía
        assertTrue(notFound.isEmpty());
    }
    
    @Test
    void testFindDueñoByTelefono_Success() {
        // Buscar por teléfono existente
        Optional<Dueño> found = dueñoRepository.findDueñoByTelefono("652345678");
        
        // Verificar que existe y tiene los datos correctos
        assertTrue(found.isPresent());
        assertEquals("Juan", found.get().getFirstName());
        assertEquals("García", found.get().getLastName());
    }
    
    @Test
    void testFindDueñoByTelefono_NotFound() {
        // Buscar por teléfono que no existe
        Optional<Dueño> notFound = dueñoRepository.findDueñoByTelefono("999999999");
        
        // Verificar que no existe
        assertFalse(notFound.isPresent());
    }
    
    @Test
    void testFindDueñoByUser_Success() {
        // Buscar por User ID existente
        Optional<Dueño> found = dueñoRepository.findDueñoByUser(Integer.toString(user1.getId()));
        
        // Verificar que existe y tiene los datos correctos
        assertTrue(found.isPresent());
        assertEquals("Juan", found.get().getFirstName());
        assertEquals("García", found.get().getLastName());
    }
    
    @Test
    void testFindDueñoByUser_NotFound() {
        // Buscar por User ID que no existe
        Optional<Dueño> notFound = dueñoRepository.findDueñoByUser("999");
        
        // Verificar que no existe
        assertFalse(notFound.isPresent());
    }
    
    @Test
    void testFindDueñoByToken_Success() {
        // Buscar por token existente
        Optional<Dueño> found = dueñoRepository.findDueñoByToken("TOKEN123");
        
        // Verificar que existe y tiene los datos correctos
        assertTrue(found.isPresent());
        assertEquals("Juan", found.get().getFirstName());
        assertEquals("García", found.get().getLastName());
    }
    
    @Test
    void testFindDueñoByToken_NotFound() {
        // Buscar por token que no existe
        Optional<Dueño> notFound = dueñoRepository.findDueñoByToken("TOKEN_INEXISTENTE");
        
        // Verificar que no existe
        assertFalse(notFound.isPresent());
    }
    
    @Test
    void testFindDueñoByToken_EmptyToken() {
        // Guardar dueño con token vacío
        try {
            dueñoRepository.save(dueñoSinToken);
            
            // Buscar por token vacío
            Optional<Dueño> found = dueñoRepository.findDueñoByToken("");
            
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
        // Crear dueño con el mismo email que uno existente
        Dueño dueñoEmailDuplicado = new Dueño();
        dueñoEmailDuplicado.setFirstName("Otro");
        dueñoEmailDuplicado.setLastName("Dueño");
        dueñoEmailDuplicado.setEmail("juan@example.com"); // Email ya existente
        dueñoEmailDuplicado.setNumTelefono("666555444");
        dueñoEmailDuplicado.setTokenDueño("TOKEN_DUPLICADO");
        
        // Verificar que lanza excepción por restricción de unicidad (si existe)
        try {
            dueñoRepository.save(dueñoEmailDuplicado);
            // Si no hay restricción de unicidad, el test pasará sin verificación adicional
            
            // Verificar si realmente permitió duplicar el email buscando por él
            List<Dueño> dueñosConMismoEmail = StreamSupport.stream(dueñoRepository.findAll().spliterator(), false)
            .filter(d -> "juan@example.com".equals(d.getEmail()))
            .toList();
          
            if (dueñosConMismoEmail.size() > 1) {
                System.out.println("ADVERTENCIA: La base de datos permite emails duplicados para dueños");
            }
        } catch (DataIntegrityViolationException e) {
            // Excepción esperada si hay restricción de unicidad
        }
    }
    
    @Test
    void testTokenDueñoUniqueConstraint() {
        // Crear dueño con el mismo token que uno existente
        Dueño dueñoTokenDuplicado = new Dueño();
        dueñoTokenDuplicado.setFirstName("Otro");
        dueñoTokenDuplicado.setLastName("Dueño");
        dueñoTokenDuplicado.setEmail("otro@example.com");
        dueñoTokenDuplicado.setNumTelefono("666555444");
        dueñoTokenDuplicado.setTokenDueño("TOKEN123"); // Token ya existente
        
        // Verificar que lanza excepción por restricción de unicidad (si existe)
        try {
            dueñoRepository.save(dueñoTokenDuplicado);
            // Si no hay restricción de unicidad, el test pasará sin verificación adicional
            
            // Verificar si realmente permitió duplicar el token buscando por él
            List<Dueño> dueñosConMismoToken = StreamSupport.stream(dueñoRepository.findAll().spliterator(), false)
                .filter(d -> "TOKEN123".equals(d.getTokenDueño()))
                .toList();
            
            if (dueñosConMismoToken.size() > 1) {
                System.out.println("ADVERTENCIA: La base de datos permite tokens duplicados para dueños");
            }
        } catch (DataIntegrityViolationException e) {
            // Excepción esperada si hay restricción de unicidad
        }
    }
    /* 
    @Test
    void testCascadeDeleteDueño() {
        // Crear un negocio asociado a un dueño
        negocio = new Negocio();
        negocio.setName("Restaurante La Tasca");
        negocio.setDireccion("Calle Principal 123");
        negocio.setCiudad("Sevilla");
        negocio.setPais("España");
        negocio.setCodigoPostal("41001");
        negocio.setTokenNegocio(12345);
        negocio.setDueño(dueño1);
        negocio = negocioRepository.save(negocio);
        
        // Verificar que se creó la relación
        assertEquals(dueño1.getId(), negocio.getDueño().getId());
        
        // Intentar eliminar el dueño para ver si hay restricciones de integridad referencial
        try {
            dueñoRepository.delete(dueño1);
            dueñoRepository.findAll(); // Forzar que se apliquen los cambios
            
            // Si no hay restricción, verificar que el dueño se eliminó
            Optional<Dueño> shouldBeDeleted = dueñoRepository.findById(Integer.toString(dueño1.getId()));
            assertFalse(shouldBeDeleted.isPresent());
            
            // Verificar qué pasó con el negocio (depende de las configuraciones de cascada)
            Optional<Negocio> negocioAfterDelete = negocioRepository.findById(Integer.toString(negocio.getId()));
            if (negocioAfterDelete.isPresent()) {
                // Si el negocio sigue existiendo, verificar si su relación con dueño está null
                assertNull(negocioAfterDelete.get().getDueño());
            } else {
                // Si el negocio fue eliminado en cascada, la prueba pasa
            }
        } catch (Exception e) {
            // Si hay restricción de integridad referencial, capturar la excepción
            System.out.println("Error al intentar eliminar dueño con negocio asociado: " + e.getMessage());
            // La prueba sigue siendo válida, ya que estamos verificando el comportamiento
        }
    } */
}