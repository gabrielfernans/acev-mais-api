package com.acev.api.models;

import com.acev.api.enums.AgrupeCategoryEnum;
import com.acev.api.enums.WeekdayEnum;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "TB_AGRUPES")
public class AgrupeModel implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID idAgrupe;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private AgrupeCategoryEnum category;

  private WeekdayEnum dayOfMeeting;

  private String description;

  private URL photoUrl;

  private Boolean archived;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "id_address")
  private AddressModel address;

  @OneToMany(mappedBy = "frequentedAgrupe", fetch = FetchType.LAZY)
  private List<PersonModel> frequenters;

  @OneToMany(mappedBy = "ledAgrupe", fetch = FetchType.LAZY)
  private List<PersonModel> leaders;

  @OneToMany(mappedBy = "apprenticeshipAgrupe", fetch = FetchType.LAZY)
  private List<PersonModel> apprentices;

  @OneToMany(mappedBy = "agrupe", cascade = CascadeType.ALL)
  private List<AgrupeMeetingModel> agrupeMeetings;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  public void removeAgrupeMeeting(AgrupeMeetingModel meeting) {
    this.agrupeMeetings.remove(meeting);
  }

  public UUID getIdAgrupe() {
      return idAgrupe;
  }

  public void setIdAgrupe(UUID idAgrupe) {
      this.idAgrupe = idAgrupe;
  }

  public String getName() {
      return name;
  }

  public void setName(String name) {
      this.name = name;
  }

  public AgrupeCategoryEnum getCategory() {
      return category;
  }

  public void setCategory(AgrupeCategoryEnum category) {
      this.category = category;
  }

  public WeekdayEnum getDayOfMeeting() {
    return dayOfMeeting;
  }

  public void setDayOfMeeting(WeekdayEnum dayOfMeeting) {
    this.dayOfMeeting = dayOfMeeting;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public URL getPhotoUrl() {
    return photoUrl;
  }

  public void setPhotoUrl(URL photoUrl) {
    this.photoUrl = photoUrl;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public AddressModel getAddress() {
    return address;
  }

  public void setAddress(AddressModel address) {
    this.address = address;
  }

  public List<PersonModel> getFrequenters() {
      return frequenters;
  }

  public void setFrequenters(List<PersonModel> frequenters) {
      this.frequenters = frequenters;
  }

  public List<PersonModel> getLeaders() {
      return leaders;
  }

  public void setLeaders(List<PersonModel> leaders) {
      this.leaders = leaders;
  }

  public List<PersonModel> getApprentices() {
      return apprentices;
  }

  public void setApprentices(List<PersonModel> apprentices) {
      this.apprentices = apprentices;
  }

  public List<AgrupeMeetingModel> getAgrupeMeetings() {
      return agrupeMeetings;
  }

  public void setAgrupeMeetings(List<AgrupeMeetingModel> agrupeMeetings) {
      this.agrupeMeetings = agrupeMeetings;
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
    archived = false;
    updatedAt = createdAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }

  @PreRemove
  protected void onRemove() {
    for (PersonModel frequenter : frequenters) {
      frequenter.setFrequentedAgrupe(this);
    }

    for (PersonModel leader : leaders) {
      leader.setLedAgrupe(this);
    }

    for (PersonModel apprentice : apprentices) {
      apprentice.setApprenticeshipAgrupe(this);
    }
  }

  public void addPerson(PersonModel person) {
    if (person != null && !this.getFrequenters().contains(person)) {
      person.setFrequentedAgrupe(this);
      this.getFrequenters().add(person);
    }
  }

  public void addAgrupeMeeting(AgrupeMeetingModel agrupeMeeting) {
    if (this.getAgrupeMeetings() == null) {
      this.setAgrupeMeetings(new ArrayList<>());
    }

    if (agrupeMeeting != null && !this.getAgrupeMeetings().contains(agrupeMeeting)) {
      agrupeMeeting.setAgrupe(this);
      this.getAgrupeMeetings().add(agrupeMeeting);
    }
  }
}
