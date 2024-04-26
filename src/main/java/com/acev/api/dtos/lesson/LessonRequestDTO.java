package com.acev.api.dtos.lesson;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record LessonRequestDTO(Integer number,
                               @NotNull(message = "O campo 'idSeries' é obrigatório") UUID idSeries,
                               @NotBlank(message = "O campo 'title' é obrigatório") String title,
                               String origin,
                               String adaptation,
                               String revision,
                               String greeting,
                               List<String> musicSuggestions) {
}
