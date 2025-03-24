package com.turkcell.planservice.enums;

public enum PlanType {
    PREPAID("Ön Ödemeli"),
    POSTPAID("Fatura"),
    HYBRID("Hibrit");

    private final String description;

    PlanType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
} 