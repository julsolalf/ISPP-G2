package ispp_g2.gastrostock.negocio;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class NegocioService {

    private final NegocioRepository negocioRepository;

    @Autowired
    public NegocioService(NegocioRepository negocioRepository) {
        this.negocioRepository = negocioRepository;
    }

    @Transactional(readOnly = true)
    public Negocio getById(Integer id) {
        return negocioRepository.findById(id).orElse(null);
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
    public List<Negocio> getByDueño(Integer dueño) {
        return negocioRepository.findByDueño(dueño);
    }

    @Transactional
    public Negocio save(Negocio newNegocio){
        return negocioRepository.save(newNegocio);
    }

    @Transactional
    public void delete(Integer id){
        negocioRepository.deleteById(id);
    }
    
}
