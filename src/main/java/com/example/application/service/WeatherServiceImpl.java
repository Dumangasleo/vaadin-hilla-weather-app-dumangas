package com.example.application.service;

import com.example.application.weather.dto.WeatherRequest;
import com.example.application.weather.dto.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Service
public class WeatherServiceImpl implements IWeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);

    @Value("${openweathermap.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public WeatherResponse getWeather(WeatherRequest request) {
        if (request == null || request.getLatitude() == 0.0 || request.getLongitude() == 0.0) {
            logger.error("Invalid WeatherRequest: {}", request);
            throw new IllegalArgumentException("Latitude and Longitude must not be null");
        }

        logger.info("Fetching weather for lat={}, lon={}", request.getLatitude(), request.getLongitude());

        String url = UriComponentsBuilder
            .fromUriString("https://api.openweathermap.org/data/2.5/weather")
            .queryParam("lat", request.getLatitude())
            .queryParam("lon", request.getLongitude())
            .queryParam("appid", apiKey)
            .queryParam("units", "metric")
            .build()
            .toUriString();

        try {
            var response = restTemplate.getForObject(url, OpenWeatherMapResponse.class);

            if (response == null) {
                logger.error("Received null response from Weather API.");
                throw new RuntimeException("Weather API returned null response.");
            }

            if (response.weather == null || response.weather.isEmpty()) {
                logger.warn("Weather list is empty in the API response: {}", response);
                throw new RuntimeException("No weather information found in API response.");
            }

            if (response.main == null || response.wind == null) {
                logger.warn("Main or Wind section missing in the response: {}", response);
                throw new RuntimeException("Incomplete weather data received.");
            }

            logger.info("Weather API response successfully received and parsed.");

            return new WeatherResponse(
                response.weather.get(0).description,
                response.main.temp,
                response.main.humidity,
                response.wind.gust,
                response.wind.speed,
                response.name
            );

        } catch (RestClientException ex) {
            logger.error("HTTP request to weather API failed: {}", ex.getMessage(), ex);
            throw new RuntimeException("Failed to call weather API", ex);
        } catch (Exception ex) {
            logger.error("Unexpected error occurred while fetching weather: {}", ex.getMessage(), ex);
            throw new RuntimeException("Unexpected error while processing weather data", ex);
        }
    }

    // Inner classes for deserialization
    private static class OpenWeatherMapResponse {
        public List<Weather> weather;
        public Main main;
        public Wind wind;
        public String name;

        static class Weather {
            public String description;
        }

        static class Main {
            public double temp;
            public int humidity;
        }

        static class Wind {
            public double speed;
            public double gust;
        }

        @Override
        public String toString() {
            return "OpenWeatherMapResponse{" +
                "weather=" + weather +
                ", main=" + main +
                ", wind=" + wind +
                ", name='" + name + '\'' +
                '}';
        }
    }
}
