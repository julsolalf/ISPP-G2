package ispp_g2.gastrostock.dueño;

import java.util.List;
import java.util.Optional;

import ispp_g2.gastrostock.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DueñoRepository extends CrudRepository<Dueño,String>{

    @Query("SELECT d FROM Dueño d WHERE d.email = ?1")
    Optional<Dueño> findDueñoByEmail(String email);

    @Query("SELECT d FROM Dueño d WHERE d.firstName = ?1")
    List<Dueño> findDueñoByNombre(String nombre);

    @Query("SELECT d FROM Dueño d WHERE d.lastName = ?1")
    List<Dueño> findDueñoByApellido(String apellido);

    @Query("SELECT d FROM Dueño d WHERE d.numTelefono = ?1")
    Optional<Dueño> findDueñoByTelefono(String telefono);

    @Query("SELECT d FROM Dueño d WHERE d.user = ?1")
    Optional<Dueño> findDueñoByUser(User user);

    @Query("SELECT d FROM Dueño d WHERE d.tokenDueño = ?1")
    Optional<Dueño> findDueñoByToken(String token);
}
