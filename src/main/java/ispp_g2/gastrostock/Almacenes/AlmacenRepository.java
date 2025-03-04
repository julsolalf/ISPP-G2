package ispp_g2.gastrostock.Almacenes;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlmacenRepository extends CrudRepository<Almacen, Long> {

    // Buscar almacenes por metros cuadrados (ejemplo)
    List<Almacen> findByMetrosCuadradosGreaterThan(Double metrosCuadrados);
}
