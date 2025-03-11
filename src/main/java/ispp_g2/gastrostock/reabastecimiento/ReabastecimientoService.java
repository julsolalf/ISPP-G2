package ispp_g2.gastrostock.reabastecimiento;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ispp_g2.gastrostock.proveedores.Proveedor;

import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDate;

@Service
public class ReabastecimientoService {

    private final ReabastecimientoRepository reabastecimientoRepository;

    @Autowired
    public ReabastecimientoService(ReabastecimientoRepository reabastecimientoRepository) {
        this.reabastecimientoRepository = reabastecimientoRepository;
    }

    @Transactional(readOnly = true)
    public Iterable<Reabastecimiento> getAll() {
        return reabastecimientoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Reabastecimiento> getById(Integer id) {
        return reabastecimientoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Reabastecimiento> getByReferencia(String referencia) {
        return reabastecimientoRepository.findByReferencia(referencia);
    }

    @Transactional(readOnly = true)
    public List<Reabastecimiento> getByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin) {
        return reabastecimientoRepository.findByFechaBetween(fechaInicio, fechaFin);
    }

    @Transactional(readOnly = true)
    public List<Reabastecimiento> getByProveedor(Proveedor proveedor) {
        return reabastecimientoRepository.findByProveedor(proveedor);
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
