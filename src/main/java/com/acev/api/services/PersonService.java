package com.acev.api.services;

import com.acev.api.dtos.address.AddressRequestDTO;
import com.acev.api.dtos.person.PersonRequestDTO;
import com.acev.api.dtos.person.PersonResponseDTO;
import com.acev.api.enums.EntryCategoryEnum;
import com.acev.api.enums.GenderEnum;
import com.acev.api.enums.MaritalStatusEnum;
import com.acev.api.enums.MemberTypeEnum;
import com.acev.api.models.AddressModel;
import com.acev.api.models.PersonModel;
import com.acev.api.repositories.PersonRepository;
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
public class PersonService {
  @Autowired
  private PersonRepository personRepository;

  @Autowired
  private S3Service s3Service;

  @Autowired
  private AddressService addressService;


  public Optional<PersonModel> getPersonByEmail(String email) {
    return personRepository.findByEmail(email);
  }

  public Optional<PersonModel> getPersonById(UUID id) {
    return personRepository.findById(id);
  }

  public PagedResponse<PersonResponseDTO> getAllPersons(String query,
                                                        MemberTypeEnum memberType,
                                                        EntryCategoryEnum entryCategory,
                                                        GenderEnum gender,
                                                        MaritalStatusEnum maritalStatus,
                                                        Boolean archived,
                                                        int page,
                                                        int size,
                                                        Sort.Direction direction) {

    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "name")); // Configura a paginação

    Specification<PersonModel> specification = (root, queryBuilder, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (query != null && !query.isEmpty()) {
        Predicate queryPredicate = criteriaBuilder.or(
              criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + query.toLowerCase() + "%"),
              criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + query.toLowerCase() + "%")
        );
        predicates.add(queryPredicate);
      }

      if (memberType != null) {
        predicates.add(criteriaBuilder.equal(root.get("memberType"), memberType));
      }

      if (entryCategory != null) {
        predicates.add(criteriaBuilder.equal(root.get("entryCategory"), entryCategory));
      }

      if (gender != null) {
        predicates.add(criteriaBuilder.equal(root.get("gender"), gender));
      }

      if (maritalStatus != null) {
        predicates.add(criteriaBuilder.equal(root.get("maritalStatus"), maritalStatus));
      }

      if (archived != null) {
        predicates.add(criteriaBuilder.equal(root.get("archived"), archived));
      }

      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };

    Page<PersonModel> pageResult = personRepository.findAll(specification, pageable);

    List<PersonResponseDTO> personResponseDTOs = pageResult.getContent().stream()
          .map(PersonResponseDTO::convertPersonDTO)
          .collect(Collectors.toList());

    long totalElements = personRepository.count(specification);
    int totalPages = (int) Math.ceil((double) totalElements / size);

    PagedResponse<PersonResponseDTO> response = new PagedResponse<>(personResponseDTOs, totalElements, pageResult.getNumber() + 1, totalPages);

    return response;
  }

  public List<PersonResponseDTO> searchPersons(String query) {
    Pageable pageable = PageRequest.of(0, 10); // Limita a 10 resultados

    List<PersonModel> persons = personRepository.findByNameContaining(query, pageable);

    return persons.stream()
          .map(PersonResponseDTO::convertPersonDTO)
          .collect(Collectors.toList());
  }

  public PersonModel createPerson(PersonRequestDTO personDTO) {
    var personModel = new PersonModel();
    AddressRequestDTO addressDTO = personDTO.address();

    if (addressDTO != null) {
      personModel.setAddress(addressService.createAddress(addressDTO));
    }

    BeanUtils.copyProperties(personDTO, personModel);
    return personRepository.save(personModel);
  }

  public PersonModel updatePerson(PersonRequestDTO personDTO, PersonModel person) {
    AddressRequestDTO addressDTO = personDTO.address();

    if (addressDTO != null) {
      AddressModel existingAddress = person.getAddress();

      if (existingAddress != null) {
        person.setAddress(addressService.updateAddress(addressDTO, existingAddress));
      } else {
        person.setAddress(addressService.createAddress(addressDTO));
      }
    }

    BeanUtils.copyProperties(personDTO, person);
    return personRepository.save(person);
  }

  public Long getCountByMemberType(MemberTypeEnum memberType) {
    return personRepository.countByMemberTypeAndArchivedIsFalse(memberType);
  }

  public Long getCountInAgrupesByMemberType(MemberTypeEnum memberType) {
    return personRepository.countByFrequentedAgrupeIsNotNullAndMemberTypeAndArchivedIsFalse(memberType);
  }

  public Long getCountInMinistries() {
    return personRepository.countByMemberOfMinistriesIsNotNullAndArchivedIsFalse();
  }

  public void updatePerson(PersonModel person) {
    personRepository.save(person);
  }

  public void changePersonState(PersonModel person, Boolean archived) {
    person.setArchived(archived);
    personRepository.save(person);
  }
}
