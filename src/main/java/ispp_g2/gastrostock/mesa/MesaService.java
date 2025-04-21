package ispp_g2.gastrostock.mesa;

import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import ispp_g2.gastrostock.exceptions.ResourceNotFoundException;
import ispp_g2.gastrostock.negocio.NegocioRepository;
import jakarta.annotation.Resource;


@Service
public class MesaService {

    private final MesaRepository mesaRepository;
    private final NegocioRepository negocioRepository;

    @Autowired
    public MesaService(MesaRepository mesaRepository, NegocioRepository negocioRepository) {
        this.mesaRepository = mesaRepository;
        this.negocioRepository = negocioRepository;
    }

    @Transactional(readOnly = true)
    public Mesa getById(Integer id) {
        return mesaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("La mesa no existe"));
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
    public List<Mesa> getMesasByNegocio(Integer negocioId) {
        return mesaRepository.findMesasByNegocio(negocioId);
    }

    @Transactional
    public Mesa save(Mesa newMesa){
        return mesaRepository.save(newMesa);
    }

    @Transactional
    public Mesa update(Mesa newMesa, Integer id) {
      Mesa toUpdate = mesaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("La mesa no existe")); 
      BeanUtils.copyProperties(newMesa, toUpdate, "id");
      return mesaRepository.save(toUpdate);
    }

    @Transactional
    public void deleteById(Integer id) {
        mesaRepository.deleteById(id);
    }

    public Mesa convertirMesa(MesaDTO mesaDTO) {
        Mesa mesa = new Mesa();
        mesa.setName(mesaDTO.getNombre());
        mesa.setNumeroAsientos(mesaDTO.getNumeroAsientos());
        mesa.setNegocio(negocioRepository.findById(mesaDTO.getNegocioId()).orElseThrow(()-> new ResourceNotFoundException("Negocio no encontrado")));
        return mesa;
    }
}
