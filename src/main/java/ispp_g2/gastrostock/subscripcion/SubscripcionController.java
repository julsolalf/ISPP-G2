package ispp_g2.gastrostock.subscripcion;

import com.stripe.exception.StripeException;
import com.stripe.model.Event;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserService;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscripcionController {
    
    @Autowired
    private SubscripcionService subscripcionService;
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/create-checkout-session")
    public ResponseEntity<CheckoutResponse> createCheckoutSession(@RequestBody CheckoutRequest request) {
        try {
            User currentUser = userService.findCurrentUser();
            String checkoutUrl = subscripcionService.createCheckoutSession(
                currentUser.getId(), 
                request.getPlanType()
            );
            
            return ResponseEntity.ok(new CheckoutResponse(checkoutUrl));
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CheckoutResponse(null, e.getMessage()));
        }
    }
    
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload, 
                                               @RequestHeader("Stripe-Signature") String sigHeader) {
        try {
            // Verificar firma de webhook
            Event event = Event.constructEvent(payload, sigHeader, webhookSecret);
            subscripcionService.handleWebhook(event);
            return ResponseEntity.ok("Webhook processed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Webhook error: " + e.getMessage());
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
    static class CheckoutRequest {
        private String planType;
        // getters/setters
    }
    
    static class CheckoutResponse {
        private String checkoutUrl;
        private String errorMessage;
        // constructor y getters
    }
    
    static class SubscriptionStatusResponse {
        private String planType;
        private String status;
        private LocalDateTime expirationDate;
        // constructor y getters
    }
}