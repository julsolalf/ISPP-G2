package ispp_g2.gastrostock.lote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class LoteService {

    private LoteRepository loteRepository;

    @Autowired
    public LoteService(LoteRepository loteRepository) {
        this.loteRepository = loteRepository;
    }

    @Transactional(readOnly = true)
    public Lote getById(Integer id) {
        return loteRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Lote> getLotes() {
        Iterable<Lote> lotes = loteRepository.findAll();
        return StreamSupport.stream(lotes.spliterator(),false)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Lote> getLotesByCantidad(Integer cantidad) {
        return loteRepository.findByCantidad(cantidad);
    }

    @Transactional(readOnly = true)
    public List<Lote> getLotesByFechaCaducidad(LocalDate fechaCaducidad) {
        return loteRepository.findByFechaCaducidad(fechaCaducidad);
    }

    @Transactional(readOnly = true)
    public List<Lote> getLotesByProductoId(Integer producto) {
        return loteRepository.findByProductoId(producto);
    }

    @Transactional(readOnly = true)
    public List<Lote> getLotesByReabastecimientoId(Integer reabastecimiento) {
        return loteRepository.findByReabastecimientoId(reabastecimiento);
    }

    @Transactional(readOnly = true)
    public List<Lote> getLotesCaducadosByNegocioId(Integer negocioId, LocalDate fechaActual) {
        return loteRepository.findCaducadosByNegocioId(negocioId, fechaActual); 
    }

    @Transactional
    public Lote save(Lote lote) {
        return loteRepository.save(lote);
    }

    @Transactional
    public void delete(Integer id) {
        loteRepository.deleteById(id);
    }

}
