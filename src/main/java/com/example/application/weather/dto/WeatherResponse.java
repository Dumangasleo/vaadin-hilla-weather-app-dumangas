package com.example.application.weather.dto;

public class WeatherResponse {
    private String description;
    private double temperature;
    private int humidity;
    private double gust;
    private double speed;
    private String name;

    public WeatherResponse(String description, double temperature, int humidity, double gust, double speed, String name) {
        this.description = description;
        this.temperature = temperature;
        this.humidity = humidity;
        this.gust = gust;
        this.speed = speed;
        this.name = name;
    }

    // Getters
    public String getDescription() { return description; }
    public double getTemperature() { return temperature; }
    public int getHumidity() {return humidity;}
    public double getGust() {return gust;}
    public double getSpeed() {return speed;}
    public String getName() {return name;}
}
