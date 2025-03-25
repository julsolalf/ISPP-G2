package ispp_g2.gastrostock.testMesa;

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

import ispp_g2.gastrostock.dueño.Dueño;
import ispp_g2.gastrostock.mesa.Mesa;
import ispp_g2.gastrostock.mesa.MesaRepository;
import ispp_g2.gastrostock.mesa.MesaService;
import ispp_g2.gastrostock.negocio.Negocio;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class MesaServiceTest {

    @Mock
    private MesaRepository mesaRepository;

    @InjectMocks
    private MesaService mesaService;

    private Mesa mesa1, mesa2, mesa3, mesaInvalida;
    private Negocio negocio;
    private Dueño dueño;
    private List<Mesa> mesasList;

    @BeforeEach
    void setUp() {
        // Crear dueño
        dueño = new Dueño();
        dueño.setId(1);
        dueño.setFirstName("Juan");
        dueño.setLastName("García");
        dueño.setEmail("juan@example.com");
        dueño.setNumTelefono("652345678");
        dueño.setTokenDueño("TOKEN123");

        // Crear negocio
        negocio = new Negocio();
        negocio.setId(1);
        negocio.setName("Restaurante La Tasca");
        negocio.setDireccion("Calle Principal 123");
        negocio.setCiudad("Sevilla");
        negocio.setPais("España");
        negocio.setCodigoPostal("41001");
        negocio.setTokenNegocio(12345);
        negocio.setDueño(dueño);

        // Crear mesas
        mesa1 = new Mesa();
        mesa1.setId(1);
        mesa1.setName("Mesa Exterior");
        mesa1.setNumeroAsientos(4);
        mesa1.setNegocio(negocio);

        mesa2 = new Mesa();
        mesa2.setId(2);
        mesa2.setName("Mesa VIP");
        mesa2.setNumeroAsientos(6);
        mesa2.setNegocio(negocio);

        mesa3 = new Mesa();
        mesa3.setId(3);
        mesa3.setName("Mesa Barra");
        mesa3.setNumeroAsientos(2);
        mesa3.setNegocio(negocio);

        // Mesa inválida para pruebas
        mesaInvalida = new Mesa();
        // No se establece ninguna propiedad para que sea inválida

        // Lista de mesas para tests
        mesasList = Arrays.asList(mesa1, mesa2, mesa3);
    }

    // TESTS PARA getById()

    @Test
    void testGetById_Success() {
        // Arrange
        when(mesaRepository.findById("1")).thenReturn(Optional.of(mesa1));

        // Act
        Mesa result = mesaService.getById("1");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Mesa Exterior", result.getName());
        verify(mesaRepository).findById("1");
    }

    @Test
    void testGetById_NotFound() {
        // Arrange
        when(mesaRepository.findById("999")).thenReturn(Optional.empty());

        // Act
        Mesa result = mesaService.getById("999");

        // Assert
        assertNull(result);
        verify(mesaRepository).findById("999");
    }

    @Test
    void testGetById_NullId() {
        // Arrange & Act
        Mesa result = mesaService.getById(null);

        // Assert
        assertNull(result);
        verify(mesaRepository).findById(null);
    }

    // TESTS PARA getByName()

    @Test
    void testGetByName_Success() {
        // Arrange
        when(mesaRepository.findMesaByName("Exterior")).thenReturn(mesa1);

        // Act
        Mesa result = mesaService.getByName("Exterior");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Mesa Exterior", result.getName());
        verify(mesaRepository).findMesaByName("Exterior");
    }

    @Test
    void testGetByName_NotFound() {
        // Arrange
        when(mesaRepository.findMesaByName("Inexistente")).thenReturn(null);

        // Act
        Mesa result = mesaService.getByName("Inexistente");

        // Assert
        assertNull(result);
        verify(mesaRepository).findMesaByName("Inexistente");
    }

    @Test
    void testGetByName_NullName() {
        // Arrange
        when(mesaRepository.findMesaByName(null)).thenReturn(null);

        // Act
        Mesa result = mesaService.getByName(null);

        // Assert
        assertNull(result);
        verify(mesaRepository).findMesaByName(null);
    }

    // TESTS PARA getMesas()

    @Test
    void testGetMesas_Success() {
        // Arrange
        when(mesaRepository.findAll()).thenReturn(mesasList);

        // Act
        List<Mesa> result = mesaService.getMesas();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Mesa Exterior", result.get(0).getName());
        assertEquals("Mesa VIP", result.get(1).getName());
        assertEquals("Mesa Barra", result.get(2).getName());
        verify(mesaRepository).findAll();
    }

    @Test
    void testGetMesas_EmptyList() {
        // Arrange
        when(mesaRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Mesa> result = mesaService.getMesas();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mesaRepository).findAll();
    }

    // TESTS PARA getMesasByNumeroAsientos()

    @Test
    void testGetMesasByNumeroAsientos_Success() {
        // Arrange
        List<Mesa> mesasConCuatroAsientos = Collections.singletonList(mesa1);
        when(mesaRepository.findMesaByNumeroAsientos(4)).thenReturn(mesasConCuatroAsientos);

        // Act
        List<Mesa> result = mesaService.getMesasByNumeroAsientos(4);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Mesa Exterior", result.get(0).getName());
        assertEquals(4, result.get(0).getNumeroAsientos());
        verify(mesaRepository).findMesaByNumeroAsientos(4);
    }

    @Test
    void testGetMesasByNumeroAsientos_Multiple() {
        // Arrange
        Mesa otraMesa = new Mesa();
        otraMesa.setId(4);
        otraMesa.setName("Otra Mesa");
        otraMesa.setNumeroAsientos(4);
        otraMesa.setNegocio(negocio);

        List<Mesa> mesasConCuatroAsientos = Arrays.asList(mesa1, otraMesa);
        when(mesaRepository.findMesaByNumeroAsientos(4)).thenReturn(mesasConCuatroAsientos);

        // Act
        List<Mesa> result = mesaService.getMesasByNumeroAsientos(4);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(4, result.get(0).getNumeroAsientos());
        assertEquals(4, result.get(1).getNumeroAsientos());
        verify(mesaRepository).findMesaByNumeroAsientos(4);
    }

    @Test
    void testGetMesasByNumeroAsientos_NotFound() {
        // Arrange
        when(mesaRepository.findMesaByNumeroAsientos(10)).thenReturn(Collections.emptyList());

        // Act
        List<Mesa> result = mesaService.getMesasByNumeroAsientos(10);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mesaRepository).findMesaByNumeroAsientos(10);
    }

    @Test
    void testGetMesasByNumeroAsientos_Zero() {
        // Arrange
        when(mesaRepository.findMesaByNumeroAsientos(0)).thenReturn(Collections.emptyList());

        // Act
        List<Mesa> result = mesaService.getMesasByNumeroAsientos(0);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mesaRepository).findMesaByNumeroAsientos(0);
    }

    @Test
    void testGetMesasByNumeroAsientos_Negative() {
        // Arrange
        when(mesaRepository.findMesaByNumeroAsientos(-1)).thenReturn(Collections.emptyList());

        // Act
        List<Mesa> result = mesaService.getMesasByNumeroAsientos(-1);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mesaRepository).findMesaByNumeroAsientos(-1);
    }

    // TESTS PARA getMesasByNegocio()

    @Test
    void testGetMesasByNegocio_Success() {
        // Arrange
        when(mesaRepository.findMesasByNegocio("1")).thenReturn(mesasList);

        // Act
        List<Mesa> result = mesaService.getMesasByNegocio("1");

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(1, result.get(0).getNegocio().getId());
        assertEquals(1, result.get(1).getNegocio().getId());
        assertEquals(1, result.get(2).getNegocio().getId());
        verify(mesaRepository).findMesasByNegocio("1");
    }

    @Test
    void testGetMesasByNegocio_NotFound() {
        // Arrange
        when(mesaRepository.findMesasByNegocio("999")).thenReturn(Collections.emptyList());

        // Act
        List<Mesa> result = mesaService.getMesasByNegocio("999");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mesaRepository).findMesasByNegocio("999");
    }

    @Test
    void testGetMesasByNegocio_NullId() {
        // Arrange
        when(mesaRepository.findMesasByNegocio(null)).thenReturn(Collections.emptyList());

        // Act
        List<Mesa> result = mesaService.getMesasByNegocio(null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mesaRepository).findMesasByNegocio(null);
    }

    // TESTS PARA save()

    @Test
    void testSave_Success() {
        // Arrange
        Mesa nuevaMesa = new Mesa();
        nuevaMesa.setName("Mesa Nueva");
        nuevaMesa.setNumeroAsientos(8);
        nuevaMesa.setNegocio(negocio);

        Mesa mesaGuardada = new Mesa();
        mesaGuardada.setId(4);
        mesaGuardada.setName("Mesa Nueva");
        mesaGuardada.setNumeroAsientos(8);
        mesaGuardada.setNegocio(negocio);

        when(mesaRepository.save(any(Mesa.class))).thenReturn(mesaGuardada);

        // Act
        Mesa result = mesaService.save(nuevaMesa);

        // Assert
        assertNotNull(result);
        assertEquals(4, result.getId());
        assertEquals("Mesa Nueva", result.getName());
        verify(mesaRepository).save(nuevaMesa);
    }

    @Test
    void testSave_Update() {
        // Arrange
        mesa1.setNumeroAsientos(10); // Modificar número de asientos

        when(mesaRepository.save(any(Mesa.class))).thenReturn(mesa1);

        // Act
        Mesa result = mesaService.save(mesa1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(10, result.getNumeroAsientos()); // Verificar la actualización
        verify(mesaRepository).save(mesa1);
    }

    @Test
    void testSave_InvalidData() {
        // Arrange
        when(mesaRepository.save(mesaInvalida)).thenReturn(mesaInvalida);

        // Act
        Mesa result = mesaService.save(mesaInvalida);

        // Assert - Verificar que devuelve la mesa, aunque sea inválida
        assertNotNull(result);
        verify(mesaRepository).save(mesaInvalida);
    }

    @Test
    void testSave_NullMesa() {
        // Arrange
        when(mesaRepository.save(null)).thenThrow(IllegalArgumentException.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> mesaService.save(null));
        verify(mesaRepository).save(null);
    }

    // TESTS PARA deleteById()

    @Test
    void testDeleteById_Success() {
        // Arrange
        doNothing().when(mesaRepository).deleteById("1");

        // Act
        mesaService.deleteById("1");

        // Assert
        verify(mesaRepository).deleteById("1");
    }

    @Test
    void testDeleteById_NotFound() {
        // Arrange
        doThrow(new RuntimeException("Mesa no encontrada")).when(mesaRepository).deleteById("999");

        // Act & Assert
        assertThrows(RuntimeException.class, () -> mesaService.deleteById("999"));
        verify(mesaRepository).deleteById("999");
    }

    @Test
    void testDeleteById_NullId() {
        // Arrange
        doThrow(new IllegalArgumentException("ID no puede ser null")).when(mesaRepository).deleteById(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> mesaService.deleteById(null));
        verify(mesaRepository).deleteById(null);
    }
}