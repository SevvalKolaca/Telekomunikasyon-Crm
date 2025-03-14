package io.github.ergulberke.model;

public enum Role {
    ADMIN, CUSTOMER_SERVICE, TECH_SUPPORT;

    public String authority() {
        return "ROLE_" + this.name(); // "ADMIN" -> "ROLE_ADMIN"
    }
}

