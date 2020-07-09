package info.m2sj.bo.support;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.m2sj.bo.exceptions.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@Service
@Slf4j
public class HttpProxy {
    private final HttpClient httpClient;

    public HttpProxy() {
        this.httpClient = HttpClient.newBuilder()
                .build();
    }

    public Map<String, Object> sendGet(String url) {
        log.info("request url :::> {}", url);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            return new ObjectMapper().readValue(response.body(), new TypeReference<>() {
            });
        } catch (IOException | InterruptedException e) {
            throw new ApiException(e.getMessage());
        }
    }
}
