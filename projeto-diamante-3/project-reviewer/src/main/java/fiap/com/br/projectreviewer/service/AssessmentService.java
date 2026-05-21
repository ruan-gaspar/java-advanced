package fiap.com.br.projectreviewer.service;

import fiap.com.br.projectreviewer.model.Assessment;
import fiap.com.br.projectreviewer.repository.AssessmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssessmentService {

    private final AssessmentRepository assessmentRepository;

    public List<Assessment> getAllAssessments() {
        return assessmentRepository.findAll(
                Pageable.unpaged(
                        Sort.by("studentName").ascending())
                ).getContent();
    }

    public Assessment addAssessment(Assessment assessment) {
        return assessmentRepository.save(assessment);
    }

    public void addAssessments(List<Assessment> assessments) {
        assessmentRepository.saveAll(assessments);
    }

}
