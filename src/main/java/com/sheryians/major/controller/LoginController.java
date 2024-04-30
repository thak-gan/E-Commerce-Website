package com.sheryians.major.controller;

import com.sheryians.major.global.GlobalData;
import com.sheryians.major.model.Role;
import com.sheryians.major.model.User;
import com.sheryians.major.repository.RoleRepository;
import com.sheryians.major.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LoginController {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    @GetMapping("/login")
    public String login()
    {
        GlobalData.cart.clear();
        return "login";
    }

    @GetMapping("/register")
    public String registerGet()
    {
        return "register";
    }

    @PostMapping("/register")
    public String registerPost(HttpServletRequest request) throws ServletException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Set role ID to 2 for all users
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findById(2).orElseThrow(() -> new RuntimeException("Role not found")));

        User user = new User.Builder(firstName, email)
                .lastName(lastName)
                .password(bCryptPasswordEncoder.encode(password))
                .roles(roles)
                .build();

        userRepository.save(user);
        request.login(user.getEmail(), password);
        return "redirect:/";
    }


}