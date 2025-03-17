package ispp_g2.gastrostock.testMesa;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import ispp_g2.gastrostock.mesa.Mesa;
import ispp_g2.gastrostock.mesa.MesaRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MesaRepositoryTest {

    @Autowired
    private MesaRepository mesaRepository;

    @Test
    void testSaveAndFindById() {
        Mesa mesa = new Mesa();
        mesa.setName("Mesa 1");
        mesa.setNumeroAsientos(4);
        mesa = mesaRepository.save(mesa);

        Mesa found = mesaRepository.findById(mesa.getId());
        assertNotNull(found);
        assertEquals("Mesa 1", found.getName());
    }

    @Test
    void testFindAll() {
        Mesa mesa1 = new Mesa();
        mesa1.setName("Mesa A");
        mesa1.setNumeroAsientos(2);
        mesaRepository.save(mesa1);

        Mesa mesa2 = new Mesa();
        mesa2.setName("Mesa B");
        mesa2.setNumeroAsientos(4);
        mesaRepository.save(mesa2);

        List<Mesa> mesas = mesaRepository.findAll();
        assertEquals(2, mesas.size());
    }

    @Test
    void testFindByNumeroAsientos() {
        Mesa mesa = new Mesa();
        mesa.setName("Mesa Pequeña");
        mesa.setNumeroAsientos(2);
        mesaRepository.save(mesa);

        List<Mesa> mesas = mesaRepository.findMesasByNumeroAsientos(2);
        assertFalse(mesas.isEmpty());
        assertEquals(2, mesas.get(0).getNumeroAsientos());
    }
}
