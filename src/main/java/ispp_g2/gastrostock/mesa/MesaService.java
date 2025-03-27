package ispp_g2.gastrostock.mesa;

import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class MesaService {

    private final MesaRepository mesaRepository;

    @Autowired
    public MesaService(MesaRepository mesaRepository) {
        this.mesaRepository = mesaRepository;
    }

    @Transactional(readOnly = true)
    public Mesa getById(String id) {
        return mesaRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Mesa getByName(String name) {
        return mesaRepository.findMesaByName(name);
    }

    @Transactional(readOnly = true)
    public List<Mesa> getMesas() {
        Iterable<Mesa> mesas = mesaRepository.findAll();
        return StreamSupport.stream(mesas.spliterator(), false)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Mesa> getMesasByNumeroAsientos(Integer numeroAsientos) {
        return mesaRepository.findMesaByNumeroAsientos(numeroAsientos);
    }

    @Transactional(readOnly = true)
    public List<Mesa> getMesasByNegocio(String negocioId) {
        return mesaRepository.findMesasByNegocio(negocioId);
    }

    @Transactional
    public Mesa save(Mesa newMesa){
        return mesaRepository.save(newMesa);
    }

    @Transactional
    public void deleteById(String id) {
        mesaRepository.deleteById(id);
    }
}
