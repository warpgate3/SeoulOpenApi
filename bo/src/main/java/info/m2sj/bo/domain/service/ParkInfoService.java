package info.m2sj.bo.domain.service;

import info.m2sj.bo.core.ParkInfoDataLoader;
import info.m2sj.bo.domain.dto.SearchParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;

@Service
@Slf4j
public class ParkInfoService {
    private final ParkInfoDataLoader parkInfoDataLoader;

    public ParkInfoService(ParkInfoDataLoader parkInfoDataLoader) {
        this.parkInfoDataLoader = parkInfoDataLoader;
    }

    public Flux<List<Map<String, String>>> findParkInfoListBy(SearchParam searchParam) {
        List<Map<String, String>> parkInfoData = parkInfoDataLoader.getParkInfoData();

        Stream<Map<String, String>> mapStream = parkInfoData.parallelStream();

        if (StringUtils.isNotEmpty(searchParam.getParkingName())) {
            mapStream = mapStream
                    .filter(getSearchFilter("PARKING_NAME", searchParam.getParkingName()));
        }

        if (StringUtils.isNotEmpty(searchParam.getTelNumber())) {
            mapStream = mapStream
                    .filter(getSearchFilter("TEL", searchParam.getTelNumber()));
        }

        if (StringUtils.isNotEmpty(searchParam.getWardName())) {
            mapStream = mapStream
                    .filter(getSearchFilter("ADDR", searchParam.getWardName()));
        }

        mapStream = mapStream.sorted(Comparator.comparingInt(getSort()));

        List<Map<String, String>> collect = mapStream.collect(Collectors.toList());
        List<Integer> fromToPageNumber = getFromToPageNumber(collect, searchParam.getPageNumber(), searchParam.getPageScale());
        return Flux.just(collect.subList(fromToPageNumber.get(0), fromToPageNumber.get(1)));
    }

    private ToIntFunction<Map<String, String>> getSort() {
        int defaultTime = 30;
        return (m) -> {
            String rates = m.get("RATES");
            String timeRate = m.get("TIME_RATE");
            if (isPay(m)) {
                if (isAllNumeric(rates, timeRate)) {
                    return Integer.parseInt(rates) * (defaultTime / Integer.parseInt(timeRate));
                }
                return Integer.MAX_VALUE; //숫자가 아닌경우 비정상으로 간주 최대값 설정해서 뒤로 보낸다.
            }
            return 0;
        };
    }

    private Predicate<Map<String, String>> getSearchFilter(String jsonKey, String searchText) {
        return (m) -> nonNull(m.get(jsonKey)) && m.get(jsonKey)
                .contains(searchText);
    }

    private List<Integer> getFromToPageNumber(List<Map<String, String>> target, int pageNumber, int pageScale) {
        int totalSize = target.size();
        int fromIdx = (pageNumber - 1) * pageScale;
        int toIdx = pageNumber * pageScale;
        if (toIdx > totalSize) {
            toIdx = totalSize;
        }
        log.info("total size:::>{}, pageNumber:::>{}, pageScale:::>{}", target.size(), pageNumber, pageScale);
        log.info("from:{} -> to:{}", fromIdx, toIdx);
        return List.of(fromIdx, toIdx);
    }

    private boolean isAllNumeric(String rates, String timeRate) {
        return StringUtils.isNotEmpty(rates)
                && StringUtils.isNotEmpty(timeRate)
                && !"0".equals(rates)
                && !"0".equals(timeRate);
    }

    private boolean isPay(Map<String, String> param) {
        return Objects.equals(param.get("PAY_YN"), "Y");
    }
}
