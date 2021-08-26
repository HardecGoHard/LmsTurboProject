package com.Turbo.Lms.controller;

import com.Turbo.Lms.domain.LessonCompletion;
import com.Turbo.Lms.dto.LessonDto;
import com.Turbo.Lms.dto.UserDto;
import com.Turbo.Lms.service.LessonCompletionService;
import com.Turbo.Lms.service.LessonService;
import com.Turbo.Lms.service.RoleType;
import com.Turbo.Lms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/lesson")
public class LessonController {

    private LessonService lessonService;
    private LessonCompletionService lessonCompletionService;
    private UserService userService;

    @Autowired
    public LessonController(LessonService lessonService, UserService userService, LessonCompletionService lessonCompletionService) {
        this.lessonService = lessonService;
        this.userService = userService;
        this.lessonCompletionService = lessonCompletionService;
    }

    @Secured(RoleType.ADMIN)
    @GetMapping("/new")
    public String lessonForm(Model model, @RequestParam("courseId") long id) {
        model.addAttribute("lesson", new LessonDto(id));
        return "lesson_form";
    }

    @Secured(RoleType.ADMIN)
    @PostMapping
    public String lessonSubmit(@Valid @ModelAttribute("lesson") LessonDto lessonDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "lesson_form";
        }
        lessonService.save(lessonDto);
        return "redirect:/course/" + lessonDto.getCourseId();
    }

    @Secured(RoleType.STUDENT)
    @PostMapping("/{lessonId}/complete")
    public String lessonCompletedForCurrentUser(@PathVariable("lessonId") long lessonId, HttpServletRequest request) {
        LessonDto lessonDto = lessonService.findById(lessonId);
        UserDto userDto = userService.findUserByUsername(request.getRemoteUser());
        if (!lessonCompletionService.isLessonAlreadyCompletedByUser(lessonId, userDto.getId())){
            LessonCompletion lessonCompletion = new LessonCompletion();
            lessonCompletion.setLessonId(lessonId);
            lessonCompletion.setUserId(userDto.getId());
            lessonCompletionService.save(lessonCompletion);
        }
        return "redirect:/course/" + lessonDto.getCourseId();
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping("/{id}")
    public String lessonForm(Model model, @PathVariable("id") Long id, HttpServletRequest request) {
        LessonDto lessonDto = lessonService.findById(id);
        UserDto userDto = userService.findUserByUsername(request.getRemoteUser());
        model.addAttribute("lesson", lessonDto);
        model.addAttribute("notEnrolled", !userService.isEnrolled(userDto.getId(), lessonDto.getCourseId()));
        return "lesson_form";
    }

    @Secured(RoleType.ADMIN)
    @DeleteMapping("/{id}")
    public String deleteLesson(@PathVariable("id") Long id) {
        Long courseId = lessonService.findById(id).getCourseId();
        lessonCompletionService.deleteByLessonId(id);
        lessonService.delete(id);
        return "redirect:/course/" + courseId;
    }

}
