package ispp_g2.gastrostock.dueño;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface DueñoRepository extends CrudRepository<Dueño,Integer>{

    List<Dueño> findAll();
    
    Optional<Dueño> findByEmail(String email);
    
    Optional<Dueño> findByTokenDueño(String tokenDueño);
    
}
