package com.Turbo.Lms.controller;


import com.Turbo.Lms.Exceptions.InternalServerError;
import com.Turbo.Lms.Exceptions.NotFoundException;
import com.Turbo.Lms.dto.UserDto;
import com.Turbo.Lms.service.CourseService;
import com.Turbo.Lms.service.UserAvatarStorageService;
import com.Turbo.Lms.service.UserService;
import com.Turbo.Lms.util.ControllerUtils;
import com.Turbo.Lms.validator.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);
    private final CourseService courseService;
    private final UserService userService;
    private final UserValidator userValidator;
    private final UserAvatarStorageService userAvatarStorageService;

    @Autowired
    public ProfileController(UserService userService, UserValidator userValidator, UserAvatarStorageService userAvatarStorageService,
                             CourseService courseService) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.userAvatarStorageService = userAvatarStorageService;
        this.courseService = courseService;
    }

    @GetMapping
    public String getUserProfile(Model model, Authentication auth, @PageableDefault(sort = "id", size = 3) Pageable pageable) {
        UserDto userDto = userService.findUserByUsername(auth.getName());
        model.addAttribute("courses", courseService.findCoursesAssignedToUser(userDto.getId(), pageable));
        model.addAttribute("user", userDto);
        return "profile";
    }

    @PostMapping
    public String getUserProfile(@Valid @ModelAttribute("user") UserDto user,
                                 BindingResult bindingResult, Authentication auth, Model model) {
       /*
        Так как в профиле в целях безопасности нет поля для выбора роли и убрано hiiden поле id,
        то тянем из бд залогиненого пользователя и назначаем его роли и айди
        */
        UserDto userAuth = userService.findUserByUsername(auth.getName());
        user.setId(userAuth.getId());
        user.setRoles(userAuth.getRoles());
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErros(bindingResult);
            model.addAllAttributes(errorsMap);
            return "profile";
        }
        userService.save(user);
        return "redirect:/course";
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
    public ResponseEntity<byte[]> avatarImage(@PathVariable("userId") Long userId) {
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
