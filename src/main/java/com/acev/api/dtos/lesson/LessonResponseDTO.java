package com.acev.api.dtos.lesson;

import com.acev.api.models.LessonModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record LessonResponseDTO(@NotNull UUID id,
                                Integer number,
                                @NotBlank String title,
                                String origin,
                                String adaptation,
                                String revision,
                                String greeting,
                                List<String> musicSuggestions,
                                URL pdfUrl,
                                UUID idSeries,
                                String seriesName,
                                LocalDateTime createdAt,
                                LocalDateTime updatedAt) {

  public static LessonResponseDTO convertLessonDTO(LessonModel lesson) {
    return new LessonResponseDTO(
          lesson.getIdLesson(),
          lesson.getNumber(),
          lesson.getTitle(),
          lesson.getOrigin(),
          lesson.getAdaptation(),
          lesson.getRevision(),
          lesson.getGreeting(),
          lesson.getMusicSuggestions(),
          lesson.getPdfUrl(),
          lesson.getSeries().getIdSeries(),
          lesson.getSeries().getTitle(),
          lesson.getCreatedAt(),
          lesson.getUpdatedAt()
    );
  }
}
