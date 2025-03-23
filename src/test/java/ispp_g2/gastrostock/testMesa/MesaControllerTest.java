package ispp_g2.gastrostock.testMesa;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ispp_g2.gastrostock.exceptions.ResourceNotFoundException;
import ispp_g2.gastrostock.mesa.Mesa;
import ispp_g2.gastrostock.mesa.MesaController;
import ispp_g2.gastrostock.mesa.MesaService;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@WebMvcTest(MesaController.class)
class MesaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MesaService mesaService;

    @InjectMocks
    private MesaController mesaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(mesaController).build();
    }

    @Test
    void testFindAll() throws Exception {
        Mesa mesa1 = new Mesa();
        mesa1.setName("Mesa 1");
        mesa1.setNumeroAsientos(4);

        Mesa mesa2 = new Mesa();
        mesa2.setName("Mesa 2");
        mesa2.setNumeroAsientos(6);

        List<Mesa> mesas = Arrays.asList(mesa1, mesa2);
        when(mesaService.getMesas()).thenReturn(mesas);

        mockMvc.perform(get("/api/mesas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testFindMesa() throws Exception {
        Mesa mesa = new Mesa();
        mesa.setId(1);
        mesa.setName("Mesa VIP");
        when(mesaService.getById(1)).thenReturn(mesa);

        mockMvc.perform(get("/api/mesas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mesa VIP"));
    }

    @Test
    void testFindByNumeroAsientos() throws Exception {
        Mesa mesa = new Mesa();
        mesa.setNumeroAsientos(4);
        List<Mesa> mesas = Arrays.asList(mesa);
        when(mesaService.getMesasByNumeroAsientos(4)).thenReturn(mesas);

        mockMvc.perform(get("/api/mesas/numeroAsientos/4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testModifyMesa() throws Exception {
        Mesa mesa = new Mesa();
        mesa.setId(1);
        mesa.setName("Mesa Nueva");
        mesa.setNumeroAsientos(4);

        when(mesaService.getById(1)).thenReturn(mesa);

        mockMvc.perform(put("/api/mesas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1, \"name\":\"Mesa Actualizada\", \"numeroAsientos\":4}"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testFindByName_Success() throws Exception {
        Mesa mesa = new Mesa();
        mesa.setName("Mesa VIP");
        mesa.setNumeroAsientos(4);

        when(mesaService.getByName("Mesa VIP")).thenReturn(mesa);

        mockMvc.perform(get("/api/mesas/name/Mesa VIP") 
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mesa VIP"))
                .andExpect(jsonPath("$.numeroAsientos").value(4));
    }

    @SuppressWarnings("null")
    @Test
    void testFindByName_NotFound() throws Exception {
        when(mesaService.getByName("Inexistente")).thenReturn(null);

        mockMvc.perform(get("/api/mesas/name/Inexistente")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("Mesa with name Inexistente not found!", 
                    result.getResolvedException().getMessage()));
    }
}
