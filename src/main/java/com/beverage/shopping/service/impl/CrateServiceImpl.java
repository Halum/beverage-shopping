package com.beverage.shopping.service.impl;

import com.beverage.shopping.model.Crate;
import com.beverage.shopping.repository.CrateRepository;
import com.beverage.shopping.service.CrateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CrateServiceImpl implements CrateService {

    private final CrateRepository crateRepository;

    @Autowired
    public CrateServiceImpl(CrateRepository crateRepository) {
        this.crateRepository = crateRepository;
    }

    @Override
    public List<Crate> getAllCrates() {
        return crateRepository.findAll();
    }

    @Override
    public Optional<Crate> getCrateById(Long id) {
        return crateRepository.findById(id);
    }

    @Override
    public Crate saveCrate(Crate crate) {
        return crateRepository.save(crate);
    }

    @Override
    public void deleteCrate(Long id) {
        crateRepository.deleteById(id);
    }

    @Override
    @Transactional
    public boolean updateCrateStock(Long id, int quantity) {
        Optional<Crate> crateOpt = crateRepository.findById(id);
        if (crateOpt.isPresent()) {
            Crate crate = crateOpt.get();
            crate.setInStock(quantity);
            crateRepository.save(crate);
            return true;
        }
        return false;
    }

    @Override
    public Crate createCrate(String name, int noOfBottles, double price, 
                           int inStock, String pic) {
        Crate crate = Crate.builder()
            .name(name)
            .noOfBottles(noOfBottles)
            .price(price)
            .inStock(inStock)
            .pic(pic)
            .build();
        return saveCrate(crate);
    }
}
