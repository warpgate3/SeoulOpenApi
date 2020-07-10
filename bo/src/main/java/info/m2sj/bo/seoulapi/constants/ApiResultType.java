package info.m2sj.bo.seoulapi.constants;

/**
 * 공공 API 응답 format
 */
public enum ApiResultType {
    JSON("json"),
    XML("xml"),
    EXCEL("xls");

    private String code;

    ApiResultType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
