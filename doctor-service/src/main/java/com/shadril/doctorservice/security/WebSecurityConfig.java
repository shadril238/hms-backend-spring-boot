package com.shadril.doctorservice.security;

import com.shadril.doctorservice.constants.AppConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.net.http.HttpRequest;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager)
            throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    auth
                            .requestMatchers(HttpMethod.POST, "/doctors/register").permitAll()
                            .requestMatchers(HttpMethod.POST, "/doctors/login").permitAll()
                            .requestMatchers(HttpMethod.POST, "/appointments/create").hasAuthority(AppConstants.ROLE_DOCTOR)
                            .requestMatchers(HttpMethod.DELETE, "/appointments/delete/**").hasAuthority(AppConstants.ROLE_DOCTOR)
                            .requestMatchers(HttpMethod.GET, "/appointments/book").hasAuthority(AppConstants.ROLE_PATIENT)
                            .requestMatchers(HttpMethod.GET, "/appointments/**").hasAnyAuthority(AppConstants.ROLE_ADMIN, AppConstants.ROLE_DOCTOR, AppConstants.ROLE_PATIENT)
                            .requestMatchers(HttpMethod.DELETE, "/doctors/delete/**").hasAuthority(AppConstants.ROLE_ADMIN)
                            .requestMatchers(HttpMethod.GET, "/doctors/**").hasAnyAuthority(AppConstants.ROLE_ADMIN, AppConstants.ROLE_DOCTOR, AppConstants.ROLE_PATIENT)
                            .requestMatchers(HttpMethod.POST, "/doctors/**").hasAnyAuthority(AppConstants.ROLE_ADMIN, AppConstants.ROLE_DOCTOR, AppConstants.ROLE_PATIENT)
                            .requestMatchers(HttpMethod.PUT, "/doctors/**").hasAnyAuthority(AppConstants.ROLE_ADMIN, AppConstants.ROLE_DOCTOR, AppConstants.ROLE_PATIENT)
                            .requestMatchers(HttpMethod.DELETE, "/doctors/**").hasAnyAuthority(AppConstants.ROLE_ADMIN, AppConstants.ROLE_DOCTOR, AppConstants.ROLE_PATIENT)
                            .anyRequest().permitAll();
                })
                .addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
        ;
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:5173");
        //config.addAllowedHeader("Authorization");
        config.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "Accept",
                "X-Requested-With",
                "Cache-Control"
        ));
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}