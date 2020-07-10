package info.m2sj.bo.core;


import info.m2sj.bo.core.exceptions.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

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
}

