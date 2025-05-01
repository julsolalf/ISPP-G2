package ispp_g2.gastrostock.proveedores;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProveedorRepository extends CrudRepository<Proveedor, Integer> {

    @Query("SELECT p FROM Proveedor p WHERE p.name = ?1")
    Proveedor findByNombre(String nombre);

    @Query("SELECT p FROM Proveedor p WHERE p.email = ?1")
    Proveedor findByEmail(String email);

    @Query("SELECT p FROM Proveedor p WHERE p.telefono = ?1")
    Proveedor findByTelefono(String telefono);

    @Query("SELECT p FROM Proveedor p WHERE p.direccion = ?1")
    Proveedor findByDireccion(String direccion);

    @Query("SELECT p FROM Proveedor p WHERE p.negocio.id = ?1")
    List<Proveedor> findProveedorByNegocioId(Integer negocio);

    @Query("SELECT p FROM Proveedor p WHERE p.negocio.dueno.id = ?1")
    List<Proveedor> findProveedorByDuenoId(Integer dueno);

}
