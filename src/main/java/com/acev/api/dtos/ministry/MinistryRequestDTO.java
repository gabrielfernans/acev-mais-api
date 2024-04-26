package com.acev.api.dtos.ministry;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record MinistryRequestDTO(
        @NotBlank(message = "O campo 'name' é obrigatório") String name,
        String description,
        LocalDateTime foundation) {
}
