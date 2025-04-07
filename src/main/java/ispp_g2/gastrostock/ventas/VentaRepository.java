package ispp_g2.gastrostock.ventas;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaRepository extends CrudRepository<Venta,Integer>{

    @Query("SELECT v FROM Venta v WHERE v.negocio.id = ?1")
    List<Venta> findVentasByNegocioId(Integer negocioId);

}
