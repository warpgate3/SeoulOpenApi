package info.m2sj.bo.core.exceptions;

/**
 * 공공 Open API 연동 처리 예외
 */
public class ApiException extends RuntimeException {
    public ApiException(String msg) {
        super(msg);
    }
}
