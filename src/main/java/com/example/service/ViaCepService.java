package com.example.service;

import com.example.model.ViaCepResponse;
import com.google.gson.Gson;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.util.concurrent.CompletableFuture;

public class ViaCepService {
    private static final String BASE_URL = "https://viacep.com.br/ws/";
    private static ViaCepService instance;
    private final CloseableHttpClient httpClient;
    private final Gson gson;

    private ViaCepService() {
        this.httpClient = HttpClients.createDefault();
        this.gson = new Gson();
    }

    public static ViaCepService getInstance() {
        if (instance == null) {
            instance = new ViaCepService();
        }
        return instance;
    }

    public CompletableFuture<ViaCepResponse> getAddressByCep(String cep) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String cleanCep = cep.replaceAll("[^0-9]", "");
                
                if (cleanCep.length() != 8) {
                    throw new IllegalArgumentException("CEP must have 8 digits");
                }
                
                String url = BASE_URL + cleanCep + "/json/";
                
                HttpGet request = new HttpGet(url);
                
                try (ClassicHttpResponse response = httpClient.executeOpen(null, request, null)) {
                    String jsonResponse = EntityUtils.toString(response.getEntity());
                    
                    ViaCepResponse viaCepResponse = gson.fromJson(jsonResponse, ViaCepResponse.class);
                    
                    if (viaCepResponse.hasError()) {
                        throw new Exception("CEP not found: " + cep);
                    }
                    
                    return viaCepResponse;
                }
                
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(e.getMessage(), e);
            } catch (Exception e) {
                throw new RuntimeException("Failed to fetch address from ViaCEP: " + e.getMessage(), e);
            }
        });
    }

    public boolean isValidCepFormat(String cep) {
        if (cep == null || cep.trim().isEmpty()) {
            return false;
        }
        
        String cleanCep = cep.replaceAll("[^0-9]", "");
        return cleanCep.length() == 8;
    }

    public String formatCep(String cep) {
        String cleanCep = cep.replaceAll("[^0-9]", "");
        
        if (cleanCep.length() != 8) {
            return cep;
        }
        
        return cleanCep.substring(0, 5) + "-" + cleanCep.substring(5);
    }

    public void close() {
        try {
            if (httpClient != null) {
                httpClient.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
