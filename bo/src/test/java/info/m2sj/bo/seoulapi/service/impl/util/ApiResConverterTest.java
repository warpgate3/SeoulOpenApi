package info.m2sj.bo.seoulapi.service.impl.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.w3c.dom.Document;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
@RunWith(SpringRunner.class)
@SpringBootTest()
class ApiResConverterTest {
    @Test
    void convertStringToXMLDocument() {
        Document document = ApiResConverter.convertStringToXMLDocument("<GetParkInfo>\n" +
                "<list_total_count>14410</list_total_count>\n" +
                "<RESULT>\n" +
                "<CODE>INFO-000</CODE>\n" +
                "<MESSAGE>정상 처리되었습니다</MESSAGE>\n" +
                "</RESULT></GetParkInfo>");
        assertThat(document.getFirstChild().getNodeName(), equalTo("GetParkInfo"));
    }

    @Test
    void xmlDocumentToJson() {
        Document document = ApiResConverter.convertStringToXMLDocument("<GetParkInfo>\n" +
                "<list_total_count>14410</list_total_count>\n" +
                "<RESULT>\n" +
                "<CODE>INFO-000</CODE>\n" +
                "<MESSAGE>정상 처리되었습니다</MESSAGE>\n" +
                "</RESULT></GetParkInfo>");
        JSONObject jsonObject = ApiResConverter.xmlDocumentToJson(document.getChildNodes());
        JSONArray getParkInfo = jsonObject.getJSONArray("GetParkInfo");
        assertThat(getParkInfo.size(), equalTo(1));
    }

    @Test
    void xmlDocumentToJsonArray() {
        Document document = ApiResConverter.convertStringToXMLDocument("<GetParkInfo>\n" +
                "<list_total_count>14410</list_total_count>\n" +
                "<RESULT>\n" +
                "<CODE>INFO-000</CODE>\n" +
                "<MESSAGE>정상 처리되었습니다</MESSAGE>\n" +
                "</RESULT></GetParkInfo>");
        JSONArray jsonArray = ApiResConverter.xmlDocumentToJsonArray(document.getChildNodes());
        assertThat(jsonArray.size(), equalTo(1));
    }
}