package com.example.api;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import com.google.gson.Gson;

public class ApiClient {
    private final CloseableHttpClient httpClient;
    private final Gson gson;

    public ApiClient() {
        this.httpClient = HttpClients.createDefault();
        this.gson = new Gson();
    }

    public String get(String url) throws Exception {
        HttpGet request = new HttpGet(url);
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            return EntityUtils.toString(response.getEntity());
        }
    }

    public void close() throws Exception {
        httpClient.close();
    }
}
