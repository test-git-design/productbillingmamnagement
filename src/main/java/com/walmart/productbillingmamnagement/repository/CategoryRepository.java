package com.walmart.productbillingmamnagement.repository;

import com.walmart.productbillingmamnagement.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
