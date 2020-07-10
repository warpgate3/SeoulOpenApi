package info.m2sj.bo.domain.parkinfo.controller;

import info.m2sj.bo.core.exceptions.BaseException;
import info.m2sj.bo.domain.parkinfo.dto.SearchParam;
import info.m2sj.bo.domain.parkinfo.service.ParkInfoService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api")
public class ApiController {
    private final ParkInfoService parkInfoService;

    public ApiController(ParkInfoService parkInfoService) {
        this.parkInfoService = parkInfoService;
    }

    @ApiOperation(value = "", notes = "서울시 공용 주차장 정보 조회")
    @GetMapping("/getParkInfo")
    public Flux<List<Map<String, String>>> getParkInfo(@Valid SearchParam searchParam,
                                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
           throw new BaseException(Objects.requireNonNull(
                   bindingResult.getFieldError()).getDefaultMessage());
        }

        return parkInfoService.findParkInfoListBy(searchParam);
    }
}
