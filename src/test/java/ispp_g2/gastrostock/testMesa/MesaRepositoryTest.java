package ispp_g2.gastrostock.testMesa;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import ispp_g2.gastrostock.dueño.Dueño;
import ispp_g2.gastrostock.dueño.DueñoRepository;
import ispp_g2.gastrostock.empleado.EmpleadoRepository;
import ispp_g2.gastrostock.negocio.NegocioRepository;
import ispp_g2.gastrostock.pedido.PedidoRepository;
import ispp_g2.gastrostock.empleado.Empleado;
import ispp_g2.gastrostock.empleado.Rol;
import ispp_g2.gastrostock.mesa.Mesa;
import ispp_g2.gastrostock.mesa.MesaRepository;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.pedido.Pedido;

@DataJpaTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
class MesaRepositoryTest {

    @Autowired
    private MesaRepository mesaRepository;
    
    @Autowired
    private DueñoRepository dueñoRepository;
    
    @Autowired
    private NegocioRepository negocioRepository;
    
    @Autowired
    private EmpleadoRepository empleadoRepository;
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    // Objetos reutilizables entre tests
    private Dueño dueño;
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
        dueñoRepository.deleteAll();
        
        // Crear un objeto Dueño
        dueño = new Dueño();
        dueño.setFirstName("Juan");
        dueño.setLastName("Dueño");
        dueño.setEmail("juan@gastrostock.com");
        dueño.setNumTelefono("689594895");
        dueño.setTokenDueño("TOKEN123");
        dueño = dueñoRepository.save(dueño);

        // Crear un objeto Negocio
        negocio = new Negocio();
        negocio.setId(400);
        negocio.setName("Restaurante La Tasca");
        negocio.setDireccion("Calle Principal 123");
        negocio.setCiudad("Sevilla");
        negocio.setPais("España");
        negocio.setCodigoPostal("41001");
        negocio.setTokenNegocio(12345);
        negocio.setDueño(dueño);
        negocio = negocioRepository.save(negocio);

        // Crear un empleado
        empleado = new Empleado();
        empleado.setName("Carlos Camarero");
        empleado.setTokenEmpleado("EMP-TOKEN-123");
        empleado.setNegocio(negocio);
        empleado = empleadoRepository.save(empleado);

        // Crear objetos Mesa para pruebas
        mesa1 = new Mesa();
        mesa1.setId(101);
        mesa1.setName("Mesa Exterior 1");
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
        
        // Crear un pedido asociado a mesa1
        Pedido pedido = new Pedido();
        pedido.setFecha(LocalDateTime.now());
        pedido.setPrecioTotal(45.50);
        pedido.setMesa(mesa1);
        pedido.setEmpleado(empleado);
        pedido.setNegocio(negocio);
        pedidoRepository.save(pedido);
    }


    @Test
    void testFindById() {
        // Busca la mesa que ya guardaste en setUp() usando su ID
        Mesa found = mesaRepository.findById(mesa1.getId());
        System.out.println(found.getId());
        assertNotNull(found);
        assertEquals("Mesa Exterior 1", found.getName());
    }
    @Test
    void testFindAll() {

        List<Mesa> mesas = mesaRepository.findAll();
        assertEquals(3, mesas.size());
    }

    @Test
    void testFindByNumeroAsientos() {
        List<Mesa> mesas = mesaRepository.findMesasByNumeroAsientos(2);
        assertFalse(mesas.isEmpty());
        assertEquals(2, mesas.get(0).getNumeroAsientos());
    }
}
