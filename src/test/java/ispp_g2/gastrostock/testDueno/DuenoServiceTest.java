package ispp_g2.gastrostock.testDueno;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import ispp_g2.gastrostock.dueno.Dueno;
import ispp_g2.gastrostock.dueno.DuenoRepository;
import ispp_g2.gastrostock.dueno.DuenoService;
import ispp_g2.gastrostock.exceptions.DuenoSaveException;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.user.AuthoritiesRepository;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserRepository;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class DuenoServiceTest {

    @Mock
    private DuenoRepository duenoRepository;

    @InjectMocks
    private DuenoService duenoService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthoritiesRepository authoritiesRepository;

    @Captor
    private ArgumentCaptor<Dueno> duenoCaptor;

    private Dueno duenoNormal;
    private Dueno duenoConNegocio;
    private Dueno duenoInvalido;
    private List<Dueno> duenosList;
    private Negocio negocio;

    @BeforeEach
    void setUp() {

        User user = new User();

        // Crear un dueno normal
        duenoNormal = new Dueno();
        duenoNormal.setId(1);
        duenoNormal.setFirstName("Juan");
        duenoNormal.setLastName("García");
        duenoNormal.setEmail("juan@example.com");
        duenoNormal.setNumTelefono("652345678");
        duenoNormal.setTokenDueno("TOKEN123");

        // Crear un negocio
        negocio = new Negocio();
        negocio.setId(1);
        negocio.setName("Restaurante La Tasca");
        negocio.setDireccion("Calle Principal 123");
        negocio.setCiudad("Sevilla");
        negocio.setPais("Espana");
        negocio.setCodigoPostal("41001");
        negocio.setTokenNegocio(12345);

        // Crear un dueno con negocio
        duenoConNegocio = new Dueno();
        duenoConNegocio.setId(2);
        duenoConNegocio.setFirstName("Ana");
        duenoConNegocio.setLastName("Martínez");
        duenoConNegocio.setEmail("ana@example.com");
        duenoConNegocio.setNumTelefono("654321987");
        duenoConNegocio.setTokenDueno("TOKEN456");
        duenoConNegocio.setUser(user);
        
        // Asignar el dueno al negocio
        negocio.setDueno(duenoConNegocio);

        // Crear un dueno con datos inválidos
        duenoInvalido = new Dueno();
        duenoInvalido.setId(3);
        // Email null o vacío
        duenoInvalido.setEmail("");
        // Token null o vacío
        duenoInvalido.setTokenDueno("");

        // Lista de duenos para tests
        duenosList = new ArrayList<>();
        duenosList.add(duenoNormal);
        duenosList.add(duenoConNegocio);
    }

    // TESTS PARA saveDueno()

    @Test
    void testSaveDueno_Success() {
        // Arrange
        User user = new User();
        duenoNormal.setUser(user);
        when(userRepository.save(user)).thenReturn(user);
        when(duenoRepository.save(any(Dueno.class))).thenReturn(duenoNormal);
    
        // Act
        Dueno saved = duenoService.saveDueno(duenoNormal);
    
        // Assert
        assertNotNull(saved);
        assertEquals(1, saved.getId());
        assertEquals("Juan", saved.getFirstName());
        verify(userRepository, times(1)).save(user);
        verify(duenoRepository, times(1)).save(duenoNormal);
    }

    @Test
    void testSaveDueno_WithNegocio() {
        // Arrange
        when(duenoRepository.save(any(Dueno.class))).thenReturn(duenoConNegocio);

        // Act
        Dueno saved = duenoService.saveDueno(duenoConNegocio);

        // Assert
        assertNotNull(saved);
        assertEquals(2, saved.getId());
        assertEquals("ana@example.com", saved.getEmail());
        verify(duenoRepository, times(1)).save(duenoConNegocio);
    }

    @Test
    void testSaveDueno_WithInvalidData() {
        // Arrange
        User user = new User();
        duenoInvalido.setUser(user);
        
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(duenoRepository.save(duenoInvalido)).thenThrow(new DuenoSaveException("Invalid data"));
    
        // Act & Assert
        Exception exception = assertThrows(DuenoSaveException.class, () -> {
            duenoService.saveDueno(duenoInvalido);
        });
        assertEquals("Error al guardar el dueño: Invalid data", exception.getMessage());
        verify(duenoRepository, times(1)).save(duenoInvalido);
    }

    @Test
    void testSaveDueno_Null() {
        // Act & Assert
        @SuppressWarnings("unused")
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            duenoService.saveDueno(null);
        });
        verify(duenoRepository, never()).save(any(Dueno.class));
    }

    // TESTS PARA getAllDuenos()

    @Test
    void testGetAllDuenos_Success() {
        // Arrange
        when(duenoRepository.findAll()).thenReturn(duenosList);

        // Act
        List<Dueno> result = duenoService.getAllDuenos();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(2, result.get(1).getId());
        verify(duenoRepository, times(1)).findAll();
    }

    @Test
    void testGetAllDuenos_EmptyList() {
        // Arrange
        when(duenoRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Dueno> result = duenoService.getAllDuenos();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(duenoRepository, times(1)).findAll();
    }

    @Test
    void testGetAllDuenos_NullList() {
        // Arrange
        when(duenoRepository.findAll()).thenReturn(null);

        // Act & Assert
        @SuppressWarnings("unused")
        Exception exception = assertThrows(NullPointerException.class, () -> {
            duenoService.getAllDuenos();
        });
        verify(duenoRepository, times(1)).findAll();
    }

    // TESTS PARA getDuenoById()

    @Test
    void testGetDuenoById_Success() {
        // Arrange
        when(duenoRepository.findById(1)).thenReturn(Optional.of(duenoNormal));

        // Act
        Dueno result = duenoService.getDuenoById(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("juan@example.com", result.getEmail());
        verify(duenoRepository, times(1)).findById(1);
    }

    @Test
    void testGetDuenoById_NotFound() {
        // Arrange
        when(duenoRepository.findById(999)).thenReturn(Optional.empty());

        // Act
        Dueno result = duenoService.getDuenoById(999);

        // Assert
        assertNull(result);
        verify(duenoRepository, times(1)).findById(999);
    }

    @SuppressWarnings("null")
    @Test
    void testGetDuenoById_NullId() {
        // Arrange
        when(duenoRepository.findById(null)).thenThrow(new IllegalArgumentException("ID cannot be null"));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            duenoService.getDuenoById(null);
        });
        assertEquals("ID cannot be null", exception.getMessage());
        verify(duenoRepository, times(1)).findById(null);
    }

    // TESTS PARA getDuenoByEmail()

    @Test
    void testGetDuenoByEmail_Success() {
        // Arrange
        when(duenoRepository.findDuenoByEmail("juan@example.com")).thenReturn(Optional.of(duenoNormal));

        // Act
        Dueno result = duenoService.getDuenoByEmail("juan@example.com");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("juan@example.com", result.getEmail());
        verify(duenoRepository, times(1)).findDuenoByEmail("juan@example.com");
    }

    @Test
    void testGetDuenoByEmail_NotFound() {
        // Arrange
        when(duenoRepository.findDuenoByEmail("notfound@example.com")).thenReturn(Optional.empty());

        // Act
        Dueno result = duenoService.getDuenoByEmail("notfound@example.com");

        // Assert
        assertNull(result);
        verify(duenoRepository, times(1)).findDuenoByEmail("notfound@example.com");
    }

    @Test
    void testGetDuenoByEmail_EmptyEmail() {
        // Arrange
        when(duenoRepository.findDuenoByEmail("")).thenReturn(Optional.empty());

        // Act
        Dueno result = duenoService.getDuenoByEmail("");

        // Assert
        assertNull(result);
        verify(duenoRepository, times(1)).findDuenoByEmail("");
    }

    @Test
    void testGetDuenoByEmail_NullEmail() {
        // Arrange
        when(duenoRepository.findDuenoByEmail(null)).thenThrow(new IllegalArgumentException("Email cannot be null"));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            duenoService.getDuenoByEmail(null);
        });
        assertEquals("Email cannot be null", exception.getMessage());
        verify(duenoRepository, times(1)).findDuenoByEmail(null);
    }

    // TESTS PARA deleteDueno()

    @Test
    void testDeleteDueno_Success() {
        // Arrange
        doNothing().when(duenoRepository).deleteById(1);

        // Act
        duenoService.deleteDueno(1);

        // Assert
        verify(duenoRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteDueno_NotFound() {
        // Arrange
        doThrow(new RuntimeException("Dueno not found")).when(duenoRepository).deleteById(999);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            duenoService.deleteDueno(999);
        });
        assertEquals("Dueno not found", exception.getMessage());
        verify(duenoRepository, times(1)).deleteById(999);
    }

    @SuppressWarnings("null")
    @Test
    void testDeleteDueno_NullId() {
        // Arrange
        doThrow(new IllegalArgumentException("ID cannot be null")).when(duenoRepository).deleteById(null);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            duenoService.deleteDueno(null);
        });
        assertEquals("ID cannot be null", exception.getMessage());
        verify(duenoRepository, times(1)).deleteById(null);
    }

    // TESTS PARA getDuenoByToken (asumiendo que existe este método)

    @Test
    void testGetDuenoByToken_Success() {
        // Arrange
        when(duenoRepository.findDuenoByToken("TOKEN123")).thenReturn(Optional.of(duenoNormal));

        // Act
        Dueno result = duenoService.getDuenoByToken("TOKEN123");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("TOKEN123", result.getTokenDueno());
        verify(duenoRepository, times(1)).findDuenoByToken("TOKEN123");
    }

    @Test
    void testGetDuenoByToken_NotFound() {
        // Arrange
        when(duenoRepository.findDuenoByToken("INVALID_TOKEN")).thenReturn(Optional.empty());

        // Act
        Dueno result = duenoService.getDuenoByToken("INVALID_TOKEN");

        // Assert
        assertNull(result);
        verify(duenoRepository, times(1)).findDuenoByToken("INVALID_TOKEN");
    }

    @Test
    void testGetDuenoByToken_EmptyToken() {
        // Arrange
        when(duenoRepository.findDuenoByToken("")).thenReturn(Optional.empty());

        // Act
        Dueno result = duenoService.getDuenoByToken("");

        // Assert
        assertNull(result);
        verify(duenoRepository, times(1)).findDuenoByToken("");
    }

    @Test
    void testGetDuenoByToken_NullToken() {
        // Arrange
        when(duenoRepository.findDuenoByToken(null)).thenThrow(new IllegalArgumentException("Token cannot be null"));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            duenoService.getDuenoByToken(null);
        });
        assertEquals("Token cannot be null", exception.getMessage());
        verify(duenoRepository, times(1)).findDuenoByToken(null);
    }
}