package info.m2sj.bo.domain.parkinfo.service;

import info.m2sj.bo.core.ParkInfoDataLoader;
import info.m2sj.bo.domain.parkinfo.dto.ParkInfoPagingResponse;
import info.m2sj.bo.domain.parkinfo.dto.ParkInfoSearchRequest;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest()
class ParkInfoServiceTest {
    @Autowired
    private ParkInfoService parkInfoService;
    @MockBean
    private ParkInfoDataLoader parkInfoDataLoader;

    @Test
    void findParkInfoListBy() {
        List<Map<String, String>> mockMap = new ArrayList<>();
        mockMap.add(Map.of("PARKING_NAME","구룡산", "TEL", "02-577-0592", "ADDR", "강남구"));
        Mockito.when(parkInfoDataLoader.getParkInfoData()).thenReturn(mockMap);

        ParkInfoSearchRequest searchParam = ParkInfoSearchRequest.builder()
                .pageNumber(1)
                .pageScale(10)
                .parkingName("구룡산")
                .telNumber("02-577-0592")
                .wardName("강남구")
                .build();
        Mono<ParkInfoPagingResponse> parkInfoListBy = parkInfoService.findParkInfoListBy(searchParam);
        ParkInfoPagingResponse parkInfoWithPaging = parkInfoListBy.block();
        assertThat(parkInfoWithPaging.getPageNumber(), equalTo(1));
        assertThat(parkInfoWithPaging.getPageScale(), equalTo(10));
        assertThat(parkInfoWithPaging.getTotalCount(), equalTo(1));
        assertThat(parkInfoWithPaging.getParkInfoResponseList().size(), equalTo(1));
    }

    @Test
    void findParkInfoListBy_searchCondition() {
        List<Map<String, String>> mockMap = new ArrayList<>();
        mockMap.add(Map.of("PARKING_NAME","구룡산", "TEL", "02-577-0592", "ADDR", "강남구"));
        Mockito.when(parkInfoDataLoader.getParkInfoData()).thenReturn(mockMap);

        ParkInfoSearchRequest searchParam = ParkInfoSearchRequest.builder()
                .pageNumber(1)
                .pageScale(10)
                .parkingName("구xx룡산")
                .telNumber("02-577-0592")
                .wardName("강남구")
                .build();
        Mono<ParkInfoPagingResponse> parkInfoListBy = parkInfoService.findParkInfoListBy(searchParam);
        ParkInfoPagingResponse parkInfoWithPaging = parkInfoListBy.block();
        assertThat(parkInfoWithPaging.getParkInfoResponseList().size(), equalTo(0));

         searchParam = ParkInfoSearchRequest.builder()
                .pageNumber(1)
                .pageScale(10)
                .parkingName("구룡산")
                .telNumber("02-177-0592")
                .wardName("강남구")
                .build();
        parkInfoListBy = parkInfoService.findParkInfoListBy(searchParam);
        parkInfoWithPaging = parkInfoListBy.block();
        assertThat(parkInfoWithPaging.getParkInfoResponseList().size(), equalTo(0));

       searchParam = ParkInfoSearchRequest.builder()
                .pageNumber(1)
                .pageScale(10)
                .parkingName("구룡산")
                .telNumber("02-577-0592")
                .wardName("강s남구")
                .build();
        parkInfoListBy = parkInfoService.findParkInfoListBy(searchParam);
        parkInfoWithPaging = parkInfoListBy.block();
        assertThat(parkInfoWithPaging.getParkInfoResponseList().size(), equalTo(0));
    }
}