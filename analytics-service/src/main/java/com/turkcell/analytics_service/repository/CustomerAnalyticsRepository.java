package com.turkcell.analytics_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.turkcell.analytics_service.entity.CustomerAnalytics;
import java.util.UUID;

public interface CustomerAnalyticsRepository extends JpaRepository<CustomerAnalytics, UUID> {

}
