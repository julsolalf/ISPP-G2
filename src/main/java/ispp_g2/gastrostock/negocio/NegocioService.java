package ispp_g2.gastrostock.negocio;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class NegocioService {

    private NegocioRepository nr;

    @Autowired
    public NegocioService(NegocioRepository negocioRepository) {
        this.nr = negocioRepository;
    }

    @Transactional(readOnly = true)
    public Negocio getById(Integer id) {
        return nr.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Negocio> getNegocios() {
        return nr.findAll();
    }

    @Transactional(readOnly = true)
    public Negocio getByName(String name) {
        return nr.findByName(name);
    }

    @Transactional(readOnly = true)
    public Negocio getByDireccion(String direccion) {
        return nr.findByDireccion(direccion);
    }

    @Transactional(readOnly = true)
    public Negocio getByToken(Integer token) {
        return nr.findByTokenNegocio(token);
    }

    @Transactional(readOnly = true)
    public List<Negocio> getByCiudad(String ciudad) {
        return nr.findByCiudad(ciudad);
    }

    @Transactional(readOnly = true)
    public List<Negocio> getByPais(String pais) {
        return nr.findByPais(pais);
    }

    @Transactional(readOnly = true)
    public List<Negocio> getByCodigoPostal(String codigoPostal) {
        return nr.findByCodigoPostal(codigoPostal);
    }

    @Transactional
    public Negocio saveNegocio(@Valid Negocio newNegocio){
        return nr.save(newNegocio);
    }

    @Transactional
    public void deleteNegocioByToken(Integer token) {
        Negocio n = nr.findByTokenNegocio(token);
        nr.delete(n);
    }
    
}
