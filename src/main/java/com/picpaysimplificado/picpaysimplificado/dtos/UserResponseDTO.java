package com.picpaysimplificado.picpaysimplificado.dtos;

import com.picpaysimplificado.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.picpaysimplificado.domain.user.UserType;

import java.math.BigDecimal;

public record UserResponseDTO(
        Long id,
        String firstName,
        String lastName,
        String document,
        BigDecimal balance,
        String email,
        UserType userType
) {
    // Construtor que aceita User
    public UserResponseDTO(User user) {
        this(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getDocument(),
                user.getBalance(),
                user.getEmail(),
                user.getUsertype()
        ); // Chama o construtor padrão do record
    }
}

