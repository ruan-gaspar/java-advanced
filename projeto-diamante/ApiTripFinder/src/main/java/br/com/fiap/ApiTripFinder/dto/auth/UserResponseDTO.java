package br.com.fiap.ApiTripFinder.dto.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String imageUrl;
    private String role;
}