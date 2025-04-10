package ispp_g2.gastrostock.subscripcion;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscripcionDTO {
    private String planType;
    private String status;
    private LocalDateTime expirationDate;
    private LocalDateTime nextBillingDate;
    private boolean isActive;
    private boolean isPremium;
}