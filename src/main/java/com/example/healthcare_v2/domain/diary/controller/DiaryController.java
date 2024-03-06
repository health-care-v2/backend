package com.example.healthcare_v2.domain.diary.controller;

import com.example.healthcare_v2.domain.diary.controller.request.CreateDiaryRequest;
import com.example.healthcare_v2.domain.diary.service.DiaryService;
import com.example.healthcare_v2.global.utill.ResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/v2/diaries")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping
    public ResponseEntity<ResponseDTO<Void>> create(
            @Valid @RequestBody CreateDiaryRequest request,
            Principal principal
    ){
        Long userId = Long.valueOf(principal.getName());
        diaryService.create(request, userId);
        return ResponseEntity.ok(ResponseDTO.ok());
    }
}
