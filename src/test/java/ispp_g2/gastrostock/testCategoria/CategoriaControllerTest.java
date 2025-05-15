package ispp_g2.gastrostock.testCategoria;

import com.fasterxml.jackson.databind.ObjectMapper;
import ispp_g2.gastrostock.categorias.Categoria;
import ispp_g2.gastrostock.categorias.CategoriaController;
import ispp_g2.gastrostock.categorias.CategoriaService;
import ispp_g2.gastrostock.categorias.Pertenece;
import ispp_g2.gastrostock.config.SecurityConfiguration;
import ispp_g2.gastrostock.config.jwt.JwtAuthFilter;
import ispp_g2.gastrostock.config.jwt.JwtService;
import ispp_g2.gastrostock.empleado.EmpleadoService;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioService;
import ispp_g2.gastrostock.user.Authorities;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.model.Person;
import ispp_g2.gastrostock.user.UserService;
import ispp_g2.gastrostock.dueno.Dueno;
import ispp_g2.gastrostock.categorias.CategoriaDTO;
import ispp_g2.gastrostock.empleado.Empleado;


import static org.mockito.ArgumentMatchers.*;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import ispp_g2.gastrostock.exceptions.ResourceNotFoundException;
import java.util.Collections;
import java.util.List;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoriaController.class)
@Import({SecurityConfiguration.class, JwtAuthFilter.class})
@ActiveProfiles("test")
@WithMockUser(username = "admin", roles = {"admin"})
public class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoriaService categoriaService;

    @MockBean
    private NegocioService negocioService;

    @MockBean
    private UserService userService;

    @MockBean
    private EmpleadoService empleadoService;

    @Autowired
    private CategoriaController categoriaController;

    // Estos son del filtro de seguridad:
    @MockBean private JwtService jwtService;
    @MockBean private AuthenticationProvider authenticationProvider;
    @MockBean private UserDetailsService userDetailsService;
    
    private Categoria categoria;
    private Negocio negocio;
    private User adminUser;

    @BeforeEach
    void setUp() {
        // Negocio y categoría de prueba
        negocio = new Negocio();
        negocio.setId(1);

        categoria = new Categoria();
        categoria.setId(1);
        categoria.setName("Bebidas");
        categoria.setNegocio(negocio);
        categoria.setPertenece(Pertenece.INVENTARIO);

        // Usuario admin simulado
        adminUser = new User();
        adminUser.setId(1);
        Authorities auth = new Authorities();
        auth.setAuthority("admin");
        adminUser.setAuthority(auth);

        // Mocks de seguridad y usuario
        when(jwtService.getUserNameFromJwtToken(anyString())).thenReturn("admin");
        when(jwtService.validateJwtToken(anyString(), any())).thenReturn(true);
        when(userService.findCurrentUser()).thenReturn(adminUser);
    }

    @Test
    public void testFindAll_ReturnsCategorias() throws Exception {
        when(categoriaService.getCategorias()).thenReturn(List.of(categoria));
        
        mockMvc.perform(get("/api/categorias")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].name", is("Bebidas")));
        
        verify(categoriaService, times(2)).getCategorias();
        }
    
    @Test
    public void testFindAll_ReturnsNoContent() throws Exception {
        when(categoriaService.getCategorias()).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/api/categorias")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
        
        verify(categoriaService).getCategorias();
    }
    
    // GET /api/categorias/{id}
    @Test
    public void testFindById_ExistingCategoria() throws Exception {
        when(categoriaService.getById(1)).thenReturn(categoria);
        
        mockMvc.perform(get("/api/categorias/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("Bebidas")));
        
        verify(categoriaService).getById(1);
    }
    
    @Test
    public void testFindById_NonExistingCategoria() throws Exception {
        when(categoriaService.getById(99)).thenReturn(null);
    
        mockMvc.perform(get("/api/categorias/99")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())          
            .andExpect(content().string(""));
    
        verify(categoriaService).getById(99);
    }
    
    
    // GET /api/categorias/negocio/{negocioId}
    @Test
    public void testFindByNegocioId_ReturnsCategorias() throws Exception {
        when(categoriaService.getCategoriasByNegocioId(1)).thenReturn(List.of(categoria));
        
        mockMvc.perform(get("/api/categorias/negocio/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));
        
        verify(categoriaService, times(2)).getCategoriasByNegocioId(1);
    }
    
    @Test
    public void testFindByNegocioId_ReturnsNoContent() throws Exception {
        when(categoriaService.getCategoriasByNegocioId(1)).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/api/categorias/negocio/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
        
        verify(categoriaService).getCategoriasByNegocioId(1);
    }
    
    // GET /api/categorias/negocio/{negocioId}/inventario
    @Test
    public void testFindByNegocioIdInventario_ReturnsCategorias() throws Exception {
        when(categoriaService.getCategoriasInventarioByNegocioId(1))
            .thenReturn(List.of(categoria));
        
        mockMvc.perform(get("/api/categorias/negocio/1/inventario")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())            // ahora espera 200
            .andExpect(jsonPath("$", hasSize(1)));
        
        verify(categoriaService, times(2)).getCategoriasInventarioByNegocioId(1);
    }
    
    
    @Test
    public void testFindByNegocioIdInventario_ReturnsNoContent() throws Exception {
        when(categoriaService.getCategoriasInventarioByNegocioId(1))
            .thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/api/categorias/negocio/1/inventario")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
        
        verify(categoriaService).getCategoriasInventarioByNegocioId(1);
    }
    
    
    // GET /api/categorias/negocio/{negocioId}/venta
    @Test
    public void testFindByNegocioIdVenta_ReturnsNoContentForEmptyListAsAdmin() throws Exception {
        when(categoriaService.getCategoriasVentaByNegocioId(1))
            .thenReturn(Collections.emptyList());
    
        mockMvc.perform(get("/api/categorias/negocio/1/venta")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));
    
        verify(categoriaService).getCategoriasVentaByNegocioId(1);
    }
    
    
    // GET /api/categorias/nombre/{name}
    @Test
    public void testFindByName_ReturnsCategorias() throws Exception {
        when(categoriaService.getCategoriasByName("Bebidas"))
            .thenReturn(List.of(categoria));
    
        mockMvc.perform(get("/api/categorias/nombre/Bebidas")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));
    
        verify(categoriaService, times(2)).getCategoriasByName("Bebidas");
    }
    
    
    @Test
    public void testFindByName_ReturnsNoContent() throws Exception {
        when(categoriaService.getCategoriasByName("NoExiste")).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/api/categorias/nombre/NoExiste")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
        
        verify(categoriaService).getCategoriasByName("NoExiste");
    }
    
    // POST /api/categorias
    @Test
    public void testSave_ReturnsCreated() throws Exception {
        when(categoriaService.save(any(Categoria.class))).thenReturn(categoria);
        
        mockMvc.perform(post("/api/categorias")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoria)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name", is("Bebidas")));
        
        verify(categoriaService).save(any(Categoria.class));
    }
    
    @Test
    public void testSave_ReturnsBadRequestWhenNull() throws Exception {
        // Enviar un JSON vacío debería provocar error de validación
        mockMvc.perform(post("/api/categorias")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }
    
    // PUT /api/categorias/{id}
    @Test
    public void testUpdate_ExistingCategoria() throws Exception {
        Categoria categoriaActualizada = new Categoria();
        categoriaActualizada.setId(1);
        categoriaActualizada.setName("Bebidas Actualizadas");
        categoriaActualizada.setNegocio(negocio);
        categoriaActualizada.setPertenece(Pertenece.INVENTARIO);
        
        when(categoriaService.getById(1)).thenReturn(categoria);
        when(categoriaService.update(eq(1), any(Categoria.class)))
            .thenReturn(categoriaActualizada);
        
        mockMvc.perform(put("/api/categorias/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoriaActualizada)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("Bebidas Actualizadas")));
        
        verify(categoriaService).getById(1);
        verify(categoriaService).update(eq(1), any(Categoria.class));
        verify(categoriaService, never()).save(any());
    }
    
    
    @Test
    public void testUpdate_NonExistingCategoria() throws Exception {
        when(categoriaService.getById(99)).thenReturn(null);
        when(categoriaService.update(eq(99), any(Categoria.class)))
            .thenReturn(categoria);

        mockMvc.perform(put("/api/categorias/99")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoria)))
            .andExpect(status().isOk())   
            .andExpect(jsonPath("$.name", is("Bebidas")));  // y el cuerpo con la categoría

        verify(categoriaService).getById(99);
        verify(categoriaService).update(eq(99), any(Categoria.class));
        verify(categoriaService, never()).save(any());
    }

    
    @Test
    public void testUpdate_NullCategoria() throws Exception {
        mockMvc.perform(put("/api/categorias/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }
    
    // DELETE /api/categorias/{id}
    @Test
    public void testDelete_ExistingCategoria() throws Exception {
        doNothing().when(categoriaService).delete(1);
        
        mockMvc.perform(delete("/api/categorias/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
        
        verify(categoriaService).delete(1);
        verify(categoriaService, never()).getById(anyInt());
    }

    
    @Test
    public void testDelete_NonExistingCategoria() throws Exception {
        doNothing().when(categoriaService).delete(99);

        mockMvc.perform(delete("/api/categorias/99")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        verify(categoriaService).delete(99);
        verify(categoriaService, never()).getById(anyInt());
    }

    @Test
    public void testFindByIdDto_ExistingCategoria() throws Exception {
        when(categoriaService.getById(1)).thenReturn(categoria);
        
        CategoriaDTO categoriaDTO = CategoriaDTO.of(categoria);
        
        mockMvc.perform(get("/api/categorias/dto/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre", is("Bebidas")))
            .andExpect(jsonPath("$.negocioId", is(1)))
            .andExpect(jsonPath("$.pertenece", is("INVENTARIO")));
        
        verify(categoriaService).getById(1);
    }
    
    @Test
    public void testFindByIdDto_NonExistingCategoria() throws Exception {
        when(categoriaService.getById(99)).thenThrow(new ResourceNotFoundException("La categoria no existe"));
        
        mockMvc.perform(get("/api/categorias/dto/99")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());  // Usa 404 para ResourceNotFoundException
        
        verify(categoriaService).getById(99);
    }
    
    // TESTS PARA findByNegocioIdDto()
    
    @Test
    public void testFindByNegocioIdDto_ReturnsCategorias() throws Exception {
        when(categoriaService.getCategoriasByNegocioId(1)).thenReturn(List.of(categoria));
        
        mockMvc.perform(get("/api/categorias/dto/negocio/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].nombre", is("Bebidas")))
            .andExpect(jsonPath("$[0].negocioId", is(1)));
        
        verify(categoriaService, times(2)).getCategoriasByNegocioId(1);
    }
    
    @Test
    public void testFindByNegocioIdDto_ReturnsNoContent() throws Exception {
        when(categoriaService.getCategoriasByNegocioId(1)).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/api/categorias/dto/negocio/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
        
        verify(categoriaService).getCategoriasByNegocioId(1);
    }
    
    // TESTS PARA findByNegocioIdInventarioDto()
    
    @Test
    public void testFindByNegocioIdInventarioDto_ReturnsCategorias() throws Exception {
        when(categoriaService.getCategoriasInventarioByNegocioId(1))
            .thenReturn(List.of(categoria));
        
        mockMvc.perform(get("/api/categorias/dto/negocio/1/inventario")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].nombre", is("Bebidas")))
            .andExpect(jsonPath("$[0].negocioId", is(1)))
            .andExpect(jsonPath("$[0].pertenece", is("INVENTARIO")));
        
        verify(categoriaService, times(2)).getCategoriasInventarioByNegocioId(1);
    }
    
    @Test
    public void testFindByNegocioIdInventarioDto_ReturnsNoContent() throws Exception {
        when(categoriaService.getCategoriasInventarioByNegocioId(1))
            .thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/api/categorias/dto/negocio/1/inventario")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
        
        verify(categoriaService).getCategoriasInventarioByNegocioId(1);
    }
    
    // TESTS PARA findByNegocioIdVentaDto()
    
    @Test
    public void testFindByNegocioIdVentaDto_ReturnsCategorias() throws Exception {
        Categoria categoriaVenta = new Categoria();
        categoriaVenta.setId(2);
        categoriaVenta.setName("Platos");
        categoriaVenta.setNegocio(negocio);
        categoriaVenta.setPertenece(Pertenece.VENTA);
        
        when(categoriaService.getCategoriasVentaByNegocioId(1))
            .thenReturn(List.of(categoriaVenta));
        
        mockMvc.perform(get("/api/categorias/dto/negocio/1/venta")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].nombre", is("Platos")))
            .andExpect(jsonPath("$[0].negocioId", is(1)))
            .andExpect(jsonPath("$[0].pertenece", is("VENTA")));
        
        verify(categoriaService, times(2)).getCategoriasVentaByNegocioId(1);
    }
    
    @Test
    public void testFindByNegocioIdVentaDto_ReturnsNoContent() throws Exception {
        when(categoriaService.getCategoriasVentaByNegocioId(1))
            .thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/api/categorias/dto/negocio/1/venta")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
        
        verify(categoriaService).getCategoriasVentaByNegocioId(1);
    }
    
    // TESTS PARA saveDto()
    
    @Test
    public void testSaveDto_ReturnsCreated() throws Exception {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre("Bebidas");
        dto.setNegocioId(1);
        dto.setPertenece(Pertenece.INVENTARIO);
        
        when(categoriaService.convertirCategoria(any(CategoriaDTO.class))).thenReturn(categoria);
        when(categoriaService.save(any(Categoria.class))).thenReturn(categoria);
        
        mockMvc.perform(post("/api/categorias/dto")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name", is("Bebidas")));
        
        verify(categoriaService).convertirCategoria(any(CategoriaDTO.class));
        verify(categoriaService).save(any(Categoria.class));
    }
    
    @Test
    public void testSaveDto_ReturnsBadRequestWhenNull() throws Exception {
        mockMvc.perform(post("/api/categorias/dto")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
            .andExpect(status().isBadRequest());
        
        verify(categoriaService, never()).convertirCategoria(any(CategoriaDTO.class));
        verify(categoriaService, never()).save(any(Categoria.class));
    }
    
    @Test
    public void testSaveDto_ReturnsBadRequestWhenInvalidData() throws Exception {
        CategoriaDTO dto = new CategoriaDTO();
        // No se establecen los campos requeridos
        
        mockMvc.perform(post("/api/categorias/dto")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest());
        
        verify(categoriaService, never()).convertirCategoria(any(CategoriaDTO.class));
        verify(categoriaService, never()).save(any(Categoria.class));
    }
    
    // TESTS PARA updateDto()
    
    @Test
    public void testUpdateDto_ExistingCategoria() throws Exception {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setId(1);
        dto.setNombre("Bebidas Actualizadas");
        dto.setNegocioId(1);
        dto.setPertenece(Pertenece.INVENTARIO);
        
        Categoria categoriaActualizada = new Categoria();
        categoriaActualizada.setId(1);
        categoriaActualizada.setName("Bebidas Actualizadas");
        categoriaActualizada.setNegocio(negocio);
        categoriaActualizada.setPertenece(Pertenece.INVENTARIO);
        
        when(categoriaService.getById(1)).thenReturn(categoria);
        when(categoriaService.convertirCategoria(any(CategoriaDTO.class))).thenReturn(categoriaActualizada);
        when(categoriaService.update(eq(1), any(Categoria.class))).thenReturn(categoriaActualizada);
        
        mockMvc.perform(put("/api/categorias/dto/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("Bebidas Actualizadas")));
        
        verify(categoriaService).getById(1);
        verify(categoriaService).convertirCategoria(any(CategoriaDTO.class));
        verify(categoriaService).update(eq(1), any(Categoria.class));
    }
    
    @Test
    public void testUpdateDto_NonExistingCategoria() throws Exception {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setId(99);
        dto.setNombre("Bebidas");
        dto.setNegocioId(1);
        dto.setPertenece(Pertenece.INVENTARIO);
        
        when(categoriaService.getById(99)).thenThrow(new ResourceNotFoundException("La categoria no existe"));
        
        mockMvc.perform(put("/api/categorias/dto/99")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isNotFound()); 
            
        verify(categoriaService).getById(99);
        verify(categoriaService, never()).convertirCategoria(any(CategoriaDTO.class));
        verify(categoriaService, never()).update(anyInt(), any(Categoria.class));
    }
    
    @Test
    public void testUpdateDto_NullCategoria() throws Exception {
        mockMvc.perform(put("/api/categorias/dto/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
            
        verify(categoriaService, never()).getById(anyInt());
        verify(categoriaService, never()).convertirCategoria(any(CategoriaDTO.class));
        verify(categoriaService, never()).update(anyInt(), any(Categoria.class));
    }
    
    // TESTS PARA CASOS CON DIFERENTES ROLES DE USUARIO
    
    @Test
    @WithMockUser(username = "dueno", roles = {"dueno"})
    public void testFindById_AsDueno_Success() throws Exception {
        User duenoUser = new User();
        duenoUser.setId(2);
        Authorities duenoAuth = new Authorities();
        duenoAuth.setAuthority("dueno");
        duenoUser.setAuthority(duenoAuth);
        
        Dueno dueno = new Dueno();
        dueno.setUser(duenoUser);
        
        Negocio negocioDueno = new Negocio();
        negocioDueno.setId(1);
        negocioDueno.setDueno(dueno);
        
        Categoria categoriaDelDueno = new Categoria();
        categoriaDelDueno.setId(1);
        categoriaDelDueno.setName("Bebidas");
        categoriaDelDueno.setNegocio(negocioDueno);
        categoriaDelDueno.setPertenece(Pertenece.INVENTARIO);
        
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(categoriaService.getById(1)).thenReturn(categoriaDelDueno);
        
        mockMvc.perform(get("/api/categorias/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("Bebidas")));
        
        verify(categoriaService, atLeastOnce()).getById(1);
    }
    
    @Test
    @WithMockUser(username = "dueno", roles = {"dueno"})
    public void testFindById_AsDueno_Forbidden() throws Exception {
        User duenoUser = new User();
        duenoUser.setId(2);
        Authorities duenoAuth = new Authorities();
        duenoAuth.setAuthority("dueno");
        duenoUser.setAuthority(duenoAuth);
        
        // Crear otro usuario con ID diferente para el dueño de la categoría
        User otroUser = new User();
        otroUser.setId(3); // ID diferente al usuario actual (2)
        
        Dueno dueno = new Dueno();
        dueno.setUser(otroUser); // Asignar el usuario con ID explícito
        
        Negocio negocioDueno = new Negocio();
        negocioDueno.setId(1);
        negocioDueno.setDueno(dueno);
        
        Categoria categoriaDeOtroDueno = new Categoria();
        categoriaDeOtroDueno.setId(1);
        categoriaDeOtroDueno.setName("Bebidas");
        categoriaDeOtroDueno.setNegocio(negocioDueno);
        categoriaDeOtroDueno.setPertenece(Pertenece.INVENTARIO);
        
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(categoriaService.getById(1)).thenReturn(categoriaDeOtroDueno);
        
        mockMvc.perform(get("/api/categorias/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
        
        verify(categoriaService).getById(1);
}
    
    @Test
    @WithMockUser(username = "empleado", roles = {"empleado"})
    public void testFindById_AsEmpleado_Success() throws Exception {
        User empleadoUser = new User();
        empleadoUser.setId(3);
        Authorities empleadoAuth = new Authorities();
        empleadoAuth.setAuthority("empleado");
        empleadoUser.setAuthority(empleadoAuth);
        
        Negocio negocioEmpleado = new Negocio();
        negocioEmpleado.setId(1);
        
        Empleado empleado = new Empleado();
        empleado.setUser(empleadoUser);
        empleado.setNegocio(negocioEmpleado);
        
        Categoria categoriaDelNegocioEmpleado = new Categoria();
        categoriaDelNegocioEmpleado.setId(1);
        categoriaDelNegocioEmpleado.setName("Bebidas");
        categoriaDelNegocioEmpleado.setNegocio(negocioEmpleado);
        categoriaDelNegocioEmpleado.setPertenece(Pertenece.INVENTARIO);
        
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoByUser(empleadoUser.getId())).thenReturn(empleado);
        when(categoriaService.getById(1)).thenReturn(categoriaDelNegocioEmpleado);
        
        mockMvc.perform(get("/api/categorias/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("Bebidas")));
        
        verify(categoriaService, atLeastOnce()).getById(1);
        verify(empleadoService).getEmpleadoByUser(empleadoUser.getId());
    }
    
    @Test
    @WithMockUser(username = "empleado", roles = {"empleado"})
    public void testFindById_AsEmpleado_Forbidden() throws Exception {
        User empleadoUser = new User();
        empleadoUser.setId(3);
        Authorities empleadoAuth = new Authorities();
        empleadoAuth.setAuthority("empleado");
        empleadoUser.setAuthority(empleadoAuth);
        
        Negocio negocioEmpleado = new Negocio();
        negocioEmpleado.setId(1);
        
        Empleado empleado = new Empleado();
        empleado.setUser(empleadoUser);
        empleado.setNegocio(negocioEmpleado);
        
        Negocio otroNegocio = new Negocio();
        otroNegocio.setId(2);
        
        Categoria categoriaDeOtroNegocio = new Categoria();
        categoriaDeOtroNegocio.setId(1);
        categoriaDeOtroNegocio.setName("Bebidas");
        categoriaDeOtroNegocio.setNegocio(otroNegocio);
        categoriaDeOtroNegocio.setPertenece(Pertenece.INVENTARIO);
        
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoByUser(empleadoUser.getId())).thenReturn(empleado);
        when(categoriaService.getById(1)).thenReturn(categoriaDeOtroNegocio);
        
        mockMvc.perform(get("/api/categorias/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
        
        verify(categoriaService).getById(1);
        verify(empleadoService).getEmpleadoByUser(empleadoUser.getId());
    }

    @Test
    @WithMockUser(username = "dueno", roles = {"dueno"})
    public void testFindAll_AsDueno_ReturnsForbidden() throws Exception {
        User duenoUser = new User();
        duenoUser.setId(2);
        Authorities duenoAuth = new Authorities();
        duenoAuth.setAuthority("dueno");
        duenoUser.setAuthority(duenoAuth);
        
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        
        mockMvc.perform(get("/api/categorias")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }
    
    // GET - findById (exitoso, ya implementado)
    
    // GET - findByNegocioId
    @Test
    @WithMockUser(username = "dueno", roles = {"dueno"})
    public void testFindByNegocioId_AsDueno_Success() throws Exception {
        // Configuración del usuario dueño
        User duenoUser = new User();
        duenoUser.setId(2);
        Authorities duenoAuth = new Authorities();
        duenoAuth.setAuthority("dueno");
        duenoUser.setAuthority(duenoAuth);
        
        // Configuración del dueño
        Dueno dueno = new Dueno();
        dueno.setUser(duenoUser);
        
        // Configuración del negocio
        Negocio negocioDueno = new Negocio();
        negocioDueno.setId(1);
        negocioDueno.setDueno(dueno);
        
        // Configuración de la categoría
        Categoria categoriaDelDueno = new Categoria();
        categoriaDelDueno.setId(1);
        categoriaDelDueno.setName("Bebidas");
        categoriaDelDueno.setNegocio(negocioDueno);
        
        // Mock de los servicios
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(negocioService.getById(1)).thenReturn(negocioDueno);
        when(categoriaService.getCategoriasByNegocioId(1)).thenReturn(List.of(categoriaDelDueno));
        
        mockMvc.perform(get("/api/categorias/negocio/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].name", is("Bebidas")));
        
        verify(userService, atLeastOnce()).findCurrentUser();
        verify(negocioService).getById(1);
        verify(categoriaService, times(2)).getCategoriasByNegocioId(1);
    }
    
    @Test
    @WithMockUser(username = "dueno", roles = {"dueno"})
    public void testFindByNegocioId_AsDueno_Forbidden() throws Exception {
        // Configuración del usuario dueño
        User duenoUser = new User();
        duenoUser.setId(2);
        Authorities duenoAuth = new Authorities();
        duenoAuth.setAuthority("dueno");
        duenoUser.setAuthority(duenoAuth);
        
        // Configuración de otro dueño
        User otroDuenoUser = new User();
        otroDuenoUser.setId(3);
        Dueno otroDueno = new Dueno();
        otroDueno.setUser(otroDuenoUser);
        
        // Configuración del negocio de otro dueño
        Negocio negocioOtroDueno = new Negocio();
        negocioOtroDueno.setId(1);
        negocioOtroDueno.setDueno(otroDueno);
        
        // Mock de los servicios
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(negocioService.getById(1)).thenReturn(negocioOtroDueno);
        
        mockMvc.perform(get("/api/categorias/negocio/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
        
        verify(userService).findCurrentUser();
        verify(negocioService).getById(1);
        verify(categoriaService, never()).getCategoriasByNegocioId(anyInt());
    }
    
    // GET - findByNegocioIdInventario
    @Test
    @WithMockUser(username = "dueno", roles = {"dueno"})
    public void testFindByNegocioIdInventario_AsDueno_Success() throws Exception {
        // Configuración del usuario dueño
        User duenoUser = new User();
        duenoUser.setId(2);
        Authorities duenoAuth = new Authorities();
        duenoAuth.setAuthority("dueno");
        duenoUser.setAuthority(duenoAuth);
        
        // Configuración del dueño
        Dueno dueno = new Dueno();
        dueno.setUser(duenoUser);
        
        // Configuración del negocio
        Negocio negocioDueno = new Negocio();
        negocioDueno.setId(1);
        negocioDueno.setDueno(dueno);
        
        // Configuración de la categoría
        Categoria categoriaInventario = new Categoria();
        categoriaInventario.setId(1);
        categoriaInventario.setName("Bebidas");
        categoriaInventario.setNegocio(negocioDueno);
        categoriaInventario.setPertenece(Pertenece.INVENTARIO);
        
        // Mock de los servicios
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(negocioService.getById(1)).thenReturn(negocioDueno);
        when(categoriaService.getCategoriasInventarioByNegocioId(1)).thenReturn(List.of(categoriaInventario));
        
        mockMvc.perform(get("/api/categorias/negocio/1/inventario")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].name", is("Bebidas")))
            .andExpect(jsonPath("$[0].pertenece", is("INVENTARIO")));
        
        verify(userService, atLeastOnce()).findCurrentUser();
        verify(negocioService).getById(1);
        verify(categoriaService, times(2)).getCategoriasInventarioByNegocioId(1);
    }
    
    // GET - findByNegocioIdVenta
    @Test
    @WithMockUser(username = "dueno", roles = {"dueno"})
    public void testFindByNegocioIdVenta_AsDueno_Success() throws Exception {
        // Configuración del usuario dueño
        User duenoUser = new User();
        duenoUser.setId(2);
        Authorities duenoAuth = new Authorities();
        duenoAuth.setAuthority("dueno");
        duenoUser.setAuthority(duenoAuth);
        
        // Configuración del dueño
        Dueno dueno = new Dueno();
        dueno.setUser(duenoUser);
        
        // Configuración del negocio
        Negocio negocioDueno = new Negocio();
        negocioDueno.setId(1);
        negocioDueno.setDueno(dueno);
        
        // Configuración de la categoría
        Categoria categoriaVenta = new Categoria();
        categoriaVenta.setId(2);
        categoriaVenta.setName("Platos");
        categoriaVenta.setNegocio(negocioDueno);
        categoriaVenta.setPertenece(Pertenece.VENTA);
        
        // Mock de los servicios
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(negocioService.getById(1)).thenReturn(negocioDueno);
        when(categoriaService.getCategoriasVentaByNegocioId(1)).thenReturn(List.of(categoriaVenta));
        
        mockMvc.perform(get("/api/categorias/negocio/1/venta")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].name", is("Platos")));
        
        verify(userService, atLeastOnce()).findCurrentUser();
        verify(negocioService).getById(1);
        verify(categoriaService, atLeastOnce()).getCategoriasVentaByNegocioId(1);
    }
    
    // GET - findByName
    @Test
    @WithMockUser(username = "dueno", roles = {"dueno"})
    public void testFindByName_AsDueno_Forbidden() throws Exception {
        User duenoUser = new User();
        duenoUser.setId(2);
        Authorities duenoAuth = new Authorities();
        duenoAuth.setAuthority("dueno");
        duenoUser.setAuthority(duenoAuth);
        
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        
        mockMvc.perform(get("/api/categorias/nombre/Bebidas")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
        
        verify(userService).findCurrentUser();
        verify(categoriaService, never()).getCategoriasByName(anyString());
    }
    
    @Test
    @WithMockUser(username = "dueno", roles = {"dueno"})
    public void testSave_AsDueno_Success() throws Exception {
        // Configuración del usuario dueño
        User duenoUser = new User();
        duenoUser.setId(2);
        duenoUser.setUsername("dueno_username"); 
        Authorities duenoAuth = new Authorities();
        duenoAuth.setId(1);
        duenoAuth.setAuthority("dueno");
        duenoUser.setAuthority(duenoAuth);
        
        // Configuración del dueño
        Dueno dueno = new Dueno();
        dueno.setId(1);
        dueno.setFirstName("Juan");
        dueno.setLastName("Pérez");
        dueno.setEmail("juan@example.com");
        dueno.setNumTelefono("123456789");
        dueno.setTokenDueno("token123");
        dueno.setUser(duenoUser);
        
        // Configuración del negocio
        Negocio negocioCompleto = new Negocio();
        negocioCompleto.setId(1);
        negocioCompleto.setName("Mi Negocio");
        negocioCompleto.setTokenNegocio(12345);
        negocioCompleto.setDireccion("Calle Test");
        negocioCompleto.setCiudad("Ciudad Test");
        negocioCompleto.setPais("País Test");
        negocioCompleto.setCodigoPostal("12345");
        negocioCompleto.setDueno(dueno);
        
        // Mocks necesarios
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(negocioService.getById(1)).thenReturn(negocioCompleto);
        
        // Categoría para el resultado (saved)
        Categoria categoriaSaved = new Categoria();
        categoriaSaved.setId(10);
        categoriaSaved.setName("Nueva Categoría");
        categoriaSaved.setNegocio(negocioCompleto);
        categoriaSaved.setPertenece(Pertenece.INVENTARIO);
        when(categoriaService.save(any(Categoria.class))).thenReturn(categoriaSaved);
        
        // JSON con la estructura mínima requerida pero completa para el controller
        String requestJson = """
                {
                  "name": "Nueva Categoría",
                  "negocio": {
                    "id": 1,
                    "dueno": {
                      "user": {
                        "id": 2
                      }
                    }
                  },
                  "pertenece": "INVENTARIO"
                }
                """;
        
        mockMvc.perform(post("/api/categorias")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", is(10)))
            .andExpect(jsonPath("$.name", is("Nueva Categoría")));
        
        verify(userService, atLeastOnce()).findCurrentUser();
        verify(categoriaService).save(any(Categoria.class));
    }
    
    
    @Test
    @WithMockUser(username = "dueno", roles = {"dueno"})
    public void testUpdate_AsDueno_Success() throws Exception {
        // Usuario actual (dueño)
        User duenoUser = new User();
        duenoUser.setId(2);
        Authorities duenoAuth = new Authorities();
        duenoAuth.setAuthority("dueno");
        duenoUser.setAuthority(duenoAuth);
        
        // Configuración del dueño
        Dueno dueno = new Dueno();
        dueno.setId(1);
        dueno.setUser(duenoUser);
        
        // Configuración del negocio completo para el mock del servicio
        Negocio negocioCompleto = new Negocio();
        negocioCompleto.setId(1);
        negocioCompleto.setTokenNegocio(12345); // Campo @NotNull
        negocioCompleto.setDireccion("Calle Test"); // Campo @NotBlank
        negocioCompleto.setCiudad("Ciudad Test"); // Campo @NotBlank
        negocioCompleto.setPais("País Test"); // Campo @NotBlank
        negocioCompleto.setCodigoPostal("12345"); // Campo @NotNull
        negocioCompleto.setDueno(dueno);
        
        // Categoría existente que retornará getById (esta tiene la estructura completa)
        Categoria existingCategoria = new Categoria();
        existingCategoria.setId(1);
        existingCategoria.setName("Bebidas");
        existingCategoria.setNegocio(negocioCompleto);
        existingCategoria.setPertenece(Pertenece.INVENTARIO);
        
        // Categoría actualizada (resultado esperado)
        Categoria updatedCategoria = new Categoria();
        updatedCategoria.setId(1);
        updatedCategoria.setName("Bebidas Actualizadas");
        updatedCategoria.setNegocio(negocioCompleto);
        updatedCategoria.setPertenece(Pertenece.INVENTARIO);
        
        // Configuración de los mocks
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(categoriaService.getById(1)).thenReturn(existingCategoria);
        when(categoriaService.update(eq(1), any(Categoria.class))).thenReturn(updatedCategoria);
        
        // JSON que incluye toda la estructura necesaria para la validación
        String requestJson = String.format("""
                {
                  "id": 1,
                  "name": "Bebidas Actualizadas",
                  "negocio": {
                    "id": 1,
                    "dueno": {
                      "user": {
                        "id": %d
                      }
                    }
                  },
                  "pertenece": "INVENTARIO"
                }
                """, duenoUser.getId());
        
        // Ejecución del test con el JSON completo
        mockMvc.perform(put("/api/categorias/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("Bebidas Actualizadas")));
        
        verify(userService, atLeastOnce()).findCurrentUser();
        verify(categoriaService).getById(1);
        verify(categoriaService).update(eq(1), any(Categoria.class));
    }
    // DELETE - delete
    @Test
    @WithMockUser(username = "dueno", roles = {"dueno"})
    public void testDelete_AsDueno_Success() throws Exception {
        // Configuración del usuario dueño
        User duenoUser = new User();
        duenoUser.setId(2);
        Authorities duenoAuth = new Authorities();
        duenoAuth.setAuthority("dueno");
        duenoUser.setAuthority(duenoAuth);
        
        // Configuración del dueño
        Dueno dueno = new Dueno();
        dueno.setUser(duenoUser);
        
        // Configuración del negocio
        Negocio negocioDueno = new Negocio();
        negocioDueno.setId(1);
        negocioDueno.setDueno(dueno);
        
        // Categoría a eliminar
        Categoria categoriaToDelete = new Categoria();
        categoriaToDelete.setId(1);
        categoriaToDelete.setName("Bebidas");
        categoriaToDelete.setNegocio(negocioDueno);
        
        when(userService.findCurrentUser()).thenReturn(duenoUser);
        when(categoriaService.getById(1)).thenReturn(categoriaToDelete);
        doNothing().when(categoriaService).delete(1);
        
        mockMvc.perform(delete("/api/categorias/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
        
        verify(userService).findCurrentUser();
        verify(categoriaService).getById(1);
        verify(categoriaService).delete(1);
    }
    
    // ============================ TESTS PARA ROL EMPLEADO ============================
    
    // GET - findById
    @Test
    @WithMockUser(username = "empleado", roles = {"empleado"})
    public void testFindByNegocioId_AsEmpleado_Success() throws Exception {
        // Configuración del usuario empleado
        User empleadoUser = new User();
        empleadoUser.setId(3);
        Authorities empleadoAuth = new Authorities();
        empleadoAuth.setAuthority("empleado");
        empleadoUser.setAuthority(empleadoAuth);
        
        // Configuración del negocio
        Negocio negocioEmpleado = new Negocio();
        negocioEmpleado.setId(1);
        
        // Configuración del empleado
        Empleado empleado = new Empleado();
        empleado.setId(1);
        empleado.setUser(empleadoUser);
        empleado.setNegocio(negocioEmpleado);
        
        // Configuración de la categoría
        Categoria categoriaDelNegocio = new Categoria();
        categoriaDelNegocio.setId(1);
        categoriaDelNegocio.setName("Bebidas");
        categoriaDelNegocio.setNegocio(negocioEmpleado);
        
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoByUser(empleadoUser.getId())).thenReturn(empleado);
        when(categoriaService.getCategoriasByNegocioId(1)).thenReturn(List.of(categoriaDelNegocio));
        
        mockMvc.perform(get("/api/categorias/negocio/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].name", is("Bebidas")));
        
        verify(userService).findCurrentUser();
        verify(empleadoService).getEmpleadoByUser(empleadoUser.getId());
        verify(categoriaService, times(2)).getCategoriasByNegocioId(1);
    }
    
    // GET - DTO endpoints
    @Test
    @WithMockUser(username = "empleado", roles = {"empleado"}) 
    public void testFindByIdDto_AsEmpleado_Success() throws Exception {
        User empleadoUser = new User();
        empleadoUser.setId(3);
        Authorities empleadoAuth = new Authorities();
        empleadoAuth.setAuthority("empleado");
        empleadoUser.setAuthority(empleadoAuth);
        
        Negocio negocioEmpleado = new Negocio();
        negocioEmpleado.setId(1);
        
        Empleado empleado = new Empleado();
        empleado.setUser(empleadoUser);
        empleado.setNegocio(negocioEmpleado);
        
        Categoria categoriaDelNegocio = new Categoria();
        categoriaDelNegocio.setId(1);
        categoriaDelNegocio.setName("Bebidas");
        categoriaDelNegocio.setNegocio(negocioEmpleado);
        categoriaDelNegocio.setPertenece(Pertenece.INVENTARIO);
        
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoByUser(empleadoUser.getId())).thenReturn(empleado);
        when(categoriaService.getById(1)).thenReturn(categoriaDelNegocio);
        
        mockMvc.perform(get("/api/categorias/dto/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre", is("Bebidas")))
            .andExpect(jsonPath("$.negocioId", is(1)))
            .andExpect(jsonPath("$.pertenece", is("INVENTARIO")));
        
        verify(userService, atLeastOnce()).findCurrentUser();
        verify(empleadoService, atLeastOnce()).getEmpleadoByUser(empleadoUser.getId());
        verify(categoriaService, atLeastOnce()).getById(1);
    }
    
    // POST - saveDto
    @Test
    @WithMockUser(username = "empleado", roles = {"empleado"})
    public void testSaveDto_AsEmpleado_Success() throws Exception {
        User empleadoUser = new User();
        empleadoUser.setId(3);
        Authorities empleadoAuth = new Authorities();
        empleadoAuth.setAuthority("empleado");
        empleadoUser.setAuthority(empleadoAuth);
        
        Negocio negocioEmpleado = new Negocio();
        negocioEmpleado.setId(1);
        
        Empleado empleado = new Empleado();
        empleado.setUser(empleadoUser);
        empleado.setNegocio(negocioEmpleado);
        
        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre("Nueva Categoría");
        dto.setNegocioId(1);
        dto.setPertenece(Pertenece.INVENTARIO);
        
        Categoria categoriaConvertida = new Categoria();
        categoriaConvertida.setName("Nueva Categoría");
        categoriaConvertida.setNegocio(negocioEmpleado);
        categoriaConvertida.setPertenece(Pertenece.INVENTARIO);
        
        Categoria categoriaSaved = new Categoria();
        categoriaSaved.setId(10);
        categoriaSaved.setName("Nueva Categoría");
        categoriaSaved.setNegocio(negocioEmpleado);
        categoriaSaved.setPertenece(Pertenece.INVENTARIO);
        
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoByUser(empleadoUser.getId())).thenReturn(empleado);
        when(categoriaService.convertirCategoria(any(CategoriaDTO.class))).thenReturn(categoriaConvertida);
        when(categoriaService.save(any(Categoria.class))).thenReturn(categoriaSaved);
        
        mockMvc.perform(post("/api/categorias/dto")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", is(10)))
            .andExpect(jsonPath("$.name", is("Nueva Categoría")));
        
        verify(userService, atLeastOnce()).findCurrentUser();
        verify(empleadoService, atLeastOnce()).getEmpleadoByUser(empleadoUser.getId());
        verify(categoriaService).convertirCategoria(any(CategoriaDTO.class));
        verify(categoriaService).save(any(Categoria.class));
    }
    
    // PUT - updateDto 
    @Test
    @WithMockUser(username = "empleado", roles = {"empleado"})
    public void testUpdateDto_AsEmpleado_Success() throws Exception {
        User empleadoUser = new User();
        empleadoUser.setId(3);
        Authorities empleadoAuth = new Authorities();
        empleadoAuth.setAuthority("empleado");
        empleadoUser.setAuthority(empleadoAuth);
        
        Negocio negocioEmpleado = new Negocio();
        negocioEmpleado.setId(1);
        
        Empleado empleado = new Empleado();
        empleado.setUser(empleadoUser);
        empleado.setNegocio(negocioEmpleado);
        
        Categoria existingCategoria = new Categoria();
        existingCategoria.setId(1);
        existingCategoria.setName("Bebidas");
        existingCategoria.setNegocio(negocioEmpleado);
        existingCategoria.setPertenece(Pertenece.INVENTARIO);
        
        CategoriaDTO dto = new CategoriaDTO();
        dto.setId(1);
        dto.setNombre("Bebidas Actualizadas");
        dto.setNegocioId(1);
        dto.setPertenece(Pertenece.INVENTARIO);
        
        Categoria categoriaConvertida = new Categoria();
        categoriaConvertida.setName("Bebidas Actualizadas");
        categoriaConvertida.setNegocio(negocioEmpleado);
        categoriaConvertida.setPertenece(Pertenece.INVENTARIO);
        
        Categoria categoriaActualizada = new Categoria();
        categoriaActualizada.setId(1);
        categoriaActualizada.setName("Bebidas Actualizadas");
        categoriaActualizada.setNegocio(negocioEmpleado);
        categoriaActualizada.setPertenece(Pertenece.INVENTARIO);
        
        when(userService.findCurrentUser()).thenReturn(empleadoUser);
        when(empleadoService.getEmpleadoByUser(empleadoUser.getId())).thenReturn(empleado);
        when(categoriaService.getById(1)).thenReturn(existingCategoria);
        when(categoriaService.convertirCategoria(any(CategoriaDTO.class))).thenReturn(categoriaConvertida);
        when(categoriaService.update(eq(1), any(Categoria.class))).thenReturn(categoriaActualizada);
        
        mockMvc.perform(put("/api/categorias/dto/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("Bebidas Actualizadas")));
        
        verify(userService, atLeastOnce()).findCurrentUser();
        verify(empleadoService, atLeastOnce()).getEmpleadoByUser(empleadoUser.getId());
        verify(categoriaService, atLeastOnce()).getById(1);
        verify(categoriaService).convertirCategoria(any(CategoriaDTO.class));
        verify(categoriaService).update(eq(1), any(Categoria.class));
    }

    @Test
    public void testSaveDto_NullDto_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/categorias/dto")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("")) // contenido vacío = null DTO
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testSaveDto_AsAdmin_ReturnsCreated() throws Exception {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre("TestCategoria");
        dto.setPertenece(Pertenece.INVENTARIO);
        dto.setNegocioId(1);

        when(categoriaService.convertirCategoria(any())).thenReturn(categoria);
        when(categoriaService.save(any())).thenReturn(categoria);
        adminUser.getAuthority().setAuthority("admin"); // ya está por defecto

        mockMvc.perform(post("/api/categorias/dto")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name", is("Bebidas")));
    }

    @Test
    public void testSaveDto_AsDueno_OwnsNegocio_ReturnsCreated() throws Exception {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre("CategoriaDueno");
        dto.setPertenece(Pertenece.INVENTARIO);
        dto.setNegocioId(1);

        Dueno dueno = new Dueno();
        dueno.setUser(adminUser);
        negocio.setDueno(dueno);

        Categoria categoriaConverted = new Categoria();
        categoriaConverted.setNegocio(negocio);
        categoriaConverted.setName("CategoriaDueno");

        adminUser.getAuthority().setAuthority("dueno");

        when(categoriaService.convertirCategoria(any())).thenReturn(categoriaConverted);
        when(categoriaService.save(any())).thenReturn(categoriaConverted);

        mockMvc.perform(post("/api/categorias/dto")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated());
    }


    @Test
    public void testSaveDto_Dueno_NotOwner_ReturnsForbidden() throws Exception {
        adminUser.getAuthority().setAuthority("dueno");

        Dueno otroDueno = new Dueno();
        User otroUser = new User();
        otroUser.setId(2); // distinto del adminUser
        otroDueno.setUser(otroUser);
        negocio.setDueno(otroDueno);

        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre("OtraCat");
        dto.setNegocioId(1);
        dto.setPertenece(Pertenece.VENTA);

        when(categoriaService.convertirCategoria(any())).thenReturn(categoria);

        mockMvc.perform(post("/api/categorias/dto")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isForbidden());
    }

    @Test
    public void testSaveDto_Empleado_CorrectNegocio_ReturnsCreated() throws Exception {
        adminUser.getAuthority().setAuthority("empleado");

        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre("EmpleadoCat");
        dto.setNegocioId(1);
        dto.setPertenece(Pertenece.VENTA);

        Empleado empleado = new Empleado();
        empleado.setNegocio(negocio);

        when(empleadoService.getEmpleadoByUser(1)).thenReturn(empleado);
        when(categoriaService.convertirCategoria(any())).thenReturn(categoria);
        when(categoriaService.save(any())).thenReturn(categoria);

        mockMvc.perform(post("/api/categorias/dto")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name", is("Bebidas")));
    }

    @Test
    public void testSaveDto_Empleado_WrongNegocio_ReturnsForbidden() throws Exception {
        adminUser.getAuthority().setAuthority("empleado");

        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre("EmpleadoCat");
        dto.setNegocioId(2);
        dto.setPertenece(Pertenece.INVENTARIO);

        Empleado empleado = new Empleado();
        Negocio otroNegocio = new Negocio();
        otroNegocio.setId(1);
        empleado.setNegocio(otroNegocio);

        when(empleadoService.getEmpleadoByUser(adminUser.getId())).thenReturn(empleado);
        when(categoriaService.convertirCategoria(any())).thenReturn(categoria);

        mockMvc.perform(post("/api/categorias/dto")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isForbidden());
    }

    @Test
    public void testDelete_Empleado_CorrectNegocio_ReturnsNoContent() throws Exception {
        adminUser.getAuthority().setAuthority("empleado");

        Categoria cat = new Categoria();
        cat.setId(1);
        cat.setNegocio(negocio);

        Empleado empleado = new Empleado();
        empleado.setNegocio(negocio);

        when(categoriaService.getById(1)).thenReturn(cat);
        when(empleadoService.getEmpleadoByUser(adminUser.getId())).thenReturn(empleado);

        mockMvc.perform(delete("/api/categorias/{id}", 1)
                .with(csrf()))
            .andExpect(status().isNoContent());

        verify(categoriaService).delete(1);
    }

    @Test
    public void testDelete_Empleado_WrongNegocio_ReturnsForbidden() throws Exception {
        adminUser.getAuthority().setAuthority("empleado");

        Negocio otroNegocio = new Negocio();
        otroNegocio.setId(2);
        Categoria cat = new Categoria();
        cat.setId(1);
        cat.setNegocio(otroNegocio);

        Empleado empleado = new Empleado();
        empleado.setNegocio(negocio); // negocio.id == 1

        when(categoriaService.getById(1)).thenReturn(cat);
        when(empleadoService.getEmpleadoByUser(adminUser.getId())).thenReturn(empleado);

        mockMvc.perform(delete("/api/categorias/{id}", 1)
                .with(csrf()))
            .andExpect(status().isForbidden());

        verify(categoriaService, never()).delete(anyInt());
    }

    @Test
    public void testUpdateDto_NullDto_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> categoriaController.updateDto(1, null));
    }

    @Test
    public void testUpdateDto_Dueno_NegocioMismatch_ReturnsForbidden() throws Exception {
        adminUser.getAuthority().setAuthority("dueno");
        Categoria existing = new Categoria();
        existing.setId(1);
        Negocio negocio1 = new Negocio();
        negocio1.setId(1);
        Dueno dueno = new Dueno();
        dueno.setUser(adminUser);
        negocio1.setDueno(dueno);
        existing.setNegocio(negocio1);
        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre("X");
        dto.setNegocioId(2);
        dto.setPertenece(Pertenece.INVENTARIO);
        when(categoriaService.getById(1)).thenReturn(existing);
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(categoriaService.convertirCategoria(any())).thenReturn(existing);
        mockMvc.perform(put("/api/categorias/dto/{id}", 1)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isForbidden());
        verify(categoriaService, never()).update(anyInt(), any());
    }

    @Test
    public void testUpdateDto_Empleado_NegocioMismatch_ReturnsForbidden() throws Exception {
        adminUser.getAuthority().setAuthority("empleado");
        Categoria existing = new Categoria();
        existing.setId(1);
        Negocio negocio1 = new Negocio();
        negocio1.setId(1);
        existing.setNegocio(negocio1);
        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre("Y");
        dto.setNegocioId(2);
        dto.setPertenece(Pertenece.VENTA);
        Empleado empleado = new Empleado();
        Negocio negocioEmpleado = new Negocio();
        negocioEmpleado.setId(1);
        empleado.setNegocio(negocioEmpleado);
        when(categoriaService.getById(1)).thenReturn(existing);
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(empleadoService.getEmpleadoByUser(adminUser.getId())).thenReturn(empleado);
        mockMvc.perform(put("/api/categorias/dto/{id}", 1)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isForbidden());
        verify(categoriaService, never()).update(anyInt(), any());
    }

    @Test
    public void testUpdate_NullCategoria_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
            categoriaController.update(1, null)
        );
    }

    @Test
    public void testUpdate_Admin_ReturnsOk() throws Exception {
        adminUser.getAuthority().setAuthority("admin");
        when(categoriaService.update(eq(1), any(Categoria.class))).thenReturn(categoria);

        mockMvc.perform(put("/api/categorias/{id}", 1)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoria)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("Bebidas")));

        verify(categoriaService).update(eq(1), any(Categoria.class));
    }

    @Test
    public void testUpdate_Dueno_NegocioMismatch_ReturnsForbidden() {
        adminUser.getAuthority().setAuthority("dueno");

        Categoria existing = new Categoria();
        existing.setId(1);
        Negocio negocio1 = new Negocio(); negocio1.setId(1);
        Dueno dueno1 = new Dueno(); dueno1.setUser(adminUser);
        negocio1.setDueno(dueno1);
        existing.setNegocio(negocio1);

        Categoria cuerpo = new Categoria();
        cuerpo.setId(1);
        Negocio negocio2 = new Negocio(); negocio2.setId(2);
        Dueno dueno2 = new Dueno();
        User mismoUser = new User(); mismoUser.setId(adminUser.getId());
        dueno2.setUser(mismoUser);
        negocio2.setDueno(dueno2);
        cuerpo.setNegocio(negocio2);

        when(categoriaService.getById(1)).thenReturn(existing);
        when(userService.findCurrentUser()).thenReturn(adminUser);

        ResponseEntity<Categoria> response = categoriaController.update(1, cuerpo);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(categoriaService, never()).update(anyInt(), any());
    }


    @Test
    public void testUpdate_Empleado_NegocioMismatch_ReturnsForbidden() throws Exception {
        adminUser.getAuthority().setAuthority("empleado");

        Categoria existing = new Categoria();
        existing.setId(1);
        Negocio negocio1 = new Negocio(); negocio1.setId(1);
        existing.setNegocio(negocio1);

        Categoria cuerpo = new Categoria();
        cuerpo.setId(1);
        cuerpo.setName("Test");
        cuerpo.setPertenece(Pertenece.INVENTARIO);
        Negocio negocio2 = new Negocio(); negocio2.setId(2);
        cuerpo.setNegocio(negocio2);

        Empleado empleado = new Empleado();
        Negocio negocioEmpleado = new Negocio(); negocioEmpleado.setId(1);
        empleado.setNegocio(negocioEmpleado);

        when(categoriaService.getById(1)).thenReturn(existing);
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(empleadoService.getEmpleadoByUser(adminUser.getId())).thenReturn(empleado);

        mockMvc.perform(put("/api/categorias/{id}", 1)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cuerpo)))
            .andExpect(status().isForbidden());

        verify(categoriaService, never()).update(anyInt(), any());
    }

    @Test
    public void testUpdate_Empleado_CorrectNegocio_ReturnsOk() throws Exception {
        adminUser.getAuthority().setAuthority("empleado");

        Categoria existing = new Categoria();
        existing.setId(1);
        Negocio negocio1 = new Negocio(); negocio1.setId(1);
        existing.setNegocio(negocio1);

        Categoria cuerpo = new Categoria();
        cuerpo.setId(1);
        cuerpo.setName("Test");
        cuerpo.setPertenece(Pertenece.VENTA);
        Negocio negocioMatch = new Negocio(); negocioMatch.setId(1);
        cuerpo.setNegocio(negocioMatch);

        Empleado empleado = new Empleado();
        Negocio negocioEmpleado = new Negocio(); negocioEmpleado.setId(1);
        empleado.setNegocio(negocioEmpleado);

        when(categoriaService.getById(1)).thenReturn(existing);
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(empleadoService.getEmpleadoByUser(adminUser.getId())).thenReturn(empleado);
        when(categoriaService.update(eq(1), any(Categoria.class))).thenReturn(cuerpo);

        mockMvc.perform(put("/api/categorias/{id}", 1)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cuerpo)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.negocio.id", is(1)));

        verify(categoriaService).update(eq(1), any(Categoria.class));
    }

    @Test
    public void testFindByNegocioIdVentaDto_Dueno_NoContent_ReturnsNoContent() throws Exception {
        adminUser.getAuthority().setAuthority("dueno");
        Dueno dueno = new Dueno();
        dueno.setUser(adminUser);
        negocio.setDueno(dueno);

        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(negocioService.getById(1)).thenReturn(negocio);
        when(categoriaService.getCategoriasVentaByNegocioId(1)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/categorias/dto/negocio/{negocioId}/venta", 1)
                .with(csrf()))
            .andExpect(status().isNoContent());
    }


    @Test
    public void testFindByNegocioIdVentaDto_Dueno_WithResults_ReturnsOk() throws Exception {
        adminUser.getAuthority().setAuthority("dueno");
        Dueno dueno = new Dueno(); dueno.setUser(adminUser);
        negocio.setDueno(dueno);
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(negocioService.getById(1)).thenReturn(negocio);
        when(categoriaService.getCategoriasVentaByNegocioId(1)).thenReturn(List.of(categoria));

        mockMvc.perform(get("/api/categorias/dto/negocio/{negocioId}/venta", 1)
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].nombre", is("Bebidas")));
    }

    @Test
    public void testFindByNegocioIdVentaDto_Dueno_NotOwner_ReturnsForbidden() throws Exception {
        adminUser.getAuthority().setAuthority("dueno");
        Dueno otroDueno = new Dueno();
        User user2 = new User(); user2.setId(2);
        otroDueno.setUser(user2);
        negocio.setDueno(otroDueno);
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(negocioService.getById(1)).thenReturn(negocio);

        mockMvc.perform(get("/api/categorias/dto/negocio/{negocioId}/venta", 1)
                .with(csrf()))
            .andExpect(status().isForbidden());
    }

    @Test
    public void testFindByNegocioIdVentaDto_Empleado_NoContent_ReturnsNoContent() throws Exception {
        adminUser.getAuthority().setAuthority("empleado");
        Empleado empleado = new Empleado(); empleado.setNegocio(negocio);
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(empleadoService.getEmpleadoByUser(adminUser.getId())).thenReturn(empleado);
        when(categoriaService.getCategoriasVentaByNegocioId(1)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/categorias/dto/negocio/{negocioId}/venta", 1)
                .with(csrf()))
            .andExpect(status().isNoContent());
    }

    @Test
    public void testFindByNegocioIdVentaDto_Empleado_WithResults_ReturnsOk() throws Exception {
        adminUser.getAuthority().setAuthority("empleado");
        Empleado empleado = new Empleado();
        empleado.setNegocio(negocio);
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(empleadoService.getEmpleadoByUser(adminUser.getId())).thenReturn(empleado);
        when(categoriaService.getCategoriasVentaByNegocioId(1)).thenReturn(List.of(categoria));

        mockMvc.perform(get("/api/categorias/dto/negocio/{negocioId}/venta", 1)
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].nombre", is("Bebidas")));
    }


    @Test
    public void testFindByNegocioIdVentaDto_Empleado_NotMatch_ReturnsForbidden() throws Exception {
        adminUser.getAuthority().setAuthority("empleado");
        Negocio otroNegocio = new Negocio(); otroNegocio.setId(2);
        Empleado empleado = new Empleado(); empleado.setNegocio(otroNegocio);
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(empleadoService.getEmpleadoByUser(adminUser.getId())).thenReturn(empleado);

        mockMvc.perform(get("/api/categorias/dto/negocio/{negocioId}/venta", 1)
                .with(csrf()))
            .andExpect(status().isForbidden());
    }

    @Test
    public void testFindByNegocioIdInventarioDto_Dueno_NoContent_ReturnsNoContent() throws Exception {
        adminUser.getAuthority().setAuthority("dueno");
        Dueno dueno = new Dueno(); dueno.setUser(adminUser);
        negocio.setDueno(dueno);
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(negocioService.getById(1)).thenReturn(negocio);
        when(categoriaService.getCategoriasInventarioByNegocioId(1)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/categorias/dto/negocio/{negocioId}/inventario", 1).with(csrf()))
            .andExpect(status().isNoContent());
    }

    @Test
    public void testFindByNegocioIdInventarioDto_Dueno_WithResults_ReturnsOk() throws Exception {
        adminUser.getAuthority().setAuthority("dueno");
        Dueno dueno = new Dueno(); dueno.setUser(adminUser);
        negocio.setDueno(dueno);
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(negocioService.getById(1)).thenReturn(negocio);
        when(categoriaService.getCategoriasInventarioByNegocioId(1)).thenReturn(List.of(categoria));

        mockMvc.perform(get("/api/categorias/dto/negocio/{negocioId}/inventario", 1).with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].nombre", is("Bebidas")));
    }

    @Test
    public void testFindByNegocioIdInventarioDto_Dueno_NotOwner_ReturnsForbidden() throws Exception {
        adminUser.getAuthority().setAuthority("dueno");
        Dueno otro = new Dueno(); otro.setUser(new User() {{ setId(2); }});
        negocio.setDueno(otro);
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(negocioService.getById(1)).thenReturn(negocio);

        mockMvc.perform(get("/api/categorias/dto/negocio/{negocioId}/inventario", 1).with(csrf()))
            .andExpect(status().isForbidden());
    }

    @Test
    public void testFindByNegocioIdInventarioDto_Empleado_NoContent_ReturnsNoContent() throws Exception {
        adminUser.getAuthority().setAuthority("empleado");
        Empleado empleado = new Empleado(); empleado.setNegocio(negocio);
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(empleadoService.getEmpleadoByUser(adminUser.getId())).thenReturn(empleado);
        when(categoriaService.getCategoriasInventarioByNegocioId(1)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/categorias/dto/negocio/{negocioId}/inventario", 1).with(csrf()))
            .andExpect(status().isNoContent());
    }

    @Test
    public void testFindByNegocioIdInventarioDto_Empleado_WithResults_ReturnsOk() throws Exception {
        adminUser.getAuthority().setAuthority("empleado");
        Empleado empleado = new Empleado(); empleado.setNegocio(negocio);
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(empleadoService.getEmpleadoByUser(adminUser.getId())).thenReturn(empleado);
        when(categoriaService.getCategoriasInventarioByNegocioId(1)).thenReturn(List.of(categoria));

        mockMvc.perform(get("/api/categorias/dto/negocio/{negocioId}/inventario", 1).with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].nombre", is("Bebidas")));
    }

    @Test
    public void testFindByNegocioIdInventarioDto_Empleado_NotMatch_ReturnsForbidden() throws Exception {
        adminUser.getAuthority().setAuthority("empleado");
        Empleado empleado = new Empleado(); empleado.setNegocio(new Negocio() {{ setId(2); }});
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(empleadoService.getEmpleadoByUser(adminUser.getId())).thenReturn(empleado);

        mockMvc.perform(get("/api/categorias/dto/negocio/{negocioId}/inventario", 1).with(csrf()))
            .andExpect(status().isForbidden());
    }
    
    @Test
    public void testFindByNegocioIdDto_Dueno_NoContent_ReturnsNoContent() throws Exception {
        adminUser.getAuthority().setAuthority("dueno");
        Dueno dueno = new Dueno(); dueno.setUser(adminUser);
        negocio.setDueno(dueno);
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(negocioService.getById(1)).thenReturn(negocio);
        when(categoriaService.getCategoriasByNegocioId(1)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/categorias/dto/negocio/{negocioId}", 1).with(csrf()))
            .andExpect(status().isNoContent());
    }

    @Test
    public void testFindByNegocioIdDto_Dueno_WithResults_ReturnsOk() throws Exception {
        adminUser.getAuthority().setAuthority("dueno");
        Dueno dueno = new Dueno(); dueno.setUser(adminUser);
        negocio.setDueno(dueno);
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(negocioService.getById(1)).thenReturn(negocio);
        when(categoriaService.getCategoriasByNegocioId(1)).thenReturn(List.of(categoria));

        mockMvc.perform(get("/api/categorias/dto/negocio/{negocioId}", 1).with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].nombre", is("Bebidas")));
    }

    @Test
    public void testFindByNegocioIdDto_Dueno_NotOwner_ReturnsForbidden() throws Exception {
        adminUser.getAuthority().setAuthority("dueno");
        Dueno otro = new Dueno(); otro.setUser(new User() {{ setId(2); }});
        negocio.setDueno(otro);
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(negocioService.getById(1)).thenReturn(negocio);

        mockMvc.perform(get("/api/categorias/dto/negocio/{negocioId}", 1).with(csrf()))
            .andExpect(status().isForbidden());
    }

    @Test
    public void testFindByNegocioIdDto_Empleado_NoContent_ReturnsNoContent() throws Exception {
        adminUser.getAuthority().setAuthority("empleado");
        Empleado empleado = new Empleado(); empleado.setNegocio(negocio);
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(empleadoService.getEmpleadoByUser(adminUser.getId())).thenReturn(empleado);
        when(categoriaService.getCategoriasByNegocioId(1)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/categorias/dto/negocio/{negocioId}", 1).with(csrf()))
            .andExpect(status().isNoContent());
    }

    @Test
    public void testFindByNegocioIdDto_Empleado_WithResults_ReturnsOk() throws Exception {
        adminUser.getAuthority().setAuthority("empleado");
        Empleado empleado = new Empleado(); empleado.setNegocio(negocio);
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(empleadoService.getEmpleadoByUser(adminUser.getId())).thenReturn(empleado);
        when(categoriaService.getCategoriasByNegocioId(1)).thenReturn(List.of(categoria));

        mockMvc.perform(get("/api/categorias/dto/negocio/{negocioId}", 1).with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].nombre", is("Bebidas")));
    }

    @Test
    public void testFindByNegocioIdDto_Empleado_NotMatch_ReturnsForbidden() throws Exception {
        adminUser.getAuthority().setAuthority("empleado");
        Empleado empleado = new Empleado(); empleado.setNegocio(new Negocio() {{ setId(2); }});
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(empleadoService.getEmpleadoByUser(adminUser.getId())).thenReturn(empleado);

        mockMvc.perform(get("/api/categorias/dto/negocio/{negocioId}", 1).with(csrf()))
            .andExpect(status().isForbidden());
    }



}
