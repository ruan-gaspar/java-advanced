package fiap.com.br.projectreviewer.seeder;

import fiap.com.br.projectreviewer.model.Assessment;
import fiap.com.br.projectreviewer.service.AssessmentService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder {

    private final AssessmentService assessmentService;

    @PostConstruct
    public void init() {
        assessmentService.addAssessments(List.of(
                Assessment.builder()
                        .studentName("João Silva")
                        .grade(new java.math.BigDecimal("8.5"))
                        .feedback("Faltou aplicar o DTO")
                        .build(),
                Assessment.builder()
                        .studentName("Julio Silva")
                        .grade(new java.math.BigDecimal("6.6"))
                        .feedback("HTTP Status incorretos. Sem vídeo de apresentação")
                        .build()
        ));
    }

}
