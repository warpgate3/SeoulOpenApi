package info.m2sj.bo.domain.parkinfo.service;

import info.m2sj.bo.core.ParkInfoDataLoader;
import info.m2sj.bo.domain.parkinfo.dto.SearchParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.Range;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
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

        List<Map<String, String>> pageList = collect.subList(fromToPageNumber.get(0), fromToPageNumber.get(1));

        //주차 가능 여부 정보 저장
        pageList.forEach(m -> m.put("AVAILABLE_PARKING", isAvailableParking(m) ? "Y" : "N"));
        return Flux.just(pageList);
    }

    /**
     * 주차 가능 여부 확인
     * <p>
     * CAPACITY 확인
     * 오늘 날짜 확인 (평일, 주말, 공휴일)
     * 시간 확인
     *
     * @return
     */
    private boolean isAvailableParking(Map<String, String> parkingLot) {
        String capacity = parkingLot.get("CAPACITY");
        if (StringUtils.isEmpty(capacity) || Objects.equals(capacity, "0")) {
            return false;
        }
        LocalDate day = LocalDate.now(ZoneId.of("Asia/Seoul"));
        int now = timeToNumber();
        //TODO :공휴일 체크 제외
        if (isWeekEnd(day)) {
            return contains(now, timeToNumber(parkingLot.get("WEEKEND_BEGIN_TIME")),
                    timeToNumber(parkingLot.get("WEEKEND_END_TIME")));
        }
        return contains(now, timeToNumber(parkingLot.get("WEEKDAY_BEGIN_TIME")),
                timeToNumber(parkingLot.get("WEEKDAY_END_TIME")));
    }

    private boolean contains(int target, int from, int to) {
        log.debug("{} < {} < {} ",from ,target, to);
        Range<Integer> range = Range.between(from, to);
        return range.contains(target);
    }

    private int timeToNumber() {
        LocalTime time = LocalTime.now(ZoneId.of("Asia/Seoul"));
        String hours = String.format("%02d", time.getHour());
        String min = String.format("%02d", time.getMinute());
        return timeToNumber(hours + min);
    }

    private int timeToNumber(String timeText) {
        return Integer.parseInt(timeText);
    }

    private boolean isWeekEnd(LocalDate today) {
        return SUNDAY == today.getDayOfWeek() || SATURDAY == today.getDayOfWeek();
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
