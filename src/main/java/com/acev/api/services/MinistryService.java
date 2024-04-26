package com.acev.api.services;

import com.acev.api.dtos.ministry.MinistryRequestDTO;
import com.acev.api.dtos.ministry.MinistryResponseDTO;
import com.acev.api.enums.PersonOfficeEnum;
import com.acev.api.exceptions.NotFoundException;
import com.acev.api.models.MinistryModel;
import com.acev.api.models.PersonModel;
import com.acev.api.repositories.MinistryRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MinistryService {
  @Autowired
  private MinistryRepository ministryRepository;

  @Autowired
  private PersonService personService;

  public Optional<MinistryModel> getMinistryById(UUID id) {
    return ministryRepository.findById(id);
  }

  public List<MinistryResponseDTO> getAllMinistries() {
    List<MinistryModel> ministries = ministryRepository.findAll();

    return ministries.stream().map(MinistryResponseDTO::convertMinistryDTO)
        .collect(Collectors.toList());
  }

  public MinistryModel createMinistry(MinistryRequestDTO ministryDTO) {
    var ministryModel = new MinistryModel();
    BeanUtils.copyProperties(ministryDTO, ministryModel);
    return ministryRepository.save(ministryModel);
  }

  public MinistryModel updateMinistry(MinistryRequestDTO ministryDTO, MinistryModel ministry) {
    BeanUtils.copyProperties(ministryDTO, ministry);
    return ministryRepository.save(ministry);
  }

  public MinistryModel updateMinistry(MinistryModel ministry) {
    return ministryRepository.save(ministry);
  }

  public void changeMinistryState(MinistryModel ministry, Boolean archived) {
    ministry.setArchived(archived);
    ministryRepository.save(ministry);
  }

  public MinistryModel addMinistryPerson(MinistryModel ministry, UUID idPerson, PersonOfficeEnum personOffice) {
    PersonModel person = personService.getPersonById(idPerson)
          .orElseThrow(() -> new NotFoundException("Esta pessoa não está cadastrada no sistema."));

    if (!ministry.getMembers().contains(person)) {
      ministry.getMembers().add(person);
      person.getMemberOfMinistries().add(ministry);
    }

    if (personOffice == PersonOfficeEnum.APPRENTICE && !ministry.getApprentices().contains(person)) {
      removeMinistryPerson(ministry, idPerson, PersonOfficeEnum.LEADER);
      ministry.getApprentices().add(person);
      person.getApprenticeOfMinistries().add(ministry);
    } else if (personOffice == PersonOfficeEnum.LEADER && !ministry.getLeaders().contains(person)) {
      removeMinistryPerson(ministry, idPerson, PersonOfficeEnum.APPRENTICE);
      ministry.getLeaders().add(person);
      person.getLeaderOfMinistries().add(ministry);
    }

    return ministryRepository.save(ministry);
  }

  public MinistryModel removeMinistryPerson(MinistryModel ministry, UUID idPerson, PersonOfficeEnum personOffice) {
    PersonModel person = personService.getPersonById(idPerson)
          .orElseThrow(() -> new NotFoundException("Esta pessoa não está cadastrada no sistema."));

    if (personOffice == PersonOfficeEnum.LEADER && ministry.getLeaders().contains(person)) {
      ministry.getLeaders().remove(person);
      person.getLeaderOfMinistries().remove(ministry);
    }

    if (personOffice == PersonOfficeEnum.APPRENTICE && ministry.getApprentices().contains(person)) {
      ministry.getApprentices().remove(person);
      person.getApprenticeOfMinistries().remove(ministry);
    }

    if (personOffice == PersonOfficeEnum.MEMBER) {
      if (ministry.getMembers().contains(person)) {
        ministry.getMembers().remove(person);
        person.getMemberOfMinistries().remove(ministry);
      }
      if (ministry.getApprentices().contains(person)) {
        ministry.getApprentices().remove(person);
        person.getApprenticeOfMinistries().remove(ministry);
      }
      if (ministry.getLeaders().contains(person)) {
        ministry.getLeaders().remove(person);
        person.getLeaderOfMinistries().remove(ministry);
      }
    }
    return ministryRepository.save(ministry);
  }
}
