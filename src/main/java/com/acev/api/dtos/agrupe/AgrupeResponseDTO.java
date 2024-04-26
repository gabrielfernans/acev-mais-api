package com.acev.api.dtos.agrupe;

import com.acev.api.dtos.address.AddressResponseDTO;
import com.acev.api.dtos.person.PersonResponseMinimalDTO;
import com.acev.api.enums.AgrupeCategoryEnum;
import com.acev.api.enums.WeekdayEnum;
import com.acev.api.models.AgrupeModel;
import jakarta.validation.constraints.NotNull;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.acev.api.dtos.address.AddressResponseDTO.convertAddressDTO;

public record AgrupeResponseDTO(@NotNull UUID id,
                                @NotNull String name,
                                @NotNull AgrupeCategoryEnum category,
                                WeekdayEnum dayOfMeeting,
                                String description,
                                URL photoUrl,
                                Boolean isArchived,
                                AddressResponseDTO address,
                                List<PersonResponseMinimalDTO> frequenters,
                                List<PersonResponseMinimalDTO> apprentices,
                                List<PersonResponseMinimalDTO> leaders,
                                Integer meetingsCount,
                                LocalDateTime createdAt,
                                LocalDateTime updatedAt) {
  public static AgrupeResponseDTO convertAgrupeDTO(AgrupeModel agrupe) {
    AddressResponseDTO addressDTO = (agrupe.getAddress() != null) ?
          convertAddressDTO(agrupe.getAddress()) : null;

    List<PersonResponseMinimalDTO> frequenters = (agrupe.getFrequenters() != null) ?
          agrupe.getFrequenters().stream().map(PersonResponseMinimalDTO::convertPersonMinimalDTO)
                .collect(Collectors.toList()) : null;

    List<PersonResponseMinimalDTO> apprentices = (agrupe.getApprentices() != null) ?
          agrupe.getApprentices().stream().map(PersonResponseMinimalDTO::convertPersonMinimalDTO)
                .collect(Collectors.toList()) : null;

    List<PersonResponseMinimalDTO> leaders = (agrupe.getLeaders() != null) ?
          agrupe.getLeaders().stream().map(PersonResponseMinimalDTO::convertPersonMinimalDTO)
                .collect(Collectors.toList()) : null;

    return new AgrupeResponseDTO(
          agrupe.getIdAgrupe(),
          agrupe.getName(),
          agrupe.getCategory(),
          agrupe.getDayOfMeeting(),
          agrupe.getDescription(),
          agrupe.getPhotoUrl(),
          agrupe.getArchived(),
          addressDTO,
          frequenters,
          apprentices,
          leaders,
          agrupe.getAgrupeMeetings().size(),
          agrupe.getCreatedAt(),
          agrupe.getUpdatedAt()
    );
  }
}
