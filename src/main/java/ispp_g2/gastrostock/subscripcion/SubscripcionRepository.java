package ispp_g2.gastrostock.subscripcion;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscripcionRepository extends CrudRepository<Subscripcion, Integer> {
    
    @Query("SELECT s FROM Subscripcion s WHERE s.stripeCustomerId = :stripeCustomerId")
    Subscripcion findByStripeCustomerId(String stripeCustomerId);
    
    @Query("SELECT s FROM Subscripcion s WHERE s.stripeSubscriptionId = :stripeSubscriptionId")
    Subscripcion findByStripeSubscriptionId(String stripeSubscriptionId);
    
    @Query("SELECT s FROM Subscripcion s WHERE s.userId = :userId")
    Subscripcion findByUserId(Integer userId);
    
    @Query("SELECT s FROM Subscripcion s WHERE s.status = :status")
    List<Subscripcion> findByStatus(SubscripcionStatus status);
    
    @Query("SELECT s FROM Subscripcion s WHERE s.type = :type AND s.status = :status")
    List<Subscripcion> findByTypeAndStatus(SubscripcionType type, SubscripcionStatus status);
    
    @Query("SELECT s FROM Subscripcion s WHERE s.type = :type")
    List<Subscripcion> findByType(SubscripcionType type);
}