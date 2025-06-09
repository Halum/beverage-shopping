package com.beverage.shopping.service;

import com.beverage.shopping.model.*;
import com.beverage.shopping.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
@Profile("seed")
public class DatabaseSeederService {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseSeederService.class);

    private final BottleRepository bottleRepository;
    private final CrateRepository crateRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    @Autowired
    public DatabaseSeederService(
            BottleRepository bottleRepository,
            CrateRepository crateRepository,
            UserRepository userRepository,
            AddressRepository addressRepository) {
        this.bottleRepository = bottleRepository;
        this.crateRepository = crateRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    @Transactional
    public void seedDatabase() {
        logger.info("Starting database seeding...");
        
        if (bottleRepository.count() == 0 && crateRepository.count() == 0) {
            seedBeverages();
        }
        
        if (userRepository.count() == 0) {
            seedUsers();
        }
        
        logger.info("Database seeding completed successfully!");
    }

    private void seedBeverages() {
        logger.info("Seeding beverages...");
        
        // Create and save bottles
        List<Bottle> bottles = Arrays.asList(
            Bottle.builder()
                .name("Paulaner Hefe-Weissbier")
                .volume(0.5)
                .inStock(50)
                .price(2.99)
                .supplier("Paulaner Brauerei")
                .volumePercent(5.5)
                .pic("https://tinyurl.com/28o82rw7")
                .build(),
            
            Bottle.builder()
                .name("Augustiner Helles")
                .volume(0.5)
                .inStock(45)
                .price(2.79)
                .supplier("Augustiner-Brau")
                .volumePercent(5.2)
                .pic("https://tinyurl.com/23cg3eza")
                .build(),
            
            Bottle.builder()
                .name("Duvel Belgian Ale")
                .volume(0.33)
                .inStock(30)
                .price(3.49)
                .supplier("Duvel Moortgat")
                .volumePercent(8.5)
                .pic("https://tinyurl.com/242yjf8r")
                .build(),
            
            Bottle.builder()
                .name("San Pellegrino Sparkling")
                .volume(0.75)
                .inStock(60)
                .price(2.29)
                .supplier("San Pellegrino S.P.A.")
                .volumePercent(0.0)
                .pic("https://tinyurl.com/25etstt6")
                .build(),
            
            Bottle.builder()
                .name("Coca-Cola Classic")
                .volume(1.0)
                .inStock(100)
                .price(1.99)
                .supplier("Coca-Cola GmbH")
                .volumePercent(0.0)
                .pic("https://tinyurl.com/27s8rlzv")
                .build()
        );
        
        bottleRepository.saveAll(bottles);
        
        // Create and save crates
        List<Crate> crates = Arrays.asList(
            Crate.builder()
                .name("Paulaner Hefe-Weissbier Crate")
                .noOfBottles(20)
                .inStock(10)
                .price(38.99)
                .pic("https://tinyurl.com/2868bn8c")
                .build(),
            
            Crate.builder()
                .name("Augustiner Helles Crate")
                .noOfBottles(20)
                .inStock(8)
                .price(36.99)
                .pic("https://tinyurl.com/29rxu9r6")
                .build(),
            
            Crate.builder()
                .name("Coca-Cola Classic Crate")
                .noOfBottles(12)
                .inStock(15)
                .price(19.99)
                .pic("https://tinyurl.com/2b6jy6an")
                .build()
        );
        
        crateRepository.saveAll(crates);
    }

    private void seedUsers() {
        logger.info("Seeding users...");
        
        // Create address
        Address address = Address.builder()
            .street("Sample Street")
            .number("123")
            .postalCode("12345")
            .build();
        
        // Create user with address
        User user = User.builder()
            .username("admin")
            .password("admin123")
            .birthday(LocalDate.of(1990, 1, 1))  
            .build();
            
        user.addAddress(address);
        
        userRepository.save(user);
    }
}
