package info.m2sj.bo.core;


import info.m2sj.bo.core.exceptions.ApiException;
import info.m2sj.bo.core.exceptions.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * 예외 공통 핸들러
 */
@ControllerAdvice
@Slf4j
public class CommonExceptionHandler {
    @ExceptionHandler(value = BaseException.class)
    @ResponseBody
    public Mono<Map<String, String>> baseException(BaseException be) {
        log.error(be.getMessage());
        be.printStackTrace();
        return Mono.just(getErrorMap("E001", be.getMessage()));
    }

    @ExceptionHandler(value = ApiException.class)
    @ResponseBody
    public Mono<Map<String, String>> apiException(ApiException ae) {
        log.error(ae.getMessage());
        ae.printStackTrace();
        return Mono.just(getErrorMap("E002", ae.getMessage()));
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Mono<Map<String, String>> commonException(Exception e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return Mono.just(getErrorMap("E003", e.getMessage()));
    }

    private Map<String, String> getErrorMap(String errCode, String errMsg) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("code", errCode);
        errorMap.put("message", errMsg);
        return errorMap;
    }
}

