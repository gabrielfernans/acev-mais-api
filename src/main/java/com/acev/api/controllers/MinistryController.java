package com.acev.api.controllers;

import com.acev.api.dtos.ministry.MinistryRequestDTO;
import com.acev.api.dtos.ministry.MinistryResponseDTO;
import com.acev.api.enums.PersonOfficeEnum;
import com.acev.api.exceptions.NotFoundException;
import com.acev.api.exceptions.ServerResponse;
import com.acev.api.models.MinistryModel;
import com.acev.api.services.MinistryService;
import com.acev.api.services.S3Service;
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

import static com.acev.api.dtos.ministry.MinistryResponseDTO.convertMinistryDTO;

@Tag(name = "Ministries")
@RequestMapping("/api/ministries")
@RestController
public class MinistryController {
  @Autowired
  MinistryService ministryService;

  @Autowired
  S3Service s3Service;

  @PostMapping()
  public ResponseEntity<MinistryResponseDTO> createMinistry(@RequestBody @Valid MinistryRequestDTO ministryDTO) {
    MinistryModel ministry = ministryService.createMinistry(ministryDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(convertMinistryDTO(ministry));
  }

  @PostMapping(value = "/{id}/photo", consumes = "multipart/form-data")
  public ResponseEntity<MinistryResponseDTO> setMinistryPhoto(@PathVariable(value = "id") UUID id,
                                                              @RequestParam("photo") MultipartFile photo) {
    Optional<MinistryModel> ministryO = ministryService.getMinistryById(id);

    if (ministryO.isPresent()) {
      MinistryModel ministry = ministryO.get();

      // Exclui a foto antiga do bucket, se existir
      if (ministry.getPhotoUrl() != null) {
        String oldPhotoKey = s3Service.extractKeyFromUrl(ministry.getPhotoUrl().toString());
        s3Service.deleteFile(oldPhotoKey);
      }

      URL newPhotoUrl = s3Service.uploadFileToS3(photo);

      ministry.setPhotoUrl(newPhotoUrl); // Armazena o URL diretamente no modelo
      ministryService.updateMinistry(ministry);

      return ResponseEntity.status(HttpStatus.OK).body(convertMinistryDTO(ministry));
    }
    throw new NotFoundException("Este ministério não está cadastrado no sistema.");
  }

  @GetMapping("/{id}")
  public ResponseEntity<MinistryResponseDTO> getMinistry(@PathVariable(value = "id") UUID id) {
    Optional<MinistryModel> ministryO = ministryService.getMinistryById(id);

    if (ministryO.isPresent()) {
      MinistryModel ministry = ministryO.get();
      return ResponseEntity.status(HttpStatus.OK).body(convertMinistryDTO(ministry));
    }
    throw new NotFoundException("Ministério não está cadastrado no sistema.");
  }

  @GetMapping()
  public ResponseEntity<List<MinistryResponseDTO>> getAllMinistries() {
    return ResponseEntity.status(HttpStatus.OK).body(ministryService.getAllMinistries());
  }

  @PutMapping("/{id}")
  public ResponseEntity<MinistryResponseDTO> updateMinistry(@PathVariable(value = "id") UUID id,
      @RequestBody @Valid MinistryRequestDTO ministryDTO) {
    Optional<MinistryModel> ministryO = ministryService.getMinistryById(id);

    if (ministryO.isPresent()) {
      MinistryModel ministry = ministryService.updateMinistry(ministryDTO, ministryO.get());
      return ResponseEntity.status(HttpStatus.OK).body(convertMinistryDTO(ministry));
    }
    throw new NotFoundException("Ministério não está cadastrado no sistema.");
  }

  @PutMapping("/{id}/archive")
  public ResponseEntity<ServerResponse> archiveMinistry(@PathVariable(value = "id") UUID id) {
    Optional<MinistryModel> ministryO = ministryService.getMinistryById(id);

    if (ministryO.isPresent()) {
      ministryService.changeMinistryState(ministryO.get(), true);
      var response = new ServerResponse("Ministério arquivado com sucesso.", HttpStatus.OK.value());
      return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    throw new NotFoundException("Ministério não está cadastrado no sistema.");
  }

  @PutMapping("/{id}/restore")
  public ResponseEntity<ServerResponse> restoreMinistry(@PathVariable(value = "id") UUID id) {
    Optional<MinistryModel> ministryO = ministryService.getMinistryById(id);

    if (ministryO.isPresent()) {
      ministryService.changeMinistryState(ministryO.get(), true);
      var response = new ServerResponse("Ministério restaurado com sucesso.", HttpStatus.OK.value());
      return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    throw new NotFoundException("Ministério não está cadastrado no sistema.");
  }

  @PostMapping("/{idMinistry}/persons/{idPerson}")
  public ResponseEntity<MinistryResponseDTO> addMinistryPerson(
        @PathVariable UUID idMinistry,
        @PathVariable UUID idPerson,
        @RequestParam String personOffice) {
    Optional<MinistryModel> ministryO = ministryService.getMinistryById(idMinistry);

    if (ministryO.isPresent()) {
      MinistryModel ministry = ministryService.addMinistryPerson(ministryO.get(), idPerson, PersonOfficeEnum.fromString(personOffice));

      return ResponseEntity.status(HttpStatus.OK).body(convertMinistryDTO(ministry));
    }
    throw new NotFoundException("Este ministério não está cadastrado no sistema.");
  }

  @DeleteMapping("/{idMinistry}/persons/{idPerson}")
  public ResponseEntity<MinistryResponseDTO> removeMinistryPerson(
        @PathVariable UUID idMinistry,
        @PathVariable UUID idPerson,
        @RequestParam String personOffice) {
    Optional<MinistryModel> ministryO = ministryService.getMinistryById(idMinistry);

    if (ministryO.isPresent()) {
      MinistryModel ministry = ministryService.removeMinistryPerson(ministryO.get(), idPerson, PersonOfficeEnum.fromString(personOffice));

      return ResponseEntity.status(HttpStatus.OK).body(convertMinistryDTO(ministry));
    }
    throw new NotFoundException("Este ministério não está cadastrado no sistema.");
  }
}
