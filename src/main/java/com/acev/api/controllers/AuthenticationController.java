package com.acev.api.controllers;

import com.acev.api.dtos.authentication.AuthRequestDTO;
import com.acev.api.dtos.authentication.AuthResponseDTO;
import com.acev.api.exceptions.ServerResponse;
import com.acev.api.infra.security.TokenService;
import com.acev.api.models.UserModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth")
@RequestMapping("/api/authentication")
@RestController
public class AuthenticationController {
  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private TokenService tokenService;

  @PostMapping()
  public ResponseEntity<Object> login(@RequestBody @Valid AuthRequestDTO authDTO) {
    try {
      var usernamePassword = new UsernamePasswordAuthenticationToken(authDTO.email(), authDTO.password());
      var auth = authenticationManager.authenticate(usernamePassword);

      var token = tokenService.generateToken((UserModel) auth.getPrincipal());

      var response = new AuthResponseDTO("Usuário logado com sucesso.", token, HttpStatus.OK.value());
      return ResponseEntity.status(HttpStatus.OK).body(response);
    } catch (Exception e) {
      var response = new ServerResponse("Credenciais inválidas.", HttpStatus.BAD_REQUEST.value());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
  }
}
