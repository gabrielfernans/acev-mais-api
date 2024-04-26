package com.acev.api.exceptions;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class ServerResponse {
  private String message;
  private List<String> details;
  private Integer status;

  public ServerResponse(String message, List<String> details, Integer status) {
    super();
    this.message = message;
    this.details = details;
    this.status = status;
  }

  public ServerResponse(String message, Integer status) {
    super();
    this.message = message;
    this.details = new ArrayList<>();
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public List<String> getDetails() {
    return details;
  }

  public void setDetails(List<String> details) {
    this.details = details;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }
}
