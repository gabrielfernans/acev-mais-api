package com.acev.api.models;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "TB_SERIES")
public class SeriesModel implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID idSeries;

  @Column(nullable = false)
  private String title;

  private String period;

  private String author;

  private URL photoUrl;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @OneToMany(mappedBy = "series", cascade = CascadeType.ALL)
  private List<LessonModel> lessons;

  public UUID getIdSeries() {
    return idSeries;
  }

  public void setIdSeries(UUID idSeries) {
    this.idSeries = idSeries;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getPeriod() {
    return period;
  }

  public void setPeriod(String period) {
    this.period = period;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public URL getPhotoUrl() {
    return photoUrl;
  }

  public void setPhotoUrl(URL photoUrl) {
    this.photoUrl = photoUrl;
  }

  public List<LessonModel> getLessons() {
    return lessons;
  }

  public void setLessons(List<LessonModel> lessons) {
    this.lessons = lessons;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  @PrePersist
  protected void onCreate() {
    updatedAt = createdAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }

  public void addLesson(LessonModel lesson) {
    if (lessons == null) {
      lessons = new ArrayList<>();
    }
    lessons.add(lesson);
    lesson.setSeries(this);
  }

  public void removeLesson(LessonModel lesson) {
    if (lessons != null) {
      lessons.remove(lesson);
      lesson.setSeries(null);
    }
  }
}
