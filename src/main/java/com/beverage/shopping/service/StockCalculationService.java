package com.beverage.shopping.service;

import com.beverage.shopping.model.Beverage;
import com.beverage.shopping.model.Bottle;
import com.beverage.shopping.model.Crate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StockCalculationService {
    
    @Autowired
    private CartService cartService;

    /**
     * Calculate the available stock for a beverage, taking into account items in cart
     *
     * @param beverage The beverage to calculate stock for
     * @return The available stock (physical stock minus items in cart)
     */
    public int calculateAvailableStock(Beverage beverage) {
        Map<Beverage, Integer> cartItems = cartService.getCartItems();
        int inCartQuantity = cartItems.getOrDefault(beverage, 0);
        return beverage.getInStock() - inCartQuantity;
    }

    /**
     * Adjust the stock levels of bottles based on cart contents
     *
     * @param bottles List of bottles to adjust
     * @return List of bottles with adjusted stock levels
     */
    public List<Bottle> adjustBottleStock(List<Bottle> bottles) {
        Map<Beverage, Integer> cartItems = cartService.getCartItems();
        
        return bottles.stream()
            .map(bottle -> {
                bottle.setInStock(calculateAvailableStock(bottle));
                
                return bottle;
            })
            .collect(Collectors.toList());
    }

    /**
     * Adjust the stock levels of crates based on cart contents
     *
     * @param crates List of crates to adjust
     * @return List of crates with adjusted stock levels
     */
    public List<Crate> adjustCrateStock(List<Crate> crates) {
        Map<Beverage, Integer> cartItems = cartService.getCartItems();
        
        return crates.stream()
            .map(crate -> {
                crate.setInStock(calculateAvailableStock(crate));
                
                return crate;
            })
            .collect(Collectors.toList());
    }
}
