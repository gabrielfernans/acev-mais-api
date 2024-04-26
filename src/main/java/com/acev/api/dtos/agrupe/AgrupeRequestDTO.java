package com.acev.api.dtos.agrupe;

import com.acev.api.dtos.address.AddressRequestDTO;
import com.acev.api.enums.AgrupeCategoryEnum;
import com.acev.api.enums.WeekdayEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AgrupeRequestDTO(@NotBlank(message = "name is a mandatory field") String name,
                               @NotNull(message = "category is a mandatory field") AgrupeCategoryEnum category,
                               WeekdayEnum dayOfMeeting,
                               String description,
                               String photoUrl,
                               AddressRequestDTO address) {
}
