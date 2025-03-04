package ispp_g2.gastrostock.Negocios;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface NegocioRepository extends CrudRepository<Negocio, Integer> {

    Optional<Negocio> findByCifNif(String cifNif);
}
