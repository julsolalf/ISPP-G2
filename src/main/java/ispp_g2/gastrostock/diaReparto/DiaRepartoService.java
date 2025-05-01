package ispp_g2.gastrostock.diaReparto;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ispp_g2.gastrostock.exceptions.ResourceNotFoundException;
import ispp_g2.gastrostock.proveedores.ProveedorRepository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class DiaRepartoService {

    private final DiaRepartoRepository diaRepartoRepository;
    private final ProveedorRepository proveedorRepository;

    @Autowired
    public DiaRepartoService(DiaRepartoRepository diaRepartoRepository, ProveedorRepository proveedorRepository) {
        this.diaRepartoRepository = diaRepartoRepository;
        this.proveedorRepository = proveedorRepository;
    }

    @Transactional(readOnly = true)
    public DiaReparto getById(Integer id) {
        return diaRepartoRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("DiaReparto no encontrado"));
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
    public List<DiaReparto> getDiaRepartoByProveedorId(Integer proveedor) {
        return diaRepartoRepository.findDiaRepartoByProveedorId(proveedor);
    }

    @Transactional
    public DiaReparto save(DiaReparto diaReparto) {
        return diaRepartoRepository.save(diaReparto);
    }

    @Transactional
    public DiaReparto update(Integer id, DiaReparto diaReparto) {
        DiaReparto toUpdate = diaRepartoRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("DiaReparto no encontrado"));
        BeanUtils.copyProperties(diaReparto, toUpdate, "id");
        return diaRepartoRepository.save(toUpdate);  
    }

    @Transactional
    public void deleteById(Integer id) {
        diaRepartoRepository.deleteById(id);
    }

    public DiaReparto convertirDTODiaReparto(DiaRepartoDTO diaRepartoDTO) {
        DiaReparto diaReparto = new DiaReparto();
        diaReparto.setId(diaRepartoDTO.getId());
        diaReparto.setDiaSemana(diaRepartoDTO.getDiaSemana());
        diaReparto.setDescripcion(diaRepartoDTO.getDescripcion());
        diaReparto.setProveedor(proveedorRepository.findById(diaRepartoDTO.getProveedorId())
            .orElseThrow(()-> new ResourceNotFoundException("Proveedor no encontrado"))); 
        return diaReparto;
    }

}
