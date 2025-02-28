package ispp_g2.gastrostock.Ventas;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatoVentaRepository extends CrudRepository<PlatoVenta, Integer> {
    
}

