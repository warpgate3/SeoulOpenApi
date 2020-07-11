package info.m2sj.bo.seoulapi.support;

import info.m2sj.bo.core.exceptions.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * HTTP 요청 연동 클래스
 */
@Service
@Slf4j
public class HttpProxy {
    private final HttpClient httpClient;

    public HttpProxy() {
        this.httpClient = HttpClient.newBuilder()
                .build();
    }

    /**
     * 입력받은 URL을 Get 요청
     *
     * @param url 요청 URL
     * @return 응답결과 문자열
     */
    public String sendGet(String url) {
        log.info("request url :::> {}", url);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .build();
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException | InterruptedException e) {
            throw new ApiException(e.getMessage());
        }
    }
}
