package com.turkcell.analytics_service.enums;

public enum ActivityType {
    // Genel aktiviteler
    LOGIN,
    LOGOUT,
    PAGE_VIEW,
    SEARCH,

    // Kullanıcı aktiviteleri
    USER_CREATED,
    USER_UPDATED,
    USER_DELETED,
    USER_LOGIN,
    USER_LOGOUT,

    // Abonelik aktiviteleri
    SUBSCRIPTION_CREATED,
    SUBSCRIPTION_RENEWED,
    SUBSCRIPTION_UPGRADED,
    SUBSCRIPTION_DOWNGRADED,
    SUBSCRIPTION_CANCELLED,
    SUBSCRIPTION_SUSPENDED,

    // Fatura aktiviteleri
    INVOICE_CREATED,
    INVOICE_PAID,
    INVOICE_OVERDUE,
    INVOICE_CANCELLED
}
