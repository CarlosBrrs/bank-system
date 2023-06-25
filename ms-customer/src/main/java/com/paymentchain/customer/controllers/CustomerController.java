package com.paymentchain.customer.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.paymentchain.customer.entities.Customer;
import com.paymentchain.customer.entities.CustomerProduct;
import com.paymentchain.customer.services.CustomerService;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    @Autowired
    private final WebClient.Builder loadBalancedWebClientBuilder;

    HttpClient httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .option(EpollChannelOption.TCP_KEEPIDLE, 300)
            .responseTimeout(Duration.ofSeconds(1))
            .doOnConnected(connection -> {
                connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
                connection.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));
            });

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> allCustomers = customerService.getAllCustomers();
        return (allCustomers == null || allCustomers.isEmpty()) ?
                ResponseEntity.noContent().build() : ResponseEntity.ok(allCustomers);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Customer> getCustomer(@PathVariable String uuid) {
        return customerService.getCustomer(uuid).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomer(customer));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Customer> editCustomer(@PathVariable String uuid, @RequestBody Customer customer) {
        return ResponseEntity.ok(customerService.editCustomer(uuid, customer));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String uuid) {
        customerService.deleteCustomer(uuid);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/full")
    public ResponseEntity<Customer> getCustomerByCode(@RequestParam String code) {
        Customer customerByCode = customerService.findCustomerByCode(code);
        List<CustomerProduct> productList = customerByCode.getProductList();
        productList.forEach(product -> {
            String productName = getProductNameFromProductMs(product.getProductId());
            product.setProductName(productName);
        });
        List<?> transactions = getTransactionsFromTransactionMs(customerByCode.getIban());
        customerByCode.setTransactionsList(transactions);
        return ResponseEntity.ok(customerByCode);
    }


    //TODO: Replace localhost with ms names
    private String getProductNameFromProductMs(long id) {
        WebClient webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("http://localhost:8082/api/v1/product")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8082/api/v1/product"))
                .build();
        JsonNode block = webClient
                .method(HttpMethod.GET).uri("/"+id)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
        assert block != null;
        return block.get("name").asText();
    }

    private  List<?> getTransactionsFromTransactionMs(String iban) {
        WebClient webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .baseUrl("http://localhost:8083/api/v1/transaction")
                .build();
        return webClient
                .method(HttpMethod.GET).uri(uriBuilder -> uriBuilder
                        .path("/customer/transactions")
                        .queryParam("ibanAccount", iban)
                        .build())
                .retrieve()
                .bodyToFlux(Object.class)
                .collectList()
                .block();
    }
}
