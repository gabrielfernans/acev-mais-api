package com.acev.api.services;

import com.acev.api.dtos.user.UserRequestDTO;
import com.acev.api.dtos.user.UserResponseDTO;
import com.acev.api.models.PersonModel;
import com.acev.api.models.UserModel;
import com.acev.api.repositories.PersonRepository;
import com.acev.api.repositories.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PersonRepository personRepository;

  public Optional<UserDetails> getUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public Optional<UserModel> getUserById(UUID id) {
    return userRepository.findById(id);
  }

  public List<UserResponseDTO> getAllUsers() {
    List<UserModel> users = userRepository.findAll();

    return users.stream().map(this::convertUserDTO)
        .collect(Collectors.toList());
  }

  public UserModel createUser(UserRequestDTO userDTO) {
    var user = new UserModel();
    BeanUtils.copyProperties(userDTO, user);

    // Encrypt user password
    String encryptedPassword = new BCryptPasswordEncoder().encode(userDTO.password());
    user.setPassword(encryptedPassword);

    if (userDTO.idPerson() != null) {
      UUID idPerson = userDTO.idPerson();

      Optional<PersonModel> personO = personRepository.findById(idPerson);

      personO.ifPresent(user::setPerson);
    }
    return userRepository.save(user);
  }

  public UserModel updateUser(UserRequestDTO userDTO, UserModel user) {
    BeanUtils.copyProperties(userDTO, user);

    // Encrypt user password
    String encryptedPassword = new BCryptPasswordEncoder().encode(userDTO.password());
    user.setPassword(encryptedPassword);

    if (userDTO.idPerson() != null) {
      UUID idPerson = userDTO.idPerson();

      Optional<PersonModel> personO = personRepository.findById(idPerson);

      personO.ifPresent(user::setPerson);
    }
    return userRepository.save(user);
  }

  public void deleteUser(UserModel user) {
    userRepository.delete(user);
  }

  public UserResponseDTO convertUserDTO(UserModel user) {
    UUID idPerson = user.getPerson() != null ? user.getPerson().getIdPerson() : null;

    return new UserResponseDTO(
        user.getIdUser(),
        user.getEmail(),
        user.getRoles(),
        user.getCreatedAt(),
        user.getUpdatedAt(),
        idPerson);
  }
}
