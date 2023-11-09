package com.shadril.patientservice.networkmanager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class FeignClientConfiguration {
    @Bean
    public TokenInterceptor customTokenInterceptor() {
        log.info("Configuring custom TokenInterceptor for Feign clients");
        return new TokenInterceptor();
    }
}