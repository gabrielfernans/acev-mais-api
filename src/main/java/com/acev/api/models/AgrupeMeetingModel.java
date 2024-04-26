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
@Table(name = "TB_AGRUPE_MEETINGS")
public class AgrupeMeetingModel implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID idAgrupeMeeting;

  @Column(columnDefinition = "TIMESTAMP")
  private LocalDateTime date;

  @Column(nullable = false)
  private Boolean customLesson;

  private String customLessonTitle;

  private List<String> guests;

  private URL photoUrl;

  private String note;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @ManyToOne
  @JoinColumn(name = "id_lesson")
  private LessonModel lesson;

  @ManyToOne
  @JoinColumn(name = "id_agrupe")
  private AgrupeModel agrupe;

  @ManyToMany
  @JoinTable(
        name = "tb_agrupe_meeting_persons",
        joinColumns = @JoinColumn(name = "id_agrupe_meeting"),
        inverseJoinColumns = @JoinColumn(name = "id_person")
  )
  private List<PersonModel> participants;

  public UUID getIdAgrupeMeeting() {
    return idAgrupeMeeting;
  }

  public void setIdAgrupeMeeting(UUID idAgrupeMeeting) {
    this.idAgrupeMeeting = idAgrupeMeeting;
  }

  public AgrupeModel getAgrupe() {
    return agrupe;
  }

  public void setAgrupe(AgrupeModel agrupe) {
    this.agrupe = agrupe;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public void setDate(LocalDateTime date) {
    this.date = date;
  }

  public LessonModel getLesson() {
    return lesson;
  }

  public void setLesson(LessonModel lesson) {
    this.lesson = lesson;
  }

  public List<PersonModel> getParticipants() {
    return participants;
  }

  public void setParticipants(List<PersonModel> participants) {
    this.participants = participants;
  }

  public List<String> getGuests() {
    return guests;
  }

  public void setGuests(List<String> guests) {
    this.guests = guests;
  }

  public URL getPhotoUrl() {
    return photoUrl;
  }

  public void setPhotoUrl(URL photoUrl) {
    this.photoUrl = photoUrl;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
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

  public Boolean getCustomLesson() {
    return customLesson;
  }

  public void setCustomLesson(Boolean customLesson) {
    this.customLesson = customLesson;
  }

  public String getCustomLessonTitle() {
    return customLessonTitle;
  }

  public void setCustomLessonTitle(String customLessonTitle) {
    this.customLessonTitle = customLessonTitle;
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
  protected void onRemove() {
    if (lesson != null) lesson.getAgrupeMeetings().remove(this);
    if (agrupe != null) agrupe.getAgrupeMeetings().remove(this);

    for (PersonModel person : participants) {
      person.getAttendedMeetings().remove(this);
    }
  }

  public void addParticipant(PersonModel person) {
    if (this.participants == null) {
      this.participants = new ArrayList<>();
    }
    this.participants.add(person);
    person.addAttendedMeeting(this);
  }
}
