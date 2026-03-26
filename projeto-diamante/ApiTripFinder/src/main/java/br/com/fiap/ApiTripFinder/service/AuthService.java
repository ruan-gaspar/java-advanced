package br.com.fiap.ApiTripFinder.service;

import  br.com.fiap.ApiTripFinder.dto.auth.AuthResponseDTO;
import br.com.fiap.ApiTripFinder.dto.auth.LoginRequestDTO;
import br.com.fiap.ApiTripFinder.dto.auth.RegisterRequestDTO;
import br.com.fiap.ApiTripFinder.dto.auth.UpdateUserRequestDTO;
import br.com.fiap.ApiTripFinder.entity.Role;
import br.com.fiap.ApiTripFinder.entity.User;
import br.com.fiap.ApiTripFinder.exception.BusinessException;
import br.com.fiap.ApiTripFinder.exception.ResourceNotFoundException;
import br.com.fiap.ApiTripFinder.repository.UserRepository;
import br.com.fiap.ApiTripFinder.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDTO register(RegisterRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Já existe um usuário cadastrado com esse email");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
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

        User user = userRepository.findByEmail(request.getEmail())
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
    public AuthResponseDTO getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));
        return AuthResponseDTO.builder()
                .token(null)
                .type("Bearer")
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
 public AuthResponseDTO updateCurrentUser(String currentEmail, UpdateUserRequestDTO request) {
        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(()->new ResourceNotFoundException("Usuário não encontrado"));
        if(!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BusinessException("Senha inválida");
        }
     if (request.getName() != null && !request.getName().isBlank()) {
         user.setName(request.getName());
     }
        if(request.getEmail() != null && !request.getEmail().isBlank()
        && !user.getEmail().equals(request.getEmail())
        && userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Já existe um usuário cadastrado com esse e-mail.");
            }
        if (request.getNewPassword() != null && !request.getNewPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }
        User updatedUser = userRepository.save(user);

        return AuthResponseDTO.builder()
                .token(null)
                .type("Bearer")
                .userId(updatedUser.getId())
                .name(updatedUser.getName())
                .email(updatedUser.getEmail())
                .role(updatedUser.getRole().name())
                .build();
 }
}
