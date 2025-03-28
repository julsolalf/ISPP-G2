package ispp_g2.gastrostock.diaReparto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class DiaRepartoService {

    private final DiaRepartoRepository diaRepartoRepository;

    @Autowired
    public DiaRepartoService(DiaRepartoRepository diaRepartoRepository) {
        this.diaRepartoRepository = diaRepartoRepository;
    }

    @Transactional(readOnly = true)
    public DiaReparto getById(Integer id) {
        return diaRepartoRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<DiaReparto> getDiasReparto() {
        Iterable<DiaReparto> dia= diaRepartoRepository.findAll();
        return StreamSupport.stream(dia.spliterator(), false)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DiaReparto> getDiaRepartoByDiaSemana(DayOfWeek diaSemana) {
        return diaRepartoRepository.findDiaRepartoByDiaSemana(diaSemana);
    }

    @Transactional(readOnly = true)
    public List<DiaReparto> getDiaRepartoByNegocioId(String negocio) {
        return diaRepartoRepository.findDiaRepartoByNegocioId(negocio);
    }

    @Transactional(readOnly = true)
    public List<DiaReparto> getDiaRepartoByProveedorId(Integer proveedor) {
        return diaRepartoRepository.findDiaRepartoByProveedorId(proveedor);
    }

    @Transactional
    public DiaReparto save(DiaReparto diaReparto) {
        return diaRepartoRepository.save(diaReparto);
    }

    @Transactional
    public void deleteById(Integer id) {
        diaRepartoRepository.deleteById(id);
    }

}
