package ispp_g2.gastrostock.testMesa;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import ispp_g2.gastrostock.dueno.Dueno;
import ispp_g2.gastrostock.dueno.DuenoService;
import ispp_g2.gastrostock.empleado.Empleado;
import ispp_g2.gastrostock.empleado.EmpleadoService;
import ispp_g2.gastrostock.exceptions.ExceptionHandlerController;
import ispp_g2.gastrostock.exceptions.ResourceNotFoundException;
import ispp_g2.gastrostock.mesa.Mesa;
import ispp_g2.gastrostock.mesa.MesaController;
import ispp_g2.gastrostock.mesa.MesaDTO;
import ispp_g2.gastrostock.mesa.MesaService;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioService;
import ispp_g2.gastrostock.user.Authorities;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserService;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class MesaControllerTest {

    private MockMvc mockMvc;

    @Mock 
    private MesaService mesaService;
    @Mock 
    private UserService userService;
    @Mock 
    private NegocioService negocioService;
    @Mock 
    private EmpleadoService empleadoService;
    @Mock 
    private DuenoService duenoService;
    
    @InjectMocks
    private MesaController mesaController;

    private ObjectMapper objectMapper;

    private User adminUser;
    private Mesa mesa1, mesa2, mesa3, mesaInvalida, mesaNueva;
    private Negocio negocio;
    private Dueno dueno;
    private List<Mesa> mesasList;
    private User duenoUser;
    private User empleadoUser;
    private Empleado empleado;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(mesaController)
                .setControllerAdvice(new ExceptionHandlerController())
                .build();
        
        objectMapper = new ObjectMapper();

        adminUser = new User();
        Authorities adminAuthority = new Authorities();
        adminAuthority.setAuthority("admin");  
        adminUser.setAuthority(adminAuthority);
        
        empleadoUser = new User();
        Authorities empleadoAuth = new Authorities();
        empleadoAuth.setAuthority("empleado");
        empleadoUser.setAuthority(empleadoAuth);
        empleadoUser.setUsername("empleado1");

        empleado = new Empleado();
        empleado.setId(1);
        empleado.setUser(empleadoUser);

        duenoUser = new User();
        Authorities duenoAuth = new Authorities();
        duenoAuth.setAuthority("dueno");
        duenoUser.setAuthority(duenoAuth);
        duenoUser.setUsername("dueno1");

        duenoUser.setId(1);
        empleadoUser.setId(1);

        dueno = new Dueno();
        dueno.setId(1);
        dueno.setFirstName("Juan");
        dueno.setLastName("García");
        dueno.setEmail("juan@example.com");
        dueno.setNumTelefono("652345678");
        dueno.setTokenDueno("TOKEN123");
        dueno.setUser(duenoUser);

        negocio = new Negocio();
        negocio.setId(1);
        negocio.setName("Restaurante La Tasca");
        negocio.setDireccion("Calle Principal 123");
        negocio.setCiudad("Sevilla");
        negocio.setPais("Espana");
        negocio.setCodigoPostal("41001");
        negocio.setTokenNegocio(12345);
        negocio.setDueno(dueno);
        empleado.setNegocio(negocio);

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

        mesaInvalida = new Mesa();

        mesaNueva = new Mesa();
        mesaNueva.setName("Mesa Nueva");
        mesaNueva.setNumeroAsientos(8);
        mesaNueva.setNegocio(negocio);

        mesasList = Arrays.asList(mesa1, mesa2, mesa3);
    }


    @Test
    void testFindAll_Success() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(mesaService.getMesas()).thenReturn(mesasList);
        
        mockMvc.perform(get("/api/mesas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Mesa Exterior")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Mesa VIP")))
                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[2].name", is("Mesa Barra")));
        
        verify(mesaService,atLeastOnce()).getMesas();
    }
    
    @Test
    void testFindAll_NoContent() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(mesaService.getMesas()).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/api/mesas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
        
        verify(mesaService).getMesas();
    }

    
    @Test
    void testFindById_Success() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(mesaService.getById(1)).thenReturn(mesa1);
        
        mockMvc.perform(get("/api/mesas/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Mesa Exterior")))
                .andExpect(jsonPath("$.numeroAsientos", is(4)));
        
        verify(mesaService).getById(1);
    }
    
    @Test
    void testFindById_NotFound() throws Exception {
        when(mesaService.getById(999)).thenThrow(new ResourceNotFoundException("La mesa no existe"));
        when(userService.findCurrentUser()).thenReturn(adminUser);
        
        mockMvc.perform(get("/api/mesas/999"))
                .andExpect(status().isNotFound());
    
        verify(mesaService).getById(999);
    }

    
    @Test
    void testFindByNumeroAsientos_Success() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);

        List<Mesa> mesasCuatroAsientos = Collections.singletonList(mesa1);
        when(mesaService.getMesasByNumeroAsientos(4)).thenReturn(mesasCuatroAsientos);
        
        mockMvc.perform(get("/api/mesas/numeroAsientos/4"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].numeroAsientos", is(4)));
        
        verify(mesaService).getMesasByNumeroAsientos(4);
    }
    /* 
    @Test
    void testFindByNumeroAsientos_NotFound() throws Exception {
        
        when(mesaService.getMesasByNumeroAsientos(10)).thenReturn(null);
        
      
        mockMvc.perform(get("/api/mesas/numeroAsientos/10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));

        
        verify(mesaService).getMesasByNumeroAsientos(10);
    }
    */
    @Test
    void testFindByNumeroAsientos_EmptyList() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);

        when(mesaService.getMesasByNumeroAsientos(10)).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/api/mesas/numeroAsientos/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
        
        verify(mesaService).getMesasByNumeroAsientos(10);
    }
    
    @Test
    void testFindByNumeroAsientos_ZeroSeats() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);

        when(mesaService.getMesasByNumeroAsientos(0)).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/api/mesas/numeroAsientos/0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
        
        verify(mesaService).getMesasByNumeroAsientos(0);
    }
    
    @Test
    void testFindByNumeroAsientos_NegativeSeats() throws Exception {
        when(mesaService.getMesasByNumeroAsientos(-1)).thenReturn(Collections.emptyList());
        when(userService.findCurrentUser()).thenReturn(adminUser);
        mockMvc.perform(get("/api/mesas/numeroAsientos/-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
        
        verify(mesaService).getMesasByNumeroAsientos(-1);
    }

    
    @Test
    void testFindByNegocio_Success() throws Exception {
        when(mesaService.getMesasByNegocio(1)).thenReturn(mesasList);
        when(userService.findCurrentUser()).thenReturn(adminUser);

        mockMvc.perform(get("/api/mesas/negocio/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[2].id", is(3)));
        
        verify(mesaService).getMesasByNegocio(1);
    }
    
    @Test
    void testFindByNegocio_NotFound() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);

        when(mesaService.getMesasByNegocio(999)).thenReturn(null);
        
        mockMvc.perform(get("/api/mesas/negocio/999"))
            .andExpect(status().isOk())
            .andExpect(content().string(""));
        
        verify(mesaService).getMesasByNegocio(999);
    }
    
    @Test
    void testFindByNegocio_EmptyList() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);

        when(mesaService.getMesasByNegocio(999)).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/api/mesas/negocio/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
        
        verify(mesaService).getMesasByNegocio(999);
    }

    
    @Test
    void testCreate_Success() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);

        Dueno duenoact = new Dueno();
        duenoact.setFirstName("Anton");
        duenoact.setLastName("García");
        duenoact.setEmail("anton@example.com");
        duenoact.setNumTelefono("652349978");
        duenoact.setTokenDueno("TOKEN333");

        Negocio negocioActualizado = new Negocio();
        negocioActualizado.setName("Restaurante 2 Tasca");
        negocioActualizado.setDireccion("Calle Principal 123");
        negocioActualizado.setCiudad("Sevilla");
        negocioActualizado.setPais("Espana");
        negocioActualizado.setCodigoPostal("41001");
        negocioActualizado.setTokenNegocio(12995);
        negocioActualizado.setDueno(duenoact);
        
        Mesa mesaCreada = new Mesa();
        mesaCreada.setName("Mesa Nueva");
        mesaCreada.setNumeroAsientos(8);
        mesaCreada.setNegocio(negocioActualizado);

        
        
        when(mesaService.save(any(Mesa.class))).thenReturn(mesaCreada);
        
        mockMvc.perform(post("/api/mesas")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mesaCreada)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Mesa Nueva")))
                .andExpect(jsonPath("$.numeroAsientos", is(8)));
        
        verify(mesaService).save(any(Mesa.class));
    }
    /* 
    @Test
    void testCreate_NullMesa() throws Exception {
        mockMvc.perform(post("/api/mesas")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest());
        
        verify(mesaService, never()).save(any(Mesa.class));
    }
    */
    @Test
    void testCreate_InvalidMesa() throws Exception {
        mockMvc.perform(post("/api/mesas")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mesaInvalida)))
                .andExpect(status().isBadRequest());
        
        verify(mesaService, never()).save(any(Mesa.class));
    }

    
    @Test
    void testUpdate_Success() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
    
        Dueno duenoact = new Dueno();
        duenoact.setFirstName("Anton");
        duenoact.setLastName("García");
        duenoact.setEmail("anton@example.com");
        duenoact.setNumTelefono("652349978");
        duenoact.setTokenDueno("TOKEN333");
    
        Negocio negocioActualizado = new Negocio();
        negocioActualizado.setId(1);  // Asegurarse de que el id coincide
        negocioActualizado.setName("Restaurante 2 Tasca");
        negocioActualizado.setDireccion("Calle Principal 123");
        negocioActualizado.setCiudad("Sevilla");
        negocioActualizado.setPais("Espana");
        negocioActualizado.setCodigoPostal("41001");
        negocioActualizado.setTokenNegocio(12995);
        negocioActualizado.setDueno(duenoact);
    
        Mesa mesaActualizada = new Mesa();
        mesaActualizada.setName("Mesa Exterior Actualizada");
        mesaActualizada.setNumeroAsientos(5);
        mesaActualizada.setNegocio(negocioActualizado);
        
        when(mesaService.getById(1)).thenReturn(mesa1);
        when(mesaService.update(any(Mesa.class), eq(1))).thenReturn(mesaActualizada);           
        mockMvc.perform(put("/api/mesas/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mesaActualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Mesa Exterior Actualizada")))
                .andExpect(jsonPath("$.numeroAsientos", is(5)));
        
        verify(mesaService).getById(1);
        verify(mesaService).update(any(Mesa.class), eq(1));
    }
    /*
    @Test
    void testUpdate_NotFound() throws Exception {
        when(mesaService.getById("999")).thenReturn(null);
        
        mockMvc.perform(put("/api/mesas/999")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mesa1)))
                .andExpect(status().isNotFound());
        
        verify(mesaService).getById("999");
        verify(mesaService, never()).save(any(Mesa.class));
    }
    
    @Test
    void testUpdate_NullMesa() throws Exception {
        mockMvc.perform(put("/api/mesas/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest());
        
        verify(mesaService, never()).getById(anyString());
        verify(mesaService, never()).save(any(Mesa.class));
    }
    
    @Test
    void testUpdate_InvalidMesa() throws Exception {
        when(mesaService.getById("1")).thenReturn(mesa1);
        
        mockMvc.perform(put("/api/mesas/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mesaInvalida)))
                .andExpect(status().isBadRequest());
        
        verify(mesaService, never()).save(any(Mesa.class));
    } */

    
    @Test
    void testDelete_Success() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);

        
        mockMvc.perform(delete("/api/mesas/1")
                .with(csrf()))
                .andExpect(status().isNoContent());

        verify(mesaService).deleteById(1);
    }
    
    @Test
    void testDelete_NotFound() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        
        mockMvc.perform(delete("/api/mesas/999")
                .with(csrf()))
                .andExpect(status().isNoContent());
        
        verify(mesaService).deleteById(anyInt());
    }
    @Test
    void testFindAll_AsAdmin() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(mesaService.getMesas()).thenReturn(mesasList);
        mockMvc.perform(get("/api/mesas"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)));
    }
    
    @Test
    void testFindAll_AsEmpleado_Forbidden() throws Exception {
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        mockMvc.perform(get("/api/mesas"))
            .andExpect(status().isForbidden());
    }
    
    @Test
    void testFindAll_AsDueno_Forbidden() throws Exception {
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        mockMvc.perform(get("/api/mesas"))
            .andExpect(status().isForbidden());
    }
    
    @Test
    void testFindById_AsAdmin() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(mesaService.getById(1)).thenReturn(mesa1);
        mockMvc.perform(get("/api/mesas/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)));
    }
    
    @Test
    void testFindById_AsDueno_Match() throws Exception {
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(duenoService.getDuenoByUser(anyInt())).thenReturn(dueno);
        when(mesaService.getById(1)).thenReturn(mesa1);
        mockMvc.perform(get("/api/mesas/1"))
            .andExpect(status().isOk());
    }
    
    @Test
    void testFindById_AsDueno_NoMatch() throws Exception {
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        Dueno duenoWrong = new Dueno();
        duenoWrong.setId(2);
        when(duenoService.getDuenoByUser(anyInt())).thenReturn(duenoWrong);
    
        when(mesaService.getById(1)).thenReturn(mesa1);
        mockMvc.perform(get("/api/mesas/1"))
               .andExpect(status().isForbidden());
    }
    
    
    @Test
    void testFindById_AsEmpleado_Match() throws Exception {
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoByUser(anyInt())).thenReturn(empleado);
        when(mesaService.getById(1)).thenReturn(mesa1);
        mockMvc.perform(get("/api/mesas/1"))
            .andExpect(status().isOk());
    }
    
    @Test
    void testFindById_AsEmpleado_NoMatch() throws Exception {
        Mesa otra = new Mesa();
        Negocio n2 = new Negocio(); n2.setId(2); otra.setNegocio(n2);
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoByUser(anyInt())).thenReturn(empleado);
        when(mesaService.getById(1)).thenReturn(otra);
        mockMvc.perform(get("/api/mesas/1"))
            .andExpect(status().isForbidden());
    }
    
    @Test
    void testFindByIdDTO_AsAdmin() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(mesaService.getById(1)).thenReturn(mesa1);
        mockMvc.perform(get("/api/mesas/dto/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)));
    }
    
    @Test
    void testFindByIdDTO_AsDueno_Match() throws Exception {
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(duenoService.getDuenoByUser(anyInt())).thenReturn(dueno);
        when(mesaService.getById(1)).thenReturn(mesa1);
        mockMvc.perform(get("/api/mesas/dto/1"))
            .andExpect(status().isOk());
    }
    
    @Test
    void testFindByIdDTO_AsDueno_NoMatch() throws Exception {
        Dueno other = new Dueno();
        other.setId(2); 
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(duenoService.getDuenoByUser(anyInt())).thenReturn(other);
        when(mesaService.getById(1)).thenReturn(mesa1);
    
        mockMvc.perform(get("/api/mesas/dto/1"))
               .andExpect(status().isForbidden());
    }
    
    
    @Test
    void testFindByNumeroAsientos_AsAdmin() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(mesaService.getMesasByNumeroAsientos(4)).thenReturn(Collections.singletonList(mesa1));
        mockMvc.perform(get("/api/mesas/numeroAsientos/4"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].numeroAsientos", is(4)));
    }
    
    @Test
    void testFindByNumeroAsientos_AsEmpleado_Forbidden() throws Exception {
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        mockMvc.perform(get("/api/mesas/numeroAsientos/4"))
            .andExpect(status().isForbidden());
    }
    
    @Test
    void testFindByNegocio_AsAdmin() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(mesaService.getMesasByNegocio(1)).thenReturn(mesasList);
        mockMvc.perform(get("/api/mesas/negocio/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)));
    }
    
    @Test
    void testFindByNegocio_AsDueno_Match() throws Exception {
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(duenoService.getDuenoByUser(anyInt())).thenReturn(dueno);
        when(negocioService.getById(1)).thenReturn(negocio);
        when(mesaService.getMesasByNegocio(1)).thenReturn(mesasList);
        mockMvc.perform(get("/api/mesas/negocio/1"))
            .andExpect(status().isOk());
    }
    
    @Test
    void testFindByNegocio_AsDueno_NoMatch() throws Exception {
        Dueno other = new Dueno();
        other.setId(2); 
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(duenoService.getDuenoByUser(anyInt())).thenReturn(other);
        when(negocioService.getById(1)).thenReturn(negocio);
    
        mockMvc.perform(get("/api/mesas/negocio/1"))
               .andExpect(status().isForbidden());
    }
    
    
    @Test
    void testFindByNegocio_AsEmpleado_Match() throws Exception {
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoByUser(anyInt())).thenReturn(empleado);
        when(mesaService.getMesasByNegocio(1)).thenReturn(mesasList);
        mockMvc.perform(get("/api/mesas/negocio/1"))
            .andExpect(status().isOk());
    }
    
    @Test
    void testFindByNegocio_AsEmpleado_NoMatch() throws Exception {
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoByUser(anyInt())).thenReturn(empleado);
        mockMvc.perform(get("/api/mesas/negocio/2"))
            .andExpect(status().isForbidden());
    }
    
    @Test
    void testCreate_AsAdmin() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(mesaService.save(any(Mesa.class))).thenReturn(mesaNueva);
    
        mockMvc.perform(post("/api/mesas")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "name": "Mesa Nueva",
                      "numeroAsientos": 8
                    }
                    """))
               .andExpect(status().isCreated());
    }
    
    
    
    @Test
    void testUpdate_AsAdmin() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(mesaService.getById(1)).thenReturn(mesa1);
        when(mesaService.update(any(Mesa.class), eq(1))).thenReturn(mesa1);

        Map<String, Object> payload = new HashMap<>();
        payload.put("name", mesa1.getName());
        payload.put("numeroAsientos", mesa1.getNumeroAsientos());
        payload.put("negocio", Collections.singletonMap("id", mesa1.getNegocio().getId()));

        mockMvc.perform(put("/api/mesas/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(mesa1.getId())))
            .andExpect(jsonPath("$.name", is(mesa1.getName())))
            .andExpect(jsonPath("$.numeroAsientos", is(mesa1.getNumeroAsientos())));
    }

    
    @Test
    void testUpdate_MismatchedNegocio() throws Exception {
        when(mesaService.getById(1)).thenReturn(mesa1); 
        Mesa payload = new Mesa();
        payload.setName("Mesa Test");
        payload.setNumeroAsientos(4);
        Negocio other = new Negocio();
        other.setId(2);
        payload.setNegocio(other);
        mockMvc.perform(put("/api/mesas/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
               .andExpect(status().isForbidden());
        verify(mesaService, never()).update(any(Mesa.class), anyInt());
    }
    
    
    @Test
    void testDelete_AsAdmin() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        mockMvc.perform(delete("/api/mesas/1")
            .with(csrf()))
            .andExpect(status().isNoContent());
    }
    
    @Test
    void testDelete_AsDueno_Match() throws Exception {
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(duenoService.getDuenoByUser(anyInt())).thenReturn(dueno);
        when(mesaService.getById(1)).thenReturn(mesa1);
        mockMvc.perform(delete("/api/mesas/1")
            .with(csrf()))
            .andExpect(status().isNoContent());
    }
    
    @Test
    void testDelete_AsDueno_NoMatch() throws Exception {
        Dueno otherDueno = new Dueno();
        otherDueno.setId(2);
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(duenoService.getDuenoByUser(anyInt())).thenReturn(otherDueno);
        when(mesaService.getById(1)).thenReturn(mesa1);
        mockMvc.perform(delete("/api/mesas/1")
                .with(csrf()))
               .andExpect(status().isForbidden());
        verify(mesaService, never()).deleteById(anyInt());
    }
    
    
    @Test
    void testDelete_AsEmpleado_Match() throws Exception {
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoByUser(anyInt())).thenReturn(empleado);
        when(mesaService.getById(1)).thenReturn(mesa1);
        mockMvc.perform(delete("/api/mesas/1")
            .with(csrf()))
            .andExpect(status().isNoContent());
    }
    
    @Test
    void testDelete_AsEmpleado_NoMatch() throws Exception {
        empleado.getNegocio().setId(2);
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoByUser(anyInt())).thenReturn(empleado);
        Mesa otra = new Mesa();
        Negocio diff = new Negocio();
        diff.setId(1);
        otra.setNegocio(diff);
        when(mesaService.getById(1)).thenReturn(otra);
    
        mockMvc.perform(delete("/api/mesas/1")
                .with(csrf()))
               .andExpect(status().isForbidden());
    
        verify(mesaService, never()).deleteById(anyInt());
    }
    
    @Test
    void testFindByNegocioDto_AsAdmin() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(mesaService.getMesasByNegocio(1)).thenReturn(Arrays.asList(mesa1, mesa2));

        mockMvc.perform(get("/api/mesas/dto/negocio/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].nombre", is("Mesa Exterior")))
            .andExpect(jsonPath("$[0].numeroAsientos", is(4)))
            .andExpect(jsonPath("$[0].negocioId", is(1)))
            .andExpect(jsonPath("$[0].nombreNegocio", is("Restaurante La Tasca")))
            .andExpect(jsonPath("$[1].id", is(2)))
            .andExpect(jsonPath("$[1].nombre", is("Mesa VIP")));
    }

    @Test
    void testFindByNegocioDto_AsDueno_Match() throws Exception {
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(duenoService.getDuenoByUser(anyInt())).thenReturn(dueno);
        when(negocioService.getById(1)).thenReturn(negocio);
        when(mesaService.getMesasByNegocio(1)).thenReturn(Collections.singletonList(mesa3));

        mockMvc.perform(get("/api/mesas/dto/negocio/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id", is(3)))
            .andExpect(jsonPath("$[0].nombre", is("Mesa Barra")));
    }

    @Test
    void testFindByNegocioDto_AsDueno_NoMatch() throws Exception {
        dueno.setId(2);
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(duenoService.getDuenoByUser(anyInt())).thenReturn(dueno);
    
        Dueno otherOwner = new Dueno();
        otherOwner.setId(1);
        Negocio mismatchNegocio = new Negocio();
        mismatchNegocio.setDueno(otherOwner);
        when(negocioService.getById(1)).thenReturn(mismatchNegocio);
    
        mockMvc.perform(get("/api/mesas/dto/negocio/1"))
            .andExpect(status().isForbidden());
    }
    

    @Test
    void testFindByNegocioDto_AsEmpleado_Match() throws Exception {
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoByUser(anyInt())).thenReturn(empleado);
        when(mesaService.getMesasByNegocio(1)).thenReturn(Collections.singletonList(mesa1));

        mockMvc.perform(get("/api/mesas/dto/negocio/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].nombre", is("Mesa Exterior")));
    }

    @Test
    void testFindByNegocioDto_AsEmpleado_NoMatch() throws Exception {
        empleado.getNegocio().setId(2);
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoByUser(anyInt())).thenReturn(empleado);

        mockMvc.perform(get("/api/mesas/dto/negocio/1"))
            .andExpect(status().isForbidden());
    }

    @Test
    void testUpdateDto_NullBody() throws Exception {
        
        mockMvc.perform(put("/api/mesas/dto/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")) 
               .andExpect(status().isBadRequest());
    }
    
    @Test
    void testUpdateDto_Forbidden_NegocioMismatch() throws Exception {
        MesaDTO dto = new MesaDTO();
        dto.setId(1);
        dto.setNombre("X");
        dto.setNumeroAsientos(4);
        dto.setNegocioId(2);
    
        Mesa mesaConvertida = new Mesa();
        mesaConvertida.setId(1);
        mesaConvertida.setName("X");
        mesaConvertida.setNumeroAsientos(4);
        Negocio n2 = new Negocio();
        n2.setId(2); 
        mesaConvertida.setNegocio(n2);
    
        when(mesaService.convertirMesa(any(MesaDTO.class))).thenReturn(mesaConvertida);
        when(mesaService.getById(1)).thenReturn(mesa1); 
    
        mockMvc.perform(put("/api/mesas/dto/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
               .andExpect(status().isForbidden());
    }
    
    
    @Test
    void testUpdateDto_AsAdmin() throws Exception {
        MesaDTO dto = new MesaDTO();
        dto.setId(1);
        dto.setNombre("Mesa Exterior");
        dto.setNumeroAsientos(4);
        dto.setNegocioId(1);
    
        when(mesaService.convertirMesa(any(MesaDTO.class))).thenReturn(mesa1);
        when(mesaService.getById(1)).thenReturn(mesa1);
        when(mesaService.update(any(Mesa.class), eq(1))).thenReturn(mesa1);
        when(userService.findCurrentUser()).thenReturn(adminUser);
    
        mockMvc.perform(put("/api/mesas/dto/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id", is(1)))
               .andExpect(jsonPath("$.name", is("Mesa Exterior")))
               .andExpect(jsonPath("$.numeroAsientos", is(4)))
               .andExpect(jsonPath("$.negocio.id", is(1)));
    }
    
    
    @Test
    void testUpdateDto_AsDueno_Match() throws Exception {
        MesaDTO dto = new MesaDTO();
        dto.setId(1);
        dto.setNombre("Mesa Exterior");
        dto.setNumeroAsientos(4);
        dto.setNegocioId(1);
        when(mesaService.convertirMesa(any(MesaDTO.class))).thenReturn(mesa1);
        when(mesaService.getById(1)).thenReturn(mesa1);
        when(duenoService.getDuenoByUser(anyInt())).thenReturn(dueno);
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(mesaService.update(any(Mesa.class), eq(1))).thenReturn(mesa1);
        mockMvc.perform(put("/api/mesas/dto/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
               .andExpect(status().isOk());
    }
    
    @Test
    void testUpdateDto_AsDueno_NoMatch() throws Exception {
        MesaDTO dto = new MesaDTO();
        dto.setId(1);
        dto.setNombre("Mesa Exterior");
        dto.setNumeroAsientos(4);
        dto.setNegocioId(1);
    
        Dueno otroDueno = new Dueno();
        otroDueno.setId(99);
        Negocio negocio = new Negocio();
        negocio.setId(1);
        negocio.setDueno(otroDueno);
        mesa1.setNegocio(negocio);
    
        when(mesaService.convertirMesa(any(MesaDTO.class))).thenReturn(mesa1);
        when(mesaService.getById(1)).thenReturn(mesa1);
    
        dueno.setId(2);
        when(duenoService.getDuenoByUser(anyInt())).thenReturn(dueno);
        when(userService.findCurrentUser()).thenReturn(duenoUser);
    
        mockMvc.perform(put("/api/mesas/dto/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
               .andExpect(status().isForbidden());
    }
    
    
    @Test
    void testUpdateDto_AsEmpleado_Match() throws Exception {
        MesaDTO dto = new MesaDTO();
        dto.setId(1);
        dto.setNombre("Mesa Exterior");
        dto.setNumeroAsientos(4);
        dto.setNegocioId(1);
        when(mesaService.convertirMesa(any(MesaDTO.class))).thenReturn(mesa1);
        when(mesaService.getById(1)).thenReturn(mesa1);
        when(empleadoService.getEmpleadoByUser(anyInt())).thenReturn(empleado);
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(mesaService.update(any(Mesa.class), eq(1))).thenReturn(mesa1);
        mockMvc.perform(put("/api/mesas/dto/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
               .andExpect(status().isOk());
    }
    
    @Test
    void testUpdateDto_AsEmpleado_NoMatch() throws Exception {
        MesaDTO dto = new MesaDTO();
        dto.setId(1);
        dto.setNombre("Mesa Exterior");
        dto.setNumeroAsientos(4);
        dto.setNegocioId(1);
        when(mesaService.convertirMesa(any(MesaDTO.class))).thenReturn(mesa1);
        when(mesaService.getById(1)).thenReturn(mesa1);
        empleado.setNegocio(new Negocio() {{ setId(2); }});
        when(empleadoService.getEmpleadoByUser(anyInt())).thenReturn(empleado);
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        mockMvc.perform(put("/api/mesas/dto/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
               .andExpect(status().isForbidden());
    }
    
    @Test
    void testCreateDto_AsAdmin() throws Exception {
        MesaDTO dto = new MesaDTO();
        dto.setNombre("Mesa A");
        dto.setNumeroAsientos(4);
        dto.setNegocioId(1);
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(mesaService.convertirMesa(any(MesaDTO.class))).thenReturn(mesaNueva);
        when(mesaService.save(any(Mesa.class))).thenReturn(mesaNueva);
    
        mockMvc.perform(post("/api/mesas/dto")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name", is("Mesa Nueva")))
            .andExpect(jsonPath("$.numeroAsientos", is(8)));
    }
    
    @Test
    void testCreateDto_AsDueno_Match() throws Exception {
        MesaDTO dto = new MesaDTO();
        dto.setNombre("Mesa B");
        dto.setNumeroAsientos(5);
        dto.setNegocioId(1);
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(duenoService.getDuenoByUser(anyInt())).thenReturn(dueno);
        when(mesaService.convertirMesa(any(MesaDTO.class))).thenReturn(mesaNueva);
        when(mesaService.save(any(Mesa.class))).thenReturn(mesaNueva);
    
        mockMvc.perform(post("/api/mesas/dto")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated());
    }
    
    @Test
    void testCreateDto_AsDueno_NoMatch() throws Exception {
        MesaDTO dto = new MesaDTO();
        dto.setNombre("Mesa C");
        dto.setNumeroAsientos(6);
        dto.setNegocioId(1);
    
        dueno.setId(2);
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(duenoService.getDuenoByUser(anyInt())).thenReturn(dueno);
    
        Dueno duenoMesa = new Dueno();
        duenoMesa.setId(1);
        Negocio negocioMesa = new Negocio();
        negocioMesa.setDueno(duenoMesa);
        Mesa mesaParaConvertir = new Mesa();
        mesaParaConvertir.setNegocio(negocioMesa);
    
        when(mesaService.convertirMesa(any(MesaDTO.class))).thenReturn(mesaParaConvertir);
    
        mockMvc.perform(post("/api/mesas/dto")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isForbidden());
    }
    
    
    @Test
    void testCreateDto_AsEmpleado_Match() throws Exception {
        MesaDTO dto = new MesaDTO();
        dto.setNombre("Mesa D");
        dto.setNumeroAsientos(3);
        dto.setNegocioId(1);
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoByUser(anyInt())).thenReturn(empleado);
        when(mesaService.convertirMesa(any(MesaDTO.class))).thenReturn(mesaNueva);
        when(mesaService.save(any(Mesa.class))).thenReturn(mesaNueva);
    
        mockMvc.perform(post("/api/mesas/dto")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated());
    }
    
    @Test
    void testCreateDto_AsEmpleado_NoMatch() throws Exception {
        MesaDTO dto = new MesaDTO();
        dto.setNombre("Mesa E");
        dto.setNumeroAsientos(2);
        dto.setNegocioId(1);
    
        Negocio negocioMesa = new Negocio(); negocioMesa.setId(1);
        Mesa mesaParaConvertir = new Mesa();
        mesaParaConvertir.setNegocio(negocioMesa);
    
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoByUser(anyInt())).thenReturn(empleado);
        when(mesaService.convertirMesa(any(MesaDTO.class))).thenReturn(mesaParaConvertir);
    
        empleado.getNegocio().setId(2);
    
        mockMvc.perform(post("/api/mesas/dto")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isForbidden());
    }
    
    
    @Test
    void testCreateDto_NullBody() throws Exception {
    
        mockMvc.perform(post("/api/mesas/dto")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }
    
    
    
    
    

}