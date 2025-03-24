package com.turkcell.notification_service.kafka;
import java.util.function.Consumer;

import io.github.ergulberke.event.billingPayment.BillCreatedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentBillingConsumer {
    
    @Bean
    public Consumer<BillCreatedEvent> billingPaymentFunction() {     //buradaki billingPaymentFunction, application.yml dosyasında
        return event -> {                                 //tanımlanan billingPaymentFunction'a karşılık gelmektedir
            System.out.println("Yeni fatura oluşturuldu:");
            System.out.println("ID: " + event.getId());
            System.out.println("Fatura Numarası: " + event.getBillNumber());
            System.out.println("Tutar: " + event.getAmount());
            System.out.println("Son Ödeme Tarihi: " + event.getDueDate());
            System.out.println("Ödeme Tarihi: " + event.getPaymentDate());
            System.out.println("Durum: " + event.getStatus());
            System.out.println("Müşteri ID: " + event.getCustomerId());
        };
    }






}
