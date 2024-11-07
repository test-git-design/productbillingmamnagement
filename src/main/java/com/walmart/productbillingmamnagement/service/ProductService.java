package com.walmart.productbillingmamnagement.service;

import com.walmart.productbillingmamnagement.entity.Category;
import com.walmart.productbillingmamnagement.entity.Product;
import com.walmart.productbillingmamnagement.repository.CategoryRepository;
import com.walmart.productbillingmamnagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // Category Management Methods
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, Category updatedCategory) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id " + id));
        category.setName(updatedCategory.getName());
        // Add any additional fields that need to be updated
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id " + id));
        categoryRepository.delete(category);
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }


    // Product Management Methods
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id " + categoryId));
        return productRepository.findByCategory(category);
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + id));
        product.setName(updatedProduct.getName());
        product.setPrice(updatedProduct.getPrice());
        product.setQuantity(updatedProduct.getQuantity());
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + id));
        productRepository.delete(product);
    }

    public Optional<Product> getProductById(Long productId) {
        return productRepository.findById(productId);
    }


    public void saveProduct(Product product) {
        productRepository.save(product);
    }
}
