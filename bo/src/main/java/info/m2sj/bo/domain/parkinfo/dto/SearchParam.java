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
public class SearchParam {
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
}
