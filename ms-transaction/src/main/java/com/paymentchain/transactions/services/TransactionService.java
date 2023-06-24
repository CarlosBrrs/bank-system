package com.paymentchain.transactions.services;

import com.paymentchain.transactions.entities.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionService {
    List<Transaction> findAllTransactions();

    Optional<Transaction> findTransactionById(long id);

    Optional<List<Transaction>> findTransactionsByIban(String ibanAccount);

    Transaction editTransaction(long id, Transaction transaction);

    Transaction createTransaction(Transaction transaction);

    void deleteCustomer(long id);
}
