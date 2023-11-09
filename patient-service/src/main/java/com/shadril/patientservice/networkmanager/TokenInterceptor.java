package com.shadril.patientservice.networkmanager;

import com.shadril.patientservice.constants.AppConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Component
@Slf4j
public class TokenInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        String jwtToken = retrieveJwtToken();

        if (jwtToken != null && !jwtToken.isEmpty()) {
            requestTemplate.header(AppConstants.HEADER_STRING, AppConstants.TOKEN_PREFIX + jwtToken);
        }
    }
    private String retrieveJwtToken() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        return request.getHeader(AppConstants.HEADER_STRING);
    }
}
