package br.com.fiap.ApiTripFinder.controller;

import br.com.fiap.ApiTripFinder.dto.auth.AuthResponseDTO;
import br.com.fiap.ApiTripFinder.dto.auth.LoginRequestDTO;
import br.com.fiap.ApiTripFinder.dto.auth.RegisterRequestDTO;
import br.com.fiap.ApiTripFinder.dto.auth.UpdateUserRequestDTO;
import br.com.fiap.ApiTripFinder.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final AuthService authService;
    @Operation(summary = "Registrar usuário")
    @SecurityRequirements
    @PostMapping("/register")
    public AuthResponseDTO register(@Valid @RequestBody RegisterRequestDTO request) {
        return authService.register(request);
    }
    @Operation(summary = "Autenticar usuário")
    @SecurityRequirements
    @PostMapping("/login")
    public AuthResponseDTO login(@Valid @RequestBody LoginRequestDTO request) {
        return authService.login(request);
    }
    @Operation(summary = "Dados do usuário autenticado")
    @GetMapping("/me")
    public AuthResponseDTO me(Authentication authentication) {
        return authService.getCurrentUser(authentication.getName());
    }
    @Operation(summary = "Alterar nome, e-mail ou senha")
    @PutMapping("/me")
    public AuthResponseDTO updateMe(
            Authentication authentication,
            @Valid @RequestBody UpdateUserRequestDTO request) {
        return authService.updateCurrentUser(authentication.getName(), request);
    }
}
