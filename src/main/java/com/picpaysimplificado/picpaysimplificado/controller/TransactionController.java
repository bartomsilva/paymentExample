package com.picpaysimplificado.picpaysimplificado.controller;

import com.picpaysimplificado.picpaysimplificado.domain.transaction.Transaction;
import com.picpaysimplificado.picpaysimplificado.dtos.TransactionDTO;
import com.picpaysimplificado.picpaysimplificado.dtos.TransactionResponseDTO;
import com.picpaysimplificado.picpaysimplificado.dtos.UserResponseDTO;
import com.picpaysimplificado.picpaysimplificado.services.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(@RequestBody TransactionDTO data) throws Exception {

        // TO-DO 
        // referente aos dados do usuário retornar apenas os id's dos envovidos, data e valor da transação.
        TransactionResponseDTO newTransaction = this.transactionService.createTransaction(data);
        return new ResponseEntity<>(newTransaction, HttpStatus.CREATED);
    }


    @GetMapping
    public List<TransactionResponseDTO> getAllTransctions() {
        List<Transaction> transactionList = this.transactionService.getAllTransactions();
        return transactionList.stream()
                .map(TransactionResponseDTO::new)
                .collect(Collectors.toList());
    }
}
