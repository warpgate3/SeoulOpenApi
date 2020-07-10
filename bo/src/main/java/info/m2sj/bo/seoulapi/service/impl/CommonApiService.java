package info.m2sj.bo.seoulapi.service.impl;

import info.m2sj.bo.core.exceptions.ApiException;
import info.m2sj.bo.core.exceptions.BaseException;
import info.m2sj.bo.seoulapi.constants.ApiErrResponseType;
import info.m2sj.bo.seoulapi.constants.ApiResultType;
import info.m2sj.bo.seoulapi.constants.ApiServiceType;
import info.m2sj.bo.seoulapi.dto.ApiParam;
import info.m2sj.bo.seoulapi.support.HttpProxy;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.text.MessageFormat;
import java.util.Optional;

import static info.m2sj.bo.seoulapi.constants.ApiErrResponseType.getError;
import static info.m2sj.bo.seoulapi.service.impl.util.ApiResConverter.convertStringToXMLDocument;
import static info.m2sj.bo.seoulapi.service.impl.util.ApiResConverter.xmlDocumentToJson;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.isEmpty;

/**
 * 공공 API 서비스 요청 공통 클래스
 * 공공 API 서비스를 추가할 경우 해당 클래스를 상속 받아야 된다.
 */
@Service
public abstract class CommonApiService {
    @Value("${api.credential_key}")
    String credentialKey;

    @Value("${api.api_server_url}")
    String apiServerUrl;

    private final HttpProxy httpProxy;

    private final ApiServiceType apiServiceType;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public CommonApiService(HttpProxy httpProxy, ApiServiceType apiServiceType) {
        this.httpProxy = httpProxy;
        this.apiServiceType = apiServiceType;
    }

    String buildBaseUrl() {
        return apiServerUrl + credentialKey;
    }

    String buildDetailUrl(ApiParam apiParam) {
        return MessageFormat.format("{0}/{1}/{2}",
                buildBaseUrl(), apiParam.getApiResultType(), apiServiceType);
    }

    JSONObject sendGet(String fullUrl, ApiResultType resultType) {
        if (isEmpty(fullUrl) || isNull(resultType)) {
            throw new BaseException("check parameter:::> fullUrl:[" + fullUrl +"], resultType:[" +resultType+"]");
        }
        String responseMsg = httpProxy.sendGet(fullUrl);
        switch (resultType) {
            case JSON:
                //TODO: JSON 리턴일 경우
            case XML:
                Document respXml = convertStringToXMLDocument(responseMsg);
                checkErrResponse(respXml);
                return xmlDocumentToJson(respXml.getFirstChild().getChildNodes());
            default:
                throw new BaseException("Require ApiResultType [xml, json..]");
        }
    }

    private void checkErrResponse(Document target) {
        Node root = target.getFirstChild();
        NodeList childNodes  = root.getChildNodes();
        getNodeBy(childNodes, "CODE").ifPresent(n -> {
            ApiErrResponseType error = getError(n.getTextContent());
            if (nonNull(error)) {
               throw new ApiException(error.getResponseMessage());
           }
        });
    }


    private Optional<Node> getNodeBy(NodeList childNodes, String nodeName) {
        if (isNull(childNodes) || isNull(nodeName)) {
            throw new BaseException("check parameter:::> childNodes:[" + childNodes +"], nodeName:[" +nodeName+"]");
        }
        Node child;
        for (int i = 0; i < childNodes.getLength(); i++) {
            child = childNodes.item(i);
            if (nodeName.equals(child.getNodeName())) {
               return Optional.of(child);
            }
        }
        return Optional.empty();
    }

    abstract String buildSearchUrl(ApiParam apiParam);
}
