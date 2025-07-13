//import Map, { Marker } from 'react-map-gl';
import 'mapbox-gl/dist/mapbox-gl.css';
import '../styles/dashboard.css';
import React, { useRef, useEffect, useState } from 'react'
import mapboxgl from 'mapbox-gl'
import { WeatherEndpoint } from 'Frontend/generated/endpoints';
import WeatherRequest from 'Frontend/generated/com/example/application/weather/dto/WeatherRequest';
import WeatherResponse from 'Frontend/generated/com/example/application/weather/dto/WeatherResponse';

const MAPBOX_TOKEN = import.meta.env.VITE_MAPBOX_TOKEN

const WeatherDashboard : React.FC = () => {
  const mapRef = useRef<mapboxgl.Map | null>(null);
  const mapContainerRef = useRef<HTMLDivElement | null>(null);
  const markerRef = useRef<mapboxgl.Marker | null>(null);
  const [coordinates, setCoordinates] = useState<{ lat: number; lng: number } | null>(null);
  const [weather, setWeather] = useState<WeatherResponse | null>(null);

    useEffect(() => {
    if (!mapContainerRef.current) return;
    mapboxgl.accessToken = MAPBOX_TOKEN;
    mapRef.current = new mapboxgl.Map({
      container: mapContainerRef.current,
      style: 'mapbox://styles/mapbox/navigation-night-v1',
      center: [125.8072, 7.4478],
      zoom: 10.12
    });

    // Handle click event on map to fetch weather data
    mapRef.current.on('click', async (e) => {
      const { lng, lat } = e.lngLat;
      setCoordinates({ lng, lat });

      if (markerRef.current) {
        markerRef.current.remove();
      }
      
      if (mapRef.current) {
        markerRef.current = new mapboxgl.Marker({ color: 'red' })
        .setLngLat([lng, lat])
        .addTo(mapRef.current); 
    }

        // Fetch weather data based on clicked coordinates
        try {

            const request: WeatherRequest = {
                latitude: lat,
                longitude: lng
            };

            const response = await WeatherEndpoint.getWeather(request);
            setWeather(response || null);
            if (response) {
                console.log(response);
            }

        } catch (error) {
          console.error("Error fetching weather data:", error);
        }
    
    });

        return () => {
    if (mapRef.current) {
        mapRef.current.remove();
    }
};
  }, []);


  return (
  <div className="dashboard">
  <div className="sidebar">
    <img src="/images/weather-icon.svg" alt="Weather Icon" />
  </div>

  <div className="main">
    <div className="topbar">
      {coordinates ? (
           <span>
                Latitude: <span className="coordinates-content">{coordinates.lat.toFixed(4)}</span>, 
                Longitude: <span className="coordinates-content">{coordinates.lng.toFixed(4)}</span>
    </span>
      ) : (
        <span>Click on the map to get weather data</span>
      )}
    </div>
    <div ref={mapContainerRef} id="map" className="map-container" />
  </div>

  <div className="weather-panel">
    {weather ? (
      <>
        <div>
          <h2>{weather.name}</h2>
          <p>Updated: {new Date().toLocaleTimeString()}</p>
        </div>
        <div className="weather-row">
          <span className="label">Condition:</span>
          <span>{weather.description}</span>
        </div>
        <div className="weather-row">
          <span className="label">Temperature:</span>
          <span>{weather.temperature} °C</span>
        </div>
        <div className="weather-row">
          <span className="label">Humidity:</span>
          <span>{weather.humidity}%</span>
        </div>
        <div className="weather-row">
          <span className="label">Wind:</span>
          <span>Speed: {weather.speed} m/s, Gust: {weather.gust} m/s</span>
        </div>
      </>
    ) : (
      <p>No weather data. Click on the map.</p>
    )}
  </div>
</div>

  );
};

export default WeatherDashboard;
