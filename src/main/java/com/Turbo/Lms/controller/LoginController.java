package com.Turbo.Lms.controller;

import com.Turbo.Lms.dto.PasswordDto;
import com.Turbo.Lms.dto.UserDto;
import com.Turbo.Lms.service.EmailSenderService;
import com.Turbo.Lms.service.UserService;
import com.Turbo.Lms.util.ControllerUtils;
import com.Turbo.Lms.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/login")
public class LoginController {
    private final UserService userService;
    private final EmailSenderService emailSender;
    private final UserValidator userValidator;

    @Value("${app.host.name}")
    private String appHostName;

    @Autowired
    public LoginController(UserService userService, EmailSenderService emailSender, UserValidator userValidator) {
        this.userService = userService;
        this.emailSender = emailSender;
        this.userValidator = userValidator;
    }

    @GetMapping()
    public String getLoginForm() {
        return "login";
    }

    @GetMapping("/resetPassword")
    public String getResetPasswordForm() {
        return "reset_password";
    }

    @PostMapping("/resetPassword")
    public String ResetPassword(HttpServletRequest request, @RequestParam("email") String userEmail, Model model) {
        if (userEmail.isEmpty()) {
            model.addAttribute("emailError", "Поле email должно быть заполнено");
            return "reset_password";
        }
        UserDto user = userService.findUserByEmail(userEmail);
        if (user == null) {
            model.addAttribute("emailError", "Пользователь с таким email не найден");
            return "reset_password";
        }
        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);
        emailSender.sendEmail(userEmail,
                constructResetTokenEmail(appHostName, token, user),
                "Сброос пароля Turbo LMS");
        model.addAttribute("successSend", "Ссылка для подвереждения сброса пароля отправлена на ваш email");
        return "reset_password";
    }

    private String constructResetTokenEmail(String contextPath, String token, UserDto user) {
        String text = "Для сброса пароля перейдите по ссылке: \n";
        return text + contextPath + "/login/changePassword?token=" + token;
    }
    @GetMapping("/changePassword")
    public String showChangePasswordPage(@RequestParam("token") String token, Model model) {
        String result = userService.validatePasswordResetToken(token);
        if(result != null) {
            model.addAttribute("tokenError", "Ваш токен невалидный, повторите сброс пароля!");
            return "change_password";
        } else {
            PasswordDto passwordDto = new PasswordDto();
            passwordDto.setToken(token);
            model.addAttribute("password", passwordDto);
            return "change_password";
        }
    }
    @PostMapping("/changePassword")
    public String savePassword(@Valid @ModelAttribute("password") PasswordDto passwordDto, Model model) {
        if (!passwordDto.getNewPassword().equals(passwordDto.getConfirmNewPassword())){
            model.addAttribute("passwordError", "Пароли не совпадают");
            return "change_password";
        }
        String result = userService.validatePasswordResetToken(passwordDto.getToken());
        if(result != null) {
            model.addAttribute("tokenError", "Ваш токен невалидный, повторите сброс пароля!");
            return "change_password";
        }
        UserDto user = userService.findUserByPasswordResetToken(passwordDto.getToken());
        if(user != null) {
            userService.changeUserPassword(user.getId(), passwordDto.getNewPassword());
            return "redirect:/login";
        } else {
            model.addAttribute("tokenError", "Ваш токен невалидный, повторите сброс пароля!");
        }
        return "change_password";
    }
}
