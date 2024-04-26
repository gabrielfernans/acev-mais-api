package com.acev.api.dtos.agrupeMeeting;

import com.acev.api.dtos.agrupe.AgrupeResponseDTO;
import com.acev.api.dtos.lesson.LessonResponseDTO;
import com.acev.api.dtos.person.PersonResponseMinimalDTO;
import com.acev.api.models.AgrupeMeetingModel;
import com.acev.api.models.LessonModel;
import jakarta.validation.constraints.NotNull;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record AgrupeMeetingResponseDTO(@NotNull UUID id,
                                       @NotNull AgrupeResponseDTO agrupe,
                                       @NotNull LocalDateTime date,
                                       @NotNull Boolean customLesson,
                                       String customLessonTitle,
                                       LessonResponseDTO lesson,
                                       List<PersonResponseMinimalDTO> participants,
                                       List<String> guests,
                                       URL photoUrl,
                                       String note,
                                       LocalDateTime createdAt,
                                       LocalDateTime updatedAt) {

  public static AgrupeMeetingResponseDTO convertAgrupeMeetingDTO(AgrupeMeetingModel agrupeMeeting) {
    List<PersonResponseMinimalDTO> participants = (agrupeMeeting.getParticipants() != null) ?
          agrupeMeeting.getParticipants().stream().map(PersonResponseMinimalDTO::convertPersonMinimalDTO)
                .collect(Collectors.toList()) : null;

    LessonResponseDTO lesson = (agrupeMeeting.getLesson() != null) ?
          LessonResponseDTO.convertLessonDTO(agrupeMeeting.getLesson()) : null;

    return new AgrupeMeetingResponseDTO(
          agrupeMeeting.getIdAgrupeMeeting(),
          AgrupeResponseDTO.convertAgrupeDTO(agrupeMeeting.getAgrupe()),
          agrupeMeeting.getDate(),
          agrupeMeeting.getCustomLesson(),
          agrupeMeeting.getCustomLessonTitle(),
          lesson,
          participants,
          agrupeMeeting.getGuests(),
          agrupeMeeting.getPhotoUrl(),
          agrupeMeeting.getNote(),
          agrupeMeeting.getCreatedAt(),
          agrupeMeeting.getUpdatedAt()
    );
  }
}
