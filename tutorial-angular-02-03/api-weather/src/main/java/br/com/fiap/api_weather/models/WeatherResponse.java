package br.com.fiap.api_weather.models;

public class WeatherResponse {

    public record weatherResponse(
            CurrentWeather current_weather) {
        public record CurrentWeather(
                double temperature,
                double windspeed
        ) {}
    }
}