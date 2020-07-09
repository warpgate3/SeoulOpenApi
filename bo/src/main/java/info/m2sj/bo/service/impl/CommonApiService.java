package info.m2sj.bo.service.impl;

import info.m2sj.bo.constants.ApiResultType;
import info.m2sj.bo.constants.ApiServiceType;
import info.m2sj.bo.dto.ApiParam;
import info.m2sj.bo.support.HttpProxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Map;

@Service
public abstract class CommonApiService {
    @Value("${api.credential_key}")
    String credentialKey;

    @Value("${api.api_server_url}")
    String apiServerUrl;

    private final HttpProxy httpProxy;

    private final ApiServiceType apiServiceType;

    private final ApiResultType apiResultType;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public CommonApiService(HttpProxy httpProxy, ApiServiceType apiServiceType,
                            ApiResultType apiResultType) {
        this.httpProxy = httpProxy;
        this.apiServiceType = apiServiceType;
        this.apiResultType = apiResultType;
    }

    String buildBaseUrl() {
        return apiServerUrl + credentialKey;
    }

    String buildDetailUrl() {
        return MessageFormat.format("{0}/{1}/{2}",
                buildBaseUrl(), apiResultType, apiServiceType);
    }

    Map<String, Object> sendGet(String fullUrl) {
        return httpProxy.sendGet(fullUrl);
    }

    abstract String buildSearchUrl(ApiParam apiParam);
}
