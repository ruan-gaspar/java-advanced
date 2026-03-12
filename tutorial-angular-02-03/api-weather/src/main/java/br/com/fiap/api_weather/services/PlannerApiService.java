package br.com.fiap.api_weather.services;

import br.com.fiap.api_weather.models.WeatherResponse;
import org.springframework.stereotype.Service;

@Service
public class PlannerApiService {
    public String plan(WeatherResponse.weatherResponse weather, String activity) {
        var temp = weather.current_weather().temperature();
        return switch (activity) {
          case "Ver as estrelas" -> "Boa noite para observar estrelas!";
          default -> "Atividade não reconhecida.";
        };
    }
}
