package com.turkcell.analytics_service.controller;

import com.turkcell.analytics_service.dto.UserActivityDto;
import com.turkcell.analytics_service.enums.ActivityType;
import com.turkcell.analytics_service.service.UserActivityService;
import io.github.ergulberke.event.user.UserLoginEvent;
import io.github.ergulberke.event.customer.CustomerCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
@Slf4j
public class IntegrationTestController {

    private final UserActivityService userActivityService;
    private final StreamBridge streamBridge;

    /**
     * Kafka entegrasyonunu test etmek için manual bir giriş olayı gönderir
     * 
     * @return Test sonuçları
     */
    @PostMapping("/send-user-login-event")
    public ResponseEntity<Map<String, String>> sendTestUserLoginEvent() {
        try {
            // Test için bir kullanıcı giriş olayı oluştur
            UserLoginEvent testEvent = new UserLoginEvent();
            testEvent.setUserId(UUID.randomUUID().toString());
            testEvent.setEmail("test@example.com");
            testEvent.setLoginTime(LocalDateTime.now());
            testEvent.setIpAddress("127.0.0.1");
            testEvent.setDeviceInfo("Test Browser");

            // Olayı Kafka'ya gönder
            boolean result = streamBridge.send("userLoginFunction-in-0", testEvent);

            // Test sonucunu döndür
            Map<String, String> response = new HashMap<>();
            if (result) {
                response.put("status", "success");
                response.put("message", "Test olay başarıyla Kafka'ya gönderildi");
                log.info("Test olay başarıyla Kafka'ya gönderildi: {}", testEvent.getUserId());
            } else {
                response.put("status", "error");
                response.put("message", "Test olay gönderilemedi");
                log.error("Test olay gönderilemedi");
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Hata: " + e.getMessage());
            log.error("Test olay gönderilirken hata: ", e);
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Customer oluşturma olayını test etmek için endpoint
     * 
     * @return Test sonuçları
     */
    @PostMapping("/send-customer-event")
    public ResponseEntity<Map<String, String>> sendTestCustomerEvent() {
        try {
            // Test için bir müşteri oluşturma olayı oluştur
            CustomerCreatedEvent testEvent = new CustomerCreatedEvent();
            // CustomerCreatedEvent özelliklerini doldur

            // Olayı Kafka'ya gönder
            boolean result = streamBridge.send("customerFunction-in-0", testEvent);

            // Test sonucunu döndür
            Map<String, String> response = new HashMap<>();
            if (result) {
                response.put("status", "success");
                response.put("message", "Müşteri olayı başarıyla Kafka'ya gönderildi");
                log.info("Müşteri olayı başarıyla Kafka'ya gönderildi");
            } else {
                response.put("status", "error");
                response.put("message", "Müşteri olayı gönderilemedi");
                log.error("Müşteri olayı gönderilemedi");
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Hata: " + e.getMessage());
            log.error("Müşteri olayı gönderilirken hata: ", e);
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Kullanıcı aktivite servisini doğrudan test eder
     * 
     * @return Test sonuçları
     */
    @PostMapping("/direct-service-test")
    public ResponseEntity<Map<String, String>> testUserActivityService() {
        try {
            // Test için UserActivityDto oluştur
            UserActivityDto testActivity = new UserActivityDto();
            testActivity.setUserId(UUID.randomUUID());
            testActivity.setActivityType(ActivityType.USER_LOGIN);
            testActivity.setDescription("Test aktivitesi");
            testActivity.setTimestamp(LocalDateTime.now());
            testActivity.setIpAddress("127.0.0.1");
            testActivity.setUserAgent("Test Agent");
            testActivity.setSessionId(UUID.randomUUID().toString());

            // Servisi doğrudan çağır
            userActivityService.logActivity(testActivity);

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "UserActivityService testi başarılı");
            log.info("UserActivityService testi başarılı: {}", testActivity.getUserId());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Hata: " + e.getMessage());
            log.error("UserActivityService testi sırasında hata: ", e);
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Kafka bağlantı durumunu test eder
     * 
     * @return Durum bilgisi
     */
    @GetMapping("/kafka-status")
    public ResponseEntity<Map<String, String>> checkKafkaConnection() {
        Map<String, String> status = new HashMap<>();

        try {
            // Basit bir test mesajı gönder
            boolean result = streamBridge.send("test-topic", "test-message");

            if (result) {
                status.put("status", "UP");
                status.put("message", "Kafka bağlantısı aktif");
            } else {
                status.put("status", "DOWN");
                status.put("message", "Kafka bağlantısı başarısız");
            }
        } catch (Exception e) {
            status.put("status", "ERROR");
            status.put("message", "Kafka bağlantısı test edilirken hata: " + e.getMessage());
        }

        return ResponseEntity.ok(status);
    }

    /**
     * Servisin sağlık durumunu kontrol eder
     * 
     * @return Durum bilgisi
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("service", "analytics-service");
        status.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.ok(status);
    }
}