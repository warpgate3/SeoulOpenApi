package info.m2sj.bo.seoulapi.constants;

/**
 * 공공 API 오류 응답 코드
 */
public enum ApiErrResponseType {
    ERROR_300("ERROR-300","필수 값이 누락되어 있습니다."),
    ERROR_301("ERROR-301","파일타입 값이 누락 혹은 유효하지 않습니다. 요청인자 중 TYPE을 확인하십시오."),
    ERROR_310("ERROR-310","해당하는 서비스를 찾을 수 없습니다. 요청인자 중 SERVICE를 확인하십시오"),
    ERROR_331("ERROR-331","요청시작위치 값을 확인하십시오. 요청인자 중 START_INDEX를 확인하십시오"),
    ERROR_332("ERROR-332","요청종료위치 값을 확인하십시오. 요청인자 중 END_INDEX를 확인하십시오"),
    ERROR_333("ERROR-333","요청위치 값의 타입이 유효하지 않습니다. 요청위치 값은 정수를 입력하세요"),
    ERROR_334("ERROR-334","요청종료위치 보다 요청시작위치가 더 큽니다. 요청시작조회건수는 정수를 입력하세요"),
    ERROR_335("ERROR-335","샘플데이터(샘플키:sample) 는 한번에 최대 5건을 넘을 수 없습니다. 요청시작위치와 요청종료위치 값은 1 ~ 5 사이만 가능합니다"),
    ERROR_336("ERROR-336","데이터요청은 한번에 최대 1000건을 넘을 수 없습니다. 요청종료위치에서 요청시작위치를 뺀 값이 1000을 넘지 않도록 수정하세요"),
    ERROR_500("ERROR-500","서버 오류입니다. 지속적으로 발생시 열린 데이터 광장으로 문의(Q&A) 바랍니다"),
    ERROR_600("ERROR-600","데이터베이스 연결 오류입니다. 지속적으로 발생시 열린 데이터 광장으로 문의(Q&A) 바랍니다"),
    ERROR_601("ERROR0-601","SQL 문장 오류 입니다. 지속적으로 발생시 열린 데이터 광장으로 문의(Q&A) 바랍니다");

    private String responseCode;

    private String responseMessage;

    ApiErrResponseType(String responseCode, String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }

    /**
     * 입력 받는 에러코드 Enum 반환
     * @param errCode 에러코드
     * @return ApiErrResponseType
     */
    public static ApiErrResponseType getError(String errCode) {
        ApiErrResponseType[] values = ApiErrResponseType.values();
        for (ApiErrResponseType value : values) {
            if (value.getResponseCode().equals(errCode)) {
                return value;
            }
        }
        return null;
    }

    public String getResponseCode() {
       return this.responseCode;
    }

    public String getResponseMessage() {
       return this.responseMessage;
    }
}
