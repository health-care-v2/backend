package com.example.healthcare_v2.config;

import com.example.healthcare_v2.domain.patient.entity.Patient;
import com.example.healthcare_v2.domain.patient.repository.PatientRepository;
import com.example.healthcare_v2.global.config.security.SecurityConfig;
import com.example.healthcare_v2.global.config.security.jwt.JwtFilter;
import com.example.healthcare_v2.global.config.security.jwt.JwtProvider;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@Import({SecurityConfig.class, JwtProvider.class, JwtFilter.class})
public class TestSecurityConfig {
    @MockBean private PatientRepository patientRepository;

    @BeforeTestMethod
    void securitySetUp() {
        Patient patient = Patient.builder()
                .email("email@email.com")
                .encryptedPassword("password1")
                .name("name1")
                .phoneNumber("010-1234-1234")
                .addr1("부산시 해운대구 우동 543-21")
                .addr2("102")
                .build();

        ReflectionTestUtils.setField(patient, "id", 1L);

        given(patientRepository.findById(anyLong())).willReturn(Optional.of(patient));
    }
}
