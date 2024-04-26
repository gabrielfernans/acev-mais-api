package com.acev.api.dtos.address;

import jakarta.validation.constraints.NotBlank;

public record AddressRequestDTO(@NotBlank(message = "O campo 'street' é obrigatório") String street,
                                @NotBlank(message = "O campo 'number' é obrigatório") String number,
                                String complement,
                                @NotBlank(message = "O campo 'neighborhood' é obrigatório") String neighborhood,
                                @NotBlank(message = "O campo 'city' é obrigatório") String city,
                                @NotBlank(message = "O campo 'state' é obrigatório") String state,
                                @NotBlank(message = "O campo 'postalCode' é obrigatório") String postalCode
) {
}
