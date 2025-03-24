package com.turkcell.analytics_service.repository;

import com.turkcell.analytics_service.entity.PlanAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PlanAnalyticsRepository extends JpaRepository<PlanAnalytics, UUID> {

}
