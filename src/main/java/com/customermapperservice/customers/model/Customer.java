package com.customermapperservice.customers.model;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true) @Getter
@Entity
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Customer {
    @Id
    @EqualsAndHashCode.Include
    private int customerId;
    @NonNull
    private String externalId;
    @NonNull
    private LocalDate createdAt;
}

