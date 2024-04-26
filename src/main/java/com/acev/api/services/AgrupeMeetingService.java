package com.acev.api.services;

import com.acev.api.dtos.agrupeMeeting.AgrupeMeetingRequestDTO;
import com.acev.api.dtos.agrupeMeeting.AgrupeMeetingResponseDTO;
import com.acev.api.exceptions.NotFoundException;
import com.acev.api.models.AgrupeMeetingModel;
import com.acev.api.models.PersonModel;
import com.acev.api.repositories.AgrupeMeetingRepository;
import com.acev.api.util.classes.PagedResponse;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AgrupeMeetingService {
  @Autowired
  private AgrupeMeetingRepository agrupeMeetingRepository;

  @Autowired
  private AgrupeService agrupeService;

  @Autowired
  private PersonService personService;

  @Autowired
  private LessonService lessonService;

  public Optional<AgrupeMeetingModel> getAgrupeMeetingById(UUID id) {
    return agrupeMeetingRepository.findById(id);
  }

  public PagedResponse<AgrupeMeetingResponseDTO> getAllAgrupeMeetings(
        String idAgrupe,
        String idLesson,
        int page,
        int size
  ) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

    Specification<AgrupeMeetingModel> specification = (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (idAgrupe != null) {
        predicates.add(criteriaBuilder.equal(root.get("agrupe").get("id"), UUID.fromString(idAgrupe)));
      }

      if (idLesson != null) {
        predicates.add(criteriaBuilder.equal(root.get("lesson").get("id"), UUID.fromString(idLesson)));
      }

      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };

    Page<AgrupeMeetingModel> pageResult = agrupeMeetingRepository.findAll(specification, pageable);

    List<AgrupeMeetingResponseDTO> agrupeMeetings = pageResult.getContent().stream()
          .map(AgrupeMeetingResponseDTO::convertAgrupeMeetingDTO)
          .collect(Collectors.toList());

    return new PagedResponse<>(agrupeMeetings, pageResult.getTotalElements(), pageResult.getNumber(), pageResult.getTotalPages());
  }


  public AgrupeMeetingModel createAgrupeMeeting(AgrupeMeetingRequestDTO agrupeMeetingDTO) {
    AgrupeMeetingModel agrupeMeetingModel = new AgrupeMeetingModel();
    BeanUtils.copyProperties(agrupeMeetingDTO, agrupeMeetingModel);

    agrupeService.getAgrupeById(agrupeMeetingDTO.idAgrupe())
          .orElseThrow(() -> new NotFoundException("Este agrupe não está cadastrado no sistema."))
          .addAgrupeMeeting(agrupeMeetingModel);

    if (agrupeMeetingDTO.idLesson() != null) {
      lessonService.getLessonById(agrupeMeetingDTO.idLesson())
            .orElseThrow(() -> new NotFoundException("Esta lição não está cadastrada no sistema."))
            .addAgrupeMeeting(agrupeMeetingModel);
    }

    agrupeMeetingDTO.idParticipants().forEach(personId -> {
      PersonModel person = personService.getPersonById(personId)
            .orElseThrow(() -> new NotFoundException("Pessoa não encontrada com ID: " + personId));
      agrupeMeetingModel.addParticipant(person);
    });

    return agrupeMeetingRepository.save(agrupeMeetingModel);
  }

  public AgrupeMeetingModel updateAgrupeMeeting(AgrupeMeetingRequestDTO agrupeMeetingDTO,
                                                AgrupeMeetingModel agrupeMeeting) {
    // Atualizar membros
    agrupeMeeting.setParticipants(new ArrayList<>());
    agrupeMeetingDTO.idParticipants().forEach(personId -> {
      PersonModel person = personService.getPersonById(personId)
            .orElseThrow(() -> new NotFoundException("Pessoa não encontrada com ID: " + personId));
      agrupeMeeting.addParticipant(person);
    });
    BeanUtils.copyProperties(agrupeMeetingDTO, agrupeMeeting);
    return agrupeMeetingRepository.save(agrupeMeeting);
  }

  public void updateAgrupeMeeting(AgrupeMeetingModel agrupeMeeting) {
    agrupeMeetingRepository.save(agrupeMeeting);
  }

  public void deleteAgrupeMeeting(AgrupeMeetingModel agrupeMeeting) {
    agrupeMeetingRepository.delete(agrupeMeeting);
  }
}
