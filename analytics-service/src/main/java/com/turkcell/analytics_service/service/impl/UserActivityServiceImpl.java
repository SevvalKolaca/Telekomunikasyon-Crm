package com.turkcell.analytics_service.service.impl;

import com.turkcell.analytics_service.dto.UserActivityDTO;
import com.turkcell.analytics_service.repository.UserActivityRepository;
import com.turkcell.analytics_service.service.UserActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserActivityServiceImpl implements UserActivityService {

    private final UserActivityRepository userActivityRepository;

}
