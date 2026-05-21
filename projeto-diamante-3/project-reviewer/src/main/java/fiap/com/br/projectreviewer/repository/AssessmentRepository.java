package fiap.com.br.projectreviewer.repository;

import fiap.com.br.projectreviewer.model.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
}
