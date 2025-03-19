package com.turkcell.notification_service.controllers;

import com.turkcell.notification_service.entity.Notification;
import com.turkcell.notification_service.repository.NotificationRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notification")
public class NotificationController {

    public NotificationController(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    private final NotificationRepository notificationRepository;

    @GetMapping
    public String get(){
        return "Notification Service";
    }


    @PostMapping
    public void add(@RequestBody Notification notification){  // dto olmalı çevrilcek
        this.notificationRepository.save(notification);    // servise gitmeli

    }




}
