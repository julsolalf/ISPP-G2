package ispp_g2.gastrostock.subscripcion;

import ispp_g2.gastrostock.model.BaseEntity;
import ispp_g2.gastrostock.user.User;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;
import ispp_g2.gastrostock.subscripcion.SubscripcionStatus;
import ispp_g2.gastrostock.subscripcion.SubscripcionType;

@Entity
@Getter
@Setter
public class Subscripcion extends BaseEntity implements Serializable {
    
    @Enumerated(EnumType.STRING)
    private SubscripcionType type;
    
    @Enumerated(EnumType.STRING)
    private SubscripcionStatus status;
    
    private String stripeCustomerId;
    private String stripeSubscriptionId;
    private String stripePaymentMethodId;
    
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime nextBillingDate;
    
    @JsonBackReference
    @OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "user_id", unique = true)
    private User user;
    
    public boolean isActive() {
        return status == SubscripcionStatus.ACTIVE && 
            (type == SubscripcionType.FREE || LocalDateTime.now().isBefore(endDate));
    }
    
    public boolean isPremium() {
        return type == SubscripcionType.PREMIUM && isActive();
    }
}



