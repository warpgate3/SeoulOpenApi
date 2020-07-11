package info.m2sj.bo.seoulapi.service.impl;

import info.m2sj.bo.seoulapi.dto.ParkApiParam;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(properties = "spring.config.location=classpath:/application.yml")
@RunWith(SpringRunner.class)
class ParkApiServiceTest {
    @Autowired
    private ParkApiService parkApiService;

    @Test
    void buildSearchUrl() {
        ParkApiParam searchParam = new ParkApiParam();
        searchParam.setStartIndex(1);
        searchParam.setEndIndex(30);
        searchParam.setAddr("동작구");
        searchParam.setParkingCode("1234");
        String searchUrl = parkApiService.buildSearchUrl(searchParam);
        assertThat(searchUrl, equalTo(String.format("/%s/%s/%s/%s",
                searchParam.getStartIndex(),
                searchParam.getEndIndex(),
                searchParam.getAddr(),
                searchParam.getParkingCode())));
    }
}