package com.turkcell.customer_service.service;

import com.turkcell.contractservice.dtos.requests.CancelContractRequest;
import com.turkcell.contractservice.dtos.requests.CreateContractRequest;
import com.turkcell.contractservice.dtos.requests.UpdateContractRequest;
import com.turkcell.customer_service.client.PlanServiceClient;
import com.turkcell.customer_service.dto.CustomerRequest;
import com.turkcell.customer_service.dto.CustomerResponse;
import com.turkcell.customer_service.dto.Plan.PlanResponse;
import com.turkcell.customer_service.entity.Customer;
import com.turkcell.customer_service.repository.CustomerRepository;
import com.turkcell.customer_service.rules.CustomerBusinnessRules;
import com.turkcell.contractservice.dtos.responses.GetContractResponse;
import com.turkcell.contractservice.entities.enums.BillingPlan;
import com.turkcell.customer_service.client.ContractServiceClient;
import io.github.ergulberke.event.customer.CustomerCreatedEvent;
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

        // Otomatik olarak sözleşme oluştur
        CreateContractRequest contractRequest = new CreateContractRequest();
        contractRequest.setContractNumber("CNT-" + System.currentTimeMillis()); // Benzersiz bir sözleşme numarası oluştur
        contractRequest.setStartDate(LocalDateTime.now());
        contractRequest.setEndDate(LocalDateTime.now().plusYears(1)); // Varsayılan olarak 1 yıllık sözleşme
        contractRequest.setCustomerId(savedCustomer.getId().toString());
        contractRequest.setBillingPlan(request.getBillingPlan());
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
        String oldPlanName = customer.getPlanName();

        // Customer entity'sini güncelle
        customer.setPlanId(newPlanId);
        customer.setPlanName(newPlan.getName());
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
    public void add(String customerId, BillingPlan planId, LocalDateTime startDate, LocalDateTime endDate) {
        CreateContractRequest request = new CreateContractRequest();
        request.setCustomerId(customerId);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setBillingPlan(planId);
        contractServiceClient.add(request);
    }

    @Override
    public void update(UUID contractId, LocalDateTime endDate, BillingPlan planId) {
        UpdateContractRequest request = new UpdateContractRequest();
        request.setEndDate(endDate);
        request.setBillingPlan(planId);
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
                .planName(request.getPlanName())
                .planStartDate(request.getPlanStartDate())
                .planEndDate(request.getPlanEndDate())
                .billingPlan(request.getBillingPlan())
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
                .planName(customer.getPlanName())
                .planStartDate(customer.getPlanStartDate())
                .planEndDate(customer.getPlanEndDate())
                .billingPlan(customer.getBillingPlan())
                .build();
    }
}
