package com.example.Weather.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherForecastModel {

    private String cityName;
    private String weatherDescription;
    private double temperature;
    private double minTemperature;
    private double maxTemperature;
    private int humidity;
    private double windSpeed;
    private String date;

}
