package com.turkcell.notification_service.kafka;
import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentBillingConsumer {
    
    @Bean
    public Consumer<String> billingPaymentFunction() {     //buradaki billingPaymentFunction, application.yml dosyasında
        return message -> {                                 //tanımlanan billingPaymentFunction'a karşılık gelmektedir
            System.out.println(message);                    // definition olarak ne verirsek buraya da onu yazmamız zorunlu
        };
    }






}
