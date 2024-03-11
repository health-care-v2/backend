package com.example.healthcare_v2.domain.diary.controller;

import com.example.healthcare_v2.domain.patient.entity.Patient;
import com.example.healthcare_v2.domain.patient.repository.PatientRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles(profiles = "test")
@Transactional
public class DiaryControllerTest {

    @Autowired
    private PatientRepository patientRepository;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(@Autowired WebApplicationContext applicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .alwaysDo(print())
                .build();

        patientRepository.save(Patient.builder()
                .name("test1")
                .email("test1@naver.com")
                .encryptedPassword("test1")
                .phoneNumber("010-0000-0000")
                .addr1("서울")
                .addr2("송파구")
                .build()
        );
    }

    @DisplayName("일기 등록_인증있을경우")
    @Test
    @WithMockUser(username = "21")
    void postDiary_인증있음() throws Exception {
        mockMvc.perform(
                        post("/v2/diaries")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{"
                                        + "\"title\":\"제목\","
                                        + "\"isWalk\":true,"
                                        + "\"isStretching\":true,"
                                        + "\"takeMedicine\":\"아침,점심,저녁\","
                                        + "\"status\":4.33,"
                                        + "\"content\":\"배가아프다\","
                                        + "\"isPublic\":true"
                                        + "}")
                )
                .andExpect(status().isOk());
    }

    @DisplayName("일기 등록_인증없을경우")
    @Test
    void postDiary_인증없음() throws Exception {
        mockMvc.perform(post("/v2/diaries"))
                .andExpect(status().is4xxClientError());
    }
}
