package com.Turbo.Lms.controller;

import com.Turbo.Lms.domain.Course;
import com.Turbo.Lms.service.CourseLister;
import com.Turbo.Lms.service.CourseService;
import com.Turbo.Lms.service.StatisticsCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseLister courseLister;
    @Autowired
    private StatisticsCounter statisticsCounter;

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public String courseTable(Model model, @RequestParam(name = "titlePrefix", required = false) String titlePrefix) {
        model.addAttribute("activePage", "courses");
        model.addAttribute("courses", courseService.findByTitleWithPrefix(titlePrefix == null ? "" : titlePrefix));
        return "find_course";
    }

    @RequestMapping("/{id}")
    public String courseForm(Model model, @PathVariable("id") Long id) {
        model.addAttribute("course", courseService.findById(id).orElseThrow(NotFoundException::new));
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
        courseService.delete(id);
        return "redirect:/course";
    }
    @ExceptionHandler
    public ModelAndView notFoundExceptionHandler(NotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView("not_found");
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }

}