package info.m2sj.bo.core;

import info.m2sj.bo.seoulapi.dto.ParkApiParam;
import info.m2sj.bo.seoulapi.service.ApiService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static info.m2sj.bo.seoulapi.constants.ApiResultType.XML;

@Service
@Slf4j
public class ParkInfoDataLoader {
    private final ApiService apiService;

    public ParkInfoDataLoader(ApiService apiService) {
        this.apiService = apiService;
    }

    @Cacheable(value = "park-data", key = "'park-date-key'")
    public List<Map<String, String>> getParkInfoData() {
        ParkApiParam parkApiParam = new ParkApiParam();
        parkApiParam.setStartIndex(1);
        parkApiParam.setEndIndex(1000);
        parkApiParam.setApiResultType(XML);
        JSONObject apiData = apiService.call(parkApiParam);
        int totalCount = Integer.parseInt(String.valueOf(apiData.get("list_total_count")));
        log.info("ParkInfo Data Total Rows => {}", totalCount);
        JSONArray rows = apiData.getJSONArray("row");

        if (totalCount > 1000) {
            int l = totalCount / 1000;
            if (totalCount % 1000 > 0) {
                l = l + 1;
            }
            int startIdx = 1001;
            for (int s = 1; s < l; s++) {
                parkApiParam.setStartIndex(startIdx);
                int e = startIdx + 999;
                parkApiParam.setEndIndex(e);
                JSONObject data = apiService.call(parkApiParam);
                JSONArray addedRows;
                try {
                    addedRows = data.getJSONArray("row");
                    rows.addAll(addedRows);
                } catch (JSONException jsonException) {
                    log.error(jsonException.getMessage(), "skip data!!");
                }

                startIdx = e + 1;
            }
        }
        return (List<Map<String, String>>) rows;
    }

    @CacheEvict(value = "park-data", allEntries = true)
    public void evictAllCacheValues() {}
}
