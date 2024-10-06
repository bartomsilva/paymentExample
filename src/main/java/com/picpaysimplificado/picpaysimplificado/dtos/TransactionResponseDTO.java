package com.picpaysimplificado.picpaysimplificado.dtos;

import com.picpaysimplificado.picpaysimplificado.domain.transaction.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponseDTO(
        Long id,
        BigDecimal amount,
        TransactionUserResponseDTO sender,
        TransactionUserResponseDTO receiver,
        LocalDateTime timestamp
) {
    // Construtor que mapeia a entidade Transaction para o DTO
    public TransactionResponseDTO(Transaction data) {
        this(
                data.getId(),
                data.getAmount(),
                new TransactionUserResponseDTO(data.getSender()), // Mapeando User para UserResponseDTO
                new TransactionUserResponseDTO(data.getReceiver()), // Mapeando User para UserResponseDTO
                data.getTimestamp()
        );
    }
}
