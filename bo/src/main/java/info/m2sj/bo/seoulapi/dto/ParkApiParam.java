package info.m2sj.bo.seoulapi.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class ParkApiParam extends ApiParam {
    private String addr = "";

    private String parkingCode = "";
}
