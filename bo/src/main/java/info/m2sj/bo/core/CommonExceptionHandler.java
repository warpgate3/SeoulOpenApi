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
       Map<String, String> error = new HashMap<>();
       error.put("code","E001");
       error.put("message",be.getMessage());
       return Mono.just(error);
    }

    @ExceptionHandler(value = ApiException.class)
    @ResponseBody
    public Mono<Map<String, String>> apiException(ApiException ae) {
        log.error(ae.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("code","E002");
        error.put("message",ae.getMessage());
        return Mono.just(error);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Mono<Map<String, String>> commonException(Exception e) {
        log.error(e.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("code","E003");
        error.put("message",e.getMessage());
        return Mono.just(error);
    }
}

