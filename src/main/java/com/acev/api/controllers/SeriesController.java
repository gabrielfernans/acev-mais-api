package com.acev.api.controllers;

import com.acev.api.dtos.series.SeriesRequestDTO;
import com.acev.api.dtos.series.SeriesResponseDTO;
import com.acev.api.exceptions.NotFoundException;
import com.acev.api.exceptions.ServerResponse;
import com.acev.api.models.SeriesModel;
import com.acev.api.services.S3Service;
import com.acev.api.services.SeriesService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Tag(name = "Series")
@RequestMapping("/api/series")
@RestController
public class SeriesController {
  @Autowired
  private SeriesService seriesService;

  @Autowired
  private S3Service s3Service;

  @PostMapping()
  public ResponseEntity<SeriesResponseDTO> createSeries(@RequestBody @Valid SeriesRequestDTO seriesDTO) {
    SeriesModel series = seriesService.createSeries(seriesDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(SeriesResponseDTO.convertSeriesDTO(series));
  }

  @PostMapping("/{id}/photo")
  public ResponseEntity<SeriesResponseDTO> setSeriesPhoto(@PathVariable(value = "id") UUID id,
                                                          @RequestParam("photo") MultipartFile photo) {
    Optional<SeriesModel> seriesO = seriesService.getSeriesById(id);

    if (seriesO.isPresent()) {
      SeriesModel series = seriesO.get();

      // Exclui a foto antiga, se existir
      if (series.getPhotoUrl() != null) {
        String oldPhotoKey = s3Service.extractKeyFromUrl(series.getPhotoUrl().toString());
        s3Service.deleteFile(oldPhotoKey);
      }

      URL newPhotoUrl = s3Service.uploadFileToS3(photo);

      series.setPhotoUrl(newPhotoUrl); // Armazena o URL diretamente no modelo
      seriesService.updateSeries(series);

      return ResponseEntity.status(HttpStatus.OK).body(SeriesResponseDTO.convertSeriesDTO(series));
    }
    throw new NotFoundException("Série não está cadastrada no sistema.");
  }

  @GetMapping("/{id}")
  public ResponseEntity<SeriesResponseDTO> getSeries(@PathVariable(value = "id") UUID id) {
    Optional<SeriesModel> seriesO = seriesService.getSeriesById(id);

    if (seriesO.isPresent()) {
      SeriesModel series = seriesO.get();
      return ResponseEntity.status(HttpStatus.OK).body(SeriesResponseDTO.convertSeriesDTO(series));
    }
    throw new NotFoundException("Esta série de lições não está cadastrada no sistema.");
  }

  @GetMapping()
  public ResponseEntity<List<SeriesResponseDTO>> getAllSeries() {
    return ResponseEntity.status(HttpStatus.OK).body(seriesService.getAllSeries());
  }

  @PutMapping("/{id}")
  public ResponseEntity<SeriesResponseDTO> updateSeries(@PathVariable(value = "id") UUID id,
                                                        @RequestBody @Valid SeriesRequestDTO seriesDTO) {
    Optional<SeriesModel> seriesO = seriesService.getSeriesById(id);

    if (seriesO.isPresent()) {
      SeriesModel series = seriesService.updateSeries(seriesDTO, seriesO.get());
      return ResponseEntity.status(HttpStatus.OK).body(SeriesResponseDTO.convertSeriesDTO(series));
    }
    throw new NotFoundException("Esta série de lições não está cadastrada no sistema.");
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ServerResponse> deleteSeries(@PathVariable(value = "id") UUID id) {
    Optional<SeriesModel> seriesO = seriesService.getSeriesById(id);

    if (seriesO.isPresent()) {
      SeriesModel series = seriesO.get();

      // Exclui a foto do bucket, se existir
      if (series.getPhotoUrl() != null) {
        String oldPhotoKey = s3Service.extractKeyFromUrl(series.getPhotoUrl().toString());
        s3Service.deleteFile(oldPhotoKey);
      }

      seriesService.deleteSeries(series);
      var response = new ServerResponse("Série de lições excluída com sucesso.", HttpStatus.OK.value());
      return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    throw new NotFoundException("Esta série de lições não está cadastrada no sistema.");
  }
}
