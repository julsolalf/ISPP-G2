package ispp_g2.gastrostock.empleado;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpleadoRepository extends CrudRepository<Empleado, Integer> {

    @Query("SELECT e FROM Empleado e WHERE e.email = ?1")
    Optional<Empleado> findByEmail(String email);

    @Query("SELECT e FROM Empleado e WHERE e.firstName = ?1")
    List<Empleado> findByNombre(String nombre);

    @Query("SELECT e FROM Empleado e WHERE e.lastName = ?1")
    List<Empleado> findByApellido(String apellido);

    @Query("SELECT e FROM Empleado e WHERE e.numTelefono = ?1")
    Optional<Empleado> findByTelefono(String telefono);

    @Query("SELECT e FROM Empleado e WHERE e.negocio.id = ?1")
    List<Empleado> findByNegocio(Integer id);

    @Query("SELECT e FROM Empleado e WHERE e.negocio.dueno.id = ?1")
    List<Empleado> findByDueno(Integer id);

    @Query("SELECT e FROM Empleado e WHERE e.user.id = ?1")
    Optional<Empleado> findByUserId(Integer userId);

    @Query("SELECT e FROM Empleado e WHERE e.tokenEmpleado = ?1")
    Optional<Empleado> findByTokenEmpleado(String tokenEmpleado);

}
