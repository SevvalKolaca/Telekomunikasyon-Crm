package com.turkcell.analytics_service.service.impl;

import com.turkcell.analytics_service.dto.SubscriptionAnalyticsDTO;
import com.turkcell.analytics_service.dto.SubscriptionEventDTO;
import com.turkcell.analytics_service.service.SubscriptionAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionAnalyticsServiceImpl implements SubscriptionAnalyticsService {

    private final SubscriptionAnalyticsRepository subscriptionAnalyticsRepository;

}
