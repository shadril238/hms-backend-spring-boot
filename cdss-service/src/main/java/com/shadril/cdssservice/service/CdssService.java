package com.shadril.cdssservice.service;

import com.shadril.cdssservice.exception.CustomException;

public interface CdssService {
    String getResponseFromOpenAi(String prompt) throws CustomException;
}
