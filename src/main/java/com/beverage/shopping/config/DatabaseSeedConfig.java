package com.beverage.shopping.config;

import com.beverage.shopping.service.DatabaseSeederService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("seed")
public class DatabaseSeedConfig {
    private final DatabaseSeederService seederService;

    public DatabaseSeedConfig(DatabaseSeederService seederService) {
        this.seederService = seederService;
    }

    @Bean
    CommandLineRunner initSeedDatabase() {
        return args -> seederService.seedDatabase();
    }
}
