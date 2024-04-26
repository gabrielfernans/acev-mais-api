package com.acev.api.util.classes;

import java.util.List;

public class PagedResponse<T> {
  private List<T> data;
  private long totalElements;
  private int currentPage;
  private int totalPages;

  public PagedResponse(List<T> data, long totalElements, int currentPage, int totalPages) {
    this.data = data;
    this.totalElements = totalElements;
    this.currentPage = currentPage;
    this.totalPages = totalPages;
  }

  public List<T> getData() {
    return data;
  }

  public void setData(List<T> data) {
    this.data = data;
  }

  public long getTotalElements() {
    return totalElements;
  }

  public void setTotalElements(long totalElements) {
    this.totalElements = totalElements;
  }

  public int getCurrentPage() {
    return currentPage;
  }

  public void setCurrentPage(int currentPage) {
    this.currentPage = currentPage;
  }

  public int getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(int totalPages) {
    this.totalPages = totalPages;
  }
}
