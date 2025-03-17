package ispp_g2.gastrostock.testMesa;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import ispp_g2.gastrostock.mesa.Mesa;
import ispp_g2.gastrostock.mesa.MesaRepository;
import ispp_g2.gastrostock.mesa.MesaService;

@ActiveProfiles("test")
class MesaServiceTest {

    @Mock
    private MesaRepository mesaRepository;

    @InjectMocks
    private MesaService mesaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetById() {
        Mesa mesa = new Mesa();
        mesa.setId(1);
        mesa.setName("Mesa Grande");
        when(mesaRepository.findById(1)).thenReturn(mesa);

        Mesa found = mesaService.getById(1);
        assertNotNull(found);
        assertEquals("Mesa Grande", found.getName());
    }

    @Test
    void testGetByName() {
        Mesa mesa = new Mesa();
        mesa.setName("Mesa VIP");
        when(mesaRepository.findByName("Mesa VIP")).thenReturn(mesa);

        Mesa found = mesaService.getByName("Mesa VIP");
        assertNotNull(found);
        assertEquals("Mesa VIP", found.getName());
    }

    @Test
    void testGetMesas() {
        Mesa mesa1 = new Mesa();
        mesa1.setName("Mesa 1");

        Mesa mesa2 = new Mesa();
        mesa2.setName("Mesa 2");

        when(mesaRepository.findAll()).thenReturn(Arrays.asList(mesa1, mesa2));

        List<Mesa> mesas = mesaService.getMesas();
        assertEquals(2, mesas.size());
    }
    
    @Test
    void testGetMesasByNumeroAsientos() {
        Mesa mesa1 = new Mesa();
        mesa1.setNumeroAsientos(4);
        Mesa mesa2 = new Mesa();
        mesa2.setNumeroAsientos(4);

        when(mesaRepository.findMesasByNumeroAsientos(4)).thenReturn(Arrays.asList(mesa1, mesa2));

        List<Mesa> mesas = mesaService.getMesasByNumeroAsientos(4);
        assertEquals(2, mesas.size());
        assertEquals(4, mesas.get(0).getNumeroAsientos());
    }

    @Test
    void testSaveMesa() {
        Mesa mesa = new Mesa();
        mesa.setName("Mesa Nueva");
        mesa.setNumeroAsientos(6);

        when(mesaRepository.save(mesa)).thenReturn(mesa);

        Mesa saved = mesaService.saveMesa(mesa);
        assertNotNull(saved);
        assertEquals("Mesa Nueva", saved.getName());
        assertEquals(6, saved.getNumeroAsientos());
    }
}
