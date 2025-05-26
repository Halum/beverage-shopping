package com.beverage.shopping.repository;

import com.beverage.shopping.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    @EntityGraph(value = "User.withAddresses")
    List<User> findAll();
    
    @EntityGraph(value = "User.withAddresses")
    Optional<User> findById(Long id);
    
    @EntityGraph(value = "User.withAddresses")
    Optional<User> findByUsername(String username);
}
