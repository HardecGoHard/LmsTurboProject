package com.Turbo.Lms.controller;

import com.Turbo.Lms.domain.Course;
import com.Turbo.Lms.domain.User;
import com.Turbo.Lms.dto.CourseDto;
import com.Turbo.Lms.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/course")
public class CourseController {
    private final CourseService courseService;
    private final UserService userService;
    private final LessonService lessonService;

    @Autowired
    public CourseController(CourseService courseService, UserService userService, LessonService lessonService) {
        this.courseService = courseService;
        this.userService = userService;
        this.lessonService = lessonService;
    }

    @GetMapping
    public String courseTable(Model model, @RequestParam(name = "titlePrefix", required = false) String titlePrefix) {
        model.addAttribute("activePage", "courses");
        model.addAttribute("courses", courseService.findByTitleLike(titlePrefix == null ? "%" : titlePrefix + "%"));
        return "find_course";
    }

    @RequestMapping("/{id}")
    public String courseForm(Model model, @PathVariable("id") Long id) {
        CourseDto course = courseService.findByIdAndConvertToDto(id);
        model.addAttribute("course", course);
        model.addAttribute("lessons", lessonService.findAllForLessonIdWithoutText(id));
        model.addAttribute("users", userService.getUsersOfCourse(course.getId()));
        return "form_course";
    }

    @PostMapping
    public String submitCourseForm(@Valid Course course, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "form_course";
        }
        courseService.save(course);
        return "redirect:/course";
    }

    @GetMapping("/new")
    public String courseForm(Model model) {
        model.addAttribute("course", new Course());
        return "form_course";
    }

    @DeleteMapping("/{id}")
    public String deleteCourse(@PathVariable("id") Long id) {
        courseService.delete(courseService.findById(id));
        return "redirect:/course";
    }

    @GetMapping("/{id}/assign")
    public String userAssign(Model model, @PathVariable("id") Long id) {
        model.addAttribute("users", userService.findUsersNotAssignedToCourse(id));
        model.addAttribute("courseId", id);
        return "assign_lesson";
    }

    @PostMapping("/{courseId}/assign")
    public String assignUserForm(@PathVariable("courseId") Long courseId,
                                 @RequestParam("userId") Long userid) {

        userService.assignUserById(userid,courseId);
        return "redirect:/course/" + courseId + "/assign";
    }

    @DeleteMapping("/{courseId}/unsign")
    public String userDelete(@PathVariable("courseId") Long courseId, @RequestParam("userId") Long userId) {
        userService.unassignUserById(userId,courseId);
        return "redirect:/course/" + courseId;
    }
}