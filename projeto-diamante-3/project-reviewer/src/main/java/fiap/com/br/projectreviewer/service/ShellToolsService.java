package fiap.com.br.projectreviewer.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class ShellToolsService {

    // Altere aqui se quiser um diretório diferente para os clones
    private static final String BASE_DIR = "repos";

    public String cloneRepository(String repoUrl) {
        String repoName = extractRepoName(repoUrl);

        File baseDir = new File(BASE_DIR);
        if (!baseDir.exists() && !baseDir.mkdirs()) {
            throw new IllegalStateException("Não foi possível criar diretório base: " + baseDir.getAbsolutePath());
        }

        File targetDir = new File(baseDir, repoName);

        // Monta o comando git clone
        ProcessBuilder builder = new ProcessBuilder("git", "clone", repoUrl, targetDir.getAbsolutePath());
        builder.redirectErrorStream(true); // junta stdout e stderr

        try {
            Process process = builder.start();

            // Lê a saída do comando (para debug/log se quiser)
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("[git] " + line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IllegalStateException("Falha ao clonar repositório (código " + exitCode + ")");
            }

            return targetDir.getAbsolutePath();
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Erro ao executar git clone", e);
        }
    }

    private String extractRepoName(String repoUrl) {
        if (repoUrl == null || repoUrl.isBlank()) {
            throw new IllegalArgumentException("URL do repositório não pode ser vazia");
        }
        String trimmed = repoUrl.trim();
        int lastSlash = trimmed.lastIndexOf('/');
        String name = (lastSlash >= 0) ? trimmed.substring(lastSlash + 1) : trimmed;
        if (name.endsWith(".git")) {
            name = name.substring(0, name.length() - 4);
        }
        return name;
    }
}