package com.example.application.endpoints;

import com.example.application.service.IWeatherService;
import com.example.application.weather.dto.WeatherRequest;
import com.example.application.weather.dto.WeatherResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class WeatherEndpointTest {

    @MockitoBean
    private IWeatherService weatherService;

    @Test
    public void testWeatherEndpointReturnsData() {
        WeatherEndpoint endpoint = new WeatherEndpoint(weatherService);

        WeatherRequest request = new WeatherRequest();
        request.setLatitude(12.34);
        request.setLongitude(56.78);

        WeatherResponse mockedResponse = new WeatherResponse("clear sky", 30.0, 65, 2.3, 3.1, "Manila");
        when(weatherService.getWeather(request)).thenReturn(mockedResponse);

        WeatherResponse result = endpoint.getWeather(request);

        assertEquals("clear sky", result.getDescription());
        assertEquals(30.0, result.getTemperature());
    }
}
