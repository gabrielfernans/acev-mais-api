package com.acev.api.models;

import com.acev.api.enums.EntryCategoryEnum;
import com.acev.api.enums.GenderEnum;
import com.acev.api.enums.MaritalStatusEnum;
import com.acev.api.enums.MemberTypeEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "TB_PERSONS")
public class PersonModel implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID idPerson;

  @Column(nullable = false)
  private String name;

  @Column(unique = true)
  private String email;

  @Column(nullable = false)
  private MemberTypeEnum memberType;

  private String phone;

  @Column(nullable = false)
  private GenderEnum gender;

  @Column(nullable = false)
  private MaritalStatusEnum maritalStatus;

  @Column(columnDefinition = "TIMESTAMP", nullable = false)
  private LocalDateTime birthDate;

  @Column(columnDefinition = "TIMESTAMP")
  private LocalDateTime entryDate;

  private EntryCategoryEnum entryCategory;

  private URL photoUrl;

  private Boolean archived;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @OneToOne(mappedBy = "person")
  private UserModel user;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "id_address")
  private AddressModel address;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_frequented_agrupe")
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private AgrupeModel frequentedAgrupe;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_led_agrupe")
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private AgrupeModel ledAgrupe;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_apprenticeship_agrupe")
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private AgrupeModel apprenticeshipAgrupe;

  @ManyToMany(mappedBy = "members")
  private List<MinistryModel> memberOfMinistries;

  @ManyToMany(mappedBy = "leaders")
  private List<MinistryModel> leaderOfMinistries;

  @ManyToMany(mappedBy = "apprentices")
  private List<MinistryModel> apprenticeOfMinistries;

  @ManyToMany(mappedBy = "participants")
  private List<AgrupeMeetingModel> attendedMeetings;

  public Boolean isAgrupeLeader() {
    return ledAgrupe != null;
  }

  public Boolean isAgrupeApprentice() {
    return apprenticeshipAgrupe != null;
  }

  public UUID getIdPerson() {
    return idPerson;
  }

  public void setIdPerson(UUID idPerson) {
    this.idPerson = idPerson;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public MemberTypeEnum getMemberType() {
    return memberType;
  }

  public void setMemberType(MemberTypeEnum memberType) {
    this.memberType = memberType;
  }

  public MaritalStatusEnum getMaritalStatus() {
    return maritalStatus;
  }

  public void setMaritalStatus(MaritalStatusEnum maritalStatus) {
    this.maritalStatus = maritalStatus;
  }

  public LocalDateTime getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(LocalDateTime birthDate) {
    this.birthDate = birthDate;
  }

  public LocalDateTime getEntryDate() {
    return entryDate;
  }

  public void setEntryDate(LocalDateTime entryDate) {
    this.entryDate = entryDate;
  }

  public EntryCategoryEnum getEntryCategory() {
    return entryCategory;
  }

  public void setEntryCategory(EntryCategoryEnum entryCategory) {
    this.entryCategory = entryCategory;
  }

  public GenderEnum getGender() {
    return gender;
  }

  public void setGender(GenderEnum gender) {
    this.gender = gender;
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

  public UserModel getUser() {
    return user;
  }

  public void setUser(UserModel user) {
    this.user = user;
  }

  public AddressModel getAddress() {
    return address;
  }

  public void setAddress(AddressModel address) {
    this.address = address;
  }

  public AgrupeModel getFrequentedAgrupe() {
    return frequentedAgrupe;
  }

  public void setFrequentedAgrupe(AgrupeModel frequentedAgrupe) {
    this.frequentedAgrupe = frequentedAgrupe;
  }

  public AgrupeModel getLedAgrupe() {
    return ledAgrupe;
  }

  public void setLedAgrupe(AgrupeModel ledAgrupe) {
    this.ledAgrupe = ledAgrupe;
  }

  public AgrupeModel getApprenticeshipAgrupe() {
    return apprenticeshipAgrupe;
  }

  public void setApprenticeshipAgrupe(AgrupeModel apprenticeshipAgrupe) {
    this.apprenticeshipAgrupe = apprenticeshipAgrupe;
  }

  public List<MinistryModel> getMemberOfMinistries() {
    return memberOfMinistries;
  }

  public void setMemberOfMinistries(List<MinistryModel> memberOfMinistries) {
    this.memberOfMinistries = memberOfMinistries;
  }

  public List<MinistryModel> getLeaderOfMinistries() {
    return leaderOfMinistries;
  }

  public void setLeaderOfMinistries(List<MinistryModel> leaderOfMinistries) {
    this.leaderOfMinistries = leaderOfMinistries;
  }

  public List<MinistryModel> getApprenticeOfMinistries() {
    return apprenticeOfMinistries;
  }

  public void setApprenticeOfMinistries(List<MinistryModel> apprenticeOfMinistries) {
    this.apprenticeOfMinistries = apprenticeOfMinistries;
  }

  public List<AgrupeMeetingModel> getAttendedMeetings() {
    return attendedMeetings;
  }

  public void setAttendedMeetings(List<AgrupeMeetingModel> attendedMeetings) {
    this.attendedMeetings = attendedMeetings;
  }

  public void removeAttendedMeeting(AgrupeMeetingModel meeting) {
    this.attendedMeetings.remove(meeting);
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
    user.setPerson(null);
    frequentedAgrupe.getFrequenters().remove(this);
    ledAgrupe.getLeaders().remove(this);
    apprenticeshipAgrupe.getApprentices().remove(this);

    for (MinistryModel ministry : memberOfMinistries) {
      ministry.removeMember(this);
    }

    for (MinistryModel ministry : apprenticeOfMinistries) {
      ministry.removeApprentice(this);
    }

    for (MinistryModel ministry : leaderOfMinistries) {
      ministry.removeLeader(this);
    }

    for (AgrupeMeetingModel agrupeMeeting : attendedMeetings) {
      agrupeMeeting.getParticipants().remove(this);
    }
  }

  public void addAttendedMeeting(AgrupeMeetingModel meeting) {
    if (this.attendedMeetings == null) {
      this.attendedMeetings = new ArrayList<>();
    }
    this.attendedMeetings.add(meeting);
  }
}
