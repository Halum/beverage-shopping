package com.beverage.shopping.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@MappedSuperclass
public abstract class Beverage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @NotNull(message = "Name cannot be null")
    @NotEmpty(message = "Name cannot be empty")
    @Pattern(regexp = "^[a-zA-Z0-9\\s-]+$", message = "Name must contain only letters, numbers, spaces, and hyphens")
    private String name;

    @Positive(message = "Price must be positive")
    private Double price;

    @PositiveOrZero(message = "In stock must be greater than or equal to 0")
    private int inStock;

    private String pic;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Beverage)) return false;
        Beverage beverage = (Beverage) o;
        return id == beverage.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
