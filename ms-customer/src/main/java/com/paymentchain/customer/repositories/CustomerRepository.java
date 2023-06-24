package com.paymentchain.customer.repositories;

import com.paymentchain.customer.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    @Query(value = "SELECT * FROM customer WHERE code = ?1 LIMIT 1", nativeQuery = true)
    Optional<Customer> findByCode(String code);

    @Query("SELECT c FROM Customer c WHERE c.iban=?1")
    Optional<Customer> findByIban(String iban);
}
