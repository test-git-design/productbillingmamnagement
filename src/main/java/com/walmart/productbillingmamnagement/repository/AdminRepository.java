package com.walmart.productbillingmamnagement.repository;

import com.walmart.productbillingmamnagement.entity.Admin;
import com.walmart.productbillingmamnagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByUsernameAndPassword(String username, String password);
    Admin findByUsername(String username);
    Admin findByEmail(String email);  // Add this method to check for email uniqueness
}