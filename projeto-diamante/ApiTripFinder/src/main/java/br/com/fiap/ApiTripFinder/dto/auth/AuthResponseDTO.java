package br.com.fiap.ApiTripFinder.dto.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDTO {

    private String token;
    private String type;
    private Long userId;
    private String name;
    private String email;
    private String imageUrl;
    private String role;
}