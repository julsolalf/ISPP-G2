package ispp_g2.gastrostock.Proveedores;

import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface ProveedorRepository extends CrudRepository<Proveedor, Long> {

    Optional<Proveedor> findByNombre(String nombre);
}
