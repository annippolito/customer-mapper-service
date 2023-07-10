package com.customermapperservice.customers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static com.customermapperservice.customers.Stubs.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    CustomerService customerService;


    @Test
    @DisplayName("Post customer with valid fields should return OK")
    void postCustomerWithValidFieldsShouldReturnOK() throws Exception {
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aValidRequest));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());

        verify(customerService, times(1)).saveCustomer(aValidRequest.customerId(), aValidRequest.createdAt());
    }

    @Test
    @DisplayName("Post customer with date format not valid should return a bad request")
    void postCustomerWithDateFormatNotValidShouldReturnBadRequest() throws Exception {
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(aRequestWithInvalidDateFormat);

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest());

        verify(customerService, times(0)).saveCustomer(anyInt(), any());

    }

    @Test
    @DisplayName("Post customer with date in the future should return a bad request")
    void postCustomerWithDateInTheFutureShouldReturnBadRequest() throws Exception {
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aRequestWithFutureDate));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest());

        verify(customerService, times(0)).saveCustomer(anyInt(), any());

    }

    @Test
    @DisplayName("Post customer with null date should return a bad request")
    void postCustomerWithNullDateShouldReturnBadRequest() throws Exception {
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aRequestWithNullDate));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest());

        verify(customerService, times(0)).saveCustomer(anyInt(), any());

    }

    @Test
    @DisplayName("Post customer with negative customerId should return a bad request")
    void postCustomerWithNegativeCustomerIdShouldReturnBadRequest() throws Exception {
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aRequestWithNegativeCustomerId));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest());

        verify(customerService, times(0)).saveCustomer(anyInt(), any());

    }

    @Test
    @DisplayName("Get externalId for a valid customer")
    void getExternalIdForValidCustomer() throws Exception {
        Mockito.when(customerService.findByCustomerId(aValidRequest.customerId())).thenReturn(Optional.of(aCustomer));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/v1/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(content().string(anExternalId));
    }

    @Test
    @DisplayName("Get externalId for a customer not found should return a Not found")
    void getExternalIdForCustomerNotFound() throws Exception {
        Mockito.when(customerService.findByCustomerId(aValidRequest.customerId())).thenReturn(Optional.empty());

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/v1/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("Get external id with a customerId not valid should return bad request")
    void getExternalIdWithCustomerIdNotValid() throws Exception {
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/v1/customers/notvalid")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest());

        verify(customerService, times(0)).saveCustomer(anyInt(), any());
    }
}