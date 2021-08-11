package com.Turbo.Lms.service;

import com.Turbo.Lms.Exceptions.NotFoundException;
import com.Turbo.Lms.dao.CourseRepository;
import com.Turbo.Lms.dao.LessonRepository;
import com.Turbo.Lms.dao.UserRepository;
import com.Turbo.Lms.domain.Course;
import com.Turbo.Lms.domain.Lesson;
import com.Turbo.Lms.dto.CourseDto;
import com.Turbo.Lms.dto.LessonDto;
import com.Turbo.Lms.dto.UserDto;
import com.Turbo.Lms.util.mapper.LessonMapper;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class LessonServiceTest {
    private static final Long INCORRECT_LESSON_ID = 1234567890L;
    private static LessonService lessonService;

    private static List<Lesson> lessonList;

    private static final LessonMapper LESSON_MAPPER = new LessonMapper();
    ;

    private static LessonRepository lessonRepositoryMock;
    private static CourseRepository courseRepositoryMock;

    private static final Long INCORRECT_COURSE_ID = 1234567890L;

    @BeforeAll
    public static void setUp() {
        lessonList = List.of(new Lesson(1L, "TITLE", "TEXT", new Course()),
                new Lesson(2L, "TITLE2", "TEXT2", new Course()));

        lessonRepositoryMock = Mockito.mock(LessonRepository.class);
        courseRepositoryMock = Mockito.mock(CourseRepository.class);

        lessonService = new LessonService(lessonRepositoryMock, courseRepositoryMock, LESSON_MAPPER);
    }

    @Test
    public void findAll_Should_Return_True() {
        List<LessonDto> lessonDtoList = List.of(new LessonDto(), new LessonDto());
        Mockito.when(lessonRepositoryMock.findAllForLessonIdWithoutText(1L)).thenReturn(lessonDtoList);
        assertThat(lessonService.findAllForLessonIdWithoutText(1L)).isEqualTo(lessonDtoList);
    }

    @Test
    public void findAll_Should_Return_False() {
        List<LessonDto> lessonDtoList = LESSON_MAPPER.convertToDtoList(lessonList);
        Mockito.when(lessonRepositoryMock.findAllForLessonIdWithoutText(1L))
                .thenReturn(LESSON_MAPPER.convertToDtoList(lessonList));

        lessonDtoList.remove(1);
        assertThat(lessonService.findAllForLessonIdWithoutText(1L).equals(lessonDtoList)).isFalse();
    }

    @Test
    public void findById_Should_Return_True() {
        Mockito.when(lessonRepositoryMock.findById(1L)).thenReturn(Optional.ofNullable(lessonList.get(0)));
        LessonDto lessonDto = LESSON_MAPPER.toLessonDto(lessonRepositoryMock.findById(1L).get());

        AssertionsForClassTypes.assertThat(lessonService.findById(1L)).isEqualTo(lessonDto);
    }

    @Test
    public void findById_Should_Return_NotFoundException() {
        Mockito.when(lessonRepositoryMock.findById(INCORRECT_LESSON_ID)).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> {
            lessonService.findById(INCORRECT_LESSON_ID);
        });

        Mockito.when(lessonRepositoryMock.findById(null)).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> {
            lessonService.findById(null);
        });
    }

    @Test
    public void delete_Should_Return_True() {
        Mockito.when(lessonRepositoryMock.findById(1L)).thenReturn(Optional.ofNullable(lessonList.get(0)));
        lessonService.delete(1L);
        Mockito.verify(lessonRepositoryMock, Mockito.times(1)).delete(lessonList.get(0));
    }

    @Test
    public void saveLesson_Should_Return_NullPointerException() {
        Mockito.when(lessonRepositoryMock.save(null)).thenThrow(NullPointerException.class);
        Assertions.assertThrows(NullPointerException.class, () -> {
            lessonService.save(null);
        });
    }

    @Test
    public void saveLesson_Should_Return_True() {
        Course course = new Course(3L, "NAME1", "TITLE1");
        Lesson lesson = new Lesson(3L, "TITLE", "ewewewe", course);
        Mockito.when(courseRepositoryMock.findById(3L)).
                thenReturn(Optional.of(course));

        lessonService.save(LESSON_MAPPER.toLessonDto(lesson));

        Mockito.verify(lessonRepositoryMock, Mockito.times(1)).save(lesson);
    }
}
