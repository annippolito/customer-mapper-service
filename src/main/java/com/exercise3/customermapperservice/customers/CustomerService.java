package com.exercise3.customermapperservice.customers;

import com.exercise3.customermapperservice.customers.model.Customer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
@Service
public class CustomerService {
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    //Autowired in constructor
    private final CustomerRepository customerRepository;

    public Optional<Customer> findByCustomerId(int customerId) {
        logger.info("find by CustomerId with customerId={}", customerId);
        return customerRepository.findById(customerId);
    }

    public void saveCustomer(int customerId, LocalDate createdAt) {
        logger.info("save Customer with customerId={} and createdAt={}", customerId, createdAt);
        customerRepository.save(Customer.builder()
                .customerId(customerId)
                .externalId(externalId())
                .createdAt(createdAt)
                .build());
    }

    private static String externalId(){
        return String.valueOf(UUID.randomUUID());
    }
}
