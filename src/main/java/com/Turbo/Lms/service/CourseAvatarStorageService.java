package com.Turbo.Lms.service;

import com.Turbo.Lms.dao.CourseAvatarImageRepository;
import com.Turbo.Lms.dao.CourseRepository;
import com.Turbo.Lms.domain.Course;
import com.Turbo.Lms.domain.CourseAvatarImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CourseAvatarStorageService extends AbstractAvatarStorageService<CourseAvatarImage, Course>{
    private static final Logger logger = LoggerFactory.getLogger(CourseAvatarStorageService.class);

    private final CourseRepository courseRepository;
    private final CourseAvatarImageRepository courseAvatarImageRepository;

    @Value("${course.avatar.storage.path}")
    private String path;

    public CourseAvatarStorageService(CourseRepository courseRepository, CourseAvatarImageRepository courseAvatarImageRepository) {
        this.courseRepository = courseRepository;
        this.courseAvatarImageRepository = courseAvatarImageRepository;
    }

    @Override
    protected Optional<CourseAvatarImage> findAvatarImageInRepository(Long id) {
        return courseAvatarImageRepository.findByCourse_Id(id);
    }

    @Override
    protected Course findEntityInRepository(Long id) {
        return courseRepository.findById(id).get();
    }

    @Override
    protected CourseAvatarImage createNewAvatarImage(String contentType, String filename, Course entity) {
        return new CourseAvatarImage(null, contentType,filename,entity);
    }

    @Override
    protected void saveAvatarImageToRepository(CourseAvatarImage avatarImage) {
        courseAvatarImageRepository.save(avatarImage);
    }

    @Override
    protected Optional<String> getContentTypeMap(Long id) {
        return findAvatarImageInRepository(id)
                .map(CourseAvatarImage::getContentType);
    }

    @Override
    protected String getPath() {
        return this.path;
    }

    @Override
    protected String getFilename(CourseAvatarImage avatarImage) {
        return avatarImage.getFilename();
    }

    @Override
    protected void setContentType(CourseAvatarImage avatarImage, String contentType) {
        avatarImage.setContentType(contentType);
    }

    @Override
    protected Optional<byte[]> getAvatarImageMap(Long id) {
        return findAvatarImageInRepository(id)
                .map(CourseAvatarImage::getFilename)
                .map(filename -> readFileImage(filename));
    }
}
