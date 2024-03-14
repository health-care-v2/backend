package com.example.healthcare_v2.domain.diagnosis.controller;

import com.example.healthcare_v2.config.JsonDataEncoder;
import com.example.healthcare_v2.config.TestSecurityConfig;
import com.example.healthcare_v2.domain.diagnosis.dto.DiagnosisDto;
import com.example.healthcare_v2.domain.diagnosis.dto.request.DiagnosisRequestDto;
import com.example.healthcare_v2.domain.diagnosis.service.DiagnosisService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Diagnosis 컨트롤러 테스트")
@Import({TestSecurityConfig.class, JsonDataEncoder.class})
@WebMvcTest(DiagnosisController.class)
class DiagnosisControllerTest {
    @Autowired private JsonDataEncoder jsonDataEncoder;
    @Autowired private MockMvc mvc;
    @MockBean private DiagnosisService diagnosisService;

    @WithMockUser(username = "1")
    @DisplayName("진료하기")
    @Test
    void givenDiagnosisInfo_whenCreateNewDiagnosis_thenReturns200() throws Exception {
        // given
        DiagnosisRequestDto diagnosisRequestDto = createDiagnosisRequestDto();
        DiagnosisDto diagnosisDto = diagnosisRequestDto.toDto();
        willDoNothing().given(diagnosisService).saveDiagnosis(diagnosisDto);

        // when & then
        mvc.perform(post("/v2/diagnosis")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDataEncoder.encode(diagnosisRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("message").isEmpty()
                );
        then(diagnosisService).should().saveDiagnosis(diagnosisDto);
    }

    private DiagnosisRequestDto createDiagnosisRequestDto() {
        return new DiagnosisRequestDto(
                "질병1",
                "질병내용1",
                1L,
                1L
        );
    }

}