package com.exercise3.customermapperservice.customers;

import com.exercise3.customermapperservice.customers.model.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

import static com.exercise3.customermapperservice.customers.Stubs.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @InjectMocks
    CustomerService customerService;

    @Mock
    CustomerRepository customerRepository;

    @Test
    @DisplayName("Save customer should hit save in repository")
    void SaveCustomerShouldHitSaveInRepository() {
        customerService.saveCustomer(1, aValidDate);

        verify(customerRepository, times(1)).save(aCustomer);
    }

    @Test
    @DisplayName("Find by customerId should return a customer")
    void FindByCustomerIdShouldReturnCustomer() {
        Mockito.when(customerRepository.findById(aValidRequest.customerId())).thenReturn(Optional.of(aCustomer));

        final Optional<Customer> customer = customerService.findByCustomerId(aCustomer.customerId());

        Assertions.assertTrue(customer.isPresent(), "Find by customerId should return a customer");
        Assertions.assertEquals(aCustomer.customerId(), customer.get().customerId(), "CustomerId should be the same as the requested");
    }


    @Test
    @DisplayName("Find by customerId should return an empty object when customer is not present")
    void FindByCustomerIdShouldReturnAnEmptyObjWhenCustomerIsNotPresent() {
        Mockito.when(customerRepository.findById(aValidRequest.customerId())).thenReturn(Optional.empty());

        final Optional<Customer> customer = customerService.findByCustomerId(aCustomer.customerId());

        Assertions.assertTrue(customer.isEmpty(), "Find by customerId should return an empty object when customer is not present");
    }

}