package com.paymentchain.customer.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class CustomerProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long productId;
    @Transient
    private String productName;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Customer.class)
    @JoinColumn(name = "customer_id", nullable = true)
    private Customer customer;

}
