package com.beverage.shopping.exception;

public class InsufficientStockException extends RuntimeException {
    private final String beverageName;
    private final int available;
    private final int requested;

    public InsufficientStockException(String beverageName, int available, int requested) {
        super(String.format("Not enough stock for %s. Available: %d, Requested: %d", 
              beverageName, available, requested));
        this.beverageName = beverageName;
        this.available = available;
        this.requested = requested;
    }

    public String getBeverageName() {
        return beverageName;
    }

    public int getAvailable() {
        return available;
    }

    public int getRequested() {
        return requested;
    }
}
