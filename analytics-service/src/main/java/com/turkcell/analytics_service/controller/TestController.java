package com.turkcell.analytics_service.controller;

import com.turkcell.analytics_service.producer.EventProducer;

import io.github.ergulberke.event.customer.CustomerEvent;
import io.github.ergulberke.event.plan.PlanEvent;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private static final Logger logger = Logger.getLogger(TestController.class.getName());
    private final EventProducer eventProducer;

    public TestController(EventProducer eventProducer) {
        this.eventProducer = eventProducer;
    }

    @PostMapping("/send-customer-event")
    public ResponseEntity<String> sendCustomerEvent(@RequestBody CustomerEvent event) {
        // Event için bazı alanları doldur
        if (event.getCustomerId() == null) {
            event.setCustomerId(UUID.randomUUID().toString());
        }
        if (event.getTimestamp() == null) {
            event.setTimestamp(LocalDateTime.now());
        }

        // Event'i gönder
        eventProducer.sendEvent("customer-events", event.getCustomerId(), event);
        logger.info("Customer event sent via test controller: " + event.getEventType());

        return ResponseEntity.ok("Customer event sent successfully");
    }

    @PostMapping("/send-plan-event")
    public ResponseEntity<String> sendPlanEvent(@RequestBody PlanEvent event) {
        // Event için bazı alanları doldur
        if (event.getPlanId() == null) {
            event.setPlanId(UUID.randomUUID().toString());
        }
        if (event.getTimestamp() == null) {
            event.setTimestamp(LocalDateTime.now());
        }

        // Event'i gönder
        eventProducer.sendEvent("plan-events", event.getPlanId(), event);
        logger.info("Plan event sent via test controller: " + event.getEventType());

        return ResponseEntity.ok("Plan event sent successfully");
    }
}