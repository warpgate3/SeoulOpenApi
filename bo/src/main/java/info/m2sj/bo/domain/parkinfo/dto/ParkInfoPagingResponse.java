package info.m2sj.bo.domain.parkinfo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParkInfoPagingResponse {
    /** 주차장 정보 리스트 */
    List<ParkInfoResponse> parkInfoResponseList;

    /** 총 아이템수 */
    int totalCount;

    /** 현재 페이지번호 */
    int pageNumber;

    /** 페이지당 표시갯수 */
    int pageScale;
}
