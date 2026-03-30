package br.com.fiap.ApiTripFinder.service;

import br.com.fiap.ApiTripFinder.dto.auth.AuthResponseDTO;
import br.com.fiap.ApiTripFinder.dto.auth.LoginRequestDTO;
import br.com.fiap.ApiTripFinder.dto.auth.RegisterRequestDTO;
import br.com.fiap.ApiTripFinder.dto.auth.UpdateUserRequestDTO;
import br.com.fiap.ApiTripFinder.dto.auth.UserResponseDTO;
import br.com.fiap.ApiTripFinder.entity.Role;
import br.com.fiap.ApiTripFinder.entity.User;
import br.com.fiap.ApiTripFinder.exception.BusinessException;
import br.com.fiap.ApiTripFinder.exception.ResourceNotFoundException;
import br.com.fiap.ApiTripFinder.repository.UserRepository;
import br.com.fiap.ApiTripFinder.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public AuthResponseDTO register(RegisterRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail().trim())) {
            throw new BusinessException("Já existe um usuário cadastrado com esse email");
        }

        User user = User.builder()
                .name(request.getName().trim())
                .email(request.getEmail().trim())
                .password(passwordEncoder.encode(request.getPassword()))
                .imageUrl(null)
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(savedUser.getEmail());

        return AuthResponseDTO.builder()
                .token(token)
                .type("Bearer")
                .userId(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole().name())
                .build();
    }

    public AuthResponseDTO login(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail().trim())
                .orElseThrow(() -> new BusinessException("Usuário ou senha inválidos"));

        String token = jwtService.generateToken(user.getEmail());

        return AuthResponseDTO.builder()
                .token(token)
                .type("Bearer")
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    public UserResponseDTO getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        return mapToResponse(user);
    }

    public UserResponseDTO updateCurrentUser(String currentEmail, UpdateUserRequestDTO request) {
        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        if (request.getCurrentPassword() == null || request.getCurrentPassword().isBlank()) {
            throw new BusinessException("Para continuar, insira sua senha atual.");
        }

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BusinessException("Senha inválida");
        }

        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName().trim());
        }

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            String newEmail = request.getEmail().trim();

            if (!user.getEmail().equalsIgnoreCase(newEmail) && userRepository.existsByEmail(newEmail)) {
                throw new BusinessException("Já existe um usuário cadastrado com esse e-mail.");
            }

            user.setEmail(newEmail);
        }

        if (request.getImageUrl() != null) {
            String newImageUrl = request.getImageUrl().trim();
            user.setImageUrl(newImageUrl.isBlank() ? null : newImageUrl);
        }

        if (request.getNewPassword() != null && !request.getNewPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword().trim()));
        }

        User updatedUser = userRepository.save(user);

        return mapToResponse(updatedUser);
    }

    public UserResponseDTO uploadProfilePhoto(String currentEmail, MultipartFile file) {
        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        if (file == null || file.isEmpty()) {
            throw new BusinessException("Arquivo não enviado.");
        }

        try {
            Path uploadPath = Paths.get(uploadDir, "profile").toAbsolutePath();
            Files.createDirectories(uploadPath);

            String extension = ".jpg";
            String original = file.getOriginalFilename();
            if (original != null && original.contains(".")) {
                extension = original.substring(original.lastIndexOf("."));
            }

            String fileName = UUID.randomUUID() + extension;
            Path filePath = uploadPath.resolve(fileName);

            file.transferTo(filePath.toFile());

            String imageUrl = baseUrl + "/uploads/profile/" + fileName;
            user.setImageUrl(imageUrl);

            userRepository.save(user);

            return mapToResponse(user);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar imagem");
        }
    }
    private UserResponseDTO mapToResponse(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .imageUrl(user.getImageUrl())
                .role(user.getRole().name())
                .build();
    }
}