package com.m2j2.haruseoul.anonymous.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        log.error("HttpMessageNotReadableException: {}", ex.getMessage(), ex);

        // 상세한 원인을 클라이언트에 반환 (프로덕션 환경에서는 구체적 오류 메시지 대신 일반 메시지를 반환하는 것이 안전함)
        return new ResponseEntity<>("Invalid request body. Error: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
