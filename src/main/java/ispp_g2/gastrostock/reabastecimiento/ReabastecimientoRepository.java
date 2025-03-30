package ispp_g2.gastrostock.reabastecimiento;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReabastecimientoRepository extends CrudRepository<Reabastecimiento, Integer> {

    @Query("SELECT r FROM Reabastecimiento r WHERE r.fecha = ?1")
    List<Reabastecimiento> findByFecha(LocalDate fecha);

    @Query("SELECT r FROM Reabastecimiento r WHERE r.fecha BETWEEN ?1 AND ?2")
    List<Reabastecimiento> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);

    @Query("SELECT r FROM Reabastecimiento r WHERE r.precioTotal = ?1")
    List<Reabastecimiento> findByPrecioTotal(Double precioTotal);

    @Query("SELECT r FROM Reabastecimiento r WHERE r.referencia = ?1")
    List<Reabastecimiento> findByReferencia(String referencia);

    @Query("SELECT r FROM Reabastecimiento r WHERE r.proveedor.id = ?1")
    List<Reabastecimiento> findByProveedor(Integer proveedor);

    @Query("SELECT r FROM Reabastecimiento r WHERE r.negocio.id = ?1")
    List<Reabastecimiento> findByNegocio(Integer negocio);

}
