package com.walmart.productbillingmamnagement.controller;

import com.walmart.productbillingmamnagement.entity.Category;
import com.walmart.productbillingmamnagement.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserCategoryController {

    @Autowired
    private ProductService productService;

    @GetMapping("/categories")
    public String showCategories(Model model) {
        List<Category> categories = productService.getAllCategories();
        model.addAttribute("categories", categories);
        return "categories";
    }

}
