package com.shadril.patientservice.networkmanager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shadril.patientservice.dtos.ResponseMessageDto;
import com.shadril.patientservice.exceptions.CustomException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomFeignErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        String errorDetails = extractBody(response);
        String errorMessage = "Unknown error";

        if (response.body() != null) {
            try {
                Map<String, Object> errorAttributes = objectMapper.readValue(errorDetails, Map.class);
                errorMessage = (String) errorAttributes.get("message");
            } catch (JsonProcessingException e) {
                errorMessage = "Error occurred while parsing the error response body";
            }
        }

        if (response.status() >= 400 && response.status() < 500) {
            return new CustomException(
                    new ResponseMessageDto(errorMessage, HttpStatus.valueOf(response.status())),
                    HttpStatus.valueOf(response.status()));
        } else if (response.status() >= 500 && response.status() < 600) {
            return new CustomException(
                    new ResponseMessageDto(errorMessage, HttpStatus.valueOf(response.status())),
                    HttpStatus.valueOf(response.status()));
        }
        return new Exception(errorMessage);
    }

    private String extractBody(Response response) {
        if (response.body() != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().asInputStream(), StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining("\n"));
            } catch (IOException e) {
                return "Error occurred while reading the error response body";
            }
        }
        return "No response body";
    }
}