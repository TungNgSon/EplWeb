package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String apiUrl;

    public Map<String, Object> getWeather(String city) {
        RestTemplate restTemplate = new RestTemplate();
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("q", city)
                .queryParam("appid", apiKey)
                .queryParam("units", "metric")
                .toUriString();
        System.out.println("URL LA: "+url);
        Map<String, Object> response = restTemplate.getForObject(url, HashMap.class);
        return response;
    }
    public Map<String, Object> getWeather1(double latitude,double longtitude) {
        RestTemplate restTemplate = new RestTemplate();
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("lat", latitude)
                .queryParam("lon", longtitude)
                .queryParam("appid", apiKey)
                .queryParam("units", "metric")
                .toUriString();
        System.out.println("URL LA: "+url);
        Map<String, Object> response = restTemplate.getForObject(url, HashMap.class);
        return response;
    }
}