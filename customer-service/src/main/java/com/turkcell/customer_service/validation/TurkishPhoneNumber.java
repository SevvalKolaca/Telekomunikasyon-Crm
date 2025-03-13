package com.turkcell.customer_service.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TurkishPhoneNumberValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface TurkishPhoneNumber {
    String message() default "Geçerli bir Türkiye telefon numarası olmalıdır (örn: 05xxxxxxxxx)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
