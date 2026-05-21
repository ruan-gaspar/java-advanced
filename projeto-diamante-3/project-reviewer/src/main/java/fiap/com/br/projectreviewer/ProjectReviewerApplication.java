package fiap.com.br.projectreviewer;

import com.vaadin.flow.component.page.ColorScheme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;

@SpringBootApplication
@StyleSheet(Lumo.STYLESHEET)
@ColorScheme(ColorScheme.Value.DARK)

public class ProjectReviewerApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(ProjectReviewerApplication.class, args);
    }
}
