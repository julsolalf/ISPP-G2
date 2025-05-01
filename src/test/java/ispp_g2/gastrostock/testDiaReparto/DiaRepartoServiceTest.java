package ispp_g2.gastrostock.testDiaReparto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import ispp_g2.gastrostock.diaReparto.DiaReparto;
import ispp_g2.gastrostock.diaReparto.DiaRepartoDTO;
import ispp_g2.gastrostock.diaReparto.DiaRepartoRepository;
import ispp_g2.gastrostock.diaReparto.DiaRepartoService;
import ispp_g2.gastrostock.exceptions.ResourceNotFoundException;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.proveedores.Proveedor;
import ispp_g2.gastrostock.proveedores.ProveedorRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.DayOfWeek;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class DiaRepartoServiceTest {

    @Mock
    private DiaRepartoRepository diaRepartoRepository;

    @Mock
    private ProveedorRepository proveedorRepository;

    @InjectMocks
    private DiaRepartoService diaRepartoService;

    private DiaReparto diaReparto;
    private Negocio negocio;
    private Proveedor proveedor;

    @BeforeEach
    void setUp() {
        negocio = new Negocio();
        negocio.setId(1);

        proveedor = new Proveedor();
        proveedor.setId(1);

        diaReparto = new DiaReparto();
        diaReparto.setId(1);
        diaReparto.setDiaSemana(DayOfWeek.MONDAY);
        diaReparto.setProveedor(proveedor);
    }

    @Test
    void testGetById_ExistingId() {
        when(diaRepartoRepository.findById(1)).thenReturn(Optional.of(diaReparto));

        DiaReparto result = diaRepartoService.getById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(diaRepartoRepository).findById(1);
    }

    @Test
    void testGetById_NonExistingId() {
        // Dado que el repositorio no encuentra el ID
        when(diaRepartoRepository.findById(99)).thenReturn(Optional.empty());
    
        assertThrows(ResourceNotFoundException.class, () -> {
            diaRepartoService.getById(99);
        });
    
        verify(diaRepartoRepository).findById(99);
    }


    @Test
    void testGetDiasReparto_EmptyList() {
        when(diaRepartoRepository.findAll()).thenReturn(Collections.emptyList());

        List<DiaReparto> result = diaRepartoService.getDiasReparto();

        assertTrue(result.isEmpty());
        verify(diaRepartoRepository).findAll();
    }

    @Test
    void testGetDiasReparto_MultipleEntries() {
        DiaReparto diaReparto2 = new DiaReparto();
        diaReparto2.setId(2);
        diaReparto2.setDiaSemana(DayOfWeek.TUESDAY);
        diaReparto2.setProveedor(proveedor);

        when(diaRepartoRepository.findAll()).thenReturn(List.of(diaReparto, diaReparto2));

        List<DiaReparto> result = diaRepartoService.getDiasReparto();

        assertEquals(2, result.size());
        verify(diaRepartoRepository).findAll();
    }

    @Test
    void testGetDiaRepartoByDiaSemana_NoResults() {
        when(diaRepartoRepository.findDiaRepartoByDiaSemana(DayOfWeek.WEDNESDAY)).thenReturn(Collections.emptyList());

        List<DiaReparto> result = diaRepartoService.getDiaRepartoByDiaSemana(DayOfWeek.WEDNESDAY);

        assertTrue(result.isEmpty());
        verify(diaRepartoRepository).findDiaRepartoByDiaSemana(DayOfWeek.WEDNESDAY);
    }

    @Test
    void testSave() {
        when(diaRepartoRepository.save(diaReparto)).thenReturn(diaReparto);

        DiaReparto result = diaRepartoService.save(diaReparto);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(diaRepartoRepository).save(diaReparto);
    }


    @Test
    void testSave_InvalidData() {
        DiaReparto invalidDiaReparto = new DiaReparto();
        invalidDiaReparto.setId(2);
        invalidDiaReparto.setProveedor(null);

        when(diaRepartoRepository.save(invalidDiaReparto)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> diaRepartoService.save(invalidDiaReparto));
    }

    @Test
    void testDeleteById_ExistingId() {
        doNothing().when(diaRepartoRepository).deleteById(1);

        diaRepartoService.deleteById(1);

        verify(diaRepartoRepository).deleteById(1);
    }

    @Test
    void testDeleteById_NonExistingId() {
        doNothing().when(diaRepartoRepository).deleteById(99);

        diaRepartoService.deleteById(99);

        verify(diaRepartoRepository).deleteById(99);
    }

    @Test
    void testGetDiaRepartoByProveedorId_NoResults() {
        when(diaRepartoRepository.findDiaRepartoByProveedorId(1)).thenReturn(Collections.emptyList());

        List<DiaReparto> result = diaRepartoService.getDiaRepartoByProveedorId(1);

        assertTrue(result.isEmpty());
        verify(diaRepartoRepository).findDiaRepartoByProveedorId(1);
    }

    @Test
    void testGetDiaRepartoByProveedorId_WithResults() {
        when(diaRepartoRepository.findDiaRepartoByProveedorId(1)).thenReturn(List.of(diaReparto));

        List<DiaReparto> result = diaRepartoService.getDiaRepartoByProveedorId(1);

        assertEquals(1, result.size());
        assertEquals(diaReparto, result.get(0));
        verify(diaRepartoRepository).findDiaRepartoByProveedorId(1);
    }

    @Test
    void testGetDiaRepartoByDiaSemana_WithResults() {
        when(diaRepartoRepository.findDiaRepartoByDiaSemana(DayOfWeek.MONDAY)).thenReturn(List.of(diaReparto));

        List<DiaReparto> result = diaRepartoService.getDiaRepartoByDiaSemana(DayOfWeek.MONDAY);

        assertEquals(1, result.size());
        assertEquals(diaReparto, result.get(0));
        verify(diaRepartoRepository).findDiaRepartoByDiaSemana(DayOfWeek.MONDAY);
    }

    @Test
    void testUpdate_Existing() {
        DiaReparto updated = new DiaReparto();
        updated.setId(1);
        updated.setDiaSemana(DayOfWeek.FRIDAY);
        updated.setProveedor(proveedor);
        updated.setDescripcion("Updated description");

        when(diaRepartoRepository.findById(1)).thenReturn(Optional.of(diaReparto));
        when(diaRepartoRepository.save(any(DiaReparto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DiaReparto result = diaRepartoService.update(1, updated);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(DayOfWeek.FRIDAY, result.getDiaSemana());
        assertEquals("Updated description", result.getDescripcion());
        verify(diaRepartoRepository).findById(1);
        verify(diaRepartoRepository).save(result);
    }

    @Test
    void testUpdate_NonExisting() {
        when(diaRepartoRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> diaRepartoService.update(99, diaReparto));
        verify(diaRepartoRepository).findById(99);
    }

    @Test
    void testConvertirDTODiaReparto_Success() {
        DiaRepartoDTO dto = new DiaRepartoDTO();
        dto.setId(2);
        dto.setDiaSemana(DayOfWeek.TUESDAY);
        dto.setDescripcion("Desc");
        dto.setProveedorId(1);

        when(proveedorRepository.findById(1)).thenReturn(Optional.of(proveedor));

        DiaReparto result = diaRepartoService.convertirDTODiaReparto(dto);

        assertNotNull(result);
        assertEquals(2, result.getId());
        assertEquals(DayOfWeek.TUESDAY, result.getDiaSemana());
        assertEquals("Desc", result.getDescripcion());
        assertEquals(proveedor, result.getProveedor());
        verify(proveedorRepository).findById(1);
    }

    @Test
    void testConvertirDTODiaReparto_ProveedorNotFound() {
        DiaRepartoDTO dto = new DiaRepartoDTO();
        dto.setProveedorId(99);

        when(proveedorRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> diaRepartoService.convertirDTODiaReparto(dto));
        verify(proveedorRepository).findById(99);
    }

    @Test
    void testGetDiaRepartoByDiaSemana_NullDay() {
        when(diaRepartoRepository.findDiaRepartoByDiaSemana(null)).thenReturn(List.of(diaReparto));

        List<DiaReparto> result = diaRepartoService.getDiaRepartoByDiaSemana(null);

        assertEquals(1, result.size());
        verify(diaRepartoRepository).findDiaRepartoByDiaSemana(null);
    }

    @Test
    void testGetDiaRepartoByProveedorId_NullId() {
        when(diaRepartoRepository.findDiaRepartoByProveedorId(null)).thenReturn(List.of(diaReparto));

        List<DiaReparto> result = diaRepartoService.getDiaRepartoByProveedorId(null);

        assertEquals(1, result.size());
        verify(diaRepartoRepository).findDiaRepartoByProveedorId(null);
    }

    @Test
    void testSave_RepositoryThrows() {
        when(diaRepartoRepository.save(diaReparto)).thenThrow(new RuntimeException("DB down"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> diaRepartoService.save(diaReparto));
        assertEquals("DB down", ex.getMessage());
        verify(diaRepartoRepository).save(diaReparto);
    }

    @Test
    void testUpdate_PreservesId() {
        DiaReparto updated = new DiaReparto();
        updated.setId(99);
        updated.setDiaSemana(DayOfWeek.FRIDAY);
        updated.setDescripcion("Nueva");
        updated.setProveedor(proveedor);

        when(diaRepartoRepository.findById(1)).thenReturn(Optional.of(diaReparto));
        when(diaRepartoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        DiaReparto result = diaRepartoService.update(1, updated);

        assertEquals(1, result.getId());
        assertEquals(DayOfWeek.FRIDAY, result.getDiaSemana());
        assertEquals("Nueva", result.getDescripcion());
    }

    @Test
    void testDeleteById_VerifyCalled() {
        doNothing().when(diaRepartoRepository).deleteById(5);

        diaRepartoService.deleteById(5);

        verify(diaRepartoRepository).deleteById(5);
    }

    @Test
    void testConvertirDTODiaReparto_NullDescripcion() {
        DiaRepartoDTO dto = new DiaRepartoDTO();
        dto.setId(3);
        dto.setDiaSemana(DayOfWeek.THURSDAY);
        dto.setProveedorId(1);
        when(proveedorRepository.findById(1)).thenReturn(Optional.of(proveedor));

        DiaReparto result = diaRepartoService.convertirDTODiaReparto(dto);

        assertEquals(3, result.getId());
        assertNull(result.getDescripcion());
    }

}
