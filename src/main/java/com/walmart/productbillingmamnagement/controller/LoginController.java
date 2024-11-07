package com.walmart.productbillingmamnagement.controller;

import com.walmart.productbillingmamnagement.entity.Admin;
import com.walmart.productbillingmamnagement.entity.User;
import com.walmart.productbillingmamnagement.repository.AdminRepository;
import com.walmart.productbillingmamnagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    // Admin Login
    @GetMapping("/adminlogin")
    public String adminLogin() {
        return "adminlogin";
    }

    @PostMapping("/adminlogin")
    public String adminLoginSubmit(@RequestParam("username") String username,
                                   @RequestParam("password") String password, Model model) {
        Admin admin = adminRepository.findByUsernameAndPassword(username, password);
        if (admin != null) {
            return "redirect:/admin/categories";  // Redirect to the categories page for admin
        }
        model.addAttribute("error", "Invalid Credentials");
        return "adminlogin";  // Return to admin login page if credentials are invalid
    }

    // Admin Registration
    @GetMapping("/adminregister")
    public String adminRegister() {
        return "adminregister";  // Display registration page
    }

    @PostMapping("/adminregister")
    public String adminRegisterSubmit(@RequestParam("username") String username,
                                      @RequestParam("email") String email,
                                      @RequestParam("password") String password,
                                      @RequestParam("confirmPassword") String confirmPassword,
                                      Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match!");
            return "adminregister";
        }

        // Check if email already exists
        if (adminRepository.findByEmail(email) != null) {
            model.addAttribute("error", "Email already exists!");
            return "adminregister";
        }

        /*// Check if username already exists
        if (adminRepository.findByUsername(username) != null) {
            model.addAttribute("error", "Username already exists!");
            return "adminregister";
        }*/

        // Save new admin
        Admin newAdmin = new Admin();
        newAdmin.setUsername(username);
        newAdmin.setEmail(email);
        newAdmin.setPassword(password);
        adminRepository.save(newAdmin);

        return "redirect:/adminlogin";  // Redirect to admin login page after successful registration
    }

    // User Login
    @GetMapping("/userlogin")
    public String userLogin() {
        return "userlogin";
    }

    @PostMapping("/userlogin")
    public String userLoginSubmit(@RequestParam("username") String username,
                                  @RequestParam("password") String password, Model model) {
        User user = userRepository.findByUsernameAndPassword(username, password);
        if (user != null) {
            return "redirect:/user/categories";  // Redirect to the categories page for users
        }
        model.addAttribute("error", "Invalid Credentials");
        return "userlogin";
    }

    // User Registration
    @GetMapping("/userregister")
    public String userRegister() {
        return "userregister";  // Display user registration page
    }

    @PostMapping("/userregister")
    public String userRegisterSubmit(@RequestParam("username") String username,
                                     @RequestParam("email") String email,
                                     @RequestParam("password") String password,
                                     @RequestParam("confirmPassword") String confirmPassword,
                                     Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match!");
            return "userregister";
        }

        // Check if email already exists
        if (userRepository.findByEmail(email) != null) {
            model.addAttribute("error", "Email already exists!");
            return "userregister";
        }

        /*// Check if username already exists
        if (userRepository.findByUsername(username) != null) {
            model.addAttribute("error", "Username already exists!");
            return "userregister";
        }
*/
        // Save new user
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(password);
        userRepository.save(newUser);

        return "redirect:/userlogin";  // Redirect to user login page after successful registration
    }
}
