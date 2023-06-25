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

    Customer findCustomerByCode(String code);

    Customer findCustomerByIban(String iban);
}
