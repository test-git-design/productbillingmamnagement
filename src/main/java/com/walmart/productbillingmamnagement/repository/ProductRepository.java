package com.walmart.productbillingmamnagement.repository;

import com.walmart.productbillingmamnagement.entity.Category;
import com.walmart.productbillingmamnagement.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Category category);

}
