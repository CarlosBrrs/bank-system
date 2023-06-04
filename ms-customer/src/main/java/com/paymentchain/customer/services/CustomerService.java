package com.paymentchain.customer.services;

import com.paymentchain.customer.entities.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {

    Customer editCustomer(String uuid, Customer customer);

    Customer getCustomer(String uuid);


    List<Customer> getAllCustomers();

    Customer createCustomer(Customer customer);

    void deleteCustomer(String uuid);
}
