package com.acev.api.controllers;

import com.acev.api.dtos.user.UserRequestDTO;
import com.acev.api.dtos.user.UserResponseDTO;
import com.acev.api.exceptions.DuplicateEntryException;
import com.acev.api.exceptions.NotFoundException;
import com.acev.api.exceptions.ServerResponse;
import com.acev.api.models.UserModel;
import com.acev.api.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Tag(name = "Users")
@RequestMapping("/api/users")
@RestController
public class UserController {
  @Autowired
  UserService userService;

  @PostMapping()
  public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid UserRequestDTO userDTO) {
    Optional<UserDetails> userO = userService.getUserByEmail(userDTO.email());

    if (userO.isEmpty()) {
      UserModel user = userService.createUser(userDTO);
      return ResponseEntity.status(HttpStatus.CREATED).body(userService.convertUserDTO(user));
    }
    throw new DuplicateEntryException("Usuário já está cadastrado no sistema.");
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDTO> getUser(@PathVariable(value = "id") UUID id) {
    Optional<UserModel> userO = userService.getUserById(id);

    if (userO.isPresent()) {
      UserModel user = userO.get();
      return ResponseEntity.status(HttpStatus.OK).body(userService.convertUserDTO(user));
    }
    throw new NotFoundException("Usuário não está cadastrado no sistema.");
  }

  @GetMapping()
  public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
    return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
  }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable(value = "id") UUID id,
                                                      @RequestBody @Valid UserRequestDTO userDTO) {
      Optional<UserModel> userO = userService.getUserById(id);

      if (userO.isPresent()) {
        UserModel user = userService.updateUser(userDTO, userO.get());
        return ResponseEntity.status(HttpStatus.OK).body(userService.convertUserDTO(user));
      }
      throw new NotFoundException("Usuário não está cadastrado no sistema.");
    }

  @DeleteMapping("/{id}")
  public ResponseEntity<ServerResponse> deleteUser(@PathVariable(value = "id") UUID id) {
    Optional<UserModel> userO = userService.getUserById(id);

    if (userO.isPresent()) {
      userService.deleteUser(userO.get());
      var response = new ServerResponse("Usuário excluido com sucesso.", HttpStatus.OK.value());
      return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    throw new NotFoundException("Usuário não está cadastrado no sistema.");
  }
}
