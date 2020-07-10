package info.m2sj.bo.seoulapi.service;

import info.m2sj.bo.seoulapi.dto.ApiParam;
import net.sf.json.JSONObject;

/**
 * 공공 API 호출 서비스
 */
public interface ApiService {
    /**
     * API 요청
     * @param param 요청조건
     * @return
     */
    JSONObject call(ApiParam param);
}
