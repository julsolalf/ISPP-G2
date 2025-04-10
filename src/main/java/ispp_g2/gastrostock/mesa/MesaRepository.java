package ispp_g2.gastrostock.mesa;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MesaRepository extends CrudRepository<Mesa,Integer> {

    @Query("SELECT m FROM Mesa m WHERE m.name LIKE %?1%")
    Mesa findMesaByName(String name);

    @Query("SELECT m FROM Mesa m WHERE m.negocio.id = ?1")
    List<Mesa> findMesasByNegocio(Integer negocioId);

    @Query("SELECT m FROM Mesa m WHERE m.numeroAsientos = ?1")
    List<Mesa> findMesaByNumeroAsientos(Integer numeroAsientos);

}
