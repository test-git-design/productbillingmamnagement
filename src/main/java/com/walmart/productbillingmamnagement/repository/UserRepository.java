package com.walmart.productbillingmamnagement.repository;

import com.walmart.productbillingmamnagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameAndPassword(String username, String password);
    User findByUsername(String username);
    User findByEmail(String email);  // Add this method to check for email uniqueness
}