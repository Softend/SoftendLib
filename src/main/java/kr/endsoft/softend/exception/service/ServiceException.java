package kr.endsoft.softend.exception.service;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ServiceException extends RuntimeException {

    public static final String UNKNOWN_SERVICE = "알 수 없는 서비스입니다.";
    private final String message;

    @Override
    public String getMessage() {
        return message;
    }
}
