package com.Turbo.Lms.controller;

import com.Turbo.Lms.domain.Course;
import com.Turbo.Lms.dto.CourseDto;
import com.Turbo.Lms.dto.UserDto;
import com.Turbo.Lms.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/course")
public class CourseController {
    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);
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
    public String courseTable(Model model, @RequestParam(name = "titlePrefix", required = false) String titlePrefix,
                              HttpServletRequest request) {
        model.addAttribute("activePage", "courses");
        String title = titlePrefix == null ? "%" : titlePrefix + "%";
        //если роль пользователя админ, то передаем модели список всех курсов.
        if (request.isUserInRole("ROLE_ADMIN")) {
            model.addAttribute("courses", courseService.findByTitleLike(title));
            //если роль пользователя студент, то передаем модели список всех курсов на которые студент ещё не записан.
        } else {
            UserDto userDto = userService.findUserByUsername(request.getRemoteUser());
            model.addAttribute("courses", courseService.findCoursesNotAssignToUser(userDto.getId(), title));
            model.addAttribute("user", userDto);
        }
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

    @Secured("ROLE_ADMIN")
    @PostMapping
    public String submitCourseForm(@Valid Course course, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "form_course";
        }
        courseService.save(course);
        return "redirect:/course";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/new")
    public String courseForm(Model model) {
        model.addAttribute("course", new Course());
        return "form_course";
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    public String deleteCourse(@PathVariable("id") Long id) {
        courseService.delete(courseService.findById(id));
        return "redirect:/course";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/{id}/assign")
    public String userAssign(Model model, @PathVariable("id") Long id) {
        model.addAttribute("users", userService.findUsersNotAssignedToCourse(id));
        model.addAttribute("courseId", id);
        return "assign_user";
    }

    @PostMapping("/{courseId}/assign")
    public String assignUserForm(@PathVariable("courseId") Long courseId,
                                 @RequestParam("userId") Long userid, HttpServletRequest request) {
        userService.assignUserById(userid, courseId);
        if (request.isUserInRole("ROLE_STUDENT")) {
            return "redirect:/course";
        }
        return "redirect:/course/" + courseId + "/assign";
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{courseId}/unsign")
    public String userDelete(@PathVariable("courseId") Long courseId, @RequestParam("userId") Long userId) {
        userService.unassignUserById(userId, courseId);
        return "redirect:/course/" + courseId;
    }
}