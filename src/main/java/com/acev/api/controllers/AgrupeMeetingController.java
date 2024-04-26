package com.acev.api.controllers;

import com.acev.api.dtos.agrupeMeeting.AgrupeMeetingRequestDTO;
import com.acev.api.dtos.agrupeMeeting.AgrupeMeetingResponseDTO;
import com.acev.api.exceptions.NotFoundException;
import com.acev.api.exceptions.ServerResponse;
import com.acev.api.models.AgrupeMeetingModel;
import com.acev.api.services.AgrupeMeetingService;
import com.acev.api.services.S3Service;
import com.acev.api.util.classes.PagedResponse;
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

import static com.acev.api.dtos.agrupeMeeting.AgrupeMeetingResponseDTO.convertAgrupeMeetingDTO;

@Tag(name = "Agrupe Meetings")
@RequestMapping("/api/agrupe-meetings")
@RestController
public class AgrupeMeetingController {
  @Autowired
  AgrupeMeetingService agrupeMeetingService;

  @Autowired
  S3Service s3Service;

  @PostMapping()
  public ResponseEntity<AgrupeMeetingResponseDTO> createAgrupeMeeting(@RequestBody @Valid
                                                                      AgrupeMeetingRequestDTO agrupeMeetingDTO) {
    AgrupeMeetingModel agrupeMeeting = agrupeMeetingService.createAgrupeMeeting(agrupeMeetingDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(convertAgrupeMeetingDTO(agrupeMeeting));
  }

  @PostMapping(value = "/{id}/photo", consumes = "multipart/form-data")
  public ResponseEntity<AgrupeMeetingResponseDTO> setAgrupeMeetingPhoto(@PathVariable(value = "id") UUID id,
                                                                        @RequestParam("photo") MultipartFile photo) {
    Optional<AgrupeMeetingModel> agrupeMeetingO = agrupeMeetingService.getAgrupeMeetingById(id);

    if (agrupeMeetingO.isPresent()) {
      AgrupeMeetingModel agrupeMeeting = agrupeMeetingO.get();

      // Exclui a foto antiga do bucket, se existir
      if (agrupeMeeting.getPhotoUrl() != null) {
        String oldPhotoKey = s3Service.extractKeyFromUrl(agrupeMeeting.getPhotoUrl().toString());
        s3Service.deleteFile(oldPhotoKey);
      }

      URL newPhotoUrl = s3Service.uploadFileToS3(photo);

      agrupeMeeting.setPhotoUrl(newPhotoUrl); // Armazena o URL diretamente no modelo
      agrupeMeetingService.updateAgrupeMeeting(agrupeMeeting);

      return ResponseEntity.status(HttpStatus.OK).body(convertAgrupeMeetingDTO(agrupeMeeting));
    }
    throw new NotFoundException("Esta reunião de agrupe não está cadastrada no sistema.");
  }

  @GetMapping("/{id}")
  public ResponseEntity<AgrupeMeetingResponseDTO> getAgrupeMeeting(@PathVariable(value = "id") UUID id) {
    Optional<AgrupeMeetingModel> agrupeMeetingO = agrupeMeetingService.getAgrupeMeetingById(id);

    if (agrupeMeetingO.isPresent()) {
      AgrupeMeetingModel agrupeMeeting = agrupeMeetingO.get();
      return ResponseEntity.status(HttpStatus.OK).body(convertAgrupeMeetingDTO(agrupeMeeting));
    }
    throw new NotFoundException("Esta reunião de agrupe não está cadastrada no sistema.");
  }

  @GetMapping()
  public ResponseEntity<PagedResponse<AgrupeMeetingResponseDTO>> getAllAgrupeMeetings(
        @RequestParam(required = false) String idAgrupe,
        @RequestParam(required = false) String idLesson,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
  ) {
    PagedResponse<AgrupeMeetingResponseDTO> response = agrupeMeetingService.getAllAgrupeMeetings(idAgrupe, idLesson, page, size);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<AgrupeMeetingResponseDTO> updateAgrupeMeeting(@PathVariable(value = "id") UUID id,
                                                                      @RequestBody @Valid
                                                                      AgrupeMeetingRequestDTO agrupeMeetingDTO) {
    Optional<AgrupeMeetingModel> agrupeMeetingO = agrupeMeetingService.getAgrupeMeetingById(id);

    if (agrupeMeetingO.isPresent()) {
      AgrupeMeetingModel agrupeMeeting = agrupeMeetingService.updateAgrupeMeeting(agrupeMeetingDTO, agrupeMeetingO.get());
      return ResponseEntity.status(HttpStatus.OK).body(convertAgrupeMeetingDTO(agrupeMeeting));
    }
    throw new NotFoundException("Esta reunião de agrupe não está cadastrada no sistema.");
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ServerResponse> deleteAgrupeMeeting(@PathVariable(value = "id") UUID id) {
    Optional<AgrupeMeetingModel> agrupeMeetingO = agrupeMeetingService.getAgrupeMeetingById(id);

    if (agrupeMeetingO.isPresent()) {
      agrupeMeetingService.deleteAgrupeMeeting(agrupeMeetingO.get());
      var response = new ServerResponse("Reunião de agrupe excluida com sucesso.", HttpStatus.OK.value());
      return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    throw new NotFoundException("Esta reunião de agrupe não está cadastrada no sistema.");
  }
}
