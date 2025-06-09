package com.beverage.shopping.service;

import java.util.Map;
import java.util.HashMap;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import com.beverage.shopping.model.Beverage;

@Service
@SessionScope
public class CartService {
    private Map<Beverage, Integer> cartItems;

    public CartService() {
        cartItems = new HashMap<>();
    }

    public void addToCart(Beverage beverage) {
        cartItems.put(beverage, cartItems.getOrDefault(beverage, 0) + 1);
    }

    public void removeFromCart(Beverage beverage) {
        cartItems.remove(beverage);
    }

    public void increaseQuantity(Beverage beverage) {
        cartItems.computeIfPresent(beverage, (k, v) -> v + 1);
    }

    public void decreaseQuantity(Beverage beverage) {
        cartItems.computeIfPresent(beverage, (k, v) -> {
            int newQuantity = v - 1;

            return newQuantity > 0 ? newQuantity : null;
        });
    }

    public int getTotalItems() {
        return cartItems.values().stream().mapToInt(Integer::intValue).sum();
    }

    public Map<Beverage, Integer> getCartItems() {
        return cartItems;
    }

    public void clearCart() {
        cartItems.clear();
    }

    public double getTotal() {
        return cartItems.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue())
                .sum();
    }
}
