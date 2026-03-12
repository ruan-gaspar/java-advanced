package br.com.fiap.api_weather.services;

import br.com.fiap.api_weather.models.WeatherResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherApiService {

    private final RestTemplate restTemplate = new RestTemplate();

    public WeatherResponse.weatherResponse getWeather() {

        var url = "https://api.open-meteo.com/v1/forecast?latitude=-23.5505&longitude=-46.6333&current_weather=true";

        return restTemplate.getForObject(url, WeatherResponse.weatherResponse.class);
    }
}