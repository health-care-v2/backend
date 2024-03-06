package com.example.healthcare_v2.domain.doctor.controller;

import static com.example.healthcare_v2.domain.patient.controller.PatientController.ACCESS_TOKEN_COOKIE_NAME;

import com.example.healthcare_v2.domain.doctor.controller.request.CreateDoctorRequest;
import com.example.healthcare_v2.domain.doctor.controller.response.CreateDoctorResponse;
import com.example.healthcare_v2.domain.doctor.service.DoctorService;
import com.example.healthcare_v2.domain.patient.controller.request.LoginRequest;
import com.example.healthcare_v2.domain.patient.dto.TokenDTO;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/doctors")
public class DoctorController {

    @Value("${cookie.domain}")
    private String domain;
    private final CookieUtils cookieUtils;
    private final DoctorService doctorService;

    @PostMapping("/signup")
    public ResponseEntity<ResponseDTO<CreateDoctorResponse>> signup(
        @Valid @RequestBody CreateDoctorRequest request
    ) {
        return ResponseEntity.ok(
            ResponseDTO.okWithData(doctorService.registerNewUser(request))
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<Void>> login(
        @Valid @RequestBody LoginRequest request,
        HttpServletResponse response
    ) {
        TokenDTO tokenDTO = doctorService.login(request);

        Cookie accessToken = cookieUtils.makeCookie(
            ACCESS_TOKEN_COOKIE_NAME, tokenDTO.accessToken()
        );
        response.addCookie(accessToken);

        return ResponseEntity.ok(ResponseDTO.ok());
    }

    @DeleteMapping
    public ResponseEntity<ResponseDTO<Void>> delete(HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(authentication.getName());
        doctorService.delete(userId);

        Cookie emptyAccessToken = new Cookie(ACCESS_TOKEN_COOKIE_NAME, null);
        emptyAccessToken.setMaxAge(0);
        emptyAccessToken.setHttpOnly(false);
        emptyAccessToken.setDomain(domain);
        emptyAccessToken.setPath("/");

        response.addCookie(emptyAccessToken);

        return ResponseEntity.ok(ResponseDTO.ok());

    }
}
