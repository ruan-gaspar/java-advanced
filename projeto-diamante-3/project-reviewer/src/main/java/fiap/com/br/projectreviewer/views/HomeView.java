package fiap.com.br.projectreviewer.views;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import fiap.com.br.projectreviewer.model.Assessment;
import fiap.com.br.projectreviewer.service.AssessmentService;

@Route("")
public class HomeView extends VerticalLayout {

    private final AssessmentService assessmentService;
    private final Grid<Assessment> grid = new Grid<>(Assessment.class, false);

    public HomeView(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;

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
        Notification.show("Atividade Corrigida", 5000, Notification.Position.BOTTOM_STRETCH);
    }

    private void loadDataToGrid(){
        assert assessmentService != null;
        grid.setItems(assessmentService.getAllAssessments());
    }
}
