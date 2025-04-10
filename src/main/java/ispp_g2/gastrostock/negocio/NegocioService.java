package ispp_g2.gastrostock.negocio;

import ispp_g2.gastrostock.dueno.DuenoRepository;
import ispp_g2.gastrostock.exceptions.ResourceNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class NegocioService {

    private final NegocioRepository negocioRepository;
    private final DuenoRepository duenoRepository;

    @Autowired
    public NegocioService(NegocioRepository negocioRepository, DuenoRepository duenoRepository) {
        this.negocioRepository = negocioRepository;
        this.duenoRepository = duenoRepository;
    }

    @Transactional(readOnly = true)
    public Negocio getById(Integer id) {
        return negocioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El negocio no existe"));
    }

    @Transactional(readOnly = true)
    public List<Negocio> getNegocios() {
        Iterable<Negocio> negocios = negocioRepository.findAll();
        return StreamSupport.stream(negocios.spliterator(), false)
                .toList();
    }

    @Transactional(readOnly = true)
    public Negocio getByName(String name) {
        return negocioRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public List<Negocio> getByDireccion(String direccion) {
        return negocioRepository.findByDireccion(direccion);
    }

    @Transactional(readOnly = true)
    public Negocio getByToken(Integer token) {
        return negocioRepository.findByTokenNegocio(token);
    }

    @Transactional(readOnly = true)
    public List<Negocio> getByCiudad(String ciudad) {
        return negocioRepository.findByCiudad(ciudad);
    }

    @Transactional(readOnly = true)
    public List<Negocio> getByPais(String pais) {
        return negocioRepository.findByPais(pais);
    }

    @Transactional(readOnly = true)
    public List<Negocio> getByCodigoPostal(String codigoPostal) {
        return negocioRepository.findByCodigoPostal(codigoPostal);
    }

    @Transactional(readOnly = true)
    public List<Negocio> getByDueno(Integer dueno) {
        return negocioRepository.findByDueno(dueno);
    }

    @Transactional
    public Negocio save(Negocio newNegocio){
        return negocioRepository.save(newNegocio);
    }

    @Transactional
    public Negocio update(int id, Negocio newNegocio) {
        Negocio toUpdate = negocioRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("El negocio no existe"));
        BeanUtils.copyProperties(newNegocio, toUpdate, "id", "dueno", "tokenNegocio");
        return save(toUpdate);
    }

    @Transactional
    public void delete(Integer id){
        negocioRepository.deleteById(id);
    }

    public Negocio convertirDTONegocio(NegocioDTO negocioDTO) {
        Negocio negocio = new Negocio();
        negocio.setName(negocioDTO.getName());
        negocio.setDireccion(negocioDTO.getDireccion());
        negocio.setCodigoPostal(negocioDTO.getCodigoPostal());
        negocio.setCiudad(negocioDTO.getCiudad());
        negocio.setPais(negocioDTO.getPais());
        negocio.setTokenNegocio(negocioDTO.getTokenNegocio());
        negocio.setDueno(duenoRepository.findById(negocioDTO.getIdDueno()).orElse(null));
        return negocio;
    }

    public NegocioDTO convertirNegocioDTO(Negocio negocio) {
        NegocioDTO negocioDTO = new NegocioDTO();
        negocioDTO.setName(negocio.getName());
        negocioDTO.setDireccion(negocio.getDireccion());
        negocioDTO.setCodigoPostal(negocio.getCodigoPostal());
        negocioDTO.setCiudad(negocio.getCiudad());
        negocioDTO.setPais(negocio.getPais());
        negocioDTO.setTokenNegocio(negocio.getTokenNegocio());
        negocioDTO.setIdDueno(negocio.getDueno().getId());
        return negocioDTO;
    }
    
}
