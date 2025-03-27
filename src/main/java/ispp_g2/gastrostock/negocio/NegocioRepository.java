package ispp_g2.gastrostock.negocio;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NegocioRepository extends CrudRepository<Negocio, String> {

    @Query("SELECT n FROM Negocio n WHERE n.name = ?1")
    Negocio findByName(String nombre);

    @Query("SELECT n FROM Negocio n WHERE n.direccion = ?1")
    List<Negocio> findByDireccion(String direccion);

    @Query("SELECT n FROM Negocio n WHERE n.tokenNegocio = ?1")
    Negocio findByTokenNegocio(Integer tokenNegocio);

    @Query("SELECT n FROM Negocio n WHERE n.codigoPostal = ?1")
    List<Negocio> findByCodigoPostal(String codigoPostal);

    @Query("SELECT n FROM Negocio n WHERE n.ciudad = ?1")
    List<Negocio> findByCiudad(String ciudad);

    @Query("SELECT n FROM Negocio n WHERE n.pais = ?1")
    List<Negocio> findByPais(String pais);

    @Query("SELECT n FROM Negocio n WHERE n.dueno.id = ?1")
    List<Negocio> findByDueno(String dueno);

}
