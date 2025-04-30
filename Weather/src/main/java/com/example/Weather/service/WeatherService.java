package com.example.Weather.service;

import com.example.Weather.model.WeatherForecastModel;
import com.example.Weather.model.WeatherResponseModel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class WeatherService {

    @Autowired
    private WebClient webClient;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String apiKey = "cce33b5d410d0c3913a126b11c54c73d";

    public WeatherResponseModel getWeatherForCity(String city) throws Exception {
        String endpoint = "/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric";

        String response = webClient.get()
                .uri(endpoint)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JsonNode jsonNode = objectMapper.readTree(response);

        if (jsonNode.has("cod") && jsonNode.get("cod").asInt() != 200) {
            throw new RuntimeException("Error: " + jsonNode.get("message").asText());
        }

        return new WeatherResponseModel(
                jsonNode.get("name").asText(),
                jsonNode.get("weather").get(0).get("description").asText(),
                jsonNode.get("main").get("temp").asDouble(),
                jsonNode.get("main").get("temp_min").asDouble(),
                jsonNode.get("main").get("temp_max").asDouble(),
                jsonNode.get("main").get("humidity").asInt(),
                jsonNode.get("wind").get("speed").asDouble()
        );
    }



    public List<WeatherForecastModel> getNext5DayForecast(String city) {
        String endpoint = "/data/2.5/forecast?q=" + city + "&appid=" + apiKey + "&units=metric";

        try {
            String response = webClient.get()
                    .uri(endpoint)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            JsonNode root = objectMapper.readTree(response);
            if (!"200".equals(root.get("cod").asText())) {
                throw new RuntimeException("API error: " + root.get("message").asText());
            }

            List<WeatherForecastModel> result = new ArrayList<>();
            String lastDate = "";
            String cityName = root.get("city").get("name").asText();

            for (JsonNode forecast : root.get("list")) {
                long timestamp = forecast.get("dt").asLong();
                String date = getFormattedDateOnly(timestamp);

                if (!date.equals(lastDate)) {
                    lastDate = date;

                    WeatherForecastModel model = new WeatherForecastModel(
                            cityName,
                            forecast.get("weather").get(0).get("description").asText(),
                            forecast.get("main").get("temp").asDouble(),
                            forecast.get("main").get("temp_min").asDouble(),
                            forecast.get("main").get("temp_max").asDouble(),
                            forecast.get("main").get("humidity").asInt(),
                            forecast.get("wind").get("speed").asDouble(),
                            date
                    );
                    result.add(model);
                }
                if (result.size() == 5) {
                    break;
                }
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    private String getFormattedDateOnly(long unixTimestamp) {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date(unixTimestamp * 1000));
    }


}
