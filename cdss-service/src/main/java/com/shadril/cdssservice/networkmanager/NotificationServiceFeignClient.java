package com.shadril.cdssservice.networkmanager;

import com.shadril.cdssservice.dto.NotificationDto;
import com.shadril.cdssservice.dto.ResponseMessageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service", configuration = FeignClientConfiguration.class)
public interface NotificationServiceFeignClient {
    @PostMapping("/notifications/create")
    ResponseEntity<ResponseMessageDto> createNotification(@RequestBody NotificationDto notificationDto);
}
