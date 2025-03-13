package ispp_g2.gastrostock.empleado;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface EmpleadoRepository extends CrudRepository<Empleado, Integer> {
    Optional<Empleado> findByTokenEmpleado(String tokenEmpleado);
}
