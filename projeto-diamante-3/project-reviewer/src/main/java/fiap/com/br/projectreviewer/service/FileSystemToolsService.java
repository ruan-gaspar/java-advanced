package fiap.com.br.projectreviewer.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileSystemToolsService {

    public List<String> listAllFiles(String baseDirPath) {
        File baseDir = new File(baseDirPath);
        if (!baseDir.exists() || !baseDir.isDirectory()) {
            throw new IllegalArgumentException("Diretório inválido: " + baseDirPath);
        }

        List<String> result = new ArrayList<>();
        collectFilesRecursively(baseDir, baseDir, result);
        return result;
    }

    public String readFile(String baseDirPath, String relativePath) {
        File baseDir = new File(baseDirPath);
        if (!baseDir.exists() || !baseDir.isDirectory()) {
            throw new IllegalArgumentException("Diretório inválido: " + baseDirPath);
        }

        File targetFile = new File(baseDir, relativePath);
        if (!targetFile.exists() || !targetFile.isFile()) {
            throw new IllegalArgumentException("Arquivo não encontrado: " + targetFile.getAbsolutePath());
        }

        try {
            Path path = targetFile.toPath();
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler arquivo: " + targetFile.getAbsolutePath(), e);
        }
    }

    private void collectFilesRecursively(File baseDir, File current, List<String> result) {
        File[] files = current.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                collectFilesRecursively(baseDir, file, result);
            } else {
                String relative = baseDir.toPath().relativize(file.toPath()).toString();
                result.add(relative);
            }
        }
    }
}