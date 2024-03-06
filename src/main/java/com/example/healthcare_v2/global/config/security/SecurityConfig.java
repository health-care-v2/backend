package com.example.healthcare_v2.global.config.security;

import com.example.healthcare_v2.global.config.security.jwt.JwtFilter;
import com.example.healthcare_v2.global.utill.ResponseDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String[] ALLOWED_PATHS
        = {
        "/v2/patients/email/**", "/v2/doctors/email/**",
        "/v2/patients/signup","/v2/patients/login",
        "/v2/doctors/signup","/v2/doctors/login"
    };

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // JWT 인증을 사용하므로 CSRF 보안 해제 및 세션 사용안함 설정
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(sessionManagement -> sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
        ;

        // 인증/인가 예외 문구 처리
        http
            .exceptionHandling(exceptionHandling -> exceptionHandling.accessDeniedHandler(
                        (request, response, accessDeniedException) -> {
                            ResponseDTO<Void> responseDTO =
                                ResponseDTO.errorWithMessage(HttpStatus.FORBIDDEN, "Access Denied");
                            sendResponse(response, responseDTO);
                        }
                    )
                    .authenticationEntryPoint(
                        (request, response, accessDeniedException) -> {
                            ResponseDTO<Void> responseDTO =
                                ResponseDTO.errorWithMessage(HttpStatus.UNAUTHORIZED,
                                    "Unauthorized: 1. 비로그인 상태로 로그인이 필요한 API에 접근했거나 2. 없는 API URI에 요청을 보내고 있습니다");
                            sendResponse(response, responseDTO);
                        }
                    )
            );

        // REST API의 URI에 대한 인가 적용/미적용 설정
        http.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
            .requestMatchers(
                Arrays.stream(ALLOWED_PATHS)
                    .map(AntPathRequestMatcher::new)
                    .toArray(RequestMatcher[]::new)
            ).permitAll()
            .anyRequest().authenticated()
        );

        http.addFilterBefore(
            jwtFilter,
            UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    };


    private void sendResponse(HttpServletResponse response, ResponseDTO<Void> responseDTO) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(responseDTO.getCode());
        PrintWriter out = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        out.print(objectMapper.writeValueAsString(responseDTO));
        out.flush();
    }
}
