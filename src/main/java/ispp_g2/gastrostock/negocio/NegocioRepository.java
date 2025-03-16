package ispp_g2.gastrostock.negocio;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NegocioRepository extends CrudRepository<Negocio, String> {

    public List<Negocio> findAll();

    public Negocio findById(Integer id);

    public Negocio findByDireccion(String direccion);

    public Negocio findByName(String name);

    public List<Negocio> findByCiudad(String ciudad);

    public List<Negocio> findByCodigoPostal(String codigoPostal);

    public List<Negocio> findByPais(String pais);

    public Negocio findByTokenNegocio(Integer token);

}
