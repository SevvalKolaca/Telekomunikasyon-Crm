package com.turkcell.notification_service.repository;

import com.netflix.spectator.impl.PatternExpr;
import com.turkcell.notification_service.entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface NotificationRepository extends MongoRepository <Notification, String> {


    Optional<Notification> findByid(String id);  // test amaçlı sonrasında silicem





}
