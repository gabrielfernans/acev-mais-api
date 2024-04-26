package com.acev.api.dtos.ministry;

import com.acev.api.models.MinistryModel;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MinistryResponseMinimalDTO(@NotNull UUID id,
                                         @NotNull String name) {

  public static MinistryResponseMinimalDTO convertMinistryMinimalDTO(MinistryModel ministry) {
    return new MinistryResponseMinimalDTO(
          ministry.getIdMinistry(),
          ministry.getName());
  }
}
