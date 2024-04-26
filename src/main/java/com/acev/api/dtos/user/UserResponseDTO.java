package com.acev.api.dtos.user;

import com.acev.api.enums.RolesEnum;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record UserResponseDTO(@NotNull UUID id,
                              @NotNull String email,
                              @NotNull List<RolesEnum> roles,
                              LocalDateTime createdAt,
                              LocalDateTime updatedAt,
                              UUID idPerson) {
}
