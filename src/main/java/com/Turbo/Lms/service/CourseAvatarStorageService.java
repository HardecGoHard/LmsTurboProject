package com.Turbo.Lms.service;

import com.Turbo.Lms.dao.CourseAvatarImageRepository;
import com.Turbo.Lms.dao.CourseRepository;
import com.Turbo.Lms.domain.Course;
import com.Turbo.Lms.domain.CourseAvatarImage;
import com.Turbo.Lms.domain.UserAvatarImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

import static java.nio.file.StandardOpenOption.*;

@Service
public class CourseAvatarStorageService {
    private static final Logger logger = LoggerFactory.getLogger(CourseAvatarStorageService.class);

    private final CourseRepository courseRepository;
    private final CourseAvatarImageRepository courseAvatarImageRepository;

    @Value("${course.avatar.storage.path}")
    private String path;

    public CourseAvatarStorageService(CourseRepository courseRepository, CourseAvatarImageRepository courseAvatarImageRepository) {
        this.courseRepository = courseRepository;
        this.courseAvatarImageRepository = courseAvatarImageRepository;
    }

    public void save(Long courseId, String contentType, InputStream is) {
        Optional<CourseAvatarImage> opt = courseAvatarImageRepository.findByCourse_Id(courseId);
        CourseAvatarImage courseAvatarImage;
        String filename;
        if (opt.isEmpty()) {
            filename = UUID.randomUUID().toString();
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(IllegalArgumentException::new);
            courseAvatarImage = new CourseAvatarImage(null, contentType, filename, course);
        } else {
            courseAvatarImage = opt.get();
            filename = courseAvatarImage.getFilename();
            courseAvatarImage.setContentType(contentType);
        }
        courseAvatarImageRepository.save(courseAvatarImage);

        try (OutputStream os = Files.newOutputStream(Path.of(path, filename), CREATE, WRITE, TRUNCATE_EXISTING)) {
            is.transferTo(os);
        } catch (Exception ex) {
            logger.error("Can't write to file {}", filename, ex);
            throw new IllegalStateException(ex);
        }
    }

    public Optional<String> getContentTypeByCourse(Long courseId) {
        return courseAvatarImageRepository.findByCourse_Id(courseId)
                .map(CourseAvatarImage::getContentType);
    }

    public Optional<byte[]> getAvatarImageByCourse(Long courseId) {
        return courseAvatarImageRepository.findByCourse_Id(courseId)
                .map(CourseAvatarImage::getFilename)
                .map(filename -> {
                    try {
                        return Files.readAllBytes(Path.of(path, filename));
                    } catch (IOException ex) {
                        logger.error("Can't read file {}", filename, ex);
                        throw new IllegalStateException(ex);
                    }
                });
    }
}
