package com.turkcell.customer_service.enums;

public enum AccountStatus {
    ACTIVE,      // Kullanıcı aktif
    SUSPENDED,   // Geçici olarak askıya alınmış
    CLOSED,      // Tamamen kapatılmış
    PENDING,     // Aktivasyon bekleniyor
    BLACKLISTED  // Kara listede
}
