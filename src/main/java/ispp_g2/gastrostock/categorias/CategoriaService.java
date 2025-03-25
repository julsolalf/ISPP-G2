package ispp_g2.gastrostock.categorias;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Autowired
    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public Categoria getById(String id) {
        return categoriaRepository.findById(id).orElse(null);
    }

    public List<Categoria> getCategorias() {
        Iterable<Categoria> categorias = categoriaRepository.findAll();
        return StreamSupport.stream(categorias.spliterator(), false)
                .toList();
    }

    public List<Categoria> getCategoriasByNegocioId(String negocioId) {
        return categoriaRepository.findByNegocioId(negocioId);
    }

    public List<Categoria> getCategoriasByName(String name) {
        return categoriaRepository.findByName(name);
    }


    public Categoria save(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public void delete(String id) {
        categoriaRepository.deleteById(id);
    }
}
