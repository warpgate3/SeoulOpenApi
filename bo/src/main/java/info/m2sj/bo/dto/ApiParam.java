package info.m2sj.bo.dto;

import info.m2sj.bo.constants.ApiResultType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiParam {
    protected long startIndex;

    protected long endIndex;

    protected ApiResultType apiResultType;
}
