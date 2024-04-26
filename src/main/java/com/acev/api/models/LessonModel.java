package com.acev.api.models;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;

import java.io.Serial;
import java.io.Serializable;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "TB_LESSONS")
public class LessonModel implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID idLesson;

  private Integer number;

  @Column(nullable = false)
  private String title;

  private String origin;

  private String adaptation;

  private String revision;

  private String greeting;

  private List<String> musicSuggestions;

  private URL pdfUrl;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @ManyToOne
  @JoinColumn(name = "id_series")
  private SeriesModel series;

  @OneToMany(mappedBy = "lesson")
  private List<AgrupeMeetingModel> agrupeMeetings;

  public UUID getIdLesson() {
    return idLesson;
  }

  public void setIdLesson(UUID idLesson) {
    this.idLesson = idLesson;
  }

  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getOrigin() {
    return origin;
  }

  public void setOrigin(String origin) {
    this.origin = origin;
  }

  public String getAdaptation() {
    return adaptation;
  }

  public void setAdaptation(String adaptation) {
    this.adaptation = adaptation;
  }

  public String getRevision() {
    return revision;
  }

  public void setRevision(String revision) {
    this.revision = revision;
  }

  public String getGreeting() {
    return greeting;
  }

  public void setGreeting(String greeting) {
    this.greeting = greeting;
  }

  public List<String> getMusicSuggestions() {
    return musicSuggestions;
  }

  public void setMusicSuggestions(List<String> musicSuggestions) {
    this.musicSuggestions = musicSuggestions;
  }

  public URL getPdfUrl() {
    return pdfUrl;
  }

  public void setPdfUrl(URL pdfUrl) {
    this.pdfUrl = pdfUrl;
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

  public SeriesModel getSeries() {
    return series;
  }

  public void setSeries(SeriesModel series) {
    this.series = series;
  }

  public List<AgrupeMeetingModel> getAgrupeMeetings() {
    return agrupeMeetings;
  }

  public void setAgrupeMeetings(List<AgrupeMeetingModel> agrupeMeetings) {
    this.agrupeMeetings = agrupeMeetings;
  }

  @PrePersist
  protected void onCreate() {
    updatedAt = createdAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }

  @PreRemove
  protected void onDelete() {
    if (series != null) series = null;

    for (AgrupeMeetingModel agrupeMeeting : agrupeMeetings) {
      agrupeMeeting.setLesson(null);
    }
  }

  public void addAgrupeMeeting(AgrupeMeetingModel agrupeMeeting) {
    if (this.getAgrupeMeetings() == null) {
      this.setAgrupeMeetings(new ArrayList<>());
    }

    if (agrupeMeeting != null && !this.getAgrupeMeetings().contains(agrupeMeeting)) {
      agrupeMeeting.setLesson(this);
      this.getAgrupeMeetings().add(agrupeMeeting);
    }
  }
}
