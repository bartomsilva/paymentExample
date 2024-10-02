package com.picpaysimplificado.picpaysimplificado.controller;

import com.picpaysimplificado.picpaysimplificado.domain.transaction.Transaction;
import com.picpaysimplificado.picpaysimplificado.dtos.TransactionDTO;
import com.picpaysimplificado.picpaysimplificado.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transf")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody TransactionDTO data) throws Exception {

        // TO-DO 
        // referente aos dados do usuário retornar apenas os id's dos envovidos, data e valor da transação.
        Transaction newTransaction = this.transactionService.createTransaction(data);
        return new ResponseEntity<>(newTransaction, HttpStatus.CREATED);
   }
//
//    @GetMapping
//    public List<Transaction> getAllTransctions(){
//        return this.transactionService.
//    }


}
