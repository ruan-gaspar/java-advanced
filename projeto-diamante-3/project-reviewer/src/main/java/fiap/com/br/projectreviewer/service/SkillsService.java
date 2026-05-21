package fiap.com.br.projectreviewer.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class SkillsService {

    private static final String SKILL_PATH = "skills/project-review-skill.md";

    public String loadProjectReviewSkill() {
        ClassPathResource resource = new ClassPathResource(SKILL_PATH);
        try {
            byte[] bytes = resource.getInputStream().readAllBytes();
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível carregar a skill de revisão de projeto em " + SKILL_PATH, e);
        }
    }
}