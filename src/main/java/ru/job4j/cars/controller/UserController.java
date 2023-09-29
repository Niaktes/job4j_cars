package ru.job4j.cars.controller;

import java.util.Optional;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cars.model.User;
import ru.job4j.cars.service.UserService;

@Controller
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/register")
    public String getRegistrationPage() {
        return "users/register";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "users/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {
        Optional<User> savedUser = userService.save(user);
        if (savedUser.isEmpty()) {
            model.addAttribute("error", "Пользователь с таким email уже существует");
            return "users/register";
        }
        return "redirect:/users/login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute User user, Model model, HttpServletRequest request) {
        Optional<User> userOptional = userService.findByEmailAndPassword(
                user.getEmail(), user.getPassword()
        );
        if (userOptional.isEmpty()) {
            model.addAttribute("error", "Email или пароль введены не верно!");
            return "users/login";
        }
        HttpSession session = request.getSession();
        session.setAttribute("user", userOptional.get());
        return "redirect:/";
    }

}