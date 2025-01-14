package kr.endsoft.softend.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PaginateException extends RuntimeException {
    private final String message;

    @Override
    public String getMessage() {
        return message;
    }
}
