package com.acev.api.services;

import com.acev.api.dtos.address.AddressRequestDTO;
import com.acev.api.dtos.agrupe.AgrupeRequestDTO;
import com.acev.api.dtos.agrupe.AgrupeResponseDTO;
import com.acev.api.enums.PersonOfficeEnum;
import com.acev.api.exceptions.NotFoundException;
import com.acev.api.models.AddressModel;
import com.acev.api.models.AgrupeModel;
import com.acev.api.models.PersonModel;
import com.acev.api.repositories.AgrupeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AgrupeService {
  @Autowired
  private AgrupeRepository agrupeRepository;

  @Autowired
  private AddressService addressService;

  @Autowired
  private PersonService personService;

  public Optional<AgrupeModel> getAgrupeById(UUID id) {
    return agrupeRepository.findById(id);
  }

  public List<AgrupeResponseDTO> getAllAgrupes() {
    List<AgrupeModel> agrupes = agrupeRepository.findAll();

    return agrupes.stream().map(AgrupeResponseDTO::convertAgrupeDTO)
          .collect(Collectors.toList());
  }

  public AgrupeModel createAgrupe(AgrupeRequestDTO agrupeDTO) {
    var agrupeModel = new AgrupeModel();
    AddressRequestDTO addressDTO = agrupeDTO.address();

    if (addressDTO != null) {
      agrupeModel.setAddress(addressService.createAddress(addressDTO));
    }

    BeanUtils.copyProperties(agrupeDTO, agrupeModel);
    return agrupeRepository.save(agrupeModel);
  }

  public AgrupeModel updateAgrupe(AgrupeRequestDTO agrupeDTO, AgrupeModel agrupe) {
    AddressRequestDTO addressDTO = agrupeDTO.address();

    if (addressDTO != null) {
      AddressModel existingAddress = agrupe.getAddress();

      if (existingAddress != null) {
        agrupe.setAddress(addressService.updateAddress(addressDTO, existingAddress));
      } else {
        agrupe.setAddress(addressService.createAddress(addressDTO));
      }
    }

    BeanUtils.copyProperties(agrupeDTO, agrupe);
    return agrupeRepository.save(agrupe);
  }

  public AgrupeModel updateAgrupe(AgrupeModel agrupe) {
    return agrupeRepository.save(agrupe);
  }

  public void changeAgrupeState(AgrupeModel agrupe, Boolean archived) {
    agrupe.setArchived(archived);
    agrupeRepository.save(agrupe);
  }

  public AgrupeModel addAgrupePerson(AgrupeModel agrupe, UUID idPerson, PersonOfficeEnum personOffice) {
    PersonModel person = personService.getPersonById(idPerson)
          .orElseThrow(() -> new NotFoundException("Esta pessoa não está cadastrada no sistema."));

    if (!agrupe.getFrequenters().contains(person)) {
      agrupe.getFrequenters().add(person);
      person.setFrequentedAgrupe(agrupe);
    }

    if (personOffice == PersonOfficeEnum.APPRENTICE && !agrupe.getApprentices().contains(person)) {
      removeAgrupePerson(agrupe, idPerson, PersonOfficeEnum.LEADER);
      agrupe.getApprentices().add(person);
      person.setApprenticeshipAgrupe(agrupe);
    } else if (personOffice == PersonOfficeEnum.LEADER && !agrupe.getLeaders().contains(person)) {
      removeAgrupePerson(agrupe, idPerson, PersonOfficeEnum.APPRENTICE);
      agrupe.getLeaders().add(person);
      person.setLedAgrupe(agrupe);
    }

    return agrupeRepository.save(agrupe);
  }

  public AgrupeModel removeAgrupePerson(AgrupeModel agrupe, UUID idPerson, PersonOfficeEnum personOffice) {
    PersonModel person = personService.getPersonById(idPerson)
          .orElseThrow(() -> new NotFoundException("Esta pessoa não está cadastrada no sistema."));

    if (personOffice == PersonOfficeEnum.LEADER && agrupe.getLeaders().contains(person)) {
      agrupe.getLeaders().remove(person);
      person.setLedAgrupe(null);
    }

    if (personOffice == PersonOfficeEnum.APPRENTICE && agrupe.getApprentices().contains(person)) {
      agrupe.getApprentices().remove(person);
      person.setApprenticeshipAgrupe(null);
    }

    if (personOffice == PersonOfficeEnum.MEMBER) {
      if (agrupe.getFrequenters().contains(person)) {
        agrupe.getFrequenters().remove(person);
        person.setFrequentedAgrupe(null);
      }
      if (agrupe.getApprentices().contains(person)) {
        agrupe.getApprentices().remove(person);
        person.setApprenticeshipAgrupe(null);
      }
      if (agrupe.getLeaders().contains(person)) {
        agrupe.getLeaders().remove(person);
        person.setLedAgrupe(null);
      }
    }
    return agrupeRepository.save(agrupe);
  }
}
