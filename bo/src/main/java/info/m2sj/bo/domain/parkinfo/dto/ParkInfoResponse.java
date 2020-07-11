package info.m2sj.bo.domain.parkinfo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 주차장 정보 Client Response
 *
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class ParkInfoResponse {
    @ApiModelProperty(value = "주차장 이름")
    private String parkingName;

    @ApiModelProperty(value = "주차장 주소")
    private String addr;

    @ApiModelProperty(value = "주차장 코드")
    private String parkingCode;

    @ApiModelProperty(value = "주차장 유형명")
    private String parkingTypeNm;

    @ApiModelProperty(value = "주차장 운영형태")
    private String operationRuleNm;

    @ApiModelProperty(value = "주차장 전화번호")
    private String tel;

    @ApiModelProperty(value = "유료 여부")
    private String payYn;

    @ApiModelProperty(value = "기본 주차 요금")
    private String rates;

    @ApiModelProperty(value = "기본 주차 시간(분 단위)")
    private String timeRate;

    @ApiModelProperty(value = "주차장 사용가능 여부(현시간 기준)")
    private String availableParkingYn;
}
