package com.example.healthcare_v2.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // PATIENT
    USER_ALREADY_REGISTERED(HttpStatus.BAD_REQUEST,"이미 가입된 회원입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 틀렸습니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당하는 이메일이 존재하지 않습니다."),

    // 5xx - 서버 에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message){
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
