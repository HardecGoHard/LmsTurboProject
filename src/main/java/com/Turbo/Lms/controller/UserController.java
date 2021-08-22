package com.Turbo.Lms.controller;

import com.Turbo.Lms.domain.Role;
import com.Turbo.Lms.dto.UserDto;
import com.Turbo.Lms.service.RoleService;
import com.Turbo.Lms.service.RoleType;
import com.Turbo.Lms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@Secured(RoleType.ADMIN)
@RequestMapping("/admin/user")
public class UserController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String userList(Model model, @RequestParam(name = "namePrefix", required = false) String namePrefix) {
        model.addAttribute("users", userService.findByUsernameLike(namePrefix == null ? "%" : namePrefix + "%"));
        model.addAttribute("activePage", "users");
        return "find_user";
    }

    @GetMapping("/{id}")
    public String userProfile(Model model, @PathVariable("id") Long id) {
        UserDto userDto = userService.findById(id);
        model.addAttribute("user", userDto);
        return "user_form";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("user", new UserDto());
        return "user_form";
    }
    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "registration";
    }
    @PostMapping
    public String submitUserForm(@Valid @ModelAttribute("user") UserDto user,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user_form";
        }
        userService.save(user);
        return "redirect:/admin/user";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.delete(userService.findById(id));
        return "redirect:/admin/user";
    }

    @ModelAttribute("roles")
    public List<Role> rolesAttribute() {
        return roleService.findAll();
    }

}
