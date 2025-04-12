package com.turkcell.customer_service.enums;

import lombok.Getter;

@Getter
public enum BillingPlan {
    MONTHLY_BASIC(1, "Aylık Temel Paket", 100.0, 1, 0),
    MONTHLY_PREMIUM(2, "Aylık Premium Paket", 200.0, 1, 0),

    QUARTERLY_BASIC(3, "3 Aylık Temel Paket", 270.0, 3, 10),
    QUARTERLY_PREMIUM(4, "3 Aylık Premium Paket", 540.0, 3, 10),

    SEMI_ANNUAL_BASIC(5, "6 Aylık Temel Paket", 480.0, 6, 20),
    SEMI_ANNUAL_PREMIUM(6, "6 Aylık Premium Paket", 960.0, 6, 20),

    ANNUAL_BASIC(7, "Yıllık Temel Paket", 840.0, 12, 30),
    ANNUAL_PREMIUM(8, "Yıllık Premium Paket", 1680.0, 12, 30);

    private final int id;
    private final String description;
    private final double basePrice;
    private final int duration; // Ay cinsinden süre
    private final double discountPercentage;

    private BillingPlan(int id, String description, double basePrice, int duration, double discountPercentage) {
        this.id = id;
        this.description = description;
        this.basePrice = basePrice;
        this.duration = duration;
        this.discountPercentage = discountPercentage;
    }

    public double calculateMonthlyFee() {
        return (basePrice * (1 - discountPercentage / 100.0)) / duration;
    }
}
