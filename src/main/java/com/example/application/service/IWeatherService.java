package com.example.application.service;
import com.example.application.weather.dto.WeatherRequest;
import com.example.application.weather.dto.WeatherResponse;

public interface IWeatherService {
       WeatherResponse getWeather(WeatherRequest request);
} 
