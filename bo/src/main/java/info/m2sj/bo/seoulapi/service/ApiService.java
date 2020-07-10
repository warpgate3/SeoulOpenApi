package info.m2sj.bo.seoulapi.service;

import info.m2sj.bo.seoulapi.dto.ApiParam;
import net.sf.json.JSONObject;

public interface ApiService {
    JSONObject call(ApiParam param);
}
