package kr.endsoft.softend.exception.paginate;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PaginateException extends RuntimeException {
    private final String message;

    @Override
    public String getMessage() {
        return message;
    }
}
