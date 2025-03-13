package com.turkcell.customer_service.rules;

import com.turkcell.customer_service.entity.Customer;
import com.turkcell.customer_service.repository.CustomerRepository;
import io.github.ergulberke.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class CustomerBusinnessRules {

    private final CustomerRepository customerRepository;

    // Regex for Turkish phone numbers (05xx xxx xx xx format)
    private static final Pattern TURKISH_PHONE_PATTERN = Pattern.compile("^(05)([0-9]{9})$");

    public void checkIfEmailExists(String email) {
        if (customerRepository.existsByEmail(email)) {
            throw new BusinessException("Email already exists: " + email);
        }
    }

    public void checkIfEmailExistsForUpdate(String currentEmail, String newEmail) {
        if (!currentEmail.equals(newEmail) && customerRepository.existsByEmail(newEmail)) {
            throw new BusinessException("Email already exists: " + newEmail);
        }
    }

    public void checkIfCustomerExistsById(UUID id) {
        if (!customerRepository.existsById(id)) {
            throw new BusinessException("Customer not found with id: " + id);
        }
    }

    public void checkIfCustomerNotExistsById(UUID id) {
        // This is used when we need to make sure a customer with given ID does NOT
        // exist
        if (customerRepository.existsById(id)) {
            throw new BusinessException("Customer already exists with id: " + id);
        }
    }

    public Customer findCustomerByIdOrThrow(UUID id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Customer not found with id: " + id));
    }

    public Customer findCustomerByEmailOrThrow(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Customer not found with email: " + email));
    }

    public void validatePhoneNumberIfPresent(String phone) {
        // If phone is null or empty, it's valid
        if (phone == null || phone.trim().isEmpty()) {
            return;
        }

        // Remove any spaces, dashes, parentheses that might be in the number
        String cleaned = phone.replaceAll("[\\s\\-()]", "");

        // Check if it matches the Turkish phone number pattern
        if (!TURKISH_PHONE_PATTERN.matcher(cleaned).matches()) {
            throw new BusinessException("Invalid Turkish phone number format. Should be like: 05xxxxxxxxx");
        }
    }

}
