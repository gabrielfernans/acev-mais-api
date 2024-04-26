package com.acev.api.services;

import com.acev.api.dtos.series.SeriesRequestDTO;
import com.acev.api.dtos.series.SeriesResponseDTO;
import com.acev.api.models.SeriesModel;
import com.acev.api.repositories.SeriesRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SeriesService {
  @Autowired
  private SeriesRepository seriesRepository;

  public Optional<SeriesModel> getSeriesById(UUID id) {
    return seriesRepository.findById(id);
  }

  public List<SeriesResponseDTO> getAllSeries() {
    List<SeriesModel> seriesList = seriesRepository.findAll();

    return seriesList.stream().map(SeriesResponseDTO::convertSeriesDTO)
          .collect(Collectors.toList());
  }

  public SeriesModel createSeries(SeriesRequestDTO seriesDTO) {
    var seriesModel = new SeriesModel();
    BeanUtils.copyProperties(seriesDTO, seriesModel);
    return seriesRepository.save(seriesModel);
  }

  public SeriesModel updateSeries(SeriesRequestDTO seriesDTO, SeriesModel series) {
    BeanUtils.copyProperties(seriesDTO, series);
    return seriesRepository.save(series);
  }

  public void updateSeries(SeriesModel series) {
    seriesRepository.save(series);
  }

  public void deleteSeries(SeriesModel series) {
    seriesRepository.delete(series);
  }
}
