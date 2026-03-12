    package br.com.fiap.api_weather.controllers;

    import br.com.fiap.api_weather.services.PlannerApiService;
    import br.com.fiap.api_weather.services.WeatherApiService;
    import org.springframework.web.bind.annotation.*;
    import br.com.fiap.api_weather.models.WeatherResponse;

    @RestController
    @RequestMapping("/api/planner")
    @CrossOrigin(origins = "http://localhost:4200")
    public class WeatherApiController {
        private final PlannerApiService plannerApiService;
        private final WeatherApiService weatherApiService;

        public WeatherApiController(
                PlannerApiService plannerApiService,
                WeatherApiService weatherApiService
        ) {
            this.plannerApiService = plannerApiService;
            this.weatherApiService = weatherApiService;
        }
        public record PlannerResponse(String result) {}

        @GetMapping
        public PlannerResponse getPlannerResponse(@RequestParam String activity) {
            var weather = weatherApiService.getWeather();
            var result = plannerApiService.plan(weather, activity);

            return new PlannerResponse(result);
        }
    }
