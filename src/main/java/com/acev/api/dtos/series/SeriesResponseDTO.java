package com.acev.api.dtos.series;

import com.acev.api.dtos.lesson.LessonResponseDTO;
import com.acev.api.dtos.person.PersonResponseMinimalDTO;
import com.acev.api.models.SeriesModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record SeriesResponseDTO(@NotNull UUID id,
                                @NotBlank String title,
                                String period,
                                String author,
                                URL photoUrl,
                                List<LessonResponseDTO> lessons,
                                LocalDateTime createdAt,
                                LocalDateTime updatedAt) {

  public static SeriesResponseDTO convertSeriesDTO(SeriesModel series) {
    List<LessonResponseDTO> lessons = (series.getLessons() != null) ?
          series.getLessons().stream().map(LessonResponseDTO::convertLessonDTO)
                .collect(Collectors.toList()) : null;

    return new SeriesResponseDTO(
          series.getIdSeries(),
          series.getTitle(),
          series.getPeriod(),
          series.getAuthor(),
          series.getPhotoUrl(),
          lessons,
          series.getCreatedAt(),
          series.getUpdatedAt()
    );
  }
}
