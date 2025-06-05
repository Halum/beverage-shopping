package com.beverage.shopping.repository;

import com.beverage.shopping.model.Crate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrateRepository extends JpaRepository<Crate, Long> {
}
