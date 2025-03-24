package com.turkcell.planservice.dto.request;

import com.turkcell.planservice.entity.PlanType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePlanRequest {
    
    @NotBlank(message = "Plan adı boş olamaz")
    private String name;
    
    @NotNull(message = "Fiyat boş olamaz")
    @Min(value = 0, message = "Fiyat negatif olamaz")
    private BigDecimal price;
    
    @NotNull(message = "Tür boş olamaz")
    private PlanType type;
    
    private Integer smsLimit;
    
    private Integer internetLimit;
    
    private Integer voiceLimit;
} 