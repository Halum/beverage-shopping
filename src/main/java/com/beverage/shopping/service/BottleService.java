package com.beverage.shopping.service;

import com.beverage.shopping.model.Bottle;
import java.util.List;
import java.util.Optional;

public interface BottleService {
    List<Bottle> getAllBottles();
    Optional<Bottle> getBottleById(Long id);
    Bottle saveBottle(Bottle bottle);
    void deleteBottle(Long id);
    boolean updateBottleStock(Long id, int quantity);
    Bottle createBottle(String name, double volume, double volumePercent, 
                       double price, int inStock, String supplier, String pic);
}
