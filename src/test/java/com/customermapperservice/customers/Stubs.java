package com.customermapperservice.customers;

import com.customermapperservice.customers.model.Customer;
import com.customermapperservice.customers.model.CustomerRequest;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;

@UtilityClass
public class Stubs {
    final static LocalDate aValidDate = LocalDate.now().minusDays(1);
    final static LocalDate aDateInTheFuture = LocalDate.now().plusDays(1);

    final static String aRequestWithInvalidDateFormat = "{\"createdAt\":\"2022/02/05\",\"customerId\":1}";

    final static String anExternalId = "85dfb9a2-caf0-4494-b35a-1aabf6c65703";

    final static Customer aCustomer = Customer.builder()
            .customerId(1)
            .externalId(anExternalId)
            .createdAt(aValidDate)
            .build();

    final static CustomerRequest aValidRequest = new CustomerRequest(1, aValidDate);
    final static CustomerRequest aValidRequestWithCustomerId2 = new CustomerRequest(2, aValidDate);
    final static CustomerRequest aRequestWithNegativeCustomerId = new CustomerRequest(-1, aValidDate);
    final static CustomerRequest aRequestWithFutureDate = new CustomerRequest(1, aDateInTheFuture);
    final static CustomerRequest aRequestWithNullDate = new CustomerRequest(1, null);
}
