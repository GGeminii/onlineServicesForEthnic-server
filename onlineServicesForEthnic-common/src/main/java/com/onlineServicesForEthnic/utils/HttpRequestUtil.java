package com.onlineServicesForEthnic.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlineServicesForEthnic.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * 用来进行一些http请求
 */
@RequiredArgsConstructor
@Component
public class HttpRequestUtil {
    private final ObjectMapper objectMapper;

    /**
     * get-API的请求,指定泛型则返回对象
     * @param url url
     * @param clazz 请求体类型
     * @return 响应体
     * @param <T> 请求体范型
     */
    public <T> T  sendGetRequest(String url,Class<T> clazz) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();
            return objectMapper.readValue(body,clazz);
        } catch (IOException | InterruptedException e) {
            throw new ServiceException(url+"api请求失败");
        }
    }

    /**
     * get-API的请求
     * @param url url
     * @return 响应体字符串
     */
    public String sendGetRequest(String url) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new ServiceException(url+"api请求失败");
        }
    }


}
