package ispp_g2.gastrostock.testDueño;

import static org.junit.jupiter.api.Assertions.*;
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

import ispp_g2.gastrostock.dueño.Dueño;
import ispp_g2.gastrostock.dueño.DueñoRepository;
import ispp_g2.gastrostock.dueño.DueñoService;
import ispp_g2.gastrostock.negocio.Negocio;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class DueñoServiceTest {

    @Mock
    private DueñoRepository dueñoRepository;

    @InjectMocks
    private DueñoService dueñoService;

    @Captor
    private ArgumentCaptor<Dueño> dueñoCaptor;

    private Dueño dueñoNormal;
    private Dueño dueñoConNegocio;
    private Dueño dueñoInvalido;
    private List<Dueño> dueñosList;
    private Negocio negocio;

    @BeforeEach
    void setUp() {
        // Crear un dueño normal
        dueñoNormal = new Dueño();
        dueñoNormal.setId(1);
        dueñoNormal.setFirstName("Juan");
        dueñoNormal.setLastName("García");
        dueñoNormal.setEmail("juan@example.com");
        dueñoNormal.setNumTelefono("652345678");
        dueñoNormal.setTokenDueño("TOKEN123");

        // Crear un negocio
        negocio = new Negocio();
        negocio.setId(1);
        negocio.setName("Restaurante La Tasca");
        negocio.setDireccion("Calle Principal 123");
        negocio.setCiudad("Sevilla");
        negocio.setPais("España");
        negocio.setCodigoPostal("41001");
        negocio.setTokenNegocio(12345);

        // Crear un dueño con negocio
        dueñoConNegocio = new Dueño();
        dueñoConNegocio.setId(2);
        dueñoConNegocio.setFirstName("Ana");
        dueñoConNegocio.setLastName("Martínez");
        dueñoConNegocio.setEmail("ana@example.com");
        dueñoConNegocio.setNumTelefono("654321987");
        dueñoConNegocio.setTokenDueño("TOKEN456");
        
        // Asignar el dueño al negocio
        negocio.setDueño(dueñoConNegocio);

        // Crear un dueño con datos inválidos
        dueñoInvalido = new Dueño();
        dueñoInvalido.setId(3);
        // Email null o vacío
        dueñoInvalido.setEmail("");
        // Token null o vacío
        dueñoInvalido.setTokenDueño("");

        // Lista de dueños para tests
        dueñosList = new ArrayList<>();
        dueñosList.add(dueñoNormal);
        dueñosList.add(dueñoConNegocio);
    }

    // TESTS PARA saveDueño()

    @Test
    void testSaveDueño_Success() {
        // Arrange
        when(dueñoRepository.save(any(Dueño.class))).thenReturn(dueñoNormal);

        // Act
        Dueño saved = dueñoService.saveDueño(dueñoNormal);

        // Assert
        assertNotNull(saved);
        assertEquals(1, saved.getId());
        assertEquals("Juan", saved.getFirstName());
        assertEquals("García", saved.getLastName());
        assertEquals("juan@example.com", saved.getEmail());
        verify(dueñoRepository, times(1)).save(dueñoNormal);
    }

    @Test
    void testSaveDueño_WithNegocio() {
        // Arrange
        when(dueñoRepository.save(any(Dueño.class))).thenReturn(dueñoConNegocio);

        // Act
        Dueño saved = dueñoService.saveDueño(dueñoConNegocio);

        // Assert
        assertNotNull(saved);
        assertEquals(2, saved.getId());
        assertEquals("ana@example.com", saved.getEmail());
        verify(dueñoRepository, times(1)).save(dueñoConNegocio);
    }

    @Test
    void testSaveDueño_WithInvalidData() {
        // Arrange
        when(dueñoRepository.save(dueñoInvalido)).thenThrow(new IllegalArgumentException("Invalid data"));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            dueñoService.saveDueño(dueñoInvalido);
        });
        assertEquals("Invalid data", exception.getMessage());
        verify(dueñoRepository, times(1)).save(dueñoInvalido);
    }

    @Test
    void testSaveDueño_Null() {
        // Arrange
        when(dueñoRepository.save(null)).thenThrow(new IllegalArgumentException("Dueño cannot be null"));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            dueñoService.saveDueño(null);
        });
        assertEquals("Dueño cannot be null", exception.getMessage());
        verify(dueñoRepository, times(1)).save(null);
    }

    // TESTS PARA getAllDueños()

    @Test
    void testGetAllDueños_Success() {
        // Arrange
        when(dueñoRepository.findAll()).thenReturn(dueñosList);

        // Act
        List<Dueño> result = dueñoService.getAllDueños();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(2, result.get(1).getId());
        verify(dueñoRepository, times(1)).findAll();
    }

    @Test
    void testGetAllDueños_EmptyList() {
        // Arrange
        when(dueñoRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Dueño> result = dueñoService.getAllDueños();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(dueñoRepository, times(1)).findAll();
    }

    @Test
    void testGetAllDueños_NullList() {
        // Arrange
        when(dueñoRepository.findAll()).thenReturn(null);

        // Act & Assert
        Exception exception = assertThrows(NullPointerException.class, () -> {
            dueñoService.getAllDueños();
        });
        verify(dueñoRepository, times(1)).findAll();
    }

    // TESTS PARA getDueñoById()

    @Test
    void testGetDueñoById_Success() {
        // Arrange
        when(dueñoRepository.findById("1")).thenReturn(Optional.of(dueñoNormal));

        // Act
        Dueño result = dueñoService.getDueñoById("1");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("juan@example.com", result.getEmail());
        verify(dueñoRepository, times(1)).findById("1");
    }

    @Test
    void testGetDueñoById_NotFound() {
        // Arrange
        when(dueñoRepository.findById("999")).thenReturn(Optional.empty());

        // Act
        Dueño result = dueñoService.getDueñoById("999");

        // Assert
        assertNull(result);
        verify(dueñoRepository, times(1)).findById("999");
    }

    @Test
    void testGetDueñoById_NullId() {
        // Arrange
        when(dueñoRepository.findById(null)).thenThrow(new IllegalArgumentException("ID cannot be null"));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            dueñoService.getDueñoById(null);
        });
        assertEquals("ID cannot be null", exception.getMessage());
        verify(dueñoRepository, times(1)).findById(null);
    }

    // TESTS PARA getDueñoByEmail()

    @Test
    void testGetDueñoByEmail_Success() {
        // Arrange
        when(dueñoRepository.findDueñoByEmail("juan@example.com")).thenReturn(Optional.of(dueñoNormal));

        // Act
        Dueño result = dueñoService.getDueñoByEmail("juan@example.com");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("juan@example.com", result.getEmail());
        verify(dueñoRepository, times(1)).findDueñoByEmail("juan@example.com");
    }

    @Test
    void testGetDueñoByEmail_NotFound() {
        // Arrange
        when(dueñoRepository.findDueñoByEmail("notfound@example.com")).thenReturn(Optional.empty());

        // Act
        Dueño result = dueñoService.getDueñoByEmail("notfound@example.com");

        // Assert
        assertNull(result);
        verify(dueñoRepository, times(1)).findDueñoByEmail("notfound@example.com");
    }

    @Test
    void testGetDueñoByEmail_EmptyEmail() {
        // Arrange
        when(dueñoRepository.findDueñoByEmail("")).thenReturn(Optional.empty());

        // Act
        Dueño result = dueñoService.getDueñoByEmail("");

        // Assert
        assertNull(result);
        verify(dueñoRepository, times(1)).findDueñoByEmail("");
    }

    @Test
    void testGetDueñoByEmail_NullEmail() {
        // Arrange
        when(dueñoRepository.findDueñoByEmail(null)).thenThrow(new IllegalArgumentException("Email cannot be null"));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            dueñoService.getDueñoByEmail(null);
        });
        assertEquals("Email cannot be null", exception.getMessage());
        verify(dueñoRepository, times(1)).findDueñoByEmail(null);
    }

    // TESTS PARA deleteDueño()

    @Test
    void testDeleteDueño_Success() {
        // Arrange
        doNothing().when(dueñoRepository).deleteById("1");

        // Act
        dueñoService.deleteDueño("1");

        // Assert
        verify(dueñoRepository, times(1)).deleteById("1");
    }

    @Test
    void testDeleteDueño_NotFound() {
        // Arrange
        doThrow(new RuntimeException("Dueño not found")).when(dueñoRepository).deleteById("999");

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            dueñoService.deleteDueño("999");
        });
        assertEquals("Dueño not found", exception.getMessage());
        verify(dueñoRepository, times(1)).deleteById("999");
    }

    @Test
    void testDeleteDueño_NullId() {
        // Arrange
        doThrow(new IllegalArgumentException("ID cannot be null")).when(dueñoRepository).deleteById(null);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            dueñoService.deleteDueño(null);
        });
        assertEquals("ID cannot be null", exception.getMessage());
        verify(dueñoRepository, times(1)).deleteById(null);
    }

    // TESTS PARA getDueñoByToken (asumiendo que existe este método)

    @Test
    void testGetDueñoByToken_Success() {
        // Arrange
        when(dueñoRepository.findDueñoByToken("TOKEN123")).thenReturn(Optional.of(dueñoNormal));

        // Act
        Dueño result = dueñoService.getDueñoByToken("TOKEN123");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("TOKEN123", result.getTokenDueño());
        verify(dueñoRepository, times(1)).findDueñoByToken("TOKEN123");
    }

    @Test
    void testGetDueñoByToken_NotFound() {
        // Arrange
        when(dueñoRepository.findDueñoByToken("INVALID_TOKEN")).thenReturn(Optional.empty());

        // Act
        Dueño result = dueñoService.getDueñoByToken("INVALID_TOKEN");

        // Assert
        assertNull(result);
        verify(dueñoRepository, times(1)).findDueñoByToken("INVALID_TOKEN");
    }

    @Test
    void testGetDueñoByToken_EmptyToken() {
        // Arrange
        when(dueñoRepository.findDueñoByToken("")).thenReturn(Optional.empty());

        // Act
        Dueño result = dueñoService.getDueñoByToken("");

        // Assert
        assertNull(result);
        verify(dueñoRepository, times(1)).findDueñoByToken("");
    }

    @Test
    void testGetDueñoByToken_NullToken() {
        // Arrange
        when(dueñoRepository.findDueñoByToken(null)).thenThrow(new IllegalArgumentException("Token cannot be null"));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            dueñoService.getDueñoByToken(null);
        });
        assertEquals("Token cannot be null", exception.getMessage());
        verify(dueñoRepository, times(1)).findDueñoByToken(null);
    }
}