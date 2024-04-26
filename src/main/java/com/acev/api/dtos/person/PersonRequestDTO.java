package com.acev.api.dtos.person;

import com.acev.api.dtos.address.AddressRequestDTO;
import com.acev.api.enums.EntryCategoryEnum;
import com.acev.api.enums.GenderEnum;
import com.acev.api.enums.MaritalStatusEnum;
import com.acev.api.enums.MemberTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record PersonRequestDTO(@NotBlank(message = "O campo 'name' é obrigatório") String name,
                               String email,
                               @NotNull(message = "O campo 'memberType' é obrigatório") MemberTypeEnum memberType,
                               String phone,
                               @NotNull(message = "O campo 'gender' é obrigatório") GenderEnum gender,
                               @NotNull(message = "O campo 'maritalStatus' é obrigatório") MaritalStatusEnum maritalStatus,
                               @NotNull(message = "O campo 'birthDate' é obrigatório") LocalDateTime birthDate,
                               LocalDateTime entryDate,
                               EntryCategoryEnum entryCategory,
                               AddressRequestDTO address) {
}
