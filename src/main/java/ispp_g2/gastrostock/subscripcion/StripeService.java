package ispp_g2.gastrostock.subscripcion;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Price;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.SubscriptionCreateParams;
import com.stripe.param.InvoiceCreateParams.PaymentSettings.PaymentMethodType;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {
    
    @Value("${stripe.api.key}")
    private String stripeApiKey;
    
    @Value("${stripe.premium.price.id}")
    private String premiumPriceId;
    
    @Value("${app.frontend.url}")
    private String frontendUrl;
    
    @PostConstruct
    public void init() {
        initializeStripeApiKey(stripeApiKey);
    }
    
    private static void initializeStripeApiKey(String apiKey) {
        Stripe.apiKey = apiKey;
    }
    
    public Session createCheckoutSession(String customerId, String planType) throws StripeException {
        if (!"PREMIUM".equalsIgnoreCase(planType)) {
            // Para planes gratuitos no se necesita checkout
            throw new IllegalArgumentException("Checkout session is only available for PREMIUM plan");
        }
        
        String priceId = premiumPriceId; // Solo para premium
        
        SessionCreateParams params = SessionCreateParams.builder()
                .setCustomer(customerId)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setPrice(priceId)
                                .setQuantity(1L)
                                .build()
                )
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSuccessUrl(frontendUrl + "/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(frontendUrl + "/planes")
                .build();
        
        return Session.create(params);
    }
    
    public Customer createCustomer(String email, String name) throws StripeException {
        CustomerCreateParams params = 
            CustomerCreateParams.builder()
                .setEmail(email)
                .setName(name)
                .build();
        
        return Customer.create(params);
    }
    

}