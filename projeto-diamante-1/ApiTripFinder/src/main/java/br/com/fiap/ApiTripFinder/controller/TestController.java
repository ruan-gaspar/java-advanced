package br.com.fiap.ApiTripFinder.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @Operation(summary = "Endpoint protegido para teste", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    public String test() {
        return "Autenticado com sucesso!";
    }
}
