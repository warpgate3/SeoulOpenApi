package info.m2sj.bo;

import info.m2sj.bo.dto.ParkApiParam;
import info.m2sj.bo.service.impl.ParkApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static info.m2sj.bo.constants.ApiResultType.JSON;

@SpringBootTest
class ParkApiServiceTest {
    @Autowired
    private ParkApiService parkApiService;

    @Test
    public void call_test() {
        ParkApiParam parkApiParam = new ParkApiParam();
        parkApiParam.setStartIndex(1);
        parkApiParam.setEndIndex(5);
        parkApiParam.setApiResultType(JSON);
        parkApiParam.setAddr("강남");
        parkApiParam.setParkingCode("");
        Map<String, Object> call = parkApiService.call(parkApiParam);
        System.out.println(call);
    }
}