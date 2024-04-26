package com.acev.api.dtos.person;

import com.acev.api.dtos.address.AddressResponseDTO;
import com.acev.api.dtos.ministry.MinistryResponseMinimalDTO;
import com.acev.api.enums.EntryCategoryEnum;
import com.acev.api.enums.GenderEnum;
import com.acev.api.enums.MaritalStatusEnum;
import com.acev.api.enums.MemberTypeEnum;
import com.acev.api.models.AgrupeModel;
import com.acev.api.models.PersonModel;
import jakarta.validation.constraints.NotNull;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.acev.api.dtos.address.AddressResponseDTO.convertAddressDTO;

public record PersonResponseDTO(@NotNull UUID id,
                                @NotNull String name,
                                @NotNull MemberTypeEnum memberType,
                                @NotNull GenderEnum gender,
                                @NotNull MaritalStatusEnum maritalStatus,
                                @NotNull LocalDateTime birthDate,
                                String email,
                                String phone,
                                URL photoUrl,
                                LocalDateTime entryDate,
                                EntryCategoryEnum entryCategory,
                                Boolean isAgrupeLeader,
                                Boolean isAgrupeApprentice,
                                Boolean isArchived,
                                AddressResponseDTO address,
                                UUID idAgrupe,
                                String agrupeName,
                                List<MinistryResponseMinimalDTO> ministries,
                                LocalDateTime createdAt,
                                LocalDateTime updatedAt) {
  public static PersonResponseDTO convertPersonDTO(PersonModel person) {
    AddressResponseDTO addressDTO = (person.getAddress() != null) ?
          convertAddressDTO(person.getAddress()) : null;

    AgrupeModel agrupe = (person.getFrequentedAgrupe() != null) ?
          person.getFrequentedAgrupe() : null;

    List<MinistryResponseMinimalDTO> ministries = (person.getMemberOfMinistries() != null) ?
          person.getMemberOfMinistries().stream().map(MinistryResponseMinimalDTO::convertMinistryMinimalDTO)
                .collect(Collectors.toList()) : null;

    return new PersonResponseDTO(
          person.getIdPerson(),
          person.getName(),
          person.getMemberType(),
          person.getGender(),
          person.getMaritalStatus(),
          person.getBirthDate(),
          person.getEmail(),
          person.getPhone(),
          person.getPhotoUrl(),
          person.getEntryDate(),
          person.getEntryCategory(),
          person.isAgrupeLeader(),
          person.isAgrupeApprentice(),
          person.getArchived(),
          addressDTO,
          agrupe != null ? agrupe.getIdAgrupe() : null,
          agrupe != null ? agrupe.getName() : null,
          ministries,
          person.getCreatedAt(),
          person.getUpdatedAt()
    );
  }
}
