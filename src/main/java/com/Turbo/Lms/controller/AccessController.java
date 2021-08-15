package com.Turbo.Lms.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AccessController {
    @GetMapping("/access_denied")
    public String accessDenied() {
        ModelAndView modelAndView = new ModelAndView("access_denied");
        modelAndView.addObject("message", "У вас недостаточно прав для доступа");
        modelAndView.setStatus(HttpStatus.FORBIDDEN);
        return "access_denied";
    }
}
