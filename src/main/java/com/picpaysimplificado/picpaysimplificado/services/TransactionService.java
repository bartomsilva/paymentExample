package com.picpaysimplificado.picpaysimplificado.services;

import com.picpaysimplificado.picpaysimplificado.domain.transaction.Transaction;
import com.picpaysimplificado.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.picpaysimplificado.dtos.TransactionDTO;
import com.picpaysimplificado.picpaysimplificado.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class TransactionService {

    @Autowired
    UserService userService;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    RestTemplate restTemplate;

    public void createTransaction(TransactionDTO transaction) throws Exception {
        User sender = this.userService.findUserById(transaction.senderId().getId());
        User receiver = this.userService.findUserById(transaction.receiverId().getId());
        this.userService.validTransction(sender, transaction.value());

        boolean isAuthorized = this.authorizeTransaction(sender, transaction.value());
        if(!isAuthorized){
            throw new Exception("Transação não autorizada.");
        }

    }

    private boolean authorizeTransaction(User sender, BigDecimal value) throws Exception {

        // requisição no link indicado
        ResponseEntity<Map> authorizationResponse = restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", Map.class);

        // extrair corpo da resposta
        Map responseBody = authorizationResponse.getBody();

        if(responseBody==null) {
            throw new Exception("o servidor não respondeu, tente novamente mais tarde.");
        }

        // extrair o status
        String status = (String) responseBody.get("status");

        // extrair data
        Map<String, Object> dataMap = (Map<String, Object>) responseBody.get("data");

        if(dataMap==null){
            throw new Exception("falha na transação.");
        }

        Boolean authorization = (Boolean) dataMap.get("authorization");

        return status.equals("success") && authorization;
    }
}
