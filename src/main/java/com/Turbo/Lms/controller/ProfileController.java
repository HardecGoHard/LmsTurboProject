package com.Turbo.Lms.controller;


import com.Turbo.Lms.Exceptions.InternalServerError;
import com.Turbo.Lms.Exceptions.NotFoundException;
import com.Turbo.Lms.dto.UserDto;
import com.Turbo.Lms.service.AvatarStorageService;
import com.Turbo.Lms.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);
    private AvatarStorageService avatarStorageService;

    @Autowired
    public ProfileController(UserService userService, AvatarStorageService avatarStorageService) {
        this.userService = userService;
        this.avatarStorageService = avatarStorageService;
    }

    @GetMapping
    public String getUserProfile(Model model, Authentication auth) {
        model.addAttribute("user", userService.findUserByUsername(auth.getName()));
        return "profile";
    }

    @PostMapping("/avatar")
    public String updateAvatarImage(Authentication auth,
                                    @RequestParam("avatar") MultipartFile avatar) {
        logger.info("File name {}, file content type {}, file size {}", avatar.getOriginalFilename(), avatar.getContentType(), avatar.getSize());
        try {
            avatarStorageService.save(auth.getName(), avatar.getContentType(), avatar.getInputStream());
        } catch (Exception ex) {
            logger.info("", ex);
            throw new InternalServerError();
        }
        return "redirect:/profile";
    }
    @GetMapping("/avatar")
    @ResponseBody
    public ResponseEntity<byte[]> avatarImage(Authentication auth) {
        String contentType = avatarStorageService.getContentTypeByUser(auth.getName())
                .orElseThrow(() -> new NotFoundException("Аватар не найден"));
        byte[] data = avatarStorageService.getAvatarImageByUser(auth.getName())
                .orElseThrow(() -> new NotFoundException("Аватар не найден"));
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(data);
    }
}
