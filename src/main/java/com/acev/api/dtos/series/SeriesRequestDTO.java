package com.acev.api.dtos.series;

import jakarta.validation.constraints.NotBlank;

public record SeriesRequestDTO(@NotBlank(message = "O campo 'title' é obrigatório") String title,
                               String period,
                               String author) {
}
