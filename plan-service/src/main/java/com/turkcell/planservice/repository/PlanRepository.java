package com.turkcell.planservice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turkcell.planservice.entity.Plan;
import com.turkcell.planservice.enums.PlanStatus;
import com.turkcell.planservice.enums.PlanType;

@Repository
public interface PlanRepository extends JpaRepository<Plan, UUID> {
    
    List<Plan> findByStatus(PlanStatus status);
    
    List<Plan> findByType(PlanType type);
    
    List<Plan> findByTypeAndStatus(PlanType type, PlanStatus status);
    
    boolean existsByName(String name);
    
    List<Plan> findByName(String name);
} 