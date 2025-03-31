package ispp_g2.gastrostock.testEmpleado;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import ispp_g2.gastrostock.empleado.Empleado;
import ispp_g2.gastrostock.empleado.EmpleadoRepository;
import ispp_g2.gastrostock.empleado.EmpleadoService;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.Authorities;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class EmpleadoServiceTest {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @InjectMocks
    private EmpleadoService empleadoService;
    
    private Empleado empleado1;
    private Empleado empleado2;
    private Empleado empleadoInvalido;
    private List<Empleado> empleadosList;
    private Negocio negocio;
    private User user;
    private Authorities authority;

    @BeforeEach
    void setUp() {
        // Crear una autoridad
        authority = new Authorities();
        authority.setId(1);
        authority.setAuthority("EMPLEADO");
        
        // Crear usuario para asociar a empleado
        user = new User();
        user.setId(1);
        user.setUsername("jperez");
        user.setPassword("password123");
        user.setAuthority(authority);
        
        // Crear negocio
        negocio = new Negocio();
        negocio.setId(1);
        negocio.setName("Restaurante Test");
        negocio.setDireccion("Calle Test 123");
        negocio.setCiudad("Sevilla");
        negocio.setPais("Espana");
        negocio.setCodigoPostal("41001");
        negocio.setTokenNegocio(12345);

        // Crear empleado estándar
        empleado1 = new Empleado();
        empleado1.setId(1);
        empleado1.setFirstName("Juan");
        empleado1.setLastName("Pérez");
        empleado1.setEmail("juan.perez@example.com");
        empleado1.setNumTelefono("666111222");
        empleado1.setTokenEmpleado("TOKEN123");
        empleado1.setDescripcion("Camarero principal");
        empleado1.setUser(user);
        empleado1.setNegocio(negocio);
        
        // Crear segundo empleado para pruebas de lista
        empleado2 = new Empleado();
        empleado2.setId(2);
        empleado2.setFirstName("Ana");
        empleado2.setLastName("García");
        empleado2.setEmail("ana.garcia@example.com");
        empleado2.setNumTelefono("666333444");
        empleado2.setTokenEmpleado("TOKEN456");
        empleado2.setDescripcion("Cocinera");
        empleado2.setNegocio(negocio);
        
        // Crear empleado con datos inválidos
        empleadoInvalido = new Empleado();
        empleadoInvalido.setId(3);
        // No se establecen otras propiedades para simular datos inválidos
        
        // Lista de empleados para test
        empleadosList = Arrays.asList(empleado1, empleado2);
    }

    // TESTS PARA saveEmpleado()
    
    @Test
    void testSaveEmpleado_Success() {
        // Arrange
        when(empleadoRepository.save(any(Empleado.class))).thenReturn(empleado1);
        
        // Act
        Empleado result = empleadoService.saveEmpleado(empleado1);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Juan", result.getFirstName());
        assertEquals("Pérez", result.getLastName());
        verify(empleadoRepository).save(empleado1);
    }
    
    @Test
    void testSaveEmpleado_WithInvalidData() {
        // Arrange
        when(empleadoRepository.save(empleadoInvalido)).thenThrow(new IllegalArgumentException("Datos inválidos"));
        
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            empleadoService.saveEmpleado(empleadoInvalido);
        });
        
        assertEquals("Datos inválidos", exception.getMessage());
        verify(empleadoRepository).save(empleadoInvalido);
    }
    
    @Test
    void testSaveEmpleado_Null() {
        // Arrange
        when(empleadoRepository.save(null)).thenThrow(new IllegalArgumentException("Empleado no puede ser null"));
        
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            empleadoService.saveEmpleado(null);
        });
        
        assertEquals("Empleado no puede ser null", exception.getMessage());
        verify(empleadoRepository).save(null);
    }

    // TESTS PARA getAllEmpleados()
    
    @Test
    void testGetAllEmpleados_Success() {
        // Arrange
        when(empleadoRepository.findAll()).thenReturn(empleadosList);
        
        // Act
        List<Empleado> result = empleadoService.getAllEmpleados();
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Juan", result.get(0).getFirstName());
        assertEquals("Ana", result.get(1).getFirstName());
        verify(empleadoRepository).findAll();
    }
    
    @Test
    void testGetAllEmpleados_EmptyList() {
        // Arrange
        when(empleadoRepository.findAll()).thenReturn(Collections.emptyList());
        
        // Act
        List<Empleado> result = empleadoService.getAllEmpleados();
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(empleadoRepository).findAll();
    }
    
    @Test
    void testGetAllEmpleados_ExceptionHandling() {
        // Arrange
        when(empleadoRepository.findAll()).thenThrow(new RuntimeException("Error de base de datos"));
        
        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            empleadoService.getAllEmpleados();
        });
        
        assertEquals("Error de base de datos", exception.getMessage());
        verify(empleadoRepository).findAll();
    }

    // TESTS PARA getEmpleadoById()
    
    @Test
    void testGetEmpleadoById_Success() {
        // Arrange
        when(empleadoRepository.findById(1)).thenReturn(Optional.of(empleado1));
        
        // Act
        Empleado result = empleadoService.getEmpleadoById(1);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Juan", result.getFirstName());
        verify(empleadoRepository).findById(1);
    }
    
    @Test
    void testGetEmpleadoById_NotFound() {
        // Arrange
        when(empleadoRepository.findById(999)).thenReturn(Optional.empty());
        
        // Act
        Empleado result = empleadoService.getEmpleadoById(999);
        
        // Assert
        assertNull(result);
        verify(empleadoRepository).findById(999);
    }
    
    @Test
    void testGetEmpleadoById_NullId() {
        // Arrange
        when(empleadoRepository.findById(null)).thenThrow(new IllegalArgumentException("ID no puede ser null"));
        
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            empleadoService.getEmpleadoById(null);
        });
        
        assertEquals("ID no puede ser null", exception.getMessage());
        verify(empleadoRepository).findById(null);
    }

    // TESTS PARA deleteEmpleado()
    
    @Test
    void testDeleteEmpleado_Success() {
        // Arrange
        doNothing().when(empleadoRepository).deleteById(1);
        
        // Act
        empleadoService.deleteEmpleado(1);
        
        // Assert
        verify(empleadoRepository).deleteById(1);
    }
    
    @Test
    void testDeleteEmpleado_NotFound() {
        // Arrange
        doThrow(new RuntimeException("Empleado no encontrado")).when(empleadoRepository).deleteById(999);
        
        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            empleadoService.deleteEmpleado(999);
        });
        
        assertEquals("Empleado no encontrado", exception.getMessage());
        verify(empleadoRepository).deleteById(999);
    }
    
    @Test
    void testDeleteEmpleado_NullId() {
        // Arrange
        doThrow(new IllegalArgumentException("ID no puede ser null")).when(empleadoRepository).deleteById(null);
        
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            empleadoService.deleteEmpleado(null);
        });
        
        assertEquals("ID no puede ser null", exception.getMessage());
        verify(empleadoRepository).deleteById(null);
    }

    // TESTS PARA getEmpleadoByEmail()
    
    @Test
    void testGetEmpleadoByEmail_Success() {
        // Arrange
        when(empleadoRepository.findByEmail("juan.perez@example.com")).thenReturn(Optional.of(empleado1));
        
        // Act
        Empleado result = empleadoService.getEmpleadoByEmail("juan.perez@example.com");
        
        // Assert
        assertNotNull(result);
        assertEquals("juan.perez@example.com", result.getEmail());
        verify(empleadoRepository).findByEmail("juan.perez@example.com");
    }
    
    @Test
    void testGetEmpleadoByEmail_NotFound() {
        // Arrange
        when(empleadoRepository.findByEmail("noexiste@example.com")).thenReturn(Optional.empty());
        
        // Act
        Empleado result = empleadoService.getEmpleadoByEmail("noexiste@example.com");
        
        // Assert
        assertNull(result);
        verify(empleadoRepository).findByEmail("noexiste@example.com");
    }
    
    @Test
    void testGetEmpleadoByEmail_NullEmail() {
        // Arrange
        when(empleadoRepository.findByEmail(null)).thenThrow(new IllegalArgumentException("Email no puede ser null"));
        
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            empleadoService.getEmpleadoByEmail(null);
        });
        
        assertEquals("Email no puede ser null", exception.getMessage());
        verify(empleadoRepository).findByEmail(null);
    }

    // TESTS PARA getEmpleadoByNombre()
    
    @Test
    void testGetEmpleadoByNombre_Success() {
        // Arrange
        when(empleadoRepository.findByNombre("Juan")).thenReturn(Collections.singletonList(empleado1));
        
        // Act
        List<Empleado> result = empleadoService.getEmpleadoByNombre("Juan");
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Juan", result.get(0).getFirstName());
        verify(empleadoRepository).findByNombre("Juan");
    }
    
    @Test
    void testGetEmpleadoByNombre_NotFound() {
        // Arrange
        when(empleadoRepository.findByNombre("NoExiste")).thenReturn(Collections.emptyList());
        
        // Act
        List<Empleado> result = empleadoService.getEmpleadoByNombre("NoExiste");
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(empleadoRepository).findByNombre("NoExiste");
    }
    
    @Test
    void testGetEmpleadoByNombre_NullNombre() {
        // Arrange
        when(empleadoRepository.findByNombre(null)).thenThrow(new IllegalArgumentException("Nombre no puede ser null"));
        
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            empleadoService.getEmpleadoByNombre(null);
        });
        
        assertEquals("Nombre no puede ser null", exception.getMessage());
        verify(empleadoRepository).findByNombre(null);
    }

    // TESTS PARA getEmpleadoByApellido()
    
    @Test
    void testGetEmpleadoByApellido_Success() {
        // Arrange
        when(empleadoRepository.findByApellido("Pérez")).thenReturn(Collections.singletonList(empleado1));
        
        // Act
        List<Empleado> result = empleadoService.getEmpleadoByApellido("Pérez");
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Pérez", result.get(0).getLastName());
        verify(empleadoRepository).findByApellido("Pérez");
    }
    
    @Test
    void testGetEmpleadoByApellido_NotFound() {
        // Arrange
        when(empleadoRepository.findByApellido("NoExiste")).thenReturn(Collections.emptyList());
        
        // Act
        List<Empleado> result = empleadoService.getEmpleadoByApellido("NoExiste");
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(empleadoRepository).findByApellido("NoExiste");
    }
    
    @Test
    void testGetEmpleadoByApellido_NullApellido() {
        // Arrange
        when(empleadoRepository.findByApellido(null)).thenThrow(new IllegalArgumentException("Apellido no puede ser null"));
        
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            empleadoService.getEmpleadoByApellido(null);
        });
        
        assertEquals("Apellido no puede ser null", exception.getMessage());
        verify(empleadoRepository).findByApellido(null);
    }

    // TESTS PARA getEmpleadoByTelefono()
    
    @Test
    void testGetEmpleadoByTelefono_Success() {
        // Arrange
        when(empleadoRepository.findByTelefono("666111222")).thenReturn(Optional.of(empleado1));
        
        // Act
        Empleado result = empleadoService.getEmpleadoByTelefono("666111222");
        
        // Assert
        assertNotNull(result);
        assertEquals("666111222", result.getNumTelefono());
        verify(empleadoRepository).findByTelefono("666111222");
    }
    
    @Test
    void testGetEmpleadoByTelefono_NotFound() {
        // Arrange
        when(empleadoRepository.findByTelefono("999999999")).thenReturn(Optional.empty());
        
        // Act
        Empleado result = empleadoService.getEmpleadoByTelefono("999999999");
        
        // Assert
        assertNull(result);
        verify(empleadoRepository).findByTelefono("999999999");
    }
    
    @Test
    void testGetEmpleadoByTelefono_NullTelefono() {
        // Arrange
        when(empleadoRepository.findByTelefono(null)).thenThrow(new IllegalArgumentException("Teléfono no puede ser null"));
        
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            empleadoService.getEmpleadoByTelefono(null);
        });
        
        assertEquals("Teléfono no puede ser null", exception.getMessage());
        verify(empleadoRepository).findByTelefono(null);
    }

    // TESTS PARA getEmpleadoByNegocio()
    
    @Test
    void testGetEmpleadoByNegocio_Success() {
        // Arrange
        when(empleadoRepository.findByNegocio(1)).thenReturn(Arrays.asList(empleado1, empleado2));
        
        // Act
        List<Empleado> result = empleadoService.getEmpleadoByNegocio(1);
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(empleadoRepository).findByNegocio(1);
    }
    
    @Test
    void testGetEmpleadoByNegocio_NotFound() {
        // Arrange
        when(empleadoRepository.findByNegocio(999)).thenReturn(Collections.emptyList());
        
        // Act
        List<Empleado> result = empleadoService.getEmpleadoByNegocio(999);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(empleadoRepository).findByNegocio(999);
    }
    
    @Test
    void testGetEmpleadoByNegocio_NullId() {
        // Arrange
        when(empleadoRepository.findByNegocio(null)).thenThrow(new IllegalArgumentException("ID de negocio no puede ser null"));
        
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            empleadoService.getEmpleadoByNegocio(null);
        });
        
        assertEquals("ID de negocio no puede ser null", exception.getMessage());
        verify(empleadoRepository).findByNegocio(null);
    }

    // TESTS PARA getEmpleadoByUser()
    
    @Test
    void testGetEmpleadoByUser_Success() {
        // Arrange
        when(empleadoRepository.findByUserId(1)).thenReturn(Optional.of(empleado1));
        
        // Act
        Empleado result = empleadoService.getEmpleadoByUser(1);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.getUser().getId());
        verify(empleadoRepository).findByUserId(1);
    }
    
    @Test
    void testGetEmpleadoByUser_NotFound() {
        // Arrange
        when(empleadoRepository.findByUserId(999)).thenReturn(Optional.empty());
        
        // Act
        Empleado result = empleadoService.getEmpleadoByUser(999);
        
        // Assert
        assertNull(result);
        verify(empleadoRepository).findByUserId(999);
    }
    
    @Test
    void testGetEmpleadoByUser_NullUserId() {
        // Arrange
        when(empleadoRepository.findByUserId(null)).thenThrow(new IllegalArgumentException("User ID no puede ser null"));
        
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            empleadoService.getEmpleadoByUser(null);
        });
        
        assertEquals("User ID no puede ser null", exception.getMessage());
        verify(empleadoRepository).findByUserId(null);
    }

    // TESTS PARA getEmpleadoByTokenEmpleado()
    
    @Test
    void testGetEmpleadoByTokenEmpleado_Success() {
        // Arrange
        when(empleadoRepository.findByTokenEmpleado("TOKEN123")).thenReturn(Optional.of(empleado1));
        
        // Act
        Empleado result = empleadoService.getEmpleadoByTokenEmpleado("TOKEN123");
        
        // Assert
        assertNotNull(result);
        assertEquals("TOKEN123", result.getTokenEmpleado());
        verify(empleadoRepository).findByTokenEmpleado("TOKEN123");
    }
    
    @Test
    void testGetEmpleadoByTokenEmpleado_NotFound() {
        // Arrange
        when(empleadoRepository.findByTokenEmpleado("UNKNOWN_TOKEN")).thenReturn(Optional.empty());
        
        // Act
        Empleado result = empleadoService.getEmpleadoByTokenEmpleado("UNKNOWN_TOKEN");
        
        // Assert
        assertNull(result);
        verify(empleadoRepository).findByTokenEmpleado("UNKNOWN_TOKEN");
    }
    
    @Test
    void testGetEmpleadoByTokenEmpleado_NullToken() {
        // Arrange
        when(empleadoRepository.findByTokenEmpleado(null)).thenThrow(new IllegalArgumentException("Token no puede ser null"));
        
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            empleadoService.getEmpleadoByTokenEmpleado(null);
        });
        
        assertEquals("Token no puede ser null", exception.getMessage());
        verify(empleadoRepository).findByTokenEmpleado(null);
    }
}