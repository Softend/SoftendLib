package kr.ypshop.softend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RedundantServiceException extends RuntimeException {

    private final String serviceName;

}
