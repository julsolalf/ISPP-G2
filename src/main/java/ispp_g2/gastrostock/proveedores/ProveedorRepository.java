package ispp_g2.gastrostock.proveedores;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProveedorRepository extends CrudRepository<Proveedor, String> {

    @Query("SELECT p FROM Proveedor p WHERE p.name = ?1")
    Proveedor findByNombre(String nombre);

    @Query("SELECT p FROM Proveedor p WHERE p.email = ?1")
    Proveedor findByEmail(String email);

    @Query("SELECT p FROM Proveedor p WHERE p.telefono = ?1")
    Proveedor findByTelefono(String telefono);

    @Query("SELECT p FROM Proveedor p WHERE p.direccion = ?1")
    Proveedor findByDireccion(String direccion);

}
