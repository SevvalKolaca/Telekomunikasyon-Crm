package com.turkcell.analytics_service.config;

import io.github.ergulberke.event.customer.CustomerEvent;
import io.github.ergulberke.event.plan.PlanEvent;
import io.github.ergulberke.event.user.UserEvent;

import com.turkcell.analytics_service.event.SubscriptionEvent;
import com.turkcell.analytics_service.event.BillingEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

@Configuration
public class ConsumerConfig {

    private static final Logger logger = Logger.getLogger(ConsumerConfig.class.getName());

    @Bean
    public Consumer<CustomerEvent> customerEventConsumer() {
        return event -> {
            try {
                logger.info(
                        "Customer event received: " + event.getEventType() + " for customer: " + event.getCustomerId());

                // Burada müşteri olaylarını işleyebilirsiniz
                switch (event.getEventType().toUpperCase()) {
                    case "REGISTERED":
                        logger.info("Customer registered: " + event.getFullName());
                        break;
                    case "PROFILE_UPDATED":
                        logger.info("Customer profile updated: " + event.getCustomerId());
                        break;
                    case "CONTACT_UPDATED":
                        logger.info("Customer contact info updated: " + event.getCustomerId());
                        break;
                    case "STATUS_CHANGED":
                        logger.info("Customer status changed to: " + event.getStatus());
                        break;
                    case "DELETED":
                        logger.info("Customer deleted: " + event.getCustomerId());
                        break;
                    default:
                        logger.warning("Unknown customer event type: " + event.getEventType());
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error processing customer event", e);
            }
        };
    }

    @Bean
    public Consumer<PlanEvent> planEventConsumer() {
        return event -> {
            try {
                logger.info("Plan event received: " + event.getEventType() + " for plan: " + event.getPlanId());

                // Burada plan olaylarını işleyebilirsiniz
                switch (event.getEventType().toUpperCase()) {
                    case "CREATED":
                        logger.info("Plan created: " + event.getPlanName());
                        break;
                    case "UPDATED":
                        logger.info("Plan updated: " + event.getPlanName());
                        break;
                    case "DELETED":
                        logger.info("Plan deleted: " + event.getPlanId());
                        break;
                    case "PRICE_CHANGED":
                        logger.info("Plan price changed: " + event.getPlanName() + ", New price: " + event.getPrice());
                        break;
                    default:
                        logger.warning("Unknown plan event type: " + event.getEventType());
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error processing plan event", e);
            }
        };
    }

    @Bean
    public Consumer<UserEvent> userEventConsumer() {
        return event -> {
            try {
                logger.info("User event received: " + event.getEventType() + " for user: " + event.getUserId());

                switch (event.getEventType().toUpperCase()) {
                    case "CREATED":
                        logger.info("User created: " + event.getUsername());
                        break;
                    case "UPDATED":
                        logger.info("User updated: " + event.getUserId());
                        break;
                    case "DELETED":
                        logger.info("User deleted: " + event.getUserId());
                        break;
                    case "LOGIN":
                        logger.info("User logged in: " + event.getUserId());
                        break;
                    case "LOGOUT":
                        logger.info("User logged out: " + event.getUserId());
                        break;
                    default:
                        logger.warning("Unknown user event type: " + event.getEventType());
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error processing user event", e);
            }
        };
    }

    @Bean
    public Consumer<SubscriptionEvent> subscriptionEventConsumer() {
        return event -> {
            try {
                logger.info("Subscription event received: " + event.getEventType() + " for subscription: "
                        + event.getSubscriptionId());

                switch (event.getEventType().toUpperCase()) {
                    case "CREATED":
                        logger.info("Subscription created for user: " + event.getUserId());
                        break;
                    case "RENEWED":
                        logger.info("Subscription renewed: " + event.getSubscriptionId());
                        break;
                    case "UPGRADED":
                        logger.info("Subscription upgraded: " + event.getSubscriptionId());
                        break;
                    case "DOWNGRADED":
                        logger.info("Subscription downgraded: " + event.getSubscriptionId());
                        break;
                    case "CANCELLED":
                        logger.info("Subscription cancelled: " + event.getSubscriptionId());
                        break;
                    case "SUSPENDED":
                        logger.info("Subscription suspended: " + event.getSubscriptionId());
                        break;
                    default:
                        logger.warning("Unknown subscription event type: " + event.getEventType());
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error processing subscription event", e);
            }
        };
    }

    @Bean
    public Consumer<BillingEvent> billingEventConsumer() {
        return event -> {
            try {
                logger.info(
                        "Billing event received: " + event.getEventType() + " for invoice: " + event.getInvoiceId());

                switch (event.getEventType().toUpperCase()) {
                    case "CREATED":
                        logger.info("Invoice created for subscription: " + event.getSubscriptionId());
                        break;
                    case "PAID":
                        logger.info("Invoice paid: " + event.getInvoiceId());
                        break;
                    case "OVERDUE":
                        logger.info("Invoice overdue: " + event.getInvoiceId());
                        break;
                    case "CANCELLED":
                        logger.info("Invoice cancelled: " + event.getInvoiceId());
                        break;
                    default:
                        logger.warning("Unknown billing event type: " + event.getEventType());
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error processing billing event", e);
            }
        };
    }
}