package ispp_g2.gastrostock.testDiaReparto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import ispp_g2.gastrostock.diaReparto.DiaReparto;
import ispp_g2.gastrostock.diaReparto.DiaRepartoController;
import ispp_g2.gastrostock.diaReparto.DiaRepartoService;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.proveedores.Proveedor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.time.DayOfWeek;
import java.util.List;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class DiaRepartoControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private DiaRepartoService diaRepartoService;

    @InjectMocks
    private DiaRepartoController diaRepartoController;

    private DiaReparto diaReparto;
    private Negocio negocio;
    private Proveedor proveedor;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(diaRepartoController).build();

        negocio = new Negocio();
        negocio.setId(1);

        proveedor = new Proveedor();
        proveedor.setId(1);

        diaReparto = new DiaReparto();
        diaReparto.setId(1);
        diaReparto.setDiaSemana(DayOfWeek.MONDAY);
        diaReparto.setNegocio(negocio);
        diaReparto.setProveedor(proveedor);
    }

    @Test
    void testFindAll_WhenDataExists_ReturnsOk() throws Exception {
        when(diaRepartoService.getDiasReparto()).thenReturn(List.of(diaReparto));

        mockMvc.perform(get("/api/diasReparto"))
                .andExpect(status().isOk());
    }

    @Test
    void testFindAll_WhenNoData_ReturnsNotFound() throws Exception {
        when(diaRepartoService.getDiasReparto()).thenReturn(List.of());

        mockMvc.perform(get("/api/diasReparto"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindById_WhenExists_ReturnsOk() throws Exception {
        when(diaRepartoService.getById(1)).thenReturn(diaReparto);

        mockMvc.perform(get("/api/diasReparto/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testFindById_WhenNotExists_ReturnsNotFound() throws Exception {
        when(diaRepartoService.getById(99)).thenReturn(null);

        mockMvc.perform(get("/api/diasReparto/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindByDiaSemana_WhenExists_ReturnsOk() throws Exception {
        when(diaRepartoService.getDiaRepartoByDiaSemana(DayOfWeek.MONDAY)).thenReturn(List.of(diaReparto));

        mockMvc.perform(get("/api/diasReparto/diaSemana/MONDAY"))
                .andExpect(status().isOk());
    }

    @Test
    void testFindByDiaSemana_WhenNotExists_ReturnsNotFound() throws Exception {
        when(diaRepartoService.getDiaRepartoByDiaSemana(DayOfWeek.WEDNESDAY)).thenReturn(List.of());

        mockMvc.perform(get("/api/diasReparto/diaSemana/WEDNESDAY"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSave_WhenValid_ReturnsCreated() throws Exception {
        when(diaRepartoService.save(any())).thenReturn(diaReparto);

        mockMvc.perform(post("/api/diasReparto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(diaReparto)))
                .andExpect(status().isCreated());
    }

    @Test
    void testSave_WhenInvalid_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/diasReparto")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdate_WhenExists_ReturnsOk() throws Exception {
        when(diaRepartoService.getById(1)).thenReturn(diaReparto);
        when(diaRepartoService.save(any())).thenReturn(diaReparto);

        mockMvc.perform(put("/api/diasReparto/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(diaReparto)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdate_WhenNotExists_ReturnsNotFound() throws Exception {
        when(diaRepartoService.getById(99)).thenReturn(null);

        mockMvc.perform(put("/api/diasReparto/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(diaReparto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDelete_WhenExists_ReturnsNoContent() throws Exception {
        when(diaRepartoService.getById(1)).thenReturn(diaReparto);
        doNothing().when(diaRepartoService).deleteById(1);

        mockMvc.perform(delete("/api/diasReparto/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDelete_WhenNotExists_ReturnsNotFound() throws Exception {
        when(diaRepartoService.getById(99)).thenReturn(null);

        mockMvc.perform(delete("/api/diasReparto/99"))
                .andExpect(status().isNotFound());
    }
}

