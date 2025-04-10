package ispp_g2.gastrostock.testMesa;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import ispp_g2.gastrostock.dueno.Dueno;
import ispp_g2.gastrostock.dueno.DuenoRepository;
import ispp_g2.gastrostock.empleado.Empleado;
import ispp_g2.gastrostock.empleado.EmpleadoRepository;
import ispp_g2.gastrostock.mesa.Mesa;
import ispp_g2.gastrostock.mesa.MesaRepository;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioRepository;
import ispp_g2.gastrostock.pedido.Pedido;
import ispp_g2.gastrostock.pedido.PedidoRepository;
import ispp_g2.gastrostock.user.Authorities;
import ispp_g2.gastrostock.user.AuthoritiesRepository;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class MesaRepositoryTest {

    @Autowired
    private MesaRepository mesaRepository;
    
    @Autowired
    private DuenoRepository duenoRepository;
    
    @Autowired
    private NegocioRepository negocioRepository;
    
    @Autowired
    private EmpleadoRepository empleadoRepository;
    
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthoritiesRepository authoritiesRepository;
    
    private Dueno dueno;
    private Negocio negocio;
    private Mesa mesa1, mesa2, mesa3;
    private Empleado empleado;
    
    @BeforeEach
    void setUp() {
        // Limpiar datos anteriores
        pedidoRepository.deleteAll();
        mesaRepository.deleteAll();
        empleadoRepository.deleteAll();
        negocioRepository.deleteAll();
        duenoRepository.deleteAll();
        
        Authorities authority = new Authorities();
        authority.setAuthority("DUENO");
        authority = authoritiesRepository.save(authority);

        // Crear usuario
        User user = new User();
        user.setUsername("juangarcia");
        user.setPassword("password123");
        user.setAuthority(authority);
        user = userRepository.save(user);
        // Crear un objeto Dueno
        dueno = new Dueno();
        dueno.setFirstName("Juan");
        dueno.setLastName("Dueno");
        dueno.setEmail("juan@gastrostock.com");
        dueno.setNumTelefono("689594895");
        dueno.setTokenDueno("TOKEN123");
        dueno.setUser(user);
        dueno = duenoRepository.save(dueno);

        // Crear un objeto Negocio
        negocio = new Negocio();
        negocio.setName("Restaurante La Tasca");
        negocio.setDireccion("Calle Principal 123");
        negocio.setCiudad("Sevilla");
        negocio.setPais("Espana");
        negocio.setCodigoPostal("41001");
        negocio.setTokenNegocio(12345);
        negocio.setDueno(dueno);
        negocio = negocioRepository.save(negocio);

        // Crear un empleado
        empleado = new Empleado();
        empleado.setTokenEmpleado("testToken");
        empleado.setNegocio(negocio);
        empleado.setFirstName("Antonio");
        empleado.setLastName("García");
        empleado.setEmail("antonio@test.com");
        empleado.setNumTelefono("666111222");
        empleado.setUser(user);
        empleado = empleadoRepository.save(empleado);

        // Crear objetos Mesa para pruebas
        mesa1 = new Mesa();
        mesa1.setName("Mesa Exterior");
        mesa1.setNumeroAsientos(4);
        mesa1.setNegocio(negocio);
        mesa1 = mesaRepository.save(mesa1);

        mesa2 = new Mesa();
        mesa2.setName("Mesa VIP");
        mesa2.setNumeroAsientos(6);
        mesa2.setNegocio(negocio);
        mesa2 = mesaRepository.save(mesa2);

        mesa3 = new Mesa();
        mesa3.setName("Mesa Barra");
        mesa3.setNumeroAsientos(2);
        mesa3.setNegocio(negocio);
        mesa3 = mesaRepository.save(mesa3);
    }

    // Tests para operaciones CRUD estándar

    @Test
    void testSave() {
        // Crear una nueva mesa
        Mesa nuevaMesa = new Mesa();
        nuevaMesa.setName("Mesa Nueva");
        nuevaMesa.setNumeroAsientos(8);
        nuevaMesa.setNegocio(negocio);
        
        // Guardar la mesa
        Mesa saved = mesaRepository.save(nuevaMesa);
        
        // Verificar que se guardó correctamente
        assertNotNull(saved.getId());
        assertEquals("Mesa Nueva", saved.getName());
        assertEquals(8, saved.getNumeroAsientos());
        
        // Verificar que se puede recuperar de la base de datos
        Optional<Mesa> retrieved = mesaRepository.findById(saved.getId());
        assertTrue(retrieved.isPresent());
        assertEquals("Mesa Nueva", retrieved.get().getName());
    }
    
    @Test
    void testSaveWithoutNegocio() {
        // Crear una mesa sin negocio (debería fallar)
        Mesa mesaSinNegocio = new Mesa();
        mesaSinNegocio.setName("Mesa Sin Negocio");
        mesaSinNegocio.setNumeroAsientos(2);
        
        // Esta operación debería fallar con una excepción
        assertThrows(DataIntegrityViolationException.class, () -> {
            mesaRepository.save(mesaSinNegocio);
            mesaRepository.findAll(); // Forzar flush
        });
    }
    
    @Test
    void testFindById() {
        // Buscar una mesa existente por ID
        Optional<Mesa> found = mesaRepository.findById(mesa1.getId());
        
        // Verificar que existe y tiene los datos correctos
        assertTrue(found.isPresent());
        assertEquals("Mesa Exterior", found.get().getName());
        assertEquals(4, found.get().getNumeroAsientos());
    }
    
    @Test
    void testFindById_NotFound() {
        // Buscar una mesa que no existe
        Optional<Mesa> notFound = mesaRepository.findById(999);
        
        // Verificar que no existe
        assertFalse(notFound.isPresent());
    }
    
    @Test
    void testFindAll() {
        // Obtener todas las mesas
        List<Mesa> mesas = (List<Mesa>) mesaRepository.findAll();
        
        // Verificar que hay 3 mesas (las creadas en setUp)
        assertEquals(3, mesas.size());
        
        // Verificar que las mesas tienen los nombres correctos
        boolean foundExterior = false;
        boolean foundVIP = false;
        boolean foundBarra = false;
        
        for (Mesa mesa : mesas) {
            if ("Mesa Exterior".equals(mesa.getName())) foundExterior = true;
            if ("Mesa VIP".equals(mesa.getName())) foundVIP = true;
            if ("Mesa Barra".equals(mesa.getName())) foundBarra = true;
        }
        
        assertTrue(foundExterior);
        assertTrue(foundVIP);
        assertTrue(foundBarra);
    }
    
    @Test
    void testDelete() {
        // Eliminar una mesa
        mesaRepository.delete(mesa2);
        
        // Verificar que se eliminó
        Optional<Mesa> shouldBeDeleted = mesaRepository.findById(mesa2.getId());
        assertFalse(shouldBeDeleted.isPresent());
        
        // Verificar que el resto sigue existiendo
        assertEquals(2, ((List<Mesa>) mesaRepository.findAll()).size());
    }
    
    @Test
    void testDeleteById() {
        // Eliminar una mesa por ID
        mesaRepository.deleteById(mesa3.getId());
        
        // Verificar que se eliminó
        Optional<Mesa> shouldBeDeleted = mesaRepository.findById(mesa3.getId());
        assertFalse(shouldBeDeleted.isPresent());
        
        // Verificar que el resto sigue existiendo
        assertEquals(2, ((List<Mesa>) mesaRepository.findAll()).size());
    }
    
    // Tests para métodos de consulta personalizados
    
    @Test
    void testFindMesaByName_Success() {
        // Buscar mesa por nombre exacto
        Mesa found = mesaRepository.findMesaByName("Exterior");
        
        // Verificar que se encontró y tiene los datos correctos
        assertNotNull(found);
        assertEquals(mesa1.getId(), found.getId());
        assertEquals("Mesa Exterior", found.getName());
    }
    
    @Test
    void testFindMesaByName_PartialMatch() {
        // Buscar mesa por nombre parcial
        Mesa found = mesaRepository.findMesaByName("VIP");
        
        // Verificar que se encontró y tiene los datos correctos
        assertNotNull(found);
        assertEquals(mesa2.getId(), found.getId());
        assertEquals("Mesa VIP", found.getName());
    }
    
    @Test
    void testFindMesaByName_NotFound() {
        // Buscar mesa por nombre que no existe
        Mesa notFound = mesaRepository.findMesaByName("Inexistente");
        
        // Verificar que no se encontró
        assertNull(notFound);
    }
    
    @Test
    void testFindMesasByNegocio_Success() {
        // Buscar mesas por ID de negocio
        List<Mesa> found = mesaRepository.findMesasByNegocio(negocio.getId());
        
        // Verificar que se encontraron todas las mesas del negocio
        assertEquals(3, found.size());
    }
    
    @Test
    void testFindMesasByNegocio_NotFound() {
        // Buscar mesas por ID de negocio que no existe
        List<Mesa> notFound = mesaRepository.findMesasByNegocio(999);
        
        // Verificar que la lista está vacía
        assertTrue(notFound.isEmpty());
    }
    
    @Test
    void testFindMesaByNumeroAsientos_Success() {
        // Buscar mesas por número de asientos
        List<Mesa> found = mesaRepository.findMesaByNumeroAsientos(4);
        
        // Verificar que se encontró la mesa con 4 asientos
        assertEquals(1, found.size());
        assertEquals(mesa1.getId(), found.get(0).getId());
    }
    
    @Test
    void testFindMesaByNumeroAsientos_Multiple() {
        // Crear otra mesa con el mismo número de asientos
        Mesa otraMesa = new Mesa();
        otraMesa.setName("Otra Mesa");
        otraMesa.setNumeroAsientos(4); // Mismo número que mesa1
        otraMesa.setNegocio(negocio);
        mesaRepository.save(otraMesa);
        
        // Buscar mesas por número de asientos
        List<Mesa> found = mesaRepository.findMesaByNumeroAsientos(4);
        
        // Verificar que se encontraron ambas mesas
        assertEquals(2, found.size());
    }
    
    @Test
    void testFindMesaByNumeroAsientos_NotFound() {
        // Buscar mesas por número de asientos que no existe
        List<Mesa> notFound = mesaRepository.findMesaByNumeroAsientos(10);
        
        // Verificar que la lista está vacía
        assertTrue(notFound.isEmpty());
    }
    
    // Tests para relaciones entre entidades
    /* 
    @Test
    void testRelationshipWithPedido() {
        // Crear un pedido asociado a mesa1
        Pedido pedido = new Pedido();
        pedido.setFecha(LocalDateTime.now());
        pedido.setPrecioTotal(45.50);
        pedido.setMesa(mesa1);
        pedido.setEmpleado(empleado);
        pedido.setNegocio(negocio);
        pedidoRepository.save(pedido);
        
        // Verificar que la relación se estableció correctamente
        assertEquals(mesa1.getId(), pedido.getMesa().getId());
        
        // Intentar eliminar la mesa con pedido asociado
        try {
            mesaRepository.delete(mesa1);
            mesaRepository.findAll(); // Forzar flush
            
            // Si llega aquí, la mesa se eliminó (no hay restricción de eliminación en cascada)
            Optional<Mesa> shouldBeDeleted = mesaRepository.findById(Integer.toString(mesa1.getId()));
            assertFalse(shouldBeDeleted.isPresent());
            
            // Verificar qué pasó con el pedido
            Pedido pedidoAfterDelete = pedidoRepository.findById(Integer.toString(pedido.getId())).orElse(null);
            if (pedidoAfterDelete != null) {
                // Si el pedido sigue existiendo, su campo mesa debería ser nulo
                assertNull(pedidoAfterDelete.getMesa());
            }
        } catch (Exception e) {
            // Si hay una restricción, esto es válido también (depende de la configuración de JPA)
            System.out.println("No se pudo eliminar la mesa con pedido asociado: " + e.getMessage());
        }
    }
        */
}