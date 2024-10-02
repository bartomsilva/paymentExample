package com.picpaysimplificado.picpaysimplificado.services;

import com.picpaysimplificado.picpaysimplificado.domain.transaction.Transaction;
import com.picpaysimplificado.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.picpaysimplificado.dtos.TransactionDTO;
import com.picpaysimplificado.picpaysimplificado.infra.ServiceUnavailableException;
import com.picpaysimplificado.picpaysimplificado.infra.UnAuthorizedException;
import com.picpaysimplificado.picpaysimplificado.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TransactionService {

    @Autowired
    UserService userService;

    @Autowired
    TransactionRepository repository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    NotificationService notificationService;

    @Autowired
    AuthorizationService authorizationService;

    @Transactional
    public Transaction createTransaction(TransactionDTO transaction) throws Exception {

        User sender = this.userService.findUserById(transaction.senderId());
        User receiver = this.userService.findUserById(transaction.receiverId());
        this.userService.validTransaction(sender, transaction.value());
        this.authorizationService.authorizeTransaction(sender, transaction.value());

        // nova transação
        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transaction.value());
        newTransaction.setSender(sender);
        newTransaction.setReceiver(receiver);
        newTransaction.setTimestamp(LocalDateTime.now());

        // Atualizar saldos
        sender.setBalance(sender.getBalance().subtract(transaction.value()));
        receiver.setBalance(receiver.getBalance().add(transaction.value()));  // Ajuste aqui

        // Salvar transação e atualizar saldo dos usuários
        this.repository.save(newTransaction);
        this.userService.saveUser(sender);
        this.userService.saveUser(receiver);

        // Enviar notificações
        try {
            this.notificationService.senderNotification(sender, "Transação realizada com sucesso.");
            this.notificationService.senderNotification(receiver, "Transação recebida com sucesso.");
        } catch (Exception e) {
            throw new ServiceUnavailableException("Falha ao enviar notificações. Transação será revertida.");
        }
        // retornar a transação
        return newTransaction;
    }
}
