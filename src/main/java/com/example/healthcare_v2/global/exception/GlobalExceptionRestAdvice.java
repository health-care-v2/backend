package com.example.healthcare_v2.global.exception;

import com.example.healthcare_v2.global.utill.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionRestAdvice {

    @ExceptionHandler
    public ResponseEntity<ResponseDTO<Void>> applicationException(ApplicationException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
            .status(e.getErrorCode().getHttpStatus())
            .body(ResponseDTO.error(e.getErrorCode()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDTO<Void>> dbException(DataAccessException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ResponseDTO.errorWithMessage(HttpStatus.INTERNAL_SERVER_ERROR, "디비 에러!"));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDTO<Void>> serverException(RuntimeException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ResponseDTO.errorWithMessage(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러!"));
    }
}
