package ispp_g2.gastrostock.testUser;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import ispp_g2.gastrostock.user.Authorities;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserRepository;
import ispp_g2.gastrostock.user.AuthoritiesRepository;


@DataJpaTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AuthoritiesRepository authoritiesRepository;
    
    private Authorities authorityUser;
    private Authorities authorityAdmin;
    private User user1;
    private User user2;
    
    @BeforeEach
    void setUp() {
        // Limpiar repositorios
        userRepository.deleteAll();
        authoritiesRepository.deleteAll();
        
        // Crear autoridades
        authorityUser = new Authorities();
        authorityUser.setAuthority("user");
        authorityUser = authoritiesRepository.save(authorityUser);
        
        authorityAdmin = new Authorities();
        authorityAdmin.setAuthority("admin");
        authorityAdmin = authoritiesRepository.save(authorityAdmin);
        
        // Crear y guardar usuarios
        user1 = new User();
        user1.setUsername("john");
        user1.setPassword("secret");
        user1.setAuthority(authorityUser);
        user1 = userRepository.save(user1);
        
        user2 = new User();
        user2.setUsername("adminUser");
        user2.setPassword("adminPass");
        user2.setAuthority(authorityAdmin);
        user2 = userRepository.save(user2);
    }
    

    
    @Test
    void testFindByUsername_Success() {
        User found = userRepository.findByUsername("john").orElse(null);
        assertNotNull(found, "El usuario debería existir.");
        assertEquals("john", found.getUsername());
        assertEquals("user", found.getAuthority().getAuthority());
    }
    
    @Test
    void testFindByUsername_NotFound() {
        User notFound = userRepository.findByUsername("nonexistent").orElse(null);
        assertNull(notFound, "No se debería encontrar un usuario inexistente.");
    }
    
    @Test
    void testFindByUsernameAndPassword_Success() {
        User found = userRepository.findByUsernameAndPassword("john", "secret");
        assertNotNull(found, "El usuario y la contraseña deben coincidir.");
        assertEquals("john", found.getUsername());
        assertEquals("secret", found.getPassword());
    }
    
    @Test
    void testFindByUsernameAndPassword_NotFound() {
        User notFound = userRepository.findByUsernameAndPassword("john", "wrongPassword");
        assertNull(notFound, "No se debería encontrar usuario con contraseña incorrecta.");
    }
    
    @Test
    void testFindByAuthority_Success() {
       
        User found = userRepository.findByAuthority("admin");
        assertNotNull(found, "Debe encontrar un usuario con autoridad admin.");
        assertEquals("adminUser", found.getUsername());
        assertEquals("admin", found.getAuthority().getAuthority());
    }
    
    @Test
    void testFindByAuthority_NotFound() {
        User notFound = userRepository.findByAuthority("nonexistent");
        assertNull(notFound, "No se debería encontrar usuario con autoridad inexistente.");
    }
    
    @Test
    void testFindUserByUsername_Success() {
        Optional<User> optUser = userRepository.findUserByUsername("john");
        assertTrue(optUser.isPresent(), "El Optional debería contener un usuario existente.");
        assertEquals("john", optUser.get().getUsername());
    }
    
    @Test
    void testFindUserByUsername_NotFound() {
        Optional<User> optUser = userRepository.findUserByUsername("nonexistent");
        assertFalse(optUser.isPresent(), "El Optional no debería contener un usuario inexistente.");
    }
    
    
    @Test
    void testSaveUser() {
        User newUser = new User();
        newUser.setUsername("mary");
        newUser.setPassword("maryPass");
        newUser.setAuthority(authorityUser);
        
        User saved = userRepository.save(newUser);
        assertNotNull(saved.getId(), "El ID no debe ser nulo después del guardado.");
        assertEquals("mary", saved.getUsername());
        
        Optional<User> retrieved = userRepository.findById(saved.getId());
        assertTrue(retrieved.isPresent(), "El usuario debe existir en la BD.");
        assertEquals("mary", retrieved.get().getUsername());
    }
    
    @Test
    void testUpdateUser() {
        user1.setPassword("newSecret");
        User updated = userRepository.save(user1);
        assertEquals("newSecret", updated.getPassword(), "La contraseña debe actualizarse correctamente.");
        
        Optional<User> retrieved = userRepository.findById(user1.getId());
        assertTrue(retrieved.isPresent(), "El usuario actualizado debe existir.");
        assertEquals("newSecret", retrieved.get().getPassword());
    }
    
    @SuppressWarnings("unused")
    @Test
    void testFindAllUsers() {
        // Recuperar todos los usuarios
        Iterable<User> allUsers = userRepository.findAll();
        int count = 0;
        for (User u : allUsers) {
            count++;
        }
        assertTrue(count >= 2, "Se deben recuperar al menos 2 usuarios.");
    }
    
    @Test
    void testCountUsers() {
        long count = userRepository.count();
        assertEquals(2, count, "El count debe coincidir con la cantidad de usuarios insertados en setUp.");
    }
    
    @Test
    void testDeleteUser() {
        userRepository.delete(user2);
        Optional<User> retrieved = userRepository.findById(user2.getId());
        assertFalse(retrieved.isPresent(), "El usuario eliminado no debe existir.");
    }
    
    @Test
    void testDeleteById() {
        // Eliminar usuario por ID
        Integer id = user1.getId();
        userRepository.deleteById(id);
        Optional<User> retrieved = userRepository.findById(id);
        assertFalse(retrieved.isPresent(), "El usuario eliminado por ID no debe existir.");
    }
    
    @Test
    void testCrudRepositoryOperationsSequence() {
        // Comprueba la secuencia de operaciones: count, save, update, delete
        long initialCount = userRepository.count();
        
        // Guardar un nuevo usuario
        User newUser = new User();
        newUser.setUsername("carlos");
        newUser.setPassword("carlosPass");
        newUser.setAuthority(authorityAdmin);
        newUser = userRepository.save(newUser);
        assertNotNull(newUser.getId());
        assertEquals(initialCount + 1, userRepository.count());
        
        // Actualizar el usuario
        newUser.setPassword("nuevoCarlosPass");
        newUser = userRepository.save(newUser);
        Optional<User> optUpdated = userRepository.findById(newUser.getId());
        assertTrue(optUpdated.isPresent());
        assertEquals("nuevoCarlosPass", optUpdated.get().getPassword());
        
        // Eliminar el usuario
        userRepository.delete(newUser);
        assertEquals(initialCount, userRepository.count());
    }
    

    
    @Test
    void testFindByUsernameWithNull() {
        User found = userRepository.findByUsername(null).orElse(null);
        assertNull(found, "Pasar null debería devolver null o arrojar excepción (según implementación).");
    }
    
    @Test
    void testFindByUsernameAndPasswordWithNulls() {
        User found = userRepository.findByUsernameAndPassword(null, null);
        assertNull(found, "Pasar ambos argumentos null debería devolver null o arrojar excepción.");
    }
    
    @Test
    void testFindByAuthorityWithNull() {
        User found = userRepository.findByAuthority(null);
        assertNull(found, "Pasar null en la autoridad debería devolver null o arrojar excepción.");
    }
    
    @Test
    void testFindUserByUsernameWithNull() {
        Optional<User> optUser = userRepository.findUserByUsername(null);
        assertFalse(optUser.isPresent(), "Pasar null debería retornar un Optional vacío.");
    }
}

