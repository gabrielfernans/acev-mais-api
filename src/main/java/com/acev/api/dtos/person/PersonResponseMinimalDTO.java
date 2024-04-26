package com.acev.api.dtos.person;

import com.acev.api.models.PersonModel;
import jakarta.validation.constraints.NotNull;

import java.net.URL;
import java.util.UUID;

public record PersonResponseMinimalDTO(@NotNull UUID id,
                                       @NotNull String name,
                                       String email,
                                       URL photoUrl,
                                       Boolean isAgrupeLeader,
                                       Boolean isAgrupeApprentice) {
  public static PersonResponseMinimalDTO convertPersonMinimalDTO(PersonModel person) {
    return new PersonResponseMinimalDTO(
          person.getIdPerson(),
          person.getName(),
          person.getEmail(),
          person.getPhotoUrl(),
          person.isAgrupeLeader(),
          person.isAgrupeApprentice()
    );
  }
}
