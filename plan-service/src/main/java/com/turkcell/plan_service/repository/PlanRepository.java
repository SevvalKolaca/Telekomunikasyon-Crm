package com.turkcell.plan_service.repository;

import com.turkcell.plan_service.entity.Plan;
import com.turkcell.plan_service.entity.PlanType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlanRepository extends JpaRepository<Plan, UUID> {
    List<Plan> findByPlanType(PlanType planType);
    List<Plan> findByIsActiveTrue();
    List<Plan> findByPlanTypeAndIsActiveTrue(PlanType planType);
} 