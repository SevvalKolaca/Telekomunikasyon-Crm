package com.turkcell.planservice.dto.request;

import com.turkcell.planservice.entity.PlanStatus;
import com.turkcell.planservice.entity.PlanType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePlanRequest {
    
    @NotNull(message = "Plan ID boş olamaz")
    private UUID id;
    
    private String name;
    
    @Min(value = 0, message = "Fiyat negatif olamaz")
    private BigDecimal price;
    
    private PlanType type;
    
    private PlanStatus status;
    
    private Integer smsLimit;
    
    private Integer internetLimit;
    
    private Integer voiceLimit;
} 