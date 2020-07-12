package info.m2sj.bo.domain.parkinfo.dto;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

/**
 * 주차장 정보 조회 검색 (UI Request)
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkInfoSearchRequest {
    /** 동 or 구 검색어*/
    @ApiParam(value = "동 or 구 검색어",  example = "서초구")
    private String wardName;

    /** 전화번호 */
    @ApiParam(value = "전화번호",  example = "02-111-2345")
    private String telNumber;

    /** 주자창 시설명 */
    @ApiParam(value = " 주자창 시설명", example = "마장동공영주차장")
    private String parkingName;

    /** 페이징 시작*/
    @ApiParam(value = "페이징 시작", example = "1")
    @Min(value = 1, message = "pageNumber should not be less than 1")
    private int pageNumber;

    /** 페이지당 보여질 주차장 정보 갯수 */
    @ApiParam(value = "페이지당 보여질 주차장 정보 갯수", example = "20")
    @Min(value = 1, message = "pageNumber should not be less than 1")
    private int pageScale;

    /** 사용자 위도 */
    @ApiParam(value = "사용자의 위도정보", example = "35.999995")
    private double myLat;

    /** 사용자 경도 */
    @ApiParam(value = "사용자의 경도정보 ", example = "124.1231255546")
    private double myLng;

    /** 위치정보 기준검색  */
    @ApiParam(value = "위치정보 기준으로 검색", example = "true")
    private boolean byCoord;
}
