package com.acev.api.repositories;

import com.acev.api.enums.MemberTypeEnum;
import com.acev.api.models.PersonModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonRepository extends JpaRepository<PersonModel, UUID>, JpaSpecificationExecutor<PersonModel> {
  Optional<PersonModel> findByEmail(String email);

  @Query("SELECT p FROM PersonModel p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%'))")
  List<PersonModel> findByNameContaining(String query, Pageable pageable);

  Long countByMemberTypeAndArchivedIsFalse(MemberTypeEnum memberType);

  Long countByFrequentedAgrupeIsNotNullAndMemberTypeAndArchivedIsFalse(MemberTypeEnum memberType);

  Long countByMemberOfMinistriesIsNotNullAndArchivedIsFalse();
}
