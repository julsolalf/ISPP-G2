package ispp_g2.gastrostock.proveedores;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {

    List<Proveedor> findByFirstNameContainingIgnoreCase(String firstName);

    List<Proveedor> findByDiasRepartoContaining(DiaSemana diaSemana);
}
