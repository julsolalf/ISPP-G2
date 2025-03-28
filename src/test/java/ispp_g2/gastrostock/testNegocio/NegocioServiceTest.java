package ispp_g2.gastrostock.testNegocio;

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

import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioRepository;
import ispp_g2.gastrostock.negocio.NegocioService;
import ispp_g2.gastrostock.dueño.Dueño;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class NegocioServiceTest {

    @Mock
    private NegocioRepository negocioRepository;

    @InjectMocks
    private NegocioService negocioService;

    private Negocio negocio1, negocio2, negocio3, nuevoNegocio;
    private Dueño dueño1, dueño2;
    private List<Negocio> negociosList;

    @BeforeEach
    void setUp() {
        // Crear dueños
        dueño1 = new Dueño();
        dueño1.setId(1);
        dueño1.setFirstName("Juan");
        dueño1.setLastName("García");
        dueño1.setEmail("juan@example.com");
        dueño1.setNumTelefono("666111222");
        dueño1.setTokenDueño("TOKEN999");

        dueño2 = new Dueño();
        dueño2.setId(2);
        dueño2.setFirstName("María");
        dueño2.setLastName("López");
        dueño2.setEmail("maria@example.com");
        dueño2.setNumTelefono("666333444");
        dueño2.setTokenDueño("TOKEN456");

        // Crear negocios
        negocio1 = new Negocio();
        negocio1.setId(1);
        negocio1.setName("Restaurante La Tasca");
        negocio1.setDireccion("Calle Principal 123");
        negocio1.setCiudad("Sevilla");
        negocio1.setPais("España");
        negocio1.setCodigoPostal("41001");
        negocio1.setTokenNegocio(12345);
        negocio1.setDueño(dueño1);

        negocio2 = new Negocio();
        negocio2.setId(2);
        negocio2.setName("Bar El Rincón");
        negocio2.setDireccion("Avenida de la Constitución 45");
        negocio2.setCiudad("Sevilla");
        negocio2.setPais("España");
        negocio2.setCodigoPostal("41001");
        negocio2.setTokenNegocio(67890);
        negocio2.setDueño(dueño1);

        negocio3 = new Negocio();
        negocio3.setId(3);
        negocio3.setName("Café Central");
        negocio3.setDireccion("Plaza Mayor 10");
        negocio3.setCiudad("Madrid");
        negocio3.setPais("España");
        negocio3.setCodigoPostal("28001");
        negocio3.setTokenNegocio(54321);
        negocio3.setDueño(dueño2);

        // Negocio nuevo para tests de creación
        nuevoNegocio = new Negocio();
        nuevoNegocio.setName("Heladería Polar");
        nuevoNegocio.setDireccion("Calle Fresa 15");
        nuevoNegocio.setCiudad("Valencia");
        nuevoNegocio.setPais("España");
        nuevoNegocio.setCodigoPostal("46001");
        nuevoNegocio.setTokenNegocio(11223);
        nuevoNegocio.setDueño(dueño2);

        // Lista de negocios
        negociosList = Arrays.asList(negocio1, negocio2, negocio3);
    }

    // TESTS PARA getById()

    @Test
    void testGetById_Success() {
        // Arrange
        when(negocioRepository.findById(1)).thenReturn(Optional.of(negocio1));

        // Act
        Negocio result = negocioService.getById(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Restaurante La Tasca", result.getName());
        verify(negocioRepository).findById(1);
    }

    @Test
    void testGetById_NotFound() {
        // Arrange
        when(negocioRepository.findById(999)).thenReturn(Optional.empty());

        // Act
        Negocio result = negocioService.getById(999);

        // Assert
        assertNull(result);
        verify(negocioRepository).findById(999);
    }

    @Test
    void testGetById_NullId() {
        // Arrange & Act
        Negocio result = negocioService.getById(null);

        // Assert
        assertNull(result);
        verify(negocioRepository).findById(null);
    }

    // TESTS PARA getNegocios()

    @Test
    void testGetNegocios_Success() {
        // Arrange
        when(negocioRepository.findAll()).thenReturn(negociosList);

        // Act
        List<Negocio> result = negocioService.getNegocios();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Restaurante La Tasca", result.get(0).getName());
        assertEquals("Bar El Rincón", result.get(1).getName());
        assertEquals("Café Central", result.get(2).getName());
        verify(negocioRepository).findAll();
    }

    @Test
    void testGetNegocios_EmptyList() {
        // Arrange
        when(negocioRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Negocio> result = negocioService.getNegocios();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(negocioRepository).findAll();
    }

    // TESTS PARA getByName()

    @Test
    void testGetByName_Success() {
        // Arrange
        when(negocioRepository.findByName("Bar El Rincón")).thenReturn(negocio2);

        // Act
        Negocio result = negocioService.getByName("Bar El Rincón");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getId());
        assertEquals("Bar El Rincón", result.getName());
        verify(negocioRepository).findByName("Bar El Rincón");
    }

    @Test
    void testGetByName_NotFound() {
        // Arrange
        when(negocioRepository.findByName("Negocio Inexistente")).thenReturn(null);

        // Act
        Negocio result = negocioService.getByName("Negocio Inexistente");

        // Assert
        assertNull(result);
        verify(negocioRepository).findByName("Negocio Inexistente");
    }

    @Test
    void testGetByName_NullName() {
        // Arrange
        when(negocioRepository.findByName(null)).thenReturn(null);

        // Act
        Negocio result = negocioService.getByName(null);

        // Assert
        assertNull(result);
        verify(negocioRepository).findByName(null);
    }

    // TESTS PARA getByDireccion()

    @Test
    void testGetByDireccion_Success() {
        // Arrange
        List<Negocio> negociosDireccion = Collections.singletonList(negocio3);
        when(negocioRepository.findByDireccion("Plaza Mayor 10")).thenReturn(negociosDireccion);

        // Act
        List<Negocio> result = negocioService.getByDireccion("Plaza Mayor 10");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Café Central", result.get(0).getName());
        verify(negocioRepository).findByDireccion("Plaza Mayor 10");
    }

    @Test
    void testGetByDireccion_NotFound() {
        // Arrange
        when(negocioRepository.findByDireccion("Dirección Inexistente")).thenReturn(Collections.emptyList());

        // Act
        List<Negocio> result = negocioService.getByDireccion("Dirección Inexistente");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(negocioRepository).findByDireccion("Dirección Inexistente");
    }

    @Test
    void testGetByDireccion_NullDireccion() {
        // Arrange
        when(negocioRepository.findByDireccion(null)).thenReturn(Collections.emptyList());

        // Act
        List<Negocio> result = negocioService.getByDireccion(null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(negocioRepository).findByDireccion(null);
    }

    // TESTS PARA getByToken()

    @Test
    void testGetByToken_Success() {
        // Arrange
        when(negocioRepository.findByTokenNegocio(12345)).thenReturn(negocio1);

        // Act
        Negocio result = negocioService.getByToken(12345);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Restaurante La Tasca", result.getName());
        verify(negocioRepository).findByTokenNegocio(12345);
    }

    @Test
    void testGetByToken_NotFound() {
        // Arrange
        when(negocioRepository.findByTokenNegocio(99999)).thenReturn(null);

        // Act
        Negocio result = negocioService.getByToken(99999);

        // Assert
        assertNull(result);
        verify(negocioRepository).findByTokenNegocio(99999);
    }

    @Test
    void testGetByToken_NullToken() {
        // Arrange
        when(negocioRepository.findByTokenNegocio(null)).thenReturn(null);

        // Act
        Negocio result = negocioService.getByToken(null);

        // Assert
        assertNull(result);
        verify(negocioRepository).findByTokenNegocio(null);
    }

    // TESTS PARA getByCiudad()

    @Test
    void testGetByCiudad_Success() {
        // Arrange
        List<Negocio> negociosSevilla = Arrays.asList(negocio1, negocio2);
        when(negocioRepository.findByCiudad("Sevilla")).thenReturn(negociosSevilla);

        // Act
        List<Negocio> result = negocioService.getByCiudad("Sevilla");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Restaurante La Tasca", result.get(0).getName());
        assertEquals("Bar El Rincón", result.get(1).getName());
        verify(negocioRepository).findByCiudad("Sevilla");
    }

    @Test
    void testGetByCiudad_NotFound() {
        // Arrange
        when(negocioRepository.findByCiudad("Ciudad Inexistente")).thenReturn(Collections.emptyList());

        // Act
        List<Negocio> result = negocioService.getByCiudad("Ciudad Inexistente");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(negocioRepository).findByCiudad("Ciudad Inexistente");
    }

    @Test
    void testGetByCiudad_NullCiudad() {
        // Arrange
        when(negocioRepository.findByCiudad(null)).thenReturn(Collections.emptyList());

        // Act
        List<Negocio> result = negocioService.getByCiudad(null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(negocioRepository).findByCiudad(null);
    }

    // TESTS PARA getByPais()

    @Test
    void testGetByPais_Success() {
        // Arrange
        when(negocioRepository.findByPais("España")).thenReturn(negociosList);

        // Act
        List<Negocio> result = negocioService.getByPais("España");

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(negocioRepository).findByPais("España");
    }

    @Test
    void testGetByPais_NotFound() {
        // Arrange
        when(negocioRepository.findByPais("País Inexistente")).thenReturn(Collections.emptyList());

        // Act
        List<Negocio> result = negocioService.getByPais("País Inexistente");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(negocioRepository).findByPais("País Inexistente");
    }

    @Test
    void testGetByPais_NullPais() {
        // Arrange
        when(negocioRepository.findByPais(null)).thenReturn(Collections.emptyList());

        // Act
        List<Negocio> result = negocioService.getByPais(null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(negocioRepository).findByPais(null);
    }

    // TESTS PARA getByCodigoPostal()

    @Test
    void testGetByCodigoPostal_Success() {
        // Arrange
        List<Negocio> negociosCP = Arrays.asList(negocio1, negocio2);
        when(negocioRepository.findByCodigoPostal("41001")).thenReturn(negociosCP);

        // Act
        List<Negocio> result = negocioService.getByCodigoPostal("41001");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Restaurante La Tasca", result.get(0).getName());
        assertEquals("Bar El Rincón", result.get(1).getName());
        verify(negocioRepository).findByCodigoPostal("41001");
    }

    @Test
    void testGetByCodigoPostal_NotFound() {
        // Arrange
        when(negocioRepository.findByCodigoPostal("00000")).thenReturn(Collections.emptyList());

        // Act
        List<Negocio> result = negocioService.getByCodigoPostal("00000");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(negocioRepository).findByCodigoPostal("00000");
    }

    @Test
    void testGetByCodigoPostal_NullCodigoPostal() {
        // Arrange
        when(negocioRepository.findByCodigoPostal(null)).thenReturn(Collections.emptyList());

        // Act
        List<Negocio> result = negocioService.getByCodigoPostal(null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(negocioRepository).findByCodigoPostal(null);
    }

    // TESTS PARA getByDueño()

    @Test
    void testGetByDueño_Success() {
        // Arrange
        List<Negocio> negociosDueño = Arrays.asList(negocio1, negocio2);
        when(negocioRepository.findByDueño(1)).thenReturn(negociosDueño);

        // Act
        List<Negocio> result = negocioService.getByDueño(1);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(negocioRepository).findByDueño(1);
    }

    @Test
    void testGetByDueño_NotFound() {
        // Arrange
        when(negocioRepository.findByDueño(999)).thenReturn(Collections.emptyList());

        // Act
        List<Negocio> result = negocioService.getByDueño(999);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(negocioRepository).findByDueño(999);
    }

    @Test
    void testGetByDueño_NullDueño() {
        // Arrange
        when(negocioRepository.findByDueño(null)).thenReturn(Collections.emptyList());

        // Act
        List<Negocio> result = negocioService.getByDueño(null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(negocioRepository).findByDueño(null);
    }

    // TESTS PARA save()

    @Test
    void testSave_NewNegocio() {
        // Arrange
        Negocio negocioGuardado = new Negocio();
        negocioGuardado.setId(4);
        negocioGuardado.setName("Heladería Polar");
        negocioGuardado.setDireccion("Calle Fresa 15");
        negocioGuardado.setCiudad("Valencia");
        negocioGuardado.setPais("España");
        negocioGuardado.setCodigoPostal("46001");
        negocioGuardado.setTokenNegocio(11223);
        negocioGuardado.setDueño(dueño2);

        when(negocioRepository.save(any(Negocio.class))).thenReturn(negocioGuardado);

        // Act
        Negocio result = negocioService.save(nuevoNegocio);

        // Assert
        assertNotNull(result);
        assertEquals(4, result.getId());
        assertEquals("Heladería Polar", result.getName());
        assertEquals("Valencia", result.getCiudad());
        verify(negocioRepository).save(nuevoNegocio);
    }

    @Test
    void testSave_UpdateNegocio() {
        // Arrange
        negocio1.setName("Restaurante La Tasca Renovado");
        negocio1.setCiudad("Córdoba");

        when(negocioRepository.save(negocio1)).thenReturn(negocio1);

        // Act
        Negocio result = negocioService.save(negocio1);

        // Assert
        assertNotNull(result);
        assertEquals("Restaurante La Tasca Renovado", result.getName());
        assertEquals("Córdoba", result.getCiudad());
        verify(negocioRepository).save(negocio1);
    }

    @Test
    void testSave_NullNegocio() {
        // Arrange
        when(negocioRepository.save(null)).thenThrow(IllegalArgumentException.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> negocioService.save(null));
        verify(negocioRepository).save(null);
    }

    // TESTS PARA delete()

    @Test
    void testDelete_Success() {
        // Arrange
        doNothing().when(negocioRepository).deleteById(1);

        // Act
        negocioService.delete(1);

        // Assert
        verify(negocioRepository).deleteById(1);
    }

    @Test
    void testDelete_NonExistentId() {
        // Arrange
        doThrow(new RuntimeException("Negocio no encontrado")).when(negocioRepository).deleteById(999);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> negocioService.delete(999));
        verify(negocioRepository).deleteById(999);
    }

    @Test
    void testDelete_NullId() {
        // Arrange
        doThrow(new IllegalArgumentException("ID no puede ser null")).when(negocioRepository).deleteById(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> negocioService.delete(null));
        verify(negocioRepository).deleteById(null);
    }
}