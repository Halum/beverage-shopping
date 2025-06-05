package com.beverage.shopping.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "crate")
@SequenceGenerator(name = "crate_seq_gen", sequenceName = "crate_seq", initialValue = 200000, allocationSize = 50)
public class Crate extends Beverage {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "crate_seq_gen")
    private long id;

    @Positive(message = "Number of bottles must be greater than 0")
    private int noOfBottles;
}
