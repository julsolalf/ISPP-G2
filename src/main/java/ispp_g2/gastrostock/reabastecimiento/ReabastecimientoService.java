package ispp_g2.gastrostock.reabastecimiento;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDate;
import java.util.stream.StreamSupport;

@Service
public class ReabastecimientoService {

    private final ReabastecimientoRepository reabastecimientoRepository;

    @Autowired
    public ReabastecimientoService(ReabastecimientoRepository reabastecimientoRepository) {
        this.reabastecimientoRepository = reabastecimientoRepository;
    }

    @Transactional(readOnly = true)
    public List<Reabastecimiento> getAll() {
        Iterable<Reabastecimiento> iterable = reabastecimientoRepository.findAll();
        return StreamSupport.stream(iterable.spliterator(), false).toList();
    }

    @Transactional(readOnly = true)
    public List<Reabastecimiento> getByFecha(LocalDate fecha) {
        return reabastecimientoRepository.findByFecha(fecha);
    }

    @Transactional(readOnly = true)
    public List<Reabastecimiento> getByPrecioTotal(Double precioTotal) {
        return reabastecimientoRepository.findByPrecioTotal(precioTotal);
    }

    @Transactional(readOnly = true)
    public List<Reabastecimiento> getByNegocio(Integer negocio) {
        return reabastecimientoRepository.findByNegocio(negocio);
    }

    @Transactional(readOnly = true)
    public Reabastecimiento getById(Integer  id) {
        return reabastecimientoRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Reabastecimiento> getByReferencia(String referencia) {
        return reabastecimientoRepository.findByReferencia(referencia);
    }

    @Transactional(readOnly = true)
    public List<Reabastecimiento> getByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin) {
        return reabastecimientoRepository.findByFechaBetween(fechaInicio, fechaFin);
    }

    @Transactional(readOnly = true)
    public List<Reabastecimiento> getByProveedor(Integer proveedor) {
        return reabastecimientoRepository.findByProveedor(proveedor);
    }

    @Transactional(readOnly = true)
    public List<Reabastecimiento> getByDueno(Integer dueno) {
        return reabastecimientoRepository.findByDueno(dueno);
    }

    @Transactional
    public Reabastecimiento save(Reabastecimiento reabastecimiento) {
        return reabastecimientoRepository.save(reabastecimiento);
    }

    @Transactional
    public void deleteById(Integer id) {
        reabastecimientoRepository.deleteById(id);
    }

}
