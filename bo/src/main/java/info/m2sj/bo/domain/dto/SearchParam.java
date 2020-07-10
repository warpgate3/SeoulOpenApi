package info.m2sj.bo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchParam {
    private String wardName;

    private String telNumber;

    private String parkingName;

    @Min(value = 1, message = "pageNumber should not be less than 1")
    private int pageNumber;

    @Min(value = 1, message = "pageNumber should not be less than 1")
    private int pageScale;
}
