package com.example.healthcare_v2.domain.like.controller;

import com.example.healthcare_v2.domain.like.repository.DiaryLikeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@ActiveProfiles(profiles = "test")
@Transactional
public class DiaryLikeControllerTest {

    @Autowired
    private DiaryLikeRepository diaryLikeRepository;
    private MockMvc mockMvc;



    @DisplayName("좋아요 버튼 클릭")
    @Test
    void createWish(){

    }

}
