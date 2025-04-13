package com.turkcell.customer_service.entity;

import com.turkcell.customer_service.enums.AccountStatus;
import com.turkcell.customer_service.enums.BillingPeriod;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="customers")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name="first_name",nullable = false)
    private String firstName;

    @Column(name = "last_name",nullable = false)
    private String lastName;

    @Column(unique = true,nullable = false)
    private String email;

    private String phone;

    private String address;

    @Column(name = "account_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "plan_id")
    private UUID planId;

    @Column(name = "plan_start_date")
    private LocalDate planStartDate;

    @Column(name = "plan_end_date")
    private LocalDate planEndDate;

    @Column(name = "billing_period")
    @Enumerated(EnumType.STRING)
    private BillingPeriod billingPeriod;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (accountStatus == null) { // Eğer dışarıdan bir değer atanmadıysa
            accountStatus = AccountStatus.ACTIVE; //varsıyal olarak ACTIVE başlasın
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // TODO: müşteri kayıtlarını tamamen silmesek?? elimizde eski müşterilerin
    //  kayıtlarını tutmak mantıklı olabilir bu şekilde durum ataması ile kontrol sağlayabiliriz
    @PreRemove
    protected void onRemove() {
        accountStatus = AccountStatus.CLOSED; // Müşteri silinmeden önce durum CLOSED olarak değişir
    }
}
