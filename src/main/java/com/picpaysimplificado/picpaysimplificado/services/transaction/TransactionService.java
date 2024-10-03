package com.picpaysimplificado.picpaysimplificado.services.transaction;

import com.picpaysimplificado.picpaysimplificado.domain.transaction.Transaction;
import com.picpaysimplificado.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.picpaysimplificado.dtos.TransactionDTO;
import com.picpaysimplificado.picpaysimplificado.infra.ServiceUnavailableException;
import com.picpaysimplificado.picpaysimplificado.repositories.TransactionRepository;
import com.picpaysimplificado.picpaysimplificado.services.AuthorizationService;
import com.picpaysimplificado.picpaysimplificado.services.notification.NotificationService;
import com.picpaysimplificado.picpaysimplificado.services.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

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

        try{
            this.saveTransaction(newTransaction, sender, receiver);
        } catch(Exception e){
            throw new ServiceUnavailableException("falha na transação");
        }

        // Enviar notificações
        try {
            this.notificationService.senderNotification(sender, "Transação realizada com sucesso.");
            this.notificationService.senderNotification(receiver, "Transação recebida com sucesso.");
        } catch (Exception e) {
            throw new ServiceUnavailableException("Transação concluída, mensagem de confirmação será enviada em breve.");
        }
        // retornar a transação
        return newTransaction;
    }

    @Transactional
    public void saveTransaction(Transaction newTransaction, User sender, User receiver) {
        // Salvar transação e atualizar saldo dos usuários
        this.repository.save(newTransaction);
        this.userService.saveUser(sender);
        this.userService.saveUser(receiver);
    }
}
