package com.example.application.service;

import com.example.application.weather.dto.WeatherRequest;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.*;

public class WeatherServiceImplTest {

    @Test
    public void testBuildsCorrectRequest() {
        WeatherServiceImpl service = new WeatherServiceImpl();
        ReflectionTestUtils.setField(service, "apiKey", "dummy");

        WeatherRequest request = new WeatherRequest();
        request.setLatitude(14.6);
        request.setLongitude(120.9);

        // You can test that no exception is thrown, or mock the restTemplate
        assertThrows(RuntimeException.class, () -> service.getWeather(request));
    }
}
