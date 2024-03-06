package com.example.healthcare_v2.domain.patient.service;

import com.example.healthcare_v2.domain.patient.controller.request.CreatePatientRequest;
import com.example.healthcare_v2.domain.patient.controller.request.LoginRequest;
import com.example.healthcare_v2.domain.patient.controller.response.CreatePatientResponse;
import com.example.healthcare_v2.domain.patient.dto.TokenDTO;
import com.example.healthcare_v2.domain.patient.entity.Patient;
import com.example.healthcare_v2.domain.patient.exception.InvalidPasswordException;
import com.example.healthcare_v2.domain.patient.exception.UserAlreadyRegisteredException;
import com.example.healthcare_v2.domain.patient.exception.UserNotFoundException;
import com.example.healthcare_v2.domain.patient.repository.PatientRepository;
import com.example.healthcare_v2.global.config.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientService {

    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional(readOnly = true)
    public Patient findById(Long patientId) {
        return patientRepository.findById(patientId).orElseThrow(UserNotFoundException::new);
    }

    public CreatePatientResponse registerNewUser(CreatePatientRequest createUserRequest) {
        patientRepository.findByEmail(createUserRequest.email()).ifPresent(user -> {
            throw new UserAlreadyRegisteredException();
        });
        String encodedPassword = passwordEncoder.encode(createUserRequest.password());
        Patient newUser = createUserRequest.toEntity(encodedPassword);

        patientRepository.save(newUser);
        return CreatePatientResponse.fromEntity(newUser);
    }

    public TokenDTO login(LoginRequest request) {
        Patient patient = patientRepository.findByEmail(request.email())
            .orElseThrow(UserNotFoundException::new);

        isDeletedPatient(patient);
        if (!passwordEncoder.matches(request.password(), patient.getEncryptedPassword())) {
            throw new InvalidPasswordException();
        }

        String accessToken = jwtProvider.createToken(patient);
        return new TokenDTO(accessToken);
    }

    public void delete(Long patientId) {
        Patient patient = findById(patientId);
        patient.delete();
        patientRepository.save(patient);
    }

    private void isDeletedPatient(Patient patient) {
        if (patient.isDeleted()) {
            throw new UserNotFoundException();
        }
    }
}
