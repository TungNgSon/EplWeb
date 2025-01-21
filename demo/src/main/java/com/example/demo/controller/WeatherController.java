package com.example.demo.controller;

import com.example.demo.service.impl.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/weather")
    public String getWeather(@RequestParam(value = "city", required = false, defaultValue = "Hanoi") String city, Model model) {
        Map<String, Object> weatherData = weatherService.getWeather(city);
        model.addAttribute("weatherData", weatherData);
        model.addAttribute("city", city);
        return "weather";
    }
}