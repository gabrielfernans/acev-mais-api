package com.acev.api.dtos.address;

import com.acev.api.models.AddressModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record AddressResponseDTO(@NotNull UUID id,
                                 @NotBlank String street,
                                 @NotBlank String number,
                                 String complement,
                                 @NotBlank String neighborhood,
                                 @NotBlank String city,
                                 @NotBlank String state,
                                 @NotBlank String postalCode,
                                 LocalDateTime createdAt,
                                 LocalDateTime updatedAt) {
  public static AddressResponseDTO convertAddressDTO(AddressModel address) {
    return new AddressResponseDTO(
          address.getIdAddress(),
          address.getStreet(),
          address.getNumber(),
          address.getComplement(),
          address.getNeighborhood(),
          address.getCity(),
          address.getState(),
          address.getPostalCode(),
          address.getCreatedAt(),
          address.getUpdatedAt()
    );
  }
}
