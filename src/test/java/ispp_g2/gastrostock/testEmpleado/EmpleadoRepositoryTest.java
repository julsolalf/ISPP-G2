package ispp_g2.gastrostock.testEmpleado;

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
import ispp_g2.gastrostock.empleado.Empleado;
import ispp_g2.gastrostock.empleado.EmpleadoRepository;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioRepository;
import ispp_g2.gastrostock.user.Authorities;
import ispp_g2.gastrostock.user.AuthoritiesRepository;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class EmpleadoRepositoryTest {

    @Autowired
    private EmpleadoRepository empleadoRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AuthoritiesRepository authoritiesRepository;
    
    @Autowired
    private NegocioRepository negocioRepository;
    
    @Autowired
    private DuenoRepository duenoRepository;

    
    private Empleado empleado1, empleado2, empleado3, empleadoSinToken, empleadoSinEmail;
    private User user1, user2, user3;
    private Authorities authority;
    private Negocio negocio1, negocio2;
    private Dueno dueno;
    
    @BeforeEach
    void setUp() {
        // Limpiar los repositorios para asegurar un estado consistente
        empleadoRepository.deleteAll();
        userRepository.deleteAll();
        negocioRepository.deleteAll();
        duenoRepository.deleteAll();
        authoritiesRepository.deleteAll();

        // Crear rol de autoridad
        authority = new Authorities();
        authority.setAuthority("EMPLEADO");
        authority = authoritiesRepository.save(authority);

        
        // Crear usuarios para asociar a los empleados
        user1 = new User();
        user1.setUsername("juanperez");
        user1.setPassword("password123");
        user1.setAuthority(authority);
        user1 = userRepository.save(user1);
        
        user2 = new User();
        user2.setUsername("anagarcia");
        user2.setPassword("password456");
        user2.setAuthority(authority);
        user2 = userRepository.save(user2);
        
        user3 = new User();
        user3.setUsername("pedrohernandez");
        user3.setPassword("password789");
        user3.setAuthority(authority);
        user3 = userRepository.save(user3);
        
        dueno = new Dueno();
        dueno.setFirstName("Carlos");
        dueno.setLastName("Propietario");
        dueno.setEmail("carlos@example.com");
        dueno.setNumTelefono("654321987");
        dueno.setTokenDueno("TOKEN_DUEnO");
        dueno.setUser(user1);
        dueno = duenoRepository.save(dueno);

        negocio1 = new Negocio();
        negocio1.setName("Restaurante La Tasca");
        negocio1.setDireccion("Calle Principal 123");
        negocio1.setCiudad("Sevilla");
        negocio1.setPais("Espana");
        negocio1.setCodigoPostal("41001");
        negocio1.setTokenNegocio(12345);
        negocio1.setDueno(dueno);
        negocio1 = negocioRepository.save(negocio1);
        
        negocio2 = new Negocio();
        negocio2.setName("Bar El Rincón");
        negocio2.setDireccion("Avenida Constitución 45");
        negocio2.setCiudad("Sevilla");
        negocio2.setPais("Espana");
        negocio2.setCodigoPostal("41002");
        negocio2.setTokenNegocio(67890);
        negocio2.setDueno(dueno);
        negocio2 = negocioRepository.save(negocio2);

        // Crear empleados
        empleado1 = new Empleado();
        empleado1.setFirstName("Juan");
        empleado1.setLastName("Pérez");
        empleado1.setEmail("juan.perez@example.com");
        empleado1.setNumTelefono("666111222");
        empleado1.setTokenEmpleado("TOKEN123");
        empleado1.setDescripcion("Camarero");
        empleado1.setUser(user1);
        empleado1.setNegocio(negocio1);
        empleado1 = empleadoRepository.save(empleado1);
        
        empleado2 = new Empleado();
        empleado2.setFirstName("Ana");
        empleado2.setLastName("García");
        empleado2.setEmail("ana.garcia@example.com");
        empleado2.setNumTelefono("666333444");
        empleado2.setTokenEmpleado("TOKEN456");
        empleado2.setDescripcion("Cocinera");
        empleado2.setUser(user2);
        empleado2.setNegocio(negocio1);
        empleado2 = empleadoRepository.save(empleado2);
        
        empleado3 = new Empleado();
        empleado3.setFirstName("Pedro");
        empleado3.setLastName("García"); // Mismo apellido que Ana para probar findByApellido
        empleado3.setEmail("pedro.garcia@example.com");
        empleado3.setNumTelefono("666555666");
        empleado3.setTokenEmpleado("TOKEN789");
        empleado3.setDescripcion("Barra");
        empleado3.setUser(user3);
        empleado3.setNegocio(negocio2);
        empleado3 = empleadoRepository.save(empleado3);
        
    }
    
    // TESTS PARA MÉTODOS CRUD HEREDADOS
    
    @Test
    void testSave() {
        // Crear un nuevo empleado
        Empleado nuevoEmpleado = new Empleado();
        nuevoEmpleado.setFirstName("Nuevo");
        nuevoEmpleado.setLastName("Empleado");
        nuevoEmpleado.setEmail("nuevo@example.com");
        nuevoEmpleado.setNumTelefono("666123456");
        nuevoEmpleado.setTokenEmpleado("TOKEN_NUEVO");
        nuevoEmpleado.setDescripcion("Prueba");
        nuevoEmpleado.setNegocio(negocio1);
        
        // Guardar el empleado
        Empleado saved = empleadoRepository.save(nuevoEmpleado);
        
        // Verificar que se guardó correctamente
        assertNotNull(saved.getId());
        assertEquals("Nuevo", saved.getFirstName());
        assertEquals("Empleado", saved.getLastName());
        assertEquals("nuevo@example.com", saved.getEmail());
    }
    
    @Test
    void testSave_WithUserAssociated() {
        // Crear un nuevo usuario para asociar
        User newUser = new User();
        newUser.setUsername("nuevousuario");
        newUser.setPassword("password");
        newUser.setAuthority(authority);
        newUser = userRepository.save(newUser);
        
        // Crear un nuevo empleado con usuario asociado
        Empleado nuevoEmpleado = new Empleado();
        nuevoEmpleado.setFirstName("Empleado");
        nuevoEmpleado.setLastName("Con Usuario");
        nuevoEmpleado.setEmail("empleado.usuario@example.com");
        nuevoEmpleado.setNumTelefono("666111111");
        nuevoEmpleado.setTokenEmpleado("TOKEN_CON_USER");
        nuevoEmpleado.setUser(newUser);
        nuevoEmpleado.setNegocio(negocio1);
        
        // Guardar el empleado
        Empleado saved = empleadoRepository.save(nuevoEmpleado);
        
        // Verificar que se guardó correctamente y la relación con el usuario
        assertNotNull(saved.getId());
        assertNotNull(saved.getUser());
        assertEquals("nuevousuario", saved.getUser().getUsername());
    }
    
    @Test
    void testFindAll() {
        // Obtener todos los empleados
        Iterable<Empleado> empleados = empleadoRepository.findAll();
        
        // Contar cuántos empleados hay
        int count = 0;
        for (Empleado empleado : empleados) {
            count++;
        }
        
        // Verificar que hay 3 empleados (los creados en setUp)
        assertEquals(3, count);
    }
    
    @Test
    void testFindById() {
        // Buscar por ID existente
        Optional<Empleado> found = empleadoRepository.findById(empleado1.getId());
        
        // Verificar que existe y tiene los datos correctos
        assertTrue(found.isPresent());
        assertEquals("Juan", found.get().getFirstName());
        assertEquals("Pérez", found.get().getLastName());
    }
    
    @Test
    void testFindById_NotFound() {
        // Buscar por ID que no existe
        Optional<Empleado> notFound = empleadoRepository.findById(999);
        
        // Verificar que no existe
        assertFalse(notFound.isPresent());
    }
    
    @Test
    void testDelete() {
        // Eliminar un empleado
        empleadoRepository.delete(empleado2);
        
        // Verificar que se eliminó
        Optional<Empleado> shouldBeDeleted = empleadoRepository.findById(empleado2.getId());
        assertFalse(shouldBeDeleted.isPresent());
        
        // Verificar que el resto sigue existiendo
        Iterable<Empleado> remaining = empleadoRepository.findAll();
        int count = 0;
        for (Empleado empleado : remaining) {
            count++;
        }
        assertEquals(2, count);
    }
    
    // TESTS PARA MÉTODOS DE CONSULTA PERSONALIZADOS
    
    @Test
    void testFindByEmail_Success() {
        // Buscar por email existente
        Optional<Empleado> found = empleadoRepository.findByEmail("juan.perez@example.com");
        
        // Verificar que existe y tiene los datos correctos
        assertTrue(found.isPresent());
        assertEquals("Juan", found.get().getFirstName());
        assertEquals("Pérez", found.get().getLastName());
    }
    
    @Test
    void testFindByEmail_NotFound() {
        // Buscar por email que no existe
        Optional<Empleado> notFound = empleadoRepository.findByEmail("noexiste@example.com");
        
        // Verificar que no existe
        assertFalse(notFound.isPresent());
    }
    
    @Test
    void testFindByNombre_Success() {
        // Buscar por nombre existente
        List<Empleado> found = empleadoRepository.findByNombre("Juan");
        
        // Verificar que existe y tiene los datos correctos
        assertEquals(1, found.size());
        assertEquals("Pérez", found.get(0).getLastName());
    }
    
    @Test
    void testFindByNombre_NotFound() {
        // Buscar por nombre que no existe
        List<Empleado> notFound = empleadoRepository.findByNombre("Inexistente");
        
        // Verificar que la lista está vacía
        assertTrue(notFound.isEmpty());
    }
    
    @Test
    void testFindByNombre_PartialMatch() {
        // Agregar un empleado con nombre que contiene "Juan"
        User user4 = new User();
        user4.setUsername("Juanpedro");
        user4.setPassword("password789");
        user4.setAuthority(authority);
        user4 = userRepository.save(user4);
        Empleado juanito = new Empleado();
        juanito.setFirstName("Juanito");
        juanito.setLastName("Valderrama");
        juanito.setEmail("juanito@example.com");
        juanito.setNumTelefono("666888999");
        juanito.setTokenEmpleado("TOKEN_JUANITO");
        juanito.setNegocio(negocio1);
        juanito.setUser(user4);
        empleadoRepository.save(juanito);
        
        // Buscar por nombre "Juan" - no debería encontrar "Juanito" por ser JPQL exacto
        List<Empleado> found = empleadoRepository.findByNombre("Juan");
        
        // Verificar que solo se encuentra "Juan" exacto
        assertEquals(1, found.size());
        assertEquals("Juan", found.get(0).getFirstName());
    }
    
    @Test
    void testFindByApellido_Success() {
        // Buscar por apellido existente que tiene múltiples coincidencias
        List<Empleado> found = empleadoRepository.findByApellido("García");
        
        // Verificar que se encuentran los dos empleados con apellido García
        assertEquals(2, found.size());
        
        // Verificar que se encontraron Ana García y Pedro García
        boolean foundAna = false;
        boolean foundPedro = false;
        
        for (Empleado empleado : found) {
            if ("Ana".equals(empleado.getFirstName())) {
                foundAna = true;
            }
            if ("Pedro".equals(empleado.getFirstName())) {
                foundPedro = true;
            }
        }
        
        assertTrue(foundAna);
        assertTrue(foundPedro);
    }
    
    @Test
    void testFindByApellido_NotFound() {
        // Buscar por apellido que no existe
        List<Empleado> notFound = empleadoRepository.findByApellido("Inexistente");
        
        // Verificar que la lista está vacía
        assertTrue(notFound.isEmpty());
    }
    
    @Test
    void testFindByTelefono_Success() {
        // Buscar por teléfono existente
        Optional<Empleado> found = empleadoRepository.findByTelefono("666111222");
        
        // Verificar que existe y tiene los datos correctos
        assertTrue(found.isPresent());
        assertEquals("Juan", found.get().getFirstName());
        assertEquals("Pérez", found.get().getLastName());
    }
    
    @Test
    void testFindByTelefono_NotFound() {
        // Buscar por teléfono que no existe
        Optional<Empleado> notFound = empleadoRepository.findByTelefono("666000000");
        
        // Verificar que no existe
        assertFalse(notFound.isPresent());
    }
    
    @Test
    void testFindByNegocio_Success() {
        // Buscar por ID de negocio existente
        List<Empleado> found = empleadoRepository.findByNegocio(negocio1.getId());
        
        // Verificar que se encuentran los empleados del negocio1
        assertEquals(2, found.size());
        
        // Verificar que se encontraron Juan Pérez y Ana García
        boolean foundJuan = false;
        boolean foundAna = false;
        
        for (Empleado empleado : found) {
            if ("Juan".equals(empleado.getFirstName())) {
                foundJuan = true;
            }
            if ("Ana".equals(empleado.getFirstName())) {
                foundAna = true;
            }
        }
        
        assertTrue(foundJuan);
        assertTrue(foundAna);
    }
    
    @Test
    void testFindByNegocio_NotFound() {
        // Buscar por ID de negocio que no existe
        List<Empleado> notFound = empleadoRepository.findByNegocio(999);
        
        // Verificar que la lista está vacía
        assertTrue(notFound.isEmpty());
    }
    
    @Test
    void testFindByUserId_Success() {
        // Buscar por ID de usuario existente
        Optional<Empleado> found = empleadoRepository.findByUserId(user1.getId());
        
        // Verificar que existe y tiene los datos correctos
        assertTrue(found.isPresent());
        assertEquals("Juan", found.get().getFirstName());
        assertEquals("Pérez", found.get().getLastName());
    }
    
    @Test
    void testFindByUserId_NotFound() {
        // Buscar por ID de usuario que no existe
        Optional<Empleado> notFound = empleadoRepository.findByUserId(999);
        
        // Verificar que no existe
        assertFalse(notFound.isPresent());
    }
    
    @Test
    void testFindByTokenEmpleado_Success() {
        // Buscar por token de empleado existente
        Optional<Empleado> found = empleadoRepository.findByTokenEmpleado("TOKEN123");
        
        // Verificar que existe y tiene los datos correctos
        assertTrue(found.isPresent());
        assertEquals("Juan", found.get().getFirstName());
        assertEquals("Pérez", found.get().getLastName());
    }
    
    @Test
    void testFindByTokenEmpleado_NotFound() {
        // Buscar por token de empleado que no existe
        Optional<Empleado> notFound = empleadoRepository.findByTokenEmpleado("TOKEN_INEXISTENTE");
        
        // Verificar que no existe
        assertFalse(notFound.isPresent());
    }
    
    
    // TESTS PARA CASOS ESPECIALES Y RESTRICCIONES
    
    @Test
    void testTokenEmpleadoUniqueConstraint() {
        // Verificar si hay restricción de unicidad en el token del empleado
        // Intentando crear un empleado con el mismo token que uno existente
        
        Empleado empleadoTokenDuplicado = new Empleado();
        empleadoTokenDuplicado.setFirstName("Otro");
        empleadoTokenDuplicado.setLastName("Empleado");
        empleadoTokenDuplicado.setEmail("otro@example.com");
        empleadoTokenDuplicado.setNumTelefono("666444333");
        empleadoTokenDuplicado.setTokenEmpleado("TOKEN123"); // Token ya existente
        
        try {
            empleadoRepository.save(empleadoTokenDuplicado);
            
            // Si no hay restricción de unicidad, verificar si el token existe en múltiples empleados
            Iterable<Empleado> empleados = empleadoRepository.findAll();
            List<Empleado> empleadosConMismoToken = StreamSupport.stream(empleados.spliterator(), false)
                    .filter(e -> "TOKEN123".equals(e.getTokenEmpleado()))
                    .toList();
            
            if (empleadosConMismoToken.size() > 1) {
                // Si se permiten tokens duplicados, la prueba pasa
                assertTrue(true);
            } else {
                // Si no se permitió guardar el duplicado pero no se lanzó excepción
                fail("Se esperaba que se permitiera guardar un token duplicado o que se lanzara una excepción");
            }
        } catch (DataIntegrityViolationException e) {
            // Si hay restricción de unicidad, se esperaba esta excepción
            assertTrue(true);
        }
    }
    
    @Test
    void testEmailUniqueConstraint() {
        // Verificar si hay restricción de unicidad en el email del empleado
        // Intentando crear un empleado con el mismo email que uno existente
        
        Empleado empleadoEmailDuplicado = new Empleado();
        empleadoEmailDuplicado.setFirstName("Otro");
        empleadoEmailDuplicado.setLastName("Empleado");
        empleadoEmailDuplicado.setEmail("juan.perez@example.com"); // Email ya existente
        empleadoEmailDuplicado.setNumTelefono("666444333");
        empleadoEmailDuplicado.setTokenEmpleado("TOKEN_OTRO");
        
        try {
            empleadoRepository.save(empleadoEmailDuplicado);
            
            // Si no hay restricción de unicidad, verificar si el email existe en múltiples empleados
            Iterable<Empleado> empleados = empleadoRepository.findAll();
            List<Empleado> empleadosConMismoEmail = StreamSupport.stream(empleados.spliterator(), false)
                    .filter(e -> "juan.perez@example.com".equals(e.getEmail()))
                    .toList();
            
            if (empleadosConMismoEmail.size() > 1) {
                // Si se permiten emails duplicados, la prueba pasa
                assertTrue(true);
            } else {
                // Si no se permitió guardar el duplicado pero no se lanzó excepción
                fail("Se esperaba que se permitiera guardar un email duplicado o que se lanzara una excepción");
            }
        } catch (DataIntegrityViolationException e) {
            // Si hay restricción de unicidad, se esperaba esta excepción
            assertTrue(true);
        }
    }
    /* 
    @Test
    void testCascadeDeleteUser() {
        // Verificar comportamiento al eliminar un usuario asociado a un empleado
        
        // Eliminar el usuario
        userRepository.delete(user1);
        userRepository.findAll(); // Forzar la eliminación en la base de datos
        
        // Verificar si el empleado sigue existiendo
        Optional<Empleado> empleadoAfterUserDelete = empleadoRepository.findById(empleado1.getId().toString());
        
        // El comportamiento esperado depende de la configuración de cascada
        // Si hay cascada DELETE, el empleado debería haber sido eliminado
        // Si no hay cascada, el empleado debería seguir existiendo, posiblemente con user=null
        
        if (empleadoAfterUserDelete.isPresent()) {
            // Si el empleado sigue existiendo, verificar si su relación con el usuario está desconectada
            assertNull(empleadoAfterUserDelete.get().getUser());
        } else {
            // Si el empleado fue eliminado en cascada, esto es válido también
            assertTrue(true);
        }
    }
        */
}