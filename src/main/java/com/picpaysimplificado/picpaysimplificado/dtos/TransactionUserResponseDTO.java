package com.picpaysimplificado.picpaysimplificado.dtos;

import com.picpaysimplificado.picpaysimplificado.domain.user.User;

public record TransactionUserResponseDTO(
        Long id,
        String firstName,
        String lastName,
        String email
) {
    // Construtor que aceita User
    public TransactionUserResponseDTO(User user) {
        this(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }
}
