package com.Turbo.Lms.controller;


import com.Turbo.Lms.Exceptions.InternalServerError;
import com.Turbo.Lms.Exceptions.NotFoundException;
import com.Turbo.Lms.service.AbstractAvatarStorageService;
import com.Turbo.Lms.service.UserAvatarStorageService;
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

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);
    private  UserAvatarStorageService userAvatarStorageService;

    @Autowired
    public ProfileController(UserService userService, UserAvatarStorageService userAvatarStorageService) {
        this.userService = userService;
        this.userAvatarStorageService = userAvatarStorageService;
    }

    @GetMapping
    public String getUserProfile(Model model, Authentication auth) {
        model.addAttribute("user", userService.findUserByUsername(auth.getName()));
        return "profile";
    }

    @PostMapping("/avatar/{userId}")
    public String updateAvatarImage(@PathVariable("userId") Long userId,
                                    @RequestParam("avatar") MultipartFile avatar) {
        logger.info("File name {}, file content type {}, file size {}", avatar.getOriginalFilename(), avatar.getContentType(), avatar.getSize());
        try {
            userAvatarStorageService.save(userId, avatar.getContentType(), avatar.getInputStream());
        } catch (Exception ex) {
            logger.info("", ex);
            throw new InternalServerError();
        }
        return "redirect:/profile";
    }
    @GetMapping("/avatar/{userId}")
    @ResponseBody
    public ResponseEntity<byte[]> avatarImage(@PathVariable("userId") Long userId)  {
        String contentType = userAvatarStorageService.getContentTypeByEntity(userId)
                .orElseThrow(() -> new NotFoundException("Аватар не найден"));
        byte[] data = userAvatarStorageService.getAvatarImageByEntity(userId)
                .orElseThrow(() -> new NotFoundException("Аватар не найден"));
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(data);
    }
}
