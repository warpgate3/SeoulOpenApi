package info.m2sj.bo.seoulapi.dto;

import info.m2sj.bo.seoulapi.constants.ApiResultType;
import lombok.Getter;
import lombok.Setter;

/**
 * 공공API 조회 공통 검색 조건
 */
@Getter
@Setter
public class ApiParam {
    /** 시작 번호*/
    protected long startIndex;

    /** 페이지당 보여질 아이템수 */
    protected long endIndex;

    /** 응답 문서 type */
    protected ApiResultType apiResultType;
}
