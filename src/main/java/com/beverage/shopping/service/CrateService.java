package com.beverage.shopping.service;

import com.beverage.shopping.model.Crate;
import java.util.List;
import java.util.Optional;

public interface CrateService {
    List<Crate> getAllCrates();
    Optional<Crate> getCrateById(Long id);
    Crate saveCrate(Crate crate);
    void deleteCrate(Long id);
    boolean updateCrateStock(Long id, int quantity);
    Crate createCrate(String name, int noOfBottles, double price, int inStock, String pic);
}
