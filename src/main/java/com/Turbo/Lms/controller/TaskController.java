package com.Turbo.Lms.controller;

import com.Turbo.Lms.domain.Answer;
import com.Turbo.Lms.domain.Course;
import com.Turbo.Lms.domain.Role;
import com.Turbo.Lms.domain.Task;
import com.Turbo.Lms.dto.*;
import com.Turbo.Lms.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/task")
public class TaskController {
    private final TaskService taskService;
    private final AnswerService answerService;
    private final TaskCompletionService taskCompletionService;
    private final UserService userService;

    @Autowired
    public TaskController(TaskService taskService, CourseService courseService, AnswerService answerService,
                          TaskCompletionService taskCompletionService, UserService userService) {
        this.taskService = taskService;
        this.answerService = answerService;
        this.taskCompletionService = taskCompletionService;
        this.userService = userService;
    }

    @Secured(RoleType.ADMIN)
    @GetMapping("/new")
    public String createTask(Model model, @RequestParam("courseId") Long courseId) {
        TaskDto task = new TaskDto(courseId);
        List<AnswerDto> answerList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            answerList.add(new AnswerDto());
        }
        AnswerCreationDto answerCreationDto = new AnswerCreationDto(answerList);
        model.addAttribute("task", task);
        model.addAttribute("answers", answerCreationDto);
        return "create_task";
    }

    @Secured(RoleType.ADMIN)
    @PostMapping
    public String submitTaskForm(@Valid @ModelAttribute("task") TaskDto task, BindingResult bindingResult, @ModelAttribute("answers") AnswerCreationDto answersCreation,
                                  @ModelAttribute("answer") Long answer, Model model) {
        if (bindingResult.hasErrors()) {
            return "create_task";
        }

        List<AnswerDto> answers = answersCreation.getAnswerDtoList();
        Long savedTaskId = taskService.save(task);
        boolean correct = false;
        int step = 0;
        for (AnswerDto a : answers) {
            correct = (step == answer);
            a.setTaskId(savedTaskId);
            a.setCorrect(correct);
            answerService.save(a);
            step++;
        }

        task.setId(savedTaskId);
        taskService.save(task);

        return "redirect:/course/" + task.getCourseId();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{taskId}")
    public String taskForm(Model model, @PathVariable("taskId") Long taskId, HttpServletRequest request) {
        TaskDto taskDto = taskService.findTaskById(taskId);
        List<AnswerDto> answerList = answerService.findByTaskId(taskId);
        AnswerCreationDto answerCreationDto = new AnswerCreationDto(answerList);
        model.addAttribute("task", taskDto);
        model.addAttribute("answers", answerCreationDto);
        if (request.isUserInRole("ROLE_ADMIN"))
            return "create_task";
        else
        {
            UserDto user = userService.findUserByUsername(request.getRemoteUser());
            var record = taskCompletionService.findByUserAndTaskId(user.getId(), taskId);
            boolean isCompleted = record.isPresent() && record.get().isCompleted();
            boolean attemptsLimitExceeded = record.isPresent() && taskDto.getAttempts() == record.get().getAttemptsNumber();
            model.addAttribute("isCompleted", isCompleted);
            model.addAttribute("attemptsLimitExceeded", attemptsLimitExceeded);
            model.addAttribute("attemptsRemained", taskDto.getAttempts() - (record.isPresent() ? record.get().getAttemptsNumber() : 0));
            model.addAttribute("NotEnrolled", !userService.isEnrolled(user.getId(), taskDto.getCourseId()));
            return "form_task";
        }
    }


    @Secured(RoleType.ADMIN)
    @DeleteMapping("/{taskId}")
    public String deleteTask( @PathVariable("taskId") Long taskId, @RequestParam("courseId") Long courseId) {
        taskService.delete(taskId);
        return "redirect:/course/" + courseId;
    }

    @Secured(RoleType.STUDENT)
    @PostMapping("/{taskId}/complete")
    public String submitTaskCompletion(@PathVariable("taskId") Long taskId, @RequestParam("chosen_answer") Long chosen_answer,
                                       HttpServletRequest request) {
        TaskDto taskDto = taskService.findTaskById(taskId);
        UserDto user = userService.findUserByUsername(request.getRemoteUser());
        List<AnswerDto> answerDtoList = answerService.findByTaskId(taskId);
        boolean correctAnswer = answerDtoList.get(chosen_answer.intValue()).isCorrect();
        var record = taskCompletionService.findByUserAndTaskId(user.getId(), taskId);
        TaskCompletionDto temp;
        temp = record.orElseGet(() -> new TaskCompletionDto(user.getId(), taskId, 0, false));
        if (correctAnswer) {
            temp.setCompleted(true);
        }
        else
            temp.setAttemptsNumber(temp.getAttemptsNumber() + 1);
        taskCompletionService.save(temp);
        return "redirect:/course/" + taskDto.getCourseId();
    }
}
