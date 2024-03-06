package com.example.healthcare_v2.domain.patient.controller;

import com.example.healthcare_v2.domain.patient.controller.request.CreatePatientRequest;
import com.example.healthcare_v2.domain.patient.controller.request.LoginRequest;
import com.example.healthcare_v2.domain.patient.controller.response.CreatePatientResponse;
import com.example.healthcare_v2.domain.patient.dto.TokenDTO;
import com.example.healthcare_v2.domain.patient.service.PatientService;
import com.example.healthcare_v2.global.common.CookieUtils;
import com.example.healthcare_v2.global.utill.ResponseDTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v2/patients")
@RequiredArgsConstructor
@Transactional
public class PatientController {

    @Value("${cookie.domain}")
    private String domain;
    public static final String ACCESS_TOKEN_COOKIE_NAME = "accessToken";

    private final CookieUtils cookieUtils;

    private final PatientService patientService;

    @PostMapping("/signup")
    public ResponseEntity<ResponseDTO<CreatePatientResponse>> signup(
        @Valid @RequestBody CreatePatientRequest request
    ) {
        return ResponseEntity.ok(
            ResponseDTO.okWithData(patientService.registerNewUser(request))
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<Void>> login(
        @Valid @RequestBody LoginRequest request,
        HttpServletResponse response
    ) {
        TokenDTO tokenDTO = patientService.login(request);

        Cookie accessToken = cookieUtils.makeCookie(
            ACCESS_TOKEN_COOKIE_NAME, tokenDTO.accessToken()
        );
        response.addCookie(accessToken);

        return ResponseEntity.ok(ResponseDTO.ok());
    }

    @DeleteMapping
    public ResponseEntity<ResponseDTO<Void>> delete(HttpServletResponse response){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(authentication.getName());
        patientService.delete(userId);

        Cookie emptyAccessToken = new Cookie(ACCESS_TOKEN_COOKIE_NAME, null);
        emptyAccessToken.setMaxAge(0);
        emptyAccessToken.setHttpOnly(false);
        emptyAccessToken.setDomain(domain);
        emptyAccessToken.setPath("/");

        response.addCookie(emptyAccessToken);

        return ResponseEntity.ok(ResponseDTO.ok());
    }
}
