package com.acev.api.infra.security;

import com.acev.api.enums.RolesEnum;
import com.acev.api.models.UserModel;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TokenService {
  @Value("{api.security.token.secret}")
  private String secret;

  public String generateToken(UserModel user) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);

      List<String> userRoles = user.getRoles().stream()
            .map(RolesEnum::name)
            .collect(Collectors.toList());

      return JWT.create()
            .withIssuer("acev-api")
            .withSubject(user.getEmail())
            .withExpiresAt(generateExpirationDate())
            .withClaim("roles", userRoles)
            .withClaim("person", user.getPerson().getName())
            .withClaim("personId", user.getPerson().getIdPerson().toString())
            .sign(algorithm);
    } catch (JWTCreationException exception) {
      throw new RuntimeException("Error while generating token", exception);
    }
  }

  public String validateToken(String token) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);
      return JWT.require(algorithm)
            .withIssuer("acev-api")
            .build()
            .verify(token)
            .getSubject();
    } catch (JWTVerificationException exception) {
      return "";
    }
  }

  private Instant generateExpirationDate() {
    return LocalDateTime.now().plusHours(4).toInstant(ZoneOffset.of("-03:00"));
  }
}
