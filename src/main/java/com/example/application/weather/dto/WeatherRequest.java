package com.example.application.weather.dto;

public class WeatherRequest {
    private double latitude;
    private double longitude;

    // Getters & setters
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
}
