package com.exercise3.customermapperservice.customers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static com.exercise3.customermapperservice.customers.Stubs.aValidRequest;
import static com.exercise3.customermapperservice.customers.Stubs.aValidRequestWithCustomerId2;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CustomersIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp(){
        customerRepository.deleteAll();
    }

    @Test
    @DisplayName("When post a customer it should be present in DB")
    void whenPostCustomerItShouldBePresentInDB() throws Exception {

        MockHttpServletRequestBuilder mockRequest =MockMvcRequestBuilders.post("/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aValidRequest));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());

        assertTrue(customerRepository.findById(1).isPresent(), "When post a customer it should be present in DB");
        Assertions.assertNotNull(customerRepository.findById(1).get().externalId(), "The customer should have a generated externalId");
    }

    @Test
    @DisplayName("Post customer and then retrieving his externalId")
    void postCustomerAndThenRetrievingHisExternalIdOK() throws Exception {

        MockHttpServletRequestBuilder postRequest = MockMvcRequestBuilders.post("/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aValidRequestWithCustomerId2));
        mockMvc.perform(postRequest).andExpect(status().isOk());

        assertTrue(customerRepository.findById(aValidRequestWithCustomerId2.customerId()).isPresent(), "When post a customer it should be present in DB");

        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/v1/customers/" + aValidRequestWithCustomerId2.customerId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(getRequest)
                .andExpect(content().string(customerRepository.findById(aValidRequestWithCustomerId2.customerId()).get().externalId()))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("After deleted a customer in Db I should not be able to retrieve it")
    void afterDeletedCustomerInDbIShouldNotBeAbleToRetrieveIt() throws Exception {

        MockHttpServletRequestBuilder postRequest = MockMvcRequestBuilders.post("/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aValidRequest));
        mockMvc.perform(postRequest).andExpect(status().isOk());

        customerRepository.deleteById(aValidRequest.customerId());

        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/v1/customers/" + aValidRequest.customerId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("After two post with the same customerId I should update the same customer with a new externalId")
    void afterTwoPostWithTheSameCustomerIdIShouldUpdateTheSameCustomerWithNewExternalId() throws Exception {

        MockHttpServletRequestBuilder postRequest = MockMvcRequestBuilders.post("/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aValidRequest));
        mockMvc.perform(postRequest).andExpect(status().isOk());

        assertTrue(customerRepository.findById(aValidRequest.customerId()).isPresent(), "When post a customer it should be present in DB");

        String previousExternalId = customerRepository.findById(aValidRequest.customerId()).get().externalId();
        mockMvc.perform(postRequest).andExpect(status().isOk());
        String newExternalId = customerRepository.findById(aValidRequest.customerId()).get().externalId();

        assertNotEquals(previousExternalId, newExternalId, "The new externalId should be changed");

    }

}
