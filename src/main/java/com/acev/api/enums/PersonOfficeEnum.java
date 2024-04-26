package com.acev.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PersonOfficeEnum {
  MEMBER,
  APPRENTICE,
  LEADER;

  @JsonCreator
  public static PersonOfficeEnum fromString(String value) {
    for (PersonOfficeEnum office : PersonOfficeEnum.values()) {
      if (office.name().equalsIgnoreCase(value)) {
        return office;
      }
    }
    throw new IllegalArgumentException("Invalid PersonOfficeEnum value: " + value);
  }

  @JsonValue
  public String toString() {
    return name();
  }
}
