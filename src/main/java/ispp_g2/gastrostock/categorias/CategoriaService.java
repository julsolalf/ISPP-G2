package ispp_g2.gastrostock.categorias;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ispp_g2.gastrostock.exceptions.ResourceNotFoundException;
import ispp_g2.gastrostock.negocio.NegocioRepository;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final NegocioRepository negocioRepository;

    @Autowired
    public CategoriaService(CategoriaRepository categoriaRepository, NegocioRepository negocioRepository) {
        this.categoriaRepository = categoriaRepository;
        this.negocioRepository = negocioRepository;
    }

    @Transactional(readOnly = true)
    public Categoria getById(Integer id) {
        return categoriaRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("La categoria no existe"));
    }

    @Transactional(readOnly = true)
    public List<Categoria> getCategorias() {
        Iterable<Categoria> categorias = categoriaRepository.findAll();
        return StreamSupport.stream(categorias.spliterator(), false)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Categoria> getCategoriasByNegocioId(Integer negocioId) {
        return categoriaRepository.findByNegocioId(negocioId);
    }

    @Transactional(readOnly = true)
    public List<Categoria> getCategoriasByName(String name) {
        return categoriaRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public List<Categoria> getCategoriasInventarioByNegocioId(Integer negocioId) {
        return categoriaRepository.findInventarioByNegocioId(negocioId);
    }

    @Transactional(readOnly = true)
    public List<Categoria> getCategoriasVentaByNegocioId(Integer negocioId) {
        return categoriaRepository.findVentaByNegocioId(negocioId);
    }

    @Transactional
    public Categoria save(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @Transactional
    public Categoria update(Integer id, Categoria categoria) {
        Categoria toUpdate = categoriaRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("La categoria no existe"));
        BeanUtils.copyProperties(categoria, toUpdate, "id");
        return categoriaRepository.save(toUpdate);
    }

    @Transactional
    public void delete(Integer id) {
        categoriaRepository.deleteById(id);
    }

    public Categoria convertirCategoria(CategoriaDTO categoriaDTO) {
        Categoria categoria = new Categoria();
        categoria.setName(categoriaDTO.getNombre());
        categoria.setNegocio(negocioRepository.findById(categoriaDTO.getNegocioId())
            .orElseThrow(() -> new ResourceNotFoundException("El negocio no existe")));
        categoria.setPertenece(categoriaDTO.getPertenece());
        return categoria;
    }
}
