package com.Turbo.Lms.service;

import com.Turbo.Lms.Exceptions.NotFoundException;
import com.Turbo.Lms.dao.CourseRepository;
import com.Turbo.Lms.dao.LessonRepository;
import com.Turbo.Lms.dao.UserRepository;
import com.Turbo.Lms.domain.Course;
import com.Turbo.Lms.dto.CourseDto;
import com.Turbo.Lms.util.mapper.CourseMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class CourseServiceTest {
    private static CourseService courseService;


    private static List<Course> courseList;

    private static final CourseMapper COURSE_MAPPER = new CourseMapper();
    ;

    private static CourseRepository courseRepositoryMock;
    private static UserRepository userRepositoryMock;
    private static LessonRepository lessonRepositoryMock;

    private static final Long INCORRECT_COURSE_ID = 1234567890L;

    @BeforeAll
    public static void setUp() {
        courseList = List.of(
                new Course(1L,
                        "NAME1",
                        "TITLE1"),
                new Course(2L,
                        "NAME2",
                        "TITLE2"
                )
        );
        courseRepositoryMock = Mockito.mock(CourseRepository.class);
        userRepositoryMock = Mockito.mock(UserRepository.class);
        lessonRepositoryMock = Mockito.mock(LessonRepository.class);
        courseService = new CourseService(courseRepositoryMock, COURSE_MAPPER, lessonRepositoryMock, userRepositoryMock);
    }

    @Test
    public void findAll_Should_Return_True() {
        Mockito.when(courseRepositoryMock.findAll()).thenReturn(courseList);

        List<CourseDto> allCourse = COURSE_MAPPER.convertToDtoList(courseRepositoryMock.findAll());

        assertThat(courseService.findAll()).isEqualTo(allCourse);
    }

    @Test
    public void findAll_Should_Return_False() {
        Mockito.when(courseRepositoryMock.findAll()).thenReturn(courseList);

        List<CourseDto> allCourse = COURSE_MAPPER.convertToDtoList(courseRepositoryMock.findAll());
        allCourse.get(1).setId(INCORRECT_COURSE_ID);

        assertThat(courseService.findAll().equals(allCourse)).isFalse();
    }

    @Test
    public void findByTitle_Should_Return_True() {
        Mockito.when(courseRepositoryMock.findByTitleLike("", Pageable.unpaged()))
                .thenReturn(new PageImpl<>(courseList));

        Page<CourseDto> allCourse = COURSE_MAPPER.convertToDtoList(courseRepositoryMock.findByTitleLike("",
                Pageable.unpaged()));

        assertThat(courseService.findByTitleLike("", Pageable.unpaged())).isEqualTo(allCourse);
    }

    @Test
    public void findById_Should_Return_True() {
        Mockito.when(courseRepositoryMock.findById(1L)).thenReturn(Optional.ofNullable(courseList.get(0)));

        CourseDto courseDto = COURSE_MAPPER.toCourseDto(courseRepositoryMock.findById(1L).get());

        assertThat(courseService.findById(1L)).isEqualTo(courseDto);
    }

    @Test
    public void findById_Should_Return_NotFoundException() {
        Mockito.when(courseRepositoryMock.findById(50L)).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> {
            courseService.findById(INCORRECT_COURSE_ID);
        });
    }

    @Test
    public void delete_Should_Return_True() {
        courseService.delete(COURSE_MAPPER.toCourseDto(courseList.get(1)));
        Mockito.verify(courseRepositoryMock, Mockito.times(1)).delete(courseList.get(1));
    }

    @Test
    public void save_Should_Return_True() {
        Course newCourse = new Course(3L, "NAME3", "TITLE3");
        Mockito.when(lessonRepositoryMock.findLessonsByCourse_Id(3L)).thenReturn(Collections.emptyList());
        Mockito.when(userRepositoryMock.getUsersOfCourse(3L)).thenReturn(Collections.emptySet());
        courseService.save(COURSE_MAPPER.toCourseDto(newCourse));
        Mockito.verify(courseRepositoryMock, Mockito.times(1)).save(newCourse);
    }
}
