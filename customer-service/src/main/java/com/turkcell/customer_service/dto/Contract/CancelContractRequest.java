package com.turkcell.customer_service.dto.Contract;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancelContractRequest {
    @NotBlank(message = "İptal sebebi boş olamaz")
    private String reason;
}
