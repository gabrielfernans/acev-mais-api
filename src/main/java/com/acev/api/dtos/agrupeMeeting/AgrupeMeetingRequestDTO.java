package com.acev.api.dtos.agrupeMeeting;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record AgrupeMeetingRequestDTO(@NotNull(message = "O campo 'idAgrupe' é obrigatório") UUID idAgrupe,
                                      @NotNull(message = "O campo 'date' é obrigatório") LocalDateTime date,
                                      @NotNull(message = "O campo 'customLesson' é obrigatório") Boolean customLesson,
                                      UUID idLesson,
                                      String customLessonTitle,
                                      String note,
                                      List<UUID> idParticipants,
                                      List<String> guests) {
}
