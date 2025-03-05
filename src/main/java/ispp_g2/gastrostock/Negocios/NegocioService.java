package ispp_g2.gastrostock.Negocios;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NegocioService {

    private final NegocioRepository negocioRepository;

    @Autowired
    public NegocioService(NegocioRepository negocioRepository) {
        this.negocioRepository = negocioRepository;
    }

    public List<Negocio> findAll() {
        List<Negocio> negocios = new ArrayList<>();
        negocioRepository.findAll().forEach(negocios::add);
        return negocios;
    }

    public Optional<Negocio> findById(Integer id) {
        return negocioRepository.findById(id);
    }

    public Optional<Negocio> findByCifNif(String cifNif) {
        return negocioRepository.findByCifNif(cifNif);
    }

    public Negocio save(Negocio negocio) {
        return negocioRepository.save(negocio);
    }

    public void deleteById(Integer id) {
        negocioRepository.deleteById(id);
    }
}
