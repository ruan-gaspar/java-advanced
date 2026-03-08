package fiap.com.example.gameHub.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RecomendacaoController {

    private final RecomendacaoService service;

    public RecomendacaoController(RecomendacaoService service) {
        this.service = service;
    }

    @PostMapping("/recomendacao")
    public RecomendacaoResponse recomendar(@RequestBody JogoRequest request) {
        return service.analisar(request.nome());
    }
}