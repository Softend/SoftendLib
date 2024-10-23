package kr.ypshop.softend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class RedundantServiceException extends ServiceException {

    private final String serviceName;

    public RedundantServiceException(String serviceName) {
        super("이미 같은 이름을 가진 서비스가 존재합니다!");
        this.serviceName = serviceName;
    }
}
