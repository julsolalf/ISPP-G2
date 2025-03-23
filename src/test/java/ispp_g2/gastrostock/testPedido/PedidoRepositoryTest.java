package ispp_g2.gastrostock.testPedido;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import ispp_g2.gastrostock.dueño.Dueño;
import ispp_g2.gastrostock.dueño.DueñoRepository;
import ispp_g2.gastrostock.empleado.Empleado;
import ispp_g2.gastrostock.empleado.EmpleadoRepository;
import ispp_g2.gastrostock.mesa.Mesa;
import ispp_g2.gastrostock.mesa.MesaRepository;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioRepository;
import ispp_g2.gastrostock.pedido.Pedido;
import ispp_g2.gastrostock.pedido.PedidoRepository;

@DataJpaTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
class PedidoRepositoryTest {

    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private MesaRepository mesaRepository;
    
    @Autowired
    private EmpleadoRepository empleadoRepository;
    
    @Autowired
    private NegocioRepository negocioRepository;
    
    @Autowired
    private DueñoRepository dueñoRepository;
    
    private Pedido pedido1, pedido2;
    private Mesa mesa1, mesa2;
    private Empleado empleado;
    private Negocio negocio;
    private Dueño dueño;
    private LocalDateTime fecha1, fecha2;
    
    @BeforeEach
    void setUp() {
        // Clean repositories
        pedidoRepository.deleteAll();
        mesaRepository.deleteAll();
        empleadoRepository.deleteAll();
        negocioRepository.deleteAll();
        dueñoRepository.deleteAll();
        
        // Create Dueño
        dueño = new Dueño();
        dueño.setFirstName("Juan");
        dueño.setLastName("García");
        dueño.setEmail("juan@example.com");
        dueño.setNumTelefono("652345678");
        dueño.setTokenDueño("TOKEN123");
        dueño = dueñoRepository.save(dueño);
        
        // Create Negocio
        negocio = new Negocio();
        negocio.setName("Restaurante La Tasca");
        negocio.setDireccion("Calle Principal 123");
        negocio.setCiudad("Sevilla");
        negocio.setPais("España");
        negocio.setCodigoPostal("41001");
        negocio.setTokenNegocio(12345);
        negocio.setDueño(dueño);
        negocio = negocioRepository.save(negocio);
        
        // Create Mesas
        mesa1 = new Mesa();
        mesa1.setName("Mesa 1");
        mesa1.setNumeroAsientos(4);
        mesa1.setNegocio(negocio);
        mesa1 = mesaRepository.save(mesa1);
        
        mesa2 = new Mesa();
        mesa2.setName("Mesa 2");
        mesa2.setNumeroAsientos(2);
        mesa2.setNegocio(negocio);
        mesa2 = mesaRepository.save(mesa2);
        
        // Create Empleado
        empleado = new Empleado();
        empleado.setTokenEmpleado("testToken");
        empleado.setNegocio(negocio);
        empleado.setFirstName("Antinio");
        empleado.setLastName("García");
        empleado.setEmail("antoninio@test.com");
        empleado.setNumTelefono("666111222");
        empleadoRepository.save(empleado);
        
        // Set up dates
        fecha1 = LocalDateTime.now().minusHours(1);
        fecha2 = LocalDateTime.now();
        
        // Create Pedidos
        pedido1 = new Pedido();
        pedido1.setFecha(fecha1);
        pedido1.setPrecioTotal(50.75);
        pedido1.setMesa(mesa1);
        pedido1.setEmpleado(empleado);
        pedido1.setNegocio(negocio);
        pedido1 = pedidoRepository.save(pedido1);
        
        pedido2 = new Pedido();
        pedido2.setFecha(fecha2);
        pedido2.setPrecioTotal(75.50);
        pedido2.setMesa(mesa2);
        pedido2.setEmpleado(empleado);
        pedido2.setNegocio(negocio);
        pedido2 = pedidoRepository.save(pedido2);
    }
    

    
    @Test
    void testSave() {
        // Create a new Pedido
        Pedido newPedido = new Pedido();
        newPedido.setFecha(LocalDateTime.now().plusHours(1));
        newPedido.setPrecioTotal(100.00);
        newPedido.setMesa(mesa1);
        newPedido.setEmpleado(empleado);
        newPedido.setNegocio(negocio);
        
        // Save it to repository
        newPedido = pedidoRepository.save(newPedido);
        
        // Verify it was saved
        assertNotNull(newPedido.getId());
        
        // Verify it can be retrieved
        Optional<Pedido> found = pedidoRepository.findById(newPedido.getId().toString());
        assertTrue(found.isPresent());
        assertEquals(100.00, found.get().getPrecioTotal());
    }
    
    @Test
    void testFindById() {
        Optional<Pedido> found = pedidoRepository.findById(pedido1.getId().toString());
        
        // Verify
        assertTrue(found.isPresent());
        assertEquals(50.75, found.get().getPrecioTotal());
    }
    
    @Test
    void testFindById_NotFound() {
        Optional<Pedido> found = pedidoRepository.findById("999999");
        
        // Verify
        assertFalse(found.isPresent());
    }
    
    @Test
    void testFindAll() {
        Iterable<Pedido> pedidos = pedidoRepository.findAll();
        
        // Convert to list and verify
        List<Pedido> pedidoList = (List<Pedido>) pedidos;
        assertEquals(2, pedidoList.size());
    }
    
    @Test
    void testDelete() {
        assertTrue(pedidoRepository.findById(pedido1.getId().toString()).isPresent());
        
        // Delete pedido
        pedidoRepository.delete(pedido1);
        
        // Verify it was deleted
        assertFalse(pedidoRepository.findById(pedido1.getId().toString()).isPresent());
    }
    
    @Test
    void testDeleteById() {
        // Verify pedido exists
        assertTrue(pedidoRepository.findById(pedido1.getId().toString()).isPresent());
        
        // Delete pedido by ID
        pedidoRepository.deleteById(pedido1.getId().toString());
        
        // Verify it was deleted
        assertFalse(pedidoRepository.findById(pedido1.getId().toString()).isPresent());
    }
    
    // Test custom queries
    
    /* 
    @Test
    void testFindPedidoByFecha() {
        List<Pedido> pedidos = pedidoRepository.findPedidoByFecha(fecha1);
        
        // Verify
        assertEquals(1, pedidos.size());
        assertEquals(50.75, pedidos.get(0).getPrecioTotal());
    }
    */

    @Test
    void testFindPedidoByFecha_NotFound() {
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
        List<Pedido> pedidos = pedidoRepository.findPedidoByFecha(futureDate);
        
        // Verify
        assertTrue(pedidos.isEmpty());
    }
    
    @Test
    void testFindPedidoByPrecioTotal() {
        List<Pedido> pedidos = pedidoRepository.findPedidoByPrecioTotal(75.50);
        
        // Verify
        assertEquals(1, pedidos.size());
        assertEquals(mesa2.getId(), pedidos.get(0).getMesa().getId());
    }
    
    @Test
    void testFindPedidoByPrecioTotal_NotFound() {
        List<Pedido> pedidos = pedidoRepository.findPedidoByPrecioTotal(999.99);
        
        // Verify
        assertTrue(pedidos.isEmpty());
    }
    
    @Test
    void testFindPedidoByMesaId() {
        List<Pedido> pedidos = pedidoRepository.findPedidoByMesaId(mesa1.getId());
        
        // Verify
        assertEquals(1, pedidos.size());
        assertEquals(50.75, pedidos.get(0).getPrecioTotal());
    }
    
    @Test
    void testFindPedidoByMesaId_NotFound() {
        List<Pedido> pedidos = pedidoRepository.findPedidoByMesaId(999);
        
        // Verify
        assertTrue(pedidos.isEmpty());
    }
    
    @Test
    void testFindPedidoByEmpleadoId() {
        List<Pedido> pedidos = pedidoRepository.findPedidoByEmpleadoId(empleado.getId());
        
        // Verify - both pedidos are assigned to the same empleado
        assertEquals(2, pedidos.size());
    }
    
    @Test
    void testFindPedidoByEmpleadoId_NotFound() {
        List<Pedido> pedidos = pedidoRepository.findPedidoByEmpleadoId(999);
        
        // Verify
        assertTrue(pedidos.isEmpty());
    }
    
    @Test
    void testFindPedidoByNegocioId() {
        List<Pedido> pedidos = pedidoRepository.findPedidoByNegocioId(negocio.getId());
        
        // Verify - both pedidos are for the same negocio
        assertEquals(2, pedidos.size());
    }
    
    @Test
    void testFindPedidoByNegocioId_NotFound() {
        List<Pedido> pedidos = pedidoRepository.findPedidoByNegocioId(999);
        
        // Verify
        assertTrue(pedidos.isEmpty());
    }
    


    
    @Test
    void testFindPedidoByPrecioTotal_AlmostZeroPrice() {
        Pedido zeroPricePedido = new Pedido();
        zeroPricePedido.setFecha(LocalDateTime.now());
        zeroPricePedido.setPrecioTotal(0.1);
        zeroPricePedido.setMesa(mesa1);
        zeroPricePedido.setEmpleado(empleado);
        zeroPricePedido.setNegocio(negocio);
        pedidoRepository.save(zeroPricePedido);
        
        // Find with zero price
        List<Pedido> pedidos = pedidoRepository.findPedidoByPrecioTotal(0.1);
        
        // Verify
        assertEquals(1, pedidos.size());
        assertEquals(0.1, pedidos.get(0).getPrecioTotal());
    }
    
    @Test
    void testFindPedidoByPrecioTotal_HighPrecisionDecimals() {
        // Create a pedido with high precision decimal price
        double highPrecisionPrice = 123.456789;
        
        Pedido highPrecisionPedido = new Pedido();
        highPrecisionPedido.setFecha(LocalDateTime.now());
        highPrecisionPedido.setPrecioTotal(highPrecisionPrice);
        highPrecisionPedido.setMesa(mesa1);
        highPrecisionPedido.setEmpleado(empleado);
        highPrecisionPedido.setNegocio(negocio);
        pedidoRepository.save(highPrecisionPedido);
        
        // Find with exact same price
        List<Pedido> pedidos = pedidoRepository.findPedidoByPrecioTotal(highPrecisionPrice);
        
        // Verify
        assertEquals(1, pedidos.size());
        assertEquals(highPrecisionPrice, pedidos.get(0).getPrecioTotal());
    }
}