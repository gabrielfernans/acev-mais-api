package com.acev.api.controllers;

import com.acev.api.dtos.person.PersonRequestDTO;
import com.acev.api.dtos.person.PersonResponseDTO;
import com.acev.api.enums.EntryCategoryEnum;
import com.acev.api.enums.GenderEnum;
import com.acev.api.enums.MaritalStatusEnum;
import com.acev.api.enums.MemberTypeEnum;
import com.acev.api.exceptions.DuplicateEntryException;
import com.acev.api.exceptions.NotFoundException;
import com.acev.api.exceptions.ServerResponse;
import com.acev.api.models.PersonModel;
import com.acev.api.services.PersonService;
import com.acev.api.services.S3Service;
import com.acev.api.util.classes.PagedResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.acev.api.dtos.person.PersonResponseDTO.convertPersonDTO;


@Tag(name = "Persons")
@RequestMapping("/api/persons")
@RestController
public class PersonController {
  @Autowired
  PersonService personService;

  @Autowired
  S3Service s3Service;

  @PostMapping()
  public ResponseEntity<PersonResponseDTO> createPerson(@RequestBody @Valid PersonRequestDTO personDTO) {
    Optional<PersonModel> personO = personService.getPersonByEmail(personDTO.email());

    if (personO.isEmpty()) {
      PersonModel person = personService.createPerson(personDTO);

      return ResponseEntity.status(HttpStatus.CREATED).body(convertPersonDTO(person));
    }
    throw new DuplicateEntryException("Pessoa já está cadastrada no sistema.");
  }

  @PostMapping("/{id}/photo")
  public ResponseEntity<PersonResponseDTO> setPersonPhoto(@PathVariable(value = "id") UUID id,
                                                             @RequestParam("photo") MultipartFile photo) {
    Optional<PersonModel> personO = personService.getPersonById(id);

    if (personO.isPresent()) {
      PersonModel person = personO.get();

      // Exclui a foto antiga, se existir
      if (person.getPhotoUrl() != null) {
        String oldPhotoKey = s3Service.extractKeyFromUrl(person.getPhotoUrl().toString());
        s3Service.deleteFile(oldPhotoKey);
      }

      URL newPhotoUrl = s3Service.uploadFileToS3(photo);

      person.setPhotoUrl(newPhotoUrl); // Armazena o URL diretamente no modelo
      personService.updatePerson(person);

      return ResponseEntity.status(HttpStatus.OK).body(convertPersonDTO(person));
    }
    throw new NotFoundException("Pessoa não está cadastrada no sistema.");
  }

  @GetMapping("/{id}")
  public ResponseEntity<PersonResponseDTO> getPerson(@PathVariable(value = "id") UUID id) {
    Optional<PersonModel> personO = personService.getPersonById(id);

    if (personO.isPresent()) {
      PersonModel person = personO.get();

      return ResponseEntity.status(HttpStatus.OK).body(convertPersonDTO(person));
    }
    throw new NotFoundException("Pessoa não está cadastrada no sistema.");
  }

  @GetMapping()
  public ResponseEntity<PagedResponse<PersonResponseDTO>> getAllPersons(
        @RequestParam(required = false) String query,
        @RequestParam(required = false) MemberTypeEnum memberType,
        @RequestParam(required = false) EntryCategoryEnum entryCategory,
        @RequestParam(required = false) GenderEnum gender,
        @RequestParam(required = false) MaritalStatusEnum maritalStatus,
        @RequestParam(required = false) Boolean archived,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "ASC") Sort.Direction direction) {

    PagedResponse<PersonResponseDTO> response = personService.getAllPersons(query, memberType, entryCategory, gender,
          maritalStatus, archived, page, size, direction);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @GetMapping("/search")
  public ResponseEntity<List<PersonResponseDTO>> searchPersons(@RequestParam(required = false) String query) {
    List<PersonResponseDTO> persons = personService.searchPersons(query);

    return ResponseEntity.status(HttpStatus.OK).body(persons);
  }

  @PutMapping("/{id}")
  public ResponseEntity<PersonResponseDTO> updatePerson(@PathVariable(value = "id") UUID id,
                                                        @RequestBody @Valid PersonRequestDTO personDTO) {
    Optional<PersonModel> personO = personService.getPersonById(id);

    if (personO.isPresent()) {
      PersonModel person = personService.updatePerson(personDTO, personO.get());

      return ResponseEntity.status(HttpStatus.OK).body(convertPersonDTO(person));
    }
    throw new NotFoundException("Pessoa não está cadastrada no sistema.");
  }

  @PutMapping("/{id}/archive")
  public ResponseEntity<ServerResponse> archivePerson(@PathVariable(value = "id") UUID id) {
    Optional<PersonModel> personO = personService.getPersonById(id);

    if (personO.isPresent()) {
      PersonModel person = personO.get();

      personService.changePersonState(person, true);
      var response = new ServerResponse("Pessoa arquivada com sucesso.", HttpStatus.OK.value());

      return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    throw new NotFoundException("Pessoa não está cadastrada no sistema.");
  }

  @PutMapping("/{id}/restore")
  public ResponseEntity<ServerResponse> restorePerson(@PathVariable(value = "id") UUID id) {
    Optional<PersonModel> personO = personService.getPersonById(id);

    if (personO.isPresent()) {
      PersonModel person = personO.get();

      personService.changePersonState(person, false);
      var response = new ServerResponse("Pessoa restaurada com sucesso.", HttpStatus.OK.value());

      return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    throw new NotFoundException("Pessoa não está cadastrada no sistema.");
  }
}
