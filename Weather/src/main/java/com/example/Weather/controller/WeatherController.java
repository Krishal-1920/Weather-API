package com.example.Weather.controller;

import com.example.Weather.model.WeatherForecastModel;
import com.example.Weather.model.WeatherResponseModel;
import com.example.Weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;


    @GetMapping("/weatherToday")
    public WeatherResponseModel getWeather(@RequestHeader("Authorization") String token,
                                           @RequestParam("city") String city) throws Exception {

        if (token == null) {
            throw new RuntimeException("Invalid or missing token");
        }
        return weatherService.getWeatherForCity(city);
    }



    @GetMapping("/forecast")
    public List<WeatherForecastModel> get5DayForecast(@RequestHeader("Authorization") String token,
                                                      @RequestParam("city") String city) {
        if (token == null) {
            throw new RuntimeException("Invalid or missing token");
        }
        return weatherService.getNext5DayForecast(city);
    }

}
