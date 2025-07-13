package com.example.application.endpoints;

import com.example.application.weather.dto.WeatherRequest;
import com.example.application.weather.dto.WeatherResponse;
import com.example.application.service.IWeatherService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@BrowserCallable
@AnonymousAllowed
public class WeatherEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(WeatherEndpoint.class);

    private final IWeatherService weatherService;

    public WeatherEndpoint(IWeatherService weatherService) {
        this.weatherService = weatherService;
    }

    public WeatherResponse getWeather(WeatherRequest request) {
        try {
            // Validate the request object
            if (request == null) {
                logger.warn("WeatherRequest is null.");
                throw new IllegalArgumentException("Request body cannot be null.");
            }

            if (request == null || request.getLatitude() == 0.0 || request.getLongitude() == 0.0) {
                logger.warn("Latitude or Longitude is null: lat={}, lon={}", 
                            request.getLatitude(), request.getLongitude());
                throw new IllegalArgumentException("Latitude and Longitude must be provided.");
            }

            logger.info("Received weather request: lat={}, lon={}", 
                        request.getLatitude(), request.getLongitude());

            WeatherResponse response = weatherService.getWeather(request);

            logger.info("Returning weather response: {}", response);
            return response;

        } catch (IllegalArgumentException ex) {
            logger.error("Invalid input: {}", ex.getMessage());
            throw ex; 

        } catch (RuntimeException ex) {
            logger.error("Weather service failed: {}", ex.getMessage(), ex);
            throw new RuntimeException("An error occurred while fetching weather data. Please try again later.");
        } catch (Exception ex) {
            logger.error("Unexpected error: {}", ex.getMessage(), ex);
            throw new RuntimeException("Unexpected server error occurred. Please contact support.");
        }
    }
}
