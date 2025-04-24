package ispp_g2.gastrostock.testEmpleado;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import ispp_g2.gastrostock.empleado.Empleado;
import ispp_g2.gastrostock.empleado.EmpleadoDTO;
import ispp_g2.gastrostock.empleado.EmpleadoRepository;
import ispp_g2.gastrostock.empleado.EmpleadoService;
import ispp_g2.gastrostock.exceptions.ResourceNotFoundException;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioRepository;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserRepository;
import ispp_g2.gastrostock.user.Authorities;
import ispp_g2.gastrostock.user.AuthoritiesRepository;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class EmpleadoServiceTest {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NegocioRepository negocioRepository;
    
    @Mock
    private AuthoritiesRepository authoritiesRepository;

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
        authority = new Authorities();
        authority.setId(1);
        authority.setAuthority("EMPLEADO");
        
        user = new User();
        user.setId(1);
        user.setUsername("jperez");
        user.setPassword("password123");
        user.setAuthority(authority);
        
        negocio = new Negocio();
        negocio.setId(1);
        negocio.setName("Restaurante Test");
        negocio.setDireccion("Calle Test 123");
        negocio.setCiudad("Sevilla");
        negocio.setPais("Espana");
        negocio.setCodigoPostal("41001");
        negocio.setTokenNegocio(12345);

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
        
        empleado2 = new Empleado();
        empleado2.setId(2);
        empleado2.setFirstName("Ana");
        empleado2.setLastName("García");
        empleado2.setEmail("ana.garcia@example.com");
        empleado2.setNumTelefono("666333444");
        empleado2.setTokenEmpleado("TOKEN456");
        empleado2.setDescripcion("Cocinera");
        empleado2.setNegocio(negocio);
        
        empleadoInvalido = new Empleado();
        empleadoInvalido.setId(3);
 
        empleadosList = Arrays.asList(empleado1, empleado2);
    }

    
    @Test
    void testSaveEmpleado_Success() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(empleadoRepository.save(any(Empleado.class))).thenReturn(empleado1);
        
        Empleado result = empleadoService.saveEmpleado(empleado1);
        
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Juan", result.getFirstName());
        verify(empleadoRepository).save(empleado1);
        verify(userRepository).save(any(User.class));
    }
    
    @Test
    void testSaveEmpleado_WithInvalidData() {
        empleadoInvalido.setUser(user); 
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(empleadoRepository.save(empleadoInvalido)).thenThrow(new IllegalArgumentException("Datos inválidos"));
        
        assertThrows(IllegalArgumentException.class, () -> {
            empleadoService.saveEmpleado(empleadoInvalido);
        });
    }
    
    @Test
    void testSaveEmpleado_Null() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            empleadoService.saveEmpleado(null);
        });
        
        assertEquals("No se puede guardar un empleado null", exception.getMessage());
        verify(empleadoRepository, never()).save(any());
        verify(userRepository, never()).save(any());
    }
    
    @Test
    void testGetAllEmpleados_Success() {
        when(empleadoRepository.findAll()).thenReturn(empleadosList);
        
        List<Empleado> result = empleadoService.getAllEmpleados();
        
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Juan", result.get(0).getFirstName());
        assertEquals("Ana", result.get(1).getFirstName());
        verify(empleadoRepository).findAll();
    }
    
    @Test
    void testGetAllEmpleados_EmptyList() {
        when(empleadoRepository.findAll()).thenReturn(Collections.emptyList());
        
        List<Empleado> result = empleadoService.getAllEmpleados();
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(empleadoRepository).findAll();
    }
    
    @Test
    void testGetAllEmpleados_ExceptionHandling() {
        when(empleadoRepository.findAll()).thenThrow(new RuntimeException("Error de base de datos"));
        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            empleadoService.getAllEmpleados();
        });
        
        assertEquals("Error de base de datos", exception.getMessage());
        verify(empleadoRepository).findAll();
    }

    
    @Test
    void testGetEmpleadoById_Success() {
        when(empleadoRepository.findById(1)).thenReturn(Optional.of(empleado1));
        
        Empleado result = empleadoService.getEmpleadoById(1);
        
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Juan", result.getFirstName());
        verify(empleadoRepository).findById(1);
    }
    
    @Test
    void testGetEmpleadoById_NotFound() {
        when(empleadoRepository.findById(999)).thenReturn(Optional.empty());
        
        Empleado result = empleadoService.getEmpleadoById(999);
        
        assertNull(result);
        verify(empleadoRepository).findById(999);
    }
    
    @SuppressWarnings("null")
    @Test
    void testGetEmpleadoById_NullId() {
        when(empleadoRepository.findById(null)).thenThrow(new IllegalArgumentException("ID no puede ser null"));
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            empleadoService.getEmpleadoById(null);
        });
        
        assertEquals("ID no puede ser null", exception.getMessage());
        verify(empleadoRepository).findById(null);
    }

    
    @Test
    void testDeleteEmpleado_Success() {
        doNothing().when(empleadoRepository).deleteById(1);
        
        empleadoService.deleteEmpleado(1);
        
        verify(empleadoRepository).deleteById(1);
    }
    
    @Test
    void testDeleteEmpleado_NotFound() {
        doThrow(new RuntimeException("Empleado no encontrado")).when(empleadoRepository).deleteById(999);
        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            empleadoService.deleteEmpleado(999);
        });
        
        assertEquals("Empleado no encontrado", exception.getMessage());
        verify(empleadoRepository).deleteById(999);
    }
    
    @SuppressWarnings("null")
    @Test
    void testDeleteEmpleado_NullId() {
        doThrow(new IllegalArgumentException("ID no puede ser null")).when(empleadoRepository).deleteById(null);
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            empleadoService.deleteEmpleado(null);
        });
        
        assertEquals("ID no puede ser null", exception.getMessage());
        verify(empleadoRepository).deleteById(null);
    }

    
    @Test
    void testGetEmpleadoByEmail_Success() {
        when(empleadoRepository.findByEmail("juan.perez@example.com")).thenReturn(Optional.of(empleado1));
        
        Empleado result = empleadoService.getEmpleadoByEmail("juan.perez@example.com");
        
        assertNotNull(result);
        assertEquals("juan.perez@example.com", result.getEmail());
        verify(empleadoRepository).findByEmail("juan.perez@example.com");
    }
    
    @Test
    void testGetEmpleadoByEmail_NotFound() {
        when(empleadoRepository.findByEmail("noexiste@example.com")).thenReturn(Optional.empty());
        
        Empleado result = empleadoService.getEmpleadoByEmail("noexiste@example.com");
        
        assertNull(result);
        verify(empleadoRepository).findByEmail("noexiste@example.com");
    }
    
    @Test
    void testGetEmpleadoByEmail_NullEmail() {
        when(empleadoRepository.findByEmail(null)).thenThrow(new IllegalArgumentException("Email no puede ser null"));
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            empleadoService.getEmpleadoByEmail(null);
        });
        
        assertEquals("Email no puede ser null", exception.getMessage());
        verify(empleadoRepository).findByEmail(null);
    }

    
    @Test
    void testGetEmpleadoByNombre_Success() {
        when(empleadoRepository.findByNombre("Juan")).thenReturn(Collections.singletonList(empleado1));
        
        List<Empleado> result = empleadoService.getEmpleadoByNombre("Juan");
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Juan", result.get(0).getFirstName());
        verify(empleadoRepository).findByNombre("Juan");
    }
    
    @Test
    void testGetEmpleadoByNombre_NotFound() {
        when(empleadoRepository.findByNombre("NoExiste")).thenReturn(Collections.emptyList());
        
        List<Empleado> result = empleadoService.getEmpleadoByNombre("NoExiste");
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(empleadoRepository).findByNombre("NoExiste");
    }
    
    @Test
    void testGetEmpleadoByNombre_NullNombre() {
        when(empleadoRepository.findByNombre(null)).thenThrow(new IllegalArgumentException("Nombre no puede ser null"));
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            empleadoService.getEmpleadoByNombre(null);
        });
        
        assertEquals("Nombre no puede ser null", exception.getMessage());
        verify(empleadoRepository).findByNombre(null);
    }

    
    @Test
    void testGetEmpleadoByApellido_Success() {
        when(empleadoRepository.findByApellido("Pérez")).thenReturn(Collections.singletonList(empleado1));
        
        List<Empleado> result = empleadoService.getEmpleadoByApellido("Pérez");
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Pérez", result.get(0).getLastName());
        verify(empleadoRepository).findByApellido("Pérez");
    }
    
    @Test
    void testGetEmpleadoByApellido_NotFound() {
        when(empleadoRepository.findByApellido("NoExiste")).thenReturn(Collections.emptyList());
        
        List<Empleado> result = empleadoService.getEmpleadoByApellido("NoExiste");
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(empleadoRepository).findByApellido("NoExiste");
    }
    
    @Test
    void testGetEmpleadoByApellido_NullApellido() {
        when(empleadoRepository.findByApellido(null)).thenThrow(new IllegalArgumentException("Apellido no puede ser null"));
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            empleadoService.getEmpleadoByApellido(null);
        });
        
        assertEquals("Apellido no puede ser null", exception.getMessage());
        verify(empleadoRepository).findByApellido(null);
    }

    
    @Test
    void testGetEmpleadoByTelefono_Success() {
        when(empleadoRepository.findByTelefono("666111222")).thenReturn(Optional.of(empleado1));
        
        Empleado result = empleadoService.getEmpleadoByTelefono("666111222");
        
        assertNotNull(result);
        assertEquals("666111222", result.getNumTelefono());
        verify(empleadoRepository).findByTelefono("666111222");
    }
    
    @Test
    void testGetEmpleadoByTelefono_NotFound() {
        when(empleadoRepository.findByTelefono("999999999")).thenReturn(Optional.empty());
        
        Empleado result = empleadoService.getEmpleadoByTelefono("999999999");
        
        assertNull(result);
        verify(empleadoRepository).findByTelefono("999999999");
    }
    
    @Test
    void testGetEmpleadoByTelefono_NullTelefono() {
        when(empleadoRepository.findByTelefono(null)).thenThrow(new IllegalArgumentException("Teléfono no puede ser null"));
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            empleadoService.getEmpleadoByTelefono(null);
        });
        
        assertEquals("Teléfono no puede ser null", exception.getMessage());
        verify(empleadoRepository).findByTelefono(null);
    }

    
    @Test
    void testGetEmpleadoByNegocio_Success() {
        when(empleadoRepository.findByNegocio(1)).thenReturn(Arrays.asList(empleado1, empleado2));
        
        List<Empleado> result = empleadoService.getEmpleadoByNegocio(1);
        
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(empleadoRepository).findByNegocio(1);
    }
    
    @Test
    void testGetEmpleadoByNegocio_NotFound() {
        when(empleadoRepository.findByNegocio(999)).thenReturn(Collections.emptyList());
        
        List<Empleado> result = empleadoService.getEmpleadoByNegocio(999);
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(empleadoRepository).findByNegocio(999);
    }
    
    @Test
    void testGetEmpleadoByNegocio_NullId() {
        when(empleadoRepository.findByNegocio(null)).thenThrow(new IllegalArgumentException("ID de negocio no puede ser null"));
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            empleadoService.getEmpleadoByNegocio(null);
        });
        
        assertEquals("ID de negocio no puede ser null", exception.getMessage());
        verify(empleadoRepository).findByNegocio(null);
    }

    
    @Test
    void testGetEmpleadoByUser_Success() {
        when(empleadoRepository.findByUserId(1)).thenReturn(Optional.of(empleado1));
        
        Empleado result = empleadoService.getEmpleadoByUser(1);
        
        assertNotNull(result);
        assertEquals(1, result.getUser().getId());
        verify(empleadoRepository).findByUserId(1);
    }
    
    @Test
    void testGetEmpleadoByUser_NotFound() {
        when(empleadoRepository.findByUserId(999)).thenReturn(Optional.empty());
        
        Empleado result = empleadoService.getEmpleadoByUser(999);
        
        assertNull(result);
        verify(empleadoRepository).findByUserId(999);
    }
    
    @Test
    void testGetEmpleadoByUser_NullUserId() {
        when(empleadoRepository.findByUserId(null)).thenThrow(new IllegalArgumentException("User ID no puede ser null"));
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            empleadoService.getEmpleadoByUser(null);
        });
        
        assertEquals("User ID no puede ser null", exception.getMessage());
        verify(empleadoRepository).findByUserId(null);
    }

    
    @Test
    void testGetEmpleadoByTokenEmpleado_Success() {
        when(empleadoRepository.findByTokenEmpleado("TOKEN123")).thenReturn(Optional.of(empleado1));
        
        Empleado result = empleadoService.getEmpleadoByTokenEmpleado("TOKEN123");
        
        assertNotNull(result);
        assertEquals("TOKEN123", result.getTokenEmpleado());
        verify(empleadoRepository).findByTokenEmpleado("TOKEN123");
    }
    
    @Test
    void testGetEmpleadoByTokenEmpleado_NotFound() {
        when(empleadoRepository.findByTokenEmpleado("UNKNOWN_TOKEN")).thenReturn(Optional.empty());
        
        Empleado result = empleadoService.getEmpleadoByTokenEmpleado("UNKNOWN_TOKEN");
        
        assertNull(result);
        verify(empleadoRepository).findByTokenEmpleado("UNKNOWN_TOKEN");
    }
    
    @Test
    void testGetEmpleadoByTokenEmpleado_NullToken() {
        when(empleadoRepository.findByTokenEmpleado(null)).thenThrow(new IllegalArgumentException("Token no puede ser null"));
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            empleadoService.getEmpleadoByTokenEmpleado(null);
        });
        
        assertEquals("Token no puede ser null", exception.getMessage());
        verify(empleadoRepository).findByTokenEmpleado(null);
    }

    @Test
    void testGetEmpleadoByDueno_Success() {
        when(empleadoRepository.findByDueno(1)).thenReturn(empleadosList);

        List<Empleado> result = empleadoService.getEmpleadoByDueno(1);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(empleadoRepository).findByDueno(1);
    }

    @Test
    void testGetEmpleadoByDueno_Empty() {
        when(empleadoRepository.findByDueno(99)).thenReturn(Collections.emptyList());

        List<Empleado> result = empleadoService.getEmpleadoByDueno(99);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(empleadoRepository).findByDueno(99);
    }

    @Test
    void testConvertirDTOEmpleado_Success() {
        EmpleadoDTO dto = new EmpleadoDTO();
        dto.setUsername("jperez");
        dto.setFirstName("Juan");
        dto.setLastName("Pérez");
        dto.setEmail("juan.perez@example.com");
        dto.setNumTelefono("666111222");
        dto.setDescripcion("Camarero");
        dto.setNegocio(1);

        when(userRepository.findByUsername("jperez")).thenReturn(Optional.of(user));
        when(negocioRepository.findById(1)).thenReturn(Optional.of(negocio));

        Empleado result = empleadoService.convertirDTOEmpleado(dto);

        assertNotNull(result);
        assertEquals("Juan", result.getFirstName());
        assertEquals("Pérez", result.getLastName());
        assertEquals("juan.perez@example.com", result.getEmail());
        assertEquals("666111222", result.getNumTelefono());
        assertEquals("Camarero", result.getDescripcion());
        assertEquals(user, result.getUser());
        assertEquals(negocio, result.getNegocio());
        assertTrue(result.getTokenEmpleado().startsWith("gst-"));
        assertTrue(result.getTokenEmpleado().endsWith("-emp1"));
    }

    @Test
    void testConvertirDTOEmpleado_UserNotFound() {
        EmpleadoDTO dto = new EmpleadoDTO();
        dto.setUsername("noperez");
        dto.setNegocio(1);

        when(userRepository.findByUsername("noperez"))
            .thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> empleadoService.convertirDTOEmpleado(dto)
        );
        assertEquals("Usuario no encontrado", ex.getMessage());
    }

    @Test
    void testConvertirDTOEmpleado_NegocioNotFound() {
        EmpleadoDTO dto = new EmpleadoDTO();
        dto.setUsername("jperez");
        dto.setNegocio(99);

        when(userRepository.findByUsername("jperez")).thenReturn(Optional.of(user));
        when(negocioRepository.findById(99)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> empleadoService.convertirDTOEmpleado(dto)
        );
        assertEquals("Negocio no encontrado", ex.getMessage());
    }

    @Test
    void testConvertirEmpleadoDTO() {
        Empleado input = new Empleado();
        input.setFirstName("Luisa");
        input.setLastName("Martínez");
        input.setEmail("luisa@example.com");
        input.setTokenEmpleado("gst-XYZ-emp2");
        input.setNumTelefono("678123456");
        input.setDescripcion("Barra");
        User u = new User();
        u.setUsername("luisa");
        u.setPassword("pass");
        input.setUser(u);
        Negocio n2 = new Negocio();
        n2.setId(2);
        input.setNegocio(n2);

        EmpleadoDTO dto = empleadoService.convertirEmpleadoDTO(input);

        assertNotNull(dto);
        assertEquals("Luisa", dto.getFirstName());
        assertEquals("Martínez", dto.getLastName());
        assertEquals("luisa@example.com", dto.getEmail());
        assertEquals("gst-XYZ-emp2", dto.getTokenEmpleado());
        assertEquals("678123456", dto.getNumTelefono());
        assertEquals("Barra", dto.getDescripcion());
        assertEquals("luisa", dto.getUsername());
        assertEquals("pass", dto.getPassword());
        assertEquals(2, dto.getNegocio());
    }
}