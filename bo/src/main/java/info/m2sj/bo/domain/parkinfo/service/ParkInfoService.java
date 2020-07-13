package info.m2sj.bo.domain.parkinfo.service;

import info.m2sj.bo.core.ParkInfoDataLoader;
import info.m2sj.bo.domain.parkinfo.dto.ParkInfoPagingResponse;
import info.m2sj.bo.domain.parkinfo.dto.ParkInfoResponse;
import info.m2sj.bo.domain.parkinfo.dto.ParkInfoSearchRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.Range;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.sqrt;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * 주차장 정보 조회 처리 서비스
 * - 페이징된 주차정 정보 리스트를 반환
 * - 주차장명, 전화번호, 주소로 검색 가능
 * - 최저요금 순으로 정렬 처리(30분 기준)
 * - 주차가능여부정보 체크(현재시각기준)
 */
@Service
@Slf4j
public class ParkInfoService {
    private final ParkInfoDataLoader parkInfoDataLoader;

    public ParkInfoService(ParkInfoDataLoader parkInfoDataLoader) {
        this.parkInfoDataLoader = parkInfoDataLoader;
    }

    /**
     * 주차장 정보 조회 처리
     *
     * @param parkInfoSearchRequest 검색조건
     * @return 페이징된 주차장 정보 리스트
     */
    public Mono<ParkInfoPagingResponse> findParkInfoListBy(ParkInfoSearchRequest parkInfoSearchRequest) {
        //cache된 주차장 정보 조회
        List<Map<String, String>> parkInfoData = parkInfoDataLoader.getParkInfoData();

        //병렬 스트림 생성
        Stream<Map<String, String>> mapStream = parkInfoData.parallelStream();

        //검색 처리
        if (StringUtils.isNotEmpty(parkInfoSearchRequest.getParkingName())) {
            mapStream = mapStream.filter(getSearchFilter("PARKING_NAME",
                    parkInfoSearchRequest.getParkingName()));
        }

        if (StringUtils.isNotEmpty(parkInfoSearchRequest.getTelNumber())) {
            mapStream = mapStream.filter(getSearchFilter("TEL",
                    parkInfoSearchRequest.getTelNumber()));
        }

        if (StringUtils.isNotEmpty(parkInfoSearchRequest.getWardName())) {
            mapStream = mapStream.filter(getSearchFilter("ADDR",
                    parkInfoSearchRequest.getWardName()));
        }

        //정렬 처리
        if (parkInfoSearchRequest.isByCoord()) { //나의 위치 기준
            mapStream = mapStream.sorted(Comparator.comparingDouble(
                    getSortByCoord(parkInfoSearchRequest.getMyLat(), parkInfoSearchRequest.getMyLng())));
        } else {
            mapStream = mapStream.sorted(Comparator.comparingInt(getSortByRate()));
        }

        //페이징처리
        List<Map<String, String>> collect = mapStream.collect(Collectors.toList());
        List<Integer> fromToPageNumber = getFromToPageNumber(collect, parkInfoSearchRequest.getPageNumber(), parkInfoSearchRequest.getPageScale());
        List<Map<String, String>> pageList = collect.subList(fromToPageNumber.get(0), fromToPageNumber.get(1));

        //DTO 변환
        List<ParkInfoResponse> parkInfoResponseList = pageList.stream()
                .map(m -> mapToDtoAndAvailableParkYn(m).orElse(null))
                .collect(Collectors.toList());

        return Mono.just(ParkInfoPagingResponse.builder()
                .pageNumber(parkInfoSearchRequest.getPageNumber())
                .pageScale(parkInfoSearchRequest.getPageScale())
                .totalCount(collect.size())
                .parkInfoResponseList(parkInfoResponseList)
                .build());
    }

    /**
     * 현재 시간 기준으로 주차 가능 여부 확인
     * <p>
     * CAPACITY 확인
     * 오늘 날짜 확인 (평일, 주말, 공휴일)
     * 시간 확인
     *
     * @return true:주차가능, false:주차불가
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

    /**
     * target 값이 from ~ to 사이에 존재하는 검사
     *
     * @param target 검색 대상
     * @param from   검색 범위 시작
     * @param to     검색 범위 끝
     * @return true:포함, false:불포함
     */
    private boolean contains(int target, int from, int to) {
        log.debug("{} < {} < {} ", from, target, to);
        Range<Integer> range = Range.between(from, to);
        return range.contains(target);
    }

    /**
     * 현재 시간,분을 숫자로 반환한다.
     * ex) PM 02:05 ---> 1405
     *
     * @return 시간, 분 int변환 값
     */
    private int timeToNumber() {
        LocalTime time = LocalTime.now(ZoneId.of("Asia/Seoul"));
        String hours = String.format("%02d", time.getHour());
        String min = String.format("%02d", time.getMinute());
        return timeToNumber(hours + min);
    }

    /**
     * 문자열을 숫자로 변환
     * 주차장 개장,폐장 시간을 range 계산을 위해
     *
     * @param timeText 숫자형 문자열
     * @return 입력받은 문자열의 int 형
     */
    private int timeToNumber(String timeText) {
        return Integer.parseInt(timeText);
    }

    /**
     * 입력받는 날짜의 주말 여부 확인
     *
     * @param today 검색할 날짜
     * @return true: 주말,  false:평일
     */
    private boolean isWeekEnd(LocalDate today) {
        return SUNDAY == today.getDayOfWeek() || SATURDAY == today.getDayOfWeek();
    }

    /**
     * 정렬 처리 함수 반환
     * 30분당 기본 요금이 적은 값으로 정렬
     * TODO:주말 평일 체크
     *
     * @return ToIntFunction
     */
    private ToIntFunction<Map<String, String>> getSortByRate() {
        int defaultTime = 30;
        return (parkingLotMap) -> {
            String rates = parkingLotMap.get("RATES");
            String timeRate = parkingLotMap.get("TIME_RATE");
            if (isPay(parkingLotMap)) {
                if (isAllNumeric(rates, timeRate)) {
                    return Integer.parseInt(rates) * (defaultTime / Integer.parseInt(timeRate));
                }
                return Integer.MAX_VALUE; //숫자가 아닌경우 비정상으로 간주 최대값 설정해서 뒤로 보낸다.
            }
            return 0;
        };
    }

    /**
     * 검색용 처리 필터
     * 입력받은 키에 해당하는 값이 searchText를 포함 하는 체크하는 함수 반환
     *
     * @param jsonKey    검색 대상 json key
     * @param searchText 검색어
     * @return Predicate
     */
    private Predicate<Map<String, String>> getSearchFilter(String jsonKey, String searchText) {
        return (parkingLotMap) -> nonNull(parkingLotMap.get(jsonKey)) && parkingLotMap.get(jsonKey)
                .contains(searchText);
    }

    /**
     * 페이징 처리
     * 입력값에 해당하는 페이징 리스트를 반환
     *
     * @param target     대상 리스트
     * @param pageNumber 시작번호
     * @param pageScale  page당 아이템 갯수
     * @return 화면에 표시할 리스트
     */
    private List<Integer> getFromToPageNumber(List<Map<String, String>> target, int pageNumber, int pageScale) {
        int totalSize = target.size();
        int fromIdx = (pageNumber - 1) * pageScale;
        int toIdx = pageNumber * pageScale;
        if (toIdx > totalSize) {
            toIdx = totalSize;
        }
        log.debug("total size:::>{}, pageNumber:::>{}, pageScale:::>{}", target.size(), pageNumber, pageScale);
        log.debug("from:{} -> to:{}", fromIdx, toIdx);
        return List.of(fromIdx, toIdx);
    }

    /**
     * 입력받은 2개 문자열이 숫자인지 확인
     *
     * @param rates    기본요금
     * @param timeRate 기본 주차시간
     * @return true: 숫자형문자 , false: 한개 이상의 숫자가 아닌 문자열 포함
     */
    private boolean isAllNumeric(String rates, String timeRate) {
        return StringUtils.isNotEmpty(rates)
                && StringUtils.isNotEmpty(timeRate)
                && !"0".equals(rates)
                && !"0".equals(timeRate);
    }

    /**
     * 주차장, 유 무료 상태값 확인
     *
     * @param param 주차장 정보
     * @return true:유료, false:무료
     */
    private boolean isPay(Map<String, String> param) {
        return Objects.equals(param.get("PAY_YN"), "Y");
    }

    /**
     * 좌표 기반 정렬
     * @param myLat 나의 위치  위도
     * @param myLng 나의 위치 경도
     * @return 주차장과의 거리
     */
    private ToDoubleFunction<Map<String, String>> getSortByCoord(double myLat, double myLng) {
        return (m) -> {
            if (StringUtils.isEmpty(m.get("LAT")) || StringUtils.isEmpty(m.get("LNG"))) {
                return Double.MAX_VALUE;
            }
            double lat = Double.parseDouble(m.get("LAT"));
            double lng = Double.parseDouble(m.get("LNG"));
            double distance = sqrt((lat - myLat) * (lat - myLat) + (lng - myLng) * (lng - myLng));
            return distance;
        };
    }

    /**
     * Map형으로 담겨져있는 주차정 정보를 DTO 클래스로 변환
     * 변환처리를 하면서 주차장 사용가능 여부 정보도 담는다.
     * @param parkingLot 주차장 정보
     * @return 주차장정보 DTO
     */
    private Optional<ParkInfoResponse> mapToDtoAndAvailableParkYn(Map<String, String> parkingLot) {
        if (isNull(parkingLot)) {
            return Optional.empty();
        }

        ParkInfoResponse parkInfoResponse = ParkInfoResponse.builder()
                .parkingCode(parkingLot.get("PARKING_CODE"))
                .parkingName(parkingLot.get("PARKING_NAME"))
                .operationRuleNm(parkingLot.get("OPERATION_RULE_NM"))
                .payYn(parkingLot.get("PAY_YN"))
                .parkingTypeNm(parkingLot.get("PARKING_TYPE_NM"))
                .rates(parkingLot.get("RATES"))
                .addr(parkingLot.get("ADDR"))
                .tel(parkingLot.get("TEL"))
                .timeRate(parkingLot.get("TIME_RATE"))
                .build();
        //주차장 이용 가능여부 확인
        parkInfoResponse.setAvailableParkingYn(isAvailableParking(parkingLot) ? "Y" : "N");

        return Optional.of(parkInfoResponse);
    }
}
