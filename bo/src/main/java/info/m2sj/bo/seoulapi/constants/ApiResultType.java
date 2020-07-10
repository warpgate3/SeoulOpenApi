package info.m2sj.bo.seoulapi.constants;

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
