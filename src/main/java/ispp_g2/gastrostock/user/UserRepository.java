package ispp_g2.gastrostock.user;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends  CrudRepository<User, Integer>{

	@Query("SELECT u FROM User u WHERE u.username = ?1")
	User findByUsername(String username);

	@Query("SELECT u FROM User u WHERE u.username = ?1 AND u.password = ?2")
	User findByUsernameAndPassword(String username, String password);

	@Query("SELECT u FROM User u WHERE u.authority.authority = ?1")
	User findByAuthority(String authority);

	@Query("SELECT u FROM User u WHERE u.username = ?1")
	Optional<User> findUserByUsername(String username);

	@Query("SELECT u FROM User u WHERE u.subscription IS NULL")
	List<User> findUsersWithoutSubscription();


}