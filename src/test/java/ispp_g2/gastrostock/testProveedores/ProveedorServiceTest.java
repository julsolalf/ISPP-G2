package ispp_g2.gastrostock.testProveedores;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import ispp_g2.gastrostock.proveedores.Proveedor;
import ispp_g2.gastrostock.proveedores.ProveedorDTO;
import ispp_g2.gastrostock.proveedores.ProveedorRepository;
import ispp_g2.gastrostock.proveedores.ProveedorService;
import ispp_g2.gastrostock.diaReparto.DiaReparto;
import ispp_g2.gastrostock.exceptions.ResourceNotFoundException;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioRepository;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class ProveedorServiceTest {

    @Mock
    private ProveedorRepository proveedorRepository;
    
    @Mock
    private NegocioRepository negocioRepository;

    @InjectMocks
    private ProveedorService proveedorService;

    

    private Proveedor proveedor1;
    private Proveedor proveedor2;
    private Proveedor proveedor3;
    private List<Proveedor> proveedores;

    @BeforeEach
    void setUp() {
        // Crear proveedores de prueba
        proveedor1 = new Proveedor();
        proveedor1.setId(1);
        proveedor1.setName("Distribuciones Alimentarias S.L.");
        proveedor1.setEmail("distribuciones@example.com");
        proveedor1.setTelefono("954111222");
        proveedor1.setDireccion("Polígono Industrial, Nave 7");
        Set<DiaReparto> diasReparto1 = new HashSet<>();
        DiaReparto dia1 = new DiaReparto();
        dia1.setDiaSemana(DayOfWeek.MONDAY);
        diasReparto1.add(dia1);
        DiaReparto dia2 = new DiaReparto();
        dia2.setDiaSemana(DayOfWeek.WEDNESDAY);
        diasReparto1.add(dia2);

        proveedor2 = new Proveedor();
        proveedor2.setId(2);
        proveedor2.setName("Productos Frescos del Sur");
        proveedor2.setEmail("frescos@example.com");
        proveedor2.setTelefono("954333444");
        proveedor2.setDireccion("Avenida de la Industria, 42");
        Set<DiaReparto> diasReparto2 = new HashSet<>();
        DiaReparto dia3 = new DiaReparto();
        dia3.setDiaSemana(DayOfWeek.TUESDAY);
        diasReparto2.add(dia3);
        DiaReparto dia4 = new DiaReparto();
        dia4.setDiaSemana(DayOfWeek.FRIDAY);
        diasReparto2.add(dia4);

        // Crear un tercer proveedor para casos extremos
        proveedor3 = new Proveedor();
        proveedor3.setId(3);
        proveedor3.setName("Distribuciones Rápidas");
        proveedor3.setEmail("rapidas@example.com");
        proveedor3.setTelefono("954555666");
        proveedor3.setDireccion("Calle Comercio, 15");
        // Proveedor sin días de reparto asignados

        // Lista de proveedores para los métodos que devuelven colecciones
        proveedores = Arrays.asList(proveedor1, proveedor2, proveedor3);
    }

    // Tests para getAll()

    @Test
    void testGetAll_Success() {
        // Arrange
        when(proveedorRepository.findAll()).thenReturn(proveedores);

        // Act
        List<Proveedor> result = proveedorService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Distribuciones Alimentarias S.L.", result.get(0).getName());
        assertEquals("Productos Frescos del Sur", result.get(1).getName());
        verify(proveedorRepository, times(1)).findAll();
    }

    @Test
    void testGetAll_EmptyList() {
        // Arrange
        when(proveedorRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Proveedor> result = proveedorService.findAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(proveedorRepository, times(1)).findAll();
    }

    // Tests para getById()

    @Test
    void testGetById_Exists() {
        // Arrange
        when(proveedorRepository.findById(1)).thenReturn(Optional.of(proveedor1));

        // Act
        Optional<Proveedor> result = Optional.of(proveedorService.findById(1));

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Distribuciones Alimentarias S.L.", result.get().getName());
        verify(proveedorRepository, times(1)).findById(1);
    }
    
    @Test
    void testGetById_NotExists() {
        // Arrange
        when(proveedorRepository.findById(999)).thenReturn(Optional.empty());
    
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            proveedorService.findById(999);
        });
    
        verify(proveedorRepository, times(1)).findById(999);
    }

    @Test
    void testGetById_NullId() {
        // Arrange
        when(proveedorRepository.findById(null)).thenThrow(new IllegalArgumentException("ID cannot be null"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            proveedorService.findById(null);
        });
        verify(proveedorRepository, times(1)).findById(null);
    }

    // Tests para findByEmail

    @Test
    void testFindByEmail_Found() {
        // Arrange
        when(proveedorRepository.findByEmail("distribuciones@example.com")).thenReturn(proveedor1);

        // Act
        Proveedor result = proveedorService.findByEmail("distribuciones@example.com");

        // Assert
        assertNotNull(result);
        assertEquals("Distribuciones Alimentarias S.L.", result.getName());
        verify(proveedorRepository, times(1)).findByEmail("distribuciones@example.com");
    }

    @Test
    void testFindByEmail_NotFound() {
        // Arrange
        when(proveedorRepository.findByEmail("noexiste@example.com")).thenReturn(null);

        // Act
        Proveedor result = proveedorService.findByEmail("noexiste@example.com");

        // Assert
        assertNull(result);
        verify(proveedorRepository, times(1)).findByEmail("noexiste@example.com");
    }

    @Test
    void testFindByEmail_NullEmail() {
        // Arrange
        when(proveedorRepository.findByEmail(null)).thenReturn(null);

        // Act
        Proveedor result = proveedorService.findByEmail(null);

        // Assert
        assertNull(result);
        verify(proveedorRepository, times(1)).findByEmail(null);
    }

    // Tests para findByTelefono

    @Test
    void testFindByTelefono_Found() {
        // Arrange
        when(proveedorRepository.findByTelefono("954111222")).thenReturn(proveedor1);

        // Act
        Proveedor result = proveedorService.findByTelefono("954111222");

        // Assert
        assertNotNull(result);
        assertEquals("Distribuciones Alimentarias S.L.", result.getName());
        verify(proveedorRepository, times(1)).findByTelefono("954111222");
    }

    @Test
    void testFindByTelefono_NotFound() {
        // Arrange
        when(proveedorRepository.findByTelefono("999999999")).thenReturn(null);

        // Act
        Proveedor result = proveedorService.findByTelefono("999999999");

        // Assert
        assertNull(result);
        verify(proveedorRepository, times(1)).findByTelefono("999999999");
    }

    @Test
    void testFindByTelefono_NullTelefono() {
        // Arrange
        when(proveedorRepository.findByTelefono(null)).thenReturn(null);

        // Act
        Proveedor result = proveedorService.findByTelefono(null);

        // Assert
        assertNull(result);
        verify(proveedorRepository, times(1)).findByTelefono(null);
    }

    // Tests para findByDireccion

    @Test
    void testFindByDireccion_Found() {
        // Arrange
        when(proveedorRepository.findByDireccion("Polígono Industrial, Nave 7")).thenReturn(proveedor1);

        // Act
        Proveedor result = proveedorService.findByDireccion("Polígono Industrial, Nave 7");

        // Assert
        assertNotNull(result);
        assertEquals("Distribuciones Alimentarias S.L.", result.getName());
        verify(proveedorRepository, times(1)).findByDireccion("Polígono Industrial, Nave 7");
    }

    @Test
    void testFindByDireccion_NotFound() {
        // Arrange
        when(proveedorRepository.findByDireccion("Dirección inexistente")).thenReturn(null);

        // Act
        Proveedor result = proveedorService.findByDireccion("Dirección inexistente");

        // Assert
        assertNull(result);
        verify(proveedorRepository, times(1)).findByDireccion("Dirección inexistente");
    }

    @Test
    void testFindByDireccion_NullDireccion() {
        // Arrange
        when(proveedorRepository.findByDireccion(null)).thenReturn(null);

        // Act
        Proveedor result = proveedorService.findByDireccion(null);

        // Assert
        assertNull(result);
        verify(proveedorRepository, times(1)).findByDireccion(null);
    }

    // Tests para findByNombre

    @Test
    void testFindByNombre_Found() {
        // Arrange
        when(proveedorRepository.findByNombre("Distribuciones Alimentarias S.L.")).thenReturn(proveedor1);

        // Act
        Proveedor result = proveedorService.findByNombre("Distribuciones Alimentarias S.L.");

        // Assert
        assertNotNull(result);
        assertEquals("Distribuciones Alimentarias S.L.", result.getName());
        verify(proveedorRepository, times(1)).findByNombre("Distribuciones Alimentarias S.L.");
    }

    @Test
    void testFindByNombre_NotFound() {
        // Arrange
        when(proveedorRepository.findByNombre("Nombre inexistente")).thenReturn(null);

        // Act
        Proveedor result = proveedorService.findByNombre("Nombre inexistente");

        // Assert
        assertNull(result);
        verify(proveedorRepository, times(1)).findByNombre("Nombre inexistente");
    }

    @Test
    void testFindByNombre_NullNombre() {
        // Arrange
        when(proveedorRepository.findByNombre(null)).thenReturn(null);

        // Act
        Proveedor result = proveedorService.findByNombre(null);

        // Assert
        assertNull(result);
        verify(proveedorRepository, times(1)).findByNombre(null);
    }

    // Tests para getByFirstName()

    @Test
    void testGetByFirstName_Found() {
        // Arrange
        List<Proveedor> distribuciones = Arrays.asList(proveedor1, proveedor3);
        when(proveedorRepository.findByNombre("Distri")).thenReturn(proveedor1);

        // Act
        Proveedor result = proveedorService.findByNombre("Distri");

        // Assert
        assertNotNull(result);
        assertTrue(result.getName().contains("Distribuciones"));
        verify(proveedorRepository, times(1)).findByNombre("Distri");
    }

    @Test
    void testGetByFirstName_NotFound() {
        // Arrange
        when(proveedorRepository.findByNombre("Inexistente")).thenReturn(any());

        // Act
        Proveedor result = proveedorService.findByNombre("Inexistente");

        // Assert
        assertNull(result);
        verify(proveedorRepository, times(1)).findByNombre("Inexistente");
    }

    @Test
    void testGetByName_EmptyString() {
        // Arrange
        when(proveedorRepository.findByNombre("")).thenReturn(proveedor1);

        // Act
        Proveedor result = proveedorService.findByNombre("");

        // Assert
        assertNotNull(result);
        verify(proveedorRepository, times(1)).findByNombre("");
    }

    // Tests para save()

    @Test
    void testSave_NewProveedor() {
        // Arrange
        Proveedor newProveedor = new Proveedor();
        newProveedor.setName("Nuevo Proveedor S.A.");
        newProveedor.setEmail("nuevo@example.com");
        newProveedor.setTelefono("954777888");
        newProveedor.setDireccion("Calle Nueva, 123");
        
        Proveedor savedProveedor = new Proveedor();
        savedProveedor.setId(4);
        savedProveedor.setName("Nuevo Proveedor S.A.");
        savedProveedor.setEmail("nuevo@example.com");
        savedProveedor.setTelefono("954777888");
        savedProveedor.setDireccion("Calle Nueva, 123");
        
        when(proveedorRepository.save(any(Proveedor.class))).thenReturn(savedProveedor);

        // Act
        Proveedor result = proveedorService.save(newProveedor);

        // Assert
        assertNotNull(result);
        assertEquals(4, result.getId());
        assertEquals("Nuevo Proveedor S.A.", result.getName());
        verify(proveedorRepository, times(1)).save(any(Proveedor.class));
    }

    @Test
    void testSave_ExistingProveedor() {
        // Arrange
        proveedor1.setName("Nombre Actualizado S.L.");
        when(proveedorRepository.save(proveedor1)).thenReturn(proveedor1);

        // Act
        Proveedor result = proveedorService.save(proveedor1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Nombre Actualizado S.L.", result.getName());
        verify(proveedorRepository, times(1)).save(proveedor1);
    }

    @Test
    void testSave_NullProveedor() {
        // Arrange
        when(proveedorRepository.save(null)).thenThrow(new IllegalArgumentException("Proveedor cannot be null"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            proveedorService.save(null);
        });
        verify(proveedorRepository, times(1)).save(null);
    }

    // Tests para deleteById()

    @Test
    void testDeleteById_Success() {
        // Arrange
        doNothing().when(proveedorRepository).deleteById(1);

        // Act
        proveedorService.deleteById(1);

        // Assert
        verify(proveedorRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteById_NotFound() {
        // Arrange
        doThrow(new RuntimeException("Proveedor not found")).when(proveedorRepository).deleteById(999);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            proveedorService.deleteById(999);
        });
        verify(proveedorRepository, times(1)).deleteById(999);
    }

    @Test
    void testDeleteById_NullId() {
        // Arrange
        doThrow(new IllegalArgumentException("ID cannot be null")).when(proveedorRepository).deleteById(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            proveedorService.deleteById(null);
        });
        verify(proveedorRepository, times(1)).deleteById(null);
 
    }

    @Test
    void testFindProveedorByNegocioId_Success() {
        when(proveedorRepository.findProveedorByNegocioId(1)).thenReturn(Arrays.asList(proveedor1, proveedor2));
        List<Proveedor> result = proveedorService.findProveedorByNegocioId(1);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(proveedor1, result.get(0));
        assertEquals(proveedor2, result.get(1));
        verify(proveedorRepository, times(1)).findProveedorByNegocioId(1);
    }

    @Test
    void testFindProveedorByNegocioId_EmptyList() {
        when(proveedorRepository.findProveedorByNegocioId(42)).thenReturn(Collections.emptyList());
        List<Proveedor> result = proveedorService.findProveedorByNegocioId(42);
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(proveedorRepository, times(1)).findProveedorByNegocioId(42);
    }

    @Test
    void testFindProveedorByDuenoId_Success() {
        when(proveedorRepository.findProveedorByDuenoId(5)).thenReturn(Arrays.asList(proveedor2, proveedor3));
        List<Proveedor> result = proveedorService.findProveedorByDuenoId(5);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(proveedor2, result.get(0));
        assertEquals(proveedor3, result.get(1));
        verify(proveedorRepository, times(1)).findProveedorByDuenoId(5);
    }

    @Test
    void testFindProveedorByDuenoId_EmptyList() {
        when(proveedorRepository.findProveedorByDuenoId(99)).thenReturn(Collections.emptyList());
        List<Proveedor> result = proveedorService.findProveedorByDuenoId(99);
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(proveedorRepository, times(1)).findProveedorByDuenoId(99);
    }



    @Test
    void testConvertirDTOProveedor_Success() {
        ProveedorDTO dto = new ProveedorDTO();
        dto.setName("Test");
        dto.setEmail("t@example.com");
        dto.setTelefono("123");
        dto.setDireccion("Dir");
        dto.setNegocioId(1);
        Negocio negocio = new Negocio();
        negocio.setId(1);
        when(negocioRepository.findById(1)).thenReturn(Optional.of(negocio));

        Proveedor result = proveedorService.convertirDTOProveedor(dto);

        assertEquals("Test", result.getName());
        assertEquals("t@example.com", result.getEmail());
        assertEquals("123", result.getTelefono());
        assertEquals("Dir", result.getDireccion());
        assertEquals(negocio, result.getNegocio());
        verify(negocioRepository).findById(1);
    }

    @Test
    void testConvertirDTOProveedor_NegocioNotFound_Throws() {
        ProveedorDTO dto = new ProveedorDTO();
        dto.setNegocioId(99);
        when(negocioRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
            proveedorService.convertirDTOProveedor(dto)
        );
        verify(negocioRepository).findById(99);
    }

    @Test
    void testConvertirProveedorDTO() {
        Proveedor p = new Proveedor();
        p.setName("A");
        p.setEmail("a@a");
        p.setTelefono("000");
        p.setDireccion("D");
        Negocio n = new Negocio(); n.setId(5);
        p.setNegocio(n);

        ProveedorDTO dto = proveedorService.convertirProveedorDTO(p);

        assertEquals("A", dto.getName());
        assertEquals("a@a", dto.getEmail());
        assertEquals("000", dto.getTelefono());
        assertEquals("D", dto.getDireccion());
        assertEquals(5, dto.getNegocioId());
    }

    @Test
    void testUpdate_Success() {
        Proveedor existing = new Proveedor();
        existing.setId(1);
        existing.setName("Old");
        when(proveedorRepository.findById(1)).thenReturn(Optional.of(existing));
        Proveedor input = new Proveedor();
        input.setName("New");
        input.setEmail("n@e");
        input.setTelefono("111");
        input.setDireccion("X");
        when(proveedorRepository.save(existing)).thenReturn(existing);

        Proveedor result = proveedorService.update(1, input);

        assertEquals("New", result.getName());
        assertEquals("n@e", result.getEmail());
        assertEquals("111", result.getTelefono());
        assertEquals("X", result.getDireccion());
        verify(proveedorRepository).findById(1);
        verify(proveedorRepository).save(existing);
    }

    @Test
    void testUpdate_NotFound_Throws() {
        when(proveedorRepository.findById(2)).thenReturn(Optional.empty());
        Proveedor input = new Proveedor();
        assertThrows(ResourceNotFoundException.class, () -> proveedorService.update(2, input));
        verify(proveedorRepository).findById(2);
    }

}
