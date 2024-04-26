package com.acev.api.controllers;

import com.acev.api.dtos.agrupe.AgrupeRequestDTO;
import com.acev.api.dtos.agrupe.AgrupeResponseDTO;
import com.acev.api.enums.PersonOfficeEnum;
import com.acev.api.exceptions.NotFoundException;
import com.acev.api.exceptions.ServerResponse;
import com.acev.api.models.AgrupeModel;
import com.acev.api.services.AgrupeService;
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

import static com.acev.api.dtos.agrupe.AgrupeResponseDTO.convertAgrupeDTO;

@Tag(name = "Agrupes")
@RequestMapping("/api/agrupes")
@RestController
public class AgrupeController {
  @Autowired
  AgrupeService agrupeService;

  @Autowired
  S3Service s3Service;

  @PostMapping()
  public ResponseEntity<AgrupeResponseDTO> createAgrupe(@RequestBody @Valid AgrupeRequestDTO agrupeDTO) {
    AgrupeModel agrupe = agrupeService.createAgrupe(agrupeDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(convertAgrupeDTO(agrupe));
  }

  @PostMapping(value = "/{id}/photo", consumes = "multipart/form-data")
  public ResponseEntity<AgrupeResponseDTO> setAgrupePhoto(@PathVariable(value = "id") UUID id,
                                                          @RequestParam("photo") MultipartFile photo) {
    Optional<AgrupeModel> agrupeO = agrupeService.getAgrupeById(id);

    if (agrupeO.isPresent()) {
      AgrupeModel agrupe = agrupeO.get();

      // Exclui a foto antiga do bucket, se existir
      if (agrupe.getPhotoUrl() != null) {
        String oldPhotoKey = s3Service.extractKeyFromUrl(agrupe.getPhotoUrl().toString());
        s3Service.deleteFile(oldPhotoKey);
      }

      URL newPhotoUrl = s3Service.uploadFileToS3(photo);

      agrupe.setPhotoUrl(newPhotoUrl); // Armazena o URL diretamente no modelo
      agrupeService.updateAgrupe(agrupe);

      return ResponseEntity.status(HttpStatus.OK).body(convertAgrupeDTO(agrupe));
    }
    throw new NotFoundException("Este agrupe não está cadastrado no sistema.");
  }

  @GetMapping("/{id}")
  public ResponseEntity<AgrupeResponseDTO> getAgrupe(@PathVariable(value = "id") UUID id) {
    Optional<AgrupeModel> agrupeO = agrupeService.getAgrupeById(id);

    if (agrupeO.isPresent()) {
      AgrupeModel agrupe = agrupeO.get();
      return ResponseEntity.status(HttpStatus.OK).body(convertAgrupeDTO(agrupe));
    }
    throw new NotFoundException("Este agrupe não está cadastrado no sistema.");
  }

  @GetMapping()
  public ResponseEntity<List<AgrupeResponseDTO>> getAllAgrupes() {
    return ResponseEntity.status(HttpStatus.OK).body(agrupeService.getAllAgrupes());
  }

  @PutMapping("/{id}")
  public ResponseEntity<AgrupeResponseDTO> updateAgrupe(@PathVariable(value = "id") UUID id,
      @RequestBody @Valid AgrupeRequestDTO agrupeDTO) {
    Optional<AgrupeModel> agrupeO = agrupeService.getAgrupeById(id);

    if (agrupeO.isPresent()) {
      AgrupeModel agrupe = agrupeService.updateAgrupe(agrupeDTO, agrupeO.get());
      return ResponseEntity.status(HttpStatus.OK).body(convertAgrupeDTO(agrupe));
    }
    throw new NotFoundException("Este agrupe não está cadastrado no sistema.");
  }

  @PutMapping("/{id}/archive")
  public ResponseEntity<ServerResponse> archiveAgrupe(@PathVariable(value = "id") UUID id) {
    Optional<AgrupeModel> agrupeO = agrupeService.getAgrupeById(id);

    if (agrupeO.isPresent()) {
      AgrupeModel agrupe = agrupeO.get();

      agrupeService.changeAgrupeState(agrupe, true);
      var response = new ServerResponse("Agrupe arquivado com sucesso.", HttpStatus.OK.value());
      return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    throw new NotFoundException("Este agrupe não está cadastrado no sistema.");
  }

  @PutMapping("/{id}/restore")
  public ResponseEntity<ServerResponse> restoreAgrupe(@PathVariable(value = "id") UUID id) {
    Optional<AgrupeModel> agrupeO = agrupeService.getAgrupeById(id);

    if (agrupeO.isPresent()) {
      AgrupeModel agrupe = agrupeO.get();

      agrupeService.changeAgrupeState(agrupe, false);
      var response = new ServerResponse("Agrupe restaurado com sucesso.", HttpStatus.OK.value());
      return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    throw new NotFoundException("Este agrupe não está cadastrado no sistema.");
  }

  @PostMapping("/{idAgrupe}/persons/{idPerson}")
  public ResponseEntity<AgrupeResponseDTO> addAgrupePerson(
        @PathVariable UUID idAgrupe,
        @PathVariable UUID idPerson,
        @RequestParam String personOffice) {
    Optional<AgrupeModel> agrupeO = agrupeService.getAgrupeById(idAgrupe);

    if (agrupeO.isPresent()) {
      AgrupeModel agrupe = agrupeService.addAgrupePerson(agrupeO.get(), idPerson, PersonOfficeEnum.fromString(personOffice));

      return ResponseEntity.status(HttpStatus.OK).body(convertAgrupeDTO(agrupe));
    }
    throw new NotFoundException("Este agrupe não está cadastrado no sistema.");
  }

  @DeleteMapping("/{idAgrupe}/persons/{idPerson}")
  public ResponseEntity<AgrupeResponseDTO> removeAgrupePerson(
        @PathVariable UUID idAgrupe,
        @PathVariable UUID idPerson,
        @RequestParam String personOffice) {
    Optional<AgrupeModel> agrupeO = agrupeService.getAgrupeById(idAgrupe);

    if (agrupeO.isPresent()) {
      AgrupeModel agrupe = agrupeService.removeAgrupePerson(agrupeO.get(), idPerson, PersonOfficeEnum.fromString(personOffice));

      return ResponseEntity.status(HttpStatus.OK).body(convertAgrupeDTO(agrupe));
    }
    throw new NotFoundException("Este agrupe não está cadastrado no sistema.");
  }
}
