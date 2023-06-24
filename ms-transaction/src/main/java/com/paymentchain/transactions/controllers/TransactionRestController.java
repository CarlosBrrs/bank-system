/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.paymentchain.transactions.controllers;

import com.paymentchain.transactions.entities.Transaction;
import com.paymentchain.transactions.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;

/**
 *
 * @author sotobotero
 */
@RestController
@RequestMapping("api/v1/transaction")
@RequiredArgsConstructor
public class TransactionRestController {

    private final TransactionService transactionService;
    @Autowired
    private final WebClient.Builder loadBalancedWebClientBuilder;
    @Value("${user.alias}")
    private String alias;

    @GetMapping()
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        System.out.println(alias);
        return ResponseEntity.ok(transactionService.findAllTransactions());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable long id) {
        Optional<Transaction> transactionOptional = transactionService.findTransactionById(id);
        return transactionOptional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/customer/transactions")
    public ResponseEntity<List<Transaction>> getTransactionsByIban(@RequestParam String ibanAccount) {
        Optional<List<Transaction>> transactionOptional = transactionService.findTransactionsByIban(ibanAccount);
        return transactionOptional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> editTransaction(@PathVariable long id, @RequestBody Transaction transaction) {
        return ResponseEntity.ok(transactionService.editTransaction(id, transaction));
    }
    
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.createTransaction(transaction));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Transaction> deleteTransaction(@PathVariable long id) {
        transactionService.deleteCustomer(id);
        return ResponseEntity.ok().build();
    }
}
