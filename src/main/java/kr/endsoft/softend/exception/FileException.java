package kr.endsoft.softend.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FileException extends Exception {
    private final String message;

    @Override
    public String getMessage() {
        return message;
    }
}
