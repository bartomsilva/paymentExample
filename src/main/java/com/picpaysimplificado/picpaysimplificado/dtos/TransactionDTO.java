package com.picpaysimplificado.picpaysimplificado.dtos;

import com.picpaysimplificado.picpaysimplificado.domain.user.User;

import java.math.BigDecimal;

public record TransactionDTO(BigDecimal value, User senderId, User receiverId) {

}