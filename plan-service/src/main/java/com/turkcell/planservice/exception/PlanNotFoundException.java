package com.turkcell.planservice.exception;

import java.util.UUID;

public class PlanNotFoundException extends PlanBusinessException {

    public PlanNotFoundException(String message) {
        super(message);
    }

    public PlanNotFoundException(UUID id) {
        super("Plan bulunamadı: " + id);
    }
} 