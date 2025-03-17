package ispp_g2.gastrostock.testDueño;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;
import java.util.List;

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

import ispp_g2.gastrostock.dueño.Dueño;
import ispp_g2.gastrostock.dueño.DueñoController;
import ispp_g2.gastrostock.dueño.DueñoService;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class DueñoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DueñoService dueñoService;

    @InjectMocks
    private DueñoController dueñoController;

    private Dueño DueñoP;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(dueñoController).build();
        DueñoP = new Dueño();
        DueñoP.setId(1);
        DueñoP.setTokenDueño("testToken");
    }

    @Test
    void testGetAllDueños() throws Exception {
        when(dueñoService.getAllDueños()).thenReturn(List.of(DueñoP));

        mockMvc.perform(get("/api/v1/1/dueño"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testGetDueñoById() throws Exception {
        when(dueñoService.getDueñoById(1)).thenReturn(Optional.of(DueñoP));

        mockMvc.perform(get("/api/v1/1/dueño/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testGetDueñoByEmail() throws Exception {
        when(dueñoService.getDueñoByEmail("test@example.com")).thenReturn(Optional.of(DueñoP));

        mockMvc.perform(get("/api/v1/1/dueño/email/test@example.com"))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateDueño() throws Exception {
        when(dueñoService.saveDueño(any(Dueño.class))).thenReturn(DueñoP);

        mockMvc.perform(post("/api/v1/1/dueño")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"tokenDueño\": \"testToken\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteDueño() throws Exception {
        doNothing().when(dueñoService).deleteDueño(1);

        mockMvc.perform(delete("/api/v1/1/dueño/1"))
                .andExpect(status().isNoContent());
    }
}

