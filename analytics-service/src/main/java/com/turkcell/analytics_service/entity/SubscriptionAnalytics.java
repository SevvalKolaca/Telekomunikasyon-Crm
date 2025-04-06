package com.turkcell.analytics_service.entity;

import io.github.ergulberke.enums.PlanStatus;
import io.github.ergulberke.enums.PlanType;
import io.github.ergulberke.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "subscription_analytics")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionAnalytics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Genel Plan Bilgileri
    @Column(name = "plan_id")
    private UUID planId;

    @Column(name = "plan_name")
    private String planName;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private PlanType type;

    @Column(name = "status")
    private String status; // Object tipinden String'e değiştirildi

    @Column(name = "billing_cycle")
    private String billingCycle; // Fatura döngüsü: aylık/yıllık

    // İhtiyaç olursa ayrı olarak enum statüsleri
    @Transient
    private PlanStatus planStatus;

    @Transient
    private AccountStatus accountStatus;

    // Limit Bilgileri (Plan bazlı)
    @Column(name = "sms_limit")
    private Integer smsLimit;

    @Column(name = "internet_limit")
    private Integer internetLimit;

    @Column(name = "voice_limit")
    private Integer voiceLimit;

    // Analitik Veriler
    @Column(name = "new_subscriptions")
    private Integer newSubscriptions;

    @Column(name = "canceled_subscriptions")
    private Integer canceledSubscriptions;

    @Column(name = "revenue", precision = 10, scale = 2)
    private BigDecimal revenue;

    // Tarihler
    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    // Eğer createdAt gibi zaman damgası istiyorsan:
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // PlanStatus veya AccountStatus tipini status alanına aktarmak için yardımcı
    // metodlar
    public void setPlanStatus(PlanStatus planStatus) {
        this.planStatus = planStatus;
        this.status = planStatus.name();
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
        this.status = accountStatus.name();
    }
}
