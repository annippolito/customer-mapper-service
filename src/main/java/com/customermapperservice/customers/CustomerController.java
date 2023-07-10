package com.customermapperservice.customers;

import com.customermapperservice.customers.model.Customer;
import com.customermapperservice.customers.model.CustomerRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import logstats.annotation.LogStats;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    //Autowired in constructor
    private final CustomerService customerService;

    @Operation(summary = "Get the customer externalId from a customerId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer found and externalId returned", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "404", description = "Customer not found for given customerId", content = @Content) })
    @GetMapping("/{customerId}")
    @LogStats
    public ResponseEntity<String> getExternalId(@PathVariable int customerId) {
        logger.info("getExternalId with customerId={}", customerId);
        Optional<Customer> customer = customerService.findByCustomerId(customerId);
        return customer
                .map(value -> new ResponseEntity<>(value.externalId(), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Save or update a customer with customerId and creation date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer saved or updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request (customerId should be a positive int and creation date should not be in the future")})
    @PostMapping()
    @LogStats
    public ResponseEntity<Void> saveCustomer(@Valid @RequestBody CustomerRequest customerRequest){
        logger.info("save customer with customerRequest={}", customerRequest);
        try {
            customerService.saveCustomer(customerRequest.customerId(), customerRequest.createdAt());
            return  new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
