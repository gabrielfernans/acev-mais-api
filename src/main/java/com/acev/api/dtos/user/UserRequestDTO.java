package com.acev.api.dtos.user;

import com.acev.api.enums.RolesEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.UUID;

public record UserRequestDTO(@NotBlank(message = "O campo 'email' é obrigatório") String email,
                             @NotBlank(message = "O campo 'password' é obrigatório") @Length(min = 8, message = "A senha deve conter no mínimo 8 caracteres") String password,
                             @NotNull(message = "O campo 'roles' é obrigatório") List<RolesEnum> roles,
                             @NotNull(message = "O campo 'idPerson' é obrigatório") UUID idPerson) {
}
