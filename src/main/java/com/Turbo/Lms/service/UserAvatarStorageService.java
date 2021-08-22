package com.Turbo.Lms.service;

import com.Turbo.Lms.dao.UserAvatarImageRepository;
import com.Turbo.Lms.dao.UserRepository;
import com.Turbo.Lms.domain.UserAvatarImage;
import com.Turbo.Lms.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserAvatarStorageService extends AbstractAvatarStorageService<UserAvatarImage, User> {
    private static final Logger logger = LoggerFactory.getLogger(UserAvatarStorageService.class);

    private final UserAvatarImageRepository userAvatarImageRepository;

    private final UserRepository userRepository;

    @Value("${user.avatar.storage.path}")
    private String path;

    @Autowired
    public UserAvatarStorageService(UserAvatarImageRepository userAvatarImageRepository, UserRepository userRepository) {
        this.userAvatarImageRepository = userAvatarImageRepository;
        this.userRepository = userRepository;
    }

    @Override
    protected Optional<UserAvatarImage> findAvatarImageInRepository(Long id) {
        return userAvatarImageRepository.findAvatarImageByUser_Id(id);
    }

    @Override
    protected User findEntityInRepository(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    protected UserAvatarImage createNewAvatarImage(String contentType, String filename, User entity) {
        return new UserAvatarImage(null, contentType, filename, entity);
    }

    @Override
    protected void saveAvatarImageToRepository(UserAvatarImage avatarImage) {
        userAvatarImageRepository.save(avatarImage);
    }

    @Override
    protected Optional<String> getContentTypeMap(Long id) {
        return findAvatarImageInRepository(id)
                .map(UserAvatarImage::getContentType);
    }

    @Override
    protected String getPath() {
        return this.path;
    }

    @Override
    protected String getFilename(UserAvatarImage avatarImage) {
        return avatarImage.getFilename();
    }

    @Override
    protected void setContentType(UserAvatarImage avatarImage, String contentType) {
        avatarImage.setContentType(contentType);
    }

    @Override
    protected Optional<byte[]> getAvatarImageMap(Long id) {
        return findAvatarImageInRepository(id)
                .map(UserAvatarImage::getFilename)
                .map(filename -> readFileImage(filename));
    }
}


