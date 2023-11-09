package com.shadril.patientservice.networkmanager;
import com.shadril.patientservice.constants.AppConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

@Component
@Slf4j
public class TokenInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        String jwtToken = retrieveJwtToken();
        if (jwtToken != null && !jwtToken.isEmpty()) {
            requestTemplate.header(AppConstants.HEADER_STRING, AppConstants.TOKEN_PREFIX + jwtToken);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization token is missing or invalid");
        }
    }

    private String retrieveJwtToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        return request.getHeader(AppConstants.HEADER_STRING);
    }
}
