package fiap.com.example.gameHub.services;

import org.springframework.stereotype.Service;

@Service
public class RecomendacaoService {

    private final RawgClient rawgClient;

    public RecomendacaoService(RawgClient rawgClient) {
        this.rawgClient = rawgClient;
    }

    public RecomendacaoResponse analisar(String nome) {

        // Aqui você parseia o JSON da RAWG
        double nota = 4.5;
        int metacritic = 85;
        int reviews = 1500;

        String recomendacao;

        if (nota >= 4.0 && metacritic >= 75 && reviews > 1000) {
            recomendacao = "🔥 Vale muito a pena jogar!";
        } else if (nota >= 3.5) {
            recomendacao = "👍 Pode ser uma boa experiência.";
        } else {
            recomendacao = "⚠️ Talvez não seja um bom investimento.";
        }

        return new RecomendacaoResponse(nome, nota, metacritic, recomendacao);
    }
}