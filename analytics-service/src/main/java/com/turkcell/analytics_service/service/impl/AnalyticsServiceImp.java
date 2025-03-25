package com.turkcell.analytics_service.service.impl;

import org.springframework.stereotype.Service;

import com.turkcell.analytics_service.entity.ContractAnalytics;
import com.turkcell.analytics_service.entity.CustomerAnalytics;
import com.turkcell.analytics_service.entity.PlanAnalytics;
import com.turkcell.analytics_service.repository.ContractAnalyticsRepository;
import com.turkcell.analytics_service.repository.CustomerAnalyticsRepository;
import com.turkcell.analytics_service.repository.PlanAnalyticsRepository;
import com.turkcell.analytics_service.service.AnalyticsService;

import io.github.ergulberke.event.contract.ContractCreatedEvent;
import io.github.ergulberke.event.customer.CustomerCreatedEvent;
import io.github.ergulberke.event.plan.PlanCreatedEvent;

@Service
public class AnalyticsServiceImp implements AnalyticsService {

    private final PlanAnalyticsRepository planAnalyticsRepository;
    private final CustomerAnalyticsRepository customerAnalyticsRepository;
    private final ContractAnalyticsRepository contractAnalyticsRepository;

    public AnalyticsServiceImp(PlanAnalyticsRepository planAnalyticsRepository,
            CustomerAnalyticsRepository customerAnalyticsRepository,
            ContractAnalyticsRepository contractAnalyticsRepository) {
        this.planAnalyticsRepository = planAnalyticsRepository;
        this.customerAnalyticsRepository = customerAnalyticsRepository;
        this.contractAnalyticsRepository = contractAnalyticsRepository;
    }

    @Override
    public void savePlanAnalytics(PlanCreatedEvent event) {
        PlanAnalytics analytics = new PlanAnalytics();
        analytics.setPlanId((event.getPlanId()));
        analytics.setPlanName(event.getPlanName());
        analytics.setPrice(event.getPrice());
        analytics.setCurrency(event.getCurrency());
        analytics.setDurationInMonths(event.getDurationInMonths());
        analytics.setFeatures(event.getFeatures());
        analytics.setEventType(event.getEventType());
        analytics.setCreatedAt(event.getTimestamp()); // Timestamp olarak kaydediyoruz
        planAnalyticsRepository.save(analytics);
        System.out.println("Plan verisi analitik veritabanına kaydedildi: " + analytics);
    }

    @Override
    public void saveCustomerAnalytics(CustomerCreatedEvent event) {
        CustomerAnalytics customerAnalytics = new CustomerAnalytics();
        customerAnalytics.setCustomerId((event.getCustomerId()));
        customerAnalytics.setFirstName(event.getFirstName());
        customerAnalytics.setLastName(event.getLastName());
        customerAnalytics.setEmail(event.getEmail());
        customerAnalytics.setPhoneNumber(event.getPhone());
        customerAnalytics.setAddress(event.getAddress());
        customerAnalytics.setCustomerStatus(event.getStatus());
        customerAnalytics.setEventType(event.getEventType());
        customerAnalytics.setCreatedAt(event.getTimestamp());

        customerAnalyticsRepository.save(customerAnalytics);
        System.out.println("CustomerAnalytics verisi kaydedildi: " + customerAnalytics);
    }

    @Override
    public void saveContractAnalytics(ContractCreatedEvent event) {
        ContractAnalytics contractAnalytics = new ContractAnalytics();
        contractAnalytics.setContractId(event.getContractId());
        contractAnalytics.setCustomerId(event.getCustomerId());
        contractAnalytics.setPlanId(event.getPlanId());
        contractAnalytics.setStartDate(event.getStartDate());
        contractAnalytics.setEndDate(event.getEndDate());
        contractAnalytics.setPrice(event.getPrice());
        contractAnalytics.setStatus(event.getStatus());
        contractAnalytics.setCreatedAt(event.getTimestamp()); // Created at timestamp

        contractAnalyticsRepository.save(contractAnalytics);
        System.out.println("ContractAnalytics verisi kaydedildi: " + contractAnalytics);
    }

}
