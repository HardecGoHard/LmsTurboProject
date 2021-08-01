package com.Turbo.Lms.controller;

import com.Turbo.Lms.domain.Role;
import com.Turbo.Lms.dto.UserDto;
import com.Turbo.Lms.service.UserAuthService;
import com.Turbo.Lms.service.UserService;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;
@Controller
@RequestMapping("/admin/user")
public class UserController {
    private final UserService userService;
    private final UserAuthService userAuthService;

    public UserController(UserService userService, UserAuthService userAuthService) {
        this.userService = userService;
        this.userAuthService = userAuthService;
    }
    @GetMapping
    public String userList(Model model,  @RequestParam(name = "namePrefix", required = false) String namePrefix){
        model.addAttribute("users",userService.findByUsernameLike(namePrefix == null ? "%" : namePrefix + "%"));
        return "find_user";
    }
    @GetMapping("/{id}")
    public String userProfile(Model model, @PathVariable("id") Long id) {
        UserDto userDto = userService.findById(id);
        model.addAttribute("user", userDto);
        model.addAttribute("roles", userService.rolesAttribute());
        return "user_form";
    }

    @PostMapping
    public String submitUserForm( @Valid @ModelAttribute("user") UserDto user,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user_form";
        }
        userService.save(user);
        return "redirect:/admin/user";
    }
}
