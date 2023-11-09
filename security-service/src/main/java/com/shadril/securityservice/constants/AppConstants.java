package com.shadril.securityservice.constants;

public class AppConstants {
    public static final long EXPIRATION_TIME = 864000000; //10 days

    public static final String SIGN_IN = "/users/login";
    public static final String SIGN_UP = "/users/register";
    public static final String HEADER_STRING = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_PATIENT = "PATIENT";
    public static final String ROLE_DOCTOR = "DOCTOR";
    public static final String USER_UNAUTHORIZED = "You are not authorized to access this!";
    public static final String USER_NOTFOUND = "User does not exist!";
    public static final String TOKEN_INVALID = "Token is invalid!";
}
