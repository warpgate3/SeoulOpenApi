package info.m2sj.bo;

import info.m2sj.bo.seoulapi.dto.ParkApiParam;
import info.m2sj.bo.seoulapi.service.impl.ParkApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static info.m2sj.bo.seoulapi.constants.ApiResultType.XML;

@SpringBootTest
class ParkApiServiceTest {
    @Autowired
    private ParkApiService parkApiService;

    @Test
    public void call_test() {
        List<String> wardList = List.of(
                "강남구",
                "강동구",
                "강북구",
                "강서구",
                "관악구",
                "광진구",
                "구로구",
                "금천구",
                "노원구",
                "도봉구",
                "동대문구",
                "동작구",
                "마포구",
                "서대문구",
                "서초구",
                "성동구",
                "성북구",
                "송파구",
                "양천구",
                "영등포구",
                "용산구",
                "은평구",
                "종로구",
                "중구",
                "중랑구"
        );

        for (String wardName : wardList) {
            ParkApiParam parkApiParam = new ParkApiParam();
            parkApiParam.setStartIndex(1);
            parkApiParam.setEndIndex(1000);
            parkApiParam.setApiResultType(XML);
            parkApiParam.setAddr(wardName);
            parkApiParam.setParkingCode("");
            Map<String, String> result = parkApiService.call(parkApiParam);
            System.out.println(result.get("list_total_count"));
        }
    }
}