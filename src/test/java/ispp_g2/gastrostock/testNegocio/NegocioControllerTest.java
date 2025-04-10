package ispp_g2.gastrostock.testNegocio;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import ispp_g2.gastrostock.dueno.Dueno;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioController;
import ispp_g2.gastrostock.negocio.NegocioDTO;
import ispp_g2.gastrostock.negocio.NegocioService;
import ispp_g2.gastrostock.user.Authorities;
import ispp_g2.gastrostock.user.User;  
import ispp_g2.gastrostock.user.UserService;
import ispp_g2.gastrostock.dueno.DuenoService;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class NegocioControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NegocioService negocioService;
    
    @Mock
    private UserService userService;
    
    @Mock
    private DuenoService duenoService;

    @InjectMocks
    private NegocioController negocioController;

    private Negocio negocio1, negocio2;
    private List<Negocio> negocioList;
    private Dueno dueno;
    private ObjectMapper objectMapper;

    // Usuarios para simular diferentes autoridades
    private User duenoUser;
    private User adminUser;
    private Authorities duenoAuth;
    private Authorities adminAuth;

    @BeforeEach
    void setUp() {
        // Configurar MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(negocioController).build();
        objectMapper = new ObjectMapper();

        // Configurar autoridad y usuario para "dueno"
        duenoAuth = new Authorities();
        duenoAuth.setId(1);
        duenoAuth.setAuthority("dueno");

        duenoUser = Mockito.spy(new User());
        duenoUser.setId(1);
        duenoUser.setUsername("duenoUser");
        duenoUser.setPassword("pass");
        duenoUser.setAuthority(duenoAuth);

        // Configurar autoridad y usuario para "admin"
        adminAuth = new Authorities();
        adminAuth.setId(2);
        adminAuth.setAuthority("admin");

        adminUser = new User();
        adminUser.setId(100);
        adminUser.setUsername("admin");
        adminUser.setPassword("adminpass");
        adminUser.setAuthority(adminAuth);

        // Crear objeto Dueno y asignarle el usuario de dueno
        dueno = new Dueno();
        dueno.setId(1);
        dueno.setFirstName("Juan Propietario");
        dueno.setLastName("García");
        dueno.setEmail("juan@gastrostock.com");
        dueno.setTokenDueno("TOKEN123");
        dueno.setUser(duenoUser);

        // Crear datos de negocio
        negocio1 = new Negocio();
        negocio1.setId(1);
        negocio1.setName("Restaurante La Tasca");
        negocio1.setDireccion("Calle Principal 123");
        negocio1.setCiudad("Sevilla");
        negocio1.setPais("Espana");
        negocio1.setCodigoPostal("41001");
        negocio1.setTokenNegocio(12345);
        negocio1.setDueno(dueno);
        negocio2 = new Negocio();
        negocio2.setId(2);
        negocio2.setName("Bar El Rincón");
        negocio2.setDireccion("Avenida de la Constitución 45");
        negocio2.setCiudad("Sevilla");
        negocio2.setPais("Espana");
        negocio2.setCodigoPostal("41001");
        negocio2.setTokenNegocio(67890);
        negocio2.setDueno(dueno);
        negocioList = Arrays.asList(negocio1, negocio2);

        // Stub general para los endpoints que usan al dueno
        lenient().when(userService.findCurrentUser()).thenReturn(duenoUser);
        lenient().when(duenoService.getDuenoByUser(duenoUser.getId())).thenReturn(dueno);
        lenient().when(negocioService.getByDueno(dueno.getId())).thenReturn(negocioList);
    }

    @Test
    void testFindAll() throws Exception {
        // Llama al endpoint GET /api/negocios que usa getByDueno
        mockMvc.perform(get("/api/negocios")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Restaurante La Tasca")))
                .andExpect(jsonPath("$[1].name", is("Bar El Rincón")));
        
        verify(negocioService, atLeastOnce()).getByDueno(dueno.getId());
    }
    
    @Test
    void testFindAllDTO() throws Exception {
        // Se espera que se devuelvan los DTO de los negocios
        when(negocioService.getByDueno(dueno.getId())).thenReturn(negocioList);
        when(negocioService.convertirNegocioDTO(any(Negocio.class)))
            .thenAnswer(invocation -> {
                Negocio n = invocation.getArgument(0);
                NegocioDTO dto = new NegocioDTO();
                dto.setName(n.getName());
                dto.setDireccion(n.getDireccion());
                dto.setCiudad(n.getCiudad());
                dto.setPais(n.getPais());
                dto.setCodigoPostal(n.getCodigoPostal());
                dto.setTokenNegocio(n.getTokenNegocio());
                dto.setIdDueno(n.getDueno().getId());
                return dto;
            });
        
        mockMvc.perform(get("/api/negocios/dto")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Restaurante La Tasca")));
    }
    
    @Test
    void testFindNegocioById() throws Exception {
        // Para que el endpoint tenga éxito, el usuario actual (duenoUser) debe ser el mismo que el dueño asignado al negocio.
        when(negocioService.getById(1)).thenReturn(negocio1);
        
        mockMvc.perform(get("/api/negocios/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Restaurante La Tasca")));
        
        verify(negocioService).getById(1);
    }
    
    @Test
    void testFindNegocioById_NotFound() throws Exception {
        when(negocioService.getById(999)).thenReturn(null);
        
        mockMvc.perform(get("/api/negocios/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(negocioService).getById(999);
    }
    
    @Test
    void testFindNegocioByToken() throws Exception {
        // Este endpoint requiere usuario admin, así que lo sobreescribimos
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(negocioService.getByToken(12345)).thenReturn(negocio1);
        
        mockMvc.perform(get("/api/negocios/token/12345")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tokenNegocio", is(12345)))
                .andExpect(jsonPath("$.name", is("Restaurante La Tasca")));
        
        verify(negocioService).getByToken(12345);
    }
    
    @Test
    void testFindNegocioByToken_NotFound() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(negocioService.getByToken(9999)).thenReturn(null);
        
        mockMvc.perform(get("/api/negocios/token/9999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(negocioService).getByToken(9999);
    }
    
    @Test
    void testFindNegocioByName() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(negocioService.getByName("Restaurante La Tasca")).thenReturn(negocio1);
        
        mockMvc.perform(get("/api/negocios/name/Restaurante La Tasca")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Restaurante La Tasca")));
        
        verify(negocioService).getByName("Restaurante La Tasca");
    }
    
    @Test
    void testFindNegocioByCiudad() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(negocioService.getByCiudad("Sevilla")).thenReturn(negocioList);
        
        mockMvc.perform(get("/api/negocios/ciudad/Sevilla")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].ciudad", is("Sevilla")));
        
        verify(negocioService).getByCiudad("Sevilla");
    }
    
    @Test
    void testFindNegocioByCodigoPostal() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(negocioService.getByCodigoPostal("41001")).thenReturn(negocioList);
        
        mockMvc.perform(get("/api/negocios/codigoPostal/41001")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].codigoPostal", is("41001")));
        
        verify(negocioService).getByCodigoPostal("41001");
    }
    
    @Test
    void testFindNegocioByPais() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(negocioService.getByPais("Espana")).thenReturn(negocioList);
        
        mockMvc.perform(get("/api/negocios/pais/Espana")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].pais", is("Espana")));
        
        verify(negocioService).getByPais("Espana");
    }
    
    @Test
    void testFindNegocioByDireccion() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        List<Negocio> negociosByDireccion = Arrays.asList(negocio1);
        when(negocioService.getByDireccion("Calle Principal 123")).thenReturn(negociosByDireccion);
    
        mockMvc.perform(get("/api/negocios/direccion/Calle Principal 123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].direccion", is("Calle Principal 123")));
    
        verify(negocioService).getByDireccion("Calle Principal 123");
    }
    
    @Test
    void testFindNegocioByDueno() throws Exception {
        // Use the user with "dueno" authority
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        doReturn(true).when(duenoUser).hasAnyAuthority();
        when(negocioService.getByDueno(dueno.getId())).thenReturn(negocioList);
        
        mockMvc.perform(get("/api/negocios/dueno/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
        
        verify(negocioService).getByDueno(1);
        verify(userService).findCurrentUser();
        verify(duenoUser).hasAnyAuthority();
    }
    
    @Test
    void testSaveNegocio() throws Exception {
        // Para el POST, el controlador asigna el token y el dueño usando el usuario actual
        NegocioDTO negocioDTO = new NegocioDTO();
        negocioDTO.setName("Restaurante Nuevo");
        negocioDTO.setDireccion("Calle Nueva 456");
        negocioDTO.setCiudad("Sevilla");
        negocioDTO.setPais("Espana");
        negocioDTO.setCodigoPostal("41001");
        // El token se asigna automáticamente, se puede pasar 0 o ignorarlo
        negocioDTO.setTokenNegocio(0);
        negocioDTO.setIdDueno(dueno.getId());
        
        Negocio negocioCreado = new Negocio();
        negocioCreado.setId(3);
        negocioCreado.setName("Restaurante Nuevo");
        negocioCreado.setDireccion("Calle Nueva 456");
        negocioCreado.setCiudad("Sevilla");
        negocioCreado.setPais("Espana");
        negocioCreado.setCodigoPostal("41001");
        // Simulamos que se genera un token, por ejemplo 55555
        negocioCreado.setTokenNegocio(55555);
        negocioCreado.setDueno(dueno);
        
        when(negocioService.convertirDTONegocio(any(NegocioDTO.class))).thenReturn(negocioCreado);
        when(negocioService.save(any(Negocio.class))).thenReturn(negocioCreado);
        
        // Para que funcione el POST, el usuario actual (duenoUser) se usa y ya está stubbeado en setUp
        
        mockMvc.perform(post("/api/negocios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(negocioDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Restaurante Nuevo")));
        
        verify(negocioService).convertirDTONegocio(any(NegocioDTO.class));
        verify(negocioService).save(any(Negocio.class));
    }
    
    @Test
    void testModifyNegocio() throws Exception {
        // Este endpoint PUT requiere que el usuario tenga autoridad "dueno" y sea dueño del negocio
        // Usamos el usuario por defecto (duenoUser) que es el dueño del negocio en setUp.
        NegocioDTO negocioDTO = new NegocioDTO();
        negocioDTO.setName("Restaurante Actualizado");
        negocioDTO.setDireccion("Nueva Dirección 123");
        negocioDTO.setCiudad("Sevilla");
        negocioDTO.setPais("Espana");
        negocioDTO.setCodigoPostal("41001");
        negocioDTO.setTokenNegocio(0);
        negocioDTO.setIdDueno(dueno.getId());
        
        Negocio negocioActualizado = new Negocio();
        negocioActualizado.setId(1);
        negocioActualizado.setName("Restaurante Actualizado");
        negocioActualizado.setDireccion("Nueva Dirección 123");
        negocioActualizado.setCiudad("Sevilla");
        negocioActualizado.setPais("Espana");
        negocioActualizado.setCodigoPostal("41001");
        negocioActualizado.setTokenNegocio(12345); // token original o modificado según lógica
        negocioActualizado.setDueno(dueno);
        
        when(negocioService.getById(1)).thenReturn(negocio1);
        when(negocioService.convertirDTONegocio(any(NegocioDTO.class))).thenReturn(negocioActualizado);
        when(negocioService.update(1, negocioActualizado)).thenReturn(negocioActualizado);
        
        mockMvc.perform(put("/api/negocios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(negocioDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Restaurante Actualizado")));
        
        verify(negocioService).update(1, negocioActualizado);
        verify(negocioService).convertirDTONegocio(any(NegocioDTO.class));
    }

    @Test
    void testModifyNegocio_InvalidId() throws Exception {
        // Since duenoUser is already set up in setUp(), we just need to mock getById
        when(negocioService.getById(1)).thenReturn(null);
    
        NegocioDTO invalidNegocioDTO = new NegocioDTO();
        invalidNegocioDTO.setName("Restaurante Inválido");
        invalidNegocioDTO.setDireccion("Calle Principal 123");
        invalidNegocioDTO.setCiudad("Sevilla");
        invalidNegocioDTO.setPais("Espana");
        invalidNegocioDTO.setCodigoPostal("41001");
        invalidNegocioDTO.setTokenNegocio(9999);
        invalidNegocioDTO.setIdDueno(dueno.getId());
    
        // Act & Assert
        mockMvc.perform(put("/api/negocios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidNegocioDTO)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertNull(result.getResolvedException()));
    
        // Verify
        verify(negocioService).getById(1);
        verify(negocioService, never()).update(anyInt(), any(Negocio.class));
        verify(negocioService, never()).convertirDTONegocio(any(NegocioDTO.class));
    }
    
    @Test
    void testDeleteNegocio_NotFound() throws Exception {
        when(negocioService.getById(9999)).thenReturn(null);
        
        mockMvc.perform(delete("/api/negocios/9999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
                
        verify(negocioService).getById(9999);
    }
}
