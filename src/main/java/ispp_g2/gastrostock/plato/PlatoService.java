package ispp_g2.gastrostock.plato;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class PlatoService {
    PlatoRepository repo;

    @Autowired
    public PlatoService(PlatoRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<Plato> getPlatos() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Plato getByName(String nombre) {
        return repo.findByName(nombre);
    }

    @Transactional
    public Plato savePlato(@Valid Plato newPlato) {
        return repo.save(newPlato);
    }

    @Transactional(readOnly = true)
    public List<Plato> getPlatoByCategoria(String categoria) {
        return repo.findByCategoria(categoria);
    }

    @Transactional
    public void deletePlayer(Plato p) {
        repo.delete(p);
    }

    public Plato getById(Integer id) {
        return repo.getById();
    }
}
