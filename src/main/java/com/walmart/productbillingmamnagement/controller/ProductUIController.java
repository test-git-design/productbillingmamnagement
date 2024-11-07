
package com.walmart.productbillingmamnagement.controller;

import com.walmart.productbillingmamnagement.entity.Category;
import com.walmart.productbillingmamnagement.entity.Product;
import com.walmart.productbillingmamnagement.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductUIController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public String getAllProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products";
    }

    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "add-product";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute("product") Product product) {
        productService.addProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable("id") Long id, Model model) {
        Product product = productService.getAllProducts().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
        model.addAttribute("product", product);
        return "edit-product";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute("product") Product product) {
        productService.updateProduct(id, product);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }

    // New '/buyproducts' for user purchases
    @GetMapping("/buyproducts")
    public String showBuyProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "buyProducts";
    }

    @PostMapping("/buyproducts")
    public String buyProduct(@RequestParam("productId") Long productId,
                             @RequestParam("quantity") int quantity, Model model) {
        Optional<Product> productOpt = productService.getProductById(productId);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            if (product.getQuantity() >= quantity) {
                product.setQuantity(product.getQuantity() - quantity); // Update product quantity
                productService.saveProduct(product);
                model.addAttribute("message", "Purchase successful!");
                return "redirect:/products/paymentSuccess";  // Redirect instead of directly returning the view
            } else {
                model.addAttribute("error", "Not enough stock available");
                return "/products/buyProducts";
            }
        }
        model.addAttribute("error", "Product not found");
        return "/products/buyProducts";
    }

    // Payment success page handler (if using redirect)
    @GetMapping("/paymentSuccess")
    public String paymentSuccess() {
        return "paymentSuccess";
    }

    @PostMapping("/checkout")
    public String checkout(@RequestParam("productIds") List<Long> productIds,
                           @RequestParam("quantities") List<Integer> quantities,
                           Model model) {
        double totalAmount = 0;
        Map<Product, Integer> productsPurchased = new HashMap<>();

        for (int i = 0; i < productIds.size(); i++) {
            Long productId = productIds.get(i);
            Integer quantity = quantities.get(i);

            if (quantity > 0) {
                Optional<Product> productOpt = productService.getProductById(productId);
                if (productOpt.isPresent()) {
                    Product product = productOpt.get();
                    if (product.getQuantity() >= quantity) {
                        product.setQuantity(product.getQuantity() - quantity); // Update product quantity
                        productService.saveProduct(product);
                        productsPurchased.put(product, quantity);
                        totalAmount += product.getPrice() * quantity;  // Calculate total amount
                    }
                }
            }
        }

        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("productsPurchased", productsPurchased);
        return "checkoutSummary";
    }

    @PostMapping("/processPurchase")
    public String processPurchase() {
        // You can add any additional logic if necessary before payment success
        // For now, simply redirect to the payment success page
        return "redirect:/products/paymentSuccess";
    }


    @GetMapping("/category/{id}")
    public String showProductsByCategory(@PathVariable Long id, Model model) {
        List<Product> products = productService.getProductsByCategory(id);
        System.out.println("Products retrieved: " + products); // Debug log
        model.addAttribute("products", products);
        return "buyproducts";
    }
}
