package info.m2sj.bo.seoulapi.service.impl;

import info.m2sj.bo.seoulapi.constants.ApiResultType;
import info.m2sj.bo.seoulapi.constants.ApiServiceType;
import info.m2sj.bo.seoulapi.dto.ParkApiParam;
import info.m2sj.bo.seoulapi.support.HttpProxy;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(properties = "spring.config.location=classpath:/application.yml")
@RunWith(SpringRunner.class)
class CommonApiServiceTest {
    @Value("${api.credential_key}")
    String credentialKey;

    @Value("${api.api_server_url}")
    String apiServerUrl;

    @Autowired
    private CommonApiService commonApiService;

    @MockBean
    private HttpProxy httpProxy;

    @Test
    void buildBaseUrl() {
        String url = commonApiService.buildBaseUrl();
        assertThat(apiServerUrl + credentialKey, equalTo(url));
    }

    @Test
    void buildDetailUrl() {
        ParkApiParam param = new ParkApiParam();
        param.setApiResultType(ApiResultType.XML);
        String s = commonApiService.buildDetailUrl(param);
        assertThat(apiServerUrl + credentialKey + "/" + ApiResultType.XML + "/" + ApiServiceType.GetParkInfo, equalTo(s));
    }

    @Test
    void sendGet() {
        Mockito.when(httpProxy.sendGet("http://openapi.seoul.go.kr:8088/XML/GetParkInfo/1/1//")).thenReturn("<GetParkInfo>\\n\" +\n" +
                "                \"<list_total_count>14410</list_total_count>\\n\" +\n" +
                "                \"<RESULT>\\n\" +\n" +
                "                \"<CODE>INFO-000</CODE>\\n\" +\n" +
                "                \"<MESSAGE>정상 처리되었습니다</MESSAGE>\\n\" +\n" +
                "                \"</RESULT></GetParkInfo>");

        JSONObject jsonObject = commonApiService
                .sendGet("http://openapi.seoul.go.kr:8088/XML/GetParkInfo/1/1//", ApiResultType.XML);

        assertThat(jsonObject.getString("list_total_count"), equalTo("14410"));

    }
}