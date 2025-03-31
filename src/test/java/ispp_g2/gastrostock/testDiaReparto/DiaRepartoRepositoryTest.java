package ispp_g2.gastrostock.testDiaReparto;

import ispp_g2.gastrostock.diaReparto.DiaReparto;
import ispp_g2.gastrostock.diaReparto.DiaRepartoRepository;
import ispp_g2.gastrostock.dueno.Dueno;
import ispp_g2.gastrostock.dueno.DuenoRepository;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioRepository;
import ispp_g2.gastrostock.proveedores.Proveedor;
import ispp_g2.gastrostock.proveedores.ProveedorRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
class DiaRepartoRepositoryTest {

    @Autowired
    private DiaRepartoRepository diaRepartoRepository;
        
    @Autowired
    private NegocioRepository negocioRepository;
    
    @Autowired
    private DuenoRepository duenoRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    private DiaReparto diaReparto;
    private Negocio negocio1;
    private Proveedor proveedor;

    @BeforeEach
    void setUp() {
                
        Dueno dueno1 = new Dueno();
        dueno1.setFirstName("Juan");
        dueno1.setLastName("García");
        dueno1.setEmail("juan@example.com");
        dueno1.setNumTelefono("666111222");
        dueno1.setTokenDueno("TOKEN999");
        dueno1 = duenoRepository.save(dueno1);

        negocio1 = new Negocio();
        negocio1.setName("Restaurante La Tasca");
        negocio1.setDireccion("Calle Principal 123");
        negocio1.setCiudad("Sevilla");
        negocio1.setPais("Espana");
        negocio1.setCodigoPostal("41001");
        negocio1.setTokenNegocio(12345);
        negocio1.setDueno(dueno1);
        negocio1 = negocioRepository.save(negocio1);

        proveedor = new Proveedor();
        proveedor.setName("Distribuciones Alimentarias S.L.");
        proveedor.setEmail("distribuciones@example.com");
        proveedor.setTelefono("954111222");
        proveedor.setDireccion("Polígono Industrial, Nave 7");
        proveedorRepository.save(proveedor);

        diaReparto = new DiaReparto();
        diaReparto.setDiaSemana(DayOfWeek.MONDAY);
        diaReparto.setProveedor(proveedor);

        diaRepartoRepository.save(diaReparto);
    }

    @Test
    void testFindById_ExistingId() {
        DiaReparto nuevoDiaReparto = new DiaReparto();
        nuevoDiaReparto.setDiaSemana(DayOfWeek.MONDAY);
        nuevoDiaReparto.setProveedor(proveedor);
        
        DiaReparto diaRepartoGuardado = diaRepartoRepository.save(nuevoDiaReparto);
        Integer id = diaRepartoGuardado.getId();
        
        Optional<DiaReparto> result = diaRepartoRepository.findById(id);
    
        assertTrue(result.isPresent());
        assertEquals(DayOfWeek.MONDAY, result.get().getDiaSemana());
        assertEquals(proveedor.getId(), result.get().getProveedor().getId());
    }

    @Test
    void testFindById_NonExistingId() {
        Optional<DiaReparto> result = diaRepartoRepository.findById(99);

        assertFalse(result.isPresent());
    }

    @Test
    void testFindDiaRepartoByDiaSemana_ExistingDay() {
        List<DiaReparto> result = diaRepartoRepository.findDiaRepartoByDiaSemana(DayOfWeek.MONDAY);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(DayOfWeek.MONDAY, result.get(0).getDiaSemana());
    }

    @Test
    void testFindDiaRepartoByDiaSemana_NonExistingDay() {
        List<DiaReparto> result = diaRepartoRepository.findDiaRepartoByDiaSemana(DayOfWeek.FRIDAY);

        assertTrue(result.isEmpty());
    }

//    @Test
//    void testFindDiaRepartoByNegocioId_ExistingNegocio() {
//        String negocioId = negocio1.getId().toString();
//
//        List<DiaReparto> result = diaRepartoRepository.findDiaRepartoByNegocioId(negocioId);
//
//        assertFalse(result.isEmpty());
//        assertEquals(1, result.size());
//        assertEquals(negocioId, result.get(0).getNegocio().getId().toString());
//    }

//    @Test
//    void testFindDiaRepartoByNegocioId_NonExistingNegocio() {
//        List<DiaReparto> result = diaRepartoRepository.findDiaRepartoByNegocioId("99");
//
//        assertTrue(result.isEmpty());
//    }

    @Test
    void testFindDiaRepartoByProveedorId_ExistingProveedor() {
        Integer proveedorId = proveedor.getId();
        
        List<DiaReparto> result = diaRepartoRepository.findDiaRepartoByProveedorId(proveedorId);
    
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void testFindDiaRepartoByProveedorId_NonExistingProveedor() {
        List<DiaReparto> result = diaRepartoRepository.findDiaRepartoByProveedorId(99);

        assertTrue(result.isEmpty());
    }
}
