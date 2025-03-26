package com.turkcell.analytics_service.service.impl;

import com.turkcell.analytics_service.dto.UserActivityDto;
import com.turkcell.analytics_service.service.UserActivityService;

import io.github.ergulberke.event.customer.CustomerCreatedEvent;

import org.springframework.stereotype.Service;

@Service
public class UserActivityServiceImpl implements UserActivityService {

    @Override
    public void logActivity(UserActivityDto activityDto) {
        // Kullanıcı aktivitelerini kaydetme işlemi
        System.out.println("Logging user activity: " + activityDto);
        // Burada, activityDto verisini veritabanına kaydedebilirsiniz.
        // Örneğin, userActivityRepository.save(activityDto);
    }

    @Override
    public void logCustomerCreatedActivity(CustomerCreatedEvent event) {
    }

}
