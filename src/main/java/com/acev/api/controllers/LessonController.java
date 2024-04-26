package com.acev.api.controllers;

import com.acev.api.dtos.lesson.LessonRequestDTO;
import com.acev.api.dtos.lesson.LessonResponseDTO;
import com.acev.api.exceptions.NotFoundException;
import com.acev.api.exceptions.ServerResponse;
import com.acev.api.models.LessonModel;
import com.acev.api.services.LessonService;
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

@Tag(name = "Lessons")
@RequestMapping("/api/lessons")
@RestController
public class LessonController {
  @Autowired
  private LessonService lessonService;

  @Autowired
  private S3Service s3Service;

  @PostMapping()
  public ResponseEntity<LessonResponseDTO> createLesson(@RequestBody @Valid LessonRequestDTO lessonDTO) {
    LessonModel lesson = lessonService.createLesson(lessonDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(LessonResponseDTO.convertLessonDTO(lesson));
  }

  @PostMapping("/{id}/file")
  public ResponseEntity<LessonResponseDTO> setLessonFile(@PathVariable(value = "id") UUID id,
                                                         @RequestParam("file") MultipartFile file) {
    Optional<LessonModel> lessonO = lessonService.getLessonById(id);

    if (lessonO.isPresent()) {
      LessonModel lesson = lessonO.get();

      // Exclui a foto antiga, se existir
      if (lesson.getPdfUrl() != null) {
        String oldPhotoKey = s3Service.extractKeyFromUrl(lesson.getPdfUrl().toString());
        s3Service.deleteFile(oldPhotoKey);
      }

      URL newPhotoUrl = s3Service.uploadFileToS3(file);

      lesson.setPdfUrl(newPhotoUrl); // Armazena o URL diretamente no modelo
      lessonService.updateLesson(lesson);

      return ResponseEntity.status(HttpStatus.OK).body(LessonResponseDTO.convertLessonDTO(lesson));
    }
    throw new NotFoundException("Esta lição não está cadastrada no sistema.");
  }

  @GetMapping("/{id}")
  public ResponseEntity<LessonResponseDTO> getLesson(@PathVariable(value = "id") UUID id) {
    Optional<LessonModel> lessonO = lessonService.getLessonById(id);

    if (lessonO.isPresent()) {
      LessonModel lesson = lessonO.get();
      return ResponseEntity.status(HttpStatus.OK).body(LessonResponseDTO.convertLessonDTO(lesson));
    }
    throw new NotFoundException("Esta lição não está cadastrada no sistema.");
  }

  @GetMapping()
  public ResponseEntity<List<LessonResponseDTO>> getAllLessons() {
    return ResponseEntity.status(HttpStatus.OK).body(lessonService.getAllLessons());
  }

  @PutMapping("/{id}")
  public ResponseEntity<LessonResponseDTO> updateLesson(@PathVariable(value = "id") UUID id,
                                                        @RequestBody @Valid LessonRequestDTO lessonDTO) {
    Optional<LessonModel> lessonO = lessonService.getLessonById(id);

    if (lessonO.isPresent()) {
      LessonModel lesson = lessonService.updateLesson(lessonDTO, lessonO.get());
      return ResponseEntity.status(HttpStatus.OK).body(LessonResponseDTO.convertLessonDTO(lesson));
    }
    throw new NotFoundException("Esta lição não está cadastrada no sistema.");
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ServerResponse> deleteLesson(@PathVariable(value = "id") UUID id) {
    Optional<LessonModel> lessonO = lessonService.getLessonById(id);

    if (lessonO.isPresent()) {
      LessonModel lesson = lessonO.get();

      // Exclui o pdf do bucket, se existir
      if (lesson.getPdfUrl() != null) {
        String oldFileKey = s3Service.extractKeyFromUrl(lesson.getPdfUrl().toString());
        s3Service.deleteFile(oldFileKey);
      }

      lessonService.deleteLesson(lesson);
      var response = new ServerResponse("Liçao excluída com sucesso.", HttpStatus.OK.value());
      return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    throw new NotFoundException("Esta lição não está cadastrada no sistema.");
  }
}
