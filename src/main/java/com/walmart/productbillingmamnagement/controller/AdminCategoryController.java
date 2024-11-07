package com.walmart.productbillingmamnagement.controller;

import com.walmart.productbillingmamnagement.entity.Category;
import com.walmart.productbillingmamnagement.entity.Product;
import com.walmart.productbillingmamnagement.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminCategoryController {

    @Autowired
    private ProductService productService;

    @GetMapping("/categories")
    public String showCategories(Model model) {
        List<Category> categories = productService.getAllCategories();
        model.addAttribute("categories", categories);
        return "admin-categories";  // Admin view for categories
    }

    @GetMapping("/categories/add")
    public String showAddCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "add-category";
    }

    @PostMapping("/categories/add")
    public String addCategory(@ModelAttribute("category") Category category) {
        productService.addCategory(category);
        return "redirect:/admin/categories";  // Redirect back to categories list
    }

    @GetMapping("/categories/edit/{id}")
    public String showEditCategoryForm(@PathVariable Long id, Model model) {
        Optional<Category> categoryOpt = productService.getCategoryById(id);
        if (categoryOpt.isPresent()) {
            model.addAttribute("category", categoryOpt.get());
            return "edit-category";
        } else {
            return "redirect:/admin/categories";  // Redirect if category not found
        }
    }

    @PostMapping("/categories/update/{id}")
    public String updateCategory(@PathVariable Long id, @ModelAttribute("category") Category category) {
        productService.updateCategory(id, category);
        return "redirect:/admin/categories";
    }

    @PostMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        // Delete all products under this category
        List<Product> products = productService.getProductsByCategory(id);
        products.forEach(product -> productService.deleteProduct(product.getId()));

        // Then delete the category itself
        productService.deleteCategory(id);

        return "redirect:/admin/categories";
    }


    // Managing products under a selected category
    @GetMapping("/categories/{id}/products")
    public String showProductsByCategory(@PathVariable Long id, Model model) {
        Optional<Category> categoryOpt = productService.getCategoryById(id);
        if (categoryOpt.isPresent()) {
            Category category = categoryOpt.get();
            List<Product> products = productService.getProductsByCategory(id);
            model.addAttribute("products", products);
            model.addAttribute("categoryId", id);
            model.addAttribute("categoryName", category.getName());  // Add the category name to the model
            return "admin-products";
        } else {
            return "redirect:/admin/categories";  // Redirect if category not found
        }
    }



    @GetMapping("/categories/{categoryId}/products/add")
    public String showAddProductForm(@PathVariable Long categoryId, Model model) {
        Optional<Category> categoryOpt = productService.getCategoryById(categoryId);
        if (categoryOpt.isPresent()) {
            Product product = new Product();
            product.setCategory(categoryOpt.get());
            model.addAttribute("product", product);
            return "add-product";
        } else {
            return "redirect:/admin/categories";  // Redirect if category not found
        }
    }

    @PostMapping("/categories/{categoryId}/products/add")
    public String addProductToCategory(@PathVariable Long categoryId, @ModelAttribute("product") Product product) {
        Optional<Category> categoryOpt = productService.getCategoryById(categoryId);
        if (categoryOpt.isPresent()) {
            product.setCategory(categoryOpt.get());
            productService.addProduct(product);
            return "redirect:/admin/categories/" + categoryId + "/products";
        } else {
            return "redirect:/admin/categories";  // Redirect if category not found
        }
    }


    @GetMapping("/products/edit/{id}")
    public String showEditProductForm(@PathVariable Long id, Model model) {
        Optional<Product> productOpt = productService.getProductById(id);
        if (productOpt.isPresent()) {
            model.addAttribute("product", productOpt.get());
            // Add all categories to the model for the dropdown
            List<Category> categories = productService.getAllCategories();
            model.addAttribute("categories", categories);
            return "edit-product";
        } else {
            return "redirect:/admin/categories";  // Redirect if product not found
        }
    }

    @PostMapping("/products/update/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute("product") Product product) {
        productService.updateProduct(id, product);
        Long categoryId = product.getCategory().getId();  // Fetch the category ID of the product
        return "redirect:/admin/categories/" + categoryId + "/products";  // Redirect to category-specific products page
    }



    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        Optional<Product> productOpt = productService.getProductById(id);
        if (productOpt.isPresent()) {
            Long categoryId = productOpt.get().getCategory().getId();
            productService.deleteProduct(id);
            return "redirect:/admin/categories/" + categoryId + "/products";
        } else {
            return "redirect:/admin/categories";  // Redirect if product not found
        }
    }
}
