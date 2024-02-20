package com.example.healthcare_v2.domain.patient.controller;

import com.example.healthcare_v2.domain.patient.controller.request.CreateUserRequest;
import com.example.healthcare_v2.domain.patient.controller.request.LoginRequest;
import com.example.healthcare_v2.domain.patient.controller.response.CreateUserResponse;
import com.example.healthcare_v2.domain.patient.dto.TokenDTO;
import com.example.healthcare_v2.domain.patient.service.PatientService;
import com.example.healthcare_v2.global.common.CookieUtils;
import com.example.healthcare_v2.global.utill.ResponseDTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v2/patients")
@RequiredArgsConstructor
public class PatientController {
    public static final String ACCESS_TOKEN_COOKIE_NAME = "accessToken";

    private final CookieUtils cookieUtils;

    private final PatientService patientService;

    @PostMapping("/signup")
    public ResponseEntity<ResponseDTO<CreateUserResponse>> signup(
        @Valid @RequestBody CreateUserRequest request
    ) {
        return ResponseEntity.ok(
            ResponseDTO.okWithData(patientService.registerNewUser(request))
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<Void>> login(
        @Valid @RequestBody LoginRequest request,
        HttpServletResponse response
    ){
        TokenDTO tokenDTO = patientService.login(request);

        Cookie accessToken = cookieUtils.makeCookie(
            ACCESS_TOKEN_COOKIE_NAME, tokenDTO.accessToken()
        );
        response.addCookie(accessToken);

        return ResponseEntity.ok(ResponseDTO.ok());
    }
}
