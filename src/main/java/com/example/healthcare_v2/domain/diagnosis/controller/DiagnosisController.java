package com.example.healthcare_v2.domain.diagnosis.controller;

import com.example.healthcare_v2.domain.diagnosis.dto.request.DiagnosisRequestDto;
import com.example.healthcare_v2.domain.diagnosis.dto.response.DiagnosisResponseDto;
import com.example.healthcare_v2.domain.diagnosis.service.DiagnosisService;
import com.example.healthcare_v2.global.utill.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<ResponseDTO<Page<DiagnosisResponseDto>>> diagnoses(
            @PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<DiagnosisResponseDto> diagnosis = diagnosisService.getDiagnoses(pageable).map(DiagnosisResponseDto::from);
        return ResponseEntity.ok(ResponseDTO.okWithData(diagnosis));
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<ResponseDTO<Page<DiagnosisResponseDto>>> diagnosis(
            @PathVariable("patientId") Long patientId,
            @PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<DiagnosisResponseDto> diagnosis = diagnosisService.getDiagnosesByPatient(pageable, patientId).map(DiagnosisResponseDto::from);
        return ResponseEntity.ok(ResponseDTO.okWithData(diagnosis));
    }

}
