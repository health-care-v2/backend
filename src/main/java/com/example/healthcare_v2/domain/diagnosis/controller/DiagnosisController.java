package com.example.healthcare_v2.domain.diagnosis.controller;

import com.example.healthcare_v2.domain.diagnosis.dto.request.DiagnosisRequestDto;
import com.example.healthcare_v2.domain.diagnosis.service.DiagnosisService;
import com.example.healthcare_v2.global.utill.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RequiredArgsConstructor
@RequestMapping("/v2/diagnosis")
@RestController
public class DiagnosisController {
    private final DiagnosisService diagnosisService;

    @PostMapping
    public ResponseEntity<ResponseDTO<Void>> createNewDiagnosis(
            @RequestBody DiagnosisRequestDto diagnosisRequestDto,
            Principal principal) {
        diagnosisService.saveDiagnosis(diagnosisRequestDto.toDto());
        return ResponseEntity.ok(ResponseDTO.ok());
    }
}
