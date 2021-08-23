package com.Turbo.Lms.controller;

import com.Turbo.Lms.dto.UserDto;
import com.Turbo.Lms.service.UserService;
import com.Turbo.Lms.util.ControllerUtils;
import com.Turbo.Lms.validator.UserValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
    private final UserService userService;
    private final UserValidator userValidator;

    public RegistrationController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }
    @GetMapping()
    public String showRegistrationForm(Model model) {
        model.addAttribute("user",new UserDto());
        return "registration";
    }

    @PostMapping
    public String showRegistrationForm(@Valid @ModelAttribute("user") UserDto user, BindingResult bindingResult, Model model) {
        userValidator.validate(user,bindingResult);
        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErros(bindingResult);
            model.addAllAttributes(errorsMap);
            return "registration";
        }
        userService.registerUser(user);
        return "redirect:/login";
    }
}
