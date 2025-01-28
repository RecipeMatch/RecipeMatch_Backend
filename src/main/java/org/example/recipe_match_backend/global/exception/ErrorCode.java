package org.example.recipe_match_backend.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 사용자 관련 오류
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "E001", "사용자를 찾을 수 없습니다."),
    DUPLICATE_USER(HttpStatus.CONFLICT, "E002", "이미 존재하는 사용자입니다."),

    // 인증/인가 관련 오류
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "E101", "유효하지 않은 토큰입니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "E102", "접근이 거부되었습니다."),

    // 서버 오류
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E500", "서버 오류가 발생했습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private String message;
}
