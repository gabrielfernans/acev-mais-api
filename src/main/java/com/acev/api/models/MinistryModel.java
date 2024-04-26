package com.acev.api.models;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "TB_MINISTRIES")
public class MinistryModel implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID idMinistry;

  @Column(nullable = false)
  private String name;

  private String description;

  @Column(columnDefinition = "TIMESTAMP")
  private LocalDateTime foundation;

  private URL photoUrl;

  private Boolean archived;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @ManyToMany
  @JoinTable(name = "tb_ministry_members", joinColumns = @JoinColumn(name = "id_ministry"), inverseJoinColumns = @JoinColumn(name = "id_person"))
  private List<PersonModel> members;

  @ManyToMany
  @JoinTable(name = "tb_ministry_leaders", joinColumns = @JoinColumn(name = "id_ministry"), inverseJoinColumns = @JoinColumn(name = "id_person"))
  private List<PersonModel> leaders;

  @ManyToMany
  @JoinTable(name = "tb_ministry_apprentices", joinColumns = @JoinColumn(name = "id_ministry"), inverseJoinColumns = @JoinColumn(name = "id_person"))
  private List<PersonModel> apprentices;

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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public UUID getIdMinistry() {
    return idMinistry;
  }

  public void setIdMinistry(UUID idMinistry) {
    this.idMinistry = idMinistry;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LocalDateTime getFoundation() {
    return foundation;
  }

  public void setFoundation(LocalDateTime foundation) {
    this.foundation = foundation;
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

  public List<PersonModel> getMembers() {
    return members;
  }

  public void setMembers(List<PersonModel> members) {
    this.members = members;
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
    for (PersonModel member : members) {
      for (MinistryModel ministry : member.getMemberOfMinistries()) {
        member.getMemberOfMinistries().remove(this);
      }
    }

    for (PersonModel leader : leaders) {
      for (MinistryModel ministry : leader.getLeaderOfMinistries()) {
        leader.getLeaderOfMinistries().remove(this);
      }
    }

    for (PersonModel apprentice : apprentices) {
      for (MinistryModel ministry : apprentice.getApprenticeOfMinistries()) {
        apprentice.getApprenticeOfMinistries().remove(this);
      }
    }
  }

  public void removeMember(PersonModel member) {
    this.members.remove(member);
  }

  public void removeLeader(PersonModel leader) {
    this.leaders.remove(leader);
  }

  public void removeApprentice(PersonModel apprentice) {
    this.apprentices.remove(apprentice);
  }
}
