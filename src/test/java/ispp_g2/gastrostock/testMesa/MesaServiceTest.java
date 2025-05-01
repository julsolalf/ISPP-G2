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
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import ispp_g2.gastrostock.dueno.Dueno;
import ispp_g2.gastrostock.exceptions.ResourceNotFoundException;
import ispp_g2.gastrostock.mesa.Mesa;
import ispp_g2.gastrostock.mesa.MesaDTO;
import ispp_g2.gastrostock.mesa.MesaRepository;
import ispp_g2.gastrostock.mesa.MesaService;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioRepository;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class MesaServiceTest {

    @Mock
    private MesaRepository mesaRepository;

    @Mock
    private NegocioRepository negocioRepository;

    @InjectMocks
    private MesaService mesaService;

    private Mesa mesa1, mesa2, mesa3, mesaInvalida;
    private Negocio negocio;
    private Dueno dueno;
    private MesaDTO mesaDTO;
    private List<Mesa> mesasList;
    private Mesa existing, updated;

    @BeforeEach
    void setUp() {
        dueno = new Dueno();
        dueno.setId(1);
        dueno.setFirstName("Juan");
        dueno.setLastName("García");
        dueno.setEmail("juan@example.com");
        dueno.setNumTelefono("652345678");
        dueno.setTokenDueno("TOKEN123");

        mesaDTO = new MesaDTO();
        mesaDTO.setNombre("Mesa Exterior");
        mesaDTO.setNumeroAsientos(4);
        mesaDTO.setNegocioId(1);
        negocio = new Negocio();
        negocio.setId(1);
        negocio.setName("Restaurante La Tasca");
        negocio.setDireccion("Calle Principal 123");
        negocio.setCiudad("Sevilla");
        negocio.setPais("Espana");
        negocio.setCodigoPostal("41001");
        negocio.setTokenNegocio(12345);
        negocio.setDueno(dueno);

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

        existing = new Mesa();
        existing.setId(1);
        existing.setName("Original");
        existing.setNumeroAsientos(4);
        updated = new Mesa();
        updated.setName("Modificada");
        updated.setNumeroAsientos(6);

        mesaInvalida = new Mesa();

        mesasList = Arrays.asList(mesa1, mesa2, mesa3);
    }


    @Test
    void testGetById_Success() {
        when(mesaRepository.findById(1)).thenReturn(Optional.of(mesa1));

        Mesa result = mesaService.getById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Mesa Exterior", result.getName());
        verify(mesaRepository).findById(1);
    }

    @Test
    void testGetById_NotFound() {
        when(mesaRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> { mesaService.getById(999);} );

        verify(mesaRepository).findById(999);
    }

    @SuppressWarnings("null")
    @Test
    void testGetById_NullId() {
        assertThrows(ResourceNotFoundException.class, () -> { mesaService.getById(null);} );
        verify(mesaRepository).findById(null);
    }


    @Test
    void testGetByName_Success() {
        when(mesaRepository.findMesaByName("Exterior")).thenReturn(mesa1);

        Mesa result = mesaService.getByName("Exterior");

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Mesa Exterior", result.getName());
        verify(mesaRepository).findMesaByName("Exterior");
    }

    @Test
    void testGetByName_NotFound() {
        when(mesaRepository.findMesaByName("Inexistente")).thenReturn(null);

        Mesa result = mesaService.getByName("Inexistente");

        assertNull(result);
        verify(mesaRepository).findMesaByName("Inexistente");
    }

    @Test
    void testGetByName_NullName() {
        when(mesaRepository.findMesaByName(null)).thenReturn(null);

        Mesa result = mesaService.getByName(null);

        assertNull(result);
        verify(mesaRepository).findMesaByName(null);
    }


    @Test
    void testGetMesas_Success() {
        when(mesaRepository.findAll()).thenReturn(mesasList);

        List<Mesa> result = mesaService.getMesas();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Mesa Exterior", result.get(0).getName());
        assertEquals("Mesa VIP", result.get(1).getName());
        assertEquals("Mesa Barra", result.get(2).getName());
        verify(mesaRepository).findAll();
    }

    @Test
    void testGetMesas_EmptyList() {
        when(mesaRepository.findAll()).thenReturn(Collections.emptyList());

        List<Mesa> result = mesaService.getMesas();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mesaRepository).findAll();
    }


    @Test
    void testGetMesasByNumeroAsientos_Success() {
        List<Mesa> mesasConCuatroAsientos = Collections.singletonList(mesa1);
        when(mesaRepository.findMesaByNumeroAsientos(4)).thenReturn(mesasConCuatroAsientos);

        List<Mesa> result = mesaService.getMesasByNumeroAsientos(4);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Mesa Exterior", result.get(0).getName());
        assertEquals(4, result.get(0).getNumeroAsientos());
        verify(mesaRepository).findMesaByNumeroAsientos(4);
    }

    @Test
    void testGetMesasByNumeroAsientos_Multiple() {
        Mesa otraMesa = new Mesa();
        otraMesa.setId(4);
        otraMesa.setName("Otra Mesa");
        otraMesa.setNumeroAsientos(4);
        otraMesa.setNegocio(negocio);

        List<Mesa> mesasConCuatroAsientos = Arrays.asList(mesa1, otraMesa);
        when(mesaRepository.findMesaByNumeroAsientos(4)).thenReturn(mesasConCuatroAsientos);

        List<Mesa> result = mesaService.getMesasByNumeroAsientos(4);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(4, result.get(0).getNumeroAsientos());
        assertEquals(4, result.get(1).getNumeroAsientos());
        verify(mesaRepository).findMesaByNumeroAsientos(4);
    }

    @Test
    void testGetMesasByNumeroAsientos_NotFound() {
        when(mesaRepository.findMesaByNumeroAsientos(10)).thenReturn(Collections.emptyList());

        List<Mesa> result = mesaService.getMesasByNumeroAsientos(10);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mesaRepository).findMesaByNumeroAsientos(10);
    }

    @Test
    void testGetMesasByNumeroAsientos_Zero() {
        when(mesaRepository.findMesaByNumeroAsientos(0)).thenReturn(Collections.emptyList());

        List<Mesa> result = mesaService.getMesasByNumeroAsientos(0);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mesaRepository).findMesaByNumeroAsientos(0);
    }

    @Test
    void testGetMesasByNumeroAsientos_Negative() {
        when(mesaRepository.findMesaByNumeroAsientos(-1)).thenReturn(Collections.emptyList());

        List<Mesa> result = mesaService.getMesasByNumeroAsientos(-1);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mesaRepository).findMesaByNumeroAsientos(-1);
    }


    @Test
    void testGetMesasByNegocio_Success() {
        when(mesaRepository.findMesasByNegocio(1)).thenReturn(mesasList);

        List<Mesa> result = mesaService.getMesasByNegocio(1);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(1, result.get(0).getNegocio().getId());
        assertEquals(1, result.get(1).getNegocio().getId());
        assertEquals(1, result.get(2).getNegocio().getId());
        verify(mesaRepository).findMesasByNegocio(1);
    }

    @Test
    void testGetMesasByNegocio_NotFound() {
        when(mesaRepository.findMesasByNegocio(999)).thenReturn(Collections.emptyList());

        List<Mesa> result = mesaService.getMesasByNegocio(999);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mesaRepository).findMesasByNegocio(999);
    }

    @Test
    void testGetMesasByNegocio_NullId() {
        when(mesaRepository.findMesasByNegocio(null)).thenReturn(Collections.emptyList());

        List<Mesa> result = mesaService.getMesasByNegocio(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mesaRepository).findMesasByNegocio(null);
    }


    @Test
    void testSave_Success() {
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

        Mesa result = mesaService.save(nuevaMesa);

        assertNotNull(result);
        assertEquals(4, result.getId());
        assertEquals("Mesa Nueva", result.getName());
        verify(mesaRepository).save(nuevaMesa);
    }

    @Test
    void testSave_Update() {
        mesa1.setNumeroAsientos(10); 

        when(mesaRepository.save(any(Mesa.class))).thenReturn(mesa1);

        Mesa result = mesaService.save(mesa1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(10, result.getNumeroAsientos()); 
        verify(mesaRepository).save(mesa1);
    }

    @Test
    void testSave_InvalidData() {
        when(mesaRepository.save(mesaInvalida)).thenReturn(mesaInvalida);

        Mesa result = mesaService.save(mesaInvalida);

        assertNotNull(result);
        verify(mesaRepository).save(mesaInvalida);
    }

    @SuppressWarnings("null")
    @Test
    void testSave_NullMesa() {
        when(mesaRepository.save(null)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> mesaService.save(null));
        verify(mesaRepository).save(null);
    }


    @Test
    void testDeleteById_Success() {
        doNothing().when(mesaRepository).deleteById(1);

        mesaService.deleteById(1);

        verify(mesaRepository).deleteById(1);
    }

    @Test
    void testDeleteById_NotFound() {
        doThrow(new RuntimeException("Mesa no encontrada")).when(mesaRepository).deleteById(999);

        assertThrows(RuntimeException.class, () -> mesaService.deleteById(999));
        verify(mesaRepository).deleteById(999);
    }

    @SuppressWarnings("null")
    @Test
    void testDeleteById_NullId() {
        doThrow(new IllegalArgumentException("ID no puede ser null")).when(mesaRepository).deleteById(null);

        assertThrows(IllegalArgumentException.class, () -> mesaService.deleteById(null));
        verify(mesaRepository).deleteById(null);
    }

    @Test
    void testConvertirMesa_Success() {
        when(negocioRepository.findById(1)).thenReturn(Optional.of(negocio));

        Mesa result = mesaService.convertirMesa(mesaDTO);

        assertNotNull(result);
        assertEquals("Mesa Exterior", result.getName());
        assertEquals(4, result.getNumeroAsientos());
        assertEquals(negocio, result.getNegocio());
        verify(negocioRepository).findById(1);
    }

    @Test
    void testConvertirMesa_NegocioNotFound() {
        when(negocioRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            mesaService.convertirMesa(mesaDTO);
        });

        verify(negocioRepository).findById(1);
    }

    @Test
    void testConvertirMesa_NullNegocioId() {
        mesaDTO.setNegocioId(null);

        assertThrows(ResourceNotFoundException.class, () -> {
            mesaService.convertirMesa(mesaDTO);
        });

        verify(negocioRepository, times(0)).findById(anyInt());
    }

    @Test
    void testUpdate_Success() {
        when(mesaRepository.findById(1)).thenReturn(Optional.of(existing));
        when(mesaRepository.save(any(Mesa.class))).thenAnswer(inv -> inv.getArgument(0));
        Mesa result = mesaService.update(updated, 1);
        assertEquals(1, result.getId());
        assertEquals("Modificada", result.getName());
        assertEquals(6, result.getNumeroAsientos());
        verify(mesaRepository).findById(1);
        verify(mesaRepository).save(existing);
    }

    @Test
    void testUpdate_NotFound() {
        when(mesaRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> mesaService.update(updated, 999));
        verify(mesaRepository).findById(999);
        verify(mesaRepository, never()).save(any());
    }
}