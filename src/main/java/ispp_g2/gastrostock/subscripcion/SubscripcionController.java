//// filepath: [SubscripcionController.java](http://_vscodecontentref_/0)
package ispp_g2.gastrostock.subscripcion;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserService;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscripcionController {

    private static final Logger log = LoggerFactory.getLogger(SubscripcionController.class);
    
    @Autowired
    private SubscripcionService subscripcionService;
    
    @Autowired
    private UserService userService;
    
    @Value("${stripe.webhook.secret}")
    private String webhookSecret;
    
    @PostMapping("/create-checkout-session")
    public ResponseEntity<CheckoutResponse> createCheckoutSession(@RequestBody CheckoutRequest request) {
        try {
            User currentUser = userService.findCurrentUser();
            String checkoutUrl = subscripcionService.createCheckoutSession(currentUser.getId(), request.getPlanType());
            return ResponseEntity.ok(new CheckoutResponse(checkoutUrl, null));
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CheckoutResponse(null, e.getMessage()));
        }
    }
    
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload, 
                                            @RequestHeader("Stripe-Signature") String sigHeader) {
        if (payload == null || sigHeader == null) {
            return ResponseEntity.badRequest().body("Missing payload or signature");
        }
        
        try {
            // Usamos Webhook.constructEvent para verificar la firma y construir el Event
            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
            log.info("Webhook recibido: {}", event.getType());
            
            // Procesar asíncronamente para responder rápidamente a Stripe
            CompletableFuture.runAsync(() -> {
                subscripcionService.handleWebhook(event);
            });
            
            // Responder a Stripe inmediatamente con 200 OK
            return ResponseEntity.ok("Webhook received");
        } catch (SignatureVerificationException e) {
            log.error("Error de verificación de firma: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Signature verification failed");
        } catch (Exception e) {
            log.error("Error procesando webhook: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing webhook: " + e.getMessage());
        }
    }
        
    @GetMapping("/status")
    public ResponseEntity<SubscriptionStatusResponse> getSubscriptionStatus() {
        User currentUser = userService.findCurrentUser();
        Subscripcion subscripcion = subscripcionService.getSubscripcionByUserId(currentUser.getId());
        if (subscripcion == null) {
            return ResponseEntity.notFound().build();
        }
        SubscriptionStatusResponse response = new SubscriptionStatusResponse(
                subscripcion.getType().toString(),
                subscripcion.getStatus().toString(),
                subscripcion.getEndDate()
        );
        return ResponseEntity.ok(response);
    }
    
    // DTOs para request/response
    
    public static class CheckoutRequest {
        private String planType;

        public String getPlanType() {
            return planType;
        }

        public void setPlanType(String planType) {
            this.planType = planType;
        }
    }
    
    public static class CheckoutResponse {
        private String checkoutUrl;
        private String errorMessage;

        public CheckoutResponse(String checkoutUrl, String errorMessage) {
            this.checkoutUrl = checkoutUrl;
            this.errorMessage = errorMessage;
        }

        public String getCheckoutUrl() {
            return checkoutUrl;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }
    
    public static class SubscriptionStatusResponse {
        private String planType;
        private String status;
        private LocalDateTime expirationDate;

        public SubscriptionStatusResponse(String planType, String status, LocalDateTime expirationDate) {
            this.planType = planType;
            this.status = status;
            this.expirationDate = expirationDate;
        }

        public String getPlanType() {
            return planType;
        }

        public String getStatus() {
            return status;
        }

        public LocalDateTime getExpirationDate() {
            return expirationDate;
        }
    }
}