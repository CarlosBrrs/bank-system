package com.paymentchain.customer.services;

import com.paymentchain.customer.entities.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    Customer editCustomer(String uuid, Customer customer);

    Optional<Customer> getCustomer(String uuid);


    List<Customer> getAllCustomers();

    Customer createCustomer(Customer customer);

    void deleteCustomer(String uuid);

    Optional<Customer> findCustomerByCode(String code);

    Optional<Customer> findCustomerByIban(String iban);
}
