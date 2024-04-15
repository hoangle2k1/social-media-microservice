package com.gmail.feign;

import com.gmail.configuration.FeignConfiguration;
import com.gmail.constants.FeignConstants;
import com.gmail.constants.PathConstants;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@CircuitBreaker(name = FeignConstants.IMAGE_SERVICE)
@FeignClient(name = FeignConstants.IMAGE_SERVICE, path = PathConstants.API_V1_IMAGE, configuration = FeignConfiguration.class)
public interface ImageClient {

    @PostMapping(PathConstants.UPLOAD)
    String uploadImage(@RequestPart("file") MultipartFile file);
}
