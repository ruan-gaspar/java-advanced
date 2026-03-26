package br.com.fiap.ApiTripFinder.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequestDTO {
    private String name;

    @Email(message = "E-mail inválido")
    private String email;

    @NotBlank(message = "Para continuar, insira sua senha")
    private String currentPassword;

    private String newPassword;
}
