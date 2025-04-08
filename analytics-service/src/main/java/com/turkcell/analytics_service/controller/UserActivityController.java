package com.turkcell.analytics_service.controller;

import com.turkcell.analytics_service.dto.UserActivityDto;
import com.turkcell.analytics_service.entity.UserActivity;
import com.turkcell.analytics_service.enums.ActivityType;
import com.turkcell.analytics_service.service.UserActivityService;
import io.github.ergulberke.event.customer.CustomerCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user-activities")
@RequiredArgsConstructor
public class UserActivityController {

    private final UserActivityService userActivityService;

    // Endpoint to log user activity
    @PostMapping("/log")
    @ResponseStatus(HttpStatus.CREATED)
    public void logActivity(@RequestBody UserActivityDto activityDto) {
        userActivityService.logActivity(activityDto);
    }

    // Endpoint to log customer created activity
    @PostMapping("/log-customer-created")
    @ResponseStatus(HttpStatus.CREATED)
    public void logCustomerCreatedActivity(@RequestBody CustomerCreatedEvent event) {
        userActivityService.logCustomerCreatedActivity(event);
    }

    // Endpoint to get all activities
    @GetMapping
    public List<UserActivity> getAllActivities() {
        return userActivityService.getAllActivities();
    }

    // Endpoint to get activities by date range
    @GetMapping("/by-date-range")
    public List<UserActivity> getActivitiesByDateRange(
            @RequestParam("start") String start,
            @RequestParam("end") String end) {
        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);
        return userActivityService.getActivitiesByDateRange(startDate, endDate);
    }

    // Endpoint to get activities by user ID
    @GetMapping("/by-user-id")
    public List<UserActivity> getActivitiesByUserId(@RequestParam UUID userId) {
        return userActivityService.getActivitiesByUserId(userId);
    }

    // Endpoint to get activities by activity type
    @GetMapping("/by-activity-type")
    public List<UserActivity> getActivitiesByActivityType(@RequestParam ActivityType activityType) {
        return userActivityService.getActivitiesByActivityType(activityType);
    }

    // Endpoint to get activities by IP address
    @GetMapping("/by-ip-address")
    public List<UserActivity> getActivitiesByIpAddress(@RequestParam String ipAddress) {
        return userActivityService.getActivitiesByIpAddress(ipAddress);
    }
}

