package com.acev.api.services;

import com.acev.api.dtos.lesson.LessonRequestDTO;
import com.acev.api.dtos.lesson.LessonResponseDTO;
import com.acev.api.exceptions.NotFoundException;
import com.acev.api.models.LessonModel;
import com.acev.api.models.SeriesModel;
import com.acev.api.repositories.LessonRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LessonService {
  @Autowired
  private LessonRepository lessonRepository;

  @Autowired
  SeriesService seriesService;

  public Optional<LessonModel> getLessonById(UUID id) {
    return lessonRepository.findById(id);
  }

  public List<LessonResponseDTO> getAllLessons() {
    List<LessonModel> lessons = lessonRepository.findAll();

    return lessons.stream().map(LessonResponseDTO::convertLessonDTO)
          .collect(Collectors.toList());
  }

  public LessonModel createLesson(LessonRequestDTO lessonDTO) {
    Optional<SeriesModel> seriesO = seriesService.getSeriesById(lessonDTO.idSeries());

    if (seriesO.isPresent()) {
      SeriesModel series = seriesO.get();
      LessonModel lesson = new LessonModel();
      BeanUtils.copyProperties(lessonDTO, lesson);
      series.addLesson(lesson);

      return lessonRepository.save(lesson);
    }
    throw new NotFoundException("Esta série de lições não está cadastrada no sistema.");
  }

  public LessonModel updateLesson(LessonRequestDTO lessonDTO, LessonModel lesson) {
    BeanUtils.copyProperties(lessonDTO, lesson);
    lesson.setUpdatedAt(LocalDateTime.now());
    return lessonRepository.save(lesson);
  }

  public void updateLesson(LessonModel lesson) {
    lessonRepository.save(lesson);
  }

  public void deleteLesson(LessonModel lesson) {
    Optional<SeriesModel> seriesO = seriesService.getSeriesById(lesson.getSeries().getIdSeries());

    if (seriesO.isPresent()) {
      SeriesModel series = seriesO.get();
      series.removeLesson(lesson);
      seriesService.updateSeries(series);
    }
    lessonRepository.delete(lesson);
  }
}
