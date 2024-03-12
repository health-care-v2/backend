package com.example.healthcare_v2.domain.like.controller;

import com.example.healthcare_v2.domain.diary.service.DiaryService;
import com.example.healthcare_v2.domain.like.service.DiaryLikeService;
import com.example.healthcare_v2.global.utill.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/wishes")
public class DiaryLikeController {

    private final DiaryLikeService diaryLikeService;

    @GetMapping("/{diaryId}")
    public ResponseEntity<ResponseDTO<Void>> createWish(
            @PathVariable Long diaryId,
            Principal principal
    ) {
        Long userId = Long.valueOf(principal.getName());
        diaryLikeService.createWish(diaryId,userId);
        return ResponseEntity.ok(ResponseDTO.ok());
    }
}
