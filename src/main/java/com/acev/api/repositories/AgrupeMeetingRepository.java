package com.acev.api.repositories;

import com.acev.api.models.AgrupeMeetingModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AgrupeMeetingRepository extends JpaRepository<AgrupeMeetingModel, UUID> {
  Page<AgrupeMeetingModel> findAll(Specification<AgrupeMeetingModel> specification, Pageable pageable);
}
