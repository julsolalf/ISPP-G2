package ispp_g2.gastrostock.testDiaReparto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ispp_g2.gastrostock.diaReparto.DiaReparto;
import ispp_g2.gastrostock.diaReparto.DiaRepartoRepository;
import ispp_g2.gastrostock.diaReparto.DiaRepartoService;
import ispp_g2.gastrostock.exceptions.ResourceNotFoundException;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.proveedores.Proveedor;
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

}

