package com.acev.api.dtos.ministry;

import com.acev.api.dtos.person.PersonResponseMinimalDTO;
import com.acev.api.models.MinistryModel;
import jakarta.validation.constraints.NotNull;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record MinistryResponseDTO(@NotNull UUID id,
                                  @NotNull String name,
                                  String description,
                                  LocalDateTime foundation,
                                  URL photoUrl,
                                  Boolean isArchived,
                                  List<PersonResponseMinimalDTO> members,
                                  List<PersonResponseMinimalDTO> apprentices,
                                  List<PersonResponseMinimalDTO> leaders,
                                  LocalDateTime createdAt,
                                  LocalDateTime updatedAt) {

  public static MinistryResponseDTO convertMinistryDTO(MinistryModel ministry) {
    List<PersonResponseMinimalDTO> members = (ministry.getMembers() != null) ?
          ministry.getMembers().stream().map(PersonResponseMinimalDTO::convertPersonMinimalDTO)
                .collect(Collectors.toList()) : null;

    List<PersonResponseMinimalDTO> apprentices = (ministry.getApprentices() != null) ?
          ministry.getApprentices().stream().map(PersonResponseMinimalDTO::convertPersonMinimalDTO)
                .collect(Collectors.toList()) : null;

    List<PersonResponseMinimalDTO> leaders = (ministry.getLeaders() != null) ?
          ministry.getLeaders().stream().map(PersonResponseMinimalDTO::convertPersonMinimalDTO)
                .collect(Collectors.toList()) : null;

    return new MinistryResponseDTO(
          ministry.getIdMinistry(),
          ministry.getName(),
          ministry.getDescription(),
          ministry.getFoundation(),
          ministry.getPhotoUrl(),
          ministry.getArchived(),
          members,
          apprentices,
          leaders,
          ministry.getCreatedAt(),
          ministry.getUpdatedAt());
  }
}
