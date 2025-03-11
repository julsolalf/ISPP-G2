package ispp_g2.gastrostock.reabastecimiento;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ispp_g2.gastrostock.proveedores.Proveedor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReabastecimientoRepository extends CrudRepository<Reabastecimiento, Integer> {

    Optional<Reabastecimiento> findByReferencia(String referencia);
    
    List<Reabastecimiento> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);

    List<Reabastecimiento> findByProveedor(Proveedor proveedor);

}
