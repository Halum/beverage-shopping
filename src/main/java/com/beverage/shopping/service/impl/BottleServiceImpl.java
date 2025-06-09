package com.beverage.shopping.service.impl;

import com.beverage.shopping.model.Bottle;
import com.beverage.shopping.repository.BottleRepository;
import com.beverage.shopping.service.BottleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BottleServiceImpl implements BottleService {

    private final BottleRepository bottleRepository;

    @Autowired
    public BottleServiceImpl(BottleRepository bottleRepository) {
        this.bottleRepository = bottleRepository;
    }

    @Override
    public List<Bottle> getAllBottles() {
        return bottleRepository.findAll();
    }

    @Override
    public Optional<Bottle> getBottleById(Long id) {
        return bottleRepository.findById(id);
    }

    @Override
    public Bottle saveBottle(Bottle bottle) {
        return bottleRepository.save(bottle);
    }

    @Override
    public void deleteBottle(Long id) {
        bottleRepository.deleteById(id);
    }

    @Override
    @Transactional
    public boolean updateBottleStock(Long id, int quantity) {
        Optional<Bottle> bottleOpt = bottleRepository.findById(id);
        if (bottleOpt.isPresent()) {
            Bottle bottle = bottleOpt.get();
            bottle.setInStock(quantity);
            bottleRepository.save(bottle);
            return true;
        }
        return false;
    }

    @Override
    public Bottle createBottle(String name, double volume, double volumePercent, 
                             double price, int inStock, String supplier, String pic) {
        Bottle bottle = Bottle.builder()
            .name(name)
            .volume(volume)
            .volumePercent(volumePercent)
            .price(price)
            .inStock(inStock)
            .supplier(supplier)
            .pic(pic)
            .isAlcoholic(volumePercent > 0.0)
            .build();
        return saveBottle(bottle);
    }
}
