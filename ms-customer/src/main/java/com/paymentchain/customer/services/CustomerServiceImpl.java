package com.paymentchain.customer.services;

import com.paymentchain.customer.entities.Customer;
import com.paymentchain.customer.repositories.CustomerRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Data
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Customer editCustomer(String uuid, Customer customer) {
        Optional<Customer> customerOptional = customerRepository.findById(UUID.fromString(uuid));
        if(customerOptional.isPresent()) {
            Customer dbCustomer = customerOptional.get();
            dbCustomer.setName(customer.getName());
            dbCustomer.setLastname(customer.getLastname());
            dbCustomer.setCode(customer.getCode());
            dbCustomer.setPhone(customer.getPhone());
            dbCustomer.setIban(customer.getIban());
            dbCustomer.setAddress(customer.getAddress());
            return customerRepository.save(dbCustomer);
        }
        return customer;
    }

    @Override
    public Optional<Customer> getCustomer(String uuid) {
        return customerRepository.findById(UUID.fromString(uuid));
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer createCustomer(Customer customer) {
        customer.getProductList().forEach(product -> product.setCustomer(customer));
        return customerRepository.save(customer);
    }

    public void deleteCustomer(String uuid) {
        Optional<Customer> customerOptional = customerRepository.findById(UUID.fromString(uuid));
        customerOptional.ifPresent(customerRepository::delete);
    }
    public Optional<Customer> findCustomerByCode(String code) {
        return customerRepository.findByCode(code);
    }

    public Optional<Customer> findCustomerByIban(String iban) {
        return customerRepository.findByIban(iban);
    }
}
