package com.acev.api.dtos.authentication;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record AuthRequestDTO(@NotBlank(message = "O campo de email é obrigatório") String email,
                             @NotBlank(message = "O campo de senha é obrigatório")
                             @Length(min = 8, message = "A senha deve conter no mínimo 8 caracteres") String password) {
}
