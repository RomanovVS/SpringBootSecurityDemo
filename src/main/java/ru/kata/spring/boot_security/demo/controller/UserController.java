package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"/", "/index"})
    public String getUserList(Model model) {
        List<String> welcomeMessage = new ArrayList<>();
        welcomeMessage.add("Hello, this is welcome page");
        welcomeMessage.add("Here is the list of available users in the database");
        model.addAttribute("users", userService.showAllUsers());
        model.addAttribute("helloMessage", welcomeMessage);
        return "index";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("newUser", new User());
        return "new";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute("user") User user) {
        userService.saveUser(user);
        return "successPage";
    }


    @GetMapping("/user/{id}")
    public String showUser(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "user";
    }

    @PostMapping("/user/edit/{id}")
    public String update(@ModelAttribute("user") User user, @PathVariable("id") Long id ) {
        userService.editUser(id, user);
        return "successPage";
    }

    @GetMapping("/user/{id}/edit")
    public String editUser(Model model, @PathVariable("id") Long id) {
        model.addAttribute("user", userService.getUserById(id));
        return "edit";
    }

    @GetMapping("/user/{id}/delete")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "successPage";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "admin";
    }

    @GetMapping(value="/logout")
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }


}