package com.turkcell.planservice.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.turkcell.planservice.config.TestSecurityConfig;
import com.turkcell.planservice.repository.PlanRepository;
import com.turkcell.planservice.service.PlanService;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class PlanIntegrationTest {

    @Autowired
    private PlanService planService;

    @Autowired
    private PlanRepository planRepository;

    @Test
    void contextLoads() {
        assertNotNull(planService);
        assertNotNull(planRepository);
    }
} 