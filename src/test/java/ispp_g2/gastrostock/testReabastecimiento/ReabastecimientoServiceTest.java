package ispp_g2.gastrostock.testReabastecimiento;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
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

import ispp_g2.gastrostock.dueno.Dueno;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.proveedores.Proveedor;
import ispp_g2.gastrostock.reabastecimiento.Reabastecimiento;
import ispp_g2.gastrostock.reabastecimiento.ReabastecimientoRepository;
import ispp_g2.gastrostock.reabastecimiento.ReabastecimientoService;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ReabastecimientoServiceTest {

    @Mock
    private ReabastecimientoRepository reabastecimientoRepository;

    @InjectMocks
    private ReabastecimientoService reabastecimientoService;

    private Reabastecimiento reabastecimiento1;
    private Reabastecimiento reabastecimiento2;
    private List<Reabastecimiento> reabastecimientos;
    private Proveedor proveedor;
    private Negocio negocio;

    @BeforeEach
    void setUp() {
        // Crear dueno
        Dueno dueno = new Dueno();
        dueno.setId(1);
        dueno.setFirstName("Juan");
        dueno.setLastName("García");
        dueno.setEmail("juan@example.com");
        dueno.setNumTelefono("652345678");
        dueno.setTokenDueno("TOKEN123");

        // Crear negocio
        negocio = new Negocio();
        negocio.setId(1);
        negocio.setName("Restaurante La Tasca");
        negocio.setDireccion("Calle Principal 123");
        negocio.setCiudad("Sevilla");
        negocio.setPais("Espana");
        negocio.setCodigoPostal("41001");
        negocio.setTokenNegocio(12345);
        negocio.setDueno(dueno);

        // Crear proveedor
        proveedor = new Proveedor();
        proveedor.setId(1);
        proveedor.setName("Distribuciones Alimentarias S.L.");
        proveedor.setEmail("distribuciones@example.com");
        proveedor.setTelefono("954111222");
        proveedor.setDireccion("Polígono Industrial, Nave 7");

        // Crear reabastecimientos
        reabastecimiento1 = new Reabastecimiento();
        reabastecimiento1.setId(1);
        reabastecimiento1.setFecha(LocalDate.of(2023, 3, 15));
        reabastecimiento1.setPrecioTotal(1250.75);
        reabastecimiento1.setReferencia("REF-001");
        reabastecimiento1.setProveedor(proveedor);
        reabastecimiento1.setNegocio(negocio);

        reabastecimiento2 = new Reabastecimiento();
        reabastecimiento2.setId(2);
        reabastecimiento2.setFecha(LocalDate.of(2023, 4, 10));
        reabastecimiento2.setPrecioTotal(875.30);
        reabastecimiento2.setReferencia("REF-002");
        reabastecimiento2.setProveedor(proveedor);
        reabastecimiento2.setNegocio(negocio);

        reabastecimientos = Arrays.asList(reabastecimiento1, reabastecimiento2);
    }

    // Tests para getAll()

    @Test
    void testGetAll_Success() {
        // Arrange
        when(reabastecimientoRepository.findAll()).thenReturn(reabastecimientos);

        // Act
        Iterable<Reabastecimiento> result = reabastecimientoService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, ((List<Reabastecimiento>) result).size());
        verify(reabastecimientoRepository, times(1)).findAll();
    }

    @Test
    void testGetAll_EmptyList() {
        // Arrange
        when(reabastecimientoRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        Iterable<Reabastecimiento> result = reabastecimientoService.getAll();

        // Assert
        assertNotNull(result);
        assertFalse(((List<Reabastecimiento>) result).iterator().hasNext());
        verify(reabastecimientoRepository, times(1)).findAll();
    }

    // Tests para getById()

    @Test
    void testGetById_Exists() {
        // Arrange
        when(reabastecimientoRepository.findById(1)).thenReturn(Optional.of(reabastecimiento1));

        // Act
        Optional<Reabastecimiento> result = Optional.of(reabastecimientoService.getById(1));

        // Assert
        assertTrue(result.isPresent());
        assertEquals("REF-001", result.get().getReferencia());
        assertEquals(1250.75, result.get().getPrecioTotal());
        verify(reabastecimientoRepository, times(1)).findById(1);
    }

    @Test
    void testGetById_NotExists() {
        // Arrange
        when(reabastecimientoRepository.findById(999)).thenReturn(Optional.empty());
    
        // Act
        Reabastecimiento result = reabastecimientoService.getById(999);
    
        // Assert
        assertNull(result);
        verify(reabastecimientoRepository, times(1)).findById(999);
    }

    @Test
    void testGetById_NullId() {
        // Arrange
        when(reabastecimientoRepository.findById(null)).thenThrow(new IllegalArgumentException("ID cannot be null"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            reabastecimientoService.getById(null);
        });
        verify(reabastecimientoRepository, times(1)).findById(null);
    }

    // Tests para getByReferencia()

    @Test
    void testGetByReferencia_Exists() {
        // Arrange
        when(reabastecimientoRepository.findByReferencia("REF-001")).thenReturn(reabastecimientos);

        // Act
        List<Reabastecimiento> result = reabastecimientoService.getByReferencia("REF-001");

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.get(0).getId());
        assertEquals(1250.75, result.get(0).getPrecioTotal());
        verify(reabastecimientoRepository, times(1)).findByReferencia("REF-001");
    }

    @Test
    void testGetByReferencia_NotExists() {
        // Arrange
        when(reabastecimientoRepository.findByReferencia("INVALID-REF")).thenReturn(Collections.emptyList());

        // Act
        List<Reabastecimiento> result = reabastecimientoService.getByReferencia("INVALID-REF");

        // Assert
        assertTrue(result.isEmpty());
        verify(reabastecimientoRepository, times(1)).findByReferencia("INVALID-REF");
    }

@Test
void testGetByReferencia_NullReferencia() {
    // Arrange
    when(reabastecimientoRepository.findByReferencia(null)).thenReturn(Collections.emptyList());

    // Act
    List<Reabastecimiento> result = reabastecimientoService.getByReferencia(null);

    // Assert
    assertTrue(result.isEmpty());
    verify(reabastecimientoRepository, times(1)).findByReferencia(null);
}

    // Tests para getByFechaBetween()

    @Test
    void testGetByFechaBetween_Success() {
        // Arrange
        LocalDate fechaInicio = LocalDate.of(2023, 3, 1);
        LocalDate fechaFin = LocalDate.of(2023, 5, 1);
        when(reabastecimientoRepository.findByFechaBetween(fechaInicio, fechaFin)).thenReturn(reabastecimientos);

        // Act
        List<Reabastecimiento> result = reabastecimientoService.getByFechaBetween(fechaInicio, fechaFin);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(reabastecimientoRepository, times(1)).findByFechaBetween(fechaInicio, fechaFin);
    }

    @Test
    void testGetByFechaBetween_NoResults() {
        // Arrange
        LocalDate fechaInicio = LocalDate.of(2022, 1, 1);
        LocalDate fechaFin = LocalDate.of(2022, 12, 31);
        when(reabastecimientoRepository.findByFechaBetween(fechaInicio, fechaFin)).thenReturn(Collections.emptyList());

        // Act
        List<Reabastecimiento> result = reabastecimientoService.getByFechaBetween(fechaInicio, fechaFin);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(reabastecimientoRepository, times(1)).findByFechaBetween(fechaInicio, fechaFin);
    }

    @Test
    void testGetByFechaBetween_SameDate() {
        // Arrange
        LocalDate fecha = LocalDate.of(2023, 3, 15);
        when(reabastecimientoRepository.findByFechaBetween(fecha, fecha)).thenReturn(Collections.singletonList(reabastecimiento1));

        // Act
        List<Reabastecimiento> result = reabastecimientoService.getByFechaBetween(fecha, fecha);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("REF-001", result.get(0).getReferencia());
        verify(reabastecimientoRepository, times(1)).findByFechaBetween(fecha, fecha);
    }

    @Test
    void testGetByFechaBetween_InvertedDates() {
        // Arrange
        LocalDate fechaInicio = LocalDate.of(2023, 5, 1);
        LocalDate fechaFin = LocalDate.of(2023, 3, 1);
        when(reabastecimientoRepository.findByFechaBetween(fechaInicio, fechaFin)).thenReturn(Collections.emptyList());

        // Act
        List<Reabastecimiento> result = reabastecimientoService.getByFechaBetween(fechaInicio, fechaFin);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(reabastecimientoRepository, times(1)).findByFechaBetween(fechaInicio, fechaFin);
    }

    // Tests para getByProveedor()

    @Test
    void testGetByProveedor_Success() {
        // Arrange
        when(reabastecimientoRepository.findByProveedor(proveedor.getId())).thenReturn(reabastecimientos);

        // Act
        List<Reabastecimiento> result = reabastecimientoService.getByProveedor(proveedor.getId());

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(reabastecimientoRepository, times(1)).findByProveedor(proveedor.getId());
    }

    @Test
    void testGetByProveedor_NoResults() {
        // Arrange
        Proveedor otroProveedor = new Proveedor();
        otroProveedor.setId(2);
        otroProveedor.setName("Otro Proveedor");
        when(reabastecimientoRepository.findByProveedor(otroProveedor.getId())).thenReturn(Collections.emptyList());

        // Act
        List<Reabastecimiento> result = reabastecimientoService.getByProveedor(otroProveedor.getId());

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(reabastecimientoRepository, times(1)).findByProveedor(otroProveedor.getId());
    }

    @Test
    void testGetByProveedor_NullProveedor() {
        // Arrange
        when(reabastecimientoRepository.findByProveedor(null)).thenReturn(Collections.emptyList());

        // Act
        List<Reabastecimiento> result = reabastecimientoService.getByProveedor(null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(reabastecimientoRepository, times(1)).findByProveedor(null);
    }

    // Tests para save()

    @Test
    void testSave_NewReabastecimiento() {
        // Arrange
        Reabastecimiento nuevoReabastecimiento = new Reabastecimiento();
        nuevoReabastecimiento.setFecha(LocalDate.now());
        nuevoReabastecimiento.setPrecioTotal(500.0);
        nuevoReabastecimiento.setReferencia("REF-NEW");
        nuevoReabastecimiento.setProveedor(proveedor);
        nuevoReabastecimiento.setNegocio(negocio);
        
        Reabastecimiento savedReabastecimiento = new Reabastecimiento();
        savedReabastecimiento.setId(3);
        savedReabastecimiento.setFecha(nuevoReabastecimiento.getFecha());
        savedReabastecimiento.setPrecioTotal(nuevoReabastecimiento.getPrecioTotal());
        savedReabastecimiento.setReferencia(nuevoReabastecimiento.getReferencia());
        savedReabastecimiento.setProveedor(nuevoReabastecimiento.getProveedor());
        savedReabastecimiento.setNegocio(nuevoReabastecimiento.getNegocio());
        
        when(reabastecimientoRepository.save(any(Reabastecimiento.class))).thenReturn(savedReabastecimiento);

        // Act
        Reabastecimiento result = reabastecimientoService.save(nuevoReabastecimiento);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.getId());
        assertEquals("REF-NEW", result.getReferencia());
        verify(reabastecimientoRepository, times(1)).save(any(Reabastecimiento.class));
    }

    @Test
    void testSave_UpdateReabastecimiento() {
        // Arrange
        reabastecimiento1.setPrecioTotal(1500.0);
        when(reabastecimientoRepository.save(reabastecimiento1)).thenReturn(reabastecimiento1);

        // Act
        Reabastecimiento result = reabastecimientoService.save(reabastecimiento1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(1500.0, result.getPrecioTotal());
        verify(reabastecimientoRepository, times(1)).save(reabastecimiento1);
    }

    @Test
    void testSave_NullReabastecimiento() {
        // Arrange
        when(reabastecimientoRepository.save(null)).thenThrow(new IllegalArgumentException("Reabastecimiento cannot be null"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            reabastecimientoService.save(null);
        });
        verify(reabastecimientoRepository, times(1)).save(null);
    }

    // Tests para deleteById()

    @Test
    void testDeleteById_Success() {
        // Arrange
        doNothing().when(reabastecimientoRepository).deleteById(1);

        // Act
        reabastecimientoService.deleteById(1);

        // Assert
        verify(reabastecimientoRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteById_NotFound() {
        // Arrange
        doThrow(new RuntimeException("Reabastecimiento not found")).when(reabastecimientoRepository).deleteById(999);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            reabastecimientoService.deleteById(999);
        });
        verify(reabastecimientoRepository, times(1)).deleteById(999);
    }

    @Test
    void testDeleteById_NullId() {
        // Arrange
        doThrow(new IllegalArgumentException("ID cannot be null")).when(reabastecimientoRepository).deleteById(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            reabastecimientoService.deleteById(null);
        });
        verify(reabastecimientoRepository, times(1)).deleteById(null);
    }
}