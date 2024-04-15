package com.gmail.feign;

import com.gmail.configuration.FeignConfiguration;
import com.gmail.dto.request.NotificationRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.gmail.constants.FeignConstants.NOTIFICATION_SERVICE;
import static com.gmail.constants.PathConstants.API_V1_NOTIFICATION;

@CircuitBreaker(name = NOTIFICATION_SERVICE)
@FeignClient(value = NOTIFICATION_SERVICE, path = API_V1_NOTIFICATION, configuration = FeignConfiguration.class)
public interface NotificationClient {

    @PostMapping
    void sendNotification(@RequestBody NotificationRequest request);
}
