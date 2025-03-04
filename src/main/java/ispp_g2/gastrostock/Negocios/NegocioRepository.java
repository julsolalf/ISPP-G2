package ispp_g2.gastrostock.Negocios;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface NegocioRepository extends CrudRepository<Negocio, Long> {

    Optional<Negocio> findByCifNif(String cifNif);
}
