package com.beverage.shopping.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "bottle")
@SequenceGenerator(name = "bottle_seq_gen", sequenceName = "bottle_seq", initialValue = 100000, allocationSize = 50)
public class Bottle extends Beverage {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bottle_seq_gen")
    private long id;

    @Positive(message = "Volume must be positive")
    private Double volume;

    private boolean isAlcoholic;

    @PositiveOrZero(message = "Volume percent must be non-negative")
    private Double volumePercent;

    @NotNull(message = "Supplier cannot be null")
    @NotEmpty(message = "Supplier cannot be empty")
    private String supplier;

    @PrePersist
    @PreUpdate
    public void updateAlcoholicStatus() {
        this.isAlcoholic = this.volumePercent > 0;
    }
}
