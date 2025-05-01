package ispp_g2.gastrostock.testDiaReparto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import ispp_g2.gastrostock.diaReparto.DiaReparto;
import ispp_g2.gastrostock.diaReparto.DiaRepartoController;
import ispp_g2.gastrostock.diaReparto.DiaRepartoDTO;
import ispp_g2.gastrostock.diaReparto.DiaRepartoService;
import ispp_g2.gastrostock.dueno.Dueno;
import ispp_g2.gastrostock.empleado.Empleado;
import ispp_g2.gastrostock.empleado.EmpleadoService;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.proveedores.Proveedor;
import ispp_g2.gastrostock.proveedores.ProveedorService;
import ispp_g2.gastrostock.user.Authorities;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.time.DayOfWeek;
import java.util.List;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class DiaRepartoControllerTest {

    public abstract class UserMixin {
        @JsonIgnore
        public abstract java.util.Collection<? extends GrantedAuthority> getAuthorities();
    }

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private DiaRepartoService diaRepartoService;
    
    @Mock
    private UserService userService;

    @Mock
    private EmpleadoService empleadoService;

    @Mock
    private ProveedorService proveedorService;

    @InjectMocks
    private DiaRepartoController diaRepartoController;

    private DiaReparto diaReparto;
    private Negocio negocio;
    private Proveedor proveedor;
    private User adminUser;
    private User duenoUser;
    private User empleadoUser;
    private Authorities adminAuth;
    private Authorities duenoAuth;  
    private Authorities empleAuth;
    private Empleado empleado;
    private Dueno dueno;

    @BeforeEach
    void setUp() {
    objectMapper.findAndRegisterModules(); 
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.addMixIn(ispp_g2.gastrostock.user.User.class, UserMixin.class);

    MappingJackson2HttpMessageConverter jacksonConverter =
        new MappingJackson2HttpMessageConverter(objectMapper);

    mockMvc = MockMvcBuilders
        .standaloneSetup(diaRepartoController)
        .setMessageConverters(jacksonConverter)
        .build();



        adminAuth = new Authorities();
        adminAuth.setAuthority("admin");
        duenoAuth = new Authorities();
        duenoAuth.setAuthority("dueno");
        empleAuth = new Authorities();
        empleAuth.setAuthority("empleado");

        adminUser = new User();
        adminUser.setId(1);
        adminUser.setAuthority(adminAuth);

        duenoUser = new User();
        duenoUser.setId(2);
        duenoUser.setAuthority(duenoAuth);

        empleadoUser = new User();
        empleadoUser.setId(3);
        empleadoUser.setAuthority(empleAuth);

        dueno = new Dueno();
        dueno.setUser(duenoUser);
        

        negocio = new Negocio();
        negocio.setId(1);
        negocio.setDueno(dueno);

        proveedor = new Proveedor();
        proveedor.setId(1);
        proveedor.setNegocio(negocio);

        diaReparto = new DiaReparto();
        diaReparto.setId(1);
        diaReparto.setDiaSemana(DayOfWeek.MONDAY);
        diaReparto.setProveedor(proveedor);

        empleado = new Empleado();
        empleado.setUser(empleadoUser);
        empleado.setNegocio(negocio);
    }

    @Test
    void testFindAll_WhenDataExists_ReturnsOk() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(diaRepartoService.getDiasReparto()).thenReturn(List.of(diaReparto));

        mockMvc.perform(get("/api/diasReparto"))
                .andExpect(status().isOk());
    }

    @Test
    void testFindAll_WhenNoData_ReturnsNoContent() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(diaRepartoService.getDiasReparto()).thenReturn(List.of());

        mockMvc.perform(get("/api/diasReparto"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testFindById_WhenExists_ReturnsOk() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(diaRepartoService.getById(1)).thenReturn(diaReparto);

        mockMvc.perform(get("/api/diasReparto/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testFindById_WhenNotExists_ReturnsOkWithNull() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(diaRepartoService.getById(99)).thenReturn(null);
    
        mockMvc.perform(get("/api/diasReparto/99"))
                .andExpect(status().isOk())  
                .andExpect(content().string(""));  
    }
    

    @Test
    void testFindByDiaSemana_WhenExists_ReturnsOk() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(diaRepartoService.getDiaRepartoByDiaSemana(DayOfWeek.MONDAY)).thenReturn(List.of(diaReparto));

        mockMvc.perform(get("/api/diasReparto/diaSemana/MONDAY"))
                .andExpect(status().isOk());
    }

    @Test
    void testFindByDiaSemana_WhenNotExists_ReturnsNoContent() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(diaRepartoService.getDiaRepartoByDiaSemana(DayOfWeek.WEDNESDAY)).thenReturn(List.of());

        mockMvc.perform(get("/api/diasReparto/diaSemana/WEDNESDAY"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testSave_WhenValid_ReturnsCreated() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
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
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(diaRepartoService.getById(1)).thenReturn(diaReparto);

        mockMvc.perform(put("/api/diasReparto/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(diaReparto)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdate_WhenNotExists_ReturnsOkWithNull() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(diaRepartoService.getById(99)).thenReturn(null);

        mockMvc.perform(put("/api/diasReparto/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(diaReparto)))
                .andExpect(status().isOk())  
                .andExpect(content().string(""));  
    }

    @Test
    void testDelete_WhenExists_ReturnsNoContent() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(diaRepartoService.getById(1)).thenReturn(diaReparto);
        doNothing().when(diaRepartoService).deleteById(1);

        mockMvc.perform(delete("/api/diasReparto/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDelete_WhenNotExists_ReturnsNoContent() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(diaRepartoService.getById(99)).thenReturn(null);

        mockMvc.perform(delete("/api/diasReparto/99"))
                .andExpect(status().isNoContent());
    }
 @Test
    void testDiaRepartoDtoMapping() {
        Dueno dueno = new Dueno();
        User ownerUser = new User();
        ownerUser.setId(2);
        dueno.setUser(ownerUser);
        negocio.setDueno(dueno);
        diaReparto.setDescripcion("Delivery on Monday");

        DiaRepartoDTO dto = DiaRepartoDTO.of(diaReparto);

        assertEquals(diaReparto.getId(), dto.getId());
        assertEquals(diaReparto.getDiaSemana(), dto.getDiaSemana());
        assertEquals(diaReparto.getDescripcion(), dto.getDescripcion());
        assertEquals(proveedor.getId(), dto.getProveedorId());
        assertEquals(proveedor.getName(), dto.getNombreProveedor());
    }

    @Test
    void testFindAllDto_WhenDataExists_ReturnsOkAndDtoContent() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(diaRepartoService.getDiasReparto()).thenReturn(List.of(diaReparto));

        mockMvc.perform(get("/api/diasReparto/dto"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].diaSemana").value("MONDAY"))
                .andExpect(jsonPath("$[0].proveedorId").value(1));
    }

    @Test
    void testFindAllDto_WhenNoData_ReturnsNoContent() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(diaRepartoService.getDiasReparto()).thenReturn(List.of());

        mockMvc.perform(get("/api/diasReparto/dto"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testFindByIdDto_WhenExists_ReturnsOkAndDto() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(diaRepartoService.getById(1)).thenReturn(diaReparto);

        mockMvc.perform(get("/api/diasReparto/dto/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.diaSemana").value("MONDAY"));
    }

    @Test
    void testFindByIdDto_WhenNotExists_ReturnsForbidden() throws Exception {
        User other = new User(); other.setId(99);
        other.setAuthority(new Authorities());
        when(userService.findCurrentUser()).thenReturn(other);
        when(diaRepartoService.getById(1)).thenReturn(diaReparto);

        mockMvc.perform(get("/api/diasReparto/dto/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testSaveDto_WhenValid_ReturnsCreated() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        DiaRepartoDTO dto = new DiaRepartoDTO();
        dto.setDiaSemana(DayOfWeek.MONDAY);
        dto.setProveedorId(1);
        when(diaRepartoService.convertirDTODiaReparto(any())).thenReturn(diaReparto);
        when(diaRepartoService.save(diaReparto)).thenReturn(diaReparto);

        mockMvc.perform(post("/api/diasReparto/dto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void testSaveDto_WhenInvalid_ReturnsBadRequest() throws Exception {
        DiaRepartoDTO dto = new DiaRepartoDTO(); // missing fields

        mockMvc.perform(post("/api/diasReparto/dto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateDto_WhenAuthorized_ReturnsOk() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(diaRepartoService.getById(1)).thenReturn(diaReparto);
        DiaRepartoDTO dto = new DiaRepartoDTO();
        dto.setDiaSemana(DayOfWeek.MONDAY);
        dto.setProveedorId(1);
        when(diaRepartoService.convertirDTODiaReparto(any())).thenReturn(diaReparto);
        when(diaRepartoService.update(eq(1), any())).thenReturn(diaReparto);

        mockMvc.perform(put("/api/diasReparto/dto/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void testFindByProveedor_AsAdmin_ReturnsOk() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(diaRepartoService.getDiaRepartoByProveedorId(1)).thenReturn(List.of(diaReparto));

        mockMvc.perform(get("/api/diasReparto/proveedor/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void testFindByProveedor_WhenNoData_ReturnsNoContent() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(diaRepartoService.getDiaRepartoByProveedorId(1)).thenReturn(List.of());

        mockMvc.perform(get("/api/diasReparto/proveedor/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testFindAll_Unauthorized_ReturnsForbidden() throws Exception {
        User noAuth = new User(); noAuth.setId(3);
        noAuth.setAuthority(new Authorities());
        when(userService.findCurrentUser()).thenReturn(noAuth);

        mockMvc.perform(get("/api/diasReparto"))
                .andExpect(status().isForbidden());
    }
        @Test
    void testFindAllDto_WhenNoDataExists_ReturnsNoContent() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(diaRepartoService.getDiasReparto()).thenReturn(List.of());

        mockMvc.perform(get("/api/diasReparto/dto"))
                .andExpect(status().isNoContent());
    }


    @Test
    void testSaveDto_WhenInvalidData_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/diasReparto/dto")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testFindByDiaSemana_Unauthorized_ReturnsForbidden() throws Exception {
        User noAuth = new User();
        noAuth.setId(5);
        Authorities auth = new Authorities();
        auth.setAuthority("someRole");
        noAuth.setAuthority(auth);

        when(userService.findCurrentUser()).thenReturn(noAuth);
        mockMvc.perform(get("/api/diasReparto/diaSemana/MONDAY"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testFindByProveedor_Unauthorized_ReturnsForbidden() throws Exception {
        User noAuth = new User();
        noAuth.setId(6);
        Authorities auth = new Authorities();
        auth.setAuthority("other");
        noAuth.setAuthority(auth);

        when(userService.findCurrentUser()).thenReturn(noAuth);
        mockMvc.perform(get("/api/diasReparto/proveedor/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testSave_Unauthorized_ReturnsForbidden() throws Exception {
        User noAuth = new User();
        noAuth.setId(7);
        Authorities auth = new Authorities();
        auth.setAuthority("guest");
        noAuth.setAuthority(auth);

        when(userService.findCurrentUser()).thenReturn(noAuth);

        mockMvc.perform(post("/api/diasReparto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(diaReparto)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testSaveDto_Unauthorized_ReturnsForbidden() throws Exception {
        User noAuth = new User();
        noAuth.setId(8);
        Authorities auth = new Authorities();
        auth.setAuthority("none");
        noAuth.setAuthority(auth);

        DiaRepartoDTO dto = new DiaRepartoDTO();
        dto.setDiaSemana(DayOfWeek.TUESDAY);
        dto.setProveedorId(1);

        when(userService.findCurrentUser()).thenReturn(noAuth);
        when(diaRepartoService.convertirDTODiaReparto(any())).thenReturn(diaReparto);

        mockMvc.perform(post("/api/diasReparto/dto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testDelete_Unauthorized_ReturnsForbidden() throws Exception {
        User noAuth = new User();
        noAuth.setId(9);
        Authorities auth = new Authorities();
        auth.setAuthority("visitor");
        noAuth.setAuthority(auth);

        when(userService.findCurrentUser()).thenReturn(noAuth);
        when(diaRepartoService.getById(1)).thenReturn(diaReparto);

        mockMvc.perform(delete("/api/diasReparto/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testFindAll_AsDueno_ReturnsOk() throws Exception {
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(diaRepartoService.getDiasReparto()).thenReturn(List.of(diaReparto));

        mockMvc.perform(get("/api/diasReparto"))
                .andExpect(status().isOk());
    }

    @Test
    void testFindAllDto_AsDueno_ReturnsOk() throws Exception {
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(diaRepartoService.getDiasReparto()).thenReturn(List.of(diaReparto));

        mockMvc.perform(get("/api/diasReparto/dto"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void testFindById_AsDueno_ReturnsOk() throws Exception {
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(diaRepartoService.getById(100)).thenReturn(diaReparto);

        mockMvc.perform(get("/api/diasReparto/100"))
                .andExpect(status().isOk());
    }

    @Test
    void testFindByIdDto_AsDueno_ReturnsOk() throws Exception {
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(diaRepartoService.getById(100)).thenReturn(diaReparto);

        mockMvc.perform(get("/api/diasReparto/dto/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testFindByDiaSemana_AsDueno_ReturnsOk() throws Exception {
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(diaRepartoService.getDiaRepartoByDiaSemana(DayOfWeek.MONDAY)).thenReturn(List.of(diaReparto));

        mockMvc.perform(get("/api/diasReparto/diaSemana/MONDAY"))
                .andExpect(status().isOk());
    }

    @Test
    void testFindByProveedor_AsDueno_ReturnsOk() throws Exception {
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(proveedorService.findById(20)).thenReturn(proveedor);
        when(diaRepartoService.getDiaRepartoByProveedorId(20)).thenReturn(List.of(diaReparto));

        mockMvc.perform(get("/api/diasReparto/proveedor/20"))
                .andExpect(status().isOk());
    }

    @Test
    void testSave_AsDueno_ReturnsCreated() throws Exception {
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(diaRepartoService.save(any())).thenReturn(diaReparto);

        mockMvc.perform(post("/api/diasReparto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(diaReparto)))
                .andExpect(status().isCreated());
    }

    @Test
    void testSaveDto_AsDueno_ReturnsCreated() throws Exception {
        DiaRepartoDTO dto = new DiaRepartoDTO();
        dto.setDiaSemana(DayOfWeek.MONDAY);
        dto.setProveedorId(20);
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(diaRepartoService.convertirDTODiaReparto(any())).thenReturn(diaReparto);
        when(diaRepartoService.save(diaReparto)).thenReturn(diaReparto);

        mockMvc.perform(post("/api/diasReparto/dto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdate_AsDueno_ReturnsOk() throws Exception {
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(diaRepartoService.getById(100)).thenReturn(diaReparto);

        mockMvc.perform(put("/api/diasReparto/100")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(diaReparto)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateDto_AsDueno_ReturnsOk() throws Exception {
        DiaRepartoDTO dto = new DiaRepartoDTO();
        dto.setDiaSemana(DayOfWeek.MONDAY);
        dto.setProveedorId(20);
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(diaRepartoService.getById(100)).thenReturn(diaReparto);
        when(diaRepartoService.convertirDTODiaReparto(any())).thenReturn(diaReparto);
        when(diaRepartoService.update(eq(100), any())).thenReturn(diaReparto);

        mockMvc.perform(put("/api/diasReparto/dto/100")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void testDelete_AsDueno_ReturnsNoContent() throws Exception {
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(diaRepartoService.getById(100)).thenReturn(diaReparto);

        mockMvc.perform(delete("/api/diasReparto/100"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testFindAll_AsEmpleado_ReturnsOk() throws Exception {
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoByUser(3)).thenReturn(empleado);
        when(diaRepartoService.getDiasReparto()).thenReturn(List.of(diaReparto));

        mockMvc.perform(get("/api/diasReparto"))
                .andExpect(status().isOk());
    }

    @Test
    void testFindAllDto_AsEmpleado_ReturnsOk() throws Exception {
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoByUser(3)).thenReturn(empleado);
        when(diaRepartoService.getDiasReparto()).thenReturn(List.of(diaReparto));

        mockMvc.perform(get("/api/diasReparto/dto"))
                .andExpect(status().isOk());
    }

    @Test
    void testFindById_AsEmpleado_ReturnsOk() throws Exception {
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoByUser(3)).thenReturn(empleado);
        when(diaRepartoService.getById(100)).thenReturn(diaReparto);

        mockMvc.perform(get("/api/diasReparto/100"))
                .andExpect(status().isOk());
    }

    @Test
    void testFindByIdDto_AsEmpleado_ReturnsOk() throws Exception {
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoByUser(3)).thenReturn(empleado);
        when(diaRepartoService.getById(100)).thenReturn(diaReparto);

        mockMvc.perform(get("/api/diasReparto/dto/100"))
                .andExpect(status().isOk());
    }

    @Test
    void testFindByDiaSemana_AsEmpleado_ReturnsOk() throws Exception {
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoByUser(3)).thenReturn(empleado);
        when(diaRepartoService.getDiaRepartoByDiaSemana(DayOfWeek.MONDAY)).thenReturn(List.of(diaReparto));

        mockMvc.perform(get("/api/diasReparto/diaSemana/MONDAY"))
                .andExpect(status().isOk());
    }

    @Test
    void testFindByProveedor_AsEmpleado_ReturnsOk() throws Exception {
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoByUser(3)).thenReturn(empleado);
        when(proveedorService.findById(20)).thenReturn(proveedor);
        when(diaRepartoService.getDiaRepartoByProveedorId(20)).thenReturn(List.of(diaReparto));

        mockMvc.perform(get("/api/diasReparto/proveedor/20"))
                .andExpect(status().isOk());
    }

    @Test
    void testSave_AsEmpleado_ReturnsCreated() throws Exception {
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoByUser(3)).thenReturn(empleado);
        when(diaRepartoService.save(any())).thenReturn(diaReparto);

        mockMvc.perform(post("/api/diasReparto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(diaReparto)))
                .andExpect(status().isCreated());
    }

    @Test
    void testSaveDto_AsEmpleado_ReturnsCreated() throws Exception {
        DiaRepartoDTO dto = new DiaRepartoDTO();
        dto.setDiaSemana(DayOfWeek.MONDAY);
        dto.setProveedorId(20);
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoByUser(3)).thenReturn(empleado);
        when(diaRepartoService.convertirDTODiaReparto(any())).thenReturn(diaReparto);
        when(diaRepartoService.save(diaReparto)).thenReturn(diaReparto);

        mockMvc.perform(post("/api/diasReparto/dto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdate_AsEmpleado_ReturnsOk() throws Exception {
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoByUser(3)).thenReturn(empleado);
        when(diaRepartoService.getById(100)).thenReturn(diaReparto);

        mockMvc.perform(put("/api/diasReparto/100")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(diaReparto)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateDto_AsEmpleado_ReturnsOk() throws Exception {
        DiaRepartoDTO dto = new DiaRepartoDTO();
        dto.setDiaSemana(DayOfWeek.MONDAY);
        dto.setProveedorId(20);
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoByUser(3)).thenReturn(empleado);
        when(diaRepartoService.getById(100)).thenReturn(diaReparto);
        when(diaRepartoService.convertirDTODiaReparto(any())).thenReturn(diaReparto);
        when(diaRepartoService.update(eq(100), any())).thenReturn(diaReparto);

        mockMvc.perform(put("/api/diasReparto/dto/100")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void testDelete_AsEmpleado_ReturnsNoContent() throws Exception {
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoByUser(3)).thenReturn(empleado);
        when(diaRepartoService.getById(100)).thenReturn(diaReparto);

        mockMvc.perform(delete("/api/diasReparto/100"))
                .andExpect(status().isNoContent());
    }


}

