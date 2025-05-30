package com.turkcell.customer_service.service;

import com.turkcell.customer_service.client.PlanServiceClient;
import com.turkcell.customer_service.dto.CustomerRequest;
import com.turkcell.customer_service.dto.CustomerResponse;
import com.turkcell.customer_service.dto.Plan.PlanResponse;
import com.turkcell.customer_service.entity.Customer;
import com.turkcell.customer_service.repository.CustomerRepository;
import com.turkcell.customer_service.rules.CustomerBusinnessRules;
import com.turkcell.customer_service.client.ContractServiceClient;
import io.github.ergulberke.event.customer.CustomerCreatedEvent;
import com.turkcell.customer_service.dto.Contract.CreateContractRequest;
import com.turkcell.customer_service.dto.Contract.GetContractResponse;
import com.turkcell.customer_service.dto.Contract.UpdateContractRequest;
import com.turkcell.customer_service.dto.Contract.CancelContractRequest;
import com.turkcell.customer_service.enums.BillingPeriod;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerBusinnessRules rules;
    private final CustomerProducer customerProducer;
    private final PlanServiceClient planServiceClient;
    private final ContractServiceClient contractServiceClient;


    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {
        rules.checkIfEmailExists(request.getEmail());
        rules.validatePhoneNumberIfPresent(request.getPhone());

        Customer customer = buildCustomerFromRequest(request);
        Customer savedCustomer = customerRepository.save(customer);

        // Create CustomerCreatedEvent
        CustomerCreatedEvent event = CustomerCreatedEvent.builder()
                .customerId(savedCustomer.getId())
                .firstName(savedCustomer.getFirstName())
                .lastName(savedCustomer.getLastName())
                .email(savedCustomer.getEmail())
                .phone(savedCustomer.getPhone())
                .address(savedCustomer.getAddress())
                .status(savedCustomer.getAccountStatus().toString())
                .eventType("CREATE")
                .timestamp(LocalDateTime.now())
                .build();

        // Send event to Kafka
        customerProducer.sendCustomerCreatedEvent(event);

        // Plan bilgilerini al
        PlanResponse planResponse = planServiceClient.getPlanById(request.getPlanId()).getBody();
        if (planResponse == null) {
            throw new RuntimeException("Plan not found with ID: " + request.getPlanId());
        }

        // Otomatik olarak sözleşme oluştur
        CreateContractRequest contractRequest = new CreateContractRequest();
        contractRequest.setContractNumber("CNT-" + System.currentTimeMillis());
        contractRequest.setStartDate(LocalDateTime.now());
        contractRequest.setEndDate(LocalDateTime.now().plusYears(1)); // Varsayılan olarak 1 yıllık sözleşme
        contractRequest.setCustomerId(savedCustomer.getId());
        contractRequest.setPlanId(request.getPlanId());
        contractRequest.setBillingPeriod(request.getBillingPeriod());
        contractRequest.setPrice(planResponse.getPrice().doubleValue());
        
        contractServiceClient.add(contractRequest);
        return buildCustomerResponse(savedCustomer);
    }

    @Override
    public CustomerResponse getCustomerById(UUID id) {
        Customer customer = rules.findCustomerByIdOrThrow(id);
        return buildCustomerResponse(customer);
    }

    @Override
    public CustomerResponse getCustomerByEmail(String email) {
        Customer customer = rules.findCustomerByEmailOrThrow(email);
        return buildCustomerResponse(customer);
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::buildCustomerResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerResponse updateCustomer(UUID id, CustomerRequest request) {
        Customer existingCustomer = rules.findCustomerByIdOrThrow(id);

        rules.checkIfEmailExistsForUpdate(existingCustomer.getEmail(), request.getEmail());

        rules.validatePhoneNumberIfPresent(request.getPhone());

        existingCustomer.setFirstName(request.getFirstName());
        existingCustomer.setLastName(request.getLastName());
        existingCustomer.setEmail(request.getEmail());
        existingCustomer.setPhone(request.getPhone());
        existingCustomer.setAddress(request.getAddress());
        existingCustomer.setAddress((request.getAddress()));
        // existingCustomer.getAccountStatus(request.getAccountStatus());

        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return buildCustomerResponse(updatedCustomer);
    }

    @Override
    public void deleteCustomer(UUID id) {
        rules.checkIfCustomerExistsById(id);
        customerRepository.deleteById(id);
    }

    @Override
    @Transactional
    public CustomerResponse changeCustomerPlan(UUID customerId, UUID newPlanId, String reason) {
        // Müşteriyi bul
        Customer customer = rules.findCustomerByIdOrThrow(customerId);

        // Plan Service'den yeni planın detaylarını al
        PlanResponse newPlan = planServiceClient.getPlanById(newPlanId).getBody();
        if (newPlan == null) {
            throw new RuntimeException("Plan not found with ID: " + newPlanId);
        }

        // Plan'ın aktif olup olmadığını kontrol et
        if (!planServiceClient.isPlanActive(newPlanId).getBody()) {
            throw new RuntimeException("Plan is not active: " + newPlanId);
        }

        // Eski plan bilgilerini sakla
        UUID oldPlanId = customer.getPlanId();

        // Customer entity'sini güncelle
        customer.setPlanId(newPlanId);
        customer.setPlanStartDate(LocalDate.now());
        customer.setPlanEndDate(LocalDate.now().plusMonths(newPlan.getDurationInMonths()));

        Customer updatedCustomer = customerRepository.save(customer);
        return buildCustomerResponse(updatedCustomer);
    }


    @Override
    public List<GetContractResponse> getByCustomerId(String customerId) {
        return contractServiceClient.getByCustomerId(customerId);
    }

    @Override
    public GetContractResponse getById(UUID contractId) {
        return contractServiceClient.getById(contractId);
    }

    @Override
    public void add(String customerId, UUID planId, BillingPeriod billingPeriod, LocalDateTime startDate, LocalDateTime endDate) {
        // Plan bilgilerini al
        PlanResponse planResponse = planServiceClient.getPlanById(planId).getBody();
        if (planResponse == null) {
            throw new RuntimeException("Plan not found with ID: " + planId);
        }

        CreateContractRequest request = new CreateContractRequest();
        request.setContractNumber("CNT-" + System.currentTimeMillis());
        request.setCustomerId(UUID.fromString(customerId));
        request.setPlanId(planId);
        request.setBillingPeriod(billingPeriod);
        request.setPrice(planResponse.getPrice().doubleValue());
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        contractServiceClient.add(request);
    }

    @Override
    public void update(UUID contractId, LocalDateTime endDate, BillingPeriod billingPeriod) {
        UpdateContractRequest request = new UpdateContractRequest();
        request.setEndDate(endDate);
        request.setBillingPeriod(billingPeriod);
        contractServiceClient.update(contractId, request);
    }

    @Override
    public void cancelContract(UUID contractId, String reason) {
        CancelContractRequest request = new CancelContractRequest();
        request.setReason(reason);
        contractServiceClient.cancelContract(contractId, request);
    }

    @Override
    public void suspendContract(UUID contractId) {
        contractServiceClient.suspendContract(contractId);
    }

    @Override
    public void reactivateContract(UUID contractId) {
        contractServiceClient.reactivateContract(contractId);
    }

    private Customer buildCustomerFromRequest(CustomerRequest request) {
        return Customer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .planId(request.getPlanId())
                .planStartDate(request.getPlanStartDate())
                .planEndDate(request.getPlanEndDate())
                .billingPeriod(request.getBillingPeriod())
                .build();
    }

    private CustomerResponse buildCustomerResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .accountStatus(customer.getAccountStatus())
                .planId(customer.getPlanId())
                .planStartDate(customer.getPlanStartDate())
                .planEndDate(customer.getPlanEndDate())
                .billingPeriod(customer.getBillingPeriod())
                .build();
    }
}
