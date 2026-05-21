package fiap.com.br.projectreviewer.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ChatClient chatClient;
    private final SkillsService skillsService;
    private final FileSystemToolsService fileSystemToolsService;

    public ReviewService(ChatClient chatClient,
                         SkillsService skillsService,
                         FileSystemToolsService fileSystemToolsService) {
        this.chatClient = chatClient;
        this.skillsService = skillsService;
        this.fileSystemToolsService = fileSystemToolsService;
    }

    public String generateFeedback(String repoUrl, String repoPath) {
        String skill = skillsService.loadProjectReviewSkill();

        List<String> files = fileSystemToolsService.listAllFiles(repoPath);
        String filesPreview = files.stream()
                .limit(20)
                .reduce((a, b) -> a + "\n- " + b)
                .orElse("Nenhum arquivo encontrado");

        String prompt = """
                Você é um revisor de projetos Java.

                Diretrizes de correção (skill):
                %s

                Projeto do aluno:
                - URL do repositório: %s
                - Arquivos encontrados (lista parcial):
                %s

                Com base nas diretrizes e na estrutura do projeto, avalie a qualidade da solução.
                Responda em português, no seguinte formato:

                Nota: <número de 0 a 10>
                Feedback: <texto objetivo explicando pontos positivos e o que precisa melhorar>
                """.formatted(skill, repoUrl, filesPreview);

        // Chamada simples ao modelo Anthropic via ChatClient
        return chatClient
                .prompt()
                .user(prompt)
                .call()
                .content();
    }
}