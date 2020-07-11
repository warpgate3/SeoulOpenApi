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

/**
 * 공용주차정 데이터 적재 클래스
 */
@Service
@Slf4j
public class ParkInfoDataLoader {
    private final ApiService apiService;

    /** 공공 API 1회 호출당 최대 조회 아이템 건수*/
    private static final int OPEN_API_MAX_RESULT = 1000;

    public ParkInfoDataLoader(ApiService apiService) {
        this.apiService = apiService;
    }

    /**
     * 공용 주차장 전체 데이터를 조회한다.
     * 애플리케이션 startup 시 실행되고 Scheduler 가 주기적으로 호출해서  최신 데이터로  갱신 처리한다.
     * 현재 API 1회 호출당 최대 조회건수 1000건으로 제한돼 있기 때문에
     * 요청 처리 결과 건수가 1000건이 넘을 경우 전체건수에 도달할 때 까지 재요청한다.
     * @return 공용주차장 정보 리스트
     */
    @Cacheable(value = "park-data", key = "'park-date-key'")
    public List<Map<String, String>> getParkInfoData() {
        ParkApiParam parkApiParam = new ParkApiParam();
        parkApiParam.setStartIndex(1);
        parkApiParam.setEndIndex(OPEN_API_MAX_RESULT);
        parkApiParam.setApiResultType(XML);

        //공공 API 호출
        JSONObject apiData = apiService.call(parkApiParam);

        int totalCount = Integer.parseInt(String.valueOf(apiData.get("list_total_count")));
        log.info("ParkInfo Data Total Rows => {}", totalCount);
        JSONArray rows = apiData.getJSONArray("row");

        if (totalCount > OPEN_API_MAX_RESULT) {
            //전체 건수를 1000건으로 나누어 조회 요청할 횟수 계산
            int l = totalCount / OPEN_API_MAX_RESULT;

            //1000건으로 나누고 나머지가 있을 경우 1회 더 요청
            if (totalCount % OPEN_API_MAX_RESULT > 0) {
                l = l + 1;
            }

            //마지막 조회 아이템 + 1 이 시작건수
            int startIdx = OPEN_API_MAX_RESULT + 1;
            for (int s = 1; s < l; s++) {
                parkApiParam.setStartIndex(startIdx);
                int e = startIdx + OPEN_API_MAX_RESULT - 1;
                parkApiParam.setEndIndex(e);

                //공공 API 재 호출
                JSONObject data = apiService.call(parkApiParam);
                JSONArray addedRows;
                try {
                    //조회 결과 Merge
                    addedRows = data.getJSONArray("row");
                    rows.addAll(addedRows);
                } catch (JSONException jsonException) {
                    //OPEN API 장애일 경우 Skip 처리
                    log.error(jsonException.getMessage(), "skip data!!");
                }

                //직전 조회의 endIdx + 1 이 시작 index가 된다.
                startIdx = e + 1;
            }
        }
        return rows;
    }

    /**
     * 공공주차장 정보 cache데이터 삭제 처리
     */
    @CacheEvict(value = "park-data", allEntries = true)
    public void evictAllCacheValues() {}
}
