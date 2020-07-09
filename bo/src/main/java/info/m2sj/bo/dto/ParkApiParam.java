package info.m2sj.bo.dto;

import lombok.*;


@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class ParkApiParam extends ApiParam {
    private String addr;

    private String parkingCode;
}
