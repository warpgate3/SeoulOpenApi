package info.m2sj.bo.seoulapi.service.impl;

import info.m2sj.bo.core.exceptions.BaseException;
import info.m2sj.bo.seoulapi.dto.ApiParam;
import info.m2sj.bo.seoulapi.dto.ParkApiParam;
import info.m2sj.bo.seoulapi.service.ApiService;
import info.m2sj.bo.seoulapi.support.HttpProxy;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import static info.m2sj.bo.seoulapi.constants.ApiServiceType.GetParkInfo;


@Service
@Slf4j
public class ParkApiService extends CommonApiService implements ApiService {
    public ParkApiService(HttpProxy httpProxy) {
        super(httpProxy, GetParkInfo);
    }

    @Override
    public JSONObject call(ApiParam apiParam) {
        return super.sendGet(buildDetailUrl(apiParam) + buildSearchUrl(apiParam) , apiParam.getApiResultType());
    }

    @Override
    String buildSearchUrl(ApiParam apiParam) {
       if (apiParam instanceof ParkApiParam) {
           ParkApiParam searchParam = (ParkApiParam) apiParam;
           return String.format("/%s/%s/%s/%s",
                   searchParam.getStartIndex(),
                   searchParam.getEndIndex(),
                   searchParam.getAddr(),
                   searchParam.getParkingCode());
       }

       throw new BaseException("unsupported search param");
    }
}
