package io.github.ergulberke.enums;

public enum PlanStatus {
    ACTIVE("Aktif"),
    INACTIVE("Pasif"),
    DEPRECATED("Kullanımdan Kaldırıldı");

    private final String description;

    PlanStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}