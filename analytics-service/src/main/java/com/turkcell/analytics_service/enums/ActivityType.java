package com.turkcell.analytics_service.enums;

public enum ActivityType {
    // User related activity types
    USER_CREATED,
    USER_UPDATED,
    USER_DELETED,
    USER_LOGIN,
    USER_LOGOUT,

    // User status related activity types
    USER_ACTIVATED,
    USER_SUSPENDED,
    USER_CLOSED,
    USER_PENDING,
    USER_BLACKLISTED,

    // Subscription related activity types
    SUBSCRIPTION_CREATED,
    SUBSCRIPTION_RENEWED,
    SUBSCRIPTION_UPGRADED,
    SUBSCRIPTION_DOWNGRADED,
    SUBSCRIPTION_CANCELLED,

    // Billing related activity types
    INVOICE_CREATED,
    PAYMENT_RECEIVED,
    PAYMENT_FAILED,

    // Plan related activity types
    PLAN_CHANGED,

    // Customer related activity types
    CUSTOMER_CREATED,
    CUSTOMER_UPDATED,
    CUSTOMER_DELETED,

    // Support related activity types
    SUPPORT_TICKET_CREATED,
    SUPPORT_TICKET_RESOLVED
}
