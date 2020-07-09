package info.m2sj.bo.service.impl;

import info.m2sj.bo.dto.ApiParam;
import info.m2sj.bo.dto.ParkApiParam;
import info.m2sj.bo.exceptions.BaseException;
import info.m2sj.bo.service.ApiService;
import info.m2sj.bo.support.HttpProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Map;

import static info.m2sj.bo.constants.ApiResultType.JSON;
import static info.m2sj.bo.constants.ApiServiceType.GetParkInfo;

@Service
@Slf4j
public class ParkApiService extends CommonApiService implements ApiService {
    public ParkApiService(HttpProxy httpProxy) {
        super(httpProxy, GetParkInfo, JSON);
    }

    @Override
    public Map<String, Object> call(ApiParam apiParam) {
        return sendGet(buildDetailUrl() + buildSearchUrl(apiParam));
    }

    @Override
    String buildSearchUrl(ApiParam apiParam) {
       if (apiParam instanceof ParkApiParam) {
           ParkApiParam searchParam = (ParkApiParam) apiParam;
           return MessageFormat.format("/{0}/{1}/{2}/{3}",
                   searchParam.getStartIndex(),
                   searchParam.getEndIndex(),
                   searchParam.getAddr(),
                   searchParam.getParkingCode());
       }

       throw new BaseException("unsupported search param");
    }
}
