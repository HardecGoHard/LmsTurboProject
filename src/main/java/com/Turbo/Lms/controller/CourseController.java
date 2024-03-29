package com.Turbo.Lms.controller;

import com.Turbo.Lms.Exceptions.InternalServerError;
import com.Turbo.Lms.Exceptions.NotFoundException;
import com.Turbo.Lms.domain.Course;
import com.Turbo.Lms.dto.*;
import com.Turbo.Lms.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/course")
public class CourseController {
    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);
    private final CourseService courseService;
    private final UserService userService;
    private final LessonService lessonService;
    private final CourseAvatarStorageService courseAvatarStorageService;
    private final LessonCompletionService lessonCompletionService;
    private final TaskService taskService;
    private final TaskCompletionService taskCompletionService;

    @Autowired
    public CourseController(CourseService courseService, UserService userService, LessonService lessonService,
                            CourseAvatarStorageService courseAvatarStorageService, LessonCompletionService lessonCompletionService,
                            TaskService taskService, TaskCompletionService taskCompletionService) {
        this.courseService = courseService;
        this.userService = userService;
        this.lessonService = lessonService;
        this.courseAvatarStorageService = courseAvatarStorageService;
        this.lessonCompletionService = lessonCompletionService;
        this.taskService = taskService;
        this.taskCompletionService = taskCompletionService;
    }

    @GetMapping()
    public String courseTable(Model model, @RequestParam(name = "titlePrefix", required = false) String titlePrefix,
                              @PageableDefault(sort = "id", size = 3) Pageable pageable,
                              HttpServletRequest request) {
        model.addAttribute("activePage", "courses");
        String title = titlePrefix == null ? "%" : titlePrefix + "%";
        //если роль пользователя админ, то передаем модели список всех курсов.
        Page<CourseDto> courseDtos;
        if (request.isUserInRole("ROLE_ADMIN")) {
            courseDtos = courseService.findByTitleLike(title, pageable);
            model.addAttribute("courses", courseDtos);
            //если роль пользователя студент, то передаем модели список всех курсов на которые студент ещё не записан.
        } else {
            UserDto userDto = userService.findUserByUsername(request.getRemoteUser());
            courseDtos = courseService.findCoursesNotAssignToUser(userDto.getId(), title,
                    pageable);
            model.addAttribute("courses", courseDtos);
            model.addAttribute("user", userDto);
        }
        int totalPages = courseDtos.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        Sort sort = pageable.getSort();
        if (sort.isSorted()) {
            Sort.Order order = sort.stream().filter(Objects::nonNull).findFirst()
                    .orElseThrow(() -> new NotFoundException("Что-то пошло не так"));
            model.addAttribute("sortField", order.getProperty());
            model.addAttribute("isAscending", order.isAscending());
            model.addAttribute("sortString",
                    String.format("%s,%s", order.getProperty(), order.isAscending() ? "asc" : "desc"));
        }
        return "find_course";
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping("/{courseId}")
    public String courseForm(Model model, @PathVariable("courseId") Long courseId, HttpServletRequest request) {
        CourseDto course = courseService.findById(courseId);
        UserDto userDto = userService.findUserByUsername(request.getRemoteUser());
        var lessons = lessonService.findAllForLessonIdWithoutText(courseId);
        boolean completed;
        List<TaskDto> tasks = taskService.findByCourseId(courseId);

        List<LessonDtoWithCompletion> lessonsWithCompletions = new ArrayList<>();
        for (var lesson : lessons) {
            completed = lessonCompletionService.isLessonAlreadyCompletedByUser(lesson.getId(), userDto.getId());
            lessonsWithCompletions.add(new LessonDtoWithCompletion(lesson, completed));
        }

        List<TaskDtoWithCompletion> taskDtoWithCompletions = new ArrayList<>();
        for (var task: tasks) {
            completed = taskCompletionService.isTaskAlreadyCompletedByUser(userDto.getId(), task.getId());
            taskDtoWithCompletions.add(new TaskDtoWithCompletion(task, completed));
        }

        model.addAttribute("tasks", taskDtoWithCompletions);
        model.addAttribute("course", course);
        model.addAttribute("lessons", lessonsWithCompletions);
        model.addAttribute("users", userService.getUsersOfCourse(courseId));
        model.addAttribute("user", userDto);
        if (userDto != null) {
            model.addAttribute("isEnrolled", userService.isEnrolled(userDto.getId(), courseId));
        }
        return "form_course";
    }

    @Secured(RoleType.ADMIN)
    @PostMapping
    public String submitCourseForm(@Valid @ModelAttribute("course") CourseDto course, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "form_course";
        }
        courseService.save(course);
        return "redirect:/course";
    }

    @Secured(RoleType.ADMIN)
    @GetMapping("/new")
    public String courseForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("newCourse", true);
        return "form_course";
    }

    @Secured(RoleType.ADMIN)
    @DeleteMapping("/{courseId}")
    public String deleteCourse(@PathVariable("courseId") Long id) {
        var lessonsList = lessonService.findAllForLessonIdWithoutText(id);
        for (var lesson : lessonsList) {
            lessonCompletionService.deleteByLessonId(lesson.getId());
        }
        courseService.delete(courseService.findById(id));
        return "redirect:/course";
    }

    @Secured(RoleType.ADMIN)
    @GetMapping("/{courseId}/assign")
    public String usersAssignForm(Model model, @PathVariable("courseId") Long id) {
        model.addAttribute("users", userService.findUsersNotAssignedToCourse(id));
        model.addAttribute("courseId", id);
        return "assign_user";
    }

    @PostMapping("/{courseId}/assign")
    public String assignUser(@PathVariable("courseId") Long courseId,
                             @RequestParam("userId") Long userId, HttpServletRequest request) {
        courseService.assignUserById(userId, courseId);
        if (request.isUserInRole("ROLE_STUDENT")) {
            return "redirect:/course";
        }
        return "redirect:/course/" + courseId + "/assign";
    }

    @Secured(RoleType.ADMIN)
    @DeleteMapping("/{courseId}/unsign")
    public String unassignUser(@PathVariable("courseId") Long courseId, @RequestParam("userId") Long userId) {
        courseService.unassignUserFromCourseById(userId, courseId);
        return "redirect:/course/" + courseId;
    }

    @PostMapping("/{courseId}/avatar")
    public String updateAvatarImage(@PathVariable("courseId") Long courseId,
                                    @RequestParam("avatar") MultipartFile avatar) {
        logger.info("File name {}, file content type {}, file size {}", avatar.getOriginalFilename(), avatar.getContentType(), avatar.getSize());
        try {
            courseAvatarStorageService.save(courseId, avatar.getContentType(), avatar.getInputStream());
        } catch (Exception ex) {
            logger.info("", ex);
            throw new InternalServerError();
        }
        return "redirect:/course/" + courseId;
    }

    @GetMapping("/{courseId}/avatar")
    @ResponseBody
    public ResponseEntity<byte[]> avatarImage(@PathVariable("courseId") Long courseId) {
        String contentType = courseAvatarStorageService.getContentTypeByEntity(courseId)
                .orElseThrow(() -> new NotFoundException("Аватар не найден"));
        byte[] data = courseAvatarStorageService.getAvatarImageByEntity(courseId)
                .orElseThrow(() -> new NotFoundException("Аватар не найден"));
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(data);
    }
}