package info.m2sj.bo.domain.parkinfo.controller;

import info.m2sj.bo.core.exceptions.BaseException;
import info.m2sj.bo.domain.parkinfo.dto.ParkInfoPagingResponse;
import info.m2sj.bo.domain.parkinfo.dto.ParkInfoSearchRequest;
import info.m2sj.bo.domain.parkinfo.service.ParkInfoService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Objects;


@Slf4j
@RestController
@RequestMapping("/api")
@CrossOrigin(value = { "*" })
public class ParkInfoController {
    private final ParkInfoService parkInfoService;

    public ParkInfoController(ParkInfoService parkInfoService) {
        this.parkInfoService = parkInfoService;
    }

    @ApiOperation(value = "", notes = "서울시 공용 주차장 정보 조회")
    @GetMapping("/getParkInfo")
    public Mono<ParkInfoPagingResponse> getParkInfo(@Valid ParkInfoSearchRequest parkInfoSearchRequest,
                                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
           throw new BaseException(Objects.requireNonNull(
                   bindingResult.getFieldError()).getDefaultMessage());
        }

        return parkInfoService.findParkInfoListBy(parkInfoSearchRequest);
    }
}
