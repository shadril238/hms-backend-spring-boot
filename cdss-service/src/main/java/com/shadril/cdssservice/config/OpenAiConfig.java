package com.shadril.cdssservice.config;

import com.shadril.cdssservice.constants.OpenAiConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OpenAiConfig {
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + OpenAiConstants.API_KEY);
            return execution.execute(request, body);
        });
        return restTemplate;
    }
}
