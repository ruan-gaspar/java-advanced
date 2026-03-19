package fiap.com.br.todo.controller;

import fiap.com.br.todo.security.LoginRequest;
import fiap.com.br.todo.service.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SecurityController {
    private final AuthenticationManager authenticationManager;

    public record TokenResponse(String token) {}

    private final TokenService tokenService;

    public SecurityController(TokenService tokenService, AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }
    @PostMapping("/login")
    public TokenResponse token(@RequestBody LoginRequest request) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        return new TokenResponse(tokenService.generateToken(authentication.getName()));
    }
}
