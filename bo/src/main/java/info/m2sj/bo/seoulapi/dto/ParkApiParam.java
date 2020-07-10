package info.m2sj.bo.seoulapi.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


/**
 * 공용 주차장 API 조회 검색 조건
 */
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class ParkApiParam extends ApiParam {
    /** 주소  */
    private String addr = "";

    /** 주차장 코드 */
    private String parkingCode = "";
}
