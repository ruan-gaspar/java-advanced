package fiap.com.br.projectreviewer.service;

import fiap.com.br.projectreviewer.model.Assessment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssessmentTools {

    private final AssessmentService assessmentService;

    /**
     * Ferramenta para criar uma nova avaliação de aluno.
     */
    public Assessment createAssessment(String studentName, BigDecimal grade, String feedback) {
        Assessment assessment = Assessment.builder()
                .studentName(studentName)
                .grade(grade)
                .feedback(feedback)
                .build();

        return assessmentService.addAssessment(assessment);
    }

    /**
     * Ferramenta para atualizar nota e feedback de uma avaliação existente.
     */
    public Assessment updateAssessment(Long id, BigDecimal grade, String feedback) {
        Optional<Assessment> existing = assessmentService
                .getAllAssessments()
                .stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();

        if (existing.isEmpty()) {
            throw new IllegalArgumentException("Avaliação com id " + id + " não encontrada");
        }

        Assessment assessment = existing.get();
        assessment.setGrade(grade);
        assessment.setFeedback(feedback);

        return assessmentService.addAssessment(assessment);
    }
}