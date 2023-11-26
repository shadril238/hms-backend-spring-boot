package com.shadril.doctorservice.networkmanager;

import com.shadril.doctorservice.dto.NotificationDto;
import com.shadril.doctorservice.dto.ResponseMessageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service", configuration = FeignClientConfiguration.class)
public interface NotificationServiceFeignClient {
    @PostMapping("/notifications/create")
    ResponseEntity<ResponseMessageDto> createNotification(@RequestBody NotificationDto notificationDto);
}
