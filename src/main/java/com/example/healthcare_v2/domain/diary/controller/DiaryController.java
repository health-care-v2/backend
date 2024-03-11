package com.example.healthcare_v2.domain.diary.controller;

import com.example.healthcare_v2.domain.diary.controller.request.CreateDiaryRequest;
import com.example.healthcare_v2.domain.diary.controller.request.UpdateDiaryRequest;
import com.example.healthcare_v2.domain.diary.service.DiaryService;
import com.example.healthcare_v2.global.utill.ResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    ) {
        Long userId = Long.valueOf(principal.getName());
        diaryService.create(request, userId);
        return ResponseEntity.ok(ResponseDTO.ok());
    }

    @PutMapping("/{diaryId}")
    public ResponseEntity<ResponseDTO<Void>> update(
            @PathVariable Long diaryId,
            @Valid @RequestBody UpdateDiaryRequest request,
            Principal principal
    ) {
        Long userId = Long.valueOf(principal.getName());
        diaryService.update(request, userId, diaryId);
        return ResponseEntity.ok(ResponseDTO.ok());
    }

    @DeleteMapping("/{diaryId}")
    public ResponseEntity<ResponseDTO<Void>> delete(
            @PathVariable Long diaryId,
            Principal principal
    ){
        Long userId = Long.valueOf(principal.getName());
        diaryService.delete(diaryId,userId);
        return ResponseEntity.ok(ResponseDTO.ok());
    }
}
