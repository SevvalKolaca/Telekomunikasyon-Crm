package com.turkcell.planservice.repository;

import com.turkcell.planservice.entity.Plan;
import io.github.ergulberke.enums.PlanStatus;
import io.github.ergulberke.enums.PlanType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlanRepository extends JpaRepository<Plan, UUID> {

    List<Plan> findByStatus(PlanStatus status);

    List<Plan> findByType(PlanType type);

    List<Plan> findByTypeAndStatus(PlanType type, PlanStatus status);
}