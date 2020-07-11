package info.m2sj.bo.domain.parkinfo.controller;

import info.m2sj.bo.domain.parkinfo.dto.ParkInfoPagingResponse;
import info.m2sj.bo.domain.parkinfo.dto.ParkInfoResponse;
import info.m2sj.bo.domain.parkinfo.dto.ParkInfoSearchRequest;
import info.m2sj.bo.domain.parkinfo.service.ParkInfoService;
import io.swagger.annotations.ApiModelProperty;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@WebFluxTest(ParkInfoController.class)
class ParkInfoControllerTest {
    @MockBean
    private ParkInfoService parkInfoService;

    @Autowired
    private WebTestClient webClient;

    @Test
    void getParkInfo() {
        List<ParkInfoResponse> parkingList = new ArrayList<>();
        parkingList.add(ParkInfoResponse.builder()
                .parkingTypeNm("노외 주차장")
                .tel("02-577-0592")
                .addr("강남구 개포동 567-23")
                .payYn("Y")
                .operationRuleNm("시간제 주차장")
                .parkingName("구룡산제1호(구)")
                .parkingCode("173474")
                .timeRate("5")
                .build());

        ParkInfoPagingResponse parkInfoPagingResponse = ParkInfoPagingResponse.builder()
                .totalCount(1)
                .pageScale(10)
                .pageNumber(1)
                .parkInfoResponseList(parkingList)
                .build();
        Mono<ParkInfoPagingResponse> rtnMono = Mono.just(parkInfoPagingResponse);

        ParkInfoSearchRequest searchParam = ParkInfoSearchRequest.builder()
                .pageNumber(1)
                .pageScale(10)
                .parkingName("구룡산")
                .telNumber("02-577-0592")
                .wardName("강남구")
                .build();

        Mockito.when(parkInfoService.findParkInfoListBy(searchParam)).thenReturn(rtnMono);

        webClient.get().uri(uriBuilder ->
            uriBuilder.path("/api/getParkInfo")
                    .queryParam("pageNumber" , searchParam.getPageNumber())
                    .queryParam("pageScale", searchParam.getPageScale())
                    .queryParam("parkingName", searchParam.getParkingName())
                    .queryParam("telNumber", searchParam.getTelNumber())
                    .queryParam("wardName", searchParam.getWardName())
                    .build()).header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.pageNumber").isEqualTo("1")
                .jsonPath("$.pageScale").isEqualTo("10")
                .jsonPath("$.totalCount").isEqualTo("1")
                .jsonPath("$.parkInfoResponseList[0].parkingName").isEqualTo("구룡산제1호(구)")
                .jsonPath("$.parkInfoResponseList[0].addr").isEqualTo("강남구 개포동 567-23")
                .jsonPath("$.parkInfoResponseList[0].operationRuleNm").isEqualTo("시간제 주차장")
                .jsonPath("$.parkInfoResponseList[0].parkingCode").isEqualTo("173474")
                .jsonPath("$.parkInfoResponseList[0].parkingTypeNm").isEqualTo("노외 주차장")
                .jsonPath("$.parkInfoResponseList[0].payYn").isEqualTo("Y")
                .jsonPath("$.parkInfoResponseList[0].tel").isEqualTo("02-577-0592")
                .jsonPath("$.parkInfoResponseList[0].timeRate").isEqualTo("5");


        Mockito.verify(parkInfoService, Mockito.times(1)).findParkInfoListBy(searchParam);
    }
}