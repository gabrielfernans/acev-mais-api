package com.acev.api.repositories;

import com.acev.api.models.AddressModel;
import com.acev.api.models.MinistryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<AddressModel, UUID> {
}
