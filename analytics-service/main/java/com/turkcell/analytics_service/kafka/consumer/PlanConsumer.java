package com.turkcell.analytics_service.kafka.consumer;

import org.springframework.stereotype.Component;
import io.github.ergulberke.event.plan.PlanCreatedEvent;
import java.util.function.Consumer;
import org.springframework.context.annotation.Bean;

@Component
public class PlanConsumer {

    @Bean
    public Consumer<PlanCreatedEvent> planCreatedFunction() {
        return event -> System.out.println(event.getPlanId() + "  " + event.getPlanName() + "  " + event.getPrice()
                + "  " + event.getCurrency() + "  " + event.getDurationInMonths() + "  " + event.getFeatures() + "  "
                + event.getEventType() + "  " + event.getTimestamp());
    }

}