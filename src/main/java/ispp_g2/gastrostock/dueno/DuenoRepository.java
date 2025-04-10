package ispp_g2.gastrostock.dueno;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DuenoRepository extends CrudRepository<Dueno,Integer>{

    @Query("SELECT d FROM Dueno d WHERE d.email = ?1")
    Optional<Dueno> findDuenoByEmail(String email);

    @Query("SELECT d FROM Dueno d WHERE d.firstName = ?1")
    List<Dueno> findDuenoByNombre(String nombre);

    @Query("SELECT d FROM Dueno d WHERE d.lastName = ?1")
    List<Dueno> findDuenoByApellido(String apellido);

    @Query("SELECT d FROM Dueno d WHERE d.numTelefono = ?1")
    Optional<Dueno> findDuenoByTelefono(String telefono);

    @Query("SELECT d FROM Dueno d WHERE d.user.id = ?1")
    Optional<Dueno> findDuenoByUser(Integer userId);

    @Query("SELECT d FROM Dueno d WHERE d.tokenDueno = ?1")
    Optional<Dueno> findDuenoByToken(String token);

}
