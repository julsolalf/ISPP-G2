package ispp_g2.gastrostock.mesa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class MesaService {

    private MesaRepository mr;

    @Autowired
    public MesaService(MesaRepository MesaRepository) {
        this.mr = MesaRepository;
    }

    @Transactional(readOnly = true)
    public Mesa getById(Integer id) {
        return mr.findById(id);
    }

    @Transactional(readOnly = true)
    public Mesa getByName(String name) {
        return mr.findByName(name);
    }

    @Transactional(readOnly = true)
    public List<Mesa> getMesas() {
        return mr.findAll();
    }

    @Transactional(readOnly = true)
    public List<Mesa> getMesasByNumeroAsientos(Integer numeroAsientos) {
        return mr.findMesasByNumeroAsientos(numeroAsientos);
    }


    @Transactional
    public Mesa saveMesa(@Valid Mesa newMesa){
        return mr.save(newMesa);
    }

    
}
