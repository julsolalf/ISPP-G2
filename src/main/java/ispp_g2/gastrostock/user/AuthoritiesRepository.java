package ispp_g2.gastrostock.user;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthoritiesRepository extends CrudRepository<Authorities,Integer> {

    @Query("SELECT a FROM Authorities a WHERE a.authority = ?1")
    Authorities findByAuthority(String authority);

}
