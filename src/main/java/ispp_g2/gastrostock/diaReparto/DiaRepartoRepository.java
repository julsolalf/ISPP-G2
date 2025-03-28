package ispp_g2.gastrostock.diaReparto;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiaRepartoRepository extends CrudRepository<DiaReparto, Integer> {

    @Query("SELECT d FROM DiaReparto d WHERE d.diaSemana = ?1")
    List<DiaReparto> findDiaRepartoByDiaSemana(DayOfWeek diaSemana);

    @Query("SELECT d FROM DiaReparto d WHERE d.negocio.id = ?1")
    List<DiaReparto> findDiaRepartoByNegocioId(String negocio);

    @Query("SELECT d FROM DiaReparto d WHERE d.proveedor.id = ?1")
    List<DiaReparto> findDiaRepartoByProveedorId(Integer proveedor);
}
