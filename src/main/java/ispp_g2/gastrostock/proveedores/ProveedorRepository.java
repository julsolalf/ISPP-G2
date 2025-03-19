package ispp_g2.gastrostock.proveedores;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {

//    TEMPORAL FIX
    @Query("SELECT email FROM Proveedor")
    List<Proveedor> findByFirstNameContainingIgnoreCase(String firstName);

//    TEMPORAL FIX
    @Query("SELECT email FROM Proveedor")
    List<Proveedor> findByDiasRepartoContaining(DayOfWeek diaSemana);
}
