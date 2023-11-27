package com.shadril.analyticresearchservice.constants;

public class AppConstants {
    public static final String TOKEN_SECRET = "9eac628c872d1fb3d806a82e95df1841d70e18dc96b664bff865d0bbe3e55ec1595b2f1672cf947c86541b22daef211021831091b8b5b6a4f2561f6e2e319bdb";
    public static final long EXPIRATION_TIME = 864000000; //10 days

    public static final String SIGN_IN = "/users/login";
    public static final String SIGN_UP = "/users/register";
    public static final String HEADER_STRING = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String ROLE_ADMIN = "Admin";
    public static final String ROLE_PATIENT = "Patient";
    public static final String ROLE_DOCTOR = "Doctor";
    public static final String USER_UNAUTHORIZED = "You are not authorized to access this!";
    public static final String USER_NOTFOUND = "User does not exist!";
    public static final String TOKEN_INVALID = "Token is invalid!";
}