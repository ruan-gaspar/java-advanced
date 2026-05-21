package fiap.com.br.projectreviewer.views;

import fiap.com.br.projectreviewer.service.ReviewService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import fiap.com.br.projectreviewer.model.Assessment;
import fiap.com.br.projectreviewer.service.AssessmentService;
import fiap.com.br.projectreviewer.service.AssessmentTools;
import fiap.com.br.projectreviewer.service.FileSystemToolsService;
import fiap.com.br.projectreviewer.service.ShellToolsService;
import fiap.com.br.projectreviewer.service.SkillsService;

import java.math.BigDecimal;

@Route("")
public class HomeView extends VerticalLayout {

    private final AssessmentService assessmentService;

    private final ReviewService reviewService;
    private final AssessmentTools assessmentTools;
    private final ShellToolsService shellToolsService;
    private final FileSystemToolsService fileSystemToolsService;
    private final SkillsService skillsService;

    private final Grid<Assessment> grid = new Grid<>(Assessment.class, false);

    private BigDecimal extractGrade(String aiResponse) {
        // procura linha começando com "Nota:"
        String[] lines = aiResponse.split("\\r?\\n");
        for (String line : lines) {
            line = line.trim();
            if (line.toLowerCase().startsWith("nota:")) {
                String value = line.substring(5).trim().replace(",", ".");
                try {
                    return new BigDecimal(value);
                } catch (NumberFormatException ignored) { }
            }
        }
        // fallback
        return new BigDecimal("7.0");
    }

    private String extractFeedback(String aiResponse) {
        String[] lines = aiResponse.split("\\r?\\n");
        StringBuilder sb = new StringBuilder();
        boolean feedbackStarted = false;
        for (String line : lines) {
            if (!feedbackStarted) {
                if (line.toLowerCase().startsWith("feedback:")) {
                    feedbackStarted = true;
                    sb.append(line.substring("Feedback:".length()).trim());
                }
            } else {
                sb.append(' ').append(line.trim());
            }
        }
        if (!feedbackStarted) {
            // se o modelo não seguir o formato, usa a resposta inteira como feedback
            return aiResponse;
        }
        return sb.toString().trim();
    }

    public HomeView(AssessmentService assessmentService,
                    AssessmentTools assessmentTools,
                    ShellToolsService shellToolsService,
                    FileSystemToolsService fileSystemToolsService,
                    SkillsService skillsService,
                    ReviewService reviewService) {


        this.assessmentService = assessmentService;
        this.assessmentTools = assessmentTools;
        this.shellToolsService = shellToolsService;
        this.fileSystemToolsService = fileSystemToolsService;
        this.skillsService = skillsService;
        this.reviewService = reviewService;

        var input = new MessageInput();
        input.addSubmitListener(e -> runReview(e.getValue()));
        input.setWidthFull();
        add(new Paragraph("Insira o link do repositório para corrigir a atividade:"));
        add(input);

        grid.addColumn(Assessment::getId).setHeader("Id").setFlexGrow(0);
        grid.addColumn(Assessment::getStudentName).setHeader("Nome");
        grid.addColumn(Assessment::getGrade).setHeader("Nota").setFlexGrow(0);
        grid.addColumn(Assessment::getFeedback).setHeader("Feedback").setFlexGrow(1);
        grid.setSizeFull();

        setSizeFull();
        setWidthFull();

        loadDataToGrid();

        add(new H2("Notas"));
        add(grid);
    }

    public void runReview(String gitRepository) {
        try {
            // 1. Clona o repositório (ShellTools)
            String repoPath = shellToolsService.cloneRepository(gitRepository);

            // 2. Pede para o modelo gerar a avaliação (nota + feedback em texto)
            String aiResponse = reviewService.generateFeedback(gitRepository, repoPath);

            // 3. Extrai nota e feedback do texto retornado
            BigDecimal grade = extractGrade(aiResponse);
            String feedback = extractFeedback(aiResponse);
            feedback = truncateFeedback(feedback, 250);

            // 4. Salva a avaliação usando as Tools de nota
            Assessment assessment = assessmentTools.createAssessment(
                    gitRepository,
                    grade,
                    feedback
            );

            // 5. Atualiza grid
            loadDataToGrid();

            Notification.show(
                    "Atividade corrigida (ID " + assessment.getId() + ", nota " + grade + ")",
                    5000,
                    Notification.Position.BOTTOM_STRETCH
            );
        } catch (Exception e) {
            e.printStackTrace();
            Notification.show(
                    "Erro ao corrigir atividade: " + e.getMessage(),
                    7000,
                    Notification.Position.BOTTOM_STRETCH
            );
        }
    }

    private String resumoSkill(String skillText) {
        if (skillText == null) {
            return "skill não carregada";
        }
        String trimmed = skillText.trim().replaceAll("\\s+", " ");
        return trimmed.length() > 120 ? trimmed.substring(0, 120) + "..." : trimmed;
    }

    private void loadDataToGrid(){
        assert assessmentService != null;
        grid.setItems(assessmentService.getAllAssessments());
    }
    private String truncateFeedback(String text, int maxLength) {
        if (text == null) {
            return null;
        }
        String trimmed = text.trim();
        if (trimmed.length() <= maxLength) {
            return trimmed;
        }
        return trimmed.substring(0, maxLength - 3) + "...";
    }
}