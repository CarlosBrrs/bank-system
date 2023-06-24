package com.paymentchain.transactions.services;

import com.paymentchain.transactions.entities.Transaction;
import com.paymentchain.transactions.respository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;


    @Override
    public List<Transaction> findAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public Optional<Transaction> findTransactionById(long id) {
        return transactionRepository.findById(id);
    }

    @Override
    public Optional<List<Transaction>> findTransactionsByIban(String ibanAccount) {
        return Optional.ofNullable(transactionRepository.findByIbanAccount(ibanAccount));
    }

    @Override
    public Transaction editTransaction(long id, Transaction transaction) {
        Optional<Transaction> transactionOptional = transactionRepository.findById(id);
        if (transactionOptional.isPresent()) {
            Transaction dbTransaction = transactionOptional.get();
            dbTransaction.setAmount(transaction.getAmount());
            dbTransaction.setChannel(transaction.getChannel());
            dbTransaction.setDate(transaction.getDate());
            dbTransaction.setDescription(transaction.getDescription());
            dbTransaction.setFee(transaction.getFee());
            dbTransaction.setIbanAccount(transaction.getIbanAccount());
            dbTransaction.setReference(transaction.getReference());
            dbTransaction.setStatus(transaction.getStatus());
            return transactionRepository.save(dbTransaction);
        }
        return transaction;
    }

    @Override
    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public void deleteCustomer(long id) {
        Optional<Transaction> transactionOptional = transactionRepository.findById(id);
        transactionOptional.ifPresent(transactionRepository::delete);
    }
}
