package ispp_g2.gastrostock.subscripcion;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.checkout.Session;
import com.stripe.model.StripeObject;
import com.stripe.model.Subscription;
import com.stripe.param.SubscriptionCancelParams;

import ispp_g2.gastrostock.dueno.Dueno;
import ispp_g2.gastrostock.dueno.DuenoService;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserService;
import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.gson.JsonObject;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class SubscripcionService {
    
    private static final Logger log = LoggerFactory.getLogger(SubscripcionService.class);
    
    @Autowired
    private SubscripcionRepository subscripcionRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private StripeService stripeService;

    @Autowired
    private DuenoService duenoService;

    /**
     * Crea una suscripción gratuita para un usuario nuevo
     */
    @Transactional
    public Subscripcion createFreeSubscription(User user) {
        Subscripcion subscription = new Subscripcion();
        subscription.setType(SubscripcionType.FREE);
        subscription.setStatus(SubscripcionStatus.ACTIVE);
        subscription.setStartDate(LocalDateTime.now());
        // Para el plan gratuito no establecemos fecha de fin
        
        // Establecer la relación bidireccional
        subscription.setUser(user);
        user.setSubscripcion(subscription);
        
        // Guardar el usuario para persistir la relación bidireccional
        userService.saveUser(user);
        
        log.info("Creada suscripción FREE para usuario ID: {}", user.getId());
        return subscription;
    }
    
    /**
     * Crea una sesión de checkout para que el usuario pueda pagar una suscripción premium
     */
    @Transactional
    public String createCheckoutSession(Integer userId, String planType) throws StripeException {
        User user = userService.findUserById(userId);
        Dueno dueno = duenoService.getDuenoByUser(userId);
        if (user == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        
        if (dueno == null || dueno.getEmail() == null || dueno.getEmail().isEmpty()) {
            throw new IllegalArgumentException("El usuario no tiene información de dueño o email asociado");
        }
        
        Subscripcion subscription = subscripcionRepository.findByUserId(userId);
        if (subscription == null) {
            subscription = createFreeSubscription(user);
        }
        
        // Crear cliente en Stripe si no existe
        if (subscription.getStripeCustomerId() == null) {
            Customer customer = stripeService.createCustomer(
                dueno.getEmail(),
                dueno.getFirstName() + " " + dueno.getLastName()
            );
            subscription.setStripeCustomerId(customer.getId());
            subscripcionRepository.save(subscription);
            log.info("Creado cliente Stripe para usuario ID: {}, Stripe Customer ID: {}", userId, customer.getId());
        }
        
        // Crear sesión de checkout
        Session session = stripeService.createCheckoutSession(
            subscription.getStripeCustomerId(), 
            planType
        );
        
        log.info("Creada sesión de checkout para usuario ID: {}, Plan: {}, Session ID: {}", 
                userId, planType, session.getId());
        
        return session.getUrl();
    }
    
    /**
     * Procesa webhooks de Stripe para mantener el estado de las suscripciones
     */
        @Transactional
    public void handleWebhook(Event event) {
        String eventType = event.getType();
        log.info("Procesando evento Stripe: {}, ID: {}", eventType, event.getId());
        log.info("Payload: {}", event.toJson());
        try {
            // Mejorar la deserialización con manejo de errores
            EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
            
            // Verificar si la deserialización tuvo éxito
            if (dataObjectDeserializer.getObject().isPresent()) {
                StripeObject stripeObject = dataObjectDeserializer.getObject().get();
                log.info("Deseriallizacion exitosa: {}", stripeObject);
                log.info("Tipo de objeto deserializado: {}", stripeObject.getClass().getName());
                log.info("Contenido del objeto: {}", stripeObject.toJson());
                switch (eventType) {
                    case "checkout.session.completed":
                        if (stripeObject instanceof Session session) {
                            handleCheckoutSessionCompleted(session);
                        } else {
                            log.warn("Objeto no es de tipo Session: {}", stripeObject.getClass().getName());
                        }
                        break;
                    case "invoice.payment_succeeded":
                        // Usar JSON directamente si la conversión a objeto específico falla
                        handleInvoicePaymentSucceeded(stripeObject);
                        break;
                    case "invoice.payment_failed":
                        handleInvoicePaymentFailed(stripeObject);
                        break;
                    case "customer.subscription.updated":
                    log.info("Es instancia de Subscription: {}", stripeObject instanceof Subscription);
                        if (stripeObject instanceof Subscription subscription) {
                            log.info("Objeto es de tipo Subscription: {}", subscription.getId());
                            handleSubscriptionUpdated(subscription);
                        } else {
                            log.warn("Objeto no es de tipo Subscription: {}", stripeObject.getClass().getName());
                        }
                        break;
                    case "customer.subscription.deleted":
                        if (stripeObject instanceof Subscription subscription) {
                            handleSubscriptionDeleted(subscription);
                        } else {
                            log.warn("Objeto no es de tipo Subscription: {}", stripeObject.getClass().getName());
                        }
                        break;
                    default:
                        log.info("Evento no manejado: {}", eventType);
                }
    /*        // En el bloque de JSON Raw en handleWebhook()
                String jsonRaw = event.getDataObjectDeserializer().getRawJson();
                log.warn("Deserialización estándar falló. Procesando como JSON Raw");
                
                com.google.gson.JsonElement element = com.google.gson.JsonParser.parseString(jsonRaw);
                if (element.isJsonObject()) {
                    JsonObject jsonObject = element.getAsJsonObject();
                    if ("checkout.session.completed".equals(eventType)) {
                        handleCheckoutSessionCompletedRaw(jsonObject);
                    } else if ("invoice.payment_succeeded".equals(eventType)) {
                        handleInvoicePaymentSucceededRaw(jsonObject);
                    } else if ("invoice.payment_failed".equals(eventType)) {
                        handleInvoicePaymentFailedRaw(jsonObject);
                    } else {
                        log.error("No se pudo deserializar el objeto del evento: {}", eventType);
                    }
            } else {
                log.error("El payload raw no es un JsonObject para el evento: {}", eventType);
            } */
        }else{  // Agregamos manejo de JSON raw si la deserialización falla
        log.warn("Deserialización fallida para evento {}, usando JSON Raw", eventType);
        String jsonRaw = event.getDataObjectDeserializer().getRawJson();
        
        if (jsonRaw != null) {
            com.google.gson.JsonParser jsonParser = new com.google.gson.JsonParser();
            JsonObject jsonObject = jsonParser.parse(jsonRaw).getAsJsonObject();
            
            switch (eventType) {
                case "checkout.session.completed":
                    handleCheckoutSessionCompletedRaw(jsonObject);
                    break;
                case "invoice.payment_succeeded":
                    handleInvoicePaymentSucceededRaw(jsonObject);
                    break;
                case "invoice.payment_failed":
                    handleInvoicePaymentFailedRaw(jsonObject);
                    break;
                case "customer.subscription.updated":
                    handleSubscriptionUpdatedRaw(jsonObject);
                    break;
                case "customer.subscription.deleted":
                    handleSubscriptionDeletedRaw(jsonObject);
                    break;
                default:
                    log.info("Evento no manejado: {}", eventType);
            }
        } else {
            log.error("JSON Raw no disponible para el evento: {}", eventType);
        }

        }
    }
        catch (Exception e) {
            log.error("Error al procesar webhook: {}", e.getMessage(), e);
    }
}

    
private void handleSubscriptionDeletedRaw(JsonObject jsonObject) {
    try {
        // Extraer los datos necesarios del JSON
        JsonObject object = jsonObject.getAsJsonObject("object");
        
        String subscriptionId = null;
        
        if (object.has("id")) {
            subscriptionId = object.get("id").getAsString();
        }
        
        log.info("Procesando customer.subscription.deleted (Raw) para Subscription ID: {}", subscriptionId);
        
        if (subscriptionId != null) {
            Subscripcion subscription = subscripcionRepository.findByStripeSubscriptionId(subscriptionId);
            
            if (subscription != null) {
                subscription.setStatus(SubscripcionStatus.CANCELED);
                subscription.setType(SubscripcionType.FREE);
                subscription.setStripeSubscriptionId(null);
                
                subscripcionRepository.save(subscription);
                log.info("Suscripción eliminada ID: {}, usuario devuelto a plan FREE (Raw)", subscriptionId);
            } else {
                log.error("No se encontró suscripción para Stripe Subscription ID: {} (Raw)", subscriptionId);
            }
        } else {
            log.error("No se pudo extraer Subscription ID del objeto JSON");
        }
    } catch (Exception e) {
        log.error("Error procesando customer.subscription.deleted raw: {}", e.getMessage(), e);
    }
}

private void handleSubscriptionUpdatedRaw(JsonObject jsonObject) {
    try {
        // Extraer los datos necesarios del JSON
        JsonObject object = jsonObject.getAsJsonObject("object");
        
        String subscriptionId = null;
        String status = null;
        
        if (object.has("id")) {
            subscriptionId = object.get("id").getAsString();
        }
        
        if (object.has("status")) {
            status = object.get("status").getAsString();
        }
        
        log.info("Procesando customer.subscription.updated (Raw) para Subscription ID: {}, estado: {}", 
                subscriptionId, status);
        
        if (subscriptionId != null) {
            Subscripcion subscription = subscripcionRepository.findByStripeSubscriptionId(subscriptionId);
            
            if (subscription != null && status != null) {
                // Actualizar el estado según el valor de Stripe
                switch (status) {
                    case "active":
                        subscription.setStatus(SubscripcionStatus.ACTIVE);
                        break;
                    case "past_due":
                        subscription.setStatus(SubscripcionStatus.PAST_DUE);
                        break;
                    case "unpaid":
                        subscription.setStatus(SubscripcionStatus.UNPAID);
                        break;
                    case "canceled":
                        subscription.setStatus(SubscripcionStatus.CANCELED);
                        // Al cancelarse, volvemos al plan gratuito
                        subscription.setType(SubscripcionType.FREE);
                        break;
                }
                
                // Si hay fecha de fin de período, actualizarla
                if (object.has("current_period_end")) {
                    long endTimestamp = object.get("current_period_end").getAsLong();
                    LocalDateTime endDate = LocalDateTime.ofInstant(
                        Instant.ofEpochSecond(endTimestamp), 
                        ZoneId.systemDefault()
                    );
                    subscription.setEndDate(endDate);
                }
                
                subscripcionRepository.save(subscription);
                log.info("Actualizado estado de suscripción ID: {} a: {} (Raw)", subscriptionId, status);
            } else {
                log.error("No se encontró suscripción para Stripe Subscription ID: {} (Raw)", subscriptionId);
            }
        } else {
            log.error("No se pudo extraer Subscription ID del objeto JSON");
        }
    } catch (Exception e) {
        log.error("Error procesando customer.subscription.updated raw: {}", e.getMessage(), e);
    }
}

private void handleCheckoutSessionCompleted(Session session) {
    String subscriptionId = session.getSubscription();
    String customerId = session.getCustomer();
    log.info("Procesando checkout.session.completed para Customer ID: {}, Subscription ID: {}", customerId, subscriptionId);
    // Buscar la suscripción mediante StripeCustomerId
    Subscripcion subscription = subscripcionRepository.findByStripeCustomerId(customerId);
    if (subscription != null) {
        try {
            // Obtener detalles de la suscripción en Stripe
            Subscription stripeSubscription = Subscription.retrieve(subscriptionId);

            // Actualizar la suscripción en la BD
            subscription.setStripeSubscriptionId(subscriptionId);
            subscription.setType(SubscripcionType.PREMIUM);
            subscription.setStatus(SubscripcionStatus.ACTIVE);
            subscription.setStartDate(LocalDateTime.now());
            // Convertir el Unix timestamp a LocalDateTime para la fecha de fin
            long endTimestamp = stripeSubscription.getCurrentPeriodEnd();
            LocalDateTime endDate = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(endTimestamp), ZoneId.systemDefault()
            );
            subscription.setEndDate(endDate);

            subscripcionRepository.save(subscription);

            // También actualizamos al usuario para propagar la relación
            User user = subscription.getUser();
            if (user != null) {
                userService.saveUser(user);
                log.info("Usuario ID: {} actualizado a PREMIUM", user.getId());
            }
            log.info("Actualizada suscripción a PREMIUM para Stripe Customer ID: {}", customerId);
        } catch (Exception e) {
            log.error("Error al actualizar suscripción después de checkout: {}", e.getMessage(), e);
        }
    } else {
        log.error("No se encontró suscripción para Stripe Customer ID: {}", customerId);
    }
}
    /**
     * Procesa el evento cuando un pago es exitoso
     */
    private void handleInvoicePaymentSucceeded(StripeObject object) {
        try {
            // Extraer el ID de cliente y suscripción del objeto invoice
            com.stripe.model.Invoice invoice = (com.stripe.model.Invoice) object;
            String customerId = invoice.getCustomer();
            
            Subscripcion subscription = subscripcionRepository.findByStripeCustomerId(customerId);
            if (subscription != null) {

                
                // Actualizar fecha de fin para el siguiente período
                LocalDateTime endDate = subscription.getNextBillingDate();
                
                subscription.setEndDate(endDate);
                subscription.setStatus(SubscripcionStatus.ACTIVE);
                subscription.setNextBillingDate(endDate);
                
                subscripcionRepository.save(subscription);
                log.info("Renovada suscripción para Stripe Customer ID: {}, nueva fecha fin: {}", 
                        customerId, endDate);
            }
        } catch (Exception e) {
            log.error("Error al procesar pago exitoso: {}", e.getMessage(), e);
        }
    }
    
    
    private void handleInvoicePaymentFailed(StripeObject object) {
        try {
            com.stripe.model.Invoice invoice = (com.stripe.model.Invoice) object;
            String customerId = invoice.getCustomer();
            
            Subscripcion subscripcion = subscripcionRepository.findByStripeCustomerId(customerId);
            if (subscripcion != null) {
                subscripcion.setStatus(SubscripcionStatus.PAST_DUE);
                subscripcionRepository.save(subscripcion);
                log.warn("Pago fallido para Stripe Customer ID: {}, estado actualizado a PAST_DUE", customerId);
            }
        } catch (Exception e) {
            log.error("Error al procesar pago fallido: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Procesa el evento cuando una suscripción se actualiza
     */
    private void handleSubscriptionUpdated(Subscription stripeSubscription) {
        try {
            String subscriptionId = stripeSubscription.getId();
            String status = stripeSubscription.getStatus();
            log.info("Procesando customer.subscription.updated para Subscription ID: {}", subscriptionId);
            Subscripcion subscription = subscripcionRepository.findByStripeSubscriptionId(subscriptionId);
            if (subscription != null) {
                // Mapear el estado de Stripe a nuestro enum
                switch (status) {
                    case "active":
                        subscription.setStatus(SubscripcionStatus.ACTIVE);
                        break;
                    case "past_due":
                        subscription.setStatus(SubscripcionStatus.PAST_DUE);
                        break;
                    case "unpaid":
                        subscription.setStatus(SubscripcionStatus.UNPAID);
                        break;
                    case "canceled":
                        subscription.setStatus(SubscripcionStatus.CANCELED);
                        // Al cancelarse, volvemos al plan gratuito
                        subscription.setType(SubscripcionType.FREE);
                        break;
                }
                
                subscripcionRepository.save(subscription);
                log.info("Actualizado estado de suscripción ID: {} a: {}", subscriptionId, status);
            }else {
                log.error("No se encontró suscripción para Stripe Subscription ID: {}", subscriptionId);
            }
        } catch (Exception e) {
            log.error("Error al procesar actualización de suscripción: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Procesa el evento cuando una suscripción se elimina
     */
    private void handleSubscriptionDeleted(Subscription stripeSubscription) {
        try {
            String subscriptionId = stripeSubscription.getId();
            
            Subscripcion subscription = subscripcionRepository.findByStripeSubscriptionId(subscriptionId);
            if (subscription != null) {
                subscription.setStatus(SubscripcionStatus.CANCELED);
                subscription.setType(SubscripcionType.FREE);
                subscription.setStripeSubscriptionId(null);
                
                subscripcionRepository.save(subscription);
                log.info("Suscripción eliminada ID: {}, usuario devuelto a plan FREE", subscriptionId);
            }
        } catch (Exception e) {
            log.error("Error al procesar eliminación de suscripción: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Obtiene la suscripción de un usuario por su ID
     */
    public Subscripcion getSubscripcionByUserId(Integer userId) {
        return subscripcionRepository.findByUserId(userId);
    }
    
    /**
     * Verifica si un usuario tiene una suscripción premium activa
     */
    public boolean hasPremiumAccess(Integer userId) {
        Subscripcion subscription = subscripcionRepository.findByUserId(userId);
        return subscription != null && subscription.isPremium();
    }
    
    /**
     * Cancela la suscripción premium de un usuario
     */
    @Transactional
    public void cancelPremiumSubscription(Integer userId) throws StripeException {
        Subscripcion subscription = subscripcionRepository.findByUserId(userId);
        
        if (subscription != null && subscription.getStripeSubscriptionId() != null) {
            // Cancelar en Stripe
            SubscriptionCancelParams cancelParams = SubscriptionCancelParams.builder()
                .build(); // Puedes configurar opciones como prorate=true
            
            Subscription canceledSubscription = Subscription.retrieve(subscription.getStripeSubscriptionId())
                .cancel(cancelParams);
            
            // Actualizar en nuestra base de datos
            subscription.setStatus(SubscripcionStatus.CANCELED);
            subscription.setType(SubscripcionType.FREE);
            subscription.setEndDate(LocalDateTime.now());
            
            subscripcionRepository.save(subscription);
            log.info("Cancelada suscripción premium para usuario ID: {}", userId);
        }
    }
    
    /**
     * Obtiene todas las suscripciones activas
     */
    public List<Subscripcion> getAllActiveSubscriptions() {
        return subscripcionRepository.findByStatus(SubscripcionStatus.ACTIVE);
    }
    
    /**
     * Obtiene todas las suscripciones premium activas
     */
    public List<Subscripcion> getAllActivePremiumSubscriptions() {
        return subscripcionRepository.findByTypeAndStatus(SubscripcionType.PREMIUM, SubscripcionStatus.ACTIVE);
    }
    
    /**
     * Asigna una suscripción gratuita a todos los usuarios que no tienen suscripción
     */
    @Transactional
    public void assignFreeSubscriptionToAllUsers() {
        List<User> usersWithoutSubscription = userService.findUsersWithoutSubscription(); 
        if (usersWithoutSubscription.isEmpty()) { 
            log.info("No hay usuarios sin suscripción gratuita.");
            return;
        }
        for (User user : usersWithoutSubscription) { 
            createFreeSubscription(user);
        } 
        log.info("Asignadas suscripciones gratuitas a {} usuarios", usersWithoutSubscription.size());
    }

    // Método para procesar checkout.session.completed con JSON raw
private void handleCheckoutSessionCompletedRaw(JsonObject jsonObject) {
    try {
        // Extraer los datos necesarios del JSON
        JsonObject object = jsonObject.getAsJsonObject("object");
        
        String customerId = null;
        String subscriptionId = null;
        
        if (object.has("customer")) {
            customerId = object.get("customer").getAsString();
        }
        
        if (object.has("subscription")) {
            subscriptionId = object.get("subscription").getAsString();
        }
        
        if (customerId != null) {
            Subscripcion subscription = subscripcionRepository.findByStripeCustomerId(customerId);
            if (subscription != null && subscriptionId != null) {
                subscription.setStripeSubscriptionId(subscriptionId);
                subscription.setType(SubscripcionType.PREMIUM);
                subscription.setStatus(SubscripcionStatus.ACTIVE);
                subscription.setStartDate(LocalDateTime.now());
                subscription.setEndDate(LocalDateTime.now().plusMonths(1)); // Asumiendo plan mensual
                
                subscripcionRepository.save(subscription);
                log.info("Actualizada suscripción a PREMIUM usando JSON Raw para Customer ID: {}", customerId);
            }
        }
    } catch (Exception e) {
        log.error("Error procesando checkout.session.completed raw: {}", e.getMessage(), e);
    }
}

// Método para procesar invoice.payment_succeeded con JSON raw
private void handleInvoicePaymentSucceededRaw(JsonObject jsonObject) {
    try {
        // Extraer los datos necesarios del JSON
        JsonObject object = jsonObject.getAsJsonObject("object");
        
        String customerId = null;
        
        if (object.has("customer")) {
            customerId = object.get("customer").getAsString();
        }
        
        if (customerId != null) {
            Subscripcion subscription = subscripcionRepository.findByStripeCustomerId(customerId);
            if (subscription != null) {
                // Actualizar fecha de fin para el siguiente período
                LocalDateTime endDate = LocalDateTime.now().plusMonths(1);
                
                subscription.setEndDate(endDate);
                subscription.setStatus(SubscripcionStatus.ACTIVE);
                subscription.setNextBillingDate(endDate);
                
                subscripcionRepository.save(subscription);
                log.info("Renovada suscripción usando JSON Raw para Customer ID: {}", customerId);
            }
        }
    } catch (Exception e) {
        log.error("Error procesando invoice.payment_succeeded raw: {}", e.getMessage(), e);
    }
}

// Método para procesar invoice.payment_failed con JSON raw
private void handleInvoicePaymentFailedRaw(JsonObject jsonObject) {
    try {
        // Extraer los datos necesarios del JSON
        JsonObject object = jsonObject.getAsJsonObject("object");
        
        String customerId = null;
        
        if (object.has("customer")) {
            customerId = object.get("customer").getAsString();
        }
        
        if (customerId != null) {
            Subscripcion subscription = subscripcionRepository.findByStripeCustomerId(customerId);
            if (subscription != null) {
                subscription.setStatus(SubscripcionStatus.PAST_DUE);
                subscripcionRepository.save(subscription);
                log.warn("Pago fallido usando JSON Raw para Customer ID: {}", customerId);
            }
        }
    } catch (Exception e) {
        log.error("Error procesando invoice.payment_failed raw: {}", e.getMessage(), e);
    }
}
}