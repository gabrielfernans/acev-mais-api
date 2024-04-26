package com.acev.api.repositories;

import com.acev.api.models.SeriesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SeriesRepository extends JpaRepository<SeriesModel, UUID> {
}
