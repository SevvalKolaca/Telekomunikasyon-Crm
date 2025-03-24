package com.turkcell.analytics_service.repository;

import com.turkcell.analytics_service.entity.ContractAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContractAnalyticsRepository extends JpaRepository<ContractAnalytics, UUID> {

}