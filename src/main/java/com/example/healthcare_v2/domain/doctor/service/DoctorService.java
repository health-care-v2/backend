package com.example.healthcare_v2.domain.doctor.service;

import com.example.healthcare_v2.domain.doctor.controller.request.CreateDoctorRequest;
import com.example.healthcare_v2.domain.doctor.controller.response.CreateDoctorResponse;
import com.example.healthcare_v2.domain.doctor.entity.Doctor;
import com.example.healthcare_v2.domain.doctor.repository.DoctorRepository;
import com.example.healthcare_v2.domain.patient.controller.request.CreatePatientRequest;
import com.example.healthcare_v2.domain.patient.controller.request.LoginRequest;
import com.example.healthcare_v2.domain.patient.controller.response.CreatePatientResponse;
import com.example.healthcare_v2.domain.patient.dto.TokenDTO;
import com.example.healthcare_v2.domain.patient.exception.InvalidPasswordException;
import com.example.healthcare_v2.domain.patient.exception.UserAlreadyRegisteredException;
import com.example.healthcare_v2.domain.patient.exception.UserNotFoundException;
import com.example.healthcare_v2.global.config.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DoctorService {

    public final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public CreateDoctorResponse registerNewUser(CreateDoctorRequest request) {
        doctorRepository.findByEmail(request.email()).ifPresent(user -> {
            throw new UserAlreadyRegisteredException();
        });

        String encodedPassword = passwordEncoder.encode(request.password());
        Doctor doctor = request.toEntity(encodedPassword);

        doctorRepository.save(doctor);
        return CreateDoctorResponse.fromEntity(doctor);
    }


    public TokenDTO login(LoginRequest request) {
        Doctor doctor = doctorRepository.findByEmail(request.email())
            .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(request.password(), doctor.getEncryptedPassword())) {
            throw new InvalidPasswordException();
        }
        String accessToken = jwtProvider.createToken(doctor);
        return new TokenDTO(accessToken);
    }
}
