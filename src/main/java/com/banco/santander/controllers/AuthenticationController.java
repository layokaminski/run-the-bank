package com.banco.santander.controllers;

import com.banco.santander.configs.security.JWTProvider;
import com.banco.santander.dtos.auth.JwtDTO;
import com.banco.santander.dtos.auth.LoginDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final JWTProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<JwtDTO> authenticateUser(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.document(), loginDTO.password())
            );

            String jwt = jwtProvider.generateJWT(authentication);
            return ResponseEntity.status(HttpStatus.OK).body(new JwtDTO(jwt));
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JwtDTO("Authentication error: " + ex.getMessage()));
        }
    }
}
